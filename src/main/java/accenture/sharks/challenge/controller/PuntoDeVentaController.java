package accenture.sharks.challenge.controller;


import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.service.PuntoDeVentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/puntos")
public class PuntoDeVentaController {

    private final PuntoDeVentaService puntoVentaService;
    private static final Logger logger = LoggerFactory.getLogger(PuntoDeVentaController.class);


    public PuntoDeVentaController(PuntoDeVentaService puntoVentaService) {
        this.puntoVentaService = puntoVentaService;
    }

    @GetMapping
    public List<PuntoDeVenta> getAllPuntosVenta() {
        logger.info("Obteniendo todos los puntos de venta");
        return puntoVentaService.getAllPuntosDeVenta();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntoDeVenta> getPuntoVenta(@PathVariable Long id) {
        logger.info("Obteniendo punto de venta con id: {}", id);
        PuntoDeVenta puntoDeVenta = puntoVentaService.getPuntoDeVenta(id);
        if (puntoDeVenta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(puntoDeVenta);
    }

    @PostMapping
    public ResponseEntity<PuntoDeVenta> addPuntoVenta(@RequestBody PuntoDeVenta puntoDeVenta) {
        logger.info("Agregando punto de venta: {}", puntoDeVenta.getNombre());
        Long newId = puntoVentaService.generateNewId();
        puntoDeVenta.setId(newId);

        puntoVentaService.addPuntoDeVenta(puntoDeVenta);

        return ResponseEntity.status(HttpStatus.CREATED).body(puntoDeVenta);
    }


    @PutMapping
    public ResponseEntity<PuntoDeVenta> updatePuntoVenta(@RequestBody PuntoDeVenta puntoVenta) {
        logger.info("Actualizando punto de venta: {}", puntoVenta.getNombre());
        puntoVentaService.updatePuntoDeVenta(puntoVenta);
        return ResponseEntity.status(HttpStatus.OK).body(puntoVenta);
    }

}
