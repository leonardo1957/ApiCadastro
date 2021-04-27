package br.com.pessoas.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.pessoas.api.config.property.PessoasApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(PessoasApiProperty.class)
public class PessoasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PessoasApiApplication.class, args);
	}
}
