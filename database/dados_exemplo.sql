-- =====================================================================
-- Chamado Urbano+  |  Dados de exemplo (OPCIONAL, so para testar)
-- Rodar DEPOIS do script.sql e apenas UMA vez (repetir da erro de
-- chave duplicada, porque os ids e e-mails ja existem).
-- =====================================================================

USE chamado_urbano;

INSERT INTO morador (id_morador, nome, endereco, email, telefone)
VALUES
(1, 'Marlene Souza', 'Rua das Palmeiras, 120', 'marlene@email.com', '(44) 99999-1111'),
(2, 'Joao Pereira', 'Avenida Brasil, 450', 'joao@email.com', '(44) 99999-2222'),
(3, 'Ana Lima', 'Rua das Flores, 88', 'ana@email.com', '(44) 99999-3333');

INSERT INTO agente_municipal (id_agente, nome, telefone, matricula, setor)
VALUES
(1, 'Carlos Mendes', '(44) 3000-1111', 'M-1001', 'Obras'),
(2, 'Paula Ribeiro', '(44) 3000-2222', 'M-1002', 'Limpeza Urbana');

INSERT INTO chamado
(id_chamado, id_morador, id_agente, tipo, descricao, localizacao, detalhe, data_abertura, status)
VALUES
(1, 1, 1, 'ILUMINACAO', 'Poste apagado na esquina', 'Rua das Palmeiras, 100', 'Escola', '2026-11-03', 'EM_ANDAMENTO'),
(2, 2, NULL, 'INFRAESTRUTURA', 'Buraco grande na pista', 'Avenida Brasil, 500', 'Avenida', '2026-11-04', 'ABERTO'),
(3, 3, 2, 'LIMPEZA', 'Entulho na calcada', 'Rua das Flores, 90', 'Grande', '2026-11-01', 'RESOLVIDO');

INSERT INTO status_historico
(id_historico, id_chamado, status_anterior, status_novo, data_alteracao)
VALUES
(1, 1, NULL, 'ABERTO', '2026-11-03'),
(2, 1, 'ABERTO', 'EM_ANDAMENTO', '2026-11-04'),
(3, 2, NULL, 'ABERTO', '2026-11-04'),
(4, 3, NULL, 'ABERTO', '2026-11-01'),
(5, 3, 'ABERTO', 'EM_ANDAMENTO', '2026-11-02'),
(6, 3, 'EM_ANDAMENTO', 'RESOLVIDO', '2026-11-05');
