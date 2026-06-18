package config;

import controllers.HospedeController;
import main.TelaHospedes;
import main.TelaReservas;
import services.HospedeService;
import services.IHospedeService;
import services.IQuartoService;
import services.IReservaService;
import services.QuartoService;
import services.ReservaService;

public class DIContainer {
    
    private static DIContainer instance;

    // Services
    private final IReservaService reservaService;
    private final IQuartoService quartoService;
    private final IHospedeService hospedeService;

    private DIContainer() {
        this.reservaService = new ReservaService();
        this.quartoService = new QuartoService();
        this.hospedeService = new HospedeService();
    }

    public static DIContainer getInstance() {
        if (instance == null) {
            instance = new DIContainer();
        }
        return instance;
    }

    public IReservaService getReservaService() {
        return reservaService;
    }

    public IQuartoService getQuartoService() {
        return quartoService;
    }

    public IHospedeService getHospedeService() {
        return hospedeService;
    }

    public TelaReservas criarTelaReservas() {
        TelaReservas tela = new TelaReservas(reservaService, quartoService, hospedeService);
        return tela;
    }

    public TelaHospedes criarTelaHospedes() {
        TelaHospedes tela = new TelaHospedes();
        HospedeController controller = new HospedeController(hospedeService);
        controller.setTelaHospedes(tela);
        tela.setController(controller);
        return tela;
    }
}
