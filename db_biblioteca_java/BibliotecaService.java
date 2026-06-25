package db_biblioteca_java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BibliotecaService {

    public void realizarEmprestimoFluxo(int matricula) {
        String sqlAluno = "SELECT possui_multa, total_emprestados FROM alunos WHERE matricula = ?";
        String sqlLivro = "SELECT disponivel FROM livros WHERE codigo = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;

            try (PreparedStatement stmtAluno = conn.prepareStatement(sqlAluno)) {
                stmtAluno.setInt(1, matricula);
                ResultSet rsAluno = stmtAluno.executeQuery();

                if (!rsAluno.next()) {
                    System.out.println("\nAluno não encontrado.");
                    return;
                }

                if (rsAluno.getBoolean("possui_multa")) {
                    System.out.println("\nEmpréstimo bloqueado por multa.");
                    return;
                }

                if (rsAluno.getInt("total_emprestados") >= 3) {
                    System.out.println("\nLimite de empréstimos atingido.");
                    return;
                }
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Informe o código do livro: ");
            int codigoLivro = scanner.nextInt();

            try (PreparedStatement stmtLivro = conn.prepareStatement(sqlLivro)) {
                stmtLivro.setInt(1, codigoLivro);
                ResultSet rsLivro = stmtLivro.executeQuery();

                if (!rsLivro.next()) {
                    System.out.println("\nLivro não cadastrado.");
                    return;
                }

                if (!rsLivro.getBoolean("disponivel")) {
                    System.out.println("\nLivro indisponível.");
                    return;
                }
            }

            conn.setAutoCommit(false);
            try {
                String insereEmprestimo = "INSERT INTO emprestimos (matricula_aluno, codigo_livro) VALUES (?, ?)";
                try (PreparedStatement stmtInsere = conn.prepareStatement(insereEmprestimo)) {
                    stmtInsere.setInt(1, matricula);
                    stmtInsere.setInt(2, codigoLivro);
                    stmtInsere.executeUpdate();
                }

                String atualizaLivro = "UPDATE livros SET disponivel = FALSE WHERE codigo = ?";
                try (PreparedStatement stmtActLivro = conn.prepareStatement(atualizaLivro)) {
                    stmtActLivro.setInt(1, codigoLivro);
                    stmtActLivro.executeUpdate();
                }

                String atualizaAluno = "UPDATE alunos SET total_emprestados = total_emprestados + 1 WHERE matricula = ?";
                try (PreparedStatement stmtActAluno = conn.prepareStatement(atualizaAluno)) {
                    stmtActAluno.setInt(1, matricula);
                    stmtActAluno.executeUpdate();
                }

                conn.commit();
                System.out.println("\nEmpréstimo realizado com sucesso.");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro na transação: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Erro de banco: " + e.getMessage());
        }
    }

    public void cadastrarAluno(int matricula, String nome) {
        String sql = "INSERT INTO alunos (matricula, nome) VALUES (?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matricula);
            stmt.setString(2, nome);
            stmt.executeUpdate();
            System.out.println("Aluno cadastrado com sucesso!");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }

    public void deletarAluno(int matricula) {
        String sql = "DELETE FROM alunos WHERE matricula = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matricula);
            stmt.executeUpdate();
            System.out.println("Aluno removido do sistema.");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }
}