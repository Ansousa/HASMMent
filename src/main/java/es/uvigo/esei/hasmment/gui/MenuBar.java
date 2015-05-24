package es.uvigo.esei.hasmment.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import es.uvigo.esei.hasmment.dao.HibernateEntities;

public class MenuBar extends JMenuBar implements ActionListener{
	private static final long serialVersionUID = 1L;
	private MainFrame owner;
	private MainContent mc;
	private JMenu userMenu,auxMenu,perMenu,asistMenu;
	private JMenuItem createUserItem,createAuxItem,createPerItem,createAsistItem;
	
	public MenuBar(MainFrame owner, MainContent mc){
		this.owner = owner;
		this.mc = mc;
		initMenuBar();
	}
	
	private void initMenuBar(){
		/*Menu Usuarios*/
		userMenu = new JMenu("Gestion Usuarios");
		add(userMenu);
		
		createUserItem = new JMenuItem("Crear Usuario");
		createUserItem.addActionListener(this);
		userMenu.add(createUserItem);
		
		/*Menu Auxiliares*/
		auxMenu = new JMenu("Gestion Auxiliares");
		add(auxMenu);
		
		createAuxItem = new JMenuItem("Crear Auxiliar");
		createAuxItem.addActionListener(this);
		auxMenu.add(createAuxItem);
		
		/*Menu Permisos*/
		perMenu = new JMenu("Gestion Permisos");
		add(perMenu);
		
		createPerItem = new JMenuItem("Crear Permiso");
		createPerItem.addActionListener(this);
		perMenu.add(createPerItem);
		
		/*Menu Asiste*/
		asistMenu = new JMenu("Gestion Asistencias");
		add(asistMenu);
		
		createAsistItem = new JMenuItem("Crear Asistencia");
		createAsistItem.addActionListener(this);
		asistMenu.add(createAsistItem);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createUserItem) {
			new CreatePersonaDialog(this.owner, HibernateEntities.USUARIO, this.mc);
		}
		else if(e.getSource() == createPerItem){
			new CreatePermisoDialog(this.owner, this.mc);
		}
		else if(e.getSource() == createAuxItem){
			new CreatePersonaDialog(this.owner, HibernateEntities.AUXILIAR, this.mc);
		}
		else if(e.getSource() == createAsistItem){
			new CreateAsisteDialog(this.owner, this.mc);
		}
	}
}
