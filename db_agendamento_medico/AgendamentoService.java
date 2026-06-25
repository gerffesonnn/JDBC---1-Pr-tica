package db_agendamento_medico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AgendamentoService {

    public void processarAgendamentoFluxo(String cpf, String especialidade) {
        String sqlPaciente = "SELECT nome FROM pacientes WHERE cpf = ?";
        String sqlHorarios = "SELECT id, data_hora FROM horarios WHERE especialidade = ? AND disponivel = TRUE";
        String sqlVerificaHorario = "SELECT disponivel FROM horarios WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;

            try (PreparedStatement stmtPac = conn.prepareStatement(sqlPaciente)) {
                stmtPac.setString(1, cpf);
                ResultSet rsPac = stmtPac.executeQuery();
                if (!rsPac.next()) {
                    System.out.println("\nPaciente não cadastrado.");
                    System.out.println("Processo encerrado.");
                    return;
                }
            }

            boolean encontrouHorarios = false;
            try (PreparedStatement stmtHor = conn.prepareStatement(sqlHorarios)) {
                stmtHor.setString(1, especialidade);
                ResultSet rsHor = stmtHor.executeQuery();

                System.out.println("\n--- Horários Livres para " + especialidade + " ---");
                while (rsHor.next()) {
                    encontrouHorarios = true;
                    System.out.println("Código do Horário: [" + rsHor.getInt("id") + "] | Data/Hora: " + rsHor.getTimestamp("data_hora"));
                }
            }

            if (!encontrouHorarios) {
                System.out.println("\nNão há horários disponíveis.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("\nInforme o Código do Horário desejado: ");
            int idHorarioEscolhido = scanner.nextInt();

            try (PreparedStatement stmtVerif = conn.prepareStatement(sqlVerificaHorario)) {
                stmtVerif.setInt(1, idHorarioEscolhido);
                ResultSet rsVerif = stmtVerif.executeQuery();

                if (!rsVerif.next() || !rsVerif.getBoolean("disponivel")) {
                    System.out.println("\nHorário indisponível.");
                    return;
                }
            }

            conn.setAutoCommit(false);
            try {
                String sqlInsereConsulta = "INSERT INTO consultas (cpf_paciente, id_horario) VALUES (?, ?)";
                try (PreparedStatement stmtInsere = conn.prepareStatement(sqlInsereConsulta)) {
                    stmtInsere.setString(1, cpf);
                    stmtInsere.setInt(2, idHorarioEscolhido);
                    stmtInsere.executeUpdate();
                }

                String sqlAtualizaHorario = "UPDATE horarios SET disponivel = FALSE WHERE id = ?";
                try (PreparedStatement stmtAtu = conn.prepareStatement(sqlAtualizaHorario)) {
                    stmtAtu.setInt(1, idHorarioEscolhido);
                    stmtAtu.executeUpdate();
                }

                conn.commit();
                System.out.println("\nEnviando confirmação...");
                System.out.println("Consulta agendada com sucesso.");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro na transação médica: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Erro de banco: " + e.getMessage());
        }
    }

    public void cadastrarNovoPaciente(String cpf, String nome) {
        String sql = "INSERT INTO pacientes (cpf, nome) VALUES (?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.executeUpdate();
            System.out.println("Paciente cadastrado com sucesso!");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }

    public void liberarHorarioBloqueado(int idHorario) {
        String sql = "UPDATE horarios SET disponivel = TRUE WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHorario);
            stmt.executeUpdate();
            System.out.println("Horário reaberto na agenda médica.");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }

    public void cancelarConsultaDoSistema(int idConsulta) {
        String sql = "DELETE FROM consultas WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.executeUpdate();
            System.out.println("Consulta removida do histórico.");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }
}
