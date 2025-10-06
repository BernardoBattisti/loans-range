using System;
using System.Linq;
using PhoneNumbers;
using System.Net.Mail;

namespace Loans_Range.cadastro
{
    internal class verify
    {
        public static bool verifyName(string name)
        {
            if (string.IsNullOrWhiteSpace(name))
            {
                Console.WriteLine("O nome não pode ser vazio.");
                return false;
            }

            var nameParts = name.Trim().Split(' ', StringSplitOptions.RemoveEmptyEntries);
            if (nameParts.Length < 2)
            {
                Console.WriteLine("Deve informar o sobrenome.");
                return false;
            }

            if (name.Length < 3)
            {
                Console.WriteLine("O nome deve ter pelo menos 3 caracteres.");
                return false;
            }
            if (name.Length > 50)
            {
                Console.WriteLine("O nome deve ter no máximo 50 caracteres.");
                return false;
            }

            foreach (var part in nameParts)
            {
                if (!part.All(char.IsLetter))
                {
                    Console.WriteLine("O nome deve incluir somente letras.");
                    return false;
                }
            }

            return true;
        }

        public static bool verifyEmail(string email)
        {
            if (string.IsNullOrWhiteSpace(email))
            {
                Console.WriteLine("O email não pode ser vazio.");
                return false;
            }
            try
            {
                var mailAddress = new MailAddress(email);
                return true;
            }
            catch
            {
                Console.WriteLine($"O email '{email}' é inválido.");
                return false;
            }
        }

        public static bool verifyPhone(string phone)
        {
            var phoneUtil = PhoneNumberUtil.GetInstance();
            try
            {
                var numberProto = phoneUtil.Parse(phone, "BR");
                return phoneUtil.IsValidNumber(numberProto);
            }
            catch (NumberParseException)
            {
                Console.WriteLine("Número de telefone inválido.");
                return false;
            }
        }

        public static bool verifyCpf(string cpf)
        {
            cpf = new string(cpf.Where(char.IsDigit).ToArray());
            if (cpf.Length != 11)
            {
                Console.WriteLine("O CPF deve conter 11 dígitos.");
                return false;
            }
            if (cpf.All(c => c == cpf[0]))
            {
                Console.WriteLine("O CPF não pode ser composto por números repetidos.");
                return false;
            }

            int[] mult1 = { 10, 9, 8, 7, 6, 5, 4, 3, 2 };
            int[] mult2 = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

            var tempCpf = cpf.Substring(0, 9);
            int sum = 0;
            for (int i = 0; i < 9; i++)
                sum += (tempCpf[i] - '0') * mult1[i];

            int resto = sum % 11;
            int digito1 = resto < 2 ? 0 : 11 - resto;

            tempCpf += digito1;
            sum = 0;
            for (int i = 0; i < 10; i++)
                sum += (tempCpf[i] - '0') * mult2[i];

            resto = sum % 11;
            int digito2 = resto < 2 ? 0 : 11 - resto;

            bool valido = cpf.EndsWith($"{digito1}{digito2}");
            if (!valido)
                Console.WriteLine("CPF inválido.");
            return valido;
        }
    }
}