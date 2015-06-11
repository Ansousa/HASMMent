package es.uvigo.esei.hasmment.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import es.uvigo.esei.hasmment.dao.HibernateFactory;

//http://paletton.com/#uid=13M0u0kmnmS5ru7dqr2tAiTL3ew
public class MainFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	public MainFrame() {
		initGUI();	
	}
	private void initGUI(){
		//Inicializando los parametros
		setTitle("HASMment");
		setDefaultCloseOperation(closeWindow());
		
		//Añadiendo las partes
		MainContent mC = new MainContent(this);
		
		setJMenuBar(new MenuBar(this,mC));
		add(mC);
		
		//Colocando en mitad de la pantalla
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Enpaquetando para que se ajuste al tamaño de los elementos en el interior
		setVisible(true);		
		pack();
	}
	
	//Cuando se manda cerrar la ventana principal, se ordena cerrar la fabrica de sesiones y se cierra la ventana
	private int closeWindow(){
		HibernateFactory.closeFactory();
		return JFrame.EXIT_ON_CLOSE;
	}
}
