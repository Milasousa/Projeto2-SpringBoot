package br.edu.uepb.matricula.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.uepb.matricula.domain.Turma;
@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {}