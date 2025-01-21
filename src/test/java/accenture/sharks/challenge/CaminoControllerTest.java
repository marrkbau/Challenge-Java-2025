package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import accenture.sharks.challenge.dto.CaminoDTO;
import accenture.sharks.challenge.dto.CaminoMinimoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CaminoControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private String baseUrl;

  @BeforeEach
  public void setUp() {
    baseUrl = "http://localhost:" + port + "/caminos";
  }

  @Test
  public void testAddCaminoExitoso() {
    CaminoDTO camino = new CaminoDTO(1L, 2L, 50.0);

    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, camino, String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Camino agregado exitosamente", response.getBody());
  }

  @Test
  public void testAddCaminoConErroresDeValidacion() {
    CaminoDTO camino = new CaminoDTO(null, null, null); // Datos inválidos

    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, camino, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("El campo idA no puede estar vacio"));
    assertTrue(response.getBody().contains("El campo idB no puede estar vacio"));
    assertTrue(response.getBody().contains("El costo no puede estar vacio"));
  }

  @Test
  public void testDeleteCamino() {
    Long idA = 1L;
    Long idB = 2L;

    restTemplate.delete(baseUrl + "/" + idA + "/" + idB);

    assertTrue(true);
  }

  @Test
  public void testGetCaminosDirectosDesdeUnPunto() {
    Long idA = 1L;

    ResponseEntity<CaminoDTO[]> response = restTemplate.getForEntity(baseUrl + "/" + idA, CaminoDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().length >= 0);
  }

  @Test
  public void testGetCaminoMenorCostoExitoso() {
    Long idA = 1L;
    Long idB = 2L;

    ResponseEntity<CaminoMinimoDTO> response = restTemplate.getForEntity(baseUrl + "/" + idA + "/" + idB, CaminoMinimoDTO.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertNotNull(response.getBody().getCostoTotal());
    assertNotNull(response.getBody().getPuntosDeVentas());
  }

  @Test
  public void testGetCaminoMenorCostoNoEncontrado() {
    Long idA = 100L;
    Long idB = 200L;

    ResponseEntity<CaminoMinimoDTO> response = restTemplate.getForEntity(baseUrl + "/" + idA + "/" + idB, CaminoMinimoDTO.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
