package main;

import leilao.LeilaoService;
import pessoa.Pessoa;
import pessoa.UserService;
import database.Database;

import java.util.Scanner;

public class Main {



    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        UserService service = new UserService();
        Database.initialize();

        boolean sair = false;
        while (!sair) {
            System.out.println("|----------Loan's--Range----------|");
            System.out.println("|-Selecione uma das opções abaixo-|");
            System.out.println("|                                 |");
            System.out.println("|-0-Sair                          |");
            System.out.println("|-1-Logar                         |");
            System.out.println("|-2-Cadastrar                     |");
            System.out.println("|---------------------------------|");

            int opcao;
            try {
                String linhaOpcao = input.nextLine();
                opcao = Integer.parseInt(linhaOpcao);
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 0:
                    sair = true;
                    System.out.println("Saindo... Obrigado por usar o Loan's Range!");
                    break;

                case 1:
                    Pessoa pessoa = service.logarUsuario(input);
                    if (pessoa != null) {
                        iniciarProgramaLogado(pessoa, input);
                    }
                    break;

                case 2:
                    iniciarCadastro(input, service);
                    break;

                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }
        }
        input.close();
    }


    private static void iniciarProgramaLogado(Pessoa pessoa, Scanner input) {
        ui.MenuUI menu = new ui.MenuUI(pessoa, input);
        menu.exibirMenuPrincipal();
    }

    private static void iniciarCadastro(Scanner input, UserService service) {
        try {
            System.out.println("--- Novo Cadastro ---");
            System.out.println("Digite o nome da pessoa:");
            String name = input.nextLine();

            System.out.println("Digite sua idade: ");
            int age = Integer.parseInt(input.nextLine());

            System.out.println("Digite seu e-mail: ");
            String email = input.nextLine();

            System.out.println("Crie uma senha: ");
            String password = input.nextLine();

            System.out.println("Digite seu cpf: ");
            String cpf = input.nextLine();

            System.out.println("Digite o número de contato: ");
            String phone = input.nextLine();

            System.out.println("Digite o tipo de pessoa (ADMIN ou USUARIO):");
            String tipoPessoa = input.nextLine();

            String codigoPermissao = null;
            if (tipoPessoa.equalsIgnoreCase("ADMIN")) {
                System.out.println("Digite o código de permissão: ");
                codigoPermissao = input.nextLine();
            }

            service.cadastrarUsuario(name, age, email, password, phone, cpf, tipoPessoa, codigoPermissao);

        } catch (RuntimeException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
        }
    }
}