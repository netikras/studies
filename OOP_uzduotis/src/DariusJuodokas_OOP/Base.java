package DariusJuodokas_OOP;

 /**
  * @Author netikras; 
  * @EMAIL dariuxas@gmail.com
  * @TITLE_LT Paprasta ir primityvi grafinė programa, demonstruojanti langų tarpusavyje susiejimą, modalumą
  * @TITLE_EN A very basic application illustrating relations between windows, modality
 //*/

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class Data{
	/*
	 * A class concentrating JForm and
	 * other components required
	 * for a window.
	 * */
	
	private   JFrame 	Frame;		// Main Frame
	protected JPanel 	pMain;		// MAIN Panel
	private   Dimension fSize;		// Size of the MAIN Frame
	private   JMenuBar 	mMenuBar;	// Main Menu bar for MAIN Frame
	private   JMenu 	mMenu;		// Main Menu entry
	
	protected ActionListener ActionL_apie;
	protected ActionListener ActionL_run;
	
	private void setMenu(JFrame frm){
		/*
		 * Functions creates main menu
		 * for the window
		 * */
		
		this.mMenuBar = new JMenuBar();
		this.Frame.setJMenuBar(mMenuBar);
		
		mMenu = new JMenu("Meniu");
		mMenuBar.add(mMenu);
		
		JMenuItem mMenuItemAbout = new JMenuItem("Apie");
			mMenuItemAbout.setActionCommand("Apie");
			mMenuItemAbout.addActionListener(this.ActionL_apie);
			mMenu.add(mMenuItemAbout);

		JMenuItem mMenuItemRun = new JMenuItem("Paleisti");
			mMenuItemRun.setActionCommand("Paleisti");
			mMenuItemRun.addActionListener(this.ActionL_run);
			mMenu.add(mMenuItemRun);

	}
	
	

	private void setFrame(String title){
		/*
		 * Function creates a frame
		 * and sets its default size
		 * */
		this.Frame = new JFrame(title);
		this.fSize = new Dimension(400, 500);
		this.Frame.setSize(this.fSize);
		this.Frame.setResizable(false);
	}
	
	private void setPanels(JFrame frm){ 
		/*
		 * Function creates MAIN panel
		 *  
		 * and assigns it to the window
		 * */
		pMain = new JPanel();
		pMain.setSize(
				fSize.width - 20, 
				fSize.height - 20
				);
		pMain.setLocation(10, 10);
		
		frm.add(pMain);
	}
	
	Data(String title){ // Constructor
		/*
		 * Constructor creates a frame 
		 * with desired title and maps
		 * a MAIN panel to it
		 * */
			this.setFrame(title);
			this.setPanels(this.Frame);		
	}
	
	
	
	protected void show(){
		/*
		 * Function displays the window
		 * with all its attributes
		 * */
		this.Frame.show();
	}
	
	protected void addMenu(){
		/*
		 * Function adds main menu
		 * to the window frame
		 * Should be used for PARENT
		 * frame.
		 * */
		this.setMenu(this.Frame);
	}
	
	protected void setSize(int x, int y){
		/*
		 * Function sets frame dimensions
		 * */
		this.fSize.setSize(x, y);
		this.Frame.setSize(this.fSize);
	}
	
	protected JFrame getFrame(){
		/*
		 * Function gives access to
		 * the frame object itself.
		 * Might be useful when assigning
		 * events listeners, etc..
		 * */
		return this.Frame;
	}
	
	protected Dimension getSize(){
		/*
		 * Function returns the 
		 * size of the frame
		 * */
		return this.fSize;
	}
	
	
}



class ActionL_apie implements ActionListener {
	/*
	 * Action listener to be triggered once
	 * user selects something from
	 * main menu. Will show() desired window
	 * created as Data class object
	 * */

	Data 	Parent;				// we might need to know what's the parent in case we want modality
	Data 	childFrame;			// a window to be show()n
	boolean lockParent = false;	// Do we want modality for MAIN Frame?
	
	ActionL_apie(Data BaseFrame, Data Child, boolean lockParent){ // Constructor
		/*
		 * Constructor simply sets
		 * local variables
		 * */
		this.Parent 	= BaseFrame;
		this.childFrame = Child;
		this.lockParent = lockParent;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.childFrame.show();	// Let's show desired window

		if(this.lockParent == true){	// and if this window should be modal
			this.Parent.getFrame().setFocusable(false);				// disable MAIN frame
			this.Parent.getFrame().setEnabled(false);				// push it back
			this.Parent.getFrame().setFocusableWindowState(false);	// remove focus from it
		}
	}	
}


class ActionCalc implements ActionListener{
	/*
	 * Listens for button press and
	 * calculates how many times
	 * the first word has been entered.
	 * NOTE: Entered string must begin with a normal word, not a word separator
	 * */

	String 		sourceText;	// Text in which we'll be looking for duplicate words
	JTextArea 	tfSource;	// Source of the above
	String 		firstWord;	// ...first word...
	int 		count=0;	// How many times does it occur?
	JLabel 		lResult;	// Label where we'll show the above count
	String[] 	tokens;		// a buffer array to store source string tokenized into words 
	
	ActionCalc(JTextArea source, JLabel destination){ // Constructor
		/*
		 * Constructor sets internal variables:
		 *  * source of text
		 *  * destination of results
		 * */
		this.tfSource = source;
		this.lResult = destination;
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		this.count 		= 0;	// Initially count is zero
		this.sourceText = this.tfSource.getText();		// Now we pull the source text
		this.tokens 	= this.sourceText.split("\\W"); // .. and tokenize it
		this.firstWord 	= this.tokens[0];				// first word a.k.a. token_0

		for(int i=0; i<this.tokens.length; i++){		// for each token...
			if(this.tokens[i].equals(this.firstWord)){	// check if it matches the first word
				this.count++;							// and increase count if it does
			}
		}
		this.lResult.setText(Integer.toString(this.count) + " (" + this.firstWord + ")"); // Finally show results on RESULTS label
	}
} 


