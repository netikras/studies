package com.planner.netikras;

import javax.swing.*;
//import PlannerPackage.Listeners;
//import PlannerPackage.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class WindowMainClass {
	
	protected SharedClass shared;
	
	private MainWindow mainWindow;
	
	public WindowMainClass() {
		mainWindow = new MainWindow();
	}
	
	// @Overload
	public WindowMainClass(int x, int y) {
		mainWindow = new MainWindow(x, y);
	}
	
	// @Overload
	public WindowMainClass(SharedClass data) {
		shared = data;
		mainWindow = new MainWindow(data);
	}
	
	
	public void show(boolean value){
		mainWindow.show(value);
	}
	
}


class MainWindow {
	
	protected SharedClass shared;
	
	private JFrame Frame;
	
	private JMenuBar MenuBar;
	protected Listeners aListener;
	

	protected JSplitPane 		Panel_split;	
	protected JPanel			Panel_main;
	
	protected PanelTopClass 	Panel_top;
	protected PanelBottomClass 	Panel_bottom;
	protected PanelLeftClass 	Panel_left;
	protected PanelCenterClass 	Panel_center;
	
	private Dimension FrameSize;
	private JButton Button_Connect;
	
	
	public MainWindow(){
		// assembling main window
		
		aListener = new Listeners();
		FrameSize = new Dimension(400, 250);
		Frame = new frame();
	}
	
	// @Overload
	public MainWindow(int x, int y){
		// assembling main window
		
		aListener = new Listeners();
		FrameSize = new Dimension(x, y);
		Frame = new frame();
	}
	
	// @Overload
	public MainWindow(SharedClass data){
		// assembling main window
		shared = data;
		
		aListener = shared.aListener;
		FrameSize = new Dimension(shared.MainFrameDimensions);
		
		Frame = new frame();
	}

	
	
	private class frame extends JFrame{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5291927517185857705L;
		private static final int GAP = 5;
		
		public frame(){
			setSize(FrameSize);
			setMinimumSize(FrameSize);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			setJMenuBar(createMenuBar());
			setTitle("Finansų Planuotojas");
			add(createPanels());
			
		}
		
		
		
		protected Container createPanels(){
			
			Panel_main 		= new JPanel();
			
			Panel_top		= new PanelTopClass		(shared);
			Panel_bottom	= new PanelBottomClass	(shared);
			Panel_left		= new PanelLeftClass	(shared);
			Panel_center	= new PanelCenterClass	(shared);
			
			Panel_split 	= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, Panel_left, Panel_center);
			
			shared.Panel_center = Panel_center;
			
			Panel_main.setLayout(new GridBagLayout());
			Panel_main.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
			
			Panel_split.setBorder(BorderFactory.createLoweredBevelBorder());
			
			// Positioning panels across the panel_main
			GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill 		= GridBagConstraints.HORIZONTAL; // make it vertically-static
			gbc.anchor 		= GridBagConstraints.FIRST_LINE_START;
			gbc.gridx 		= 0; // store it in left-
			gbc.gridy 		= 0; // -top corner
			gbc.gridwidth  	= 6;
			gbc.gridheight 	= 1;
			gbc.weightx 	= 1;
			gbc.weighty 	= 0;
			Panel_main.add(Panel_top, gbc);
			
			gbc.fill 		= GridBagConstraints.BOTH;
			gbc.anchor 		= GridBagConstraints.LINE_START;
			gbc.gridx 		= 0;
			gbc.gridy 		= 1;
			gbc.gridwidth  	= 6;
			gbc.gridheight 	= 4;
			gbc.weightx 	= 1;
			gbc.weighty 	= 1;
			Panel_main.add(Panel_split, gbc);
			
			gbc.fill 		= GridBagConstraints.HORIZONTAL; // make it vertically-static
			gbc.anchor 		= GridBagConstraints.FIRST_LINE_START;
			gbc.gridx 		= 0; // store it in left-
			gbc.gridy 		= 7; // -bottom corner
			gbc.gridwidth  	= 6;
			gbc.gridheight 	= 1;
			gbc.weightx 	= 1;
			gbc.weighty 	= 0;
			Panel_main.add(Panel_bottom, gbc);
			
			
			return Panel_main;
		}
		
		
		
		private JMenuBar createMenuBar(){
			MenuBar = new JMenuBar();
			
			//JMenu[] Menus = new JMenu[5];
			JMenu[] Menus = new JMenu[3];
			
			for (int i=1; i<Menus.length; i++){
					Menus[i]=null;	
			}
			
			Menus[1] = new JMenu("Veiksmai"	);
			//Menus[2] = new JMenu("Rodinys"	);
			//Menus[3] = new JMenu("Įrankiai"	);
			Menus[2] = new JMenu("Pagalba"	);
			
			Menus[1].setMnemonic(KeyEvent.VK_V);
			//Menus[2].setMnemonic(KeyEvent.VK_R);
			//Menus[3].setMnemonic(KeyEvent.VK_I);
			Menus[2].setMnemonic(KeyEvent.VK_P);
			
			JMenuItem MItem1_1 = new JMenuItem("Baigti");
			MItem1_1.setMnemonic(KeyEvent.VK_B);
			MItem1_1.addActionListener(aListener);
			MItem1_1.setActionCommand("exit");
			Menus[1].add(MItem1_1);
			
			
			JMenuItem MItem4_1 = new JMenuItem("Apie");
			MItem4_1.setMnemonic(KeyEvent.VK_A);
			MItem4_1.addActionListener(aListener);
			MItem4_1.setActionCommand("about");
			Menus[2].add(MItem4_1);
			
			
			for (int i=1; i<Menus.length; i++){
				MenuBar.add(Menus[i]);
			}
			
			
			return MenuBar;
		}
		
	}
	
	
	
	
	
	
	
	public void setSize(int x, int y){
		FrameSize.setSize(x, y);
		Frame.setSize(FrameSize);
	}
	
	public void setSize(Dimension NewSize){
		FrameSize = NewSize;
		Frame.setSize(FrameSize);
	}
	
	public void show(boolean value){
		
	
		Frame.setVisible(value);

	}
	
}