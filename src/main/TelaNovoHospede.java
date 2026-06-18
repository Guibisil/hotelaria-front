package main;

import DTO.HospedesDTO;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

        JTextField txt_nome = new JTextField();
        JTextField txt_cpf = new JTextField();
        JTextField txt_email = new JTextField();

        JFormattedTextField txt_data_nas = null;
        try {
            MaskFormatter mascara = new MaskFormatter("##/##/####");
            mascara.setPlaceholderCharacter('_');

            txt_data_nas = new JFormattedTextField(mascara);
            txt_data_nas.setColumns(8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton btn_hospede = new JButton("Cadastrar hóspede");
        JFormattedTextField txt_data_formatada = txt_data_nas;
        btn_hospede.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastro(txt_nome, txt_cpf, txt_email, txt_data_formatada, jf_novo_hospede);
            }
        });

        jl_titulo.setFont(new Font("Arial", Font.BOLD, 18));

        jp_dados.add(jl_titulo);
        jp_dados.add(jl_nome);
        jp_dados.add(txt_nome);
        jp_dados.add(jl_cpf);
        jp_dados.add(txt_cpf);
        jp_dados.add(jl_email);
        jp_dados.add(txt_email);
        jp_dados.add(jl_data_nas);
        jp_dados.add(txt_data_nas);
        jp_dados.add(new JLabel(""));
        jp_dados.add(btn_hospede);

        jp_dados.setOpaque(false);
        jp_dados.setLayout(new GridLayout(11,1, 1, 10));
        jp_dados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jf_novo_hospede.add(jp_dados);

        jf_novo_hospede.getContentPane().setBackground(new Color(255, 255, 255));
        jf_novo_hospede.setSize(300, 450);
        jf_novo_hospede.setLocationRelativeTo(null);
        jf_novo_hospede.setVisible(true);
    }

    public void cadastro(JTextField txt_nome, JTextField txt_cpf, JTextField txt_email, JFormattedTextField txt_data_formatada, JFrame jf_novo_hospede) {
        String data_conv = txt_data_formatada.getText()
                .replaceAll("(\\d{2})/(\\d{2})/(\\d{4})", "$3-$2-$1");

        HospedesDTO hospede = new HospedesDTO();
        hospede.setName(txt_nome.getText());
        hospede.setCpf(txt_cpf.getText());
        hospede.setEmail(txt_email.getText());
        hospede.setBirth_date(data_conv);

        cadastrar_hospede_api(hospede, jf_novo_hospede);
    }

    private void cadastrar_hospede_api(HospedesDTO hospede, JFrame jf_novo_hospede) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(hospede);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/guests"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                JOptionPane.showMessageDialog(null, "Hóspede cadastrado com sucesso!");

                new TelaHospedes();
                jf_novo_hospede.setVisible(false);
            } else if (response.statusCode() == 400) {
                JOptionPane.showMessageDialog(null, "BLoqueado pela Regra de Negocio: \n" + response.body(), "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro no servidor: " + response.statusCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro de rede ao tentar fazer cadastro de hóspede");
        }
    }

}
