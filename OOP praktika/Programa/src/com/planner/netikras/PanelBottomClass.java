package com.planner.netikras;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelBottomClass extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4344194981682912295L;

	protected SharedClass shared;
	
	protected int GAP;
	
	protected Listeners aListener;
	private   JLabel lblStatus;
	
	public PanelBottomClass(SharedClass data) {
		// constructor
		
		shared = data;
		shared.Panel_bottom = this;
		
		lblStatus = new JLabel();
		
		GAP = shared.GAP;
		
		view_main();
	}
	
	
	protected PanelBottomClass view_main() {
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(200, 20));
		setMaximumSize(new Dimension(200, 30));
		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		add(lblStatus);
		//add(new JButton("BUTTON_bottom"));
		
		return this;
	}

	
	protected void setText(String text){
		lblStatus.setText(text);
	}
	
	protected String getText(String text){
		return lblStatus.getText();
	}
	
	protected void append(String text){
		String oldtext = lblStatus.getText();
		lblStatus.setText(oldtext + text);
	}
	
}
