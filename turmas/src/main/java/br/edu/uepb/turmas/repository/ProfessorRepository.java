package br.edu.uepb.turmas.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.uepb.turmas.domain.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsByemail(String email);
}