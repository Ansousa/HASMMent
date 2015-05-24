package es.uvigo.esei.hasmment.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class CreatePermisoDialog extends JDialog implements ActionListener{
	MainContent mc;
	
	public CreatePermisoDialog(MainFrame owner, MainContent mc) {
		super(owner);
		this.mc = mc;
		initPermisoDialog();
	}
	
	private void initPermisoDialog() {
		setTitle("Crear Permiso");
		setLocationRelativeTo(this.getOwner());
		
		add(createForm());
		
		setVisible(true);
		pack();
	}
	
	private JPanel createForm() {
		JPanel form = new JPanel();
		
		return form;
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}

}
