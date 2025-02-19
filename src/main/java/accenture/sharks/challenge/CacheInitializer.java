package accenture.sharks.challenge;

import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.Camino;
import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.repository.CaminoRepository;
import accenture.sharks.challenge.repository.PuntoDeVentaRepository;
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


    private static final Logger logger = LoggerFactory.getLogger(CacheInitializer.class);

    private final RedisTemplate<String, Object> redisTemplate;

    private final PuntoDeVentaRepository puntoDeVentaRepository;

    private final CaminoRepository caminoRepository;

    public CacheInitializer(RedisTemplate<String, Object> redisTemplate, PuntoDeVentaRepository puntoDeVentaRepository, CaminoRepository caminoRepository) {
        this.redisTemplate = redisTemplate;
        this.puntoDeVentaRepository = puntoDeVentaRepository;
        this.caminoRepository = caminoRepository;
    }

    @Bean
    public ApplicationRunner initializeRedisCache() {
        return args -> {
            List<PuntoDeVenta> puntosDeVentaIniciales = Arrays.asList(
                    new PuntoDeVenta("CABA", true),
                    new PuntoDeVenta("GBA_1", true),
                    new PuntoDeVenta("GBA_2", true),
                    new PuntoDeVenta( "Santa Fe", true),
                    new PuntoDeVenta("Córdoba", true),
                    new PuntoDeVenta("Misiones", true),
                    new PuntoDeVenta("Salta", true),
                    new PuntoDeVenta("Chubut", true),
                    new PuntoDeVenta("Santa Cruz", true),
                    new PuntoDeVenta("Catamarca", true),
                    new PuntoDeVenta("Tierra del Fuego", true),
                    new PuntoDeVenta("Ushuaia", true)
            );

            puntoDeVentaRepository.saveAll(puntosDeVentaIniciales);
            HashOperations<String, String, PuntoDeVenta> hashOperations = redisTemplate.opsForHash();

            long initialCount = hashOperations.size(CacheEntries.PUNTOS_DE_VENTA.getValue());
            if (initialCount == 0) {
                puntosDeVentaIniciales.forEach(pdv -> {
                    hashOperations.put(CacheEntries.PUNTOS_DE_VENTA.getValue(), pdv.getId().toString(), pdv);
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
                    new Camino(1L, 4L, 7d),
                    new Camino(4L, 5L, 5d),
                    new Camino(2L, 5L, 14d),
                    new Camino(6L, 7L, 32d),
                    new Camino(8L, 9L, 11d),
                    new Camino(10L, 7L, 5d),
                    new Camino(3L, 8L, 10d),
                    new Camino(5L, 8L, 30d),
                    new Camino(10L, 5L, 5d),
                    new Camino(4L, 6L, 6d),
                    new Camino(11L, 12L, 7d)
            );

            caminoRepository.saveAll(caminosIniciales);

            HashOperations<String, String, Camino> caminoHashOperations = redisTemplate.opsForHash();
            long caminoCount = caminoHashOperations.size(CacheEntries.CAMINOS.getValue());
            if (caminoCount == 0) {
                caminosIniciales.forEach(camino -> {
                    String key = camino.getIdA() + "-" + camino.getIdB();
                    caminoHashOperations.put(CacheEntries.CAMINOS.getValue(), key, camino);
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
            logger.info("Limpiando cache...");
            redisTemplate.delete(CacheEntries.PUNTOS_DE_VENTA.getValue());
            redisTemplate.delete(CacheEntries.CAMINOS.getValue());
            logger.info("Cache limpiada de forma exitosa");
        } catch (Exception e) {
            logger.error("Error limpiando la cache de Redis ", e);
        }
    }
}
