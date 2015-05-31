package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class DBMessage extends JDialog implements ActionListener {
	JButton buttonOK;
	public DBMessage(JDialog owner, String msg) {
		super(owner);
		JLabel message = new JLabel("Error - " + msg);
		if(msg=="")
			message.setText("Añadido con exito");
		else
			message.setForeground(Color.red);
		message.setBorder(BorderFactory.createEmptyBorder(20,5,20,5));
		
		buttonOK = new JButton("Ok");
		buttonOK.addActionListener(this);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		add(message,c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		
		add(buttonOK,c);
		setLocationRelativeTo(owner);
		pack();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonOK){
			this.dispose();
		}
	}
}
