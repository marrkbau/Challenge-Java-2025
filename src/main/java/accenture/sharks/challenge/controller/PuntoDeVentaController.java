package accenture.sharks.challenge.controller;


import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.exceptions.IdMissingException;
import accenture.sharks.challenge.exceptions.PuntoDeVentaNotFoundException;
import accenture.sharks.challenge.service.IPuntoDeVentaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/puntos")
public class PuntoDeVentaController {

    private final IPuntoDeVentaService puntoVentaService;
    private static final Logger logger = LoggerFactory.getLogger(PuntoDeVentaController.class);


    public PuntoDeVentaController(IPuntoDeVentaService puntoVentaService) {
        this.puntoVentaService = puntoVentaService;
    }

    @GetMapping
    public List<PuntoDeVentaDTO> getAllPuntosVenta() {
        logger.info("Obteniendo todos los puntos de venta");
        return puntoVentaService.getAllPuntosDeVenta();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntoDeVentaDTO> getPuntoVenta(@PathVariable Long id) {
        logger.info("Obteniendo punto de venta con id: {}", id);
        PuntoDeVentaDTO puntoDeVentaDTO = puntoVentaService.getPuntoDeVenta(id);
        if (puntoDeVentaDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(puntoDeVentaDTO);
    }

    @PostMapping
    public ResponseEntity<String> addPuntoVenta(@RequestBody @Valid PuntoDeVentaDTO puntoDeVentaDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.error("Error agregando punto de venta");
            StringBuilder errorMessages = new StringBuilder("Error: ");
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(", ");
            });
            return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

        logger.info("Agregando punto de venta: {}", puntoDeVentaDTO.getNombre());
        Long newId = puntoVentaService.generateNewId();
        puntoDeVentaDTO.setId(newId);

        puntoVentaService.addPuntoDeVenta(puntoDeVentaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Punto de venta creado con id: " + newId);
    }


    @PutMapping
    public ResponseEntity<String> updatePuntoVenta(@RequestBody @Valid PuntoDeVentaDTO puntoDeVenta, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.error("Error actualizando punto de venta");
            StringBuilder errorMessages = new StringBuilder("Error: ");
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(", ");
            });
            return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

        logger.info("Actualizando punto de venta: {}", puntoDeVenta.getNombre());
        try {
            puntoVentaService.updatePuntoDeVenta(puntoDeVenta);
        } catch (IdMissingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Punto de venta actualizado, nuevo nombre: " + puntoDeVenta.getNombre());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePuntoVenta(@PathVariable Long id) {
        logger.info("Eliminando punto de venta con id: {}", id);
        try {
            puntoVentaService.removePuntoDeVenta(id);

        } catch (PuntoDeVentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Punto de venta eliminado con id: " + id);
    }

}
