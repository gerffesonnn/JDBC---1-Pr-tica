CREATE DATABASE IF NOT EXISTS db_streaming;
USE db_streaming;

CREATE TABLE IF NOT EXISTS assinantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS detalhes_planos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_plano VARCHAR(20) NOT NULL UNIQUE, -- 'Padrao' ou 'Premium'
    preco_base DECIMAL(10, 2) NOT NULL,
    qualidade_video VARCHAR(30) NOT NULL,
    limite_telas INT NOT NULL,
    permite_download BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS assinaturas_ativas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    assinante_id INT,
    tipo_plano VARCHAR(20),
    status_assinatura VARCHAR(20) DEFAULT 'Ativo',
    FOREIGN KEY (assinante_id) REFERENCES assinantes(id),
    FOREIGN KEY (tipo_plano) REFERENCES detalhes_planos(tipo_plano)
);

INSERT INTO detalhes_planos (tipo_plano, preco_base, qualidade_video, limite_telas, permite_download) VALUES 
('Padrao', 30.00, 'Full HD', 2, FALSE),
('Premium', 50.00, '4K Ultra HD', 4, TRUE);