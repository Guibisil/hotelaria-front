package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Principal {
    
    public void menu_principal () {
        JFrame jf_menu_principal = new JFrame();
        JPanel jp_menu_lateral = new JPanel();
        JPanel jp_conteudo = new JPanel();
        jp_conteudo.setBackground(Color.WHITE);

        jf_menu_principal.setLayout(new BorderLayout());
        jf_menu_principal.add(jp_menu_lateral, BorderLayout.WEST);
        jf_menu_principal.add(jp_conteudo, BorderLayout.CENTER);

        jp_conteudo.setLayout(new BorderLayout());
        
        pagina_inicial(jp_conteudo);

        menu_lateral(jp_menu_lateral, jp_conteudo);

        jf_menu_principal.setSize(600, 550);
        jf_menu_principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf_menu_principal.setLocationRelativeTo(null);
        jf_menu_principal.setVisible(true);
    }

    public void menu_lateral (JPanel jp_menu_lateral, JPanel jp_conteudo) {
        JButton btn_inicio = new JButton("Página Inicial");
        JButton btn_reservas = new JButton("Reservas");
        JButton btn_hospedes = new JButton("Hóspedes");

        JLabel jl_logo = new JLabel("Hotelaria");
        jl_logo.setFont(new Font("Arial", Font.BOLD, 18));

        jp_menu_lateral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridy = 0;
        gbc.weighty = 0;
        jp_menu_lateral.add(jl_logo, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        jp_menu_lateral.add(btn_inicio, gbc);

        gbc.gridy = 2;
        jp_menu_lateral.add(btn_reservas, gbc);

        gbc.gridy = 3;
        jp_menu_lateral.add(btn_hospedes, gbc);

        gbc.gridy = 4;
        gbc.weighty = 1.0;
        jp_menu_lateral.add(new JPanel(), gbc);


        btn_inicio.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pagina_inicial(jp_conteudo);
            }

        });

        btn_reservas.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reservas(jp_conteudo);
            }

        });

        btn_hospedes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                hospedes(jp_conteudo);
            }

        });
    }

    public void pagina_inicial (JPanel jp_conteudo) {
        jp_conteudo.removeAll();

        TelaPrincipal tela_principal = new TelaPrincipal();

        jp_conteudo.add(tela_principal);

        jp_conteudo.revalidate();
        jp_conteudo.repaint();
    }

    public void reservas (JPanel jp_conteudo) {
        jp_conteudo.removeAll();

        TelaReservas telaReservas = new TelaReservas();

        jp_conteudo.add(telaReservas, BorderLayout.CENTER);

        jp_conteudo.revalidate();
        jp_conteudo.repaint();
//
//        JLabel jl_reservas = new JLabel("página de reservas");
//        jl_reservas.setFont(new Font("Arial", Font.BOLD, 16));
//        jl_reservas.setHorizontalAlignment(SwingConstants.CENTER);
//
//        jp_conteudo.add(jl_reservas, BorderLayout.CENTER);
//        jp_conteudo.revalidate();
//        jp_conteudo.repaint();
    }

    public void hospedes (JPanel jp_conteudo) {
        jp_conteudo.removeAll();

        TelaHospedes tela_hospedes = new TelaHospedes();

        jp_conteudo.add(tela_hospedes);

        jp_conteudo.revalidate();
        jp_conteudo.repaint();
    }
}