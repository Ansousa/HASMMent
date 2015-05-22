package es.uvigo.esei.tfg.gui;

public class DNIWrongFormatException extends Exception{
	private String dni;
	
	public DNIWrongFormatException(String dni){
		this.dni = dni;
	}
	
	public String getDni(){
		return dni;
	}
	
	public String getCorrectDnI(){
		return "Debe ser un DNI válido, p.e: 12345678Z";
	}
}
