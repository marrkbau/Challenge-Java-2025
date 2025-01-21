package accenture.sharks.challenge;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.exceptions.IdMissingException;
import accenture.sharks.challenge.exceptions.PuntoDeVentaNotFoundException;
import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.service.impl.PuntoDeVentaService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PuntoDeVentaServiceTest {

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock
  private HashOperations<String, Object, Object> hashOperations;

  PuntoDeVentaService puntoDeVentaService;


  @BeforeEach
  void setUp() {
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    puntoDeVentaService = new PuntoDeVentaService(redisTemplate, modelMapper);
  }

  @Test
  void testGetAllPuntosDeVenta() {
    PuntoDeVenta punto1 = new PuntoDeVenta(1L, "Tienda 1");
    PuntoDeVenta punto2 = new PuntoDeVenta(2L, "Tienda 2");
    when(hashOperations.values(CacheEntries.PUNTOS_DE_VENTA.getValue())).thenReturn(Arrays.asList(punto1, punto2));

    PuntoDeVentaDTO dto1 = new PuntoDeVentaDTO(1L, "Tienda 1");
    PuntoDeVentaDTO dto2 = new PuntoDeVentaDTO(2L, "Tienda 2");
    when(modelMapper.map(punto1, PuntoDeVentaDTO.class)).thenReturn(dto1);
    when(modelMapper.map(punto2, PuntoDeVentaDTO.class)).thenReturn(dto2);

    List<PuntoDeVentaDTO> result = puntoDeVentaService.getAllPuntosDeVenta();

    assertEquals(2, result.size());
    assertEquals(dto1, result.get(0));
    assertEquals(dto2, result.get(1));
  }

  @Test
  void testGetPuntoDeVenta() {
    PuntoDeVenta punto = new PuntoDeVenta(1L, "Tienda 1");
    when(hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1")).thenReturn(punto);

    PuntoDeVentaDTO dto = new PuntoDeVentaDTO(1L, "Tienda 1");
    when(modelMapper.map(punto, PuntoDeVentaDTO.class)).thenReturn(dto);

    PuntoDeVentaDTO result = puntoDeVentaService.getPuntoDeVenta(1L);

    assertNotNull(result);
    assertEquals(dto, result);
  }


  @Test
  void testAddPuntoDeVenta() {
    PuntoDeVentaDTO dto = new PuntoDeVentaDTO(null, "Tienda Nueva");
    PuntoDeVenta punto = new PuntoDeVenta(1L, "Tienda Nueva");

    when(hashOperations.values(CacheEntries.PUNTOS_DE_VENTA.getValue())).thenReturn(Collections.emptyList());
    when(modelMapper.map(dto, PuntoDeVenta.class)).thenReturn(punto);

    puntoDeVentaService.addPuntoDeVenta(dto);

    assertNotNull(dto.getId());
    verify(hashOperations).put(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1", punto);
  }

  @Test
  void testUpdatePuntoDeVenta_Success() {
    PuntoDeVentaDTO dto = new PuntoDeVentaDTO(1L, "Tienda Actualizada");
    PuntoDeVenta punto = new PuntoDeVenta(1L, "Tienda Actualizada");

    when(hashOperations.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1")).thenReturn(new PuntoDeVenta(1L, "Tienda Vieja"));
    when(modelMapper.map(dto, PuntoDeVenta.class)).thenReturn(punto);

    boolean result = puntoDeVentaService.updatePuntoDeVenta(dto);

    assertTrue(result);
    verify(hashOperations).put(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1", punto);
  }

  @Test
  void testUpdatePuntoDeVenta_IdMissing() {
    PuntoDeVentaDTO dto = new PuntoDeVentaDTO(null, "Tienda Nueva");

    assertThrows(IdMissingException.class, () -> puntoDeVentaService.updatePuntoDeVenta(dto));
    verify(hashOperations, never()).put(anyString(), anyString(), any(PuntoDeVenta.class));
  }

  @Test
  void testRemovePuntoDeVenta_Success() {
    when(hashOperations.delete(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1")).thenReturn(1L);

    puntoDeVentaService.removePuntoDeVenta(1L);

    verify(hashOperations).delete(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1");
  }

  @Test
  void testRemovePuntoDeVenta_NotFound() {
    when(hashOperations.delete(CacheEntries.PUNTOS_DE_VENTA.getValue(), "1")).thenReturn(0L);

    assertThrows(PuntoDeVentaNotFoundException.class, () -> puntoDeVentaService.removePuntoDeVenta(1L));
  }
}

