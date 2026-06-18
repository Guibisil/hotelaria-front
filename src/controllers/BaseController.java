package controllers;

import services.ReservaService;
import java.util.concurrent.CompletionException;

public abstract class BaseController {
    
    public String extrairMensagemErro(Throwable ex) {
        Throwable causa = ex;
        if (causa instanceof CompletionException && causa.getCause() != null) {
            causa = causa.getCause();
        }

        if (causa instanceof ReservaService.BusinessRuleException) {
            return "Bloqueado pela Regra de Negócio:\n" + causa.getMessage();
        } else {
            return causa.getMessage();
        }
    }
    
    public boolean isBusinessRuleException(Throwable ex) {
        Throwable causa = ex;
        if (causa instanceof CompletionException && causa.getCause() != null) {
            causa = causa.getCause();
        }
        return causa instanceof ReservaService.BusinessRuleException;
    }
}
