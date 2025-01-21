package accenture.sharks.challenge;



import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import accenture.sharks.challenge.dto.AcreditacionDTO;
import accenture.sharks.challenge.exceptions.PuntoDeVentaNotFoundException;
import accenture.sharks.challenge.model.Acreditacion;
import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.repository.AcreditacionRepository;
import accenture.sharks.challenge.service.impl.AcreditacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AcreditacionServiceTest {

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock
  private HashOperations<String, Object, Object> hashOperations;

  @Mock
  private AcreditacionRepository acreditacionRepository;

  private AcreditacionService acreditacionService;

  private AcreditacionDTO acreditacionDTO;
  private Acreditacion acreditacion;

  @BeforeEach
  void setUp() {
    acreditacionDTO = new AcreditacionDTO();
    acreditacionDTO.setIdPuntoDeVenta(1L);

    acreditacion = new Acreditacion();
    acreditacion.setIdPuntoDeVenta(1L);
    acreditacion.setFechaAcreditacion(LocalDateTime.now());
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    acreditacionService = new AcreditacionService(modelMapper, redisTemplate, acreditacionRepository);
  }

  @Test
  void testGenerarAcreditacion_Success() {

    when(modelMapper.map(acreditacionDTO, Acreditacion.class)).thenReturn(acreditacion);
    when(hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1"))
        .thenReturn(new PuntoDeVenta("Rawson"));

    acreditacionService.generarAcreditacion(acreditacionDTO);

    assertEquals("Rawson", acreditacion.getNombrePuntoDeVenta());
    verify(acreditacionRepository, times(1)).save(acreditacion);
  }

  @Test
  void testGenerarAcreditacion_PuntoDeVentaNotFound() {
    when(modelMapper.map(acreditacionDTO, Acreditacion.class)).thenReturn(acreditacion);
    when(hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1"))
        .thenReturn(null);

    PuntoDeVentaNotFoundException exception = assertThrows(PuntoDeVentaNotFoundException.class,
        () -> acreditacionService.generarAcreditacion(acreditacionDTO));

    assertEquals("El punto de venta no existe", exception.getMessage());
    verify(acreditacionRepository, never()).save(any(Acreditacion.class));
  }
}

