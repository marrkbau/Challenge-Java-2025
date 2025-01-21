package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import accenture.sharks.challenge.model.PuntoDeVenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PuntoDeVentaTest {

  private PuntoDeVenta puntoDeVenta;

  @BeforeEach
  public void setUp() {
    puntoDeVenta = new PuntoDeVenta();
  }

  @Test
  public void testGettersAndSetters() {
    puntoDeVenta.setId(1L);
    assertEquals(1L, puntoDeVenta.getId());

    String nombre = "Punto de Venta 1";
    puntoDeVenta.setNombre(nombre);
    assertEquals(nombre, puntoDeVenta.getNombre());
  }
}
