package com.planner.netikras;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

import com.planner.netikras.UserClass.DATABASE.DB_METADATA;
import com.planner.netikras.UserClass.Member;
import com.planner.netikras.UserClass.Wallet;



public class SharedClass {

	protected final int 	GAP = 5;
	
	
	protected Listeners 		aListener;
	protected Dimension 		MainFrameDimensions;
	protected JFrame 			MainFrame;
	
	protected static final Color BG_COLOR = new Color(30,144,255);
	
	protected PanelCenterClass 	Panel_center;
	protected PanelBottomClass	Panel_bottom;
	protected PanelLeftClass	Panel_left;
	protected PanelTopClass		Panel_top;
	
	protected Object			ActionObject = null;
	private   MessageDigest		shredder;
	private   BigInteger		bigInt;
	
	protected String			currentView;
	protected String			currentAcc = "Atsijungęs";
	protected Member			currentMemb;
	protected Wallet			currentWallet;
	
	protected String			NO_CATEGORY = "Be kategorijos";
	
	protected JLabel			LabelEmpty = new JLabel(" ");
	
	protected ReturnSet			returned = new ReturnSet();
	
	protected UserClass user = new UserClass(this);
	
	protected static final int ValidateRule_USERNAME	= 0;
	protected static final int ValidateRule_USERPW		= 1;
	protected static final int ValidateRule_ROOTPW		= 2;
	protected static final int ValidateRule_USEREMAIL	= 3;
	protected static final int ValidateRule_USERGECOS	= 4;
	protected static final int ValidateRule_MEMBERNAME	= 5;
	protected static final int ValidateRule_MEMBERPW	= 6;
	protected static final int ValidateRule_MEMBEREMAIL	= 7;
	protected static final int ValidateRule_MEMBERGECOS	= 8;
	protected static final int ValidateRule_WALLETNAME	= 9;
	protected static final int ValidateRule_DATE		= 10;
	protected static final int ValidateRule_AMOUNT		= 11;
	protected static final int ValidateRule_FLOWCOMMENT	= 12;
	protected static final int ValidateRule_PLANNAME	= 13;
	protected static final int ValidateRule_PLANCOMMENT	= 14;
	protected static final int ValidateRule_CATEGORYNAME= 15;
	
	public SharedClass() {
		try {
			shredder = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			
		}
	}
	
	
	
