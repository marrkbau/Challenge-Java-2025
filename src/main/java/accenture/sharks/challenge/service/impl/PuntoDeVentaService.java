package accenture.sharks.challenge.service.impl;

import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.exceptions.IdMissingException;
import accenture.sharks.challenge.exceptions.PuntoDeVentaNotFoundException;
import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.service.IPuntoDeVentaService;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class PuntoDeVentaService implements IPuntoDeVentaService {

    private final HashOperations<String, String, PuntoDeVenta> hashOperations;
    private final ModelMapper modelMapper;


    public PuntoDeVentaService(RedisTemplate<String, Object> redisTemplate, ModelMapper modelMapper) {
        this.hashOperations = redisTemplate.opsForHash();
        this.modelMapper = modelMapper;
    }

    /**
     * Retorna todos los puntos de venta que estan en el Hash de Redis
     * Aclaración: Los ordena por ID, ya que el hash de redis no garantiza el orden de inserción
     */
    @Override
    public List<PuntoDeVentaDTO> getAllPuntosDeVenta() {
        List<PuntoDeVenta> puntosDeVenta = hashOperations.values(CacheEntries.PUNTOS_DE_VENTA.getValue());
        return puntosDeVenta.stream()
                .sorted(Comparator.comparing(PuntoDeVenta::getId))
                .map(this::toDTO).toList();
    }

    /**
     * Retorna un punto de venta por su ID
     */
    @Override
    public PuntoDeVentaDTO getPuntoDeVenta(Long id) {
        PuntoDeVenta puntoDeVenta = hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), id.toString());
        try {
            puntoDeVenta = hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), id.toString());
            return toDTO(puntoDeVenta);
        } catch (Exception e) {
            throw new PuntoDeVentaNotFoundException("No existe punto de venta con id: " + id);
        }
    }

    /**
     * Agrega un punto de venta al hash de Redis
     */
    @Override
    public void addPuntoDeVenta(PuntoDeVentaDTO puntoDeVentaDTO) {
        if (puntoDeVentaDTO.getId() == null) {
            puntoDeVentaDTO.setId(generateNewId());
        }

        PuntoDeVenta puntoDeVenta = toEntity(puntoDeVentaDTO);
        hashOperations.put(CacheEntries.PUNTOS_DE_VENTA.getValue(), puntoDeVenta.getId().toString(), puntoDeVenta);
    }


    /**
     * Genera un nuevo ID para un punto de venta
     */
    @Override
    public Long generateNewId() {
        List<PuntoDeVenta> allPuntos = hashOperations.values(CacheEntries.PUNTOS_DE_VENTA.getValue());

        return allPuntos.stream()
                .mapToLong(PuntoDeVenta::getId)
                .max()
                .orElse(0L) + 1;
    }

    /**
     * Actualiza un punto de venta en el hash de Redis
     * Si el punto de venta no existe, se añade.
     */
    @Override
    public boolean updatePuntoDeVenta(PuntoDeVentaDTO puntoDeVentaDTO) {

        boolean puntoDeVentaExistente;

        if(puntoDeVentaDTO.getId() != null) {
            puntoDeVentaExistente = hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), puntoDeVentaDTO.getId().toString()) != null;
            PuntoDeVenta puntoDeVenta = toEntity(puntoDeVentaDTO);
            hashOperations.put(CacheEntries.PUNTOS_DE_VENTA.getValue(), puntoDeVenta.getId().toString(), puntoDeVenta);
        } else {
            throw new IdMissingException("El ID del punto de venta es requerido para actualizarlo");
        }

        return puntoDeVentaExistente;
    }

    /**
     * Elimina un punto de venta del hash de Redis
     */
    @Override
    public void removePuntoDeVenta(Long id) {
        Long deletedCount = hashOperations.delete(CacheEntries.PUNTOS_DE_VENTA.getValue(), id.toString());
        if (deletedCount.equals(0L)) {
            throw new PuntoDeVentaNotFoundException("No existe punto de venta con id: " + id);
        }
    }


    private PuntoDeVentaDTO toDTO(PuntoDeVenta puntoDeVenta) {
        return modelMapper.map(puntoDeVenta, PuntoDeVentaDTO.class);
    }

    private PuntoDeVenta toEntity(PuntoDeVentaDTO puntoDeVentaDTO) {
        return modelMapper.map(puntoDeVentaDTO, PuntoDeVenta.class);
    }

}
