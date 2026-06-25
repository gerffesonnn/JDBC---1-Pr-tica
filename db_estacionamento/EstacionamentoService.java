package db_estacionamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class EstacionamentoService {

    public void registrarEntrada(String placa, String tipo) {
        String sqlVagas = "SELECT vagas_totais, vagas_ocupadas FROM vagas_controle WHERE tipo_veiculo = ?";
        String sqlEntrada = "INSERT INTO veiculos_estacionados (placa, tipo_veiculo) VALUES (?, ?)";
        String sqlAtualizaVaga = "UPDATE vagas_controle SET vagas_ocupadas = vagas_ocupadas + 1 WHERE tipo_veiculo = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;

            try (PreparedStatement stmtVagas = conn.prepareStatement(sqlVagas)) {
                stmtVagas.setString(1, tipo.toLowerCase());
                ResultSet rs = stmtVagas.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("vagas_ocupadas") >= rs.getInt("vagas_totais")) {
                        System.out.println("\nEstacionamento lotado.");
                        return;
                    }
                } else {
                    System.out.println("\nTipo de veículo inválido.");
                    return;
                }
            }
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmtEntrada = conn.prepareStatement(sqlEntrada)) {
                    stmtEntrada.setString(1, placa.toUpperCase());
                    stmtEntrada.setString(2, tipo.toLowerCase());
                    stmtEntrada.executeUpdate();
                }
                try (PreparedStatement stmtAtu = conn.prepareStatement(sqlAtualizaVaga)) {
                    stmtAtu.setString(1, tipo.toLowerCase());
                    stmtAtu.executeUpdate();
                }
                conn.commit();
                System.out.println("\nEntrada registrada! Placa: " + placa.toUpperCase() + " | Tipo: " + tipo);
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao registrar entrada: " + e.getMessage());
            }
        } catch (SQLException e) { System.out.println("Erro de banco: " + e.getMessage()); }
    }
    public void processarSaida(String placa, int horasSimuladas, boolean pagamentoAprovado, String formaPagamento) {
        String sqlBusca = "SELECT tipo_veiculo, horario_entrada FROM veiculos_estacionados WHERE placa = ? AND status = 'Estacionado'";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;

            String tipo = "";
            if (horasSimuladas < 1) horasSimuladas = 1;

            try (PreparedStatement stmtBusca = conn.prepareStatement(sqlBusca)) {
                stmtBusca.setString(1, placa.toUpperCase());
                ResultSet rs = stmtBusca.executeQuery();
                if (rs.next()) {
                    tipo = rs.getString("tipo_veiculo");
                } else {
                    System.out.println("\nVeículo não localizado ou já liberado.");
                    return;
                }
            }
            double valorTotal = 10.00;
            if (horasSimuladas > 1) {
                valorTotal += (horasSimuladas - 1) * 5.00;
            }

            System.out.println("\n--- Resumo de Permanência ---");
            System.out.println("Tempo estimado digitado: " + horasSimuladas + " hora(s) | Valor Total: R$ " + valorTotal);

            if (!pagamentoAprovado) {
                System.out.println("\nPagamento não autorizado.");
                return;
            }

            conn.setAutoCommit(false);
            try {
                String sqlPag = "INSERT INTO pagamentos_estacionamento (placa_veiculo, valor_total, forma_pagamento, status_pagamento) VALUES (?, ?, ?, 'Aprovado')";
                try (PreparedStatement stmtPag = conn.prepareStatement(sqlPag)) {
                    stmtPag.setString(1, placa.toUpperCase());
                    stmtPag.setDouble(2, valorTotal);
                    stmtPag.setString(3, formaPagamento);
                    stmtPag.executeUpdate();
                }

                String sqlFinVeiculo = "UPDATE veiculos_estacionados SET horario_saida = CURRENT_TIMESTAMP, valor_pago = ?, status = 'Liberado' WHERE placa = ?";
                try (PreparedStatement stmtFin = conn.prepareStatement(sqlFinVeiculo)) {
                    stmtFin.setDouble(1, valorTotal);
                    stmtFin.setString(2, placa.toUpperCase());
                    stmtFin.executeUpdate();
                }

                String sqlLiberaVaga = "UPDATE vagas_controle SET vagas_ocupadas = vagas_ocupadas - 1 WHERE tipo_veiculo = ?";
                try (PreparedStatement stmtVaga = conn.prepareStatement(sqlLiberaVaga)) {
                    stmtVaga.setString(1, tipo);
                    stmtVaga.executeUpdate();
                }

                conn.commit();
                System.out.println("\nSaída liberada.");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro no checkout: " + e.getMessage());
            }
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }

    public void excluirRegistroDoHistorico(String placa) {
        String sql = "DELETE FROM veiculos_estacionados WHERE placa = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, placa.toUpperCase());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                System.out.println("Registro da placa " + placa.toUpperCase() + " foi permanentemente apagado.");
            } else {
                System.out.println("Nenhuma movimentação ativa encontrada para essa placa.");
            }
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }
}