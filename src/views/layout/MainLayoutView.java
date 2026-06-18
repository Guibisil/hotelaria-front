package views.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import config.DIContainer;
import views.dashboard.DashboardView;
import views.reservas.TelaReservas;
import views.hospedes.TelaHospedes;

public class MainLayoutView {
    
    public void showMainLayout() {
        JFrame mainFrame = new JFrame();
        JPanel jpSidebar = new JPanel();
        JPanel jpContent = new JPanel();
        jpContent.setBackground(Color.WHITE);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(jpSidebar, BorderLayout.WEST);
        mainFrame.add(jpContent, BorderLayout.CENTER);

        jpContent.setLayout(new BorderLayout());
        
        showDashboard(jpContent);

        setupSidebar(jpSidebar, jpContent);

        mainFrame.setSize(600, 550);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public void setupSidebar (JPanel jpSidebar, JPanel jpContent) {
        JButton btnInicio = new JButton("Página Inicial");
        JButton btnReservas = new JButton("Reservas");
        JButton btnHospedes = new JButton("Hóspedes");

        JLabel jlLogo = new JLabel("Hotelaria");
        jlLogo.setFont(new Font("Arial", Font.BOLD, 18));

        jpSidebar.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridy = 0;
        gbc.weighty = 0;
        jpSidebar.add(jlLogo, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        jpSidebar.add(btnInicio, gbc);

        gbc.gridy = 2;
        jpSidebar.add(btnReservas, gbc);

        gbc.gridy = 3;
        jpSidebar.add(btnHospedes, gbc);

        gbc.gridy = 4;
        gbc.weighty = 1.0;
        jpSidebar.add(new JPanel(), gbc);


        btnInicio.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showDashboard(jpContent);
            }

        });

        btnReservas.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showReservations(jpContent);
            }

        });

        btnHospedes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showGuests(jpContent);
            }

        });
    }

    public void showDashboard(JPanel jpContent) {
        jpContent.removeAll();

        DashboardView dashboardView = new DashboardView();

        jpContent.add(dashboardView);

        jpContent.revalidate();
        jpContent.repaint();
    }

    public void showReservations(JPanel jpContent) {
        jpContent.removeAll();

        TelaReservas telaReservas = DIContainer.getInstance().criarTelaReservas();

        jpContent.add(telaReservas, BorderLayout.CENTER);

        jpContent.revalidate();
        jpContent.repaint();
    }

    public void showGuests(JPanel jpContent) {
        jpContent.removeAll();

        TelaHospedes telaHospedes = DIContainer.getInstance().criarTelaHospedes();

        jpContent.add(telaHospedes);

        jpContent.revalidate();
        jpContent.repaint();
    }
}
