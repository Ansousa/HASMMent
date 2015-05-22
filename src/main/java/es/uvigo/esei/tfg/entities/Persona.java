package es.uvigo.esei.tfg.entities;

public abstract class Persona implements DBEntity{
	private String dni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	
	public Persona(){};
	
	public Persona(String dni)
	{
		this.dni = dni;
		this.nombre = "";
		this.apellido1 = "";
		this.apellido2 = "";
	}
	
	public Persona(String dni, String nombre, String apellido1)
	{
		this.dni = dni;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = "";
	}
	
	public Persona(String dni, String nombre, String apellido1, String apellido2)
	{
		this.dni = dni;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
	}
	
	public void setDni(String dni)
	{
		this.dni = dni;
	}
	
	public String getDni()
	{
		return this.dni;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	public String getNombre()
	{
		return this.nombre;
	}
	
	public void setApellido1(String apellido1)
	{
		this.apellido1 = apellido1;
	}
	
	public String getApellido1()
	{
		return this.apellido1;
	}
	
	public void setApellido2(String apellido2)
	{
		this.apellido2 = apellido2;
	}
	
	public String getApellido2()
	{
		return this.apellido2;
	}
}
