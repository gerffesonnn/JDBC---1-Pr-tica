package db_agendamento_medico;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AgendamentoService servico = new AgendamentoService();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("     SISTEMA DE AGENDAMENTO MÉDICO       ");
            System.out.println("========================================");
            System.out.println("1. Marcar Nova Consulta (História de Usuário)");
            System.out.println("2. Cadastrar Novo Paciente (Insert)");
            System.out.println("3. Tornar Horário Disponível Novamente (Update)");
            System.out.println("4. Cancelar Registro de Consulta (Delete)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o CPF do Paciente: ");
                    String cpf = scanner.nextLine();
                    System.out.print("Qual especialidade deseja (Ex: Cardiologia)? ");
                    String esp = scanner.nextLine();
                    servico.processarAgendamentoFluxo(cpf, esp);
                    break;

                case 2:
                    System.out.print("CPF do Paciente: ");
                    String cpfCad = scanner.nextLine();
                    System.out.print("Nome Completo: ");
                    String nome = scanner.nextLine();
                    servico.cadastrarNovoPaciente(cpfCad, nome);
                    break;

                case 3:
                    System.out.print("Digite o código do horário para liberar: ");
                    int idHor = scanner.nextInt();
                    servico.liberarHorarioBloqueado(idHor);
                    break;

                case 4:
                    System.out.print("Digite o código ID da consulta para remoção: ");
                    int idCons = scanner.nextInt();
                    servico.cancelarConsultaDoSistema(idCons);
                    break;

                case 0:
                    System.out.println("Fechando sistema de agendamentos...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}
