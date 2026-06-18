package controllers;

import DTO.ReservaDTO;
import enums.ReservaAction;
import main.TelaReservas;
import services.IReservaService;
import services.ReservaService;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CompletionException;

public class ReservaController {

    private final IReservaService reservaService;
    private final TelaReservas view;

    public ReservaController(TelaReservas view) {
        this.view = view;
        this.reservaService = new ReservaService();
    }

    public ReservaController(TelaReservas view, IReservaService reservaService) {
        this.view = view;
        this.reservaService = reservaService;
    }

    public void carregarReservas() {
        reservaService.buscarReservas()
                .thenAccept(listaReservas -> SwingUtilities.invokeLater(() -> {
                    view.atualizarTabela(listaReservas);
                }))
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        ex.printStackTrace();
                        view.mostrarMensagemErro("Erro ao carregar reservas. Verifique se o backend está rodando.", "Erro de Conexão");
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
