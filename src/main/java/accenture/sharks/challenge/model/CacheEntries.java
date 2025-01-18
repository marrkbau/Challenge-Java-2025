package accenture.sharks.challenge.model;

public enum CacheEntries {

    PUNTOS_DE_VENTA("puntos_de_venta"),
    CAMINOS("caminos");

    private final String value;

    CacheEntries(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
