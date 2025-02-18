package accenture.sharks.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PuntoDeVentaControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private String baseUrl;

  @BeforeEach
  public void setUp() {
    baseUrl = "http://localhost:" + port + "/puntos";
  }

  @Test
  public void testGetPuntoDeVenta() {
    Long id = 1L;
    PuntoDeVentaDTO response = restTemplate.getForObject(baseUrl + "/" + id, PuntoDeVentaDTO.class);

    assertEquals(id, response.getId());
    assertEquals("CABA", response.getNombre());
  }

  @Test
  public void testGetAllPuntosDeVentas() {
    ResponseEntity<List<PuntoDeVentaDTO>> response = restTemplate.exchange(
        baseUrl,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<PuntoDeVentaDTO>>() {}
    );

    assertEquals(10, response.getBody().size());
  }

  @Test
  public void testAddPuntoVenta_Success() {
    PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();
    puntoDeVentaDTO.setNombre("Nuevo Punto de Venta");

    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, puntoDeVentaDTO, String.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertTrue(response.getBody().contains("Punto de venta creado con id"));
  }

  @Test
  public void testAddPuntoVenta_ValidationError() {
    PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();

    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, puntoDeVentaDTO, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("El campo nombre no puede estar vacio"));
  }

  @Test
  public void testDeletePuntoVenta_Success() {
    Long id = 1L;

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl + "/" + id,
        HttpMethod.DELETE,
        null,
        String.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("Punto de venta eliminado con id: 1"));
  }

  @Test
  public void testDeletePuntoVenta_NotFound() {
    Long id = 999L;

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl + "/" + id,
        HttpMethod.DELETE,
        null,
        String.class
    );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertTrue(response.getBody().contains("Error: No existe punto de venta con id: 999"));
  }


  @Test
  public void testUpdatePuntoDeVenta_Success() {
    Long id = 3L;

    PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();
    puntoDeVentaDTO.setId(id);
    puntoDeVentaDTO.setNombre("Nuevo Nombre");

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl,
        HttpMethod.PUT,
        new HttpEntity<>(puntoDeVentaDTO),
        String.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("Punto de venta actualizado"));
  }

  @Test
  public void testUpdatePuntoDeVenta_NotFound() {
    Long id = 999L;

    PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();
    puntoDeVentaDTO.setId(id);
    puntoDeVentaDTO.setNombre("Nuevo Nombre");

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl,
        HttpMethod.PUT,
        new HttpEntity<>(puntoDeVentaDTO),
        String.class
    );

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertTrue(response.getBody().contains("El punto de venta no existía, se ha creado"));
  }

  @Test
  public void testUpdatePuntoDeVenta_BadRequest_ValidationError() {
    PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();


    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl,
        HttpMethod.PUT,
        new HttpEntity<>(puntoDeVentaDTO),
        String.class
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testUpdatePuntoDeVenta_BadRequest_IdMissing() {
    PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();
    puntoDeVentaDTO.setNombre("Nuevo Nombre");

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl,
        HttpMethod.PUT,
        new HttpEntity<>(puntoDeVentaDTO),
        String.class
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
