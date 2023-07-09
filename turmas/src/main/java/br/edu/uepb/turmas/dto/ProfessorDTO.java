package br.edu.uepb.turmas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.edu.uepb.turmas.domain.IntegranteENUM;
import lombok.Data;

@Data
public class ProfessorDTO {
    private Long id;
    private String nome;
    private String formacao;
    private String email;
    private IntegranteENUM funcao;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
