package es.uvigo.esei.hasmment.gui.print;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.MainFrame;

@SuppressWarnings("serial")
public class PrintUserDialog extends PrintDialog{
	public PrintUserDialog(MainFrame owner) {
		super(owner);
		
		setUsers();
		setTitle("Imprimir horario usuario");
		setLocationRelativeTo(this.owner);
		setVisible(true);
		pack();
	}
	
	private void setUsers() {
		ArrayList<DBEntity> users = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		for (DBEntity dbEntity : users) {
			Usuario user = (Usuario) dbEntity;
			persona.addItem(user.getDni() + ", " + user.getNombre());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exportButton) {
			
		}
	}
}
