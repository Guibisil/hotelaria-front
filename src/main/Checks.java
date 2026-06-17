package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Checks {

    public void novo_check() {
        JFrame jf_janela_check = new JFrame();
        JPanel jp_check = new JPanel();

        jf_janela_check.setSize(250, 350);
        jf_janela_check.getContentPane().add(jp_check);
        jf_janela_check.setLocationRelativeTo(null);
        jf_janela_check.setVisible(true);

    }
}
