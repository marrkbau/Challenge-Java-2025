package accenture.sharks.challenge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Schema(description = "Clave compuesta para la entidad Camino")
public class CaminoId implements Serializable {
  @Schema(description = "ID del punto de venta A", example = "1")
  private Long idA;
  @Schema(description = "ID del punto de venta B", example = "2")
  private Long idB;

  public CaminoId() {
  }

  public CaminoId(Long idA, Long idB) {
    this.idA = idA;
    this.idB = idB;
  }

  public Long getIdA() {
    return idA;
  }

  public void setIdA(Long idA) {
    this.idA = idA;
  }

  public Long getIdB() {
    return idB;
  }

  public void setIdB(Long idB) {
    this.idB = idB;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CaminoId caminoId = (CaminoId) o;
    return Objects.equals(idA, caminoId.idA) && Objects.equals(idB, caminoId.idB);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA, idB);
  }
}
