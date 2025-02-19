package accenture.sharks.challenge;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import accenture.sharks.challenge.dto.CaminoDTO;
import accenture.sharks.challenge.dto.CaminoMinimoDTO;
import accenture.sharks.challenge.exceptions.AddCaminoException;
import accenture.sharks.challenge.exceptions.DeleteCaminoException;
import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.Camino;
import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.repository.CaminoRepository;
import accenture.sharks.challenge.service.impl.CaminoService;
import org.hibernate.sql.Delete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CaminoServiceTest {

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock
  private HashOperations<String, Object, Object> hashCamino;

  @Mock
  private HashOperations<String, Object, Object> hashPuntoDeVenta;

  @Mock
  private CaminoRepository caminoRepository;

  @Mock
  private ModelMapper modelMapper;

  private CaminoService caminoService;

  @BeforeEach
  void setUp() {
    when(redisTemplate.opsForHash()).thenReturn(hashCamino);
    caminoService = new CaminoService(redisTemplate, modelMapper, caminoRepository);
  }

  @Test
  void addCamino_ShouldAddCaminoSuccessfully() {
    CaminoDTO caminoDTO = new CaminoDTO(1L, 2L, 10.0);
    Camino camino = new Camino(1L, 2L, 10.0);
    String key = "1-2";

    when(modelMapper.map(caminoDTO, Camino.class)).thenReturn(camino);

    caminoService.addCamino(caminoDTO);

    verify(hashCamino).put(CacheEntries.CAMINOS.getValue(), key, camino);
  }

  @Test
  void addCamino_ShouldThrowException_WhenIdsAreEqual() {
    CaminoDTO caminoDTO = new CaminoDTO(1L, 1L, 10.0);

    AddCaminoException exception = assertThrows(AddCaminoException.class, () -> caminoService.addCamino(caminoDTO));

    assertEquals("No se puede agregar un camino entre un punto y si mismo", exception.getMessage());
  }

  @Test
  void addCamino_ShouldThrowException_WhenCostIsNegative() {
    CaminoDTO caminoDTO = new CaminoDTO(1L, 2L, -5.0);

    AddCaminoException exception = assertThrows(AddCaminoException.class, () -> caminoService.addCamino(caminoDTO));

    assertEquals("El costo de un camino no puede ser negativo", exception.getMessage());
  }

  @Test
  void deleteCamino_ShouldDeleteCaminoSuccessfully() {
    String key = "1-2";

    when(hashCamino.hasKey(CacheEntries.CAMINOS.getValue(), key)).thenReturn(true);

    caminoService.deleteCamino(1L, 2L);

    verify(hashCamino).delete(CacheEntries.CAMINOS.getValue(), key);
  }

  @Test
  void deleteCamino_ShouldThrowException_WhenCaminoDoesNotExist() {
    String key = "1-2";

    when(hashCamino.hasKey(CacheEntries.CAMINOS.getValue(), key)).thenReturn(false);

    DeleteCaminoException exception = assertThrows(DeleteCaminoException.class, () -> caminoService.deleteCamino(1L, 2L));

    assertEquals("No se encontro el camino entre los puntos 1 y 2", exception.getMessage());
  }

  @Test
  void getCaminosDirectosDesdeUnPunto_ShouldReturnCorrectCaminos() {
    Camino camino1 = new Camino(1L, 2L, 10.0);
    Camino camino2 = new Camino(2L, 3L, 15.0);
    Camino camino3 = new Camino(4L, 1L, 5.0);
    Map<Object, Object> caminos = Map.of(
        "1-2", camino1,
        "2-3", camino2,
        "1-4", camino3
    );

    when(hashCamino.entries(CacheEntries.CAMINOS.getValue())).thenReturn(caminos);
    when(modelMapper.map(camino1, CaminoDTO.class)).thenReturn(new CaminoDTO(1L, 2L, 10.0));
    when(modelMapper.map(camino3, CaminoDTO.class)).thenReturn(new CaminoDTO(4L, 1L, 5.0));

    List<CaminoDTO> result = caminoService.getCaminosDirectosDesdeUnPunto(1L);

    assertEquals(2, result.size());
  }

  @Test
  void getCaminoMenorCosto_ShouldReturnCorrectPath() {
    Camino camino1 = new Camino(1L, 2L, 10.0);
    Camino camino2 = new Camino(2L, 3L, 15.0);
    Camino camino3 = new Camino(1L, 3L, 25.0);
    Map<Object, Object> caminos = Map.of(
        "1-2", camino1,
        "2-3", camino2,
        "1-3", camino3
    );

    PuntoDeVenta punto1 = new PuntoDeVenta(1L, "Punto 1", true);
    PuntoDeVenta punto2 = new PuntoDeVenta(2L, "Punto 2", true);
    PuntoDeVenta punto3 = new PuntoDeVenta(3L, "Punto 3", true);

    when(hashCamino.entries(CacheEntries.CAMINOS.getValue())).thenReturn(caminos);
    when(hashPuntoDeVenta.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1")).thenReturn(punto1);
    when(hashPuntoDeVenta.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), "2")).thenReturn(punto2);
    when(hashPuntoDeVenta.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), "3")).thenReturn(punto3);

    CaminoMinimoDTO result = caminoService.getCaminoMenorCosto(1L, 3L);

    assertNotNull(result);
    assertEquals(25.0, result.getCostoTotal());
    assertEquals(2, result.getPuntosDeVentas().size());
  }
}

