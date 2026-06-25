package db_passagens_aereas;

import java.sql.*;

public class VendaPassagemService {

    public double buscarPrecoOriginalVoo(String numeroVoo) {
        String sql = "SELECT preco_original FROM voos_catalogo WHERE UPPER(numero_voo) = UPPER(?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroVoo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("preco_original");
        } catch (SQLException e) { System.out.println("Erro ao buscar voo: " + e.getMessage()); }
        return -1;
    }

    public void emitirBilhete(Passagem passagem, String classe, boolean despachouMala) {
        String sqlPassageiro = "INSERT INTO passageiros (nome) VALUES (?)";
        String sqlBilhete = "INSERT INTO bilhetes_emitidos (passageiro_id, numero_voo, classe, despachou_mala, preco_final) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;
            conn.setAutoCommit(false);

            try {
                int idPassageiro = -1;
                try (PreparedStatement stmtPass = conn.prepareStatement(sqlPassageiro, Statement.RETURN_GENERATED_KEYS)) {
                    stmtPass.setString(1, passagem.getNomePassageiro());
                    stmtPass.executeUpdate();
                    ResultSet rs = stmtPass.getGeneratedKeys();
                    if (rs.next()) idPassageiro = rs.getInt(1);
                }

                try (PreparedStatement stmtBilhete = conn.prepareStatement(sqlBilhete, Statement.RETURN_GENERATED_KEYS)) {
                    stmtBilhete.setInt(1, idPassageiro);
                    stmtBilhete.setString(2, passagem.getNumeroVoo().toUpperCase());
                    stmtBilhete.setString(3, classe);
                    stmtBilhete.setBoolean(4, despachouMala);
                    stmtBilhete.setDouble(5, passagem.calcularPrecoFinal());
                    stmtBilhete.executeUpdate();

                    ResultSet rsKeys = stmtBilhete.getGeneratedKeys();
                    if (rsKeys.next()) {
                        System.out.println("\n=========================================");
                        System.out.println("       BILHETE DE EMBARQUE EMITIDO       ");
                        System.out.println("=========================================");
                        System.out.println("NÚMERO DO BILHETE: #" + rsKeys.getInt(1));
                        System.out.println(passagem.toString());
                        System.out.println("=========================================");
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println(" Erro ao emitir passagem: " + e.getMessage());
            }
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }

    public void alterarVooDoBilhete(int idBilhete, String novoVoo, double novoPrecoFinal) {
        String sql = "UPDATE bilhetes_emitidos SET numero_voo = ?, preco_final = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoVoo.toUpperCase());
            stmt.setDouble(2, novoPrecoFinal);
            stmt.setInt(3, idBilhete);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" Bilhete #" + idBilhete + " remarcado para o voo " + novoVoo.toUpperCase() + " com sucesso!");
            } else {
                System.out.println(" Bilhete não encontrado.");
            }
        } catch (SQLException e) { System.out.println("Erro no Update: " + e.getMessage()); }
    }

    public void cancelarBilheteDoSistema(int idBilhete) {
        String sql = "DELETE FROM bilhetes_emitidos WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBilhete);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" Passagem #" + idBilhete + " cancelada com sucesso. Reembolso processado.");
            } else {
                System.out.println(" Bilhete não localizado.");
            }
        } catch (SQLException e) { System.out.println("Erro no Delete: " + e.getMessage()); }
    }
}