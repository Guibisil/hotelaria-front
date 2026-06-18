package services;

import DTO.HospedeDTO;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IHospedeService {
    CompletableFuture<List<HospedeDTO>> buscarHospedes();
    CompletableFuture<Void> cadastrarHospede(HospedeDTO hospede);
}
