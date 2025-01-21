package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PuntoDeVentaDTOTest {

  private PuntoDeVentaDTO puntoDeVentaDTO;

  @BeforeEach
  public void setUp() {
    puntoDeVentaDTO = new PuntoDeVentaDTO();
  }

  @Test
  public void testGettersAndSetters() {
    Long id = 1L;
    puntoDeVentaDTO.setId(id);
    assertEquals(id, puntoDeVentaDTO.getId());

    String nombre = "Punto de Venta 1";
    puntoDeVentaDTO.setNombre(nombre);
    assertEquals(nombre, puntoDeVentaDTO.getNombre());
  }

  @Test
  public void testConstructor() {
    Long id = 1L;
    String nombre = "Punto de Venta 1";

    PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO(id, nombre);

    assertEquals(id, puntoDeVentaDTO.getId());
    assertEquals(nombre, puntoDeVentaDTO.getNombre());

    puntoDeVentaDTO = new PuntoDeVentaDTO(nombre);

    assertEquals(nombre, puntoDeVentaDTO.getNombre());
  }


}
