package db_compra_online;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraService {
    public void processarFluxoCompra(String cpfCliente, int idProduto, int quantidade) {
        String sqlCliente = "SELECT nome, endereco, cartao_aprovado FROM clientes WHERE cpf = ?";
        String sqlProduto = "SELECT nome, preco FROM produtos WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn == null) return;

            String nomeCliente = "";
            String enderecoCliente = "";
            boolean cartaoAprovado = false;
            double precoProduto = 0.0;

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setString(1, cpfCliente);
                ResultSet rsCli = stmtCliente.executeQuery();
                if (rsCli.next()) {
                    nomeCliente = rsCli.getString("nome");
                    enderecoCliente = rsCli.getString("endereco");
                    cartaoAprovado = rsCli.getBoolean("cartao_aprovado");
                } else {
                    System.out.println("\nCliente não cadastrado no sistema.");
                    return;
                }
            }

            try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {
                stmtProduto.setInt(1, idProduto);
                ResultSet rsProd = stmtProduto.executeQuery();
                if (rsProd.next()) {
                    precoProduto = rsProd.getDouble("preco");
                } else {
                    System.out.println("\nProduto selecionado não existe.");
                    return;
                }
            }

            double valorProdutos = precoProduto * quantidade;
            double frete = 0.0;

            if (valorProdutos > 500.00) {
                frete = 0.0;
                System.out.println("\nValor maior que R$ 500,00: Frete Grátis!");
            } else {
                frete = 25.00;
                System.out.println("\nValor abaixo de R$ 500,00: Taxa de frete de R$ 25,00 adicionada.");
            }

            double valorTotalGeral = valorProdutos + frete;

            System.out.println("\n--- Resumo do Carrinho ---");
            System.out.println("Cliente: " + nomeCliente + " | Envio para: " + enderecoCliente);
            System.out.println("Subtotal Itens: R$ " + valorProdutos);
            System.out.println("Total Geral: R$ " + valorTotalGeral);

            if (!cartaoAprovado) {
                System.out.println("\nPagamento não autorizado.");
                System.out.println("Pedido encerrado.");
                return;
            }

            String sqlInserePedido = "INSERT INTO pedidos (cpf_cliente, valor_produtos, valor_frete, valor_total, status) VALUES (?, ?, ?, ?, 'Aprovado')";
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlInserePedido, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setString(1, cpfCliente);
                stmtPedido.setDouble(2, valorProdutos);
                stmtPedido.setDouble(3, frete);
                stmtPedido.setDouble(4, valorTotalGeral);
                stmtPedido.executeUpdate();

                ResultSet rsKeys = stmtPedido.getGeneratedKeys();
                if (rsKeys.next()) {
                    int numeroPedido = rsKeys.getInt(1);
                    System.out.println("\nEnviando confirmação...");
                    System.out.println("Número do Pedido Gerado: #" + numeroPedido);
                    System.out.println("Compra realizada com sucesso.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro na operação de compra: " + e.getMessage());
        }
    }

    public void cadastrarNovoProduto(String nome, double preco) {
        String sql = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setDouble(2, preco);
            stmt.executeUpdate();
            System.out.println("Produto adicionado ao catálogo com sucesso!");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }

    public void alterarEnderecoCliente(String cpf, String novoEndereco) {
        String sql = "UPDATE clientes SET endereco = ? WHERE cpf = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoEndereco);
            stmt.setString(2, cpf);
            stmt.executeUpdate();
            System.out.println("Endereço atualizado no banco.");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }

    public void cancelarPedidoDoBanco(int numeroPedido) {
        String sql = "DELETE FROM pedidos WHERE numero_pedido = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numeroPedido);
            stmt.executeUpdate();
            System.out.println("Registro do pedido removido do banco.");
        } catch (SQLException e) { System.out.println("Erro: " + e.getMessage()); }
    }
}
