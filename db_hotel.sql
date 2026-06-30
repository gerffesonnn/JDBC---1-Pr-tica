CREATE DATABASE IF NOT EXISTS db_hotel;
USE db_hotel;

CREATE TABLE IF NOT EXISTS hospedes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS quartos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    capacidade INT NOT NULL,
    preco_diaria DECIMAL(10, 2) NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS reservas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hospede_id INT,
    quarto_id INT,
    quantidade_diarias INT NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(30) NOT NULL,
    status_reserva VARCHAR(30) DEFAULT 'Confirmada',
    FOREIGN KEY (hospede_id) REFERENCES hospedes(id),
    FOREIGN KEY (quarto_id) REFERENCES quartos(id)
);

INSERT INTO hospedes (cpf, nome) VALUES 
('111.222.333-44', 'Carlos Eduardo'),
('555.666.777-88', 'Mariana Costa');

INSERT INTO quartos (numero, cidade, capacidade, preco_diaria, disponivel) VALUES 
('101', 'Rio de Janeiro', 2, 200.00, TRUE),
('102', 'Rio de Janeiro', 4, 350.00, TRUE),
('201', 'São Paulo', 2, 250.00, TRUE),
('202', 'São Paulo', 3, 180.00, FALSE);