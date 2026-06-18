package main;

import DTO.ReservaDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import javax.swing.*;

public class TelaHospedes extends JPanel {

    private DefaultTableModel modeloTabela;
    private JTable tabela_reservas;

    public TelaHospedes() {
        this.setLayout(new BorderLayout());

        JPanel jp_topo = new JPanel((new BorderLayout()));
        jp_topo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel jl_titulo = new JLabel("Gerenciamento de Reservas");
        jl_titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btn_nova_reserva = new JButton("Novo Hóspede");
        btn_nova_reserva.setBackground(new Color(40,167, 69));
        btn_nova_reserva.setForeground(Color.WHITE);

        jp_topo.add(jl_titulo, BorderLayout.WEST);
        jp_topo.add(btn_nova_reserva, BorderLayout.EAST);

        String[] colunas = {"Nome", "CPF", "Email", "Data de Nascimento"};

        modeloTabela = new DefaultTableModel(colunas, 0);
        tabela_reservas = new JTable((modeloTabela));
        tabela_reservas.setRowHeight(25);

        JScrollPane jp_tabela = new JScrollPane(tabela_reservas);
        jp_tabela.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel jp_acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10,10));

        this.add(jp_topo, BorderLayout.NORTH);
        this.add(jp_acoes, BorderLayout.SOUTH);
        this.add(jp_tabela, BorderLayout.CENTER);

        carregarReservasDaAPI();
    }

    private void carregarReservasDaAPI(){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/reservas"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                String jsonResposta = response.body();
                modeloTabela.setRowCount(0);

                Gson gson = new Gson();

                Type tipoLista = new TypeToken<List<ReservaDTO>>(){}.getType();
                List<ReservaDTO> listaReservas = gson.fromJson(jsonResposta, tipoLista);

                for(ReservaDTO r : listaReservas){
                    modeloTabela.addRow(new Object[]{
                            r.getId(),
                            r.getRoomId(),
                            r.getExpectedCheckInDate(),
                            r.getExpectedCheckOutDate(),
                            r.getStatuts(),
                    });
                }
            }else {
                JOptionPane.showMessageDialog(this, "Erro ao buscar dados. Status: " + response.statusCode());
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro de conexão, Verifique se o Spring Boot esta rodando");
        }
    }

    private void dispararAcaoAPI(String id , String acao) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/reservas/" + id + "/" + acao))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                JOptionPane.showMessageDialog(this, "Ação '" + acao + "' realizada com sucesso!");
                carregarReservasDaAPI();
            } else if (response.statusCode() == 400) {
                JOptionPane.showMessageDialog(this, "BLoqueado pela Regra de Negocio: \n" + response.body(), "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro no servidor: " + response.statusCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro de rede ao tentar fazer " + acao);
        }
    }


}
