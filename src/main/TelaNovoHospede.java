package main;

import DTO.HospedesDTO;
import components.DsButton;
import components.DsFormattedTextField;
import components.DsLabel;
import components.DsTextField;
import components.DsTitleLabel;
import controllers.HospedeController;
import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaNovoHospede extends JDialog {

    private final HospedeController controller;
    private DsTextField txtNome;
    private DsTextField txtEmail;
    private DsFormattedTextField txtCpf;
    private DsFormattedTextField txtDataNasc;

    public TelaNovoHospede(HospedeController controller) {
        this.controller = controller;
        setTitle("Novo Hóspede");
        setModal(true);
        setSize(350, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ColorPalette.BACKGROUND);

        initComponents();
    }

    private void initComponents() {
        JPanel jpDados = new JPanel();
        jpDados.setOpaque(false);
        jpDados.setLayout(new GridLayout(11, 1, 1, Spacing.SM));
        jpDados.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

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

        jpDados.add(jlTitulo);
        jpDados.add(jlNome);
        jpDados.add(txtNome);
        jpDados.add(jlCpf);
        jpDados.add(txtCpf);
        jpDados.add(jlEmail);
        jpDados.add(txtEmail);
        jpDados.add(jlDataNasc);
        jpDados.add(txtDataNasc);
        jpDados.add(new DsLabel("")); // spacer
        jpDados.add(btnCadastrar);

        add(jpDados);
    }

    private void onCadastrarClicked(ActionEvent e) {
        if (validar()) {
            String dataConv = txtDataNasc.getText().replaceAll("(\\d{2})/(\\d{2})/(\\d{4})", "$3-$2-$1");

            HospedesDTO hospede = new HospedesDTO();
            hospede.setName(txtNome.getText());
            hospede.setCpf(txtCpf.getText());
            hospede.setEmail(txtEmail.getText());
            hospede.setBirth_date(dataConv);

            controller.cadastrarHospede(hospede, () -> {
                JOptionPane.showMessageDialog(this, "Hóspede cadastrado com sucesso!");
                this.dispose();
            });
        }
    }

    private boolean validar() {
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        if (txtNome.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtCpf.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos");
            return false;
        } else if (!txtEmail.getText().matches(regexEmail)) {
            JOptionPane.showMessageDialog(this, "Email inválido.");
            return false;
        } else if (txtCpf.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "Preencha o CPF completo.");
            return false;
        } else if (txtDataNasc.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "Preencha a data de nascimento completa.");
            return false;
        }
        return true;
    }
}
