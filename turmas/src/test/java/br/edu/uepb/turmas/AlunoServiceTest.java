package br.edu.uepb.turmas;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import br.edu.uepb.turmas.domain.Aluno;
import br.edu.uepb.turmas.exceptions.DadosIguaisException;
import br.edu.uepb.turmas.repository.AlunoRepository;
import br.edu.uepb.turmas.services.AlunoService;
import javassist.NotFoundException;
import org.junit.jupiter.api.DisplayName;

@SpringBootTest
public class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;
    @InjectMocks
    private AlunoService alunoService;

    @Test
    @DisplayName("teste de criação do estudante")
    void ValidaCriacaoDeAluno() throws DadosIguaisException {
        long id = 1;
        Aluno aluno = Aluno.AlunoBuilder()
                .id(id)
                .nome("Andrea")
                .email("c@gmail.com")
                .username("andrea")
                .password("123")
                .build();
        when(alunoRepository.save(aluno))
                .thenReturn(aluno);
        Aluno criaAluno = alunoService.criarAlunos(aluno);
        assertEquals(criaAluno.getNome(), aluno.getNome());
        assertEquals(criaAluno.getEmail(), aluno.getEmail());
        assertEquals(criaAluno.getUsername(), aluno.getUsername());
    }

    @Test
    boolean TesteListaVazia() { 
        when(alunoRepository.findAll()).thenReturn(Collections.emptyList());
        List<Aluno> listAluno = alunoService.listAllAlunos();
        return (listAluno.isEmpty()) ;
    }

    @Test
    void TestarListaDeAlunos() {
        long id = 1;
        Aluno aluno1 = Aluno.AlunoBuilder()
                .id(id)
                .nome("Andrea")
                .email("c@gmail.com")
                .username("andrea")
                .password("123")
                .build();
        long id2 = 2;
        Aluno aluno2 = Aluno.AlunoBuilder()
                .id(id2)
                .nome("Maria")
                .email("maria@gmail.com")
                .username("maria")
                .password("123456")
                .build();
        List<Aluno> listEsperada = List.of(aluno1, aluno2);

        when(alunoRepository.findAll()).thenReturn(listEsperada);

        List<Aluno> ListAlunos = alunoService.listAllAlunos();

        assertFalse(ListAlunos.isEmpty());
        assertEquals(ListAlunos.size(), listEsperada.size());
    }

    @Test
    void TestarDeletarAluno() throws NotFoundException {
        long id = 1;
        Aluno apagarcafe = Aluno.AlunoBuilder()
                .id(id)
                .nome("Andrea")
                .email("c@gmail.com")
                .username("andrea")
                .password("123")
                .build();
        when(alunoRepository.findById(apagarcafe.getId())).thenReturn(Optional.of(apagarcafe));
        doNothing().when(alunoRepository).delete(apagarcafe);

        alunoService.apagarAlunos(apagarcafe.getId());

        verify(alunoRepository, times(1)).findById(apagarcafe.getId());
        verify(alunoRepository, times(1)).delete(apagarcafe);
    }

    @Test
    void TestarUpdateDeAluno() throws NotFoundException, DadosIguaisException {
        long id = 1;
        Aluno aluno = Aluno.AlunoBuilder()
                .id(id)
                .nome("Andrea")
                .email("c@gmail.com")
                .username("andrea")
                .password("123")
                .build();

        Aluno alunoUpdate = Aluno.AlunoBuilder()
                .id(id)
                .nome("Maria")
                .email("maria@gmail.com")
                .username("maria")
                .password("123456")
                .build();

        when(alunoRepository.findById(aluno.getId())).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(alunoUpdate)).thenReturn(alunoUpdate);
        Aluno updatedAluno = alunoService.atualizarAlunos(aluno.getId(), alunoUpdate);
        assertEquals(updatedAluno, alunoUpdate);
    }
}
