package accenture.sharks.challenge.controller;

import accenture.sharks.challenge.dto.CaminoDTO;
import accenture.sharks.challenge.exceptions.AddCaminoException;
import accenture.sharks.challenge.dto.CaminoMinimoDTO;
import accenture.sharks.challenge.exceptions.DeleteCaminoException;
import accenture.sharks.challenge.service.ICaminoService;
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
@RequestMapping("/caminos")
public class CaminoController {

    private final ICaminoService caminoService;
    private static final Logger logger = LoggerFactory.getLogger(CaminoController.class);


    public CaminoController(ICaminoService caminoService) {
        this.caminoService = caminoService;
    }

    @PostMapping
    public ResponseEntity<String> addCamino(@Valid @RequestBody CaminoDTO camino, BindingResult bindingResult) {
        logger.info("Agregando camino entre los puntos {} y {}", camino.getIdA(), camino.getIdB());
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder("Error: ");
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(", "));
            return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            caminoService.addCamino(camino);
            return new ResponseEntity<>("Camino agregado exitosamente", HttpStatus.OK);
        } catch (AddCaminoException e) {
            return new ResponseEntity<>("Error al agregar el camino: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{idA}/{idB}")
    public ResponseEntity<String> deleteCamino(@PathVariable Long idA, @PathVariable Long idB) {
        logger.info("Eliminando camino entre los puntos {} y {}", idA, idB);
        try {
            caminoService.deleteCamino(idA, idB);
            return new ResponseEntity<>("Camino eliminado exitosamente", HttpStatus.OK);
        } catch (DeleteCaminoException e) {
            logger.error("Error al eliminar el camino: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idA}")
        public List<CaminoDTO> getCaminosDirectosDesdeUnPunto(@PathVariable Long idA) {
        logger.info("Obteniendo caminos directos desde el punto {}", idA);
        return caminoService.getCaminosDirectosDesdeUnPunto(idA);
    }

    @GetMapping("/{idA}/{idB}")
    public ResponseEntity<?> getCaminoMenorCosto(@PathVariable Long idA, @PathVariable Long idB) {
        logger.info("Obteniendo camino de menor costo entre los puntos {} y {}", idA, idB);
        CaminoMinimoDTO camino;
        try {
            camino = caminoService.getCaminoMenorCosto(idA, idB);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener el camino de menor costo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        if (camino == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se encontró un camino entre los puntos " + idA + " y " + idB);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.status(HttpStatus.OK).body(camino);
    }




}
