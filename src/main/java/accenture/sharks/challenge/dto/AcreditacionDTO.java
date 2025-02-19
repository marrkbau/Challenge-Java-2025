package accenture.sharks.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Validated
@Schema(description = "DTO para representar una acreditación")
public class AcreditacionDTO {

    @NotNull(message = "El id del punto de venta no puede ser nulo")
    @Schema(description = "ID del punto de venta", example = "1")
    private Long idPuntoDeVenta;

    @NotNull(message = "El importe no puede ser nulo")
    @Schema(description = "Importe de la acreditación", example = "1500.75")
    private BigDecimal importe;

    @Schema(description = "Nombre del punto de venta", example = "Sucursal Central", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombrePuntoDeVenta;

    @Schema(description = "Fecha de la acreditación", example = "2024-02-19T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaAcreditacion;

    public AcreditacionDTO() {
    }

    public AcreditacionDTO(@NotNull(message = "El id del punto de venta no puede ser nulo") Long idPuntoDeVenta, @NotNull(message = "El importe no puede ser nulo") BigDecimal importe, String nombrePuntoDeVenta) {
        this.idPuntoDeVenta = idPuntoDeVenta;
        this.importe = importe;
        this.nombrePuntoDeVenta = nombrePuntoDeVenta;
    }

    public LocalDateTime getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(LocalDateTime fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public @NotNull(message = "El id del punto de venta no puede ser nulo") Long getIdPuntoDeVenta() {
        return idPuntoDeVenta;
    }

    public void setIdPuntoDeVenta(@NotNull(message = "El id del punto de venta no puede ser nulo") Long idPuntoDeVenta) {
        this.idPuntoDeVenta = idPuntoDeVenta;
    }

    public String getNombrePuntoDeVenta() {
        return nombrePuntoDeVenta;
    }

    public void setNombrePuntoDeVenta(String nombrePuntoDeVenta) {
        this.nombrePuntoDeVenta = nombrePuntoDeVenta;
    }

    public @NotNull(message = "El importe no puede ser nulo") BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(@NotNull(message = "El importe no puede ser nulo") BigDecimal importe) {
        this.importe = importe;
    }
}
