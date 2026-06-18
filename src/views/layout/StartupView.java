package views.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class StartupView {

    public void show() {
        JFrame mainFrame = new JFrame();

        JPanel jpPanel = new JPanel();
        JPanel jpBtn = new JPanel();

        JLabel jlTitulo = new JLabel("Hotelaria");
        jlTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        jlTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnMenu = new JButton("Iniciar");
        Dimension tamanhoBtn = new Dimension(100, 30);
        btnMenu.setPreferredSize(tamanhoBtn);
        btnMenu.setMaximumSize(tamanhoBtn);
        btnMenu.setMinimumSize(tamanhoBtn);

        jpPanel.setLayout(new GridLayout(2, 1, 10, 10));

        jpPanel.add(jlTitulo);
        jpBtn.add(btnMenu);
        jpPanel.add(jpBtn);

        mainFrame.setSize(300, 250);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().add(jpPanel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        btnMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                MainLayoutView principal = new MainLayoutView();
                principal.showMainLayout();
            }

        });
        
    }

}