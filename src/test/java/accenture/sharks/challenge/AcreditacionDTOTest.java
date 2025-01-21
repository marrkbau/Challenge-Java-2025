package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import accenture.sharks.challenge.dto.AcreditacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class AcreditacionDTOTest {

  private AcreditacionDTO acreditacionDTO;

  @BeforeEach
  public void setUp() {
    acreditacionDTO = new AcreditacionDTO();
  }

  @Test
  public void testGettersAndSetters() {
    Long idPuntoDeVenta = 1L;
    acreditacionDTO.setIdPuntoDeVenta(idPuntoDeVenta);
    assertEquals(idPuntoDeVenta, acreditacionDTO.getIdPuntoDeVenta());

    BigDecimal importe = BigDecimal.valueOf(5000);
    acreditacionDTO.setImporte(importe);
    assertEquals(importe, acreditacionDTO.getImporte());

    String nombrePuntoDeVenta = "Punto de Venta 1";
    acreditacionDTO.setNombrePuntoDeVenta(nombrePuntoDeVenta);
    assertEquals(nombrePuntoDeVenta, acreditacionDTO.getNombrePuntoDeVenta());
  }

  @Test
  public void testConstructor() {
    Long idPuntoDeVenta = 1L;
    BigDecimal importe = BigDecimal.valueOf(100.50);
    String nombrePuntoDeVenta = "Punto de Venta 1";

    AcreditacionDTO acreditacionDTO = new AcreditacionDTO(idPuntoDeVenta, importe, nombrePuntoDeVenta);

    assertEquals(idPuntoDeVenta, acreditacionDTO.getIdPuntoDeVenta());
    assertEquals(importe, acreditacionDTO.getImporte());
    assertEquals(nombrePuntoDeVenta, acreditacionDTO.getNombrePuntoDeVenta());
  }
}
