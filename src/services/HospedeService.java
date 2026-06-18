package services;

import DTO.HospedeDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.ApiConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HospedeService implements IHospedeService {

    private final HttpClient httpClient;
    private final Gson gson;
    private static final String API_URL = ApiConfig.BASE_URL + "/guests";

    public HospedeService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    public CompletableFuture<List<HospedeDTO>> buscarHospedes() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        return gson.fromJson(response.body(), new TypeToken<List<HospedeDTO>>(){}.getType());
                    } else {
                        throw new RuntimeException("Erro ao buscar hóspedes: " + response.statusCode());
                    }
                });
    }

    @Override
    public CompletableFuture<Void> cadastrarHospede(HospedeDTO hospede) {
        String json = gson.toJson(hospede);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    int status = response.statusCode();
                    if (status == 200 || status == 201) {
                        return; // Sucesso
                    } else if (status == 400) {
                        throw new services.ReservaService.BusinessRuleException(response.body());
                    } else {
                        throw new RuntimeException("Erro no servidor. Código: " + status);
                    }
                });
    }
}
