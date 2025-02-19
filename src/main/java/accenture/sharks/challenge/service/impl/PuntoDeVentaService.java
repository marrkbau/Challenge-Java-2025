package accenture.sharks.challenge.service.impl;

import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.exceptions.IdMissingException;
import accenture.sharks.challenge.exceptions.PuntoDeVentaNotFoundException;
import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.repository.PuntoDeVentaRepository;
import accenture.sharks.challenge.service.IPuntoDeVentaService;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PuntoDeVentaService implements IPuntoDeVentaService {

    private final HashOperations<String, String, PuntoDeVenta> hashOperations;
    private final ModelMapper modelMapper;

    private final PuntoDeVentaRepository puntoDeVentaRepository;


    public PuntoDeVentaService(RedisTemplate<String, Object> redisTemplate, ModelMapper modelMapper, PuntoDeVentaRepository puntoDeVentaRepository) {
        this.hashOperations = redisTemplate.opsForHash();
        this.modelMapper = modelMapper;
        this.puntoDeVentaRepository = puntoDeVentaRepository;
    }

    /**
     * Retorna todos los puntos de venta que estan en el Hash de Redis
     * Si no hay puntos de venta en el Hash, se consultan de la base de datos y se guardan en el Hash
     */
    @Override
    public List<PuntoDeVentaDTO> getAllPuntosDeVenta() {
        List<PuntoDeVenta> puntosDeVenta = hashOperations.values(CacheEntries.PUNTOS_DE_VENTA.getValue());

        if (puntosDeVenta.isEmpty()) {
            puntosDeVenta = puntoDeVentaRepository.findAll();

            puntosDeVenta = puntosDeVenta.stream()
                .filter(PuntoDeVenta::isActivo)
                .toList();

            for (PuntoDeVenta puntoDeVenta : puntosDeVenta) {
                hashOperations.put(CacheEntries.PUNTOS_DE_VENTA.getValue(), puntoDeVenta.getId().toString(), puntoDeVenta);
            }
        } else {
            puntosDeVenta = puntosDeVenta.stream()
                .filter(PuntoDeVenta::isActivo)
                .toList();
        }

        return puntosDeVenta.stream()
            .sorted(Comparator.comparing(PuntoDeVenta::getId))
            .map(this::toDTO)
            .toList();
    }

    /**
     * Retorna un punto de venta por su ID
     * Si el punto de venta fue dado de baja, no se cachea en Redis
     */
    @Override
    public PuntoDeVentaDTO getPuntoDeVenta(Long id) {
        PuntoDeVenta puntoDeVenta = hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), id.toString());
        try{
            return toDTO(puntoDeVenta);
        } catch (IllegalArgumentException e) {
            puntoDeVenta = puntoDeVentaRepository.findById(id)
                .orElseThrow(() -> new PuntoDeVentaNotFoundException("No existe punto de venta con id: " + id));

            if(!puntoDeVenta.isActivo()) {
                hashOperations.put(CacheEntries.PUNTOS_DE_VENTA.getValue(), id.toString(), puntoDeVenta);
            }
            return toDTO(puntoDeVenta);
        }
    }


    /**
     * Agrega un punto de venta al hash de Redis y a la DB
     * Si el punto de venta ya existe, se actualiza.
     */
    @Override
    public void addPuntoDeVenta(PuntoDeVentaDTO puntoDeVentaDTO) {

        PuntoDeVenta puntoDeVenta = toEntity(puntoDeVentaDTO);
        puntoDeVenta = puntoDeVentaRepository.save(puntoDeVenta);
        hashOperations.put(CacheEntries.PUNTOS_DE_VENTA.getValue(), puntoDeVenta.getId().toString(), puntoDeVenta);

    }

    /**
     * Actualiza un punto de venta en el hash de Redis
     * Si el punto de venta no existe, se añade.
     */
    @Override
    public void updatePuntoDeVenta(PuntoDeVentaDTO puntoDeVentaDTO) {
        if (puntoDeVentaDTO.getId() == null) {
            throw new IdMissingException("El ID del punto de venta es requerido para actualizarlo");
        }

        PuntoDeVenta puntoDeVentaExistente = puntoDeVentaRepository.findById(puntoDeVentaDTO.getId())
            .orElseThrow(() -> new PuntoDeVentaNotFoundException("No existe punto de venta con id: " + puntoDeVentaDTO.getId()));

        if (puntoDeVentaDTO.getNombre() != null) {
            puntoDeVentaExistente.setNombre(puntoDeVentaDTO.getNombre());
        }

        if (puntoDeVentaDTO.isActivo() != null) {
            puntoDeVentaExistente.setActivo(puntoDeVentaDTO.isActivo());
        }

        hashOperations.put(CacheEntries.PUNTOS_DE_VENTA.getValue(), puntoDeVentaExistente.getId().toString(), puntoDeVentaExistente);
        puntoDeVentaRepository.save(puntoDeVentaExistente);
    }




    /**
     * Elimina un punto de venta del hash de Redis y lo desactiva en la base de datos
     * Si el punto de venta no existe, se lanza una excepción
     *
     */
    @Override
    public void removePuntoDeVenta(Long id) {
        Long deletedFromCache = hashOperations.delete(CacheEntries.PUNTOS_DE_VENTA.getValue(), id.toString());

        if (deletedFromCache.equals(0L)) {
            throw new PuntoDeVentaNotFoundException("No existe punto de venta con id: " + id + " en Redis");
        }

        Optional<PuntoDeVenta> puntoDeVentaOptional = puntoDeVentaRepository.findById(id);
        if (puntoDeVentaOptional.isEmpty()) {
            throw new PuntoDeVentaNotFoundException("No existe punto de venta con id: " + id + " en la base de datos");
        }

        PuntoDeVenta puntoDeVenta = puntoDeVentaOptional.get();

        puntoDeVenta.setActivo(false);
        puntoDeVentaRepository.save(puntoDeVenta);
    }



    private PuntoDeVentaDTO toDTO(PuntoDeVenta puntoDeVenta) {
        return modelMapper.map(puntoDeVenta, PuntoDeVentaDTO.class);
    }

    private PuntoDeVenta toEntity(PuntoDeVentaDTO puntoDeVentaDTO) {
        return modelMapper.map(puntoDeVentaDTO, PuntoDeVenta.class);
    }

}
