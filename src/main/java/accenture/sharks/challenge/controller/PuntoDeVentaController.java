package accenture.sharks.challenge.controller;


import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.service.IPuntoDeVentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PuntoDeVentaDTO> addPuntoVenta(@RequestBody PuntoDeVentaDTO puntoDeVentaDTO) {
        logger.info("Agregando punto de venta: {}", puntoDeVentaDTO.getNombre());
        Long newId = puntoVentaService.generateNewId();
        puntoDeVentaDTO.setId(newId);

        puntoVentaService.addPuntoDeVenta(puntoDeVentaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(puntoDeVentaDTO);
    }


    @PutMapping
    public ResponseEntity<PuntoDeVentaDTO> updatePuntoVenta(@RequestBody PuntoDeVentaDTO puntoDeVenta) {
        logger.info("Actualizando punto de venta: {}", puntoDeVenta.getNombre());
        puntoVentaService.updatePuntoDeVenta(puntoDeVenta);
        return ResponseEntity.status(HttpStatus.OK).body(puntoDeVenta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PuntoDeVentaDTO> deletePuntoVenta(@PathVariable Long id) {
        logger.info("Eliminando punto de venta con id: {}", id);
        puntoVentaService.removePuntoDeVenta(id);
        return ResponseEntity.ok().build();
    }

}
