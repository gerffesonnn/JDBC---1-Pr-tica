package db_streaming;

import java.sql.*;

public class StreamingService {

    public void registrarNovaAssinatura(ContaStreaming conta, String tipoPlano) {
        String sqlAssinante = "INSERT INTO assinantes (nome_usuario, email) VALUES (?, ?)";
        String sqlVinculo = "INSERT INTO assinaturas_ativas (assinante_id, tipo_plano) VALUES (?, ?)";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;
            conn.setAutoCommit(false);

            try {
                int idAssinante = -1;
                try (PreparedStatement stmtAss = conn.prepareStatement(sqlAssinante, Statement.RETURN_GENERATED_KEYS)) {
                    stmtAss.setString(1, conta.getNomeUsuario());
                    stmtAss.setString(2, conta.getEmail());
                    stmtAss.executeUpdate();

                    ResultSet rs = stmtAss.getGeneratedKeys();
                    if (rs.next()) idAssinante = rs.getInt(1);
                }

                try (PreparedStatement stmtVin = conn.prepareStatement(sqlVinculo)) {
                    stmtVin.setInt(1, idAssinante);
                    stmtVin.setString(2, tipoPlano);
                    stmtVin.executeUpdate();
                }

                conn.commit();
                System.out.println("\n=========================================");
                System.out.println("RECIBO DA ASSINATURA GERADO COM SUCESSO");
                System.out.println("=========================================");
                System.out.println(conta.toString()); // Usa o polimorfismo do toString()
                System.out.println("Conta realizada com sucesso.");
                System.out.println("=========================================");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao processar assinatura: " + e.getMessage());
            }
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }

    public void alterarPlanoUsuario(int idAssinante, String novoPlano) {
        String sql = "UPDATE assinaturas_ativas SET tipo_plano = ? WHERE assinante_id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoPlano);
            stmt.setInt(2, idAssinante);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Plano atualizado com sucesso para " + novoPlano + "!");
            } else {
                System.out.println("Usuário não localizado.");
            }
        } catch (SQLException e) { System.out.println("Erro no Update: " + e.getMessage()); }
    }

    public void cancelarContaDoSistema(int idAssinante) {
        String sqlVinculo = "DELETE FROM assinaturas_ativas WHERE assinante_id = ?";
        String sqlAssinante = "DELETE FROM assinantes WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;
            conn.setAutoCommit(false);

            try (PreparedStatement stmtV = conn.prepareStatement(sqlVinculo);
                 PreparedStatement stmtA = conn.prepareStatement(sqlAssinante)) {

                stmtV.setInt(1, idAssinante);
                stmtV.executeUpdate();

                stmtA.setInt(1, idAssinante);
                int rows = stmtA.executeUpdate();

                if (rows > 0) {
                    conn.commit();
                    System.out.println("✔Conta e assinatura permanentemente removidas do sistema.");
                } else {
                    System.out.println("Usuário não encontrado.");
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro no cancelamento: " + e.getMessage());
            }
        } catch (SQLException e) { System.out.println("Erro de banco: " + e.getMessage()); }
    }
}