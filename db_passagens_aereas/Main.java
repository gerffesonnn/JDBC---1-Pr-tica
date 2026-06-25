package db_passagens_aereas;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        VendaPassagemService servico = new VendaPassagemService();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("     SISTEMA DE PASSAGENS AÉREAS        ");
            System.out.println("========================================");
            System.out.println("1. Emitir Passagem/Bilhete (História de Usuário)");
            System.out.println("2. Alterar Voo de um Bilhete (Update)");
            System.out.println("3. Cancelar Passagem Emitida (Delete)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome Completo do Passageiro: ");
                    String nome = scanner.nextLine();
                    System.out.print("Número do Voo (Ex: AD2026 / G31012): ");
                    String voo = scanner.nextLine();

                    double precoOriginal = servico.buscarPrecoOriginalVoo(voo);
                    if (precoOriginal == -1) {
                        System.out.println("Voo informado não está registrado na regulamentação da empresa.");
                        break;
                    }

                    System.out.println("Escolha a Classe:");
                    System.out.println("[1] Classe Econômica");
                    System.out.println("[2] Classe Executiva");
                    System.out.print("Opção: ");
                    int opClasse = scanner.nextInt();
                    scanner.nextLine();

                    Passagem passagemFinal;

                    if (opClasse == 2) {
                        passagemFinal = new ClasseExecutiva(nome, voo, precoOriginal);
                        servico.emitirBilhete(passagemFinal, "Executiva", true);
                    } else {
                        System.out.print("Deseja despachar uma mala extra por R$ 120,00? (true / false): ");
                        boolean querMala = scanner.nextBoolean();
                        passagemFinal = new ClasseEconomica(nome, voo, precoOriginal, querMala);
                        servico.emitirBilhete(passagemFinal, "Economica", querMala);
                    }
                    break;

                case 2:
                    System.out.print("Informe o Número do Bilhete: ");
                    int idUp = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Informe o código do Novo Voo desejado: ");
                    String novoVoo = scanner.nextLine();

                    double novoPrecoBase = servico.buscarPrecoOriginalVoo(novoVoo);
                    if (novoPrecoBase == -1) {
                        System.out.println("Novo voo inválido.");
                        break;
                    }
                    servico.alterarVooDoBilhete(idUp, novoVoo, novoPrecoBase);
                    break;

                case 3:
                    System.out.print("Digite o número do bilhete para cancelamento: ");
                    int idDel = scanner.nextInt();
                    servico.cancelarBilheteDoSistema(idDel);
                    break;

                case 0:
                    System.out.println("Finalizando sistema de vendas aéreas...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}