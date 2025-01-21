package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import accenture.sharks.challenge.model.Camino;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CaminoTest {

  private Camino camino;

  @BeforeEach
  public void setUp() {
    camino = new Camino();
  }

  @Test
  public void testGettersAndSetters() {
    Double costo = 100.50;
    camino.setCosto(costo);
    assertEquals(costo, camino.getCosto());

    camino.setIdA(1L);
    assertEquals(1L, camino.getIdA());

    camino.setIdB(2L);
    assertEquals(2L, camino.getIdB());
  }

  @Test
  public void testConstructor() {
    Long idA = 1L;
    Long idB = 2L;
    Double costo = 100.50;

    Camino camino = new Camino(idA, idB, costo);

    assertEquals(idA, camino.getIdA());
    assertEquals(idB, camino.getIdB());
    assertEquals(costo, camino.getCosto());
  }
}
