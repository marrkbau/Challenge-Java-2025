package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import accenture.sharks.challenge.dto.CaminoMinimoDTO;
import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CaminoMinimoDTOTest {

  private CaminoMinimoDTO caminoMinimoDTO;

  @BeforeEach
  public void setUp() {
    caminoMinimoDTO = new CaminoMinimoDTO();
  }

  @Test
  public void testGettersAndSetters() {
    PuntoDeVentaDTO puntoDeVenta1 = new PuntoDeVentaDTO(1L, "Punto 1");
    PuntoDeVentaDTO puntoDeVenta2 = new PuntoDeVentaDTO(2L, "Punto 2");
    List<PuntoDeVentaDTO> puntosDeVentas = Arrays.asList(puntoDeVenta1, puntoDeVenta2);
    caminoMinimoDTO.setPuntosDeVentas(puntosDeVentas);
    assertEquals(puntosDeVentas, caminoMinimoDTO.getPuntosDeVentas());

    Double costoTotal = 250.75;
    caminoMinimoDTO.setCostoTotal(costoTotal);
    assertEquals(costoTotal, caminoMinimoDTO.getCostoTotal());
  }
}
