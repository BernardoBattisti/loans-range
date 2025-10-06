using System;

public class userData
{
	public string name;
	public string email;
	public string password;
	public string phone;
	public string cpf;
	public string address;

    public userData(string name, string email, string password, string phone, string cpf, string address)
	{
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.cpf = cpf;
		this.address = address;
    }
}
