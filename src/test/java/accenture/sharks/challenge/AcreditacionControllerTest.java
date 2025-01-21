package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import accenture.sharks.challenge.dto.AcreditacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcreditacionControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private String baseUrl;

  private AcreditacionDTO acreditacionDTO;

  @BeforeEach
  public void setUp() {
    baseUrl = "http://localhost:" + port + "/acreditaciones";
    acreditacionDTO = new AcreditacionDTO();
  }

  @Test
  public void agregarAcreditacion() {
    acreditacionDTO.setIdPuntoDeVenta(1L);
    acreditacionDTO.setImporte(BigDecimal.valueOf(100.50));

    String response = restTemplate.postForObject(baseUrl, acreditacionDTO, String.class);

    assertEquals("Acreditación generada exitosamente", response);
  }

  @Test
  public void agregarAcreditacionConErroresDeValidacion() {
    acreditacionDTO.setIdPuntoDeVenta(null);
    acreditacionDTO.setImporte(null);

    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, acreditacionDTO, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("El id del punto de venta no puede ser nulo"));
    assertTrue(response.getBody().contains("El importe no puede ser nulo"));
  }

  @Test
  public void obtenerAcreditaciones() {
    Long id = 1L;
    AcreditacionDTO[] acreditaciones = restTemplate.getForObject(baseUrl + "/" + id, AcreditacionDTO[].class);

    assertEquals(1, acreditaciones.length);
  }

}
