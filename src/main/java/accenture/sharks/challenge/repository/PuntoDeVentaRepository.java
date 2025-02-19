package accenture.sharks.challenge.repository;

import accenture.sharks.challenge.model.PuntoDeVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuntoDeVentaRepository extends JpaRepository<PuntoDeVenta, Long> {
}
