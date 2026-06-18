package main;

import DTO.QuartoDTO;
import DTO.ReservaDTO;
import components.DsButton;
import components.DsButton.ButtonType;
import components.DsTimelineCell;
import components.DsTimelineCellRenderer;
import components.DsTimelineTable;
import components.DsTitleLabel;
import controllers.ReservaController;
import enums.ReservaAction;
import models.ui.ReservaTimelineTableModel;
import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TelaReservas extends JPanel {

    private ReservaTimelineTableModel modeloTabela;
    private final DsTimelineTable tabelaReservas;
    private final ReservaController controller;

    public TelaReservas() {
        this.controller = new ReservaController(this);
        this.setLayout(new BorderLayout());
        this.setBackground(ColorPalette.BACKGROUND);

        JPanel jpTopo = new JPanel(new BorderLayout());
        jpTopo.setBackground(ColorPalette.BACKGROUND);
        jpTopo.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        DsTitleLabel jlTitulo = new DsTitleLabel("Mapa de Reservas");

        DsButton btnNovaReserva = new DsButton("Nova Reserva", ButtonType.PRIMARY);

        jpTopo.add(jlTitulo, BorderLayout.WEST);
        jpTopo.add(btnNovaReserva, BorderLayout.EAST);

        tabelaReservas = new DsTimelineTable();
        tabelaReservas.setDefaultRenderer(Object.class, new DsTimelineCellRenderer());
        
        tabelaReservas.setModel(new ReservaTimelineTableModel(null, null, null));

        JScrollPane jpTabela = new JScrollPane(tabelaReservas);
        jpTabela.setBorder(BorderFactory.createEmptyBorder(0, Spacing.MD, 0, Spacing.MD));
        jpTabela.getViewport().setBackground(ColorPalette.BACKGROUND);

        JPanel jpAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, Spacing.MD, Spacing.MD));
        jpAcoes.setBackground(ColorPalette.BACKGROUND);

        DsButton btnCheckin = new DsButton("Fazer check-in", ButtonType.SECONDARY);
        DsButton btnCheckout = new DsButton("Fazer checkout", ButtonType.DANGER);

        jpAcoes.add(btnCheckin);
        jpAcoes.add(btnCheckout);

        this.add(jpTopo, BorderLayout.NORTH);
        this.add(jpAcoes, BorderLayout.SOUTH);
        this.add(jpTabela, BorderLayout.CENTER);

        btnCheckin.addActionListener(e -> {
            int linha = tabelaReservas.getSelectedRow();
            int coluna = tabelaReservas.getSelectedColumn();
            if (linha != -1 && coluna > 0) {
                Object val = tabelaReservas.getValueAt(linha, coluna);
                if (val instanceof DsTimelineCell) {
                    DsTimelineCell cell = (DsTimelineCell) val;
                    String id = cell.getId();
                    btnCheckin.setEnabled(false);
                    controller.realizarAcao(id, ReservaAction.CHECKIN, 
                        () -> mostrarMensagemSucesso("Ação 'check-in' realizada com sucesso!"),
                        () -> btnCheckin.setEnabled(true)
                    );
                    return;
                }
            }
            mostrarAviso("Selecione uma reserva válida na tabela primeiro", "Aviso");
        });

        btnCheckout.addActionListener(e -> {
            int linha = tabelaReservas.getSelectedRow();
            int coluna = tabelaReservas.getSelectedColumn();
            if (linha != -1 && coluna > 0) {
                Object val = tabelaReservas.getValueAt(linha, coluna);
                if (val instanceof DsTimelineCell) {
                    DsTimelineCell cell = (DsTimelineCell) val;
                    String id = cell.getId();
                    btnCheckout.setEnabled(false);
                    controller.realizarAcao(id, ReservaAction.CHECKOUT,
                        () -> mostrarMensagemSucesso("Ação 'check-out' realizada com sucesso!"),
                        () -> btnCheckout.setEnabled(true)
                    );
                    return;
                }
            }
            mostrarAviso("Selecione uma reserva válida na tabela primeiro", "Aviso");
        });

        controller.carregarReservas();
    }

    public void atualizarTabelaTimeline(List<QuartoDTO> quartos, List<LocalDate> datas, List<ReservaDTO> reservas) {
        modeloTabela = new ReservaTimelineTableModel(quartos, datas, reservas);
        tabelaReservas.setModel(modeloTabela);
        
        tabelaReservas.setDefaultRenderer(Object.class, new DsTimelineCellRenderer());
        
        if (tabelaReservas.getColumnModel().getColumnCount() > 0) {
            tabelaReservas.getColumnModel().getColumn(0).setPreferredWidth(100);
            tabelaReservas.getColumnModel().getColumn(0).setMinWidth(100);
        }
    }

    public void mostrarMensagemSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    public void mostrarMensagemErro(String mensagem, String titulo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarAviso(String mensagem, String titulo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.WARNING_MESSAGE);
    }
}
