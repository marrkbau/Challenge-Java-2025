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

@Service
public class AcreditacionService implements IAcreditacionService {

    private final ModelMapper modelMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, PuntoDeVenta> hashOperations;
    private final AcreditacionRepository acreditacionRepository;



    public AcreditacionService(ModelMapper modelMapper, RedisTemplate<String, Object> redisTemplate, AcreditacionRepository acreditacionRepository) {
        this.modelMapper = modelMapper;
        this.hashOperations = redisTemplate.opsForHash();
        this.redisTemplate = redisTemplate;
        this.acreditacionRepository = acreditacionRepository;
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


    private Acreditacion toEntity(AcreditacionDTO acreditacionDTO) {
        return modelMapper.map(acreditacionDTO, Acreditacion.class);
    }

}
