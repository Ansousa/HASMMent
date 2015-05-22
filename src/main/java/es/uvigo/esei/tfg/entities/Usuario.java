package es.uvigo.esei.tfg.entities;


public class Usuario extends Persona implements DBEntity{
	private String direccion;
	private int horas;
	private String modalidad;
	
	public Usuario(){};
	
	public Usuario(String dni, String nombre, String apellido1, String apellido2, String direccion, int horas, String modalidad)
	{
		super(dni,nombre,apellido1,apellido2);
		this.direccion = direccion;
		this.horas = horas;
		this.modalidad = modalidad;
	}
	
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}
	
	public String getDireccion()
	{
		return this.direccion;
	}
	
	public void setHoras(int horas)
	{
		this.horas = horas;
	}
	
	public int getHoras()
	{
		return this.horas;
	}
	
	public void setModalidad(String modalidad)
	{
		this.modalidad = modalidad;
	}
	
	public String getModalidad()
	{
		return this.modalidad;
	}
}
