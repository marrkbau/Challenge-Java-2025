package accenture.sharks.challenge.dto;


import java.util.List;

public class CaminoMinimoDTO {
    private List<PuntoDeVentaDTO> puntosDeVentas;
    private Double costoTotal;

    public CaminoMinimoDTO( List<PuntoDeVentaDTO> puntosDeVenta, Double costoTotal) {
        this.puntosDeVentas = puntosDeVenta;
        this.costoTotal = costoTotal;
    }


    public List<PuntoDeVentaDTO> getPuntosDeVentas() {
        return puntosDeVentas;
    }

    public void setPuntosDeVentas(List<PuntoDeVentaDTO> puntosDeVentas) {
        this.puntosDeVentas = puntosDeVentas;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }

    // Getters y Setters
}
