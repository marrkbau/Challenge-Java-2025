package accenture.sharks.challenge.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "punto_de_venta")
@Schema(description = "Entidad que representa un punto de venta")
public class PuntoDeVenta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del punto de venta", example = "1")
    private Long id;

    @Column(name = "activo")
    @Schema(description = "Indica si el punto de venta está activo", example = "true")
    private boolean activo;

    @Column(name = "nombre")
    @Schema(description = "Nombre del punto de venta", example = "Sucursal Norte")
    private String nombre;


    public PuntoDeVenta(Long id, String nombre, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    public PuntoDeVenta(String nombre, Boolean activo) {
        this.activo = activo;
        this.nombre = nombre;
    }

    public PuntoDeVenta(String nombre) {
        this.nombre = nombre;
    }

    public PuntoDeVenta() {
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
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
