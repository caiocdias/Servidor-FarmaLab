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

    pessoa_id INT NOT NULL PRIMARY KEY,
    CONSTRAINT fk_funcionario_pessoa
        FOREIGN KEY (pessoa_id)
            REFERENCES pessoa(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS cliente (
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    pessoa_id INT NOT NULL PRIMARY KEY,
    CONSTRAINT fk_cliente_pessoa
        FOREIGN KEY (pessoa_id)
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

    pessoa_id INT NOT NULL PRIMARY KEY,
    CONSTRAINT fk_medico_parceiro_pessoa
        FOREIGN KEY (pessoa_id)
        REFERENCES pessoa(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS auditoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movimento VARCHAR(255),
    data TIMESTAMP,

    funcionario_id INT NOT NULL,
    CONSTRAINT fk_auditoria_funcionario
        FOREIGN KEY (funcionario_id)
        REFERENCES funcionario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS unidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
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

    unidade_id INT NOT NULL,
    CONSTRAINT fk_estoque_unidade
        FOREIGN KEY (unidade_id)
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

CREATE TABLE IF NOT EXISTS tipo_insumo_tipo_produto (
    id INT AUTO_INCREMENT PRIMARY KEY,

    tipo_insumo_id INT NOT NULL,
    CONSTRAINT fk_tipo_insumo_tipo_produto_tipo_insumo
        FOREIGN KEY (tipo_insumo_id)
        REFERENCES tipo_insumo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    tipo_produto_id INT NOT NULL,
    CONSTRAINT fk_tipo_insumo_tipo_produto_tipo_produto
        FOREIGN KEY (tipo_produto_id)
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

    estoque_id INT NOT NULL,
    CONSTRAINT fk_insumo_estoque
        FOREIGN KEY (estoque_id)
        REFERENCES estoque(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    tipo_insumo_id INT NOT NULL,
    CONSTRAINT fk_insumo_tipo_insumo
        FOREIGN KEY (tipo_insumo_id)
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
    valor_total FLOAT,
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tributo_nota_fiscal (
    id INT AUTO_INCREMENT PRIMARY KEY,

    tributo_id INT NOT NULL,
    CONSTRAINT fk_tributo_nota_fiscal_tributo
        FOREIGN KEY (tributo_id)
        REFERENCES tributo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    nota_fistacal_id INT NOT NULL,
    CONSTRAINT fk_tributo_nota_fiscal_nota_fiscal
        FOREIGN KEY (nota_fistacal_id)
        REFERENCES nota_fiscal(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(20),
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    cliente_id INT NOT NULL,
    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    funcionario_id INT NOT NULL,
    CONSTRAINT fk_pedido_funcionario
        FOREIGN KEY (funcionario_id)
        REFERENCES funcionario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tipo_produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50),
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS venda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    unidade_id INT NOT NULL,
    CONSTRAINT fk_venda_unidade
        FOREIGN KEY (unidade_id)
        REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    nota_fiscal_id INT NOT NULL,
    CONSTRAINT fk_venda_nota_fiscal
        FOREIGN KEY (nota_fiscal_id)
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

    pedido_id INT NOT NULL,
    CONSTRAINT fk_produto_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    venda_id INT,
    CONSTRAINT fk_produto_venda
        FOREIGN KEY (venda_id)
        REFERENCES venda(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    tipo_produto_id INT NOT NULL,
    CONSTRAINT fk_produto_tipo_produto
        FOREIGN KEY (tipo_produto_id)
        REFERENCES tipo_produto(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    estoque_id INT NOT NULL,
    CONSTRAINT fk_produto_estoque
        FOREIGN KEY (estoque_id)
        REFERENCES estoque(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS pescricao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    crm VARCHAR(13),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    cliente_id INT NOT NULL,
    CONSTRAINT fk_pescricao_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS orcamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50),
    status VARCHAR(20),
    habilitado BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    unidade_id INT NOT NULL,
    CONSTRAINT fk_orcamento_unidade
        FOREIGN KEY (unidade_id)
        REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    cliente_id INT NOT NULL,
    CONSTRAINT fk_orcamento_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    funcionario_id INT NOT NULL,
    CONSTRAINT fk_orcamento_funcionario
        FOREIGN KEY (funcionario_id)
        REFERENCES funcionario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);