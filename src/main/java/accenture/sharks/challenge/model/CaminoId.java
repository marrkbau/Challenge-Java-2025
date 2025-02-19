package accenture.sharks.challenge.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CaminoId implements Serializable {

  private Long idA;
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
