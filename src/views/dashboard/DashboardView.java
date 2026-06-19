package views.dashboard;

import DTO.ReservaDTO;
import components.DsButton;
import components.DsLabel;
import components.DsTitleLabel;
import components.DsDialog;
import components.DsCard;
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
        this.setLayout(new BorderLayout(Spacing.MD, Spacing.MD));
        this.setBackground(ColorPalette.BACKGROUND);
        this.setBorder(BorderFactory.createEmptyBorder(Spacing.LG, Spacing.LG, Spacing.LG, Spacing.LG));
        renderDashboard();
        carregarDados();
    }

    private void renderDashboard() {
        JPanel jpTopo = new JPanel(new BorderLayout());
        jpTopo.setBackground(ColorPalette.BACKGROUND);
        DsTitleLabel titulo = new DsTitleLabel("Página Inicial");
        jpTopo.add(titulo, BorderLayout.WEST);

        JPanel jpMain = new JPanel(new GridLayout(1, 2, Spacing.LG, Spacing.LG));
        jpMain.setBackground(ColorPalette.BACKGROUND);

        // Painel Check-ins
        DsCard jpCheckInCard = new DsCard();
        jpCheckInCard.setLayout(new BorderLayout(Spacing.SM, Spacing.SM));
        DsTitleLabel lblCheckin = new DsTitleLabel("Check-ins do dia");
        jpCheckInCard.add(lblCheckin, BorderLayout.NORTH);

        jpCheckInList = new JPanel();
        jpCheckInList.setLayout(new BoxLayout(jpCheckInList, BoxLayout.Y_AXIS));
        jpCheckInList.setBackground(ColorPalette.SURFACE);
        JScrollPane scrollCheckin = new JScrollPane(jpCheckInList);
        scrollCheckin.setBorder(BorderFactory.createEmptyBorder());
        scrollCheckin.getVerticalScrollBar().setUnitIncrement(16);
        jpCheckInCard.add(scrollCheckin, BorderLayout.CENTER);

        // Painel Check-outs
        DsCard jpCheckOutCard = new DsCard();
        jpCheckOutCard.setLayout(new BorderLayout(Spacing.SM, Spacing.SM));
        DsTitleLabel lblCheckout = new DsTitleLabel("Check-outs do dia");
        jpCheckOutCard.add(lblCheckout, BorderLayout.NORTH);

        jpCheckOutList = new JPanel();
        jpCheckOutList.setLayout(new BoxLayout(jpCheckOutList, BoxLayout.Y_AXIS));
        jpCheckOutList.setBackground(ColorPalette.SURFACE);
        JScrollPane scrollCheckout = new JScrollPane(jpCheckOutList);
        scrollCheckout.setBorder(BorderFactory.createEmptyBorder());
        scrollCheckout.getVerticalScrollBar().setUnitIncrement(16);
        jpCheckOutCard.add(scrollCheckout, BorderLayout.CENTER);

        jpMain.add(jpCheckInCard);
        jpMain.add(jpCheckOutCard);

        this.add(jpTopo, BorderLayout.NORTH);
        this.add(jpMain, BorderLayout.CENTER);
    }

    public void carregarDados() {
        controller.carregarDadosDashboard().thenAccept(data -> SwingUtilities.invokeLater(() -> {
            List<ReservaDTO> checkins = controller.getCheckinsDoDia(data);
            List<ReservaDTO> checkouts = controller.getCheckoutsDoDia(data);

            jpCheckInList.removeAll();
            if (checkins.isEmpty()) {
                jpCheckInList.add(new DsLabel("Nenhum check-in programado para hoje."));
            } else {
                for (ReservaDTO r : checkins) {
                    jpCheckInList.add(criarItemReserva(r, true));
                    jpCheckInList.add(Box.createRigidArea(new Dimension(0, Spacing.SM)));
                }
            }

            jpCheckOutList.removeAll();
            if (checkouts.isEmpty()) {
                jpCheckOutList.add(new DsLabel("Nenhum check-out programado para hoje."));
            } else {
                for (ReservaDTO r : checkouts) {
                    jpCheckOutList.add(criarItemReserva(r, false));
                    jpCheckOutList.add(Box.createRigidArea(new Dimension(0, Spacing.SM)));
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

    private JPanel criarItemReserva(ReservaDTO reserva, boolean isCheckin) {
        JPanel card = new JPanel(new BorderLayout(Spacing.SM, Spacing.SM));
        card.setBackground(ColorPalette.BACKGROUND); // Slight contrast inside the DsCard
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_VARIANT, 1, true),
                BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(ColorPalette.BACKGROUND);
        String nome = reserva.getGuestName() != null ? reserva.getGuestName() : "Hóspede Desconhecido";
        String quarto = reserva.getRoomNumber() != null ? reserva.getRoomNumber() : "N/A";
        
        DsLabel lblNome = new DsLabel(nome);
        lblNome.setFont(theme.DesignTokens.Typography.TITLE_FONT.deriveFont(16f));
        info.add(lblNome);
        
        DsLabel lblQuarto = new DsLabel("Quarto: " + quarto);
        lblQuarto.setForeground(ColorPalette.TEXT_SECONDARY);
        info.add(lblQuarto);

        card.add(info, BorderLayout.CENTER);

        String btnLabel = isCheckin ? "Check-in" : "Check-out";
        DsButton btnAcao = new DsButton(btnLabel, isCheckin ? DsButton.ButtonType.PRIMARY : DsButton.ButtonType.DANGER);

        btnAcao.addActionListener(e -> {
            if (isCheckin) {
                new ModalCheckin(controller, reserva, this::carregarDados).setVisible(true);
            } else {
                new ModalCheckout(controller, reserva, this::carregarDados).setVisible(true);
            }
        });

        card.add(btnAcao, BorderLayout.EAST);

        return card;
    }
}
