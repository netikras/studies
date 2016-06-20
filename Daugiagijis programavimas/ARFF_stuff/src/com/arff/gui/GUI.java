package com.arff.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.arff.main.ARFF_FileAnalyzer;
import com.arff.main.DataAnalyzer;

public class GUI extends JFrame{

	
	private static final long serialVersionUID = 1639924765583434853L;
	
	private final String title;
	
	private LinkedList<String> attributesToSearch;
	
	private JPanel pInput;
	private JPanel pAnalysis;
	private JPanel pResults;
	
	private JScrollPane pStatus;
	
	private JTextArea txtStatus;
	
	private JButton bBrowseInput;
	private JButton bSelect;
	private JButton bFind;
	private JButton bShow;
	private JButton bSave;
	private JButton bGenerateDB;
	
	private JTextField tfInput;
	
	private JFileChooser browseDB;
	private JFileChooser browseDir;
	private JFileChooser browseSave;

	
	private Border bord_input   ;
	private Border bord_result  ;
	private Border bord_status  ;
	private Border bord_analysis;
	
	
	private GroupLayout layMain;
	private GroupLayout layInput;
	private GroupLayout layResult;
	private GroupLayout layAnalysis;
	
	private JRadioButton radDB;
	private JRadioButton radDir;
	
	private ButtonGroup radios;
	
	
	
	
	private DataAnalyzer dataAnalyzer = null;
	
	
	public GUI(String title) {
		System.out.println("Making a main GUI");
		this.title = title;
		init();
		setLocationRelativeTo(null);
		setResizable(false);
		radDir.doClick();
		
		setVisible(true);
		
		dataAnalyzer = new DataAnalyzer();
		
		dataAnalyzer.setThreadPoolSize(10);
		dataAnalyzer.setOperationTimeout(20);
	}
	
