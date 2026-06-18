package services;

import DTO.ReservaDTO;
import enums.ReservaAction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IReservaService {
    CompletableFuture<List<ReservaDTO>> buscarReservas();
    CompletableFuture<Void> realizarAcao(String id, ReservaAction acao);
    CompletableFuture<Void> criarReserva(ReservaDTO reserva);
}
