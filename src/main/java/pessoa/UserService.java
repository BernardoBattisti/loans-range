package pessoa;

import database.Database;

import java.sql.*;
import java.security.NoSuchAlgorithmException;
// A importação de List e ArrayList não é mais necessária aqui
import java.util.Scanner;

public class UserService {


    public void cadastrarUsuario(String name, int age, String email, String password, String phone, String cpf, String tipoPessoa, String codigoPermissao) {


        if (tipoPessoa.equalsIgnoreCase("ADMIN")) {
            try {
                new Admin(name, age, email, password, phone, cpf, codigoPermissao);
            } catch (RuntimeException e) {
                System.out.println("Erro no cadastro: " + e.getMessage());
                return;
            }
        }


        String passwordHash;
        try {
            passwordHash = Pessoa.generatePasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erro crítico: Algoritmo hash não encontrado.");
            throw new RuntimeException(e);
        }



        String sql = "INSERT INTO pessoas (name, age, email, passwordHash, phone, cpf, tipoPessoa) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";


        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.setString(4, passwordHash);
            pstmt.setString(5, phone);
            pstmt.setString(6, cpf);
            pstmt.setString(7, tipoPessoa.toUpperCase());

            pstmt.executeUpdate();

            System.out.println(tipoPessoa + " " + name + " cadastrado com sucesso!");

        } catch (SQLException e) {

            if (e.getMessage().contains("UNIQUE constraint failed: pessoas.email")) {
                System.err.println("Erro no cadastro: E-mail já cadastrado.");
            } else if (e.getMessage().contains("UNIQUE constraint failed: pessoas.cpf")) {
                System.err.println("Erro no cadastro: CPF já cadastrado.");
            } else {
                System.err.println("Erro no cadastro (SQL): " + e.getMessage());
            }

        } catch (RuntimeException e) {

            System.err.println("Erro no cadastro: " + e.getMessage());
        }
    }



    public Pessoa logarUsuario(Scanner input)
    {


        System.out.println("Digite o seu e-mail: ");
        String email = input.nextLine();
        System.out.println("Digite sua senha: ");
        String passwordInput = input.nextLine();

        String passwordHash;
        try {

            passwordHash = Pessoa.generatePasswordHash(passwordInput);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erro crítico: Algoritmo hash não encontrado.");
            throw new RuntimeException(e);
        }

        String sql = "SELECT * FROM pessoas WHERE email = ? AND passwordHash = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                {
                    System.out.println("Login realizado com sucesso!");


                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String dbEmail = rs.getString("email");
                    String dbPhone = rs.getString("phone");
                    String dbCpf = rs.getString("cpf");
                    String tipoPessoa = rs.getString("tipoPessoa");

                    Pessoa pessoaLogada;


                    if (tipoPessoa.equals("ADMIN")) {

                        pessoaLogada = new Admin(name, age, dbEmail, "bla-bla-bla", dbPhone, dbCpf, "123");
                    } else {
                        pessoaLogada = new Usuario(name, age, dbEmail, "bla-bla-bla", dbPhone, dbCpf);
                    }


                    pessoaLogada.passwordHash = passwordHash;
                    pessoaLogada.setId(id);

                    return pessoaLogada;

                } else {
                    System.out.println("Usuário ou senha inválidos!");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao tentar logar: " + e.getMessage());
            return null;
        }
    }


    public static Pessoa findPessoaById(int id) {
        String sql = "SELECT * FROM pessoas WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String dbEmail = rs.getString("email");
                    String dbPhone = rs.getString("phone");
                    String dbCpf = rs.getString("cpf");
                    String tipoPessoa = rs.getString("tipoPessoa");
                    String dbPasswordHash = rs.getString("passwordHash");

                    Pessoa pessoa;

                    if (tipoPessoa.equals("ADMIN")) {
                        pessoa = new Admin(name, age, dbEmail, "bla-bla-bla", dbPhone, dbCpf, "123");
                    } else {
                        pessoa = new Usuario(name, age, dbEmail, "bla-bla-bla", dbPhone, dbCpf);
                    }

                    pessoa.setId(id);
                    pessoa.passwordHash = dbPasswordHash;
                    return pessoa;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pessoa por ID: " + e.getMessage());
        }
        return null;
    }
}