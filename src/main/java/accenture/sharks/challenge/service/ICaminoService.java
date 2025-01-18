package accenture.sharks.challenge.service;

import accenture.sharks.challenge.dto.CaminoDTO;
import accenture.sharks.challenge.model.Camino;

import java.util.List;

public interface ICaminoService {

    void addCamino(CaminoDTO camino);

    void deleteCamino(Long idA, Long idB);

    List<CaminoDTO> getCaminosDirectosDesdeUnPunto(Long idA);

    CaminoDTO getCaminoMenorCosto(Long idA, Long idB);

}
