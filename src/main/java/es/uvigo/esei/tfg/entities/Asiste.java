package es.uvigo.esei.tfg.entities;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class Asiste implements Serializable, DBEntity{
	private String dniUsuario;
	private String dniAuxiliar;
	private Timestamp fechaHoraInicioAsistencia;
	private Timestamp fechaHoraFinAsistencia;
	private String actividad;
	
	public Asiste() {};
	
	public Asiste(String dniUsuario, String dniAuxiliar, Timestamp fHInicio, Timestamp fHFin, String actividad)
	{
		this.dniUsuario = dniUsuario;
		this.dniAuxiliar = dniAuxiliar;
		this.fechaHoraInicioAsistencia = fHInicio;
		this.fechaHoraFinAsistencia = fHFin;
		this.actividad = actividad;
	}
	
	public void setDniUsuario(String dni)
	{
		this.dniUsuario = dni;
	}
	
	public String getDniUsuario()
	{
		return this.dniUsuario;
	}
	
	public void setDniAuxiliar(String dni)
	{
		this.dniAuxiliar = dni;
	}
	
	public String getDniAuxiliar()
	{
		return this.dniAuxiliar;
	}
	
	public void setFechaHoraInicioAsistencia(Timestamp fHInicio)
	{
		this.fechaHoraInicioAsistencia = fHInicio;
	}
	
	public Timestamp getFechaHoraInicioAsistencia()
	{
		return this.fechaHoraInicioAsistencia;
	}
	
	public void setFechaHoraFinAsistencia(Timestamp fHFin)
	{
		this.fechaHoraFinAsistencia = fHFin;
	}
	
	public Timestamp getFechaHoraFinAsistencia()
	{
		return this.fechaHoraFinAsistencia;
	}
	
	public void setActividad(String actividad)
	{
		this.actividad = actividad;
	}
	
	public String getActividad()
	{
		return this.actividad;
	}
}
