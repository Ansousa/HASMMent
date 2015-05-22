package es.uvigo.esei.tfg.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

public class CreateAsisteDialog extends JDialog implements ActionListener{
	MainContent mc;
	
	public CreateAsisteDialog(MainFrame owner, MainContent mc) {
		super(owner);
		this.mc = mc;
		initAsisteDialog();
	}
	
	private void initAsisteDialog() {
		
	}

	public void actionPerformed(ActionEvent e) {
		
	}
}
