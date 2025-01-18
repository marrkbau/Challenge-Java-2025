package accenture.sharks.challenge.service.impl;

import accenture.sharks.challenge.dto.CaminoDTO;
import accenture.sharks.challenge.exceptions.AddCaminoException;
import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.Camino;
import accenture.sharks.challenge.service.ICaminoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CaminoService implements ICaminoService {

    private final HashOperations<String, String, Camino> hashOperations;
    private final ModelMapper modelMapper;


    public CaminoService(RedisTemplate<String, Object> redisTemplate, ModelMapper modelMapper) {
        this.hashOperations = redisTemplate.opsForHash();
        this.modelMapper = modelMapper;
    }

    @Override
    public void addCamino(CaminoDTO caminoDTO) {
        if (caminoDTO.getIdA().equals(caminoDTO.getIdB())) {
            throw new AddCaminoException("No se puede agregar un camino entre un punto y si mismo");
        }

        if (caminoDTO.getCosto() < 0) {
            throw new AddCaminoException("El costo de un camino no puede ser negativo");
        }

        Camino camino = toEntity(caminoDTO);
        String key = generateKey(camino.getIdA(), camino.getIdB());
        hashOperations.put(CacheEntries.CAMINOS.getValue(), key, camino);
    }

    @Override
    public void deleteCamino(Long idA, Long idB) {
        String key = generateKey(idA, idB);
        hashOperations.delete(CacheEntries.CAMINOS.getValue(), key);
    }

    @Override
    public List<CaminoDTO> getCaminosDirectosDesdeUnPunto(Long idA) {
        Map<String, Camino> allCaminos = hashOperations.entries(CacheEntries.CAMINOS.getValue());
        List<CaminoDTO> result = new ArrayList<>();

        for (Camino camino : allCaminos.values()) {
            if (camino.getIdA().equals(idA) || camino.getIdB().equals(idA)) {
                CaminoDTO caminoDTO = toDTO(camino);
                result.add(caminoDTO);
            }
        }

        return result;
    }

    public Double costoEntrePuntos(Long idA, Long idB) {
        if (idA.equals(idB)) {
            return 0d;
        }

        return 0d;
    }

    @Override
    public CaminoDTO getCaminoMenorCosto(Long idA, Long idB) {
        return null;
    }

    /**
     * Genera una clave para almacenar un camino en el hash de Redis
     */
    private String generateKey(Long idA, Long idB) {
        return idA < idB ? idA + "-" + idB : idB + "-" + idA;
    }

    private CaminoDTO toDTO(Camino camino) {
        return modelMapper.map(camino, CaminoDTO.class);
    }

    private Camino toEntity(CaminoDTO caminoDTO) {
        return modelMapper.map(caminoDTO, Camino.class);
    }


}
