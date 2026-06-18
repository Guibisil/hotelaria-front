package controllers;

import DTO.HospedesDTO;
import DTO.QuartoDTO;
import DTO.ReservaDTO;
import enums.ReservaAction;
import main.TelaReservas;
import services.HospedeService;
import services.IHospedeService;
import services.IQuartoService;
import services.IReservaService;
import services.QuartoService;
import services.ReservaService;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class ReservaController {

    private final IReservaService reservaService;
    private final IQuartoService quartoService;
    private final IHospedeService hospedeService;
    private final TelaReservas view;

    public ReservaController(TelaReservas view) {
        this.view = view;
        this.reservaService = new ReservaService();
        this.quartoService = new QuartoService();
        this.hospedeService = new HospedeService();
    }

    public ReservaController(TelaReservas view, IReservaService reservaService, IQuartoService quartoService, IHospedeService hospedeService) {
        this.view = view;
        this.reservaService = reservaService;
        this.quartoService = quartoService;
        this.hospedeService = hospedeService;
    }

    public void carregarReservas() {
        CompletableFuture<List<QuartoDTO>> quartosFuture = quartoService.buscarQuartos();
        CompletableFuture<List<HospedesDTO>> hospedesFuture = hospedeService.buscarHospedes();
        CompletableFuture<List<ReservaDTO>> reservasFuture = reservaService.buscarReservas();

        CompletableFuture.allOf(quartosFuture, hospedesFuture, reservasFuture)
                .thenRun(() -> {
                    List<QuartoDTO> quartos = quartosFuture.join();
                    List<HospedesDTO> hospedes = hospedesFuture.join();
                    List<ReservaDTO> reservas = reservasFuture.join();

                    Map<Long, String> hospedeMap = hospedes.stream()
                            .filter(h -> h.getId() != null)
                            .collect(Collectors.toMap(HospedesDTO::getId, HospedesDTO::getName, (a, b) -> a));

                    for (ReservaDTO reserva : reservas) {
                        if (reserva.getGuestId() != null && hospedeMap.containsKey(reserva.getGuestId())) {
                            reserva.setGuestName(hospedeMap.get(reserva.getGuestId()));
                        }
                    }

                    List<LocalDate> datas = new ArrayList<>();
                    LocalDate hoje = LocalDate.now();
                    for (int i = 0; i <= 60; i++) {
                        datas.add(hoje.plusDays(i));
                    }

                    SwingUtilities.invokeLater(() -> {
                        view.atualizarTabelaTimeline(quartos, datas, reservas);
                    });
                })
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        ex.printStackTrace();
                        view.mostrarMensagemErro("Erro ao carregar mapa de reservas. Verifique se o backend está rodando.", "Erro de Conexão");
                    });
                    return null;
                });
    }

    public void realizarAcao(String id, ReservaAction acao, Runnable onSuccess, Runnable onFinally) {
        reservaService.realizarAcao(id, acao)
                .thenRun(() -> SwingUtilities.invokeLater(() -> {
                    if (onFinally != null) onFinally.run();
                    if (onSuccess != null) onSuccess.run();
                    carregarReservas();
                }))
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        if (onFinally != null) onFinally.run();
                        tratarErroAPI(ex, "realizar " + acao.getValue());
                    });
                    return null;
                });
    }

    private void tratarErroAPI(Throwable ex, String operacao) {
        Throwable causa = ex;
        if (causa instanceof CompletionException && causa.getCause() != null) {
            causa = causa.getCause();
        }

        if (causa instanceof ReservaService.BusinessRuleException) {
            view.mostrarAviso("Bloqueado pela Regra de Negócio:\n" + causa.getMessage(), "Aviso");
        } else {
            causa.printStackTrace();
            view.mostrarMensagemErro("Erro ao " + operacao + ": " + causa.getMessage(), "Erro");
        }
    }
}
