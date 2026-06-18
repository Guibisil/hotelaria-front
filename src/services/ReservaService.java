package services;

import DTO.ReservaDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReservaService {

    private final HttpClient client;
    private final Gson gson;
    private static final String BASE_URL = "http://localhost:8080/api/reservations";

    public ReservaService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Busca todas as reservas de forma assíncrona da API.
     */
    public CompletableFuture<List<ReservaDTO>> buscarReservas() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        Type tipoLista = new TypeToken<List<ReservaDTO>>(){}.getType();
                        return gson.fromJson(response.body(), tipoLista);
                    } else {
                        throw new RuntimeException("Erro ao buscar dados. Status: " + response.statusCode());
                    }
                });
    }

    /**
     * Realiza uma ação (como 'checkin' ou 'checkout') de forma assíncrona na API.
     */
    public CompletableFuture<Void> realizarAcao(String id, String acao) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/" + acao))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    int status = response.statusCode();
                    if (status == 200 || status == 201) {
                        return; // Sucesso
                    } else if (status == 400) {
                        throw new BusinessRuleException(response.body());
                    } else {
                        throw new RuntimeException("Erro no servidor. Código: " + status);
                    }
                });
    }

    /**
     * Exceção customizada para encapsular erros de regra de negócio HTTP 400.
     */
    public static class BusinessRuleException extends RuntimeException {
        public BusinessRuleException(String message) {
            super(message);
        }
    }
}
