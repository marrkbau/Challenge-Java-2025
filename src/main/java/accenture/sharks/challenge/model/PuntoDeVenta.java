package accenture.sharks.challenge.model;

import jakarta.persistence.*;

import java.io.Serializable;


public class PuntoDeVenta implements Serializable {

    private Long id;

    private String nombre;

    public PuntoDeVenta(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public PuntoDeVenta(String nombre) {
        this.nombre = nombre;
    }

    public PuntoDeVenta() {
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
