package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import accenture.sharks.challenge.model.Acreditacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AcreditacionTest {

  private Acreditacion acreditacion;

  @BeforeEach
  public void setUp() {
    // Inicializar la instancia de Acreditacion antes de cada prueba
    acreditacion = new Acreditacion();
  }

  @Test
  public void testGettersAndSetters() {
    acreditacion.setId(1L);
    assertEquals(1L, acreditacion.getId());

    BigDecimal importe = BigDecimal.valueOf(5000);
    acreditacion.setImporte(importe);
    assertEquals(importe, acreditacion.getImporte());

    LocalDateTime fecha = LocalDateTime.now();
    acreditacion.setFechaAcreditacion(fecha);
    assertEquals(fecha, acreditacion.getFechaAcreditacion());

    String nombrePuntoDeVenta = "Punto de Venta 1";
    acreditacion.setNombrePuntoDeVenta(nombrePuntoDeVenta);
    assertEquals(nombrePuntoDeVenta, acreditacion.getNombrePuntoDeVenta());

    acreditacion.setIdPuntoDeVenta(2L);
    assertEquals(2L, acreditacion.getIdPuntoDeVenta());
  }
}