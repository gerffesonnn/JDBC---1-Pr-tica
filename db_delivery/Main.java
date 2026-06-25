package db_delivery;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DeliveryService servico = new DeliveryService();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("       SISTEMA DE DELIVERY ONLINE       ");
            System.out.println("========================================");
            System.out.println("1. Fazer um Pedido (História de Usuário)");
            System.out.println("2. Atualizar Status de um Pedido (Update)");
            System.out.println("3. Cancelar/Apagar Pedido (Delete)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("\n--- 🔐 Área de Acesso ---");
                    System.out.print("Digite seu E-mail: ");
                    String email = scanner.nextLine();
                    System.out.print("Digite sua Senha: ");
                    String senha = scanner.nextLine();

                    int usuarioId = servico.realizarLogin(email, senha);
                    if (usuarioId == -1) {
                        System.out.println("Processo encerrado.");
                        break;
                    }

                    servico.exibirCardapio();
                    double subtotalPedido = 0.0;
                    String continuar;

                    do {
                        System.out.print("Digite o código do produto que deseja adicionar ao carrinho: ");
                        int idProd = scanner.nextInt();
                        System.out.print("Quantidade: ");
                        int qtd = scanner.nextInt();
                        scanner.nextLine();

                        double precoProd = servico.obterPrecoProduto(idProd);
                        if (precoProd > 0) {
                            subtotalPedido += (precoProd * qtd);
                            System.out.printf("Adicionado! Subtotal do Carrinho: R$ %.2f\n", subtotalPedido);
                        } else {
                            System.out.println("Código do produto inválido.");
                        }

                        System.out.print("Deseja adicionar mais produtos? (s/n): ");
                        continuar = scanner.nextLine();
                    } while (continuar.equalsIgnoreCase("s"));

                    System.out.println("\nEscolha a Forma de Pagamento:");
                    System.out.println("[1] PIX");
                    System.out.println("[2] Cartão");
                    System.out.println("[3] Dinheiro");
                    System.out.print("Opção: ");
                    int opPag = scanner.nextInt();
                    scanner.nextLine();

                    String formaPagamento = "PIX";
                    if (opPag == 2) formaPagamento = "Cartao";
                    if (opPag == 3) formaPagamento = "Dinheiro";

                    System.out.print("O pagamento foi aprovado pelo banco/maquininha? (true/false): ");
                    boolean aprovado = scanner.nextBoolean();

                    servico.fecharPedido(usuarioId, subtotalPedido, formaPagamento, aprovado);
                    break;

                case 2:
                    System.out.print("Digite o número do pedido para atualizar: ");
                    int numPedUp = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Novo Status (Ex: Pronto para Entrega / Saiu para Entrega): ");
                    String novoStatus = scanner.nextLine();
                    servico.atualizarStatusPedido(numPedUp, novoStatus);
                    break;

                case 3:
                    System.out.print("Digite o número do pedido que deseja cancelar: ");
                    int numPedDel = scanner.nextInt();
                    servico.cancelarPedidoDoSistema(numPedDel);
                    break;

                case 0:
                    System.out.println("Fechando sistema de delivery...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}