package com.planner.netikras;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelTopClass extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7523756993713697856L;

	protected SharedClass shared;
	
	protected int GAP;
	protected Dimension ButtonSize;
	protected Listeners aListener;
	JButton[] Buttons;

	
	String[][] buttonData = {
			{"Prisij."		, "LoginButton"	},
			//{"Pagrind."		, "MainView"	},
			{"Piniginės"	, "WalletView"	},
			//{"Kategor."		, "CategView"	},
			{"Planuotojas"	, "PlannerView"	},
			//{"Skolin."		, "LoaningView"	},
			};
	
	public PanelTopClass(SharedClass data) {
		// constructor
		shared = data;
		shared.Panel_top = this;
		
		Buttons = new JButton[buttonData.length];
		
		view_main();
		
	}

	
	
	protected PanelTopClass view_main() {
		
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(200, 50));
		setBorder(BorderFactory.createEtchedBorder());
		
		ButtonSize = new Dimension(120, 35);
		
		for(int i=0; i<Buttons.length; i++){
			Buttons[i] = new JButton(buttonData[i][0]);
			Buttons[i].setActionCommand("ButtonClick");
			Buttons[i].setName(buttonData[i][1]);
			Buttons[i].setPreferredSize(ButtonSize);
			Buttons[i].addActionListener(shared.aListener);
			add(Buttons[i]);
		}
		disableButtons();
		return this;
	}
	
	protected void disableButtons(){
		for(int i=1; i<Buttons.length; i++){
			Buttons[i].setEnabled(false);
		}
	}
	
	protected void enableButtons(){
		for(int i=1; i<Buttons.length; i++){
			Buttons[i].setEnabled(true);
		}
	}
	
	protected void loggedOut(){
		disableButtons();
		Buttons[0].setText("Prisij.");
		Buttons[0].setActionCommand("ButtonClick");
		Buttons[0].setName("LoginButton");
	}
	
	protected void loggedIn(){
		enableButtons();
		Buttons[0].setText("Atsij.");
		Buttons[0].setActionCommand("ButtonClick");
		Buttons[0].setName("LogoutButton");
	}
	
}
