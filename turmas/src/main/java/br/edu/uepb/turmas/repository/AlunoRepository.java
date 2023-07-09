 package br.edu.uepb.turmas.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.edu.uepb.turmas.domain.Aluno;
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findBynome(String email);
    boolean existsByemail(String email);
    @Query(value="SELECT * FROM ALUNOS",nativeQuery = true)
    List<Aluno> findAlunosByIdProjetoqueryBy();
    
    List<Aluno>  findByprojeto_id(Long projetoId);
}