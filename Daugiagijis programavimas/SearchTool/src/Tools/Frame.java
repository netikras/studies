package Tools;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import MainPackage.Finder;

public class Frame extends JFrame {

	
	private static final long serialVersionUID = -8360736723671789436L;
	
	private static final String MESSAGE = ""
			+ Finder.TITLE+" - "+Finder.FULL_TITLE+"\n"
			+ "++++++++++++++++++++++++++++\n"
			+ "This application can find and sort files located in the \n"
			+ "filesystem. Results will be stored here, in this very window.\n"
			+ "File names are matched by a regular expression provided by a \n"
			+ "user.\n"
			+ "\n"
			+ "RegEx: '^.*'           means 'all files'\n"
			+ "RegEx: '^.*[Cc]hill.*' means 'files that have either 'Chill' or\n"
			+ "                         'chill' string in their names'\n"
			+ "\n"
			+ "Buffer Size sets results list refresh rate ^-1. I.e. the higher \n"
			+ "this value is set the less refresh operations will be triggered \n"
			+ "ergo the faster search will be completed. Values: [1;500].\n"
			+ "\n"
			+ "======================\n"
			+ "=> @author : netikras\n"
			+ "=> @date   : 2015-10-16\n"
			+ "=> @version: v1\n"
			+ "======================\n";
	
	
	Container mainPanel = null;
	GroupLayout layout = null;
	
	JLabel label_what = null;
	JLabel label_where = null;
	JLabel label_buffSZ = null;
	
	JTextField tf_what = null;
	JTextField tf_where = null;
	JTextField tf_status = null;
	
	JProgressBar bar_progress = null;
	
	JCheckBox ch_showFiles = null;
	//JCheckBox ch_showSizes = null;
	
	JTextArea resultsArea = null;
	JScrollPane scrollPanel = null;
	
	JButton btn_browse = null;
	JButton btn_search = null;
	JButton btn_stop = null;
	
	JFileChooser filesBrowser = null;
	
	JComboBox<String> ddn_sortBy = null;
	JRadioButton rad_order_asc = null;
	JRadioButton rad_order_desc = null;
	ButtonGroup rad_order = null;
	
	JSpinner spin_bufSize = null;
	
	
	File searchDir = null;
	
	Sorter sorter = null;
	SearchWorker[] worker = null;
	
	public Frame(String title) {
		super(title);
	}
	
