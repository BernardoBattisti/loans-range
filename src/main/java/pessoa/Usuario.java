package pessoa;

public class Usuario extends Pessoa
{
    public Usuario(String name, int age, String email, String password, String phone, String cpf)
    {
        super(name, age, email, password, phone, cpf);
        tipoPessoa = TipoPessoa.USUARIO;
    }
}
