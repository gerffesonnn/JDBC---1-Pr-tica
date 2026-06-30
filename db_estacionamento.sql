CREATE DATABASE IF NOT EXISTS db_estacionamento;
USE db_estacionamento;

CREATE TABLE IF NOT EXISTS vagas_controle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_veiculo VARCHAR(10) NOT NULL,
    vagas_totais INT NOT NULL,
    vagas_ocupadas INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS veiculos_estacionados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    tipo_veiculo VARCHAR(10) NOT NULL,
    horario_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    horario_saida TIMESTAMP NULL,
    valor_pago DECIMAL(10, 2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'Estacionado'
);

CREATE TABLE IF NOT EXISTS pagamentos_estacionamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa_veiculo VARCHAR(10),
    valor_total DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(50) NOT NULL,
    status_pagamento VARCHAR(20) NOT NULL
);

INSERT INTO vagas_controle (tipo_veiculo, vagas_totais, vagas_ocupadas) VALUES 
('carro', 5, 0),
('moto', 3, 3);