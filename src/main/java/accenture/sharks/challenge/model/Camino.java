package accenture.sharks.challenge.model;


public class Camino {

    public Camino() {
    }

    public Camino(Long puntoA, Long puntoB, Double costo) {
        this.costo = costo;
        this.idA = puntoA;
        this.idB = puntoB;
    }

    private Double costo;


    private Long idA;
    private Long idB;


    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
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

}
