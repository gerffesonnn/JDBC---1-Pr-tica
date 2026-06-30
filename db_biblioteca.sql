CREATE DATABASE IF NOT EXISTS db_biblioteca;
USE db_biblioteca;

CREATE TABLE IF NOT EXISTS alunos (
    matricula INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    possui_multa BOOLEAN DEFAULT FALSE,
    total_emprestados INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS livros (
    codigo INT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS emprestimos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricula_aluno INT,
    codigo_livro INT,
    data_emprestimo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
    FOREIGN KEY (codigo_livro) REFERENCES livros(codigo)
);