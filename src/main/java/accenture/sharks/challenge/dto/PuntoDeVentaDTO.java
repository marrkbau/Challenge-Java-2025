package accenture.sharks.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO para representar un punto de venta")
public class PuntoDeVentaDTO {

    @Schema(description = "ID del punto de venta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "El campo nombre no puede estar vacío")
    @Schema(description = "Nombre del punto de venta", example = "Sucursal Norte")
    private String nombre;

    @Schema(description = "Indica si el punto de venta está activo", example = "true")
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
