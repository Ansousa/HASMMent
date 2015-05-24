package es.uvigo.esei.hasmment.entities;

import java.sql.Date;

public class Auxiliar extends Persona implements DBEntity {
	private int horas;
	private Date fechaInicioContrato;
	private Date fechaFinContrato;
	
	public Auxiliar(){};
	
	public Auxiliar(String dni, String nombre, String apellido1, String apellido2, int horas, Date fInicio, Date fFin)
	{
		super(dni,nombre,apellido1,apellido2);
		this.horas = horas;
		this.fechaInicioContrato = fInicio;
		this.fechaFinContrato = fFin;
	}
	
	public void setHoras(int horas)
	{
		this.horas = horas;
	}
	
	public int getHoras()
	{
		return this.horas;
	}
	
	public void setFechaInicioContrato(Date fInicio)
	{
		this.fechaInicioContrato = fInicio;
	}
	
	public Date getFechaInicioContrato()
	{
		return this.fechaInicioContrato;
	}
	
	public void setFechaFinContrato(Date fFin)
	{
		this.fechaFinContrato = fFin;
	}
	
	public Date getFechaFinContrato()
	{
		return this.fechaFinContrato;
	}
}
