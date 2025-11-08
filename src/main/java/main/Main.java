package main;

import leilao.LeilaoService;
import pessoa.Pessoa;
import pessoa.UserService;
import database.Database;
import pessoa.UserSignInVerification;
import pessoa.ValidationException;

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


            String name = askForName(input);
            int age = askForAge(input);
            String email = askForEmail(input);
            String password = askForPassword(input);
            String cpf = askForCpf(input);
            String phone = askForPhone(input);
            String tipoPessoa = askForTipoPessoa(input);

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



    private static String askForName(Scanner input) {
        while (true) {
            try {
                System.out.println("Digite o nome da pessoa:");
                String name = input.nextLine();
                UserSignInVerification.validateName(name);
                return name;
            } catch (ValidationException e) {
                System.out.println(COR_VERMELHA + "Erro: " + e.getMessage() + " Tente novamente." + RESETAR_COR);
            }
        }
    }

    private static int askForAge(Scanner input) {
        while (true) {
            try {
                System.out.println("Digite sua idade: ");
                String ageInput = input.nextLine();
                return UserSignInVerification.validateAge(ageInput);
            } catch (ValidationException e) {
                System.out.println(COR_VERMELHA + "Erro: " + e.getMessage() + " Tente novamente." + RESETAR_COR);
            }
        }
    }

    private static String askForEmail(Scanner input) {
        while (true) {
            try {
                System.out.println("Digite seu e-mail: ");
                String email = input.nextLine();
                UserSignInVerification.validateEmail(email);
                return email;
            } catch (ValidationException e) {
                System.out.println(COR_VERMELHA + "Erro: " + e.getMessage() + " Tente novamente." + RESETAR_COR);
            }
        }
    }

    private static String askForPassword(Scanner input) {
        while (true) {
            try {
                System.out.println("Crie uma senha (mínimo 8 caracteres, 1 número): ");
                String password = input.nextLine();
                UserSignInVerification.validatePassword(password);
                return password;
            } catch (ValidationException e) {
                System.out.println(COR_VERMELHA + "Erro: " + e.getMessage() + " Tente novamente." + RESETAR_COR);
            }
        }
    }

    private static String askForCpf(Scanner input) {
        while (true) {
            try {
                System.out.println("Digite seu cpf (apenas números): ");
                String cpf = input.nextLine();
                UserSignInVerification.validateCpf(cpf);
                return cpf.replaceAll("[^0-9]", "");
            } catch (ValidationException e) {
                System.out.println(COR_VERMELHA + "Erro: " + e.getMessage() + " Tente novamente." + RESETAR_COR);
            }
        }
    }

    private static String askForPhone(Scanner input) {
        while (true) {
            try {
                System.out.println("Digite o número de contato (com DDD): ");
                String phone = input.nextLine();
                UserSignInVerification.validatePhone(phone);
                return phone.replaceAll("[^0-9]", "");
            } catch (ValidationException e) {
                System.out.println(COR_VERMELHA + "Erro: " + e.getMessage() + " Tente novamente." + RESETAR_COR);
            }
        }
    }

    private static String askForTipoPessoa(Scanner input) {
        while (true) {
            try {
                System.out.println("Digite o tipo de pessoa (ADMIN ou USUARIO):");
                String tipoPessoa = input.nextLine();
                UserSignInVerification.validateTipoPessoa(tipoPessoa);
                return tipoPessoa;
            } catch (ValidationException e) {
                System.out.println(COR_VERMELHA + "Erro: " + e.getMessage() + " Tente novamente." + RESETAR_COR);
            }
        }
    }
    private static final String COR_VERMELHA = "\u001b[31m";
    private static final String RESETAR_COR = "\u001b[0m";
}