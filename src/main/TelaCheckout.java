package main;
import javax.swing.*;
import java.awt.*;

import java.util.HashMap;

public class TelaCheckout {

    public TelaCheckout (String id_guest, String id_room) {
        JFrame jf_janela_check = new JFrame();
        JPanel jp_valor = new JPanel();
        JPanel jp_info = new JPanel();
        JLabel jl_check = new JLabel();
        JButton btn_pag = new JButton("Realizar Pagamento");

        jl_check.setFont(new Font("Arial", Font.BOLD, 18));

        info_pessoas(jp_info, jl_check, jp_valor);

        jp_info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp_valor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jf_layout(jf_janela_check, jp_valor, jp_info, jl_check, btn_pag);

        jf_janela_check.setSize(250, 400);
        jf_janela_check.setLocationRelativeTo(null);
        jf_janela_check.setVisible(true);
    }


    public void info_pessoas(JPanel jp_info, JLabel check, JPanel jp_valor) {
        HashMap<String, String> info_hospede = new HashMap<>();
        teste(info_hospede);

        check.setText("Realizar Check-in");

        JLabel jl_nome = new JLabel(info_hospede.get("nome"));
        JLabel jl_quarto = new JLabel(info_hospede.get("quarto"));
        JLabel jl_data_check_out = new JLabel(info_hospede.get("check_out"));
        JLabel jl_data_check_in = new JLabel(info_hospede.get("check_in"));

        jp_info.add(new JLabel("Hóspede"));
        jp_info.add(jl_nome);
        jp_info.add(new JLabel("Quarto"));
        jp_info.add(jl_quarto);
        jp_info.add(new JLabel("Entrda"));
        jp_info.add(jl_data_check_in);
        jp_info.add(new JLabel("Saída"));
        jp_info.add(jl_data_check_out);

        jp_info.setLayout(new GridLayout(4, 2, 10, 10));
        jp_info.setBackground(Color.WHITE);

        JLabel jl_valor = new JLabel(info_hospede.get("valor_final"));

        jp_valor.add(new JLabel("Valor total:"));
        jp_valor.add(jl_valor);

        jp_valor.setLayout(new GridLayout(1, 2, 10, 10));
        jp_valor.setBackground(Color.WHITE);
    }

    public void teste (HashMap info_hospede) {
        info_hospede.put("id", "12");
        info_hospede.put("valor_final", "2000");
        info_hospede.put("check_out", "01/11/2026");
        info_hospede.put("quarto", "101");
        info_hospede.put("check_in", "01/01/2016");
        info_hospede.put("nome", "Maria");
    }

    public  void jf_layout (JFrame jf_janela_check, JPanel jp_valor, JPanel jp_info, JLabel jl_check, JButton btn_pag) {
        jf_janela_check.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        gbc.weighty = 0;
        jf_janela_check.add(jl_check, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        jf_janela_check.add(new JLabel("Confirme os dados e pagamento"), gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;
        jf_janela_check.add(jp_info, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0;
        jf_janela_check.add(new JLabel("Pagamento"), gbc);

        gbc.gridy = 4;
        gbc.weighty = 0;
        jf_janela_check.add(jp_valor, gbc);

        gbc.gridy = 5;
        gbc.weighty = 0;
        jf_janela_check.add(btn_pag, gbc);
    }
}
