package accenture.sharks.challenge.controller;

import accenture.sharks.challenge.dto.CaminoDTO;
import accenture.sharks.challenge.exceptions.AddCaminoException;
import accenture.sharks.challenge.service.ICaminoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caminos")
public class CaminoController {

    private final ICaminoService caminoService;

    public CaminoController(ICaminoService caminoService) {
        this.caminoService = caminoService;
    }

    @PostMapping
    public ResponseEntity<String> addCamino(@Valid @RequestBody CaminoDTO camino, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder("Error: ");
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(", ");
            });
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
    public void deleteCamino(@PathVariable Long idA, @PathVariable Long idB) {
        caminoService.deleteCamino(idA, idB);
    }

    @GetMapping("/{idA}")
    public List<CaminoDTO> getCaminosDirectosDesdeUnPunto(@PathVariable Long idA) {
        return caminoService.getCaminosDirectosDesdeUnPunto(idA);
    }



}
