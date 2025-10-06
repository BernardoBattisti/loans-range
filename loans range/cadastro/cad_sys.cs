using Loans_Range.cadastro;
using System;
using System.Collections.Generic;
//Sistema geral de verificacao de dados para cadastro
public class cad_sys
{
    public string name;
    public string email;
    public string password;
    public string phone;
    public string cpf;
    public string address;
    public void cadastrarUsuario()
	{
        //Instancia o verificador de dados

        verify verify = new verify();




        //Obtem o nome do usuario
       
        do
        {
            Console.Write("Digite seu nome: ");
            this.name = Console.ReadLine();
            Console.WriteLine();

        } while (!verify.verifyName(this.name));
        Console.Write("Nome cadastrado com sucesso!");






        //Obtem o email do usuario
        do
        {
            Console.WriteLine();
            Console.Write("Digite seu email: ");
            this.email = Console.ReadLine();
            Console.WriteLine();
        } while (!verify.verifyEmail(this.email));
        Console.Write("Email cadastrado com sucesso!");





        //Obtem a senha do usuario
        
        Console.WriteLine();
        Console.Write("Digite sua senha: ");
        this.password = Console.ReadLine();
        Console.WriteLine();
        Console.Write("Senha cadastrada com sucesso!");




        //Obtem o telefone do usuario
        Console.WriteLine();
        do 
        {
           
            Console.Write("Digite seu telefone: ");
            this.phone = Console.ReadLine();
            Console.WriteLine();    

        }while (!verify.verifyPhone(this.phone));
        Console.Write("Telefone cadastrado com sucesso!");




        //Obtem o cpf do usuario

        Console.WriteLine();
        do
        {
            Console.Write("Digite seu CPF: ");
            this.cpf = Console.ReadLine();
            Console.WriteLine();
        } while (!verify.verifyCpf(this.cpf));
        Console.Write("CPF cadastrado com sucesso!");


        //Obtem o endereco do usuario

        Console.WriteLine();
        Console.Write("Digite seu endereco: ");
        this.address = Console.ReadLine();
        Console.WriteLine();
        Console.Write("Endereco cadastrado com sucesso!");
        Console.WriteLine();

        //Exibe dados do usuario
        Console.WriteLine();
        Console.WriteLine("Dados do usuario cadastrados com sucesso!");
        Console.WriteLine("Nome: " + this.name);
        Console.WriteLine("Email: " + this.email);
        Console.WriteLine("Telefone: " + this.phone);
        Console.WriteLine("CPF: " + this.cpf);
        Console.WriteLine("Endereco: " + this.address);
        Console.WriteLine("Senha: " + this.password);
        Console.WriteLine();



    }


}
