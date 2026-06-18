package controllers;

import DTO.HospedeDTO;
import DTO.QuartoDTO;
import DTO.ReservaDTO;
import services.IHospedeService;
import services.IQuartoService;
import services.IReservaService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NovaReservaController extends BaseController {

    private final IHospedeService hospedeService;
    private final IQuartoService quartoService;
    private final IReservaService reservaService;

    public NovaReservaController(IHospedeService hospedeService, IQuartoService quartoService, IReservaService reservaService) {
        this.hospedeService = hospedeService;
        this.quartoService = quartoService;
        this.reservaService = reservaService;
    }

    public CompletableFuture<List<HospedeDTO>> buscarHospedes() {
        return hospedeService.buscarHospedes();
    }

    public CompletableFuture<List<QuartoDTO>> buscarQuartosDisponiveis(java.time.LocalDate checkin, java.time.LocalDate checkout) {
        return quartoService.buscarQuartos().thenCombine(reservaService.buscarReservas(), (quartos, reservas) -> {
            return quartos.stream().filter(q -> {
                boolean hasOverlap = reservas.stream().anyMatch(r -> {
                    if (r.getRoomId() == q.getId().intValue()) {
                        if ("CANCELLED".equalsIgnoreCase(r.getStatus())) return false;
                        if ("FINISHED".equalsIgnoreCase(r.getStatus())) return false; // Finished shouldn't overlap unless it's a past record anyway
                        
                        try {
                            java.time.LocalDate rCheckin = java.time.LocalDate.parse(r.getCheckinDate());
                            java.time.LocalDate rCheckout = java.time.LocalDate.parse(r.getCheckoutDate());
                            
                            // A reservation overlaps if its checkin is BEFORE the requested checkout,
                            // AND its checkout is AFTER the requested checkin.
                            return rCheckin.isBefore(checkout) && rCheckout.isAfter(checkin);
                        } catch (Exception e) {
                            return false;
                        }
                    }
                    return false;
                });
                return !hasOverlap;
            }).toList();
        });
    }

    public CompletableFuture<Void> salvarReserva(ReservaDTO reserva) {
        return reservaService.criarReserva(reserva);
    }
}
