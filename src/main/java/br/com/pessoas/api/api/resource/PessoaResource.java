package br.com.pessoas.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.pessoas.api.event.RecursoCriadoEvent;
import br.com.pessoas.api.exceptionhandler.PessoasExceptionHandler.Erro;
import br.com.pessoas.api.model.Pessoa;
import br.com.pessoas.api.repository.PessoaRepository;
import br.com.pessoas.api.repository.filter.PessoaFilter;
import br.com.pessoas.api.service.PessoaService;
import br.com.pessoas.api.service.exception.PessoaCpfExistenteException;

@Api(tags = "Pessoas")
@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MessageSource messageSource;


    @ApiOperation("Listar todas as pessoas")
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
    public Page<Pessoa> pesquisar(PessoaFilter pessoaFilter, Pageable pageable) {
        return pessoaRepository.filtrar(pessoaFilter, pageable);
    }

    @ApiOperation("Buscar uma pessoa por ID")
    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
    public ResponseEntity<Pessoa> buscarPeloCodigo(
            @ApiParam(value = "ID de uma pessoa", example = "1")
            @PathVariable Long codigo
    ) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);
        return pessoa.isPresent() ? ResponseEntity.ok(pessoa.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation("Cadastra uma pessoa")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
    public ResponseEntity<Pessoa> criar(
            @ApiParam(name = "corpo", value = "Representação de uma nova pessoa")
            @Valid @RequestBody Pessoa pessoa, HttpServletResponse response
    ) {
        Pessoa pessoaSalvo = pessoaService.salvar(pessoa);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalvo.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalvo);
    }

    @ExceptionHandler({PessoaCpfExistenteException.class})
    public ResponseEntity<Object> handlePessoaCpfExistenteException(PessoaCpfExistenteException ex) {
        String mensagemUsuario = messageSource.getMessage("pessoa.cpf.existente", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }

    @ApiOperation("Atualiza uma pessoa por ID")
    @PutMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
    public ResponseEntity<Pessoa> atualizar(
            @ApiParam(value = "ID de uma pessoa", example = "1")
            @PathVariable Long codigo,
            @ApiParam(name = "corpo", value = "Representação de uma pessoa com os novos dados")
            @Valid @RequestBody Pessoa pessoa
    ) {
        try {
            Pessoa pessoaSalvo = pessoaService.atualizar(codigo, pessoa);
            return ResponseEntity.ok(pessoaSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Exclui uma pessoa por ID")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
    public void remover(
            @ApiParam(value = "ID de uma pessoa", example = "1")
            @PathVariable Long codigo
    ) {
        pessoaRepository.deleteById(codigo);
    }

}
