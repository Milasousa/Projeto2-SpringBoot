package br.edu.uepb.matricula.controller;

import java.util.List;
import java.util.stream.Collectors;

import br.edu.uepb.matricula.domain.Turma;
import br.edu.uepb.matricula.dto.ErroRespostaGenericaDTO;
import br.edu.uepb.matricula.dto.TurmaDTO;
import br.edu.uepb.matricula.mapper.TurmaMapper;
import br.edu.uepb.matricula.services.TurmaService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/turmas")
//@Api(value = "Turma")
public class TurmaController {
    @Autowired
    private TurmaMapper turmaMapper;

    @Autowired
    private TurmaService turmaService;

    @GetMapping
    //@ApiOperation(value = "Busca uma lista de todos as turmas")
    public List<TurmaDTO> getTurmas() {
        List<Turma> turmas = turmaService.listAllTurmas();
        return turmas.stream()
                .map(turmaMapper::convertToTurmaDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    //@ApiOperation(value = "Busca uma turma pelo seu identificador")
    public ResponseEntity<?> getTurmasById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(turmaMapper.convertToTurmaDTO(turmaService.findById(id)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(new ErroRespostaGenericaDTO(e.getMessage()));
        }
    }

    @PostMapping
    //@ApiOperation(value = "Cria uma nova turma")
    public ResponseEntity<?> criarTurma(@RequestBody TurmaDTO turmaDTO) {
        Turma turma = turmaMapper.convertFromTurmaDTO(turmaDTO);
        return new ResponseEntity<>(turmaService.criarTurma(turma), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    //@ApiOperation(value = "Atualiza uma turma a partir do seu identificador")
    public ResponseEntity<?> atualizarTurma(@PathVariable("id") Long id, @RequestBody TurmaDTO turmaDTO)
            throws NotFoundException {
        try {
            Turma turma = turmaMapper.convertFromTurmaDTO(turmaDTO);
            return new ResponseEntity<>(turmaMapper.convertToTurmaDTO(turmaService.atualizarTurma(id, turma)),
                    HttpStatus.OK);
        } catch (NotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(new ErroRespostaGenericaDTO(e.getMessage()));

        }
    }

    @DeleteMapping("/{id}")
    //@ApiOperation(value = "Exclui uma turma a partir do seu identificador")
    public @ResponseBody ResponseEntity<?> apagarTurma(@PathVariable("id") Long id) {
        try {
            turmaService.apagarTurma(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (NotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(new ErroRespostaGenericaDTO(e.getMessage()));

        }
    }

}
