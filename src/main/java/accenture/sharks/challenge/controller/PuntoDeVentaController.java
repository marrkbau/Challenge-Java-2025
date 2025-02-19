package accenture.sharks.challenge.controller;


import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.exceptions.PuntoDeVentaNotFoundException;
import accenture.sharks.challenge.service.IPuntoDeVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/puntos")
@RestControllerAdvice(name = "Puntos de venta")
@Tag(name = "Puntos de venta", description = "Operaciones relacionadas con los puntos de venta")
public class PuntoDeVentaController {

    private final IPuntoDeVentaService puntoVentaService;
    private static final Logger logger = LoggerFactory.getLogger(PuntoDeVentaController.class);


    public PuntoDeVentaController(IPuntoDeVentaService puntoVentaService) {
        this.puntoVentaService = puntoVentaService;
    }

    @Operation(summary = "Obtiene todos los puntos de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Puntos de venta obtenidos exitosamente")
    })
    @GetMapping
    public List<PuntoDeVentaDTO> getAllPuntosVenta() {
        logger.info("Obteniendo todos los puntos de venta");
        return puntoVentaService.getAllPuntosDeVenta();
    }

    @Operation(summary = "Obtiene un punto de venta por id")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Punto de venta obtenido exitosamente"),
        @ApiResponse( responseCode = "404", description = "No se encontró el punto de venta")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPuntoVenta(@PathVariable Long id) {
        logger.info("Obteniendo punto de venta con id: {}", id);
        try {
            PuntoDeVentaDTO puntoDeVentaDTO = puntoVentaService.getPuntoDeVenta(id);
            return ResponseEntity.status(HttpStatus.OK).body(puntoDeVentaDTO);
        } catch (PuntoDeVentaNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Punto de venta con id: " + id + " no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Agrega un punto de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "201", description = "Punto de venta creado exitosamente"),
        @ApiResponse( responseCode = "400", description = "Error al agregar el punto de venta"),
    })
    @PostMapping
    public ResponseEntity<String> addPuntoVenta(@RequestBody @Valid PuntoDeVentaDTO puntoDeVentaDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.error("Error agregando punto de venta");
            StringBuilder errorMessages = new StringBuilder("Error: ");
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(", "));
            return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

        logger.info("Agregando punto de venta: {}", puntoDeVentaDTO.getNombre());
        puntoDeVentaDTO.setActivo(true);

        puntoVentaService.addPuntoDeVenta(puntoDeVentaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Punto de venta creado, nombre: " + puntoDeVentaDTO.getNombre());
    }


    @Operation(summary = "Actualiza un punto de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Punto de venta actualizado exitosamente"),
        @ApiResponse( responseCode = "400", description = "Error al actualizar el punto de venta"),
    })
    @PutMapping
    public ResponseEntity<String> updatePuntoVenta(@RequestBody @Valid PuntoDeVentaDTO puntoDeVenta, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.error("Error actualizando punto de venta");
            StringBuilder errorMessages = new StringBuilder("Error: ");
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(", "));
            return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

        logger.info("Actualizando punto de venta: {}", puntoDeVenta.getNombre());
        try {
           puntoVentaService.updatePuntoDeVenta(puntoDeVenta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }


        return ResponseEntity.status(HttpStatus.OK).body("Punto de venta actualizado, nombre: " + puntoDeVenta.getNombre());
    }

    @Operation(summary = "Da de baja un punto de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Punto de venta dado de baja exitosamente"),
        @ApiResponse( responseCode = "404", description = "No se encontró el punto de venta"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePuntoVenta(@PathVariable Long id) {
        logger.info("Dando de baja punto de venta con id: {}", id);
        try {
            puntoVentaService.removePuntoDeVenta(id);

        } catch (PuntoDeVentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Punto de venta dado de baja con id: " + id);
    }

}
