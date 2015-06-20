package es.uvigo.esei.hasmment.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import es.uvigo.esei.hasmment.gui.entitymanager.ConsultAsisteDialog;
import es.uvigo.esei.hasmment.gui.entitymanager.ConsultAuxiliarDialog;
import es.uvigo.esei.hasmment.gui.entitymanager.ConsultPermisoDialog;
import es.uvigo.esei.hasmment.gui.entitymanager.ConsultUsuarioDialog;
import es.uvigo.esei.hasmment.gui.print.PrintAuxDialog;
import es.uvigo.esei.hasmment.gui.print.PrintUserDialog;

public class MenuBar extends JMenuBar implements ActionListener{
	private static final long serialVersionUID = 1L;
	private MainFrame owner;
	private MainContent mc;
	private Menu userMenu,auxMenu,perMenu,asistMenu,printMenu;
	private MenuItem consultUserItem,consultAuxItem,consultPerItem,consultAsistItem, printUserItem, printAuxItem;
	
	@SuppressWarnings("serial")
	private class Menu extends JMenu{
		public Menu(String text){
			super(text);
			setBackground(new Color(21, 43, 85));
			setForeground(Color.WHITE);
		}
	}
	
	@SuppressWarnings("serial")
	private class MenuItem extends JMenuItem{
		public MenuItem(String text){
			super(text);
			setBackground(new Color(21, 43, 85));
			setForeground(Color.WHITE);
		}
	}
	
	public MenuBar(MainFrame owner, MainContent mc){
		this.owner = owner;
		this.mc = mc;
		initMenuBar();
	}
	
	private void initMenuBar(){
		setBackground(new Color(6, 23, 57));
		/*Menu Usuarios*/
		userMenu = new Menu("Gestion Usuarios");
		add(userMenu);
		
		consultUserItem = new MenuItem("Consultar Usuarios");
		consultUserItem.addActionListener(this);
		userMenu.add(consultUserItem);
		
		/*Menu Auxiliares*/
		auxMenu = new Menu("Gestion Auxiliares");
		add(auxMenu);
		
		consultAuxItem = new MenuItem("Consultar Auxiliares");
		consultAuxItem.addActionListener(this);
		auxMenu.add(consultAuxItem);
		
		/*Menu Permisos*/
		perMenu = new Menu("Gestion Permisos");
		add(perMenu);
		
		consultPerItem = new MenuItem("Consultar Permisos");
		consultPerItem.addActionListener(this);
		perMenu.add(consultPerItem);
		
		/*Menu Asiste*/
		asistMenu = new Menu("Gestion Asistencias");
		add(asistMenu);
		
		consultAsistItem = new MenuItem("Consultar Asistencias");
		consultAsistItem.addActionListener(this);
		asistMenu.add(consultAsistItem);
		
		/*Menu Imprimir*/
		printMenu = new Menu("Generar horarios");
		add(printMenu);
		
		printUserItem = new MenuItem("Imprimir Horario Usuario");
		printAuxItem = new MenuItem("Imprimir Horario Auxiliar");
		printUserItem.addActionListener(this);
		printAuxItem.addActionListener(this);
		printMenu.add(printUserItem);
		printMenu.add(printAuxItem);
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
		else if(e.getSource() == printUserItem) {
			new PrintUserDialog(this.owner);
		}
		else if(e.getSource() == printAuxItem) {
			new PrintAuxDialog(this.owner);
		}
	}
}
