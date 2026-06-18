package views.dashboard;

import DTO.ReservaDTO;
import components.*;
import controllers.DashboardController;
import theme.DesignTokens.Spacing;
import theme.DesignTokens.ColorPalette;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ModalCheckout extends DsModal {

    private final DashboardController controller;
    private final ReservaDTO reserva;
    private final Runnable onSuccess;

    private DsTextField txtFrigobar;
    private DsTextField txtRestaurante;
    private DsTitleLabel lblTotalFinal;
    private double baseTotal;

    public ModalCheckout(DashboardController controller, ReservaDTO reserva, Runnable onSuccess) {
        super("Realizar Check-out", 450, 480);
        this.controller = controller;
        this.reserva = reserva;
        this.onSuccess = onSuccess;

        this.baseTotal = reserva.getTotalAmount() != null ? reserva.getTotalAmount() : 0.0;
        if (baseTotal == 0.0 && reserva.getRoomDailyRate() != null) {
             try {
                 java.time.LocalDate in = java.time.LocalDate.parse(reserva.getCheckinDate());
                 java.time.LocalDate out = java.time.LocalDate.parse(reserva.getCheckoutDate());
                 long dias = java.time.temporal.ChronoUnit.DAYS.between(in, out);
                 baseTotal = dias * reserva.getRoomDailyRate();
             } catch (Exception e) {}
        }

        initComponents();
    }

    private void initComponents() {
        setModalLayout(new BorderLayout(Spacing.MD, Spacing.MD));

        JPanel pnlContent = new JPanel(new GridLayout(8, 2, Spacing.MD, Spacing.MD));
        pnlContent.setBackground(ColorPalette.BACKGROUND);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        pnlContent.add(new DsLabel("Hóspede:"));
        pnlContent.add(new DsLabel(reserva.getGuestName() != null ? reserva.getGuestName() : "Desconhecido"));

        pnlContent.add(new DsLabel("Quarto:"));
        pnlContent.add(new DsLabel(reserva.getRoomNumber() != null ? reserva.getRoomNumber() : "N/A"));

        pnlContent.add(new DsLabel("Data de Saída:"));
        pnlContent.add(new DsLabel(reserva.getCheckoutDate()));

        pnlContent.add(new DsLabel("Status das Diárias:"));
        DsLabel lblStatusPago = new DsLabel("Pago");
        lblStatusPago.setForeground(ColorPalette.SUCCESS);
        pnlContent.add(lblStatusPago);

        pnlContent.add(new DsLabel("Total Reserva:"));
        pnlContent.add(new DsLabel(String.format("R$ %.2f", baseTotal)));

        pnlContent.add(new DsLabel("Consumo Extra (Frigobar) R$:"));
        txtFrigobar = new DsTextField();
        txtFrigobar.setText("0.00");
        pnlContent.add(txtFrigobar);

        pnlContent.add(new DsLabel("Consumo Extra (Restaurante) R$:"));
        txtRestaurante = new DsTextField();
        txtRestaurante.setText("0.00");
        pnlContent.add(txtRestaurante);

        pnlContent.add(new DsTitleLabel("Total a Pagar Final:"));
        lblTotalFinal = new DsTitleLabel(String.format("R$ %.2f", baseTotal));
        lblTotalFinal.setForeground(ColorPalette.PRIMARY);
        pnlContent.add(lblTotalFinal);

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { atualizarTotal(); }
            public void removeUpdate(DocumentEvent e) { atualizarTotal(); }
            public void changedUpdate(DocumentEvent e) { atualizarTotal(); }
        };
        txtFrigobar.getDocument().addDocumentListener(dl);
        txtRestaurante.getDocument().addDocumentListener(dl);

        addComponent(pnlContent, BorderLayout.CENTER);

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlAcoes.setBackground(ColorPalette.BACKGROUND);
        DsButton btnConfirmar = new DsButton("Confirmar pagamento e realizar Check-out", DsButton.ButtonType.PRIMARY);
        btnConfirmar.addActionListener(e -> realizarCheckout());
        pnlAcoes.add(btnConfirmar);

        addComponent(pnlAcoes, BorderLayout.SOUTH);
    }

    private void atualizarTotal() {
        double extraFrigobar = 0.0;
        double extraRestaurante = 0.0;
        try {
            if (!txtFrigobar.getText().isBlank()) extraFrigobar = Double.parseDouble(txtFrigobar.getText().replace(",", "."));
            if (!txtRestaurante.getText().isBlank()) extraRestaurante = Double.parseDouble(txtRestaurante.getText().replace(",", "."));
        } catch (NumberFormatException ignored) {}

        double total = baseTotal + extraFrigobar + extraRestaurante;
        lblTotalFinal.setText(String.format("R$ %.2f", total));
    }

    private void realizarCheckout() {
        controller.realizarCheckout(reserva.getId()).thenRun(() -> SwingUtilities.invokeLater(() -> {
            DsDialog.showSuccess(this, "Check-out realizado com sucesso!");
            if (onSuccess != null) {
                onSuccess.run();
            }
            dispose();
        })).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                String msg = controller.extrairMensagemErro(ex);
                DsDialog.showError(this, "Erro ao realizar check-out: " + msg, "Erro");
            });
            return null;
        });
    }
}
