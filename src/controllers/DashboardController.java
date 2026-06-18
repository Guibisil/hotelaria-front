package controllers;

import DTO.QuartoDTO;
import DTO.ReservaDTO;
import services.TimelineAggregatorService;
import services.TimelineAggregatorService.TimelineData;
import services.IReservaService;
import enums.ReservaAction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

public class DashboardController extends BaseController {

    private final TimelineAggregatorService timelineAggregatorService;
    private final IReservaService reservaService;

    public DashboardController(TimelineAggregatorService timelineAggregatorService, IReservaService reservaService) {
        this.timelineAggregatorService = timelineAggregatorService;
        this.reservaService = reservaService;
    }

    public CompletableFuture<TimelineData> carregarDadosDashboard() {
        return timelineAggregatorService.getAggregatedTimelineData().thenApply(data -> {
            for (ReservaDTO reserva : data.reservas) {
                QuartoDTO quarto = data.quartos.stream().filter(q -> q.getId() != null && q.getId() == reserva.getRoomId()).findFirst().orElse(null);
                if (quarto != null) {
                    reserva.setRoomNumber(quarto.getNumber());
                    reserva.setRoomDailyRate(quarto.getBaseDailyRate());
                }
            }
            return data;
        });
    }

    public List<ReservaDTO> getCheckinsDoDia(TimelineData data) {
        String hojeStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return data.reservas.stream()
            .filter(r -> hojeStr.equals(r.getCheckinDate()) && "SCHEDULED".equalsIgnoreCase(r.getStatus()))
            .collect(Collectors.toList());
    }

    public List<ReservaDTO> getCheckoutsDoDia(TimelineData data) {
        String hojeStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return data.reservas.stream()
            .filter(r -> hojeStr.equals(r.getCheckoutDate()) && "IN_PROGRESS".equalsIgnoreCase(r.getStatus()))
            .collect(Collectors.toList());
    }

    public CompletableFuture<Void> realizarCheckin(Long reservaId) {
        return reservaService.realizarAcao(String.valueOf(reservaId), ReservaAction.CHECKIN);
    }

    public CompletableFuture<Void> realizarCheckout(Long reservaId) {
        return reservaService.realizarAcao(String.valueOf(reservaId), ReservaAction.CHECKOUT);
    }
}
