package MainPackage;


import Tools.Frame;

public class Finder {


	
	public static final String TITLE = "JRFinder";
	public static final String FULL_TITLE = "Java Regex Finder";
	
	/**
	 * @title JRFinder<br>
	 * @author Darius Juodokas<br>
	 * @version v1<br>
	 * @college Vilniaus Kolegija<br>
	 * @group PIN13<br>
	 * @date 2015-01-16<br>
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		Frame frame = null;
		
		frame = new Frame(TITLE);

		frame.assembleFrame();
		frame.setFrameSize(445, 450);
		//frame.show();
		frame.setVisible(true);
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
