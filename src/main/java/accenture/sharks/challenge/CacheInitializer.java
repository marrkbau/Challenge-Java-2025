package accenture.sharks.challenge;

import accenture.sharks.challenge.model.Camino;
import accenture.sharks.challenge.model.PuntoDeVenta;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CacheInitializer {

    private static final String PUNTOS_DE_VENTA_CACHE = "puntosDeVenta";
    private static final String CAMINOS_CACHE = "caminos";
    private static final Logger logger = LoggerFactory.getLogger(CacheInitializer.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheInitializer(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public ApplicationRunner initializeRedisCache() {
        return args -> {
            List<PuntoDeVenta> puntosDeVentaIniciales = Arrays.asList(
                    new PuntoDeVenta(1L, "CABA"),
                    new PuntoDeVenta(2L, "GBA_1"),
                    new PuntoDeVenta(3L, "GBA_2"),
                    new PuntoDeVenta(4L, "Santa Fe"),
                    new PuntoDeVenta(5L, "Córdoba"),
                    new PuntoDeVenta(6L, "Misiones"),
                    new PuntoDeVenta(7L, "Salta"),
                    new PuntoDeVenta(8L, "Chubut"),
                    new PuntoDeVenta(9L, "Santa Cruz"),
                    new PuntoDeVenta(10L, "Catamarca")
            );

            HashOperations<String, String, PuntoDeVenta> hashOperations = redisTemplate.opsForHash();

            long initialCount = hashOperations.size(PUNTOS_DE_VENTA_CACHE);
            if (initialCount == 0) {
                puntosDeVentaIniciales.forEach(pdv -> {
                    hashOperations.put(PUNTOS_DE_VENTA_CACHE, pdv.getId().toString(), pdv);
                });
                logger.info("Cache de puntos de venta inicializada en Redis con {} elementos", puntosDeVentaIniciales.size());
            } else {
                logger.info("La cache de puntos de venta ya está inicializada con {} elementos", initialCount);
            }

            List<Camino> caminosIniciales = Arrays.asList(
                    new Camino(1L, 2L, 2d),
                    new Camino(1L, 3L, 3d),
                    new Camino(2L, 3L, 5d),
                    new Camino(2L, 4L, 10d),
                    new Camino(1L, 4L, 11d),
                    new Camino(4L, 5L, 5d),
                    new Camino(2L, 5L, 14d),
                    new Camino(6L, 7L, 32d),
                    new Camino(8L, 9L, 11d),
                    new Camino(10L, 7L, 5d),
                    new Camino(3L, 8L, 10d),
                    new Camino(5L, 8L, 30d),
                    new Camino(10L, 5L, 5d),
                    new Camino(4L, 6L, 6d)
            );

            HashOperations<String, String, Camino> caminoHashOperations = redisTemplate.opsForHash();
            long caminoCount = caminoHashOperations.size(CAMINOS_CACHE);
            if (caminoCount == 0) {
                caminosIniciales.forEach(camino -> {
                    String key = camino.getIdA() + "-" + camino.getIdB();
                    caminoHashOperations.put(CAMINOS_CACHE, key, camino);
                });
                logger.info("Cache de caminos inicializada en Redis con {} elementos", caminosIniciales.size());
            } else {
                logger.info("La cache de caminos ya está inicializada con {} elementos", caminoCount);
            }

        };
    }


    @EventListener(ContextClosedEvent.class)
    public void cleanRedisCache() {
        try {
            logger.info("Cleaning Redis cache before shutdown...");
            redisTemplate.delete(PUNTOS_DE_VENTA_CACHE);
            redisTemplate.delete(CAMINOS_CACHE);
            logger.info("Redis cache cleaned successfully.");
        } catch (Exception e) {
            logger.error("Error while cleaning Redis cache: ", e);
        }
    }
}
