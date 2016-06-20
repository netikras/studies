package com.arff.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;


public class DialogGenerateDB extends JFrame{


	private static final long serialVersionUID = 1975505038388332388L;
	
	private final String title;
	
	private GUI gui;
	
	
	private final String message = ""
			+ "All ARFF files will be read and stored to a SQLite database. "
			+ "There are two modes to do that:\n"
			+ "    * using in-memory database cache\n"
			+ "    * pushing data directly to the database\n"
			+ "The former is MUCH faster but it wil literally save whole the "
			+ "database to RAM before saving it to the filesystem. The latter "
			+ "will save DB directly to the filesystem but it will take at least "
			+ "10 times longer to finish.\n"
			+ "\n"
			+ "Side note: Reading ARFF data from the database is MUCH faster than "
			+ "from separate files."
			+ "";
			
	private JTextArea txtArea;
	private JTextField tfDB;
	private JButton bBrowse;
	private JButton bProceed;
	private Border borderInfo;
	private Border borderSettings;
	
	private JCheckBox chkInMemory;
	private JLabel lOutpFile;
	private JScrollPane pInfo;
	private JPanel pSettings;
	private JFileChooser browseOutput;
	
	private GroupLayout layMain;
	private GroupLayout laySettings;
	
	
	
	public DialogGenerateDB(String title) {
		this.title = title;
	}
	
	
	protected void init(){
		
		setTitle(title);
		
		borderInfo = BorderFactory.createTitledBorder("Info");
		borderSettings = BorderFactory.createTitledBorder("Settings");
		
		
		txtArea = new JTextArea(message);
		txtArea.setBackground(getBackground());
		txtArea.setEditable(false);
		txtArea.setLineWrap(true);
		txtArea.setWrapStyleWord(true);
		
		tfDB = new JTextField();
		
		bBrowse = new JButton("Browse");
		
		bProceed = new JButton("Proceed");
		
		bBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogRetVal = 0;
				String selectedPath = tfDB.getText();
				
				browseOutput.setCurrentDirectory(new File(selectedPath));
				
				dialogRetVal = browseOutput.showOpenDialog(null);
				if (dialogRetVal == JFileChooser.APPROVE_OPTION){
					tfDB.setText(browseOutput.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		
		bProceed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gui != null){
					DialogGenerateDB dialogGenerateDB = DialogGenerateDB.this;
					if (dialogGenerateDB.gui.performDumpToDB(new File(tfDB.getText()), chkInMemory.isSelected())){
						dialogGenerateDB.dispatchEvent(new WindowEvent(dialogGenerateDB, WindowEvent.WINDOW_CLOSING));
					}
				}
			}
		});
		
		chkInMemory = new JCheckBox("In-memory");
		chkInMemory.setSelected(true);
		
		lOutpFile = new JLabel("Output file");
		
		pInfo = new JScrollPane(txtArea);
		pInfo.setBorder(borderInfo);
		pInfo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		pSettings = new JPanel();
		pSettings.setBorder(borderSettings);
		
		browseOutput = new JFileChooser(System.getProperty("user.home"));
		browseOutput.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		
		laySettings = new GroupLayout(pSettings);
		laySettings.setAutoCreateGaps(true);
		laySettings.setAutoCreateContainerGaps(true);
		
		layMain = new GroupLayout(getContentPane());
		layMain.setAutoCreateGaps(true);
		layMain.setAutoCreateContainerGaps(true);
		
		
		laySettings.setHorizontalGroup(
				laySettings.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(laySettings.createSequentialGroup()
						.addComponent(lOutpFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(bBrowse)
					)
					.addGroup(laySettings.createSequentialGroup()
						.addComponent(tfDB)
					)
					.addGroup(laySettings.createSequentialGroup()
						.addComponent(chkInMemory)
						.addComponent(bProceed)
					)
				
				
			);
		
		laySettings.setVerticalGroup(
				laySettings.createSequentialGroup()
					.addGroup(laySettings.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lOutpFile)
						.addComponent(bBrowse)
					)
					.addGroup(laySettings.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(tfDB)
					)
					.addGroup(laySettings.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(chkInMemory)
						.addComponent(bProceed)
					)
			);
		
		
		layMain.setHorizontalGroup(
				layMain.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(pInfo)
					.addComponent(pSettings)
			);
		
		layMain.setVerticalGroup(
				layMain.createSequentialGroup()
					.addComponent(pInfo)
					.addComponent(pSettings)
			);
		
		
		pSettings.setLayout(laySettings);
		getContentPane().setLayout(layMain);
		
		
		
		
		pack();
	}
	
	protected void setGui (GUI ui) {
		gui = ui;
	}
	
	
}
