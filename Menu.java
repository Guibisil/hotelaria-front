import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu {

    public void inicio () {
        JFrame jf_janela = new JFrame();

        JPanel jp_painel = new JPanel();
        JPanel jp_btn = new JPanel();

        JLabel jl_titulo = new JLabel("Hotelaria");
        jl_titulo.setFont(new Font("Arial", Font.BOLD, 24));
        jl_titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btn_cadastrar = new JButton("Iniciar");
        Dimension tamanho_btn = new Dimension(100, 30);
        btn_cadastrar.setPreferredSize(tamanho_btn);
        btn_cadastrar.setMaximumSize(tamanho_btn);
        btn_cadastrar.setMinimumSize(tamanho_btn);

        jp_painel.setLayout(new GridLayout(2, 1, 10, 10));

        jp_painel.add(jl_titulo);
        jp_btn.add(btn_cadastrar);
        jp_painel.add(jp_btn);

        jf_janela.setSize(300, 250);
        jf_janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf_janela.getContentPane().add(jp_painel);
        jf_janela.setLocationRelativeTo(null);
        jf_janela.setVisible(true);

        btn_cadastrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });
        
    }

}