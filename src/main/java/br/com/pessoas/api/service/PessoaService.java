package br.com.pessoas.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.pessoas.api.model.Pessoa;
import br.com.pessoas.api.repository.PessoaRepository;
import br.com.pessoas.api.service.exception.PessoaCpfExistenteException;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa salvar(Pessoa pessoa) {
        if (verificaDuplicidadeCpf(pessoa.getCodigo(), pessoa)) {
            throw new PessoaCpfExistenteException();
        } else {
            return pessoaRepository.save(pessoa);
        }
    }

    public Pessoa atualizar(Long codigo, Pessoa pessoa) {
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
        if (verificaDuplicidadeCpf(pessoaSalva.getCodigo(), pessoa)) {
            throw new PessoaCpfExistenteException();
        } else {
            BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
            return pessoaRepository.save(pessoaSalva);
        }
    }

    private boolean verificaDuplicidadeCpf(Long codigo, Pessoa pessoa) {
        if (pessoa == null) {
            return false;
        }
        Pessoa pessoaCpf = buscaPessoaPorCpf(pessoa);
        if (pessoaCpf == null) {
            return false;
        }
        if (pessoaCpf.getCodigo().equals(codigo)) {
            return false;
        }
        return true;
    }


    private Pessoa buscaPessoaPorCpf(Pessoa pessoa) {
        return pessoaRepository.findByCpf(pessoa.getCpf());
    }

    public Pessoa buscarPessoaPeloCodigo(Long codigo) {
        Optional<Pessoa> pessoaSalva = pessoaRepository.findById(codigo);
        if (!pessoaSalva.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }
        return pessoaSalva.get();
    }

}
