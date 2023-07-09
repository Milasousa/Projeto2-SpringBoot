package br.edu.uepb.turmas.settings;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.edu.uepb.turmas.mapper.AlunoMapper;
import br.edu.uepb.turmas.mapper.ProfessorMapper;
import br.edu.uepb.turmas.mapper.ProjetoMapper;
import br.edu.uepb.turmas.mapper.UserMapper;

@Configuration
public class TurmasMapperConfig {  
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public AlunoMapper alunoMapper() {
        return new AlunoMapper();
    }
    @Bean
    public ProfessorMapper professorMapper() {
        return new ProfessorMapper();
    }
    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }
    @Bean
    public ProjetoMapper projetoMapper() {
        return new ProjetoMapper();
    }
}
