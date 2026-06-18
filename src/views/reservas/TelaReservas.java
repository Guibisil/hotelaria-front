package views.reservas;

import DTO.QuartoDTO;
import DTO.ReservaDTO;
import components.DsButton;
import components.DsButton.ButtonType;
import components.DsTimelineCell;
import components.DsTimelineCellRenderer;
import components.DsTimelineTable;
import components.DsTitleLabel;
import components.DsDialog;
import controllers.ReservaController;
import controllers.NovaReservaController;
import enums.ReservaAction;
import models.ui.ReservaTimelineTableModel;
import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class TelaReservas extends JPanel {

    private ReservaTimelineTableModel modeloTabela;
    private final DsTimelineTable tabelaReservas;
    private ReservaController controller;
    private NovaReservaController novaReservaController;

    public TelaReservas() {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorPalette.BACKGROUND);

        JPanel jpTopo = new JPanel(new BorderLayout());
        jpTopo.setBackground(ColorPalette.BACKGROUND);
        jpTopo.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        DsTitleLabel jlTitulo = new DsTitleLabel("Mapa de Reservas");

        DsButton btnNovaReserva = new DsButton("Nova Reserva", ButtonType.PRIMARY);
        btnNovaReserva.addActionListener(e -> {
            if (novaReservaController != null) {
                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                JFrame parentFrame = parentWindow instanceof JFrame ? (JFrame) parentWindow : null;
                TelaNovaReserva modal = new TelaNovaReserva(parentFrame, novaReservaController);
                modal.setVisible(true);
                // Refresh after modal closes
                carregarReservas();
            } else {
                DsDialog.showError(this, "Controller de nova reserva não configurado", "Erro");
            }
        });

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
                    controller.realizarAcao(id, ReservaAction.CHECKIN)
                        .thenRun(() -> SwingUtilities.invokeLater(() -> {
                            DsDialog.showSuccess(this, "Ação 'check-in' realizada com sucesso!");
                            btnCheckin.setEnabled(true);
                            carregarReservas();
                        }))
                        .exceptionally(ex -> {
                            SwingUtilities.invokeLater(() -> {
                                btnCheckin.setEnabled(true);
                                String msg = controller.extrairMensagemErro(ex);
                                if (controller.isBusinessRuleException(ex)) {
                                    DsDialog.showWarning(this, msg, "Aviso");
                                } else {
                                    DsDialog.showError(this, msg, "Erro");
                                }
                            });
                            return null;
                        });
                    return;
                }
            }
            DsDialog.showWarning(this, "Selecione uma reserva válida na tabela primeiro", "Aviso");
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
                    controller.realizarAcao(id, ReservaAction.CHECKOUT)
                        .thenRun(() -> SwingUtilities.invokeLater(() -> {
                            DsDialog.showSuccess(this, "Ação 'check-out' realizada com sucesso!");
                            btnCheckout.setEnabled(true);
                            carregarReservas();
                        }))
                        .exceptionally(ex -> {
                            SwingUtilities.invokeLater(() -> {
                                btnCheckout.setEnabled(true);
                                String msg = controller.extrairMensagemErro(ex);
                                if (controller.isBusinessRuleException(ex)) {
                                    DsDialog.showWarning(this, msg, "Aviso");
                                } else {
                                    DsDialog.showError(this, msg, "Erro");
                                }
                            });
                            return null;
                        });
                    return;
                }
            }
            DsDialog.showWarning(this, "Selecione uma reserva válida na tabela primeiro", "Aviso");
        });
    }

    public void setController(ReservaController controller) {
        this.controller = controller;
        carregarReservas();
    }

    public void setNovaReservaController(NovaReservaController novaReservaController) {
        this.novaReservaController = novaReservaController;
    }

    private void carregarReservas() {
        if (controller == null) return;
        controller.carregarReservas().thenAccept(data -> {
            List<LocalDate> datas = new ArrayList<>();
            LocalDate hoje = LocalDate.now();
            for (int i = 0; i <= 60; i++) {
                datas.add(hoje.plusDays(i));
            }
            SwingUtilities.invokeLater(() -> {
                atualizarTabelaTimeline(data.quartos, datas, data.reservas);
            });
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                ex.printStackTrace();
                DsDialog.showError(this, "Erro ao carregar mapa de reservas. Verifique se o backend está rodando.", "Erro de Conexão");
            });
            return null;
        });
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
}
