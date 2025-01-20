package accenture.sharks.challenge.repository;

import accenture.sharks.challenge.model.Acreditacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcreditacionRepository extends JpaRepository<Acreditacion, Long> {
}
