package main;

import DTO.ReservaDTO;
import components.DsButton;
import components.DsButton.ButtonType;
import components.DsTitleLabel;
import controllers.ReservaController;
import enums.ReservaAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaReservas extends JPanel {

    private final DefaultTableModel modeloTabela;
    private final JTable tabelaReservas;
    private final ReservaController controller;

    public TelaReservas() {
        this.controller = new ReservaController(this);
        this.setLayout(new BorderLayout());

        JPanel jpTopo = new JPanel(new BorderLayout());
        jpTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DsTitleLabel jlTitulo = new DsTitleLabel("Gerenciamento de Reservas");

        DsButton btnNovaReserva = new DsButton("Nova Reserva", ButtonType.PRIMARY);

        jpTopo.add(jlTitulo, BorderLayout.WEST);
        jpTopo.add(btnNovaReserva, BorderLayout.EAST);

        String[] colunas = {"ID", "Quarto", "Entrada", "Saída", "Status"};

        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaReservas = new JTable(modeloTabela);
        tabelaReservas.setRowHeight(25);

        JScrollPane jpTabela = new JScrollPane(tabelaReservas);
        jpTabela.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel jpAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        DsButton btnCheckin = new DsButton("Fazer check-in", ButtonType.SECONDARY);
        DsButton btnCheckout = new DsButton("Fazer checkout", ButtonType.DANGER);

        jpAcoes.add(btnCheckin);
        jpAcoes.add(btnCheckout);

        this.add(jpTopo, BorderLayout.NORTH);
        this.add(jpAcoes, BorderLayout.SOUTH);
        this.add(jpTabela, BorderLayout.CENTER);

        btnCheckin.addActionListener(e -> {
            int linha = tabelaReservas.getSelectedRow();
            if (linha != -1) {
                String id = tabelaReservas.getValueAt(linha, 0).toString();
                btnCheckin.setEnabled(false);
                
                controller.realizarAcao(id, ReservaAction.CHECKIN, 
                    () -> mostrarMensagemSucesso("Ação 'check-in' realizada com sucesso!"),
                    () -> btnCheckin.setEnabled(true)
                );
            } else {
                mostrarAviso("Selecione uma reserva na tabela primeiro", "Aviso");
            }
        });

        btnCheckout.addActionListener(e -> {
            int linha = tabelaReservas.getSelectedRow();
            if (linha != -1) {
                String id = tabelaReservas.getValueAt(linha, 0).toString();
                btnCheckout.setEnabled(false);
                
                controller.realizarAcao(id, ReservaAction.CHECKOUT,
                    () -> mostrarMensagemSucesso("Ação 'check-out' realizada com sucesso!"),
                    () -> btnCheckout.setEnabled(true)
                );
            } else {
                mostrarAviso("Selecione uma reserva na tabela primeiro", "Aviso");
            }
        });

        controller.carregarReservas();
    }

    public void atualizarTabela(List<ReservaDTO> listaReservas) {
        modeloTabela.setRowCount(0);
        for (ReservaDTO r : listaReservas) {
            modeloTabela.addRow(new Object[]{
                    r.getId(),
                    r.getRoomId(),
                    r.getCheckinDate(),
                    r.getCheckoutDate(),
                    r.getStatus(),
            });
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
