package db_compra_online;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CompraService servico = new CompraService();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("      SISTEMA DE COMPRA ONLINE (JAVA)    ");
            System.out.println("========================================");
            System.out.println("1. Finalizar Compra do Carrinho (História de Usuário)");
            System.out.println("2. Adicionar Novo Produto ao Catálogo (Insert)");
            System.out.println("3. Atualizar Endereço do Cliente (Update)");
            System.out.println("4. Excluir/Cancelar Registro de Pedido (Delete)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Informe o CPF do cliente: ");
                    String cpf = scanner.nextLine();
                    System.out.print("Código do Produto desejado (1, 2 ou 3): ");
                    int idProd = scanner.nextInt();
                    System.out.print("Quantidade: ");
                    int qtd = scanner.nextInt();

                    servico.processarFluxoCompra(cpf, idProd, qtd);
                    break;

                case 2:
                    System.out.print("Nome do Produto: ");
                    String nomeProd = scanner.nextLine();
                    System.out.print("Preço: R$ ");
                    double preco = scanner.nextDouble();
                    servico.cadastrarNovoProduto(nomeProd, preco);
                    break;

                case 3:
                    System.out.print("Informe o CPF do Cliente: ");
                    String cpfAlt = scanner.nextLine();
                    System.out.print("Novo Endereço Completo: ");
                    String novoEnd = scanner.nextLine();
                    servico.alterarEnderecoCliente(cpfAlt, novoEnd);
                    break;

                case 4:
                    System.out.print("Digite o número do pedido para exclusão: ");
                    int numPed = scanner.nextInt();
                    servico.cancelarPedidoDoBanco(numPed);
                    break;

                case 0:
                    System.out.println("Encerrando o sistema de compras...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}
