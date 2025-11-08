package pessoa;

import java.util.regex.Pattern;


public class UserSignInVerification {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE
    );

    public static void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty() || name.length() < 3) {
            throw new ValidationException("O nome deve ter pelo menos 3 caracteres.");
        }
    }


    public static int validateAge(String ageInput) throws ValidationException {
        try {
            int age = Integer.parseInt(ageInput);
            if (age < 18) {
                throw new ValidationException("Você deve ter pelo menos 18 anos para se cadastrar.");
            }
            return age;
        } catch (NumberFormatException e) {
            throw new ValidationException("Idade inválida. Por favor, digite um número.");
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("O e-mail fornecido não é válido.");
        }
    }

    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.length() < 8) {
            throw new ValidationException("A senha deve ter pelo menos 8 caracteres.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("A senha deve conter pelo menos um número.");
        }
    }

    public static void validateCpf(String cpf) throws ValidationException {
        String cpfDigits = cpf.replaceAll("[^0-9]", "");
        if (cpfDigits.length() != 11) {
            throw new ValidationException("O CPF deve conter 11 dígitos numéricos.");
        }
    }

    public static void validatePhone(String phone) throws ValidationException {
        String phoneDigits = phone.replaceAll("[^0-9]", "");
        if (phoneDigits.length() < 10) {
            throw new ValidationException("O número de telefone deve ter pelo menos 10 dígitos (com DDD).");
        }
    }

    public static void validateTipoPessoa(String tipoPessoa) throws ValidationException {
        if (!tipoPessoa.equalsIgnoreCase("ADMIN") && !tipoPessoa.equalsIgnoreCase("USUARIO")) {
            throw new ValidationException("Tipo de pessoa inválido. Deve ser ADMIN ou USUARIO.");
        }
    }
}