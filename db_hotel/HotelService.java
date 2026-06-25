package db_hotel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HotelService {

    public int buscarHospedePorCpf(String cpf) {
        String sql = "SELECT id FROM hospedes WHERE cpf = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) { System.out.println("Erro ao buscar hóspede: " + e.getMessage()); }
        return -1;
    }

    public boolean listarQuartosDisponiveis(String cidade, int hospedes) {
        String sql = "SELECT id, numero, preco_diaria FROM quartos WHERE LOWER(cidade) = LOWER(?) AND capacidade >= ? AND disponivel = TRUE";
        boolean encontrou = false;

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cidade);
            stmt.setInt(2, hospedes);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- QUARTOS DISPONÍVEIS ENCONTRADOS ---");
            while (rs.next()) {
                encontrou = true;
                System.out.printf("Código do Quarto: [%d] | Quarto Nº: %s | Valor da Diária: R$ %.2f\n",
                        rs.getInt("id"), rs.getString("numero"), rs.getDouble("preco_diaria"));
            }
            System.out.println("------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao buscar quartos: " + e.getMessage());
        }
        return encontrou;
    }

    public double obterPrecoDiaria(int quartoId) {
        String sql = "SELECT preco_diaria FROM quartos WHERE id = ? AND disponivel = TRUE";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quartoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("preco_diaria");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
        return 0.0;
    }

    // --- REQUISITO: INSERT & UPDATE (Processar Reserva com Regra de Desconto e Ocupação) ---
    public void finalizarReserva(int hospedeId, int quartoId, int numDiarias, String formaPagamento, boolean pagamentoAprovado) {
        if (!pagamentoAprovado) {
            System.out.println("\nPagamento recusado.");
            return;
        }

        double precoDiaria = obterPrecoDiaria(quartoId);
        if (precoDiaria == 0.0) {
            System.out.println("\nQuarto inválido ou já ocupado.");
            return;
        }

        double valorBruto = precoDiaria * numDiarias;
        double valorTotal = valorBruto;

        if (numDiarias > 5) {
            double desconto = valorBruto * 0.10;
            valorTotal = valorBruto - desconto;
            System.out.printf("Estadia superior a 5 diárias! Desconto de 10%% aplicado (Economia de R$ %.2f)\n", desconto);
        }

        String sqlReserva = "INSERT INTO reservas (hospede_id, quarto_id, quantidade_diarias, valor_total, forma_pagamento) VALUES (?, ?, ?, ?, ?)";
        String sqlOcupaQuarto = "UPDATE quartos SET disponivel = FALSE WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;
            conn.setAutoCommit(false); // Transação atômica

            try {
                try (PreparedStatement stmtRes = conn.prepareStatement(sqlReserva, Statement.RETURN_GENERATED_KEYS)) {
                    stmtRes.setInt(1, hospedeId);
                    stmtRes.setInt(2, quartoId);
                    stmtRes.setInt(3, numDiarias);
                    stmtRes.setDouble(4, valorTotal);
                    stmtRes.setString(5, formaPagamento);
                    stmtRes.executeUpdate();

                    ResultSet rsKeys = stmtRes.getGeneratedKeys();
                    if (rsKeys.next()) {
                        System.out.println("\n-------------------------------------------");
                        System.out.println("NÚMERO DA RESERVA GERADO: #" + rsKeys.getInt(1));
                    }
                }

                try (PreparedStatement stmtQuarto = conn.prepareStatement(sqlOcupaQuarto)) {
                    stmtQuarto.setInt(1, quartoId);
                    stmtQuarto.executeUpdate();
                }

                conn.commit();
                System.out.println("Enviando confirmação por e-mail...");
                System.out.println("Reserva confirmada.");
                System.out.println("-------------------------------------------");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro na transação de reserva: " + e.getMessage());
            }
        } catch (SQLException e) { System.out.println("Erro de conexão: " + e.getMessage()); }
    }

    public void cadastrarHospede(String cpf, String nome) {
        String sql = "INSERT INTO hospedes (cpf, nome) VALUES (?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.executeUpdate();
            System.out.println("Hóspede cadastrado com sucesso no sistema!");
        } catch (SQLException e) { System.out.println("Erro ao cadastrar: " + e.getMessage()); }
    }

    public void cancelarReservaDoSistema(int idReserva, int idQuarto) {
        String sqlDelete = "DELETE FROM reservas WHERE id = ?";
        String sqlLiberaQuarto = "UPDATE quartos SET disponivel = TRUE WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;
            conn.setAutoCommit(false);

            try (PreparedStatement stmtDel = conn.prepareStatement(sqlDelete);
                 PreparedStatement stmtLib = conn.prepareStatement(sqlLiberaQuarto)) {

                stmtDel.setInt(1, idReserva);
                int linhas = stmtDel.executeUpdate();

                if (linhas > 0) {
                    stmtLib.setInt(1, idQuarto);
                    stmtLib.executeUpdate();
                    conn.commit();
                    System.out.println("Reserva #" + idReserva + " removida e quarto correspondente liberado!");
                } else {
                    System.out.println("Código de reserva não encontrado.");
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao cancelar reserva: " + e.getMessage());
            }
        } catch (SQLException e) { System.out.println("Erro de banco: " + e.getMessage()); }
    }
}