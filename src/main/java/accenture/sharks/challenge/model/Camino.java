package accenture.sharks.challenge.model;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "camino")
public class Camino {

    @EmbeddedId
    private CaminoId id;

    @Column(name = "costo")
    private Double costo;

    public Camino() {
        this.id = new CaminoId();
    }

    public Camino(Long puntoA, Long puntoB, Double costo) {
        this.id = new CaminoId(puntoA, puntoB);
        this.costo = costo;
    }

    public CaminoId getId() {
        return id;
    }

    public Long getIdA() {
        return id.getIdA();
    }

    public Long getIdB() {
        return id.getIdB();
    }

    public void setIdA(Long idA) {
        id.setIdA(idA);
    }

    public void setIdB(Long idB) {
        id.setIdB(idB);
    }

    public void setId(CaminoId id) {
        this.id = id;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camino camino = (Camino) o;
        return id.equals(camino.id) && costo.equals(camino.costo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, costo);
    }


}
