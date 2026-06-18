package main;

import DTO.ReservaDTO;
import components.DsButton;
import components.DsButton.ButtonType;
import components.DsTitleLabel;
import services.ReservaService;
import services.ReservaService.BusinessRuleException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.concurrent.CompletionException;

public class TelaReservas extends JPanel {

    private final DefaultTableModel modeloTabela;
    private final JTable tabela_reservas;
    private final ReservaService reservaService;

    public TelaReservas() {
        this.reservaService = new ReservaService();
        this.setLayout(new BorderLayout());

        JPanel jp_topo = new JPanel(new BorderLayout());
        jp_topo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DsTitleLabel jl_titulo = new DsTitleLabel("Gerenciamento de Reservas");

        DsButton btn_nova_reserva = new DsButton("Nova Reserva", ButtonType.PRIMARY);

        jp_topo.add(jl_titulo, BorderLayout.WEST);
        jp_topo.add(btn_nova_reserva, BorderLayout.EAST);

        String[] colunas = {"ID", "Quarto", "Entrada", "Saída", "Status"};

        modeloTabela = new DefaultTableModel(colunas, 0);
        tabela_reservas = new JTable(modeloTabela);
        tabela_reservas.setRowHeight(25);

        JScrollPane jp_tabela = new JScrollPane(tabela_reservas);
        jp_tabela.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel jp_acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        DsButton btn_checkin = new DsButton("Fazer check-in", ButtonType.SECONDARY);
        DsButton btn_checkout = new DsButton("Fazer checkout", ButtonType.DANGER);

        jp_acoes.add(btn_checkin);
        jp_acoes.add(btn_checkout);

        this.add(jp_topo, BorderLayout.NORTH);
        this.add(jp_acoes, BorderLayout.SOUTH);
        this.add(jp_tabela, BorderLayout.CENTER);

        btn_checkin.addActionListener(e -> {
            int linha = tabela_reservas.getSelectedRow();
            if (linha != -1) {
                String id = tabela_reservas.getValueAt(linha, 0).toString();
                btn_checkin.setEnabled(false);
                reservaService.realizarAcao(id, "checkin")
                        .thenRun(() -> SwingUtilities.invokeLater(() -> {
                            btn_checkin.setEnabled(true);
                            JOptionPane.showMessageDialog(this, "Ação 'check-in' realizada com sucesso!");
                            carregarReservasDaAPI();
                        }))
                        .exceptionally(ex -> {
                            SwingUtilities.invokeLater(() -> {
                                btn_checkin.setEnabled(true);
                                tratarErroAPI(ex, "realizar check-in");
                            });
                            return null;
                        });
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma reserva na tabela primeiro");
            }
        });

        btn_checkout.addActionListener(e -> {
            int linha = tabela_reservas.getSelectedRow();
            if (linha != -1) { // BUG FIX: corrigido de != 1 para != -1
                String id = tabela_reservas.getValueAt(linha, 0).toString();
                btn_checkout.setEnabled(false);
                reservaService.realizarAcao(id, "checkout")
                        .thenRun(() -> SwingUtilities.invokeLater(() -> {
                            btn_checkout.setEnabled(true);
                            JOptionPane.showMessageDialog(this, "Ação 'check-out' realizada com sucesso!");
                            carregarReservasDaAPI();
                        }))
                        .exceptionally(ex -> {
                            SwingUtilities.invokeLater(() -> {
                                btn_checkout.setEnabled(true);
                                tratarErroAPI(ex, "realizar check-out");
                            });
                            return null;
                        });
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma reserva na tabela primeiro");
            }
        });

        carregarReservasDaAPI();
    }

    private void carregarReservasDaAPI() {
        reservaService.buscarReservas()
                .thenAccept(listaReservas -> SwingUtilities.invokeLater(() -> {
                    modeloTabela.setRowCount(0);
                    for (ReservaDTO r : listaReservas) {
                        modeloTabela.addRow(new Object[]{
                                r.getId(),
                                r.getRoom_id(),
                                r.getCheckin_date(),
                                r.getCheckout_date(),
                                r.getStatus(), // BUG FIX: corrigido de getStatuts()
                        });
                    }
                }))
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Erro ao carregar reservas. Verifique se o backend está rodando.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                    });
                    return null;
                });
    }

    private void tratarErroAPI(Throwable ex, String operacao) {
        Throwable causa = ex;
        if (causa instanceof CompletionException && causa.getCause() != null) {
            causa = causa.getCause();
        }

        if (causa instanceof BusinessRuleException) {
            JOptionPane.showMessageDialog(this, "Bloqueado pela Regra de Negócio:\n" + causa.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            causa.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao " + operacao + ": " + causa.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
