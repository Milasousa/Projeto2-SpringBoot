package br.edu.uepb.turmas.repository;
import org.springframework.stereotype.Repository;
import br.edu.uepb.turmas.domain.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {}
