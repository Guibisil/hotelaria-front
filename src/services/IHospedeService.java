package services;

import DTO.HospedesDTO;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IHospedeService {
    CompletableFuture<List<HospedesDTO>> buscarHospedes();
}
