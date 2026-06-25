package db_biblioteca_java;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BibliotecaService servico = new BibliotecaService();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("    SISTEMA DE BIBLIOTECA ESCOLAR  ");
            System.out.println("========================================");
            System.out.println("1. Realizar Empréstimo (História de Usuário)");
            System.out.println("2. Cadastrar Aluno (Insert)");
            System.out.println("3. Remover Aluno (Delete)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Digite a matrícula do aluno: ");
                    int matEmp = scanner.nextInt();
                    servico.realizarEmprestimoFluxo(matEmp);
                    break;
                case 2:
                    System.out.print("Matrícula: ");
                    int matCad = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    servico.cadastrarAluno(matCad, nome);
                    break;
                case 3:
                    System.out.print("Matrícula para exclusão: ");
                    int matDel = scanner.nextInt();
                    servico.deletarAluno(matDel);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}