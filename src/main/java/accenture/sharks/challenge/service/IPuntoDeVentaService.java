package accenture.sharks.challenge.service;

import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.model.PuntoDeVenta;

import java.util.List;

public interface IPuntoDeVentaService {

    List<PuntoDeVentaDTO> getAllPuntosDeVenta();

    PuntoDeVentaDTO getPuntoDeVenta(Long id);

    void addPuntoDeVenta(PuntoDeVentaDTO puntoDeVenta);

    void updatePuntoDeVenta(PuntoDeVentaDTO puntoVenta);

    void removePuntoDeVenta(Long id);

}
