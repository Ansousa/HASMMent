package es.uvigo.esei.hasmment.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CheckForms {
	public static Boolean checkDNI(String dni){
		Pattern dniPattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
		Matcher m = dniPattern.matcher(dni);		
		String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
		if(dni.length() != 9) {
			return false;
		}
		
		int nDni = Integer.parseInt(dni.substring(0, 8));
		
		if(!m.matches()) {
			return false;			
		}
		else if(letras.toCharArray()[nDni % 23] != dni.substring(8).toCharArray()[0]) {
			return false;
		}
		else {
			return true;
		}
	}
}
