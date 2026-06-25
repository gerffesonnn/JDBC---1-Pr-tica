package db_streaming;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StreamingService servico = new StreamingService();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("     GERENCIAMENTO DE STREAMING (POO)   ");
            System.out.println("========================================");
            System.out.println("1. Criar Nova Conta (História de Usuário)");
            System.out.println("2. Alterar Plano de um Usuário (Update)");
            System.out.println("3. Cancelar e Excluir Conta (Delete)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite seu Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Digite seu E-mail: ");
                    String email = scanner.nextLine();
                    System.out.print("Escolha o Plano (Padrao / Premium): ");
                    String planoEscolhido = scanner.nextLine();

                    ContaStreaming contaFinal;

                    if (planoEscolhido.equalsIgnoreCase("Premium")) {
                        contaFinal = new PlanoPremium(nome, email);
                        servico.registrarNovaAssinatura(contaFinal, "Premium");
                    } else {
                        contaFinal = new PlanoPadrao(nome, email);
                        servico.registrarNovaAssinatura(contaFinal, "Padrao");
                    }
                    break;

                case 2:
                    System.out.print("Digite o ID do Assinante: ");
                    int idUp = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Digite o novo plano (Padrao / Premium): ");
                    String novoPlano = scanner.nextLine();
                    servico.alterarPlanoUsuario(idUp, novoPlano);
                    break;

                case 3:
                    System.out.print("Digite o ID do Assinante para remoção: ");
                    int idDel = scanner.nextInt();
                    servico.cancelarContaDoSistema(idDel);
                    break;

                case 0:
                    System.out.println("Encerrando aplicação de streaming...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}