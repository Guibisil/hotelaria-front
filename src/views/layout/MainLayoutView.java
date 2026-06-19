package views.layout;

import javax.swing.*;
import java.awt.*;
import config.DIContainer;
import views.dashboard.DashboardView;
import views.reservas.TelaReservas;
import views.hospedes.TelaHospedes;
import components.DsSidebarButton;
import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;
import theme.DesignTokens.Spacing;

public class MainLayoutView {
    
    private JPanel jpContent;
    private CardLayout cardLayout;
    
    private DsSidebarButton btnInicio;
    private DsSidebarButton btnReservas;
    private DsSidebarButton btnHospedes;

    private DashboardView dashboardView;
    private TelaReservas telaReservas;
    private TelaHospedes telaHospedes;

    public void showMainLayout() {
        JFrame mainFrame = new JFrame("Hotelaria");
        mainFrame.setLayout(new BorderLayout());
        
        JPanel jpSidebar = new JPanel();
        jpSidebar.setBackground(ColorPalette.SIDEBAR_BG);
        jpSidebar.setPreferredSize(new Dimension(250, 0));
        
        jpContent = new JPanel();
        cardLayout = new CardLayout();
        jpContent.setLayout(cardLayout);
        jpContent.setBackground(ColorPalette.BACKGROUND);

        mainFrame.add(jpSidebar, BorderLayout.WEST);
        mainFrame.add(jpContent, BorderLayout.CENTER);

        setupSidebar(jpSidebar);
        setupViews();

        mainFrame.setSize(1024, 768); 
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        
        // Show default view
        cardLayout.show(jpContent, "DASHBOARD");
        updateActiveButton(btnInicio);
    }

    private void setupSidebar(JPanel jpSidebar) {
        jpSidebar.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Logo
        JLabel jlLogo = new JLabel("Hotelaria");
        jlLogo.setFont(Typography.TITLE_FONT);
        jlLogo.setForeground(ColorPalette.SIDEBAR_TEXT);
        
        // Add logo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(Spacing.LG, Spacing.MD, Spacing.XL, Spacing.MD);
        gbc.anchor = GridBagConstraints.WEST;
        jpSidebar.add(jlLogo, gbc);

        // Buttons
        btnInicio = new DsSidebarButton("Página Inicial");
        btnReservas = new DsSidebarButton("Reservas");
        btnHospedes = new DsSidebarButton("Hóspedes");

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, Spacing.SM, Spacing.SM, Spacing.SM);
        gbc.weightx = 1.0;
        
        gbc.gridy = 1;
        jpSidebar.add(btnInicio, gbc);

        gbc.gridy = 2;
        jpSidebar.add(btnReservas, gbc);

        gbc.gridy = 3;
        jpSidebar.add(btnHospedes, gbc);

        // Spacer to push everything up
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        jpSidebar.add(spacer, gbc);

        // Listeners
        btnInicio.addActionListener(e -> {
            cardLayout.show(jpContent, "DASHBOARD");
            updateActiveButton(btnInicio);
            if (dashboardView != null) {
                dashboardView.carregarDados();
            }
        });

        btnReservas.addActionListener(e -> {
            cardLayout.show(jpContent, "RESERVAS");
            updateActiveButton(btnReservas);
            if (telaReservas != null) {
                telaReservas.carregarReservas();
            }
        });

        btnHospedes.addActionListener(e -> {
            cardLayout.show(jpContent, "HOSPEDES");
            updateActiveButton(btnHospedes);
            if (telaHospedes != null) {
                telaHospedes.carregarHospedes();
            }
        });
    }

    private void setupViews() {
        dashboardView = new DashboardView(DIContainer.getInstance().getDashboardController());
        telaReservas = DIContainer.getInstance().criarTelaReservas();
        telaHospedes = DIContainer.getInstance().criarTelaHospedes();
        
        dashboardView.setBackground(ColorPalette.BACKGROUND);
        telaReservas.setBackground(ColorPalette.BACKGROUND);
        telaHospedes.setBackground(ColorPalette.BACKGROUND);

        jpContent.add(dashboardView, "DASHBOARD");
        jpContent.add(telaReservas, "RESERVAS");
        jpContent.add(telaHospedes, "HOSPEDES");
    }
    
    private void updateActiveButton(DsSidebarButton activeBtn) {
        btnInicio.setActive(btnInicio == activeBtn);
        btnReservas.setActive(btnReservas == activeBtn);
        btnHospedes.setActive(btnHospedes == activeBtn);
    }
}
