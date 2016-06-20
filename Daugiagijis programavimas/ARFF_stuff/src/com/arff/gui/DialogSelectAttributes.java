package com.arff.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import sun.net.www.content.text.plain;



public class DialogSelectAttributes extends JFrame {

	private static final long serialVersionUID = -2343092603583857259L;
	
	private final String title;
	
	GUI gui;
	
	final String[] columns = new String[]{"Attribute", "Selected"};
	Object[][] values;
	
	private JScrollPane pList;
	private JPanel pMain;
	private JTable table;
	private DefaultTableModel tModel;
	private JButton bSubmit;
	private Border border;
	private JCheckBox chkAll;
	
	private GroupLayout layMain;
	
	
	public DialogSelectAttributes(String title) {
		this.title = title;
	}
	
	
	protected void init(String[] attributes){
		
		setTitle(title);
		
		
		
		
		values = new Object[attributes.length][2];
		
		for (int i=0; i<attributes.length; i++){
			values[i] = new Object[]{attributes[i], false};
		}
		
		tModel = new DefaultTableModel(values, columns);
		table = new JTable(tModel){
			
			private static final long serialVersionUID = 2166516569141783241L;
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
					case 0:
						return String.class;
					default:
						return Boolean.class;
				}
			}
			
			
		};
		
		
		
		setVisibleRowCount(table, 15);
		
		
		
		border = BorderFactory.createTitledBorder("Attributes");
		
		pMain = new JPanel();
		
		pList = new JScrollPane(table);
		pList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		pList.setBorder(border);
		
		
		chkAll = new JCheckBox("All");
		chkAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chkAll.isSelected()){
					for (int i=0; i<attributes.length; i++){
						values[i] = new Object[]{attributes[i], true};
					}
				} else {
					for (int i=0; i<attributes.length; i++){
						values[i] = new Object[]{attributes[i], false};
					}
				}
				//tModel.fireTableDataChanged();
				tModel = new DefaultTableModel(values, columns);
				table.setModel(tModel);
				table.validate();
				table.repaint();
				pList.validate();
				pList.repaint();
			}
		});
		
		bSubmit = new JButton("Submit");
		
		bSubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table != null){
					DialogSelectAttributes dialogSelectAttributes = DialogSelectAttributes.this;
					LinkedList<String> selectedAttributes = new LinkedList<String>();
					String value = "";
					
					for (int r=0; r<tModel.getRowCount(); r++){
						if (tModel.getValueAt(r, 1) == Boolean.TRUE){
							value = (String)tModel.getValueAt(r, 0);
							selectedAttributes.add(value);
							System.out.println("Selected: "+value);
						}
					}
					
					dialogSelectAttributes.gui.setAttributesToSearch(selectedAttributes);
					dialogSelectAttributes.dispatchEvent(new WindowEvent(dialogSelectAttributes, WindowEvent.WINDOW_CLOSING));
				}
				
			}
		});
		
		layMain = new GroupLayout(pMain);
		
		layMain.setAutoCreateContainerGaps(true);
		layMain.setAutoCreateGaps(true);
		
		layMain.setHorizontalGroup(
				layMain.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(pList)
					.addComponent(chkAll)
					.addComponent(bSubmit)
			);
		
		layMain.setVerticalGroup(
				layMain.createSequentialGroup()
					.addComponent(pList)
					.addComponent(chkAll)
					.addComponent(bSubmit)
			);
		
		pMain.setLayout(layMain);
		getContentPane().add(pMain);
		
		
		
		pack();
	}
	
	
	public static void setVisibleRowCount(JTable table, int rows){ 
		int height = 0; 
		for(int row=0; row<rows; row++) 
			height += table.getRowHeight(row); 
		
		table.setPreferredScrollableViewportSize(new Dimension( 
				table.getPreferredScrollableViewportSize().width, 
				height 
			)); 
	}
	
	
	public void setGui(GUI ui){
		gui = ui;
	}
	
	
}
