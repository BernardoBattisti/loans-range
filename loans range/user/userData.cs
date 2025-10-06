using System;
using System.Security.Cryptography;
using System.Text;

internal class userData
{
    public string Name { get; }
    public string Email { get; }
    public string PasswordHash { get; }
    public string Phone { get; }
    public string Cpf { get; }
    public string Address { get; }

    public userData(string name, string email, string password, string phone, string cpf, string address)
    {
        Name = name;
        Email = email;
        PasswordHash = GeneratePasswordHash(password);
        Phone = phone;
        Cpf = cpf;
        Address = address;
    }

    // Construtor alternativo para quando já se tem o hash
    public userData(string name, string email, string passwordHash, string phone, string cpf, string address, bool isHash)
    {
        Name = name;
        Email = email;
        PasswordHash = passwordHash;
        Phone = phone;
        Cpf = cpf;
        Address = address;
    }

    public static string GeneratePasswordHash(string password)
    {
        using (var sha = SHA256.Create())
        {
            var hash = sha.ComputeHash(Encoding.UTF8.GetBytes(password));
            return Convert.ToBase64String(hash);
        }
    }
}
