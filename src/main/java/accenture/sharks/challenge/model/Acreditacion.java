package accenture.sharks.challenge.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "acreditacion")
public class Acreditacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "importe")
    BigDecimal importe;

    @Column(name = "fecha_acreditacion")
    Date fechaAcreditacion;

    @Column(name = "nombre_punto_venta")
    String nombrePuntoDeVenta;

    @Column(name = "id_punto_venta")
    Long idPuntoDeVenta;

    public Acreditacion() {
    }

    public Acreditacion( Long idPuntoDeVenta, BigDecimal importe, Date fechaAcreditacion) {
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

    public Date getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(Date fechaAcreditacion) {
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
