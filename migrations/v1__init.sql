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

CREATE TABLE IF NOT EXISTS nota_fiscal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    num_nota INT NOT NULL,
    data_emissao TIMESTAMP,
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tributo_nota_fiscal (
    id INT AUTO_INCREMENT PRIMARY KEY,

    id_tributo INT NOT NULL,
    CONSTRAINT fk_tributo_nota_fiscal_tributo
        FOREIGN KEY (id_tributo)
        REFERENCES tributo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    id_nota_fiscal INT NOT NULL,
    CONSTRAINT fk_tributo_nota_fiscal_nota_fiscal
        FOREIGN KEY (id_nota_fiscal)
        REFERENCES nota_fiscal(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
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
        ON UPDATE CASCADE,

    id_nota_fiscal INT NOT NULL,
    CONSTRAINT fk_venda_nota_fiscal
        FOREIGN KEY (id_nota_fiscal)
        REFERENCES nota_fiscal(id)
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