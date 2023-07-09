package br.edu.uepb.turmas.controller;

import java.util.List;
import java.util.stream.Collectors;


import br.edu.uepb.turmas.domain.Aluno;
import br.edu.uepb.turmas.dto.AlunoDTO;
import br.edu.uepb.turmas.dto.ErroRespostaGenericaDTO;
import br.edu.uepb.turmas.exceptions.DadosIguaisException;
import br.edu.uepb.turmas.mapper.AlunoMapper;
import br.edu.uepb.turmas.services.AlunoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alunos")
@Api(value = "Aluno")
public class AlunoController {
    @Autowired
    private AlunoMapper alunoMapper;

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    @ApiOperation(value = "Busca uma lista de todos os alunos")
    public List<AlunoDTO> getAlunos() {
        List<Aluno> alunos = alunoService.listAllAlunos();
        return alunos.stream()
                .map(alunoMapper::convertToAlunoDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Busca um aluno pelo seu identificador")
    public ResponseEntity<?> getAlunosById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(alunoMapper.convertToAlunoDTO(alunoService.findById(id)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(new ErroRespostaGenericaDTO(e.getMessage()));
        }
    }

    @PostMapping
    @ApiOperation(value = "Cria um novo aluno")
    public ResponseEntity<?> criarAlunos(@RequestBody AlunoDTO alunoDTO) {
        try {
            Aluno aluno = alunoMapper.convertFromAlunoDTO(alunoDTO);
            alunoService.getMessage();
            return new ResponseEntity<>(alunoService.criarAlunos(aluno), HttpStatus.CREATED);
        } catch (DadosIguaisException e) {
            return ResponseEntity.badRequest().body(new ErroRespostaGenericaDTO(e.getMessage()));
        }
    }
   
    @PutMapping("/{id}")
    @ApiOperation(value = "Atualiza um aluno a partir do seu identificador")
    public ResponseEntity<?> atualizarAlunos(@PathVariable("id") Long id, @RequestBody AlunoDTO alunoDTO)
            throws NotFoundException, DadosIguaisException {
        try {
            Aluno aluno = alunoMapper.convertFromAlunoDTO(alunoDTO);
            return new ResponseEntity<>(alunoMapper.convertToAlunoDTO(alunoService.atualizarAlunos(id, aluno)),
                    HttpStatus.OK);
        } catch (NotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(new ErroRespostaGenericaDTO(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Exclui um aluno a partir do seu identificador")
    public ResponseEntity<?> apagarAlunos(@PathVariable Long id) throws NotFoundException {

        try {
            alunoService.apagarAlunos(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(new ErroRespostaGenericaDTO(e.getMessage()));
        }
    }

    @PatchMapping("{alunoId}/matricula/{projetoId}/{papel}")
    @ApiOperation(value = "Matricula um aluno em um projeto, a partir dos identificadores do aluno, projeto e papel ")
    public ResponseEntity<?> vincularProjetoAluno(@PathVariable("alunoId") Long alunoId,
            @PathVariable("projetoId") Long projetoId,
            @PathVariable("papel") String papel) throws NotFoundException {
        try {
            return new ResponseEntity<>(alunoService.vincularProjetoAluno(alunoId, projetoId, papel),
                    HttpStatus.CREATED);

        } catch (NotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(new ErroRespostaGenericaDTO(e.getMessage()));

        }
    }
}
