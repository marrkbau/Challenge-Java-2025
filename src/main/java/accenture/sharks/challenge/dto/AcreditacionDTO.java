package accenture.sharks.challenge.dto;

import java.math.BigDecimal;

public class AcreditacionDTO {

    private Long idPuntoDeVenta;

    private BigDecimal importe;

    private String nombrePuntoDeVenta;

    public AcreditacionDTO() {
    }

    public AcreditacionDTO(Long idPuntoDeVenta, BigDecimal importe) {
        this.idPuntoDeVenta = idPuntoDeVenta;
        this.importe = importe;
    }

    public String getNombrePuntoDeVenta() {
        return nombrePuntoDeVenta;
    }

    public void setNombrePuntoDeVenta(String nombrePuntoDeVenta) {
        this.nombrePuntoDeVenta = nombrePuntoDeVenta;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Long getIdPuntoDeVenta() {
        return idPuntoDeVenta;
    }

    public void setIdPuntoDeVenta(Long idPuntoDeVenta) {
        this.idPuntoDeVenta = idPuntoDeVenta;
    }
}
