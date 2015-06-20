package es.uvigo.esei.hasmment.gui.print;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.gui.MainFrame;

@SuppressWarnings("serial")
public class PrintAuxDialog extends PrintDialog{
	public PrintAuxDialog(MainFrame owner) {
		super(owner);
		setAuxs();
		setTitle("Imprimir horario auxiliar");
		setLocationRelativeTo(this.owner);
		setVisible(true);
		pack();
	}
	
	private void setAuxs() {
		ArrayList<DBEntity> auxs = HibernateMethods.getListEntities(HibernateEntities.AUXILIAR);
		for (DBEntity dbEntity : auxs) {
			Auxiliar aux = (Auxiliar) dbEntity;
			persona.addItem(aux.getDni() + ", " + aux.getNombre());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exportButton) {
			
		}
	}
}
