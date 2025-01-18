package accenture.sharks.challenge.service;

import accenture.sharks.challenge.model.PuntoDeVenta;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PuntoDeVentaService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, PuntoDeVenta> hashOperations;


    public PuntoDeVentaService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();

    }

    private static final String PUNTOS_DE_VENTA_CACHE = "puntosDeVenta";

    /**
     * Retorna todos los puntos de venta que estan en el Hash de Redis
     * Aclaración: Los ordena por ID, ya que el hash de redis no garantiza el orden de inserción
     */
    public List<PuntoDeVenta> getAllPuntosDeVenta() {
        List<PuntoDeVenta> puntosDeVenta = hashOperations.values(PUNTOS_DE_VENTA_CACHE);
        return puntosDeVenta.stream()
                .sorted(Comparator.comparing(PuntoDeVenta::getId))
                .collect(Collectors.toList());
    }

    /**
     * Retorna un punto de venta por su ID
     */
    public PuntoDeVenta getPuntoDeVenta(Long id) {
        return hashOperations.get(PUNTOS_DE_VENTA_CACHE, id.toString());
    }

    /**
     * Agrega un punto de venta al hash de Redis
     */
    public void addPuntoDeVenta(PuntoDeVenta puntoDeVenta) {
        if (puntoDeVenta.getId() == null) {
            puntoDeVenta.setId(generateNewId());
        }

        hashOperations.put(PUNTOS_DE_VENTA_CACHE, puntoDeVenta.getId().toString(), puntoDeVenta);
    }


    /**
     * Genera un nuevo ID para un punto de venta
     */
    public Long generateNewId() {
        List<PuntoDeVenta> allPuntos = hashOperations.values(PUNTOS_DE_VENTA_CACHE);

        return allPuntos.stream()
                .mapToLong(PuntoDeVenta::getId)
                .max()
                .orElse(0L) + 1;
    }

    /**
     * Actualiza un punto de venta en el hash de Redis
     * Si el punto de venta no existe, se añade.
     */
    public void updatePuntoDeVenta(PuntoDeVenta puntoVenta) {
        hashOperations.put(PUNTOS_DE_VENTA_CACHE, puntoVenta.getId().toString(), puntoVenta);
    }

    /**
     * Elimina un punto de venta del hash de Redis
     */
    public void removePuntoDeVenta(Long id) {
        hashOperations.delete(PUNTOS_DE_VENTA_CACHE, id.toString());
    }
}
