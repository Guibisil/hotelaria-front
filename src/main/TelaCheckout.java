package main;

import DTO.HospedesDTO;
import DTO.QuartoDTO;
import DTO.ReservaDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaCheckout {

    private ReservaDTO nova_reserva = new ReservaDTO();
    private Long id_reserva;
    private float valor;

    public TelaCheckout(String id_guest, String id_room) {
        nova_reserva.setGuest_id(Integer.parseInt(id_guest));
        nova_reserva.setRoom_id(Integer.parseInt(id_room));

        JFrame jf_janela_check = new JFrame();
        JPanel jp_valor = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel jp_info = new JPanel();
        JLabel jl_check = new JLabel("Realizar Check-out");
        JButton btn_pag = new JButton("Realizar check-out");

        btn_pag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check_out();
                jf_janela_check.dispose();
            }
        });

        jl_check.setFont(new Font("Arial", Font.BOLD, 18));

        info_pessoas(jp_info, id_guest, id_room, jp_valor);

        jp_info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jf_layout(jf_janela_check, jp_info, jl_check, btn_pag, jp_valor);

        jf_janela_check.setSize(250, 350);
        jf_janela_check.setLocationRelativeTo(null);
        jf_janela_check.setVisible(true);
    }

    public void info_pessoas(JPanel jp_info, String id_guest, String id_room, JPanel jp_valor) {
        String nome = carregar_hospede_id(id_guest);
        String quarto = carregar_quarto_id(id_room);

        JLabel jl_nome = new JLabel(nome);
        JLabel jl_quarto = new JLabel(quarto);
        JLabel jl_data_check_out = new JLabel();
        JLabel jl_valor = new JLabel();

        carrega_reserva_api(jl_data_check_out, id_guest, jl_valor);

        jp_info.add(new JLabel("Hóspede"));
        jp_info.add(jl_nome);
        jp_info.add(new JLabel("Quarto"));
        jp_info.add(jl_quarto);
        jp_info.add(new JLabel("Saída"));
        jp_info.add(jl_data_check_out);

        jp_info.setLayout(new GridLayout(3, 2, 10, 10));
        jp_info.setBackground(Color.WHITE);

        jp_valor.setBackground(Color.WHITE);

        jp_valor.add(new JLabel("Valor total"));

        jp_valor.add(jl_valor);

    }

    public String carregar_quarto_id (String id_room) {
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/rooms/" + id_room))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {

                String json_quarto = response.body();

                Gson gson = new Gson();

                QuartoDTO quarto = gson.fromJson(json_quarto, QuartoDTO.class);

                if (quarto != null) {
                    System.out.println(quarto.getNumber());
                    return quarto.getNumber();
                }
                return null;

            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro de conexão, Verifique se o Spring Boot esta rodando");
            return null;
        }
    }

    public String carregar_hospede_id (String id_guest) {
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/guests"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {

                String json_reservas = response.body();

                Gson gson = new Gson();

                Type tipoLista = new TypeToken<java.util.List<HospedesDTO>>(){}.getType();
                List<HospedesDTO> ListaHospedes = gson.fromJson(json_reservas, tipoLista);

                for(HospedesDTO r : ListaHospedes) {
                    String id_hospede = String.valueOf(r.getId());
                    if (id_guest.equals(id_hospede)){
                        return r.getName();
                    }

                }
                return null;

            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro de conexão, Verifique se o Spring Boot esta rodando");
            return null;
        }
    }

    public void carrega_reserva_api (JLabel jl_data_check_out, String id_guest, JLabel jl_valor) {
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/reservations"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                String json_reservas = response.body();

                Gson gson = new Gson();

                LocalDate hoje = LocalDate.now();
                DateTimeFormatter formatador = DateTimeFormatter.ISO_LOCAL_DATE;

                Type tipoLista = new TypeToken<java.util.List<ReservaDTO>>(){}.getType();
                List<ReservaDTO> ListaReseva = gson.fromJson(json_reservas, tipoLista);

                for(ReservaDTO r : ListaReseva){
                    String id_hospede = String.valueOf(r.getGuest_id());

                    if (id_guest.equals(id_hospede)) {
                        jl_data_check_out.setText(r.getCheckout_date());
                        nova_reserva.setCheckin_date(r.getCheckin_date());
                        nova_reserva.setCheckout_date(r.getCheckout_date());
                        nova_reserva.setTotal_amount(r.getTotal_amount());
                        id_reserva = r.getId();
                        valor = r.getTotal_amount();
                        jl_valor.setText(String.valueOf(valor));
                    }
                }

            }else {
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro de conexão, Verifique se o Spring Boot esta rodando1");
        }

    }

    public  void jf_layout (JFrame jf_janela_check, JPanel jp_info, JLabel jl_check, JButton btn_pag, JPanel jp_valor) {
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
        jf_janela_check.add(jp_valor, gbc);

        gbc.gridy = 4;
        gbc.weighty = 0;
        jf_janela_check.add(btn_pag, gbc);
    }

    public void check_out () {
        try {
            Gson gson = new Gson();
            String json_corpo = gson.toJson(nova_reserva);

            System.out.println("JSON enviado no check-out: " + json_corpo);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/reservations/" + id_reserva + "/checkout"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json_corpo))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                JOptionPane.showMessageDialog(null, "Check-out feito com sucesso!");
            } else {
                System.out.println("Erro do Servidor (Body): " + response.body());
                JOptionPane.showMessageDialog(null, "Falha ao fazer check-in. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro de conexão com o servidor.");
        }
    }
}