	/**
	 * Encrypts a plain text password
	 * @param 
	 * 		source (String) - a plain-text password
	 * @return 
	 * 		(String) encrypted password<br>
	 * 		"1"	- Password does not meet requirements
	 */
	protected String encryptPW(String source){
		String result = "";
		shredder.reset();
		try {
			shredder.update(source.getBytes("UTF-8"), 0, source.length());
			bigInt = new BigInteger(1, shredder.digest());
			result = bigInt.toString(16);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	protected ReturnSet validateField(String text, int ruleName){
		//int retVal = 0;
		
		ReturnSet returns = new ReturnSet();
		
		
		switch(ruleName){
			case ValidateRule_USERNAME:
				text = text.trim();
				if(text.contains(user.DB.delimiter))
					returns.appendLn("Username cannot contain: "+user.DB.delimiter, 1);
				if(text.contains("'"))
					returns.appendLn("Username cannot contain: '\n'", 1);
				if (text.length() > DB_METADATA.colSize_USER_name)
					returns.appendLn("Username cannot be longer than "+DB_METADATA.colSize_USER_name+" symbols", 1);
				if (text.length() <= 3)
					returns.appendLn("Username should be at least 3 characters long", 1);
				break;
			case ValidateRule_USERPW:
				if (text.length() <= 3)
					returns.appendLn("User password should be at least 3 characters long", 1);
				if (text.equals(encryptPW("")))
					returns.appendLn("User password cannot be empty", 1);
				break;
			case ValidateRule_ROOTPW:
				if (text.length() <= 5)
					returns.appendLn("User ROOT password should be at least 3 characters long", 1);
				break;
			case ValidateRule_USEREMAIL:
				text = text.trim();
				if(! text.contains("@") || text.length() <= 3) // no @ sign or shorter than 4 chars
					returns.appendLn("Wrong user email address", 1);
				if(text.length() >DB_METADATA.colSize_USER_email)
					returns.appendLn("Email cannot be longer than " +DB_METADATA.colSize_USER_email+" characters", 1);
				break;
			case ValidateRule_USERGECOS:
				if(text.length() >= DB_METADATA.colSize_USER_gecos)
					returns.appendLn("User information cannot be longer than " +DB_METADATA.colSize_USER_gecos+" symbols", 1);
				break;
			
			case ValidateRule_MEMBERNAME:
				text = text.trim();
				if(text.contains(user.DB.delimiter))
					returns.appendLn("Member name cannot contain: "+user.DB.delimiter, 1);
				if(text.contains("'"))
					returns.appendLn("Member name cannot contain: '\n'", 1);
				if (text.length() > DB_METADATA.colSize_USER_name)
					returns.appendLn("Member name cannot be longer than "+DB_METADATA.colSize_MEMBER_name+" symbols", 1);
				if (text.length() <= 3)
					returns.append("Member nname should be at least 3 characters long", 1);
				break;
			case ValidateRule_MEMBERPW:
				// This in fact is optional and can be empty
				break;
			case ValidateRule_MEMBEREMAIL:
				if( ! text.isEmpty()){
					if( ! text.contains("@"))
						returns.appendLn("Wrong member email address.", 1);
					if(text.length() >DB_METADATA.colSize_MEMBER_email)
						returns.appendLn("Member email cannot be longer than " +DB_METADATA.colSize_MEMBER_email+" characters", 1);
				}
				break;
			case ValidateRule_MEMBERGECOS:
				if(text.length() >= DB_METADATA.colSize_MEMBER_gecos)
					returns.appendLn("Member information cannot be longer than " +DB_METADATA.colSize_MEMBER_gecos+" symbols", 1);
				break;
			case ValidateRule_DATE:
				text = text.trim();
				try{
					Double.parseDouble(text);
					System.out.println("DATE="+text);
					if(text.length() > DB_METADATA.colSize_FLOW_date)
						returns.appendLn("Unknown date format.", 1);
				} catch(NumberFormatException e){
					returns.appendLn("Cannot parse '"+text+"' to a number", 1);
				}
				
				break;
			case ValidateRule_AMOUNT:
				try{
					Double.parseDouble(text);

					if(text.length() > DB_METADATA.colSize_FLOW_amount)
						returns.appendLn("Unknown amount format.", 1);
				} catch(NumberFormatException e){
					returns.appendLn("Cannot parse '"+text+"' to a number", 1);
				}
				break;
			case ValidateRule_FLOWCOMMENT:
				text = text.trim();
				if(text.length() >= DB_METADATA.colSize_FLOW_comment)
					returns.appendLn("Flow Comment is too long. Maximum "+DB_METADATA.colSize_FLOW_comment+" symbols are allowed.", 1);
				if(text.length() < 1)
					returns.appendLn("Flow Comment cannot be empty", 1);
				break;
			case ValidateRule_PLANNAME:
				if( ! text.isEmpty())
					if(text.length() >= DB_METADATA.colSize_PLAN_name)
						returns.appendLn("Plan name cannot be longer than "+DB_METADATA.colSize_PLAN_name+" symbols. Currently: " + text.length(), 1);
				break;
			case ValidateRule_PLANCOMMENT:
				if( ! text.isEmpty())
					if(text.length() >= DB_METADATA.colSize_PLAN_comment)
						returns.appendLn("Plan comment cannot be longer than "+DB_METADATA.colSize_PLAN_comment+" symbols. Currently: " + text.length(), 1);
				break;
			case ValidateRule_WALLETNAME:
				int maxnamelen = DB_METADATA.colSize_WALLET_name - DB_METADATA.colSize_MEMBER_name - user.DB.delimiter.length();
				if(text.length() >= maxnamelen)
					returns.appendLn("Wallet name is too long. Maximum: " + maxnamelen + ". Current: " + text.length(), 1);
				break;
			case ValidateRule_CATEGORYNAME:
				if(text.length() <= 2)
					returns.appendLn("Category name should not be shorter than 3 symbols", 1);
				if(text.length() >= DB_METADATA.colSize_CATEGORY_name)
					returns.appendLn("Category name should not be longer than "+(DB_METADATA.colSize_CATEGORY_name - DB_METADATA.colSize_MEMBER_name-user.DB.delimiter.length())+" symbols", 1);
				
				break;
			default:
				//retVal = 1;
				returns.appendLn("Unknown ruleName: " + ruleName, 1);
				break;
		}
		
		//return retVal;
		return returns;
	}

	
	protected void loggedInActions(){

		Panel_top.loggedIn();
		Panel_left.loggedIn();
		Panel_center.loggedIn();
		
	}
	
	protected void loggedOutActions(){
		Panel_top.loggedOut();
		Panel_left.loggedOut();
		Panel_center.loggedOut();
		user.flush();
	}
	
	
	protected static class ReturnSet{
		
		private String message;
		private int errCode;
		
		public ReturnSet() {
			clear();
		}
		
		protected String getMessage(){
			return message;
		}
		
		protected int getCode(){
			return errCode;
		}
		
		
		protected void set(String text){
			message = text;
		}
		protected void set(int code){
			errCode = code;
		}
		protected void set(int code, String text){
			message = text;
			errCode = code;
		}
		
		protected void append(String text){
			message+=text;
		}
		protected void append(String text, int code){
			message+=text;
			errCode+=code;
		}
		protected void appendLn(String text, int code){
			message+=text+"\n";
			errCode+=code;
		}
		
		protected void clear(){
			message = "";
			errCode = 0;
		}
	}
	
	protected interface CALLBACK{
		void function();
		void function(String[] retVal); 
		void function(String retVal);
	}
	
	
	/**
	 * Creates a dialog box of one of the following types:<br>
	 * <b>Dialog.TYPE_INFO</b> 		<br>
	 * <b>Dialog.TYPE_WARN</b> 		<br>
	 * <b>Dialog.TYPE_ERR</b>		<br>
	 * All of the above create an informational dialog box. <br>
	 * Use method <b>setText(String)</b> to alter it's contents.<br>
	 * To make the box visible call method <b>setVisible(true)</b><br>
	 * 
	 * <b>Dialog.TYPE_QUESTION</b>	<br>
	 * Creates question dialog box. <br>
	 * Use method <b>setText(String)</b> to alter it's contents.<br>
	 * To make the box visible call method <b>setVisible(true)</b><br>
	 * <b>Dialog.TYPE_LOGIN</b>		<br>
	 * Creates a login box with 2 fields: username and password and a SUBMIT button.<br>
	 * Contents are static and not changeable.<br>
	 * <b>Dialog.TYPE_FORM</b>		<br>
	 * Creates a custom dialog box with one SUBMIT button and <br>
	 * as many JTextFields as one wants.<br>
	 * Textfields can be added by calling a method <b>addTextField(String label)</b><br>
	 * Form can be made visible by calling a method <b>ShowForm()</b>
	 * <br>
	 * <br>
	 * Method <b>setCallback(CALLBACK clbk)</b> is used to make forms do something.<br>
	 * Once form is submitted - callback function (if any) is called with parameters<br>
	 * respective to each type.<br>
	 * <b>TYPE_INFO</b>, <b>TYPE_WARN</b> and <b>TYPE_ERR</b> will not pass any parameters<br>
	 * <b>TYPE_QUESTION</b> will pass either "YES" or "NO"<br>
	 * <b>TYPE_LOGIN</b> and <b>TYPE_FORM</b> will pass String[] with fields' values<br>
	 * in the same order as fields were displayed in the box.
	 */
	protected static class Dialog extends JFrame{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7336719093492914170L;
		
		protected final static int TYPE_INFO 	= 0;
		protected final static int TYPE_WARN 	= 1;
		protected final static int TYPE_ERR		= 2;
		protected final static int TYPE_QUESTION= 3;
		protected final static int TYPE_LOGIN	= 4;
		protected final static int TYPE_FORM	= 5;
		
		
		
		private String[] retVal;
		
		protected CALLBACK callback;
		
		private LocalAL ALsnr = new LocalAL(); 
		
		JPanel panMain 		= new JPanel();
		JPanel panBody 		= new JPanel();
		JPanel panButtons 	= new JPanel();
		
		JTextArea textbox;
		protected JTextField tf;
		private JTextField[] tfForms;
		private JPasswordField pf;
		private JComboBox<String>[] ddns;
		Dimension dimTextField;
		
		
		
		Dialog(int type, String title){

			setTitle(title);
			setAlwaysOnTop(true);
			setAutoRequestFocus(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setResizable(false);
			setLocationRelativeTo(null);
			
			setMainPanel();
			
			switch (type) {
				case TYPE_INFO:
					addTextbox();
					addButton("Supratau", "ACK", "btnACK", true);
					pack();
					break;
				case TYPE_WARN:
					addTextbox();
					addButton("Gerai", "OK", "btnOK", true);
					pack();
					break;
				case TYPE_ERR:
					addTextbox();
					addButton("Gerai", "OK", "btnOK", true);
					pack();
					break;
				case TYPE_QUESTION:
					addTextbox();
					addButton("Taip", "YES/NO" ,"YES", true );
					addButton("Ne"	, "YES/NO" ,"NO" , false);
					pack();
					break;
				case TYPE_LOGIN:
					
					retVal = new String[2];
					
					dimTextField = new Dimension(200, 20);
					tf = new JTextField();
					pf = new JPasswordField();
					tf.setPreferredSize(dimTextField);
					pf.setPreferredSize(dimTextField);
					
					setLoginForm();
					addButton("Jungtis", "Login", "btnLogin", true);
					
					pack();
					break;
				case TYPE_FORM:
					panBody.setLayout(new BoxLayout(panBody, BoxLayout.Y_AXIS));
					panBody.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
					addButton("Baigta", "Form", "btnForm", true);
					//pack();
					break;
			
			}
		}
		
		protected void setCallback(CALLBACK callback){
			this.callback = callback;
		}
		
		protected void setText(String text){
			//System.out.println("Appending text: " + text);
			textbox.setText(text);
		}
		
		protected JTextField addTextField(String label, String defaultText){
			JLabel lblCustom = new JLabel(label);
			JTextField tfCustom = new JTextField();
			tfCustom.setPreferredSize(dimTextField);
			if(defaultText != null && defaultText != "")
				tfCustom.setText(defaultText);
			
			Object[] obj = SharedClass.addToArrayEnd(tfForms, tfCustom);
			tfForms = Arrays.copyOf(obj, obj.length, JTextField[].class);
			obj=null;
			
			panBody.add(lblCustom);
			panBody.add(tfCustom);
			
			return tfCustom;
		}
		
		protected JComboBox<String> addDropDownMenu(String label, String[] items){
			JComboBox<String> ddnCustom = new JComboBox<String>(items);
			JLabel lblCustom = new JLabel(label);
			ddnCustom.setPreferredSize(dimTextField);
			
			Object[] obj = SharedClass.addToArrayEnd(ddns, ddnCustom);
			ddns = Arrays.copyOf(obj, obj.length, JComboBox[].class);
			obj=null;
			
			panBody.add(lblCustom);
			panBody.add(ddnCustom);
			
			return ddnCustom; 
		}
		
		protected void ShowForm(){
			pack();
			setVisible(true);
		}
		
		private void setMainPanel(){
			panMain = new JPanel();
			panMain.setLayout(new BoxLayout(panMain, BoxLayout.Y_AXIS));
			setContentPane(panMain);
			panMain.add(panBody);
			panMain.add(panButtons);
		}
		
		private void addButton(String label, String actionCommand, String name, boolean DEFAULT){
			JButton button = new JButton(label);
			button.setActionCommand(actionCommand);
			button.setName(name);
			button.addActionListener(ALsnr);
			if(DEFAULT) button.setMnemonic(KeyEvent.VK_ENTER);
			panButtons.add(button);
		}
		
		private void addTextbox(){
			textbox = new JTextArea();
			textbox.setLineWrap(true);
			textbox.setWrapStyleWord(true);
			textbox.setEditable(false);
			textbox.setBackground(getBackground());
			textbox.setEnabled(true);
			textbox.setBorder(BorderFactory.createTitledBorder("Pranešimas"));
			textbox.setVisible(true);

			JScrollPane scroller = new JScrollPane();
			scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroller.setViewportView(textbox);
			scroller.setPreferredSize(new Dimension(200, 150));
			scroller.setVisible(true);
			panBody.add(scroller);
		}
		
		
		private void setLoginForm(){
			JLabel label1 = new JLabel("Vardas");
			JLabel label2 = new JLabel("Slaptažodis");
			
			
			
			GroupLayout layout = new GroupLayout(panBody);
			
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(label1)
						.addComponent(label2)
					)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(tf)
						.addComponent(pf)
					)
			);
			
			layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label1)
						.addComponent(tf)
					)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label2)
						.addComponent(pf)
					)
			);
			
			panBody.setLayout(layout);
			panBody.setBorder(BorderFactory.createTitledBorder("Prisijungimo duomenys"));
			
		}
		
		private class LocalAL implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				switch(e.getActionCommand()){
					case "Login":
						retVal[0] = tf.getText();
						retVal[1] = new String(pf.getPassword());
						if(callback != null) callback.function(retVal);
						dispose();
						
						break;
					case "ACK":
						if(callback != null) callback.function();
						dispose();
						break;
					case "OK":
						if(callback != null) callback.function();
						dispose();
						break;
					case "YES/NO":
						if(callback != null) callback.function(button.getName());
						dispose();
						break;
					case "Form":
						if(callback != null) {
							int resultID = 0;
							int tf_entries_ct = 0;
							int ddn_entries_ct = 0;
							
							if(tfForms != null) tf_entries_ct = tfForms.length;
							if(ddns != null) ddn_entries_ct = ddns.length;
							
							String[] results = new String[tf_entries_ct + ddn_entries_ct];
							
							for(int i=0; i<tf_entries_ct; i++)
								results[resultID++] = tfForms[i].getText();
							
							for(int i=0; i<ddn_entries_ct; i++)
								results[resultID++] = ddns[i].getSelectedItem().toString();
							
							callback.function(results);
						}
						dispose();
						break;
				
				}
			}
			
		}
		
	}
	
	
	protected static class QryBuilder{
		
		/**
		 * e.g. "SELECT *"
		 */
		
		String query = "";
		
		QryBuilder(){
		}
		
		protected void flush(){
			query = new String("");
		}
		
		protected String pack(){
			return query;
		}
		
		protected QryBuilder print(){
			System.out.println(query);
			return this;
		}
		
		protected QryBuilder select(String what){
			query = query + " SELECT " + what;
			return this;
		}
		
		protected QryBuilder insert(){
			query = query + " INSERT";
			return this;
		}
		
		protected QryBuilder delete(){
			query = query + " DELETE";
			return this;
		}
		
		protected QryBuilder update(String tableName){
			query = query + " UPDATE " + tableName;
			return this;
		}
		
		protected QryBuilder set(){
			query = query + " SET";
			return this;
		}
		
		protected QryBuilder and(){
			query = query + " AND";
			return this;
		}
		
		protected QryBuilder or(){
			query = query + " OR";
			return this;
		}
		
		protected QryBuilder into(String where){
			query = query + " INTO " + where;
			return this;
		}
		
		protected QryBuilder columns_and_values(String[] cols, String[] vals){
			if(cols != null){
				query = query + " (";
				for(int i=0; i<cols.length; i++)
					query = query + cols[i] + (i < cols.length-1?", ":"");
				query = query + ")";
			}
			
			query = query + " VALUES (";
			for(int i=0; i<vals.length; i++)
				query = query + "'" + vals[i] + "'" + (i < vals.length-1?", ":"");
			query = query + ")";
			
			return this;
		}
		
		protected QryBuilder from(String where){
			query = query + " FROM " + where;
			return this;
		}
		
		protected QryBuilder where(){
			query = query + " WHERE";
			return this;
		}
		
		protected QryBuilder bracket_L(){
			query = query + " (";
			return this;
		}
		
		protected QryBuilder bracket_R(){
			query = query + ")";
			return this;
		}
		
		protected QryBuilder x_eq_y(String columnName, String value){
			if(value == null)
				query = query + " " + columnName + " IS NULL";
			else
				query = query + " " + columnName + " = '" + value + "'";
			return this;
		}
		
		protected QryBuilder x_lt_y(String columnName, String value){
			query = query + " " + columnName + " < '" + value + "'";
			return this;
		}
		
		protected QryBuilder x_gt_y(String columnName, String value){
			query = query + " " + columnName + " > '" + value + "'";
			return this;
		}
		
		protected QryBuilder x_le_y(String columnName, String value){
			query = query + " " + columnName + " <= '" + value + "'";
			return this;
		}
		
		protected QryBuilder x_ge_y(String columnName, String value){
			query = query + " " + columnName + " >= '" + value + "'";
			return this;
		}
		
		protected QryBuilder x_ne_y(String columnName, String value){
			if(value == null)
				query = query + " " + columnName + " IS NOT NULL";
			else
				query = query + " " + columnName + " <> '" + value + "'";
			return this;
		}
		
		protected QryBuilder x_like_y(String columnName, String value){
			query = query + " " + columnName + " LIKE '" + value + "'";
			return this;
		}
	}
	
	/**
	 * A neat trick to add functions to array
	 */
	protected interface FUNCTION{
		void run();
	}
	
	
	protected static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
        	//System.out.println("    added 1 day and now it's: "+ date.getTime().toString());
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
	
	protected static <T> boolean arrayContains(final T[] array, final T v) {
	    if (v == null) {
	        for (final T e : array)
	            if (e == null)
	                return true;
	    } else {
	        for (final T e : array)
	            if (e == v || v.equals(e))
	                return true;
	    }

	    return false;
	}
	
	
	protected static <T> T[] removeFromArray(final T[] array, final T element){
		int newLength = array.length;
		int offset=0;
		
		for(int i=0; i<array.length; i++)
			if(array[i] == element)
				newLength -= 1;
			
		T[] newArr = (T[])new Object[newLength];
		
		for(int i=0; i<array.length; i++)
			if(array[i] == element)
				offset+=1;
			else
				newArr[i-offset] = array[i];
		
		return newArr;
	}
	
	
	protected static  <T> T[] addToArrayEnd(final T[] array, final T element){
		int newLength = array == null? 1 : array.length+1;
		int i = 0;
		
		T[] newArr = (T[])new Object[newLength];
		
		if(array != null)
			for(; i<array.length; i++)
				newArr[i] = array[i];
		newArr[i] = element;
		
		return newArr;
	}
	
	protected static <T> T[] addToArrayFront(final T[] array, final T element){
		int newLength = array.length+1;
		int i = 0;
		
		T[] newArr = (T[])new Object[newLength];
		
		newArr[i] = element;
		i++;
		if(array != null)
			for(; i<=array.length; i++)
				newArr[i] = array[i-1];
			
		return newArr;
	}
	

	protected static <T> T[][] addToArrayEnd(final T[][] array, final T element[]){
		int newLength = array.length+1;
		int i = 0;
		
		T[][] newArr = (T[][])new Object[newLength][element.length];
		
		if(array != null)
			for(; i<array.length; i++)
				newArr[i] = array[i];
		newArr[i] = element;
		
		return newArr;
	}

	protected static <T> T[][] addToArrayFront( T[][] array,  T element[]){
		int newLength = array.length+1;
		int i = 0;
		
		T[][] newArr = (T[][])new Object[newLength][element.length];

		newArr[i] = element;
		
		i++;
		if(array != null)
			for(; i<=array.length; i++)
				newArr[i] = array[i-1];
			
		return newArr;
	}
	
}