	public void assembleFrame(){
		
		mainPanel = getContentPane();
		
		layout = new GroupLayout(mainPanel);
		
		
		label_what  = new JLabel("What to look for (regex)?");
		label_where = new JLabel("Where to look in (path)?");
		label_buffSZ = new JLabel("      Buffer size");
		
		tf_what 	= new JTextField("^.*", 30);
		tf_where 	= new JTextField(System.getProperty("user.home"), 30);
		tf_status 	= new JTextField(getTitle()+" has been initiated");
		tf_where.setBackground(getBackground());
		tf_status.setBackground(getBackground());
		tf_status.setEditable(false);
		tf_status.setToolTipText("Displays current status");
		
		bar_progress = new JProgressBar();
		bar_progress.setStringPainted(true);
		bar_progress.setEnabled(true);
		bar_progress.setMaximum(0);
		bar_progress.setString("");
		bar_progress.setToolTipText("Displays how many worker threads are still busy. When thread count hits 0 it's safe to say that search has finished.");
		
		ch_showFiles = new JCheckBox("Show files", true);
		ch_showFiles.setHorizontalTextPosition(SwingConstants.LEFT);
		ch_showFiles.setToolTipText("If checked - normal search will be performed. If unchecked - only directories will be shown with their total size");
		//ch_showSizes = new JCheckBox("show sizes", false);
		
		btn_browse = new JButton("Browse");
		btn_search = new JButton("Search...");
		btn_stop = new JButton("Stop");
		btn_stop.setEnabled(false);
		
		filesBrowser = new JFileChooser(System.getProperty("user.home"));
		filesBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		
		ddn_sortBy = new JComboBox<String>();
		ddn_sortBy.setEditable(false);
		ddn_sortBy.addItem("Sort by pathname");
		ddn_sortBy.addItem("Sort by filename");
		ddn_sortBy.addItem("Sort by filesize");
		ddn_sortBy.setSelectedItem(ddn_sortBy.getItemAt(0));
		
		rad_order_asc  = new JRadioButton("Ascending");
		rad_order_desc = new JRadioButton("Descending");
		rad_order_asc.setSelected(true);
		
		rad_order = new ButtonGroup();
		rad_order.add(rad_order_asc);
		rad_order.add(rad_order_desc);
		
		resultsArea = new JTextArea();
		resultsArea.setEditable(false);
		resultsArea.setText(MESSAGE);
		resultsArea.setBackground(getBackground());
		resultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, resultsArea.getFont().getSize()));
		
		scrollPanel = new JScrollPane(resultsArea);
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(tf_what.getColumns()*16, 300));
		JPanel panel = null;
		
		spin_bufSize = new JSpinner();
		spin_bufSize.setModel(new SpinnerNumberModel(50, 1, 500, 1));
		spin_bufSize.setToolTipText("Search results buffer size. Buffer will be refreshed every time this many matches are found.");
		spin_bufSize.setEnabled(true);
		
		
		btn_browse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn_browse.setEnabled(false);
				
				if (filesBrowser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					//searchDir = filesBrowser.getSelectedFile();
					tf_where.setText(filesBrowser.getSelectedFile().getAbsolutePath());
				}
				
				btn_browse.setEnabled(true);
			}
		});
		
		btn_search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tf_where.getText().isEmpty()){
					return;
				}
				
				searchDir = new File(tf_where.getText());
				
				if(searchDir != null && searchDir.exists() && searchDir.isDirectory()){
					File[] contents = searchDir.listFiles();
					
					if(contents != null && contents.length > 0){
						worker = new SearchWorker[contents.length];
						btn_stop.setEnabled(true);
						resultsArea.setText("");
						
						sorter = new Sorter(resultsArea, ddn_sortBy.getSelectedIndex(), rad_order_asc.isSelected()?Sorter.ASCENDING:Sorter.DESCENDING, (Integer)spin_bufSize.getValue());
						sorter.setSummaryMode(!ch_showFiles.isSelected());
						sorter.linkButons(btn_search, btn_stop);
						sorter.setStatusField(tf_status);
						sorter.setProgressBar(bar_progress);
						sorter.setClientsCount(contents.length);
						sorter.start();
						System.out.println("Contents length: "+contents.length);
						for(int i=0; i<contents.length; i++){
							worker[i] = new SearchWorker(sorter, tf_what.getText());
							worker[i].addJob(contents[i]);
							worker[i].start();
						}
					}
				}
				
			}
		});
		
		btn_stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn_stop.setEnabled(false);
				
				System.out.println("Stopping workers");
				if(worker != null && worker.length > 0){
					for(SearchWorker w : worker)
						if(w != null) w.stopWorker();
				}
				System.out.println("Stopping sorter");
				if(sorter != null){sorter.stopSorter();}
				
			}
		});
		
		


		
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)	
			//	layout.createSequentialGroup()
				.addGroup(layout.createSequentialGroup()	
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(label_what)
							.addComponent(tf_what, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(label_where)
							.addGroup(layout.createSequentialGroup()
									.addComponent(tf_where, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(btn_browse)
									)
							.addGroup(layout.createSequentialGroup()
									.addComponent(ch_showFiles)
									.addComponent(label_buffSZ, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(spin_bufSize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									//.addComponent(spin_bufSize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								)
							//.addGroup(layout.createSequentialGroup()
									//.addComponent(spin_bufSize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									//.addComponent(label_buffSZ, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							//	)
							.addGroup(layout.createSequentialGroup()
									.addComponent(ddn_sortBy, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(rad_order_asc)
									.addComponent(rad_order_desc)
								)
							.addGroup(layout.createSequentialGroup()
									.addComponent(btn_stop)
									.addComponent(btn_search)
								)
						)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(scrollPanel, 200, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE)
							)
				)
				.addComponent(tf_status)
				.addComponent(bar_progress)
			);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()	
				//layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addGroup(layout.createSequentialGroup()
							.addComponent(label_what)
							.addComponent(tf_what, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(label_where)
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(tf_where, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(btn_browse)
									)
							
							
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(ch_showFiles)
									.addComponent(label_buffSZ, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(spin_bufSize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									//.addComponent(spin_bufSize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								)
							//.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							//		.addComponent(spin_bufSize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							//		.addComponent(label_buffSZ, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							//	)
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(ddn_sortBy, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(rad_order_asc)
									.addComponent(rad_order_desc)
								)
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(btn_stop)
									.addComponent(btn_search)
								)
							
						)
					.addComponent(scrollPanel)
					)
				.addComponent(tf_status, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(bar_progress, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			);
		
		mainPanel.setLayout(layout);
		
		
		
		panel = (JPanel) mainPanel;
		panel.add(label_what);
		panel.add(tf_what);
		panel.add(label_where);
		panel.add(tf_where);
		//mainPanel.add(, GroupLayout.Alignment.CENTER);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pack();
	}
	
	
	public void setFrameSize(int width, int height){
		setFrameSize(new Dimension(width, height));
	}
	
	public void setFrameSize(Dimension d){
		//mainPanel.setPreferredSize(d);
		//setPreferredSize(d);
		setMinimumSize(d);
	}

	
	
	
}



class SearchWorker extends Thread {
	
	int STATE = 0;
	File baseDir = null;
	Sorter sorter = null;
	String pattern = "";
	
	
	public SearchWorker(Sorter sorter, String pattern) {
		this.sorter = sorter;
		this.pattern = pattern;
		STATE = 1;
		//System.out.println("Bringing up a worker");
	}
	
	@Override
	public void run(){
		setName("WORKER thread");
		try {sorter.ping(true);} 
		catch (Exception e) {}
		try{ work(baseDir); }
		catch(Exception e){System.out.println("Worker exception: "+e.getMessage());}
//		sorter.flushBuffer();
		try {sorter.ping(false);} 
		catch (Exception e) {}
	}
	
	public void setSearchPattern(String pattern){
		this.pattern = pattern;
	}
	
	public void addJob(File file){
		baseDir = file;
		
	}
	
	
	private void work(File dir){
		if(dir == null || ! dir.exists()){
			return;
		}
		
		if(STATE < 1){return;}
		
		if (dir.isFile()){
			if (dir.getName().matches(pattern)){
				sorter.addResult(dir);
				
			}
		} else 
		if (dir.isDirectory()){
			
			try{
				for(File f : dir.listFiles()){
					if(STATE < 1){return;}
					work(f);
				}
			} catch(Exception e){
				System.out.println("WORK() exception: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			//System.out.println("Unrecognized file type: ["+dir.getAbsolutePath()+"]");
		}
		
	}
	
	
	public void stopWorker(){
		STATE = 0;
		synchronized (this) {
			this.interrupt();
		}
		
	}
}
















class Sorter extends Thread{
	
	
	public static final int BY_PATHNME 		= 0;
	public static final int BY_FILENAME 	= 1;
	public static final int BY_SIZE 		= 2;
	
	public static final int ASCENDING 		=  1;
	public static final int DESCENDING 		= -1;
	
	
	HashMap<File, Long> dirSizes = new HashMap<File, Long>();
	boolean summaryMode = false;
	
	
	long startTime = 0;
	long endTime = 0;
	JTextArea resultsPanel = null;
	JTextField status = null;
	JProgressBar bar = null;
	JButton b_start = null;
	JButton b_stop = null;
	int buffSize = 10;
	int buffCount = 10;
	ArrayList<File> results = new ArrayList<File>();
	volatile int STATE = 0;
	int sort_by = BY_PATHNME;
	int order = ASCENDING;
	String SPACE = "\t";
	volatile int clients = 0;
	
	Comparator<File> comparator = null;
	volatile Semaphore semaphore_ping = new Semaphore();
	volatile Semaphore semaphore_core = new Semaphore();
	volatile Semaphore semaphore_peripherral = new Semaphore();
	
	public Sorter(JTextArea results, int sort_by, final int order, int bufferSize) {
		resultsPanel = results;
		buffSize = bufferSize;
		STATE = 1;
		
		if(order != ASCENDING && order != DESCENDING){
			throw new IllegalArgumentException("Illegal sorting order parameter: ["+sort_by+"]. Use one of the following: Sorter.DESCENDING,  Sorter.ASCENDING");
		}
		this.order = order;
		this.sort_by = sort_by;
		
		switch (sort_by) {
			case BY_PATHNME:
				comparator = new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {
						return o1.getAbsolutePath().compareTo(o2.getAbsolutePath()) * order;
					}
				};
			break;
			
			case BY_FILENAME:
				comparator = new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {
						return o1.getName().compareTo(o2.getName()) * order;
					}
				};
			break;
			
			case BY_SIZE:
				comparator = new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {
						return (o1.length() < o2.length()?-1:1) * order;
					}
				};
			break;
	
			default:
				throw new IllegalArgumentException("Illegal sorting parameter: ["+sort_by+"]. Choose one of Sorter.BY_<...>");
			
		}
		
	}
	
	@Override
	public void run(){
		setName("SORTER thread");
		updateStatus("Search is in progress..");
		
		if (b_start != null) {b_start.setEnabled(false);}
		if (b_stop != null) {b_stop.setEnabled(true);}
		
		startTime = System.currentTimeMillis();
		while(STATE > 0){
			try {
				
				semaphore_ping.release();
				semaphore_peripherral.release();
				semaphore_core.acquire();
				sort();
			} catch (InterruptedException e) {
				System.out.println("SORTER INTERRUPTED");
				updateStatus("Search has been interrupted.. Trying to continue");
			} catch (Exception e) {
				updateStatus("Search has ran into an unexpected error: ["+e.getMessage()+"].. Trying to continue");
				e.printStackTrace();
			}
			
		}
		finish();
		endTime = System.currentTimeMillis();
		updateStatus("Finishing up search results set..");
		if (b_start != null) {b_start.setEnabled(true);}
		if (b_stop != null) {b_stop.setEnabled(false);}
		updateStatus("Search has been "+(interrupted()?"interrupted":"completed")+" after "+(endTime - startTime)+"ms. Results found: "+(summaryMode?dirSizes.size():results.size()));
		if (bar != null) bar.setMaximum(0);
	}
	
	public void setSummaryMode(boolean enable){
		summaryMode = enable;
	}
	
	public void addResult(File file){
		
		try{
			semaphore_peripherral.acquire();
			
			//System.out.println("Adding ["+file+"] to BUFFER");
			if (summaryMode) {
				Long prevSize = dirSizes.get(file.getParentFile());
				dirSizes.put(file.getParentFile(), prevSize == null? file.length() : prevSize+file.length());
			} else {
				results.add(file);
			}
			
			buffCount--;
			
		} catch(Exception e){
			System.out.println("Exception:"+e.getMessage());
			return;
		}
			
		if(buffCount == 0){
				semaphore_core.release();
				buffCount = buffSize;
		} else {
			semaphore_peripherral.release();
		}
				
	}
	
	public void setStatusField(JTextField tf){
		this.status = tf;
	}
	
	public void setClientsCount(int count){
		clients = count;
		if(this.bar != null) bar.setMaximum(clients);
	}
	
	public void setProgressBar(JProgressBar bar){
		this.bar = bar;
		this.bar.setMaximum(this.clients);
	}
	
	public void ping(boolean saying_hello) throws InterruptedException{
		semaphore_ping.acquire();
		synchronized (this) {
			
		
			if(saying_hello){
				//clients++;
			} else {
				clients--;
				if(clients == 0){
					STATE = 0;
					semaphore_core.release();
				}
			}
			updateProgressBar(clients);
			//System.out.println("Clients: "+clients);
		}
		semaphore_ping.release();
	}
	
	public void linkButons(JButton btn_start, JButton btn_stop){
		b_start = btn_start;
		b_stop = btn_stop;
	}
	
	private void updateStatus(String message){
		if(status == null) return;
		status.setText(message);
	}
	

	private void updateProgressBar(int value){
		if(bar == null) return;
		
		if(bar.getMaximum() < value){
			bar.setMaximum(value);
		}
		
		bar.setValue(value);
		bar.setString("Threads running: ["+value+"/"+bar.getMaximum()+"]");
		bar.revalidate();
	}
	
	private void sort(){
		
		String resultsReflector = "";
		if (summaryMode){
			if(sort_by == BY_SIZE) dirSizes = sortMapByValues(dirSizes);
			File[] files = dirSizes.keySet().toArray(new File[dirSizes.size()]);
			if(sort_by != BY_SIZE) Arrays.sort(files, comparator);
			for (File f :files){
				resultsReflector = resultsReflector + dirSizes.get(f) + " B" + SPACE + f.getAbsolutePath()+"\n";
			}
		} else {
			Collections.sort(results, comparator);
			//results.sort(comparator);
			if(results == null) return;
			for(File f : results){
				try{
					resultsReflector=resultsReflector+f.length()+" B"+SPACE+f.getAbsolutePath()+"\n";
				}catch (Exception e) {
					System.out.println("Exception2: "+e.getMessage());
				}
			}
		}
		
		resultsPanel.setText(resultsReflector);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap<File, Long> sortMapByValues(HashMap<File, Long> map) { 
		LinkedList list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return (((Long)((Map.Entry) (o1)).getValue()) < (Long)(((Map.Entry) (o2)).getValue())?-1:1) * order;
				}
		});
	

		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		} 
		return sortedHashMap;
	}
	
	
	private void getSpacer(){
		if(results.size() < 1)
			return;
		long fileSize = 0;
		
		for(File f :results){
			if(f.length() > fileSize) fileSize = f.length();
		}
		SPACE = "    ";
		while(fileSize > 0){
			fileSize /= 10;
			SPACE+=" ";
		}
		System.out.println("Spacer is: ["+SPACE+"]");
	}
	
	public void stopSorter(){
		this.STATE = 0;
		this.interrupt();
		System.out.println("Sorter stopped");
	}
	
	
	public void flushBuffer(){
		//getSpacer();
		//semaphore_core.release();
	}
	
	public void finish(){
		STATE = 0;
		getSpacer();
		sort();
	}
	
	
	
}



class Semaphore {
	
	volatile private int counter;
	
	public Semaphore(int count){
		if(count<0) throw new IllegalArgumentException(count+" cannot be < 0");
		this.counter=count;
	}
	
	
	public Semaphore() {
		this(0);
	}
	
	
	public synchronized void acquire() throws InterruptedException{
		while (counter == 0){
			this.wait();
		}
		counter--;
	}
	
	public synchronized void release(){
		if (counter == 0){
			this.notify();
		}
		counter++;
	}
	
	
}



