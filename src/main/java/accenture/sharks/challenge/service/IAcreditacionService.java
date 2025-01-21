package accenture.sharks.challenge.service;

import accenture.sharks.challenge.dto.AcreditacionDTO;

import java.util.List;

public interface IAcreditacionService {

    void generarAcreditacion(AcreditacionDTO acreditacionDTO);

    List<AcreditacionDTO> getAcreditacionesByIdPuntoDeVenta(Long id);

}
