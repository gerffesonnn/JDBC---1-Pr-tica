CREATE DATABASE IF NOT EXISTS db_agendamento_medico;
USE db_agendamento_medico;

CREATE TABLE IF NOT EXISTS pacientes (
    cpf VARCHAR(14) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS horarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    especialidade VARCHAR(100) NOT NULL,
    data_hora DATETIME NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS consultas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cpf_paciente VARCHAR(14),
    id_horario INT,
    data_agendamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cpf_paciente) REFERENCES pacientes(cpf),
    FOREIGN KEY (id_horario) REFERENCES horarios(id)
);

INSERT INTO pacientes (cpf, nome) VALUES 
('123.456.789-00', 'Rodrigo Alves'),
('987.654.321-11', 'Beatriz Souza');

INSERT INTO horarios (especialidade, data_hora, disponible) VALUES 
('Cardiologia', '2026-07-10 09:00:00', TRUE),
('Cardiologia', '2026-07-10 10:00:00', FALSE),
('Pediatria', '2026-07-11 14:00:00', TRUE);