class W_State implements WindowListener{
	/*
	 * Will be used by child windows to 
	 * implement custom modality
	 * and re-enable ability to 
	 * focus MAIN window
	 * */

	private Data Parent; // Parent (MAIN) window
	
	W_State(Data Parent){  // Constructor
		this.Parent = Parent;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		// Re-enabling ability to focus MAIN window 
		this.Parent.getFrame().setFocusable(true);
		this.Parent.getFrame().setEnabled(true);
		this.Parent.getFrame().setFocusableWindowState(true);
		
		this.Parent.getFrame().setVisible(true); // returning focus to main frame
	}
	
	// other methods inherited from abstract Listener class... Not interesting for us
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}	
}



public class Base { // Main class
	/*
	 * Main class
	 * */

	public static void main(String[] args) {
		
		 Data langas = new Data("Pagrindinis langas");  // creating MAIN window
		 Data apie   = new Data("Apie...");				// creating HELP window (child)
		 Data run    = new Data("Paleidimas");			// creating RUN window  (child)
		 
		langas.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // telling MAIN window to terminate application once it's closed
		
		// Adding Window Listener to detect window closure event and trigger modality-related functions 
		run.getFrame().addWindowListener(new W_State(langas));	
		
			//Adding ActionListeners for MENU selections
		langas.ActionL_apie = new ActionL_apie(langas, apie, false);	// make it non-modal
		langas.ActionL_run = new ActionL_apie(langas, run, true);		// make it modal
		
		
		apie.setSize(220, 250);		// Setting window size manually. Not a good practice to hardcode window sizes, but... oh well...
		run.setSize(250, 150);		// Setting window size manually.

		/* ########### CHILD window __APIE__ ########### */
		/*APIE*/ JTextArea text = new JTextArea(		// INFO text
		/*APIE*/ 		"Sveiki,\n"
		/*APIE*/ 		+"Tai - demonstracinė\n"
		/*APIE*/ 		+"JAVA GUI programėlė.\n\n"
		/*APIE*/ 		+"Užduotis:\n"
		/*APIE*/ 		+"7 Kiek kartų simbolių eilutėje \n"
		/*APIE*/ 		+ "pakartotas pirmasis žodis?\n\n"
		/*APIE*/ 		+ "Autorius:\n"
		/*APIE*/ 		+ "       Darius Juodokas\n"
		/*APIE*/ 		+ "\tPIN13"
		/*APIE*/ 		);
		/*APIE*/ text.setBackground(apie.getFrame().getBackground());	// Making all the window have the same background color
		/*APIE*/ text.setEditable(false);								// Lock the textField to prevent user from messing around w/ it
		/*APIE*/ apie.pMain.add(text);									// Adding textField to main panel of the child window
		/* ############################################ */
		
		
		/* ########### CHILD window __RUN__ ########### */
		/*RUN*/ JLabel lbl = new JLabel("Įveskite tekstą:");			// Creating a label telling user what to do
		/*RUN*/ run.pMain.add(lbl);										// ..and adding it to MAIN panel of CHILD window RUN
		/*RUN*/ lbl.setLocation(10, 10);								// telling the label to appear 10pts away from top & left
		/*RUN*/ lbl.setBackground(run.getFrame().getBackground());		// Making background homogenic
		/*RUN*/ 
		/*RUN*/ JTextArea inpText = new JTextArea();				// Creating area for input text
		/*RUN*/ inpText.setLineWrap(true);							// ..and making it multi-linear
		/*RUN*/ inpText.setWrapStyleWord(true);						// ..without breaking words apart
		/*RUN*/ 
		/*RUN*/ run.pMain.setLayout(new BoxLayout(run.pMain, BoxLayout.PAGE_AXIS)); // Setting CHILD window layout. Not the best one though...
		/*RUN*/ run.pMain.add(inpText);								// and adding the text input area to the MAIN panel of CHILD window RUN
		/*RUN*/ inpText.setText("Enter text here");					// setting default dummy text
		/*RUN*/ 
		/*RUN*/ inpText.setSize(new Dimension(						// Setting textArea size manually. Not the best way again.. 
		/*RUN*/ 		run.getSize().width-20,
		/*RUN*/ 		lbl.getSize().height
		/*RUN*/ 		));
		/*RUN*/ inpText.setLocation(								// umm.. this is a rebel attempt to resist layout manager and set TF position manually. Someday I'll make it! :)
		/*RUN*/ 		10,
		/*RUN*/ 		lbl.getLocation().y + lbl.getSize().height
		/*RUN*/ 		);
		/*RUN*/ 
		/*RUN*/ JButton butt = new JButton("Skaičiuoti");			// Creating button for calculating the result
		/*RUN*/ run.pMain.add(butt);								// and adding it to the MAIN panel of CHILD window RUN
		/*RUN*/ 
		/*RUN*/ JLabel lResult = new JLabel();					// Creating a label where the results will be shown
		/*RUN*/ run.pMain.add(lResult); 						// ..and adding it to MAIN panel as well	
		/*RUN*/ 
		/*RUN*/ butt.addActionListener(new ActionCalc(inpText, lResult));	// Assigning Action Listener to the button
		/* ############################################ */
		
		
		langas.addMenu();	// Adding Main Menu to the MAIN window 
		langas.show();		// and showing it on a screen
	}

}
