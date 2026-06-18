package services;

import DTO.QuartoDTO;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IQuartoService {
    CompletableFuture<List<QuartoDTO>> buscarQuartos();
}
