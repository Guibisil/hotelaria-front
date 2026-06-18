package services;

import DTO.HospedeDTO;
import DTO.QuartoDTO;
import DTO.ReservaDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TimelineAggregatorService {

    private final IQuartoService quartoService;
    private final IHospedeService hospedeService;
    private final IReservaService reservaService;

    public TimelineAggregatorService(IQuartoService quartoService, IHospedeService hospedeService, IReservaService reservaService) {
        this.quartoService = quartoService;
        this.hospedeService = hospedeService;
        this.reservaService = reservaService;
    }

    public static class TimelineData {
        public List<QuartoDTO> quartos;
        public List<HospedeDTO> hospedes;
        public List<ReservaDTO> reservas;
    }

    public CompletableFuture<TimelineData> getAggregatedTimelineData() {
        CompletableFuture<List<QuartoDTO>> quartosFuture = quartoService.buscarQuartos();
        CompletableFuture<List<HospedeDTO>> hospedesFuture = hospedeService.buscarHospedes();
        CompletableFuture<List<ReservaDTO>> reservasFuture = reservaService.buscarReservas();

        return CompletableFuture.allOf(quartosFuture, hospedesFuture, reservasFuture)
                .thenApply(v -> {
                    TimelineData data = new TimelineData();
                    data.quartos = quartosFuture.join();
                    data.hospedes = hospedesFuture.join();
                    data.reservas = reservasFuture.join();

                    // Mapeia os nomes dos hóspedes para as reservas
                    Map<Long, String> hospedeMap = data.hospedes.stream()
                            .filter(h -> h.getId() != null)
                            .collect(Collectors.toMap(HospedeDTO::getId, HospedeDTO::getName, (a, b) -> a));

                    for (ReservaDTO reserva : data.reservas) {
                        if (reserva.getGuestId() != null && hospedeMap.containsKey(reserva.getGuestId())) {
                            reserva.setGuestName(hospedeMap.get(reserva.getGuestId()));
                        }
                    }

                    return data;
                });
    }
}
