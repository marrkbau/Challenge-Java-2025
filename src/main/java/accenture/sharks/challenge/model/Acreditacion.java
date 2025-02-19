package accenture.sharks.challenge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "acreditacion")
@Schema(description = "Entidad que representa una acreditación en el sistema")
public class Acreditacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la acreditación", example = "1")
    Long id;

    @Column(name = "importe")
    @Schema(description = "Importe de la acreditación", example = "1500.75")
    BigDecimal importe;

    @Column(name = "fecha_acreditacion")
    @Schema(description = "Fecha de la acreditación", example = "2024-02-19T14:30:00")
    LocalDateTime fechaAcreditacion;

    @Column(name = "nombre_punto_venta")
    @Schema(description = "Nombre del punto de venta", example = "Sucursal Central")
    String nombrePuntoDeVenta;

    @Column(name = "id_punto_venta")
    @Schema(description = "ID del punto de venta asociado", example = "5")
    Long idPuntoDeVenta;

    public Acreditacion() {
    }

    public Acreditacion( Long idPuntoDeVenta, BigDecimal importe, LocalDateTime fechaAcreditacion) {
        this.importe = importe;
        this.fechaAcreditacion = fechaAcreditacion;
        this.nombrePuntoDeVenta = nombrePuntoDeVenta;
        this.idPuntoDeVenta = idPuntoDeVenta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public LocalDateTime getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(LocalDateTime fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public String getNombrePuntoDeVenta() {
        return nombrePuntoDeVenta;
    }

    public void setNombrePuntoDeVenta(String nombrePuntoDeVenta) {
        this.nombrePuntoDeVenta = nombrePuntoDeVenta;
    }

    public Long getIdPuntoDeVenta() {
        return idPuntoDeVenta;
    }

    public void setIdPuntoDeVenta(Long idPuntoDeVenta) {
        this.idPuntoDeVenta = idPuntoDeVenta;
    }
}
