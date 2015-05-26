package es.uvigo.esei.hasmment.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.*;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.*;

@SuppressWarnings("serial")
public class MainContent extends JPanel{
	MainFrame owner;
	public MainContent(MainFrame owner) {
		this.owner = owner;
		initMainContent();
	}
	
	private void initMainContent() {
		/*Usuarios*/
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		List<DBEntity> users = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		add(new JLabel("USUARIOS"));
		Usuario u;
		for(int i=0;i<users.size();i++)
		{
			u = (Usuario)users.get(i);
			add(new JLabel(u.getDni() +  "," + u.getNombre()));
		}
		add(new JLabel("----------"));
		
		/*Auxiliares*/
		users = HibernateMethods.getListEntities(HibernateEntities.AUXILIAR);
		add(new JLabel("AUXILIARES"));
		Auxiliar a;
		for(int i=0;i<users.size();i++)
		{
			a = (Auxiliar)users.get(i);
			add(new JLabel(a.getDni() +  "," + a.getNombre()));
		}
		add(new JLabel("----------"));
		
		/*Permisos*/
		users = HibernateMethods.getListEntities(HibernateEntities.PERMISO);
		add(new JLabel("PERMISOS"));
		Permiso p;
		for(int i=0;i<users.size();i++)
		{
			p = (Permiso)users.get(i);
			add(new JLabel(p.getDni() +  "," + p.getTipo()));
		}
		add(new JLabel("----------"));
		
		/*Asistencias*/
		users = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		add(new JLabel("ASISTENCIAS"));
		Asiste as;
		for(int i=0;i<users.size();i++)
		{
			as = (Asiste)users.get(i);
			add(new JLabel("Usuario: " + as.getDniUsuario() +  ", Auxiliar: " + as.getDniAuxiliar()));
		}
	}
}
