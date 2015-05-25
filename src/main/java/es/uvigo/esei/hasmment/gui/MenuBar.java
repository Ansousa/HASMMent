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
	private JMenuItem consultUserItem,consultAuxItem,consultPerItem,consultAsistItem;
	
	public MenuBar(MainFrame owner, MainContent mc){
		this.owner = owner;
		this.mc = mc;
		initMenuBar();
	}
	
	private void initMenuBar(){
		/*Menu Usuarios*/
		userMenu = new JMenu("Gestion Usuarios");
		add(userMenu);
		
		consultUserItem = new JMenuItem("Consultar Usuarios");
		consultUserItem.addActionListener(this);
		userMenu.add(consultUserItem);
		
		/*Menu Auxiliares*/
		auxMenu = new JMenu("Gestion Auxiliares");
		add(auxMenu);
		
		consultAuxItem = new JMenuItem("Consultar Auxiliares");
		consultAuxItem.addActionListener(this);
		auxMenu.add(consultAuxItem);
		
		/*Menu Permisos*/
		perMenu = new JMenu("Gestion Permisos");
		add(perMenu);
		
		consultPerItem = new JMenuItem("Consultar Permisos");
		consultPerItem.addActionListener(this);
		perMenu.add(consultPerItem);
		
		/*Menu Asiste*/
		asistMenu = new JMenu("Gestion Asistencias");
		add(asistMenu);
		
		consultAsistItem = new JMenuItem("Consultar Asistencias");
		consultAsistItem.addActionListener(this);
		asistMenu.add(consultAsistItem);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == consultUserItem) {
			new ConsultUsuarioDialog(this.owner, this.mc);
		}
		else if(e.getSource() == consultPerItem){
			new ConsultPermisoDialog(this.owner, this.mc);
		}
		else if(e.getSource() == consultAuxItem){
			new ConsultAuxiliarDialog(this.owner, this.mc);
		}
		else if(e.getSource() == consultAsistItem){
			new ConsultAsisteDialog(this.owner, this.mc);
		}
	}
}
