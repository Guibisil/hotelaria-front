package views.hospedes;

import DTO.HospedesDTO;
import components.DsButton;
import components.DsTable;
import components.DsTitleLabel;
import controllers.HospedeController;
import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaHospedes extends JPanel {

    private DefaultTableModel modeloTabela;
    private DsTable tabelaHospedes;
    private HospedeController controller;

    public TelaHospedes() {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorPalette.BACKGROUND);

        JPanel jpTopo = new JPanel(new BorderLayout());
        jpTopo.setBackground(ColorPalette.BACKGROUND);
        jpTopo.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        DsTitleLabel jlTitulo = new DsTitleLabel("Gerenciamento de Hóspedes");

        DsButton btnNovoHospede = new DsButton("Novo Hóspede", DsButton.ButtonType.PRIMARY);

        btnNovoHospede.addActionListener(e -> {
            TelaNovoHospede telaNovo = new TelaNovoHospede(controller);
            telaNovo.setVisible(true);
        });

        jpTopo.add(jlTitulo, BorderLayout.WEST);
        jpTopo.add(btnNovoHospede, BorderLayout.EAST);

        String[] colunas = {"Nome", "CPF", "Email", "Data de Nascimento"};

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaHospedes = new DsTable(modeloTabela);

        JScrollPane jpTabela = new JScrollPane(tabelaHospedes);
        jpTabela.setBorder(BorderFactory.createEmptyBorder(0, Spacing.MD, 0, Spacing.MD));
        jpTabela.getViewport().setBackground(ColorPalette.BACKGROUND);

        JPanel jpAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, Spacing.MD, Spacing.MD));
        jpAcoes.setBackground(ColorPalette.BACKGROUND);

        this.add(jpTopo, BorderLayout.NORTH);
        this.add(jpAcoes, BorderLayout.SOUTH);
        this.add(jpTabela, BorderLayout.CENTER);
    }

    public void setController(HospedeController controller) {
        this.controller = controller;
        this.controller.carregarHospedes();
    }

    public void atualizarTabela(List<HospedesDTO> listaHospedes) {
        modeloTabela.setRowCount(0);
        for (HospedesDTO r : listaHospedes) {
            modeloTabela.addRow(new Object[]{
                    r.getName(),
                    r.getCpf(),
                    r.getEmail(),
                    r.getBirth_date(),
            });
        }
    }

    public void mostrarMensagemErro(String mensagem, String titulo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
    }
}