	private void init(){
		
		setTitle(title);
		
		
		browseDB = new JFileChooser(System.getProperty("user.home"));
		browseDir = new JFileChooser(System.getProperty("user.home"));
		browseSave = new JFileChooser(System.getProperty("user.home"));
		
		browseDB.setFileSelectionMode(JFileChooser.FILES_ONLY);
		browseDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		browseSave.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		browseDB.setDialogTitle("Select SQlite database file");
		browseDir.setDialogTitle("Select directory with ARFF files");
		browseSave.setDialogTitle("Select file to save results to");
		
		tfInput = new JTextField();
		
		tfInput.setBackground(getBackground());
		tfInput.setEditable(false);
		
		
		bBrowseInput = new JButton("Browse");
		bSelect = new JButton("Select");
		bFind = new JButton("Search");
		bShow = new JButton("Show");
		bSave = new JButton("Save");
		bGenerateDB = new JButton("Generate DB");
		
		bSelect.setEnabled(false);
		bFind.setEnabled(false);
		bShow.setEnabled(false);
		bSave.setEnabled(false);
		bGenerateDB.setEnabled(false);
		
		
		bBrowseInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogRetVal = 0;
				String selectedPath = tfInput.getText();
				File selectedFile = null;
				
				
				
				
				
				if (radDB.isSelected()){
					browseDB.setCurrentDirectory(new File(selectedPath));
					dialogRetVal = browseDB.showOpenDialog(null);
					if (dialogRetVal == JFileChooser.APPROVE_OPTION){
						selectedPath = browseDB.getSelectedFile().getAbsolutePath();
						tfInput.setText(selectedPath);
						
						bFind.setEnabled(false);
						bShow.setEnabled(false);
						
					}
					
					if (selectedPath != null && !selectedPath.isEmpty() && (selectedFile = new File(selectedPath)) != null){
						if (selectedFile.exists() && selectedFile.isFile()){
							setStatusMessage("I: DB file selected: "+selectedFile.getName());
							bGenerateDB.setEnabled(false);
							bSelect.setEnabled(true);
						} else {
							setStatusMessage("W: No DB file selected");
							bGenerateDB.setEnabled(false);
							bSelect.setEnabled(false);
						}
					}
					
				} else 
				if (radDir.isSelected()){
					browseDir.setCurrentDirectory(new File(selectedPath));
					dialogRetVal = browseDir.showOpenDialog(null);
					if (dialogRetVal == JFileChooser.APPROVE_OPTION){
						selectedPath = browseDir.getSelectedFile().getAbsolutePath();
						tfInput.setText(selectedPath);
						
						bFind.setEnabled(false);
						bShow.setEnabled(false);
					}
					
					bGenerateDB.setEnabled(false);
					if (selectedPath != null && !selectedPath.isEmpty() && (selectedFile = new File(selectedPath)) != null){
						if (selectedFile.exists() && selectedFile.isDirectory()){
							setStatusMessage("I: ARFF directory selected: "+selectedFile.getName());
							bSelect.setEnabled(true);
							bGenerateDB.setEnabled(true);
						} else {
							setStatusMessage("W: No ARFF directory selected");
							bSelect.setEnabled(false);
						}
					}
					
				}
				
			}
		});
		
		bFind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = "";
				int resultsCount = 0;
				
				bShow.setEnabled(false);
				bSave.setEnabled(false);
				
				if (attributesToSearch != null && attributesToSearch.size() > 0){
					
					path = tfInput.getText();
					
					if (path != null && !path.isEmpty()){
						setStatusMessage("I: Searching. Please wait");
						if (radDB.isSelected()){
							setStatusMessage("I: Searching in database");
							resultsCount = dataAnalyzer.findAttributesInDatabase(attributesToSearch.toArray(new String[attributesToSearch.size()]));
						} else
						if (radDir.isSelected()){
							setStatusMessage("I: Searching in files");
							resultsCount = dataAnalyzer.findAttributesInFiles(attributesToSearch.toArray(new String[attributesToSearch.size()]));
						}
					} else {
						setStatusMessage("W: Input path missing");
					}
				}
				
				if (resultsCount > 0){
					setStatusMessage("I: Found results for: "+resultsCount+" attributes");
					bShow.setEnabled(true);
					bSave.setEnabled(true);
				} else {
					setStatusMessage("W: No results found");
				}
				
			}
		});
		
		bSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				bShow.setEnabled(false);
				bSave.setEnabled(false);
				
				String[] attributesList = getAttributesList();
				if (attributesList != null && attributesList.length > 0){
					DialogFactory.createDialog_SelectAttributes(GUI.this, attributesList);
					
				} else {
					setStatusMessage("W: Attributes list is empty");
					bFind.setEnabled(false);
				}
			}
		});
		
		bShow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogFactory.createDialog_ShowResults(GUI.this);
			}
		});
		
		bSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, ArrayList<String>> result = dataAnalyzer.getResults();
				int dialogRetVal = 0;
				String selectedPath = "";
				File selectedFile = null;
				boolean filesSaved = false;
				
				if (result != null && result.size() > 0){
					dialogRetVal = browseSave.showOpenDialog(null);
					if (dialogRetVal == JFileChooser.APPROVE_OPTION){
						selectedPath = browseSave.getSelectedFile().getAbsolutePath();
						if (selectedPath != null && !selectedPath.isEmpty()){
							selectedFile = new File(selectedPath);
							if (selectedFile != null && !selectedFile.isDirectory()){
								try {
									
									if (!selectedFile.exists()){
										setStatusMessage("I: Creating a new output file");
										selectedFile.createNewFile();
									}
									
									setStatusMessage("I: Writing results to file: "+selectedFile.getName());
									filesSaved = performWriteResultsToFile(result, selectedFile);
									
									if (filesSaved){
										setStatusMessage("I: File has been saved");
									} else {
										setStatusMessage("W: File has not been saved properly");
									}
									
								} catch (IOException e1) {
									setStatusMessage("W: Cannot create a output file");
									e1.printStackTrace();
								}
								
							} else {
								setStatusMessage("W: Output file not suitable (none or dir)");
							}
						} else {
							setStatusMessage("W: No output file selected (empty)");
						}
					} else {
						setStatusMessage("W: Output file selection discarded");
					}
				} else {
					setStatusMessage("W: No found results to save");
				}
			}
		});
		
		bGenerateDB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogFactory.createDialog_GenerateDB(GUI.this);
			}
		});
		
		
		
		txtStatus = new JTextArea("I: STATUS: OK");
		txtStatus.setLineWrap(true);
		txtStatus.setWrapStyleWord(true);
		txtStatus.setBackground(getBackground());
		txtStatus.setEditable(false);
		
		
		
		bord_input = BorderFactory.createTitledBorder("Input");
		bord_result = BorderFactory.createTitledBorder("Result");
		bord_status = BorderFactory.createTitledBorder("Status");
		bord_analysis = BorderFactory.createTitledBorder("Analysis");
		
		
		radDB  = new JRadioButton("DB file");
		radDir = new JRadioButton("ARFF dir");
		
		radDB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (radDB.isSelected()){
					
					//bGenerateDB.setEnabled(false);
					
					String selectedPath = tfInput.getText();
					if (selectedPath != null && !selectedPath.isEmpty()){
						File selectedFile = new File(selectedPath);
						if ( ! selectedFile.exists() || selectedFile.isDirectory()){
							bBrowseInput.doClick();
						}
					}
				}
			}
		});
		
		radDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (radDir.isSelected()){
					
					//bGenerateDB.setEnabled(true);
					
					String selectedPath = tfInput.getText();
					if (selectedPath != null && !selectedPath.isEmpty()){
						File selectedFile = new File(selectedPath);
						if ( ! selectedFile.exists() || ! selectedFile.isDirectory()){
							bBrowseInput.doClick();
						}
					}
				}
			}
		});
		
		
		radios = new ButtonGroup();
		radios.add(radDB);
		radios.add(radDir);
		
		pAnalysis = new JPanel();
		pInput = new JPanel();
		pResults = new JPanel();
		
		pStatus = new JScrollPane(txtStatus);
		pStatus.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pStatus.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pStatus.setBorder(bord_status);
		pStatus.setAutoscrolls(false);
		
		
		pAnalysis.setBorder(bord_analysis);
		pInput.setBorder(bord_input);
		pResults.setBorder(bord_result);
		
		
		layInput = new GroupLayout(pInput);
		layAnalysis = new GroupLayout(pAnalysis);
		layResult = new GroupLayout(pResults);
		layMain = new GroupLayout(getContentPane());
		
		
		layInput.setAutoCreateGaps(true);
		layInput.setAutoCreateContainerGaps(true);
		
		layInput.setHorizontalGroup(
				layInput.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(tfInput)
					.addGroup(layInput.createSequentialGroup()
						.addGroup(layInput.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(radDB, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(radDir)
						)
						.addComponent(bBrowseInput)
					)
					
			);
		
		layInput.setVerticalGroup(
				layInput.createSequentialGroup()
					.addComponent(tfInput)
					
					.addGroup(layInput.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addGroup(layInput.createSequentialGroup()
								.addComponent(radDB)
								.addComponent(radDir)
							)
							.addComponent(bBrowseInput, GroupLayout.Alignment.CENTER)
					)
			);
		
		
		layAnalysis.setAutoCreateGaps(true);
		layAnalysis.setAutoCreateContainerGaps(true);
		
		layAnalysis.setHorizontalGroup(
				layAnalysis.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(bSelect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(bFind, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);
		
		layAnalysis.setVerticalGroup(
				layAnalysis.createSequentialGroup()
					.addComponent(bSelect)
					.addComponent(bFind)
			);
		
		
		layResult.setAutoCreateGaps(true);
		layResult.setAutoCreateContainerGaps(true);
		
		layResult.setHorizontalGroup(
				layResult.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(bShow, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(bSave, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(bGenerateDB, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);
		
		layResult.setVerticalGroup(
				layResult.createSequentialGroup()
					.addComponent(bShow)
					.addComponent(bSave)
					.addComponent(bGenerateDB)
			);
		
		
		layMain.setAutoCreateGaps(true);
		layMain.setAutoCreateContainerGaps(true);
		
		layMain.setHorizontalGroup(
				layMain.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(pInput)
					.addGroup(layMain.createSequentialGroup()
						.addComponent(pAnalysis, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(pResults, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					)
					.addComponent(pStatus)
			);
		
		layMain.setVerticalGroup(
				layMain.createSequentialGroup()
					.addComponent(pInput, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGroup(layMain.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(pAnalysis, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(pResults, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					)
					.addComponent(pStatus)
			);
		
		
		pInput.setLayout(layInput);
		pAnalysis.setLayout(layAnalysis);
		pResults.setLayout(layResult);
		getContentPane().setLayout(layMain);
		
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pack();
	}
	
	
	
	
	
	public void setAttributesToSearch(LinkedList<String> list){
		attributesToSearch = list;
		
		if (attributesToSearch != null && attributesToSearch.size() > 0){
			setStatusMessage("I: Selected attributes: "+attributesToSearch.size());
			bFind.setEnabled(true);
		} else {
			setStatusMessage("W: No attributes have been selected to find");
			bFind.setEnabled(false);
		}
		
	}
	
	
	private String[] getAttributesList(){
		String[] retVal = null;
		
		String sourcePath = "";
		File source = null;
		
		if (radDB.isSelected()){
			sourcePath = tfInput.getText();
			if (sourcePath != null && !sourcePath.isEmpty()){
				source = new File(sourcePath);
				if (source.exists() && source.isFile()){
					dataAnalyzer.setDatabasePath(sourcePath);
					retVal = dataAnalyzer.getAttributesListFromDatabase();
				}
			}
		} else 
		if (radDir.isSelected()){
			sourcePath = tfInput.getText();
			if (sourcePath != null && !sourcePath.isEmpty()){
				source = new File(sourcePath);
				if (source.exists() && source.isDirectory()){
					dataAnalyzer.setArffDirectory(sourcePath);
					retVal = dataAnalyzer.getAttributesListFromFiles();
				}
			}
		}
		
		
		return retVal;
	}
	
	
	
	public void performAttributesSearch(){
		
		if (attributesToSearch != null){
			//sdf
		}
		
	}
	
	
	public boolean performDumpToDB(File dbFile, boolean cacheInMemory){
		boolean retVal = false;
		File arffDir = null;
		
		if (dbFile != null && !dbFile.isDirectory()){
			if (radDir.isSelected() && ! tfInput.getText().isEmpty()){
				arffDir = new File(tfInput.getText());
				if (arffDir.exists() && arffDir.isDirectory()){
					dataAnalyzer.setArffDirectory(tfInput.getText());
					dataAnalyzer.setDatabasePath(dbFile.getAbsolutePath());
					dataAnalyzer.saveAttributesToDatabase(cacheInMemory, dbFile.getAbsolutePath());
					retVal = true;
				}
			}
		}
		
		return retVal;
	}
	
	
	public boolean performWriteResultsToFile(Map<String, ArrayList<String>> res, File destFile){
		boolean retVal = false;
		
		String[] keys = null;
		FileWriter fWriter = null;
		int linesCount = 0;
		Map<String, String> types = dataAnalyzer.getCachedTypes();
		
		if (res != null && res.size() > 0){
			keys = res.keySet().toArray(new String[res.size()]);
			try{
				fWriter = new FileWriter(destFile);
				for (int i=0; i<keys.length; i++){
					fWriter.write(
							 ARFF_FileAnalyzer.attribLinePrefix+ARFF_FileAnalyzer.attribLineDelim
							+keys[i]+ARFF_FileAnalyzer.attribLineDelim
							+types.get(keys[i])+"\n"
						);
					fWriter.flush();
				}
				fWriter.write(ARFF_FileAnalyzer.dataPrefix+"\n");
				fWriter.flush();
				
				linesCount = res.get(keys[0]).size();
				
				for (int i=0; i<linesCount; i++){
					
					for (int attrib=0; attrib<keys.length; attrib++){
						try{
							fWriter.write(res.get(keys[attrib]).get(i));
							if (attrib < keys.length-1){
								fWriter.write(ARFF_FileAnalyzer.dataDelim);
							}
							fWriter.flush();
						}catch(Exception e0){
							
						}
					}
					fWriter.write("\n");
				}
				retVal = true;
			} catch(Exception e){
				retVal = false;
			} finally {
				try{fWriter.close();}catch(Exception e){}
			}
		}
		
		return retVal;
	}
	
	
	public void setStatusMessage(String msg){
		System.out.println(msg);
		txtStatus.setText(msg);
		pStatus.revalidate();
		pStatus.repaint();
	}
	
}
