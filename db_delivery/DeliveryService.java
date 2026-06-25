package db_delivery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeliveryService {

    public int realizarLogin(String email, String senha) {
        String sql = "SELECT id, nome FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nBem-vindo(a), " + rs.getString("nome") + "!");
                return rs.getInt("id"); // Retorna o ID do usuário logado
            } else {
                System.out.println("\nUsuário ou senha inválidos.");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Erro no login: " + e.getMessage());
            return -1;
        }
    }

    public void exibirCardapio() {
        String sql = "SELECT id, nome, preco FROM produtos";
        System.out.println("\n--- CARDÁPIO DO RESTAURANTE ---");
        try (Connection conn = ConexaoBanco.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("[%d] %-25s - R$ %.2f\n",
                        rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco"));
            }
            System.out.println("----------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao carregar cardápio: " + e.getMessage());
        }
    }

    public double obterPrecoProduto(int idProduto) {
        String sql = "SELECT preco FROM produtos WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("preco");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }
        return 0.0;
    }

    public void fecharPedido(int usuarioId, double valorProdutos, String formaPagamento, boolean pagamentoAprovado) {
        if (!pagamentoAprovado) {
            System.out.println("\nPagamento não autorizado.");
            return;
        }

        double taxaEntrega = 0.00;
        if (valorProdutos < 50.00) {
            taxaEntrega = 8.00;
            System.out.println("Pedido inferior a R$50,00. Taxa de entrega adicionada: R$ 8,00");
        } else {
            System.out.println("Pedido igual ou superior a R$50,00. Frete Grátis!");
        }

        double valorTotal = valorProdutos + taxaEntrega;
        String sql = "INSERT INTO pedidos (usuario_id, valor_produtos, taxa_entrega, valor_total, forma_pagamento) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, usuarioId);
            stmt.setDouble(2, valorProdutos);
            stmt.setDouble(3, taxaEntrega);
            stmt.setDouble(4, valorTotal);
            stmt.setString(5, formaPagamento);
            stmt.executeUpdate();

            ResultSet rsKeys = stmt.getGeneratedKeys();
            if (rsKeys.next()) {
                int numeroPedido = rsKeys.getInt(1);
                System.out.println("\n-------------------------------------------");
                System.out.println("NÚMERO DO PEDIDO GERADO: #" + numeroPedido);
                System.out.println("Pedido enviado para a cozinha!");
                System.out.println("Status: Atualizado para 'Enviado para a Cozinha'");
                System.out.println("Pedido realizado com sucesso.");
                System.out.println("-------------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao registrar pedido: " + e.getMessage());
        }
    }

    public void atualizarStatusPedido(int numeroPedido, String novoStatus) {
        String sql = "UPDATE pedidos SET status_pedido = ? WHERE numero_pedido = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, numeroPedido);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Status do pedido #" + numeroPedido + " alterado para: " + novoStatus);
            } else {
                System.out.println("Pedido não localizado.");
            }
        } catch (SQLException e) { System.out.println("Erro no update: " + e.getMessage()); }
    }

    public void cancelarPedidoDoSistema(int numeroPedido) {
        String sql = "DELETE FROM pedidos WHERE numero_pedido = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numeroPedido);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Pedido #" + numeroPedido + " cancelado e removido com sucesso.");
            } else {
                System.out.println("Pedido não localizado.");
            }
        } catch (SQLException e) { System.out.println("Erro no delete: " + e.getMessage()); }
    }
}