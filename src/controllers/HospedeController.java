package controllers;

import DTO.HospedesDTO;
import main.TelaHospedes;
import services.IHospedeService;
import services.ReservaService;

import javax.swing.*;
import java.util.concurrent.CompletionException;

public class HospedeController {

    private final IHospedeService hospedeService;
    private TelaHospedes telaHospedes;

    public HospedeController(IHospedeService hospedeService) {
        this.hospedeService = hospedeService;
    }

    public void setTelaHospedes(TelaHospedes telaHospedes) {
        this.telaHospedes = telaHospedes;
    }

    public void carregarHospedes() {
        if (telaHospedes == null) return;

        hospedeService.buscarHospedes()
                .thenAccept(hospedes -> {
                    SwingUtilities.invokeLater(() -> {
                        telaHospedes.atualizarTabela(hospedes);
                    });
                })
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        ex.printStackTrace();
                        telaHospedes.mostrarMensagemErro("Erro ao carregar hóspedes. Verifique o backend.", "Erro");
                    });
                    return null;
                });
    }

    public void cadastrarHospede(HospedesDTO hospede, Runnable onSuccess) {
        hospedeService.cadastrarHospede(hospede)
                .thenRun(() -> {
                    SwingUtilities.invokeLater(() -> {
                        if (onSuccess != null) onSuccess.run();
                        carregarHospedes();
                    });
                })
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        tratarErroAPI(ex, "cadastrar hóspede");
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
            JOptionPane.showMessageDialog(null, "Bloqueado pela Regra de Negócio:\n" + causa.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            causa.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao " + operacao + ": " + causa.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
