package accenture.sharks.challenge.controller;

import accenture.sharks.challenge.dto.AcreditacionDTO;
import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.service.IAcreditacionService;
import accenture.sharks.challenge.service.IPuntoDeVentaService;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acreditaciones")
public class AcreditacionController {

    private final IAcreditacionService acreditacionService;
    private final IPuntoDeVentaService puntoDeVentaService;

    public AcreditacionController(IAcreditacionService acreditacionService, IPuntoDeVentaService puntoDeVentaService) {
        this.acreditacionService = acreditacionService;
        this.puntoDeVentaService = puntoDeVentaService;
    }

    @PostMapping
    public void acreditar(@RequestBody AcreditacionDTO acreditacion) {
        acreditacionService.generarAcreditacion(acreditacion);
    }

    private PuntoDeVentaDTO getPuntoDeVenta(Long id) {
        return puntoDeVentaService.getPuntoDeVenta(id);
    }

}
