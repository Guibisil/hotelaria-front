package main;

import main.Checks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaPrincipal extends JPanel {

    private final Checks check = new Checks();

    public TelaPrincipal() {
        pagina_checks();
    }

    public void pagina_checks () {  
        JLabel titulo = new JLabel("Página Inicial");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));    

        JPanel jp_check_in = new JPanel();

        ActionListener btn_listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idHospede = e.getActionCommand();
                check.novo_check();
            }
        };

        String[] nomes = {"Anna", "Claudio", "Brenda"};
        String[] ids =  {"12", "32", "4"};

        jp_check_in.setLayout(new BorderLayout(0, 15));
        jp_check_in.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp_check_in.add(new JLabel("Check-ins"), BorderLayout.NORTH);
        jp_check_in.add(corre_lista(ids, nomes, btn_listener, "Check-in"), BorderLayout.CENTER);

        JPanel jp_check_out = new JPanel();

        String[] nomes1 = {"Beta", "Dunga", "Murilo"};
        String[] ids1 =  {"43", "2", "34"};

        jp_check_out.setLayout(new BorderLayout(0, 15));
        jp_check_out.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp_check_out.add(new JLabel("Check-outs"), BorderLayout.NORTH);
        jp_check_out.add(corre_lista(ids1, nomes1, btn_listener, "Check-out"), BorderLayout.CENTER);

        layout_pag(titulo, jp_check_out, jp_check_in);
    }

    public JPanel corre_lista (String[] ids, String[] nomes, ActionListener btn_listener, String check) {
        JPanel jp_card_hospedes = new JPanel();

        for (int i = 0; i < nomes.length; i++) {
            JLabel teste1 = new JLabel(nomes[i]);
            JButton btn_teste = new JButton(check);
            btn_teste.setActionCommand(ids[i]);
            btn_teste.addActionListener(btn_listener);

            jp_card_hospedes.add(teste1);
            jp_card_hospedes.add(btn_teste);
        }    

        jp_card_hospedes.setLayout(new GridLayout(0, 4, 10, 10));
        return jp_card_hospedes;
    } 

    public void layout_pag(JLabel titulo, JPanel jp_check_in, JPanel jp_check_out) {
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
        this.add(jp_check_in, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        this.add(jp_check_out, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        this.add(Box.createVerticalGlue(), gbc);

    }

}