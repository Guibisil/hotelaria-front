package controllers;

import DTO.HospedeDTO;
import services.IHospedeService;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HospedeController extends BaseController {

    private final IHospedeService hospedeService;

    public HospedeController(IHospedeService hospedeService) {
        this.hospedeService = hospedeService;
    }

    public CompletableFuture<List<HospedeDTO>> carregarHospedes() {
        return hospedeService.buscarHospedes();
    }

    public CompletableFuture<Void> cadastrarHospede(HospedeDTO hospede) {
        return hospedeService.cadastrarHospede(hospede);
    }
}
