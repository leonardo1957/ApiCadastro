CREATE TABLE pessoa (
  codigo BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(145) NOT NULL,
  sexo VARCHAR(45) NULL,
  email VARCHAR(45) NULL,
  data_nascimento DATE NOT NULL,
  naturalidade VARCHAR(145) NULL,
  nacionalidade VARCHAR(145) NULL,
  cpf VARCHAR(14) NOT NULL,
  data_do_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  data_da_alteracao TIMESTAMP NULL)
 ENGINE=InnoDB DEFAULT CHARSET=utf8;


