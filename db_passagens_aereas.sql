CREATE DATABASE IF NOT EXISTS db_passagens_aereas;
USE db_passagens_aereas;
CREATE TABLE IF NOT EXISTS passageiros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS voos_catalogo (
    numero_voo VARCHAR(10) PRIMARY KEY,
    origem VARCHAR(50) NOT NULL,
    destino VARCHAR(50) NOT NULL,
    preco_original DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS bilhetes_emitidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    passageiro_id INT,
    numero_voo VARCHAR(10),
    classe VARCHAR(20) NOT NULL,
    despachou_mala BOOLEAN DEFAULT FALSE,
    preco_final DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (passageiro_id) REFERENCES passageiros(id),
    FOREIGN KEY (numero_voo) REFERENCES voos_catalogo(numero_voo)
);

INSERT INTO voos_catalogo (numero_voo, origem, destino, preco_original) VALUES 
('AD2026', 'São Paulo (GRU)', 'Orlando (MCO)', 1500.00),
('G31012', 'Rio de Janeiro (GIG)', 'Lisboa (LIS)', 2500.00);