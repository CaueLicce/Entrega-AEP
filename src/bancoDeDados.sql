

CREATE DATABASE IF NOT EXISTS chamado_urbano
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE chamado_urbano;

CREATE TABLE IF NOT EXISTS morador (
    id_morador INT NOT NULL AUTO_INCREMENT,
    nome       VARCHAR(100) NOT NULL,
    endereco   VARCHAR(150) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    telefone   VARCHAR(20),
    CONSTRAINT pk_morador PRIMARY KEY (id_morador),
    CONSTRAINT uk_morador_email UNIQUE (email)
) ENGINE = InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS agente_municipal (
    id_agente INT NOT NULL AUTO_INCREMENT,
    nome      VARCHAR(100) NOT NULL,
    telefone  VARCHAR(20),
    matricula VARCHAR(30) NOT NULL,
    setor     VARCHAR(60) NOT NULL,
    CONSTRAINT pk_agente PRIMARY KEY (id_agente),
    CONSTRAINT uk_agente_matricula UNIQUE (matricula)
) ENGINE = InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
