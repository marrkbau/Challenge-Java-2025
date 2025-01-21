package accenture.sharks.challenge.service;

import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.model.PuntoDeVenta;

import java.util.List;

public interface IPuntoDeVentaService {

    List<PuntoDeVentaDTO> getAllPuntosDeVenta();

    public PuntoDeVentaDTO getPuntoDeVenta(Long id);

    void addPuntoDeVenta(PuntoDeVentaDTO puntoDeVenta);

    Long generateNewId();

    boolean updatePuntoDeVenta(PuntoDeVentaDTO puntoVenta);

    void removePuntoDeVenta(Long id);

}
