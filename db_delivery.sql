CREATE DATABASE IF NOT EXISTS db_delivery;
USE db_delivery;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(50) NOT NULL,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS pedidos (
    numero_pedido INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    valor_produtos DECIMAL(10, 2) NOT NULL,
    taxa_entrega DECIMAL(10, 2) NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(20) NOT NULL,
    status_pedido VARCHAR(50) DEFAULT 'Enviado para a Cozinha',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

INSERT INTO usuarios (email, senha, nome) VALUES 
('cliente@email.com', '123456', 'João Silva'),
('ana@email.com', 'abcde', 'Ana Souza');

INSERT INTO produtos (nome, preco) VALUES 
('Hambúrguer Artesanal', 35.00),
('Batata Frita Família', 20.00),
('Refrigerante Lata', 6.00),
('Pizza Grande Margherita', 55.00);