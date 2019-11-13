package models;

public class User {

	private int id;
	private String name;
	private String email;
	private String password;
	
	public User(int id, String name, String email, String pass) {
		this.id = id;
		this.name = name;
		this.email = email;
		password = pass;
	}

	
	public int getID()
	{
		return id;
	}

	
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}	
	
	@Override
	public String toString() {
		return "User [id = " + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
	}
	

}
