package config;

import controllers.HospedeController;
import controllers.ReservaController;
import views.hospedes.TelaHospedes;
import views.reservas.TelaReservas;
import services.HospedeService;
import services.IHospedeService;
import services.IQuartoService;
import services.IReservaService;
import services.QuartoService;
import services.ReservaService;
import services.TimelineAggregatorService;

import controllers.NovaReservaController;

public class DIContainer {
    
    private static DIContainer instance;

    // Services
    private final IReservaService reservaService;
    private final IQuartoService quartoService;
    private final IHospedeService hospedeService;
    private final TimelineAggregatorService timelineAggregatorService;

    // Controllers
    private final HospedeController hospedeController;
    private final ReservaController reservaController;
    private final NovaReservaController novaReservaController;

    private DIContainer() {
        this.reservaService = new ReservaService();
        this.quartoService = new QuartoService();
        this.hospedeService = new HospedeService();
        this.timelineAggregatorService = new TimelineAggregatorService(quartoService, hospedeService, reservaService);
        
        this.hospedeController = new HospedeController(hospedeService);
        this.reservaController = new ReservaController(reservaService, timelineAggregatorService);
        this.novaReservaController = new NovaReservaController(hospedeService, quartoService, reservaService);
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

    public HospedeController getHospedeController() {
        return hospedeController;
    }

    public ReservaController getReservaController() {
        return reservaController;
    }

    public NovaReservaController getNovaReservaController() {
        return novaReservaController;
    }

    public TelaReservas criarTelaReservas() {
        TelaReservas tela = new TelaReservas();
        tela.setController(reservaController);
        tela.setNovaReservaController(novaReservaController);
        return tela;
    }

    public TelaHospedes criarTelaHospedes() {
        TelaHospedes tela = new TelaHospedes();
        tela.setController(hospedeController);
        return tela;
    }
}
