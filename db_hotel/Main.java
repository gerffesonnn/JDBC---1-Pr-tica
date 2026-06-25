package db_hotel;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HotelService servico = new HotelService();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("     SISTEMA DE RESERVA DE HOTEL       ");
            System.out.println("========================================");
            System.out.println("1. Efetuar Nova Reserva (História de Usuário)");
            System.out.println("2. Cadastrar Novo Hóspede (Insert)");
            System.out.println("3. Excluir/Cancelar Reserva Existente (Delete)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Informe o CPF do Hóspede: ");
                    String cpf = scanner.nextLine();
                    int hospedeId = servico.buscarHospedePorCpf(cpf);

                    if (hospedeId == -1) {
                        System.out.println("\nHóspede não localizado. Use a Opção 2 para cadastrá-lo primeiro.");
                        break;
                    }

                    System.out.print("Para qual Cidade deseja viajar? ");
                    String cidade = scanner.nextLine();
                    System.out.print("Data de Check-in (AAAA-MM-DD): ");
                    String checkin = scanner.nextLine();
                    System.out.print("Data de Check-out (AAAA-MM-DD): ");
                    String checkout = scanner.nextLine();
                    System.out.print("Quantidade de hóspedes no quarto: ");
                    int qtdPessoas = scanner.nextInt();
                    scanner.nextLine();

                    boolean temQuarto = servico.listarQuartosDisponiveis(cidade, qtdPessoas);
                    if (!temQuarto) {
                        System.out.println("\nNenhum quarto disponível.");
                        break;
                    }

                    System.out.print("Digite o Código do Quarto escolhido: ");
                    int quartoEscolhido = scanner.nextInt();
                    System.out.print("Quantas diárias deseja reservar? ");
                    int numDiarias = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("\nEscolha a Forma de Pagamento (Pix / Cartao / Dinheiro): ");
                    String formaPag = scanner.nextLine();

                    System.out.print("O pagamento foi aprovado pelo gateway do banco? (true / false): ");
                    boolean pago = scanner.nextBoolean();

                    servico.finalizarReserva(hospedeId, quartoEscolhido, numDiarias, formaPag, pago);
                    break;

                case 2:
                    System.out.print("Digite o CPF do novo Hóspede: ");
                    String novoCpf = scanner.nextLine();
                    System.out.print("Nome Completo: ");
                    String nome = scanner.nextLine();
                    servico.cadastrarHospede(novoCpf, nome);
                    break;

                case 3:
                    System.out.print("Digite o ID da reserva que deseja cancelar: ");
                    int idRes = scanner.nextInt();
                    System.out.print("Digite o ID do quarto que estava vinculado a ela: ");
                    int idQua = scanner.nextInt();
                    servico.cancelarReservaDoSistema(idRes, idQua);
                    break;

                case 0:
                    System.out.println("Fechando sistema de reservas online...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}