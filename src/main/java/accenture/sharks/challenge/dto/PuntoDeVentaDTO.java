package accenture.sharks.challenge.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class PuntoDeVentaDTO {

    private Long id;

    @NotNull(message = "El campo nombre no puede estar vacio")
    private String nombre;

    private Boolean activo;

    public PuntoDeVentaDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public PuntoDeVentaDTO(Long id, String nombre, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    public PuntoDeVentaDTO(String nombre) {
        this.nombre = nombre;
    }

    public PuntoDeVentaDTO() {
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
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
