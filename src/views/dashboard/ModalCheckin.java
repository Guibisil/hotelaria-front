package views.dashboard;

import DTO.ReservaDTO;
import components.*;
import controllers.DashboardController;
import theme.DesignTokens.Spacing;
import theme.DesignTokens.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ModalCheckin extends DsModal {

    private final DashboardController controller;
    private final ReservaDTO reserva;
    private final Runnable onSuccess;

    public ModalCheckin(DashboardController controller, ReservaDTO reserva, Runnable onSuccess) {
        super("Realizar Check-in", 450, 400);
        this.controller = controller;
        this.reserva = reserva;
        this.onSuccess = onSuccess;

        initComponents();
    }

    private void initComponents() {
        setModalLayout(new BorderLayout(Spacing.MD, Spacing.MD));

        JPanel pnlContent = new JPanel(new GridLayout(6, 2, Spacing.MD, Spacing.MD));
        pnlContent.setBackground(ColorPalette.BACKGROUND);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        pnlContent.add(new DsLabel("Hóspede:"));
        pnlContent.add(new DsLabel(reserva.getGuestName() != null ? reserva.getGuestName() : "Desconhecido"));

        pnlContent.add(new DsLabel("Quarto:"));
        pnlContent.add(new DsLabel(reserva.getRoomNumber() != null ? reserva.getRoomNumber() : "N/A"));

        pnlContent.add(new DsLabel("Saída Prevista:"));
        pnlContent.add(new DsLabel(reserva.getCheckoutDate()));

        pnlContent.add(new DsLabel("Valor da Diária:"));
        double dailyRate = reserva.getRoomDailyRate() != null ? reserva.getRoomDailyRate() : 0.0;
        pnlContent.add(new DsLabel(String.format("R$ %.2f", dailyRate)));

        long dias = 0;
        try {
            LocalDate in = LocalDate.parse(reserva.getCheckinDate());
            LocalDate out = LocalDate.parse(reserva.getCheckoutDate());
            dias = ChronoUnit.DAYS.between(in, out);
        } catch (Exception e) {}

        pnlContent.add(new DsLabel("Quantidade de dias:"));
        pnlContent.add(new DsLabel(String.valueOf(dias)));

        pnlContent.add(new DsTitleLabel("Total a Pagar:"));
        double total = reserva.getTotalAmount() != null ? reserva.getTotalAmount() : (dailyRate * dias);
        DsTitleLabel lblTotal = new DsTitleLabel(String.format("R$ %.2f", total));
        lblTotal.setForeground(ColorPalette.PRIMARY);
        pnlContent.add(lblTotal);

        addComponent(pnlContent, BorderLayout.CENTER);

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlAcoes.setBackground(ColorPalette.BACKGROUND);
        DsButton btnConfirmar = new DsButton("Confirmar pagamento e realizar Check-in", DsButton.ButtonType.PRIMARY);
        btnConfirmar.addActionListener(e -> realizarCheckin());
        pnlAcoes.add(btnConfirmar);

        addComponent(pnlAcoes, BorderLayout.SOUTH);
    }

    private void realizarCheckin() {
        controller.realizarCheckin(reserva.getId()).thenRun(() -> SwingUtilities.invokeLater(() -> {
            DsDialog.showSuccess(this, "Check-in realizado com sucesso!");
            if (onSuccess != null) {
                onSuccess.run();
            }
            dispose();
        })).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                String msg = controller.extrairMensagemErro(ex);
                DsDialog.showError(this, "Erro ao realizar check-in: " + msg, "Erro");
            });
            return null;
        });
    }
}
