package accenture.sharks.challenge.controller;

import accenture.sharks.challenge.dto.AcreditacionDTO;
import accenture.sharks.challenge.service.IAcreditacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/acreditaciones")
@RestControllerAdvice(name = "Acreditaciones")
@Tag(name = "Acreditaciones", description = "Operaciones relacionadas con las acreditaciones de los puntos de venta")
public class AcreditacionController {

    private final IAcreditacionService acreditacionService;
    private static final Logger logger = LoggerFactory.getLogger(AcreditacionController.class);


    public AcreditacionController(IAcreditacionService acreditacionService) {
        this.acreditacionService = acreditacionService;
    }
    @Operation(summary = "Genera una acreditación para un punto de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Acreditación generada exitosamente"),
        @ApiResponse( responseCode = "400", description = "Error en los datos de la acreditación"),
        @ApiResponse( responseCode = "404", description = "No se encontró el punto de venta")
    })
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
        try {
            acreditacionService.generarAcreditacion(acreditacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el punto de venta con id: " + acreditacion.getIdPuntoDeVenta());
        }
        return ResponseEntity.ok("Acreditación generada exitosamente");
    }

    @Operation(summary = "Obtiene las acreditaciones de un punto de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Acreditaciones obtenidas exitosamente"),
        @ApiResponse( responseCode = "404", description = "No se encontraron acreditaciones para el punto de venta")
    })
    @GetMapping("/{idPuntoDeVenta}")
    public ResponseEntity<?> getAcreditaciones(@PathVariable Long idPuntoDeVenta) {
        logger.info("Obteniendo acreditaciones");
        List<AcreditacionDTO> acreditacionesDTO = acreditacionService.getAcreditacionesByIdPuntoDeVenta(idPuntoDeVenta);

        if(acreditacionesDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron acreditaciones para el punto de venta con id: " + idPuntoDeVenta);
        } else {
            return ResponseEntity.ok(acreditacionesDTO);
        }
    }

}
