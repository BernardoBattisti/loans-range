package pessoa;
import leilao.Emprestimo;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.nio.charset.StandardCharsets;

public abstract class Pessoa {
    protected String name;
    protected int id;
    protected int age;
    protected String email;
    public String passwordHash;
    protected String Phone;
    protected String cpf;
    protected TipoPessoa tipoPessoa;


    protected List<Emprestimo> emprestimosTomados = new ArrayList<>();
    protected List<Emprestimo> emprestimosSelecionados = new ArrayList<>();
    protected List<String> operacoes = new ArrayList<>();

    protected Pessoa(String name, int age, String email, String password, String phone, String cpf) {
        this.name = name;
        this.age = age;
        this.email = email;
        try {
            this.passwordHash = generatePasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algoritmo de hash não encontrado.");
            this.passwordHash = null;
        }
        this.Phone = phone;
        this.cpf = cpf;
    }

    public static String generatePasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }



    public void adicionarSelecionado(Emprestimo emprestimo) {
        operacoes.add("Você deu um lance de " + emprestimo.getJurosAtual() + "% de juros em um emprestimo de US$ " + emprestimo.getValroSolicitadoEmDolar());
        emprestimosSelecionados.add(emprestimo);
    }

    public void adicionarEmprestimoTomado(Emprestimo emprestimo) {
        emprestimosTomados.add(emprestimo);
    }

    public void adicionarOperacao(String operacao) {
        operacoes.add(operacao);
    }



    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<Emprestimo> getEmprestimosTomados() {
        return emprestimosTomados;
    }

    public List<Emprestimo> getEmprestimosSelecionados() {
        return emprestimosSelecionados;
    }

    public List<String> getOperacoes() {
        return operacoes;
    }
}