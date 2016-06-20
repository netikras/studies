package com.planner.netikras;


import java.awt.Dimension;

import com.planner.netikras.WindowMainClass;



/**
 * @author Darius Juodokas (a.k.a. netikras)
 * @version 1.0
 * 
 * @email dariuxas@gmail.com
 * 
 * @since Java 7
 * @comment Java v>=7 is required to run this application
 */
public class MainClass {

	
	public static void main(String[] args) {
		
		System.out.println("HelloWorld");
		SharedClass shared = new SharedClass();
		
		//shared.currentAcc			= "netikras";
		shared.aListener 			= new Listeners(shared);
		shared.MainFrameDimensions 	= new Dimension(800, 500);
		
		//WindowMainClass mainWind = new WindowMainClass(800, 500);
		
		WindowMainClass mainWind = new WindowMainClass(shared);
		
		mainWind.show(true);



	}

}
