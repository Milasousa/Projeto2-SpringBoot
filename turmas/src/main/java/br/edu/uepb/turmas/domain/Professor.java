package br.edu.uepb.turmas.domain;

import java.util.List;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Transactional
@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "professores", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "email", "id" })
})
public class Professor extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "formacao", nullable = false)
    private String formacao;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "funcao")
    private IntegranteENUM funcao;
    
    @OneToOne(mappedBy = "professor")
    @JoinColumn(name = "projeto_id")
    private Projeto projetos;

}
