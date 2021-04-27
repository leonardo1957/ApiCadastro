package br.com.pessoas.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.br.CPF;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pessoa")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode
@Getter
@Setter
@ApiModel(value = "Pessoa", description = "Representa uma pessoa")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID da pessoa", example = "1")
    private Long codigo;

    @NotEmpty
    @ApiModelProperty(example = "Lucas Guesser")
    private String nome;

    @ApiModelProperty(example = "Masculino")
    private String sexo;

    @Email
    @ApiModelProperty(example = "teste@teste.com.br")
    private String email;

    @NotNull
    @Column(name = "data_nascimento")
    @ApiModelProperty(value = "Data de Nascimento", example = "2000-11-20")
    private LocalDate dataNascimento;

    @ApiModelProperty(value = "Cidade Estado", example = "Florianópolis Santa Catarina")
    private String naturalidade;

    @ApiModelProperty(value = "País", example = "Brasil")
    private String nacionalidade;

    @NotNull
    @NotEmpty
    @CPF
    @ApiModelProperty(value = "cpf", example = "126.749.479-42")
    private String cpf;

    @Column(name = "data_do_cadastro")
    @CreationTimestamp
    @ApiModelProperty(value = "registro no sistema", example = "2021-04-26 00:00:01")
    private LocalDateTime dataDoCadastro;

    @Column(name = "data_da_alteracao")
    @UpdateTimestamp
    @ApiModelProperty(value = "registro de atualização no sistema", example = "2021-04-26 00:00:01")
    private LocalDateTime dataDaAlteracao;
}
