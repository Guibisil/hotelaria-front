package views.dashboard;

import DTO.ReservaDTO;
import components.DsButton;
import components.DsLabel;
import components.DsTitleLabel;
import components.DsDialog;
import controllers.DashboardController;
import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardView extends JPanel {

    private final DashboardController controller;
    private JPanel jpCheckInList;
    private JPanel jpCheckOutList;

    public DashboardView(DashboardController controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.setBackground(ColorPalette.BACKGROUND);
        renderDashboard();
        carregarDados();
    }

    private void renderDashboard() {
        JPanel jpTopo = new JPanel(new BorderLayout());
        jpTopo.setBackground(ColorPalette.BACKGROUND);
        jpTopo.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));
        DsTitleLabel titulo = new DsTitleLabel("Página Inicial");
        jpTopo.add(titulo, BorderLayout.WEST);

        JPanel jpMain = new JPanel(new GridLayout(2, 1, Spacing.MD, Spacing.MD));
        jpMain.setBackground(ColorPalette.BACKGROUND);
        jpMain.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        // Painel Check-ins
        JPanel jpCheckIn = new JPanel(new BorderLayout(Spacing.SM, Spacing.SM));
        jpCheckIn.setBackground(ColorPalette.BACKGROUND);
        DsTitleLabel lblCheckin = new DsTitleLabel("Check-ins do dia");
        jpCheckIn.add(lblCheckin, BorderLayout.NORTH);

        jpCheckInList = new JPanel(new FlowLayout(FlowLayout.LEFT, Spacing.MD, Spacing.MD));
        jpCheckInList.setBackground(ColorPalette.BACKGROUND);
        JScrollPane scrollCheckin = new JScrollPane(jpCheckInList);
        scrollCheckin.setBorder(BorderFactory.createEmptyBorder());
        jpCheckIn.add(scrollCheckin, BorderLayout.CENTER);

        // Painel Check-outs
        JPanel jpCheckOut = new JPanel(new BorderLayout(Spacing.SM, Spacing.SM));
        jpCheckOut.setBackground(ColorPalette.BACKGROUND);
        DsTitleLabel lblCheckout = new DsTitleLabel("Check-outs do dia");
        jpCheckOut.add(lblCheckout, BorderLayout.NORTH);

        jpCheckOutList = new JPanel(new FlowLayout(FlowLayout.LEFT, Spacing.MD, Spacing.MD));
        jpCheckOutList.setBackground(ColorPalette.BACKGROUND);
        JScrollPane scrollCheckout = new JScrollPane(jpCheckOutList);
        scrollCheckout.setBorder(BorderFactory.createEmptyBorder());
        jpCheckOut.add(scrollCheckout, BorderLayout.CENTER);

        jpMain.add(jpCheckIn);
        jpMain.add(jpCheckOut);

        this.add(jpTopo, BorderLayout.NORTH);
        this.add(jpMain, BorderLayout.CENTER);
    }

    private void carregarDados() {
        controller.carregarDadosDashboard().thenAccept(data -> SwingUtilities.invokeLater(() -> {
            List<ReservaDTO> checkins = controller.getCheckinsDoDia(data);
            List<ReservaDTO> checkouts = controller.getCheckoutsDoDia(data);

            jpCheckInList.removeAll();
            if (checkins.isEmpty()) {
                jpCheckInList.add(new DsLabel("Nenhum check-in programado para hoje."));
            } else {
                for (ReservaDTO r : checkins) {
                    jpCheckInList.add(criarCard(r, true));
                }
            }

            jpCheckOutList.removeAll();
            if (checkouts.isEmpty()) {
                jpCheckOutList.add(new DsLabel("Nenhum check-out programado para hoje."));
            } else {
                for (ReservaDTO r : checkouts) {
                    jpCheckOutList.add(criarCard(r, false));
                }
            }

            jpCheckInList.revalidate();
            jpCheckInList.repaint();
            jpCheckOutList.revalidate();
            jpCheckOutList.repaint();
        })).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                ex.printStackTrace();
                DsDialog.showError(this, "Erro ao carregar dados do dashboard.", "Erro");
            });
            return null;
        });
    }

    private JPanel criarCard(ReservaDTO reserva, boolean isCheckin) {
        JPanel card = new JPanel(new BorderLayout(Spacing.SM, Spacing.SM));
        card.setBackground(ColorPalette.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_VARIANT),
                BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD)
        ));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(ColorPalette.SURFACE);
        String nome = reserva.getGuestName() != null ? reserva.getGuestName() : "Hóspede Desconhecido";
        String quarto = reserva.getRoomNumber() != null ? reserva.getRoomNumber() : "N/A";
        
        DsLabel lblNome = new DsLabel(nome);
        lblNome.setFont(theme.DesignTokens.Typography.TITLE_FONT.deriveFont(16f));
        info.add(lblNome);
        
        DsLabel lblQuarto = new DsLabel("Quarto: " + quarto);
        lblQuarto.setForeground(ColorPalette.TEXT_SECONDARY);
        info.add(lblQuarto);

        card.add(info, BorderLayout.CENTER);

        String btnLabel = isCheckin ? "Realizar Check-in" : "Realizar Check-out";
        DsButton btnAcao = new DsButton(btnLabel, isCheckin ? DsButton.ButtonType.PRIMARY : DsButton.ButtonType.DANGER);

        btnAcao.addActionListener(e -> {
            if (isCheckin) {
                new ModalCheckin(controller, reserva, this::carregarDados).setVisible(true);
            } else {
                new ModalCheckout(controller, reserva, this::carregarDados).setVisible(true);
            }
        });

        card.add(btnAcao, BorderLayout.SOUTH);
        card.setPreferredSize(new Dimension(200, 120));

        return card;
    }
}
