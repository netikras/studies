package com.arff.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class DialogFactory {
	
	
	private static DialogGenerateDB dialogGenerateDB = null;
	private static DialogSelectAttributes dialogSelectAttributes = null;
	private static DialogShowResults dialogShowResults = null;
	
	
	public static JFrame createDialog_GenerateDB(GUI parentWindow){
		DialogGenerateDB retVal = null;
		
		if (dialogGenerateDB != null){
			retVal = dialogGenerateDB;
		} else {
			
			retVal = new DialogGenerateDB("Generate DB");
			dialogGenerateDB = retVal;
			
			retVal.init();
			retVal.setLocationRelativeTo(parentWindow);
			retVal.setResizable(false);
			
			retVal.setGui(parentWindow);
			
			retVal.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e){
					dialogGenerateDB = null;
				}
			});
		}
		
		retVal.setVisible(true);
		
		return retVal;
	}
	
	public static JFrame createDialog_SelectAttributes(GUI parentWindow, String[] attributesList){
		DialogSelectAttributes retVal = null;
		
		if (dialogSelectAttributes != null){
			retVal = dialogSelectAttributes;
		} else {
			retVal = new DialogSelectAttributes("Select attributes");
			dialogSelectAttributes = retVal;
			
			retVal.setGui(parentWindow);
			
			//retVal.init(new String[]{"aa", "bb", "cc"});
			retVal.init(attributesList);
			retVal.setLocationRelativeTo(parentWindow);
			retVal.setResizable(false);
			
			
			retVal.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e){
					dialogSelectAttributes = null;
				}
			});
		}
		
		retVal.setVisible(true);
		
		return retVal;
	}
	
	public static JFrame createDialog_ShowResults(GUI parentWindow){
		DialogShowResults retVal = null;
		
		if (dialogShowResults != null){
			retVal = dialogShowResults;
		} else {
			retVal = new DialogShowResults("Results");
			dialogShowResults = retVal;
			
			retVal.init();
			retVal.setLocationRelativeTo(parentWindow);
			retVal.setResizable(false);
			
			
			retVal.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e){
					dialogShowResults = null;
				}
			});
		}
		retVal.setVisible(true);
		
		
		return retVal;
	}
	
	
}
