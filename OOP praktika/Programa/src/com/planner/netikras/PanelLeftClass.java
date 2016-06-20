package com.planner.netikras;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelLeftClass extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1773624127337921592L;

	protected SharedClass shared;
	
	protected int GAP;
	protected Listeners aListener;
	protected JComboBox<String> dropdown;
	
	JButton ButtonMembSettings;
	JButton ButtonAccSettings;
	JButton ButtonSwitchToMember;
	
	GridBagConstraints gbc;
	
	public PanelLeftClass(SharedClass data) {
		// constructor
		shared = data;
		shared.Panel_left = this;
		
		gbc = new GridBagConstraints();
		dropdown = new JComboBox<String>();
		
		//view_main();
		loggedOut();
	}

	
	protected PanelLeftClass view_main() {
		
		removeAll();

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(100, 200));
		setBorder(BorderFactory.createTitledBorder(shared.currentAcc));
		
				
		dropdown.setActionCommand("DropDown");
		dropdown.setName("MemberList");
		//dropdown.addActionListener(shared.aListener);
		
		ButtonAccSettings = new JButton("Paskyros nust.");
		ButtonAccSettings.setActionCommand("ButtonClick");
		ButtonAccSettings.setName("AccountSettings");
		ButtonAccSettings.addActionListener(shared.aListener);

		
		
		ButtonMembSettings = new JButton("Nario nust.");
		ButtonMembSettings.setActionCommand("ButtonClick");
		ButtonMembSettings.setName("MemberSettings");
		ButtonMembSettings.addActionListener(shared.aListener);
		ButtonMembSettings.setEnabled(false);
		
		
		ButtonSwitchToMember = new JButton("Perjungti narį");
		ButtonSwitchToMember.setActionCommand("ButtonClick");
		ButtonSwitchToMember.setName("SwitchMember");
		ButtonSwitchToMember.addActionListener(shared.aListener);
		
		Component[]	elements = {
				ButtonAccSettings,
				ButtonMembSettings,
				shared.LabelEmpty,
				dropdown,
				ButtonSwitchToMember
		};
		
		for(int i=0; i<elements.length; i++){
			gbc.fill 	= GridBagConstraints.HORIZONTAL;
			gbc.anchor 	= GridBagConstraints.PAGE_START;
			gbc.gridx 		= 0;
			gbc.gridy 		= i;
			gbc.gridwidth  	= 6;
			gbc.gridheight 	= 1;
			gbc.weightx 	= 1;
			gbc.weighty 	= 0;
			add(elements[i], gbc);
		}
		
		gbc.fill 	= GridBagConstraints.HORIZONTAL;
		gbc.anchor 	= GridBagConstraints.PAGE_START;
		gbc.gridx 		= 0;
		gbc.gridy 		= elements.length;
		gbc.gridwidth  	= 6;
		gbc.gridheight 	= 1;
		gbc.weightx 	= 1;
		gbc.weighty 	= 1;
		add(new JLabel(" "), gbc);
		
		revalidate();
		repaint();
		return this;
	}	
	
	
	protected void loggedIn(){
		
		setBorder(BorderFactory.createTitledBorder(shared.currentAcc));
		view_main();
	}
	
	protected void loggedOut(){
		
		removeAll();

		setBorder(BorderFactory.createTitledBorder(shared.currentAcc));
		JPanel panNotLoggedIn = new JPanel();
		JButton btnNewUser = new JButton("Kurti naudotoją");
		
		btnNewUser.addActionListener(shared.aListener);
		btnNewUser.setActionCommand("ButtonClick");
		btnNewUser.setName("NewUser");
		
		panNotLoggedIn.add(btnNewUser);
		
		add(panNotLoggedIn);
		
		
		revalidate();
		repaint();
		
	}
	
}
