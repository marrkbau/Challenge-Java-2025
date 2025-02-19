package accenture.sharks.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;


@Validated
@Schema(description = "DTO para representar un camino entre dos puntos")
public class CaminoDTO {

    public CaminoDTO(Long idA, Long idB, Double costo) {
        this.idA = idA;
        this.idB = idB;
        this.costo = costo;
    }

    public CaminoDTO() {}

    @NotNull(message = "El campo idA no puede estar vacío")
    @Schema(description = "ID del punto A", example = "1")
    private Long idA;

    @NotNull(message = "El campo idB no puede estar vacío")
    @Schema(description = "ID del punto B", example = "2")
    private Long idB;

    @NotNull(message = "El costo no puede estar vacío")
    @Schema(description = "Costo del camino entre A y B", example = "12.5")
    private Double costo;

    public @NotNull(message = "El campo idA no puede estar vacio") Long getIdA() {
        return idA;
    }

    public void setIdA(@NotNull(message = "El campo idA no puede estar vacio") Long idA) {
        this.idA = idA;
    }

    public @NotNull(message = "El campo idB no puede estar vacio") Long getIdB() {
        return idB;
    }

    public void setIdB(@NotNull(message = "El campo idB no puede estar vacio") Long idB) {
        this.idB = idB;
    }

    public @NotNull(message = "El costo no puede estar vacio") Double getCosto() {
        return costo;
    }

    public void setCosto(@NotNull(message = "El costo no puede estar vacio") Double costo) {
        this.costo = costo;
    }

}
