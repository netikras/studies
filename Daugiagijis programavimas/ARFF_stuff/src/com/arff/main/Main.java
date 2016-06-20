package com.arff.main;

import com.arff.gui.GUI;

/**

Užduotis: Duotas ARFF failų rinkinys. 
ARFF failas - specialios struktūros tekstinis failas, 
kuriame pirmiausiai apibrėžiami požymiai, o po to 
išrašomos jų reikšmės atskirtos kableliu. 
Reikšmės pozicija eilutėje sutampa su požymio pozicija požymių sąraše.
 
Sukurkite programą, kuri iš visų pateiktų failų sugeneruoja 
vieną ARFF struktūros failą su naudotojo pasirinktais požymiais.
 
 * @author netikras
 *
 */






public class Main {
	
	
	public static DBconnection DB = null;
	
	public static void main(String[] args){
		
		//String dbfile = "/tmp/dbfile";
		//String dbfile = DBconnection.IN_MEMORY;
		String db_dump_file = "/tmp/dbfile2";
		String arffDirPath = "/home/netikras/Dropbox/Dokumentai/Studijos/3k/1sem/Daugiagijis programavimas/ARFF_stuff/arff";
		//String arffDirPath = "/home/netikras/Dropbox/Dokumentai/Studijos/3k/1sem/Daugiagijis programavimas/ARFF_stuff/arff_demo";
		System.out.println("Starting app");
		GUI gui = new GUI("ARFF dumper");
		
		
		/*
		DataAnalyzer analyzer = new DataAnalyzer();
		
		analyzer.setArffDirectory(arffDirPath);
		analyzer.setDatabasePath(db_dump_file);
		analyzer.setUseInMemoryDatabase(true);
		
		analyzer.setThreadPoolSize(200);
		analyzer.setOperationTimeout(60);
		
		analyzer.findAttributesInDatabase(new String[]{"name"});
		//analyzer.findAttributesInFiles(new String[]{"name"});
		//analyzer.printResults();
		
		//analyzer.saveAttributesToDatabase(true, db_dump_file);
		//analyzer.saveAttributesToDatabase(false, db_dump_file);
		//*/
		
		System.out.println("End of MAIN");
	}
	
	
	
}



