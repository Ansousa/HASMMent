package es.uvigo.esei.tfg.entities;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class Permiso implements Serializable, DBEntity{
	private String dni;
	private Date fechaInicioPermiso;
	private Date fechaFinPermiso;
	private String tipo;
	
	public Permiso() {};
	
	public Permiso(String dni, Date fInicio, Date fFin, String tipo)
	{
		this.dni = dni;
		this.fechaInicioPermiso = fInicio;
		this.fechaFinPermiso = fFin;
		this.tipo = tipo;
	}
	
	public void setDni(String dni)
	{
		this.dni = dni;
	}
	
	public String getDni()
	{
		return this.dni;
	}
	
	public void setFechaInicioPermiso(Date fInicio)
	{
		this.fechaInicioPermiso = fInicio;
	}
	
	public Date getFechaInicioPermiso()
	{
		return this.fechaInicioPermiso;
	}
	
	public void setFechaFinPermiso(Date fFin)
	{
		this.fechaFinPermiso = fFin;
	}
	
	public Date getFechaFinPermiso()
	{
		return this.fechaFinPermiso;
	}
	
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
	
	public String getTipo()
	{
		return this.tipo;
	}
}
