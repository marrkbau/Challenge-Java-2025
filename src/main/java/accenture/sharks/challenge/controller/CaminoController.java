package accenture.sharks.challenge.controller;

import accenture.sharks.challenge.dto.CaminoDTO;
import accenture.sharks.challenge.exceptions.AddCaminoException;
import accenture.sharks.challenge.dto.CaminoMinimoDTO;
import accenture.sharks.challenge.exceptions.DeleteCaminoException;
import accenture.sharks.challenge.service.ICaminoService;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/caminos")
@RestControllerAdvice(name = "Caminos")
@Tag(name = "Caminos", description = "Operaciones relacionadas con los caminos entre los puntos de venta")
public class CaminoController {

    private final ICaminoService caminoService;
    private static final Logger logger = LoggerFactory.getLogger(CaminoController.class);


    public CaminoController(ICaminoService caminoService) {
        this.caminoService = caminoService;
    }

    @Operation(summary = "Agrega un camino entre dos puntos de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Camino agregado exitosamente"),
        @ApiResponse( responseCode = "400", description = "Error al agregar el camino"),
    })
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

    @Operation(summary = "Obtiene los caminos directos desde un punto de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Caminos obtenidos exitosamente"),
        @ApiResponse( responseCode = "404", description = "No se encontraron caminos para el punto de venta")
    })
    @GetMapping("/{idA}")
        public ResponseEntity<List<CaminoDTO>> getCaminosDirectosDesdeUnPunto(@PathVariable Long idA) {
        logger.info("Obteniendo caminos directos desde el punto {}", idA);
        List<CaminoDTO> caminosDTOS = caminoService.getCaminosDirectosDesdeUnPunto(idA);
        if (caminosDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(caminosDTOS);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(caminosDTOS);
        }
    }

    @Operation(summary = "Obtiene el camino de menor costo entre dos puntos de venta")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Camino de menor costo obtenido exitosamente"),
        @ApiResponse( responseCode = "400", description = "Error al obtener el camino de menor costo"),
        @ApiResponse( responseCode = "404", description = "No se encontró un camino entre los puntos")
    })
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
