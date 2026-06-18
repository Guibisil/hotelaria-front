package views.hospedes;

import DTO.HospedeDTO;
import components.DsButton;
import components.DsFormattedTextField;
import components.DsLabel;
import components.DsTextField;
import components.DsTitleLabel;
import components.DsModal;
import components.DsDialog;
import controllers.HospedeController;
import theme.DesignTokens.Spacing;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaNovoHospede extends DsModal {

    private final HospedeController controller;
    private final TelaHospedes telaHospedesPai;
    private DsTextField txtNome;
    private DsTextField txtEmail;
    private DsFormattedTextField txtCpf;
    private DsFormattedTextField txtDataNasc;

    public TelaNovoHospede(HospedeController controller, TelaHospedes telaHospedesPai) {
        super("Novo Hóspede", 350, 500);
        this.controller = controller;
        this.telaHospedesPai = telaHospedesPai;

        initComponents();
    }

    private void initComponents() {
        setModalLayout(new GridLayout(11, 1, 1, Spacing.SM));

        DsTitleLabel jlTitulo = new DsTitleLabel("Cadastro de hóspede");
        DsLabel jlNome = new DsLabel("Nome");
        DsLabel jlCpf = new DsLabel("CPF");
        DsLabel jlEmail = new DsLabel("Email");
        DsLabel jlDataNasc = new DsLabel("Data de nascimento");

        txtNome = new DsTextField();
        txtEmail = new DsTextField();

        try {
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');
            txtCpf = new DsFormattedTextField(mascaraCpf);
            txtCpf.setColumns(11);

            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtDataNasc = new DsFormattedTextField(mascaraData);
            txtDataNasc.setColumns(8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DsButton btnCadastrar = new DsButton("Cadastrar hóspede", DsButton.ButtonType.PRIMARY);

        btnCadastrar.addActionListener(this::onCadastrarClicked);

        addComponent(jlTitulo);
        addComponent(jlNome);
        addComponent(txtNome);
        addComponent(jlCpf);
        addComponent(txtCpf);
        addComponent(jlEmail);
        addComponent(txtEmail);
        addComponent(jlDataNasc);
        addComponent(txtDataNasc);
        addComponent(new DsLabel("")); // spacer
        addComponent(btnCadastrar);
    }

    private void onCadastrarClicked(ActionEvent e) {
        if (validar()) {
            String dataConv = txtDataNasc.getText().replaceAll("(\\d{2})/(\\d{2})/(\\d{4})", "$3-$2-$1");

            HospedeDTO hospede = new HospedeDTO();
            hospede.setName(txtNome.getText());
            hospede.setCpf(txtCpf.getText());
            hospede.setEmail(txtEmail.getText());
            hospede.setBirthDate(dataConv);

            controller.cadastrarHospede(hospede)
                .thenRun(() -> SwingUtilities.invokeLater(() -> {
                    DsDialog.showSuccess(this, "Hóspede cadastrado com sucesso!");
                    if (telaHospedesPai != null) {
                        telaHospedesPai.carregarHospedes();
                    }
                    this.dispose();
                }))
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        String msg = controller.extrairMensagemErro(ex);
                        if (controller.isBusinessRuleException(ex)) {
                            DsDialog.showWarning(this, msg, "Aviso");
                        } else {
                            DsDialog.showError(this, msg, "Erro");
                        }
                    });
                    return null;
                });
        }
    }

    private boolean validar() {
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        if (txtNome.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtCpf.getText().trim().isEmpty()) {
            DsDialog.showWarning(this, "Preencha todos os campos", "Aviso");
            return false;
        } else if (!txtEmail.getText().matches(regexEmail)) {
            DsDialog.showWarning(this, "Email inválido.", "Aviso");
            return false;
        } else if (txtCpf.getText().contains("_")) {
            DsDialog.showWarning(this, "Preencha o CPF completo.", "Aviso");
            return false;
        } else if (txtDataNasc.getText().contains("_")) {
            DsDialog.showWarning(this, "Preencha a data de nascimento completa.", "Aviso");
            return false;
        }
        return true;
    }
}
