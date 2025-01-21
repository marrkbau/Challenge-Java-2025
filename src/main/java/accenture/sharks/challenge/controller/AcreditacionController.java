package accenture.sharks.challenge.controller;

import accenture.sharks.challenge.dto.AcreditacionDTO;
import accenture.sharks.challenge.service.IAcreditacionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acreditaciones")
public class AcreditacionController {

    private final IAcreditacionService acreditacionService;
    private static final Logger logger = LoggerFactory.getLogger(AcreditacionController.class);


    public AcreditacionController(IAcreditacionService acreditacionService) {
        this.acreditacionService = acreditacionService;
    }

    @PostMapping
    public ResponseEntity<String> acreditar(@RequestBody @Valid AcreditacionDTO acreditacion, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder("Error: ");
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(", ");
            });
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }
        logger.info("Generando acreditación para el punto de venta: {}", acreditacion.getIdPuntoDeVenta());
        acreditacionService.generarAcreditacion(acreditacion);
        return ResponseEntity.ok("Acreditación generada exitosamente");
    }


}
