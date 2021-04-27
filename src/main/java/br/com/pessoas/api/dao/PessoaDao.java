package br.com.pessoas.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pessoas.api.model.Pessoa;
import br.com.pessoas.api.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>,PessoaRepositoryQuery {
	public Pessoa findByCpf(String cpf);
	public Optional<Pessoa> findByEmail(String email);
	
}
