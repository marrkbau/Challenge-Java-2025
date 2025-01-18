package accenture.sharks.challenge.controller;

import accenture.sharks.challenge.model.Camino;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caminos")
public class CaminoController {

    @GetMapping("/{id}")
    public Camino obetenerCaminoPorId(@PathVariable Long id) {

        return new Camino();
    }

}
