CREATE DATABASE IF NOT EXISTS db_compra_online;
USE db_compra_online;

CREATE TABLE IF NOT EXISTS clientes (
    cpf VARCHAR(14) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    cartao_aprovado BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS pedidos (
    numero_pedido INT AUTO_INCREMENT PRIMARY KEY,
    cpf_cliente VARCHAR(14),
    valor_produtos DECIMAL(10, 2) NOT NULL,
    valor_frete DECIMAL(10, 2) NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'Pendente',
    FOREIGN KEY (cpf_cliente) REFERENCES clientes(cpf)
);

INSERT INTO produtos (nome, preco) VALUES 
('Smartphone Avançado', 1200.00),
('Fone de Ouvido Bluetooth', 150.00),
('Livro de Java Clean Code', 90.00);

INSERT INTO clientes (cpf, nome, endereco, cartao_aprovado) VALUES 
('111.111.111-11', 'Lucas Silva', 'Rua das Flores, 123', TRUE),
('222.222.222-22', 'Mariana Costa', 'Av. Central, 456', FALSE);