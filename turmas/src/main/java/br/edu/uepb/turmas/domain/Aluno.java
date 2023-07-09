package br.edu.uepb.turmas.domain;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "alunos", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "email", "id" })
})
public class Aluno extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "funcao")
    private IntegranteENUM funcao;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;
    @Builder(builderMethodName = "AlunoBuilder",builderClassName = "AlunoBuilder")
    public Aluno(Long id, String username, String password, String authority, Long id2, String nome, String email,
            IntegranteENUM funcao) {
        super(id, username, password, authority);
        id = id2;
        this.nome = nome;
        this.email = email;
        this.funcao = funcao;
    }


}
