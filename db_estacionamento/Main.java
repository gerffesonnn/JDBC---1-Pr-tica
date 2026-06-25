package db_estacionamento;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EstacionamentoService servico = new EstacionamentoService();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("   CONTROLE DE ESTACIONAMENTO (JAVA)    ");
            System.out.println("========================================");
            System.out.println("1. Registrar Entrada do Motorista (Check-in)");
            System.out.println("2. Processar Saída e Cobrança (Check-out)");
            System.out.println("3. Excluir Histórico de Movimentação (Delete)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite a Placa do Veículo: ");
                    String placaEnt = scanner.nextLine();
                    System.out.print("Tipo do veículo (carro / moto): ");
                    String tipo = scanner.nextLine();
                    servico.registrarEntrada(placaEnt, tipo);
                    break;

                case 2:
                    System.out.print("Digite a Placa de Saída: ");
                    String placaSai = scanner.nextLine();
                    System.out.print("Quantas horas o veículo permaneceu estacionado? ");
                    int horas = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Forma de Pagamento (Cartao / Dinheiro / Pix): ");
                    String forma = scanner.nextLine();

                    System.out.print("O pagamento foi aprovado na maquininha? (true / false): ");
                    boolean aprovado = scanner.nextBoolean();

                    servico.processarSaida(placaSai, horas, aprovado, forma);
                    break;

                case 3:
                    System.out.print("Informe a placa para apagar permanentemente do banco: ");
                    String placaDel = scanner.nextLine();
                    servico.excluirRegistroDoHistorico(placaDel);
                    break;

                case 0:
                    System.out.println("Finalizando aplicação do estacionamento...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}