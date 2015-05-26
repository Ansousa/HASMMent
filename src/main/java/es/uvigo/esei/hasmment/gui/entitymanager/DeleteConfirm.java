package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;

/* JDialog que nos pedirá la confirmación para eliminar una entidad en la base de datos*/
@SuppressWarnings("serial")
public class DeleteConfirm extends JDialog implements ActionListener{
	DBEntity entity;
	JButton buttonOK,buttonCancel;
	String identity;
	
	public DeleteConfirm(JDialog owner, DBEntity entity, String identity) {
		super(owner);
		this.entity = entity;
		this.identity = identity;
		initDialog();
		setLocationRelativeTo(owner);
		pack();
		setVisible(true);
	}
	
	private void initDialog() {
		String message = "¿Está seguro que desea eliminar?";
		
		buttonOK = new JButton("Ok");
		buttonOK.addActionListener(this);
		
		buttonCancel = new JButton("Cancelar");
		buttonCancel.addActionListener(this);
		
		setLayout(new GridLayout(2,1));
		
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new GridLayout(2,1));
		//Mensaje informativo
		messagePanel.add(new JLabel(message));
		//Mensaje detallando la fila
		messagePanel.add(new JLabel(identity));
		
		//Boton de confirmacion y cancelar
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(buttonOK);
		buttonPanel.add(buttonCancel);
		
		add(messagePanel);
		add(buttonPanel);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonOK){
			HibernateMethods.deleteEntity(this.entity);
			this.dispose();
		}
		if(e.getSource() == buttonCancel){
			this.dispose();
		}
	}
}
