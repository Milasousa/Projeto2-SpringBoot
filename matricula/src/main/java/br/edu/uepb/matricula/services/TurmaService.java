package br.edu.uepb.matricula.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.uepb.matricula.domain.Turma;
import br.edu.uepb.matricula.repository.TurmaRepository;
import javassist.NotFoundException;

@Service
public class TurmaService {
    @Autowired
    private TurmaRepository turmarepository;
    public List<Turma> listAllTurmas() {
        return turmarepository.findAll();
    }

    public Turma criarTurma(Turma turma) {
        return turmarepository.save(turma);
    }

    public Turma atualizarTurma(Long id, Turma turma) throws NotFoundException {
        try {
            if ((turmarepository.findById(id).get()) == null) {
                throw new NotFoundException("Não existe nenhuma Turma com esse identificador: " + id);

            }
            return turmarepository.save(turma);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não existe nenhuma Turma com esse identificador: " + id);

        }
    }

    public void apagarTurma(Long id) throws NotFoundException {
        try {
            turmarepository.delete(turmarepository.findById(id).get());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não existe nenhuma Turma com esse identificador: " + id);

        }
    }
    public Turma findById(Long id) throws NotFoundException {
        return turmarepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não existe nenhuma Turma com esse identificador: " + id));
    }

}
