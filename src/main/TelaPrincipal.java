package main;

import DTO.HospedesDTO;
import DTO.ReservaDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaPrincipal extends JPanel {

    public TelaPrincipal() {
        pagina_checks();
    }

    public void pagina_checks () {  
        JLabel titulo = new JLabel("Página Inicial");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));    

        JPanel jp_check_in = new JPanel();
        JPanel jp_check_out = new JPanel();

        jp_check_in.setLayout(new BorderLayout(0, 15));
        jp_check_in.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp_check_in.add(new JLabel("Check-ins"), BorderLayout.NORTH);

        jp_check_out.setLayout(new BorderLayout(0, 15));
        jp_check_out.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp_check_out.add(new JLabel("Check-outs"), BorderLayout.NORTH);

        carregar_reserva_api(jp_check_in, jp_check_out);

        layout_pag(titulo, jp_check_in,  jp_check_out);
    }

    public void card_hospede (JPanel jp_card_hospedes, String check, int id_guest, int id_room) {
        ActionListener btn_listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn_clicado = (JButton) e.getSource();

                String tipo_btn = (String) btn_clicado.getClientProperty("tipo");
                String quarto = (String) btn_clicado.getClientProperty("quarto");
                String id_cliente = e.getActionCommand();

                if ("check-in".equals(tipo_btn)) {
                    new TelaCheckin(id_cliente, quarto);
                } else if ("check-out".equals(tipo_btn)) {
                    new TelaCheckout(id_cliente, quarto);
                }
            }
        };

        String hospede = carregar_hospede_id(id_guest);

        if (hospede == null) {
            hospede = "Hóspede Desconhecido (ID: " + id_guest + ")";
        }

        JPanel miniCard = new JPanel();
        miniCard.setLayout(new GridBagLayout());
        miniCard.setBackground(Color.WHITE);
        miniCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)); // Uma bordinha arredondada

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.gridx = 0;

        JLabel jl_nome = new JLabel(hospede);
        jl_nome.setFont(new Font("Arial", Font.PLAIN, 14));
        jl_nome.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        gbc.gridy = 0;
        gbc.weighty = 0.5;
        miniCard.add(jl_nome, gbc);

        JButton btn_check = new JButton(check);
        btn_check.setActionCommand(String.valueOf(id_guest));
        btn_check.putClientProperty("quarto", String.valueOf(id_room));
        btn_check.putClientProperty("tipo", check);
        btn_check.addActionListener(btn_listener);

        btn_check.setPreferredSize(new Dimension(100, 30));

        gbc.gridy = 1;
        gbc.weighty = 0.5;
        miniCard.add(btn_check, gbc);

        jp_card_hospedes.add(miniCard);

    }

    private void carregar_reserva_api(JPanel jp_check_in, JPanel jp_check_out){
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

                JPanel jp_card_hospedes_in = new JPanel();
                jp_card_hospedes_in.setLayout(new GridLayout(0, 4, 10, 10));

                JPanel jp_card_hospedes_out = new JPanel();
                jp_card_hospedes_out.setLayout(new GridLayout(0, 4, 10, 10));

                Type tipoLista = new TypeToken<java.util.List<ReservaDTO>>(){}.getType();
                List<ReservaDTO> ListaReseva = gson.fromJson(json_reservas, tipoLista);

                for(ReservaDTO r : ListaReseva){
                    int id_guest = r.getGuest_id();
                    int id_room = r.getRoom_id();

                    if (r.getCheckin_date() != null && !r.getCheckin_date().isEmpty()) {
                        LocalDate data_checkin = LocalDate.parse(r.getCheckin_date(), formatador);

                        if (data_checkin.isEqual(hoje)) {
                            card_hospede(jp_card_hospedes_in, "check-in", id_guest, id_room);
                        }

                    }
                    if (r.getCheckout_date() != null && !r.getCheckout_date().isEmpty()) {
                        LocalDate data_checkout = LocalDate.parse(r.getCheckout_date(), formatador);

                        if (data_checkout.isEqual(hoje)) {
                            card_hospede(jp_card_hospedes_out, "check-out", id_guest, id_room);
                        }

                    }

                }

                jp_check_in.add(jp_card_hospedes_in, BorderLayout.CENTER);
                jp_check_out.add(jp_card_hospedes_out, BorderLayout.CENTER);

                jp_check_in.revalidate();
                jp_check_in.repaint();
                jp_check_out.revalidate();
                jp_check_out.repaint();

            }else {
                JOptionPane.showMessageDialog(this, "Erro ao buscar dados. Status: " + response.statusCode());
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro de conexão, Verifique se o Spring Boot esta rodando1");
        }
    }

    public String carregar_hospede_id (int id_guest) {
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
                    if (id_guest == r.getId()){
                        return r.getName();
                    }

                }
                return null;

            }else {
                JOptionPane.showMessageDialog(this, "Erro ao buscar dados. Status: " + response.statusCode());
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro de conexão, Verifique se o Spring Boot esta rodando");
            return null;
        }
    }

    public void layout_pag(JLabel titulo, JPanel jp_check_in, JPanel jp_check_out) {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        this.add(titulo, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.5;
        this.add(jp_check_in, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.5;
        this.add(jp_check_out, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        this.add(Box.createVerticalGlue(), gbc);

    }

}