package accenture.sharks.challenge.repository;

import accenture.sharks.challenge.model.Acreditacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcreditacionRepository extends JpaRepository<Acreditacion, Long> {

  @Query("SELECT a FROM Acreditacion a WHERE a.idPuntoDeVenta = :idPuntoDeVenta")
  List<Acreditacion> findAllByIdPuntoDeVenta(@Param("idPuntoDeVenta") Long idPuntoDeVenta);


}
