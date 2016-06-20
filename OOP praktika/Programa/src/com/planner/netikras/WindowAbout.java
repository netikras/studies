package com.planner.netikras;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.planner.netikras.Listeners;


public class WindowAbout extends JFrame {

	protected SharedClass shared;
	
	public WindowAbout(SharedClass data) {
		shared = data;
		
		setTitle("Apie...");
		setName("About...");
		//setResizable(false);
		setSize(new Dimension(320, 300));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		
		final String text = "Autorius:\tDarius Juodokas\n" +
							"Įstaiga:\tVilniaus Kolegija\n"+
							"Grupė:\tPIN13\n" +
							"Data:\t2015-04-10\n"+
							"\n\n\n"+
							"Finansų Planuotojas - nesudėtinga programa, " +
							"skirta sekti finansų srautus grupės narių " +
							"piniginėse, planuotis išlaidas ir įplaukas, " +
							"stebėti, kaip sekasi tų planų laikytis.";
		
		
		JTextArea info = new JTextArea();
		info.setEditable(false);
		info.setText(text);
		info.setVisible(true);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		
		info.setBackground(this.getBackground());
		
		JScrollPane panel = new JScrollPane(info, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));
		
		
		JButton button = new JButton("Uždaryti");
		button.setActionCommand("closeFrame");
		button.addActionListener(shared.aListener);
		
		
		gbc.fill 	= GridBagConstraints.BOTH;
		gbc.anchor 	= GridBagConstraints.FIRST_LINE_START;
		gbc.gridx 		= 0;
		gbc.gridy 		= 0;
		gbc.gridwidth 	= 3;
		gbc.gridheight 	= 4;
		gbc.weightx 	= 1;
		gbc.weighty 	= 1;
		
		add(panel, gbc);
		
		gbc.fill 	= GridBagConstraints.NONE;
		gbc.anchor 	= GridBagConstraints.LAST_LINE_END;
		gbc.gridx 		= 2;
		gbc.gridy 		= 5;
		gbc.gridwidth  	= 1;
		gbc.gridheight 	= 1;
		gbc.weightx 	= 0;
		gbc.weighty 	= 0;
		
		add(button, gbc);
		
		
		setVisible(true);
		shared.ActionObject = this;
	}

}
