using Loans_Range;
using System;
using System.Collections.Generic;
using Loans_Range.cadastro;
using Loans_Range.login_sys;

class main
{
    public static List<userData> users = new List<userData>();

    public static void Main()
    {
        Console.WriteLine("Bem vindo ao LOANS RANGE!");

        char option = '0';
        do
        {
            Console.WriteLine("Escolha uma opção: ");
            Console.WriteLine("1 - Cadastrar usuário");
            Console.WriteLine("2 - Fazer login");
            Console.WriteLine("3 - Sair");

            var input = Console.ReadLine();
            if (string.IsNullOrWhiteSpace(input) || input.Length != 1 || !"123".Contains(input))
            {
                Console.WriteLine("Opção inválida, tente novamente.");
                continue;
            }
            option = input[0];

            if (option == '1')
            {
                // Chama o sistema de cadastro
                cad_sys cadastro = new cad_sys();
                cadastro.cadastrarUsuario();

                // Cria o usuário com os dados do cadastro
                userData usuario = new userData(
                    cadastro.name,
                    cadastro.email,
                    cadastro.password,
                    cadastro.phone,
                    cadastro.cpf,
                    cadastro.address
                );
                users.Add(usuario);
                Console.WriteLine("Usuário cadastrado com sucesso!");
            }
            else if (option == '2')
            {
                Console.WriteLine("Digite seu email: ");
                string email = Console.ReadLine();
                Console.WriteLine("Digite sua senha: ");
                string password = Console.ReadLine();

                bool loginSuccess = login_sys.login_verify(email, password);
                if (loginSuccess)
                {
                    var usuario = users.Find(u => u.Email.Equals(email, StringComparison.OrdinalIgnoreCase));
                    Console.WriteLine("Bem-vindo ao sistema, " + usuario?.Name + "!");
                }
                else
                {
                    Console.WriteLine("Email ou senha incorretos. Tente novamente.");
                }
            }
            else if (option == '3')
            {
                Console.WriteLine("Saindo do sistema...");
            }
        } while (option != '3');
    }
}