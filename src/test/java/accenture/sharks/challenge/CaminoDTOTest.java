package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import accenture.sharks.challenge.dto.CaminoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CaminoDTOTest {

  private CaminoDTO caminoDTO;

  @BeforeEach
  public void setUp() {
    caminoDTO = new CaminoDTO();
  }

  @Test
  public void testGettersAndSetters() {
    Long idA = 1L;
    caminoDTO.setIdA(idA);
    assertEquals(idA, caminoDTO.getIdA());

    Long idB = 2L;
    caminoDTO.setIdB(idB);
    assertEquals(idB, caminoDTO.getIdB());

    Double costo = 100.50;
    caminoDTO.setCosto(costo);
    assertEquals(costo, caminoDTO.getCosto());
  }
}
