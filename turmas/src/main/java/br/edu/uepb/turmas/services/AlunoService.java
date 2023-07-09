package br.edu.uepb.turmas.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.NotFoundException;
import br.edu.uepb.turmas.domain.Aluno;
import br.edu.uepb.turmas.domain.IntegranteENUM;
import br.edu.uepb.turmas.domain.Projeto;
import br.edu.uepb.turmas.domain.User;
import br.edu.uepb.turmas.dto.EmailDTO;
import br.edu.uepb.turmas.exceptions.DadosIguaisException;
import br.edu.uepb.turmas.repository.AlunoRepository;
import br.edu.uepb.turmas.repository.ProjetoRepository;
import br.edu.uepb.turmas.repository.UserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.exchange}")
    public String EXCHANGE_NAME;
    public boolean verificarPorId(Long id) {
        return alunoRepository.existsById(id);
    }

    public boolean verificarPorEmail(String email) {
        return alunoRepository.existsByemail(email);
    }

    public List<Aluno> listAllAlunos() {
        return alunoRepository.findAll();
    }

    public Aluno criarAlunos( Aluno aluno) throws DadosIguaisException {
        User user=aluno;
        if ((verificarPorId(aluno.getId())) || (verificarPorEmail(aluno.getEmail())))
            throw new DadosIguaisException("Já existe um Aluno com essa matricula ou e-mail");
        else if(userRepository.findByUsername(user.getUsername()) != null){
            throw new DadosIguaisException("Já existe um usuário com esse username");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setAuthority("ROLE_ALUNO");
        getMessage();
        return alunoRepository.save(aluno);

    }

    public Aluno atualizarAlunos(Long id, Aluno aluno) throws NotFoundException, DadosIguaisException {
        User user=aluno;
        try {
            if ((alunoRepository.findById(id).get()) == null) {
                throw new NotFoundException("Não foi encontrado nenhum Aluno com essa matricula: " + id);

            }else if(userRepository.findByUsername(user.getUsername()) != null){
                throw new DadosIguaisException("Já existe um usuário com esse username");
            }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setAuthority("ROLE_ALUNO");
            return alunoRepository.save(aluno);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado nenhum Aluno com essa matricula: " + id);
        }

    }

    public Aluno findById(Long id) throws NotFoundException {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum Aluno com essa matricula!"));
    }

    public void apagarAlunos(Long id) throws NotFoundException {
        try {
            alunoRepository.delete(alunoRepository.findById(id).get());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado nenhum Aluno com essa matricula: " + id);
        }
    }

    public Aluno vincularProjetoAluno(Long alunoId, Long projetoId, String papel) throws NotFoundException {
        try {
            if (IntegranteENUM.validar(papel)) {
                ;
                Aluno aluno = alunoRepository.findById(alunoId).get();
                Projeto projeto = projetoRepository.findById(projetoId).get();
                aluno.setProjeto(projeto);
                aluno.setFuncao(IntegranteENUM.valueOf(papel));
                return alunoRepository.save(aluno);
            } else {
                throw new NoSuchElementException("Papel Inválido ou não encontrado");
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado, o identificador informado");

        }

    }

    public String getMessage() {
        try {
            EmailDTO emailDTO = new EmailDTO("******@gmail.com", "Bem-Vindo!", "Olá seja,Bem vindo a UEPB! ");
            String json = new ObjectMapper().writeValueAsString(emailDTO);
            Message message = MessageBuilder.withBody(json.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Hello World-Camila";
    }
}
