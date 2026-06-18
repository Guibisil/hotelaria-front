package views.dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DashboardView extends JPanel {

    private final Checks check = new Checks();

    public DashboardView() {
        renderDashboard();
    }

    public void renderDashboard () {  
        JLabel titulo = new JLabel("Página Inicial");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));    

        JPanel jpCheckIn = new JPanel();

        ActionListener btnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check.showPaymentModal();
            }
        };

        String[] nomes = {"Anna", "Claudio", "Brenda"};
        String[] ids =  {"12", "32", "4"};

        jpCheckIn.setLayout(new BorderLayout(0, 15));
        jpCheckIn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jpCheckIn.add(new JLabel("Check-ins"), BorderLayout.NORTH);
        jpCheckIn.add(buildListCards(ids, nomes, btnListener, "Check-in"), BorderLayout.CENTER);

        JPanel jpCheckOut = new JPanel();

        String[] nomes1 = {"Beta", "Dunga", "Murilo"};
        String[] ids1 =  {"43", "2", "34"};

        jpCheckOut.setLayout(new BorderLayout(0, 15));
        jpCheckOut.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jpCheckOut.add(new JLabel("Check-outs"), BorderLayout.NORTH);
        jpCheckOut.add(buildListCards(ids1, nomes1, btnListener, "Check-out"), BorderLayout.CENTER);

        setupLayout(titulo, jpCheckOut, jpCheckIn);
    }

    public JPanel buildListCards (String[] ids, String[] nomes, ActionListener btnListener, String check) {
        JPanel jpCardHospedes = new JPanel();

        for (int i = 0; i < nomes.length; i++) {
            JLabel lblNome = new JLabel(nomes[i]);
            JButton btnAcao = new JButton(check);
            btnAcao.setActionCommand(ids[i]);
            btnAcao.addActionListener(btnListener);

            jpCardHospedes.add(lblNome);
            jpCardHospedes.add(btnAcao);
        }    

        jpCardHospedes.setLayout(new GridLayout(0, 4, 10, 10));
        return jpCardHospedes;
    } 

    public void setupLayout(JLabel titulo, JPanel jpCheckIn, JPanel jpCheckOut) {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        this.add(titulo);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridy = 0;
        gbc.weighty = 0;
        this.add(titulo, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;
        this.add(jpCheckIn, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        this.add(jpCheckOut, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        this.add(Box.createVerticalGlue(), gbc);

    }

}
