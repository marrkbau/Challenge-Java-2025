package accenture.sharks.challenge.service.impl;

import accenture.sharks.challenge.dto.CaminoDTO;
import accenture.sharks.challenge.dto.PuntoDeVentaDTO;
import accenture.sharks.challenge.exceptions.AddCaminoException;
import accenture.sharks.challenge.model.CacheEntries;
import accenture.sharks.challenge.model.Camino;
import accenture.sharks.challenge.dto.CaminoMinimoDTO;
import accenture.sharks.challenge.model.PuntoDeVenta;
import accenture.sharks.challenge.service.ICaminoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CaminoService implements ICaminoService {

    private final HashOperations<String, String, Camino> hashCamino;
    private final HashOperations<String, String, PuntoDeVenta> hashPuntoDeVenta;

    private final ModelMapper modelMapper;


    public CaminoService(RedisTemplate<String, Object> redisTemplate, ModelMapper modelMapper) {
        this.hashCamino = redisTemplate.opsForHash();
        this.hashPuntoDeVenta = redisTemplate.opsForHash();
        this.modelMapper = modelMapper;
    }

    /**
     * Agrega un camino directo entre dos puntos
     * generando una clave unica entre las dos combinaciones de puntos
     * Ej: IdA=1, IdB=2 -> Clave: 1-2
     */
    @Override
    public void addCamino(CaminoDTO caminoDTO) {
        if (caminoDTO.getIdA().equals(caminoDTO.getIdB())) {
            throw new AddCaminoException("No se puede agregar un camino entre un punto y si mismo");
        }

        if (caminoDTO.getCosto() < 0) {
            throw new AddCaminoException("El costo de un camino no puede ser negativo");
        }

        Camino camino = toEntity(caminoDTO);
        String key = generateKey(camino.getIdA(), camino.getIdB());
        hashCamino.put(CacheEntries.CAMINOS.getValue(), key, camino);
    }
    /**
     * Elimina un camino directo entre dos puntos
     */
    @Override
    public void deleteCamino(Long idA, Long idB) {
        String key = generateKey(idA, idB);
        hashCamino.delete(CacheEntries.CAMINOS.getValue(), key);
    }

    /**
     * Retorna todos los caminos que estan conecatdos directamente al
     * @param id
     *
     */
    @Override
    public List<CaminoDTO> getCaminosDirectosDesdeUnPunto(Long id) {
        Map<String, Camino> allCaminos = hashCamino.entries(CacheEntries.CAMINOS.getValue());
        List<CaminoDTO> result = new ArrayList<>();

        for (Camino camino : allCaminos.values()) {
            if (camino.getIdA().equals(id) || camino.getIdB().equals(id)) {
                CaminoDTO caminoDTO = toDTO(camino);
                result.add(caminoDTO);
            }
        }

        return result;
    }


    @Override
    public CaminoMinimoDTO getCaminoMenorCosto(Long idA, Long idB) {
        Map<String, Camino> caminos = hashCamino.entries(CacheEntries.CAMINOS.getValue());

        Map<Long, Map<Long, Double>> grafo = construirGrafo(caminos);

        return buscarCaminoMasCorto(grafo, idA, idB);
    }

    private static class Nodo {
        Long id;
        Double costo;

        Nodo(Long id, Double costo) {
            this.id = id;
            this.costo = costo;
        }
    }

    /**
     * Construye un grafo a partir de los caminos almacenados en Redis
     * Este grafo sirve para luego buscar el camino de menor costo entre dos puntos
     */
    private Map<Long, Map<Long, Double>> construirGrafo(Map<String, Camino> caminos) {
        Map<Long, Map<Long, Double>> grafo = new HashMap<>();
        for (Camino camino : caminos.values()) {
            grafo.computeIfAbsent(camino.getIdA(), k -> new HashMap<>()).put(camino.getIdB(), camino.getCosto());
            grafo.computeIfAbsent(camino.getIdB(), k -> new HashMap<>()).put(camino.getIdA(), camino.getCosto());
        }
        return grafo;
    }

    /**
     * Busca el camino de menor costo entre dos puntos utilizando el algoritmo de Dijkstra
     * El algoritmo se implementa con una cola de prioridad y necesita tener a los elementos a modo de grafo
     * ya que busca desde el nodo origen al destino, el camino de nodos de menor costo
     */
    private CaminoMinimoDTO buscarCaminoMasCorto(Map<Long, Map<Long, Double>> grafo, Long origen, Long destino) {
        PriorityQueue<Nodo> cola = new PriorityQueue<>(Comparator.comparingDouble(n -> n.costo));
        Map<Long, Double> costos = new HashMap<>();
        Map<Long, Long> previos = new HashMap<>();

        cola.add(new Nodo(origen, 0.0));
        costos.put(origen, 0.0);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            if (actual.id.equals(destino)) {
                return construirRespuesta(destino, previos, costos.get(destino));
            }
            for (Map.Entry<Long, Double> vecino : grafo.getOrDefault(actual.id, new HashMap<>()).entrySet()) {
                double nuevoCosto = actual.costo + vecino.getValue();
                if (nuevoCosto < costos.getOrDefault(vecino.getKey(), Double.MAX_VALUE)) {
                    costos.put(vecino.getKey(), nuevoCosto);
                    previos.put(vecino.getKey(), actual.id);
                    cola.add(new Nodo(vecino.getKey(), nuevoCosto));
                }
            }
        }

        return null;
    }

    private CaminoMinimoDTO construirRespuesta(Long destino, Map<Long, Long> previos, Double costoTotal) {
        List<PuntoDeVentaDTO> puntosDeVenta = new LinkedList<>();

        for (Long at = destino; at != null; at = previos.get(at)) {

            PuntoDeVenta punto = hashPuntoDeVenta.get(CacheEntries.PUNTOS_DE_VENTA.getValue(), at.toString());
            puntosDeVenta.add(modelMapper.map(punto, PuntoDeVentaDTO.class));
        }

        return new CaminoMinimoDTO(puntosDeVenta, costoTotal);
    }

    /**
     * Genera una clave para almacenar un camino en el hash de Redis
     */
    private String generateKey(Long idA, Long idB) {
        return idA < idB ? idA + "-" + idB : idB + "-" + idA;
    }

    private CaminoDTO toDTO(Camino camino) {
        return modelMapper.map(camino, CaminoDTO.class);
    }

    private Camino toEntity(CaminoDTO caminoDTO) {
        return modelMapper.map(caminoDTO, Camino.class);
    }


}
