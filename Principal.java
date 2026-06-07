import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Principal {
    
    public void menu_principal () {
        JFrame jf_menu_principal = new JFrame();

        JPanel jp_menu_lateral = new JPanel();
        JPanel jp_conteudo = new JPanel();

        JButton btn_inicio = new JButton("Página Inicial");
        JButton btn_reservas = new JButton("Reservas");
        JButton btn_hospedes = new JButton("Hóspedes");

        JTextArea ta_conteudo = new JTextArea();

        jf_menu_principal.setLayout(new BorderLayout());
        jf_menu_principal.add(jp_menu_lateral, BorderLayout.WEST);
        jf_menu_principal.add(jp_conteudo, BorderLayout.CENTER);

        jp_conteudo.setLayout(new BorderLayout());
        jp_conteudo.add(ta_conteudo);

        jp_menu_lateral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridy = 0;
        gbc.weighty = 0;
        jp_menu_lateral.add(btn_inicio, gbc);

        gbc.gridy = 1;
        jp_menu_lateral.add(btn_reservas, gbc);

        gbc.gridy = 2;
        jp_menu_lateral.add(btn_hospedes, gbc);

        
        jf_menu_principal.setSize(600, 550);
        jf_menu_principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf_menu_principal.setLocationRelativeTo(null);
        jf_menu_principal.setVisible(true);
    }
}
