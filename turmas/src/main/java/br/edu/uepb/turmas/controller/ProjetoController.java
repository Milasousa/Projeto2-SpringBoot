package br.edu.uepb.turmas.controller;

import java.util.List;
import java.util.stream.Collectors;

import br.edu.uepb.turmas.domain.Aluno;
import br.edu.uepb.turmas.domain.Projeto;
import br.edu.uepb.turmas.dto.AlunoDTO;
import br.edu.uepb.turmas.dto.ErroRespostaGenericaDTO;
import br.edu.uepb.turmas.dto.ProjetoDTO;
import br.edu.uepb.turmas.mapper.AlunoMapper;
import br.edu.uepb.turmas.mapper.ProjetoMapper;
import br.edu.uepb.turmas.services.ProjetoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projetos")
@Api(value = "Projeto")
public class ProjetoController {
    @Autowired
    private ProjetoMapper projetoMapper;
    @Autowired
    private AlunoMapper alunoMapper;
    @Autowired
    private ProjetoService projetoService;
    @GetMapping
    @ApiOperation(value = "Busca uma lista de todos os projetos")
    public List<ProjetoDTO> getProjetos() {
        List<Projeto> projeto = projetoService.listAllProjetos();
        return projeto.stream()
                .map(projetoMapper::convertToProjetoDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Busca um projeto pelo seu identificador")
    public ResponseEntity<?> getProjetosById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(projetoMapper.convertToProjetoDTO(projetoService.findById(id)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(new ErroRespostaGenericaDTO(e.getMessage()));
        }
    }

    @GetMapping("/alunos/{projetoId}")
    @ApiOperation(value = "Listar os alunos associados ao projeto")
    public List<AlunoDTO> getAllAlunos(@PathVariable(value = "projetoId") Long projetoId) throws NotFoundException {
        List<Aluno> projeto = projetoService.getAllAlunos(projetoId);
        return projeto.stream()
                .map(alunoMapper::convertToAlunoDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{professorId}")
    @ApiOperation(value = "Cria um novo projeto")
    public ResponseEntity<?> criarProjeto(@RequestBody ProjetoDTO projetoDTO,
            @PathVariable("professorId") Long professorId) throws NotFoundException {
        Projeto projeto = projetoMapper.convertFromProjetoDTO(projetoDTO);
        try {
            return new ResponseEntity<>(projetoService.criarProjeto(projeto, professorId), HttpStatus.CREATED);

        } catch (NotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(new ErroRespostaGenericaDTO(e.getMessage()));

        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualiza um projeto a partir do seu identificador")
    public ResponseEntity<?> atualizarProjeto(@PathVariable("id") Long id, @RequestBody ProjetoDTO projetoDTO)
            throws NotFoundException {
        try {
            Projeto projeto = projetoMapper.convertFromProjetoDTO(projetoDTO);
            return new ResponseEntity<>(projetoMapper.convertToProjetoDTO(projetoService.atualizarProjeto(id, projeto)),
                    HttpStatus.OK);
        } catch (NotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(new ErroRespostaGenericaDTO(e.getMessage()));

        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Exclui um projeto a partir do seu identificador")
    public @ResponseBody ResponseEntity<?> apagarProjeto(@PathVariable("id") Long id) {
        try {
            projetoService.apagarProjeto(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (NotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(new ErroRespostaGenericaDTO(e.getMessage()));

        }
    }

}
