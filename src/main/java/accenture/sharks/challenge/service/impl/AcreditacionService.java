package accenture.sharks.challenge.service.impl;

import accenture.sharks.challenge.dto.AcreditacionDTO;
import accenture.sharks.challenge.exceptions.PuntoDeVentaNotFoundException;
import accenture.sharks.challenge.model.Acreditacion;
import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.repository.AcreditacionRepository;
import accenture.sharks.challenge.service.IAcreditacionService;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AcreditacionService implements IAcreditacionService {

    private final ModelMapper modelMapper;
    private final AcreditacionRepository acreditacionRepository;
    private final HashOperations<String, Object, PuntoDeVenta> hashOperations;


    public AcreditacionService(ModelMapper modelMapper, RedisTemplate<String, Object> redisTemplate, AcreditacionRepository acreditacionRepository) {
        this.modelMapper = modelMapper;
        this.acreditacionRepository = acreditacionRepository;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void generarAcreditacion(AcreditacionDTO acreditacionDTO) {
        Acreditacion acreditacion = toEntity(acreditacionDTO);
        acreditacion.setFechaAcreditacion(LocalDateTime.now());

        try {
            String nombre = hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), acreditacion.getIdPuntoDeVenta().toString()).getNombre();
            acreditacion.setNombrePuntoDeVenta(nombre);
        } catch (NullPointerException e) {
            throw new PuntoDeVentaNotFoundException("El punto de venta no existe");
        }

        acreditacionRepository.save(acreditacion);
    }

    @Override
    public List<AcreditacionDTO> getAcreditacionesByIdPuntoDeVenta(Long id) {
        List<Acreditacion> acreditaciones = acreditacionRepository.findAllByIdPuntoDeVenta(id);
        return acreditaciones.stream().map(this::toDTO).toList();
    }


    private Acreditacion toEntity(AcreditacionDTO acreditacionDTO) {
        return modelMapper.map(acreditacionDTO, Acreditacion.class);
    }

    private AcreditacionDTO toDTO(Acreditacion acreditacion) {
        return modelMapper.map(acreditacion, AcreditacionDTO.class);
    }

}
