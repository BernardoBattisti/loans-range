using System;
using System.Linq;
using System.Security.Cryptography;
using System.Text;

namespace Loans_Range.login_sys
{
    internal class login_sys
    {
        public static bool login_verify(string email, string password)
        {
            // Busca usuário pelo email (ignorando maiúsculas/minúsculas)
            var user = main.users.FirstOrDefault(u => 
                u.Email.Equals(email, StringComparison.OrdinalIgnoreCase));

            if (user == null)
                return false;

            // Verifica o hash da senha
            return VerifyPassword(password, user.PasswordHash);
        }

        // Exemplo de verificação de hash de senha (ajuste conforme seu armazenamento)
        private static bool VerifyPassword(string password, string storedHash)
        {
            // Exemplo usando SHA256 (prefira bcrypt/argon2 em produção)
            using (var sha = SHA256.Create())
            {
                var hash = Convert.ToBase64String(sha.ComputeHash(Encoding.UTF8.GetBytes(password)));
                return hash == storedHash;
            }
        }
    }
}
