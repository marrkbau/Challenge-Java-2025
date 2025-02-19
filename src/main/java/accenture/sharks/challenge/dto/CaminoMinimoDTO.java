package accenture.sharks.challenge.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO para representar el camino mínimo entre puntos de venta")
public class CaminoMinimoDTO {

    @Schema(description = "Lista de puntos de venta en el camino mínimo")
    private List<PuntoDeVentaDTO> puntosDeVentas;

    @Schema(description = "Costo total del camino mínimo", example = "45.8")
    private Double costoTotal;

    public CaminoMinimoDTO() {
    }
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

}
