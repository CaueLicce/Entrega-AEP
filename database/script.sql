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
CREATE TABLE IF NOT EXISTS chamado (
    id_chamado    INT NOT NULL AUTO_INCREMENT,
    id_morador    INT NOT NULL,
    id_agente     INT NULL,
    tipo          VARCHAR(20) NOT NULL,
    descricao     VARCHAR(255) NOT NULL,
    localizacao   VARCHAR(150) NOT NULL,
    detalhe       VARCHAR(100) NOT NULL,
    data_abertura DATE NOT NULL,
    status        VARCHAR(20) NOT NULL DEFAULT 'ABERTO',
    CONSTRAINT pk_chamado PRIMARY KEY (id_chamado),
    CONSTRAINT fk_chamado_morador FOREIGN KEY (id_morador)
        REFERENCES morador (id_morador) ON DELETE RESTRICT,
    CONSTRAINT fk_chamado_agente FOREIGN KEY (id_agente)
        REFERENCES agente_municipal (id_agente) ON DELETE RESTRICT,
    CONSTRAINT chk_chamado_tipo
        CHECK (tipo IN ('INFRAESTRUTURA', 'ILUMINACAO', 'LIMPEZA')),
    CONSTRAINT chk_chamado_status
        CHECK (status IN ('ABERTO', 'EM_ANDAMENTO', 'RESOLVIDO'))
) ENGINE = InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS status_historico (
    id_historico    INT NOT NULL AUTO_INCREMENT,
    id_chamado      INT NOT NULL,
    status_anterior VARCHAR(20),
    status_novo     VARCHAR(20) NOT NULL,
    data_alteracao  DATE NOT NULL,
    CONSTRAINT pk_historico PRIMARY KEY (id_historico),
    CONSTRAINT fk_historico_chamado FOREIGN KEY (id_chamado)
        REFERENCES chamado (id_chamado) ON DELETE RESTRICT,
    CONSTRAINT chk_historico_novo
        CHECK (status_novo IN ('ABERTO', 'EM_ANDAMENTO', 'RESOLVIDO'))
) ENGINE = InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

DELIMITER $$

DROP TRIGGER IF EXISTS trg_chamado_before_delete $$

CREATE TRIGGER trg_chamado_before_delete
BEFORE DELETE ON chamado
FOR EACH ROW
BEGIN
    DELETE FROM status_historico WHERE id_chamado = OLD.id_chamado;
END $$

DELIMITER ;
