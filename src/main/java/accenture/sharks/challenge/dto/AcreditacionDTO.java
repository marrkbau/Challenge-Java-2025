package accenture.sharks.challenge.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Validated
public class AcreditacionDTO {

    @NotNull(message = "El id del punto de venta no puede ser nulo")
    private Long idPuntoDeVenta;

    @NotNull(message = "El importe no puede ser nulo")
    private BigDecimal importe;

    private String nombrePuntoDeVenta;

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
