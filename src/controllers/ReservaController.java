package controllers;

import enums.ReservaAction;
import services.TimelineAggregatorService;
import services.IReservaService;
import java.util.concurrent.CompletableFuture;

public class ReservaController extends BaseController {

    private final IReservaService reservaService;
    private final TimelineAggregatorService timelineAggregatorService;

    public ReservaController(IReservaService reservaService, TimelineAggregatorService timelineAggregatorService) {
        this.reservaService = reservaService;
        this.timelineAggregatorService = timelineAggregatorService;
    }

    public CompletableFuture<TimelineAggregatorService.TimelineData> carregarReservas() {
        return timelineAggregatorService.getAggregatedTimelineData();
    }

    public CompletableFuture<Void> realizarAcao(String id, ReservaAction acao) {
        return reservaService.realizarAcao(id, acao);
    }
}
