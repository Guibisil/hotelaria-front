package services;

import DTO.HospedesDTO;
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
    public CompletableFuture<List<HospedesDTO>> buscarHospedes() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        return gson.fromJson(response.body(), new TypeToken<List<HospedesDTO>>(){}.getType());
                    } else {
                        throw new RuntimeException("Erro ao buscar hóspedes: " + response.statusCode());
                    }
                });
    }
}
