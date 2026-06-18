package main;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.MaskFormatter;

public class TelaNovoHospede {

    public TelaNovoHospede() {
        JFrame jf_novo_hospede = new JFrame();
        JPanel jp_dados = new JPanel();

        JLabel jl_titulo = new JLabel("Cadastro de hóspede");
        JLabel jl_nome = new JLabel("Nome");
        JLabel jl_cpf = new JLabel("CPF");
        JLabel jl_email = new JLabel("Email");
        JLabel jl_data_nas = new JLabel("Data de nascimento");


        JFormattedTextField txtData = null;
        try {
            MaskFormatter mascara = new MaskFormatter("##-##-####");
            mascara.setPlaceholderCharacter('_');

            txtData = new JFormattedTextField(mascara);
            txtData.setColumns(8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton btn_hospede = new JButton("Cadastrar hóspede");

        jl_titulo.setFont(new Font("Arial", Font.BOLD, 18));

        jf_novo_hospede.setSize(300, 350);
        jf_novo_hospede.setLocationRelativeTo(null);
        jf_novo_hospede.setVisible(true);
    }

}
