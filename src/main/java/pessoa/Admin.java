package pessoa;

public class Admin extends Pessoa
{
    private static String CODIGO_PERMISSAO = "123";


    public Admin(String name, int age, String email, String password, String phone, String cpf, String codigoPermissao) {
        super(name, age, email, password, phone, cpf);
        if (!codigoPermissao.equals(CODIGO_PERMISSAO)){throw new RuntimeException("Código de permissao incorreto");}
        tipoPessoa = TipoPessoa.ADMIN;
    }
}
