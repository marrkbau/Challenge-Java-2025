package accenture.sharks.challenge.repository;

import accenture.sharks.challenge.model.Camino;
import accenture.sharks.challenge.model.CaminoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaminoRepository extends JpaRepository<Camino, CaminoId> {

  @Query("SELECT c FROM Camino c WHERE c.id.idA = :idA OR c.id.idB = :idB")
  List<Camino> findByIdAOrIdB(@Param("idA") Long idA, @Param("idB") Long idB);
}
