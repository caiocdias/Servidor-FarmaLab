CREATE DATABASE IF NOT EXISTS FarmaLab;
USE FarmaLab;

CREATE TABLE IF NOT EXISTS pessoa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    endereco VARCHAR(100),
    telefone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS funcionario (
    cargo VARCHAR(50),
    password VARCHAR(255),
    salario FLOAT,
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_pessoa INT NOT NULL PRIMARY KEY,
    CONSTRAINT fk_funcionario_pessoa
        FOREIGN KEY (id_pessoa)
            REFERENCES pessoa(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS cliente (
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_pessoa INT NOT NULL PRIMARY KEY,
    CONSTRAINT fk_cliente_pessoa
        FOREIGN KEY (id_pessoa)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS medico_parceiro (
    crm VARCHAR(7),
    estado VARCHAR(2),
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_pessoa INT NOT NULL PRIMARY KEY,
    CONSTRAINT fk_medico_parceiro_pessoa
        FOREIGN KEY (id_pessoa)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS auditoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movimento VARCHAR(255),
    data TIMESTAMP,

    id_funcionario INT NOT NULL,
    CONSTRAINT fk_auditoria_funcionario
        FOREIGN KEY (id_funcionario)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS unidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome varchar(50),
    cep VARCHAR(9),
    cidade VARCHAR(20),
    bairro VARCHAR(20),
    rua VARCHAR(20),
    complemento VARCHAR(20),
    estado VARCHAR(2),
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS estoque (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50),
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_unidade INT NOT NULL,
    CONSTRAINT fk_estoque_unidade
        FOREIGN KEY (id_unidade)
        REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tipo_insumo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50),
    quant FLOAT,
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tipo_produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50),
    instrucoes VARCHAR(255),
    habilitado BOOLEAN,
    valor_base FLOAT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tipo_insumo_tipo_produto (
    id INT AUTO_INCREMENT PRIMARY KEY,

    id_tipo_insumo INT NOT NULL,
    CONSTRAINT fk_tipo_insumo_tipo_produto_tipo_insumo
        FOREIGN KEY (id_tipo_insumo)
        REFERENCES tipo_insumo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_tipo_produto INT NOT NULL,
    CONSTRAINT fk_tipo_insumo_tipo_produto_tipo_produto
        FOREIGN KEY (id_tipo_produto)
        REFERENCES tipo_produto(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS insumo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quant FLOAT,
    data_validade TIMESTAMP,
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_estoque INT NOT NULL,
    CONSTRAINT fk_insumo_estoque
        FOREIGN KEY (id_estoque)
        REFERENCES estoque(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_tipo_insumo INT NOT NULL,
    CONSTRAINT fk_insumo_tipo_insumo
        FOREIGN KEY (id_tipo_insumo)
        REFERENCES tipo_insumo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tributo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(2),
    nome_imposto VARCHAR(20),
    porcentagem FLOAT,
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS prescricao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    crm VARCHAR(13),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_cliente INT NOT NULL,
    CONSTRAINT fk_prescricao_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(20),
    habilitado BOOLEAN,
    pronta_entrega BOOLEAN,
    desconto_total FLOAT,
    valor_total_base FLOAT,
    valor_final FLOAT,
    tributo_total FLOAT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_cliente INT NOT NULL,
    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_funcionario INT NOT NULL,
    CONSTRAINT fk_pedido_funcionario
        FOREIGN KEY (id_funcionario)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	
    id_prescricao INT NOT NULL,
    CONSTRAINT fk_pedido_prescricao
        FOREIGN KEY (id_prescricao)
        REFERENCES prescricao(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_unidade INT NOT NULL,
    CONSTRAINT fk_pedido_unidade
        FOREIGN KEY (id_unidade)
        REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS venda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    habilitado BOOLEAN,

    id_unidade INT NOT NULL,
    CONSTRAINT fk_venda_unidade
        FOREIGN KEY (id_unidade)
        REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_pedido INT NOT NULL,
    CONSTRAINT fk_venda_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedido(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_validade TIMESTAMP,
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pronta_entrega BOOLEAN,
    coletado BOOLEAN,

    id_pedido_producao INT,
    CONSTRAINT fk_produto_pedido_producao
        FOREIGN KEY (id_pedido_producao)
        REFERENCES pedido(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_pedido_venda INT,
    CONSTRAINT fk_produto_pedido_venda
        FOREIGN KEY (id_pedido_venda)
        REFERENCES pedido(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_venda INT,
    CONSTRAINT fk_produto_venda
        FOREIGN KEY (id_venda)
        REFERENCES venda(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_tipo_produto INT NOT NULL,
    CONSTRAINT fk_produto_tipo_produto
        FOREIGN KEY (id_tipo_produto)
        REFERENCES tipo_produto(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_estoque INT NOT NULL,
    CONSTRAINT fk_produto_estoque
        FOREIGN KEY (id_estoque)
        REFERENCES estoque(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS orcamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50),
    status VARCHAR(50),
    habilitado BOOLEAN,
    observacoes VARCHAR(50),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_unidade INT NOT NULL,
    CONSTRAINT fk_orcamento_unidade
        FOREIGN KEY (id_unidade)
        REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_cliente INT NOT NULL,
    CONSTRAINT fk_orcamento_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_funcionario INT NOT NULL,
    CONSTRAINT fk_orcamento_funcionario
        FOREIGN KEY (id_funcionario)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS unidade_tributo (
    id  INT AUTO_INCREMENT PRIMARY KEY,

    id_tributo INT NOT NULL,
    CONSTRAINT fk_unidade_tributo_tributo
        FOREIGN KEY (id_tributo)
        REFERENCES tributo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_unidade INT NOT NULL,
    CONSTRAINT fk_unidade_tributo_unidade
        FOREIGN KEY (id_unidade)
        REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

INSERT INTO pessoa (nome, cpf) VALUES ('Administrador','123.456.789-10');
INSERT INTO funcionario (id_pessoa, cargo, password, habilitado) VALUES ((SELECT id FROM pessoa WHERE cpf = '123.456.789-10'),'adm','123456',TRUE);

INSERT INTO tipo_produto (nome, instrucoes, habilitado, valor_base) VALUES
('Cápsula de Melatonina', 'Pesar os ativos, misturar em excipiente adequado e encapsular.', TRUE, 30.00),
('Creme Hidratante com Ureia', 'Homogeneizar os ingredientes na base cremosa e embalar.', TRUE, 50.00),
('Xarope de Própolis', 'Dissolver o extrato de própolis em veículo adequado e homogeneizar.', TRUE, 28.00),
('Suplemento Proteico', 'Misturar as proteínas em pó com estabilizantes e embalar.', TRUE, 65.00),
('Loção Capilar Antiqueda', 'Dissolver os ativos na base alcoólica e homogeneizar.', TRUE, 45.50),
('Colírio Lubrificante', 'Diluir o princípio ativo em solução isotônica e esterilizar.', TRUE, 35.00),
('Óleo Corporal Relaxante', 'Misturar os óleos essenciais na base oleosa e embalar.', TRUE, 55.00),
('Sabonete Líquido Glicerinado', 'Dissolver os agentes de limpeza na base aquosa e homogeneizar.', TRUE, 20.00),
('Chá Detox em Sachês', 'Misturar as ervas secas e embalar em sachês individuais.', TRUE, 18.00),
('Gel Analgésico', 'Misturar os ativos anti-inflamatórios na base gel e homogeneizar.', TRUE, 40.00);

INSERT INTO tipo_insumo (nome, quant, habilitado) VALUES
('Extrato seco de Ginseng', 100.0, TRUE),  -- Cápsula Fitoterápica
('Óleo de Rosa Mosqueta', 50.0, TRUE),  -- Cremes Dermatológicos
('Extrato fluido de Guaco', 200.0, TRUE),  -- Xarope Fitoterápico
('Proteína Hidrolisada', 500.0, TRUE),  -- Suplemento em Pó
('Minoxidil 5%', 30.0, TRUE),  -- Loção Capilar
('Hipromelose 0,3%', 10.0, TRUE),  -- Colírio Manipulado
('Óleo Essencial de Lavanda', 20.0, TRUE),  -- Óleo Essencial
('Base Glicerinada Vegetal', 1000.0, TRUE),  -- Sabonete Glicerinado
('Camomila Desidratada', 50.0, TRUE),  -- Chá Medicinal
('Extrato de Arnica', 100.0, TRUE);  -- Gel Anti-inflamatório

INSERT INTO tipo_insumo_tipo_produto (id_tipo_insumo, id_tipo_produto) VALUES
(1, 1),  -- Extrato seco de Ginseng → Cápsula Fitoterápica
(2, 2),  -- Óleo de Rosa Mosqueta → Cremes Dermatológicos
(3, 3),  -- Extrato fluido de Guaco → Xarope Fitoterápico
(4, 4),  -- Proteína Hidrolisada → Suplemento em Pó
(5, 5),  -- Minoxidil 5% → Loção Capilar
(6, 6),  -- Hipromelose 0,3% → Colírio Manipulado
(7, 7),  -- Óleo Essencial de Lavanda → Óleo Essencial
(8, 8),  -- Base Glicerinada Vegetal → Sabonete Glicerinado
(9, 9),  -- Camomila Desidratada → Chá Medicinal
(10, 10); -- Extrato de Arnica → Gel Anti-inflamatório

use farmalab;
select * from pedido;
select * from pedido join produto on pedido.id = produto.id_pedido_venda;

