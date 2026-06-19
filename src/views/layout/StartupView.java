package views.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import components.DsButton;
import components.DsCard;
import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;
import theme.DesignTokens.Spacing;

public class StartupView {

    public void show() {
        JFrame mainFrame = new JFrame("Hotelaria");

        JPanel jpBackground = new JPanel();
        jpBackground.setBackground(ColorPalette.BACKGROUND);
        jpBackground.setLayout(new GridBagLayout()); // Center the card

        DsCard jpCard = new DsCard();
        jpCard.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(Spacing.LG, Spacing.LG, Spacing.MD, Spacing.LG);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel jlTitulo = new JLabel("Hotelaria");
        jlTitulo.setFont(Typography.TITLE_FONT);
        jlTitulo.setForeground(ColorPalette.PRIMARY);
        jpCard.add(jlTitulo, gbc);

        JLabel jlSubtitulo = new JLabel("Bem-vindo ao sistema de gestão");
        jlSubtitulo.setFont(Typography.BODY_FONT);
        jlSubtitulo.setForeground(ColorPalette.TEXT_SECONDARY);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, Spacing.LG, Spacing.XL, Spacing.LG);
        jpCard.add(jlSubtitulo, gbc);

        DsButton btnMenu = new DsButton("Iniciar Sessão", DsButton.ButtonType.PRIMARY);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, Spacing.LG, Spacing.LG, Spacing.LG);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpCard.add(btnMenu, gbc);

        jpBackground.add(jpCard);

        mainFrame.setSize(500, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().add(jpBackground);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                MainLayoutView principal = new MainLayoutView();
                principal.showMainLayout();
            }
        });
    }
}
