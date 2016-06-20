package com.planner.netikras;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.text.DateFormatter;

import com.planner.netikras.SharedClass.CALLBACK;
import com.planner.netikras.SharedClass.Dialog;
import com.planner.netikras.SharedClass.QryBuilder;
import com.planner.netikras.SharedClass.ReturnSet;
import com.planner.netikras.UserClass.DATABASE.DB_METADATA;

public class UserClass {

	SharedClass shared;
	DATABASE 	DB;
	
	
	
	public UserClass(SharedClass data) {
		shared = data;
		DB = new DATABASE();

		try {
			DB.init();
			DB.connect();
		} catch (SQLException e) {
			System.out.println("Nepavyko prisijungti prie DB");
			
			Dialog dialogFialedLoginDB = new Dialog(Dialog.TYPE_ERR, "Nepavyko prisijungti");
			dialogFialedLoginDB.setText(
					"Nepavyko prisijungti prie duomenų bazės.\n"+
					"Kad programa veiktų tinkamai - parenkite MySQL DB taip, kaip nurodyta diegimo instrukcijoje.\n"+
					"T.y. reikalinga tinkama DB struktūra, tinkamas vartotojas su tinkamu vartotojo vardu ir slaptažodžiu."
							);
			dialogFialedLoginDB.setVisible(true);
			
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println(DB.DBurl);
		flush();
	}

	
	protected	String		uname	;	
	protected	String		uemail	;	
	protected	String		upw_hash	;
	protected	String		upw_hash_root;
	protected	String		ugecos	;	
	
	protected	boolean		F_ROOT	;
	protected	Member[]	members ; // list of members
	//protected 	Member		memberSelected;
	
	
	
	/**
	 * Initiates object variables and empties them;
	 */
	protected void flush(){
		uname	= "";
		uemail	= "";
		upw_hash	= "";
		upw_hash_root = "";
		ugecos	= "";
		
		members	= null;
		F_ROOT = false;
		
		shared.currentMemb = null;
		shared.currentAcc = null;
		shared.currentWallet = null;
		
	}
	
	
	
	/**
	 * Login to the user, i.e. pull user from the DB and fill-in the members' list
	 * @param username (String) - username to look for
	 * @param password (String) - an encrypted password the user should have
	 * @return
	 * 		0 - SUCCESS<br>
	 * 		1 - No such user<br>
	 * 		2 - Wrong password
	 * @throws SQLException 
	 */
	protected ReturnSet login(String username, String password){
		ReturnSet retSet = new ReturnSet();
		
		ReturnSet VALID;
		VALID = shared.validateField(username, shared.ValidateRule_USERNAME);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(password, shared.ValidateRule_USERPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;
		
		
		retSet = loginUser(username, password);
		if(retSet.getCode() == 0){
			retSet = pullMembersList();
			shared.currentAcc = username;
		}
		return retSet;
	}
	
	protected ReturnSet loginROOT(String username, String password){
		ReturnSet retSet = new ReturnSet();
		
		ReturnSet VALID;
		VALID = shared.validateField(username, shared.ValidateRule_USERNAME);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(password, shared.ValidateRule_ROOTPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;
		
		if( ! username.equals(uname) )
			retSet.appendLn("User name not recognized", 1);
			
		if( ! password.equals(upw_hash_root))
			retSet.appendLn("Wrong password", 1);
		return retSet;
	}
	
	private ReturnSet loginUser(String username, String password){
		ReturnSet retSet = new ReturnSet();
		ResultSet result = null;
		String query;
		
		System.out.println("[INFO] Logging in as user: " + username);
		
		ReturnSet VALID;
		VALID = shared.validateField(username, shared.ValidateRule_USERNAME);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(password, shared.ValidateRule_ROOTPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;
		
		
		//* QUERY *//
		query = "SELECT * from " + DB_METADATA.tbl_USER_name + 
					" WHERE "+ DB_METADATA.col_USER_name +" = '" + username + "'";
		
		try { 
			result = DB.ExecuteQuery(query);
			
			while(result.next()){
				uname 	= result.getString(DB_METADATA.col_USER_name);
				uemail 	= result.getString(DB_METADATA.col_USER_email);
				upw_hash = result.getString(DB_METADATA.col_USER_password);
				upw_hash_root = result.getString(DB_METADATA.col_USER_root_password);
				ugecos 	= result.getString(DB_METADATA.col_USER_gecos);
			}
			
			if(uname == ""){
				throw new SQLException("User not found");
			} else {
				if (upw_hash.equals(password)){
					shared.currentAcc = uname;
					shared.loggedInActions();
				} else {
					flush();
					throw new SQLException("Wrong password");
				}
			}
			
			//pullMembersList();
			
		} catch (SQLException e) {
			shared.Panel_bottom.setText("Login failed ["+username+"]: " + e.getMessage());
			System.out.println("[ERR] Login failed: " + e.getMessage());
			retSet.appendLn(e.getMessage(), 1);
		}
		
		try {
			result.close();
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		return retSet;
	}
	
	protected ReturnSet loginMember(String membername){
		// if member is loaded successfully it should be accessible via member[0]
		ReturnSet retSet = new ReturnSet();
		
		ReturnSet VALID;
		VALID = shared.validateField(membername, shared.ValidateRule_MEMBERNAME);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;
		
		
		final Member M = findMemberByName(membername);
		if(M != null){
			if( ! M.mpw_hash.equals(shared.encryptPW("")) ){
				Dialog dialogMemberPW = new Dialog(Dialog.TYPE_LOGIN, "Nario slaptažodis");
				dialogMemberPW.tf.setText(membername+"@"+uname);
				dialogMemberPW.tf.setEditable(false);
				dialogMemberPW.setCallback(new CALLBACK() {
					public void function(String retVal) {}
					public void function() 				{}					
					@Override
					public void function(String[] retVal) {
						if(shared.encryptPW(retVal[1]).equals(M.mpw_hash)){
							shared.Panel_bottom.setText("Member " + M.mname + " has logged in");
							shared.currentMemb = M;
							M.loadWallets();
							M.loadCategories();
							shared.Panel_left.ButtonMembSettings.setEnabled(true);
						} else {
							Dialog dialogMemberLoginFailed = new Dialog(Dialog.TYPE_ERR, "Nepavyko");
							dialogMemberLoginFailed.setText("Nepavyko prijungti nario " + M.mname);
							dialogMemberLoginFailed.setVisible(true);
						}
					}

				});
				dialogMemberPW.setVisible(true);
			} else {
				shared.currentMemb = M;
				M.loadWallets();
				M.loadCategories();
				shared.Panel_left.ButtonMembSettings.setEnabled(true);
			}
		}
		
		return retSet;
	}
	
	protected ReturnSet logout(){
		ReturnSet retSet = new ReturnSet();
		
		flush();
		shared.currentAcc = "Atsijungęs"; 
		shared.Panel_left.dropdown.removeAllItems();
		shared.Panel_left.setBorder(BorderFactory.createTitledBorder(shared.currentAcc));
		shared.Panel_top.loggedOut();
		
		return retSet;
	}
	
	
	
	protected ReturnSet addNewUser(String name, String password, String passwordROOT, String email, String geckos) throws SQLException{
		ReturnSet retSet = new ReturnSet(); // OK
		
		System.out.println("[INFO] Creating a new user: " + name);
		

		ReturnSet VALID;
		VALID = shared.validateField(name, shared.ValidateRule_USERNAME);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(password, shared.ValidateRule_USERPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(passwordROOT, shared.ValidateRule_ROOTPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(email, shared.ValidateRule_USEREMAIL);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(geckos, shared.ValidateRule_USERGECOS);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		System.out.println("[ERR] user creation failed:\n" + retSet.getMessage());
		
		if(retSet.getCode() > 0) return retSet;
		
		
		String query = "INSERT INTO " + DB_METADATA.tbl_USER_name +
							" ( " +
								DB_METADATA.col_USER_name			+ ", " 	+
								DB_METADATA.col_USER_password		+ ", " 	+
								DB_METADATA.col_USER_root_password	+ ", " 	+
								DB_METADATA.col_USER_email			+ ", " 	+
								DB_METADATA.col_USER_gecos			+ "" 	+
							" ) " +
						"VALUES" +
							" ( " +
								"'" + name			+ "', " +
								"'" + password		+ "', " +
								"'" + passwordROOT	+ "', " +
								"'" + email			+ "', " +
								"'" + geckos		+ "'"   +
							" ) ";
		System.out.println(query);
		
		try {
			DB.ExecuteUpdate(query);
			shared.Panel_bottom.setText("User " + name + " has been created successfully");
			System.out.println("[OK] User " + name + " has been created successfully");
		} catch (SQLException e) {
			shared.Panel_bottom.setText("User creation failed: " + e.getMessage());
			System.out.println("[ERR] User creation failed: " + e.getMessage());
			//e.printStackTrace();
			retSet.appendLn(e.getMessage(), 1);
		}
		return retSet;
	}
	
	protected ReturnSet deleteUser(){
		ReturnSet retSet = new ReturnSet(); // OK

		String query;
		ResultSet result;
		System.out.println("[INFO] Removing user: " + uname);
		
		/*
		 * No matter how local data has been altered - we need to remove ALL related data from the database.
		 * Local data cannot be relied on as it could have been modified during runtime and not in sync with the DB
		 */
		
		//* QUERY *//
		query = "SELECT * FROM " + DB_METADATA.tbl_MEMBER_name + 
					" WHERE " + 
					DB_METADATA.col_USER_name + " = '" + uname + "'";
		
		try {
			result = DB.ExecuteQuery(query);
			if(result.last()){
				members = new Member[result.getRow()];
				result.beforeFirst();
			}
			
			while(result.next()){
				String membername_full = result.getString(DB_METADATA.col_MEMBER_name);
				deleteMember(membername_full);
			} //while
			
			/* removing actual user */
			//* QUERY *//
			query = "DELETE from " + DB_METADATA.tbl_USER_name + 
						" WHERE " +
							DB_METADATA.col_USER_name + " = '" + uname +"'";
			DB.ExecuteUpdate(query);
			System.out.println("[OK] User " + uname + " has been deleted successfully");
			flush();
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		
		
		
		return retSet;
	}
	
	protected ReturnSet updateUser(){
		ReturnSet retSet = new ReturnSet();
		

		ReturnSet VALID;
		VALID = shared.validateField(upw_hash, shared.ValidateRule_USERPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(upw_hash_root, shared.ValidateRule_ROOTPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(uemail, shared.ValidateRule_USEREMAIL);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(ugecos, shared.ValidateRule_USERGECOS);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;

		
		String query;
		
		query = "UPDATE " + DB_METADATA.tbl_USER_name + 
				" SET " +
					DB_METADATA.col_USER_email + "='" + uemail + "', " +
					DB_METADATA.col_USER_gecos + "='" + ugecos + "', " +
					DB_METADATA.col_USER_password + "='" + upw_hash + "', " +
					DB_METADATA.col_USER_root_password + "='" + upw_hash_root + "'" +
				" WHERE " +
					DB_METADATA.col_USER_name + "='" + uname + "'";
		
		try {
			DB.ExecuteUpdate(query);
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		
		return retSet;
		
	}
	
	
	
	/**
	 * Pulls all the user-linked members from database and populates a local list
	 * 
	 * @return 
	 * 		0 - OK<br>
	 * 		1 - FAIL
	 * @throws SQLException 
	 */ 
	protected ReturnSet pullMembersList() {
		ReturnSet retSet = new ReturnSet(); // OK
		String query;
		ResultSet result;
		int rowNo;

		//* QUERY *//
		query = "SELECT * FROM " + DB_METADATA.tbl_MEMBER_name + 
					" WHERE " + 
					DB_METADATA.col_USER_name + " = '" + uname + "'";
		
		shared.Panel_left.dropdown.removeAllItems();
		shared.Panel_left.dropdown.addItem("---");
		
		try {
			result = DB.ExecuteQuery(query);
			if(result.last()){
				members = new Member[result.getRow()];
				result.beforeFirst();
			}
			
			shared.Panel_left.dropdown.removeAllItems();
			while(result.next()){
				rowNo = result.getRow()-1;
				members[rowNo] = new Member(
										result.getString(DB_METADATA.col_MEMBER_name).split(DB.delimiter_rgx)[1],
										result.getString(DB_METADATA.col_MEMBER_email),
										result.getString(DB_METADATA.col_MEMBER_gecos),
										result.getString(DB_METADATA.col_MEMBER_password)
										);
				
				members[rowNo].mname_full	= result.getString(DB_METADATA.col_MEMBER_name);
				
				shared.Panel_left.dropdown.addItem(members[rowNo].mname);
				
				
			}
			
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		
		
		
		return retSet;
	}
	
		
	
	
	/**
	 * Creates a new member but does not push it to the database yet
	 * @param name    (String) - name of the new member
	 * @param encr_pw (String) - encrypted password
	 * @param email   (String) - email address
	 * @return 
	 * 		0 - OK<br>
	 * 		1 - FAIL
	 */
	protected ReturnSet addNewMember(String name, String encr_pw, String email, String gecos){
		ReturnSet retSet = new ReturnSet(); // OK
		
		System.out.println("[INFO] Adding new member: " + name);
		

		ReturnSet VALID;
		VALID = shared.validateField(name, shared.ValidateRule_MEMBERNAME);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(encr_pw, shared.ValidateRule_MEMBERPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(email, shared.ValidateRule_MEMBEREMAIL);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		VALID = shared.validateField(gecos, shared.ValidateRule_MEMBERGECOS);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;

		
		
		String query = "INSERT INTO " + DB_METADATA.tbl_MEMBER_name +
							" ( " +
								DB_METADATA.col_MEMBER_name			+ ", "	+
								DB_METADATA.col_MEMBER_password		+ ", "	+
								DB_METADATA.col_MEMBER_email		+ ", "	+
								DB_METADATA.col_USER_name			+ ", "	+ 
								DB_METADATA.col_MEMBER_gecos		+ ""	+
							" ) " +
						"VALUES" +
							" ( " +
								"'" + this.uname + DB.delimiter + name	+ "', "	+
								"'" + encr_pw	+ "', "	+
								"'" + email		+ "', "	+
								"'" + this.uname+ "', " +
								"'" + gecos		+"'"	+
							" ) ";
		System.out.println("[OK] Member created successfully!");
		try{
			DB.ExecuteUpdate(query);
		}catch (SQLException e) {
			System.out.println("[ERR] Member creation failed: " + e.getMessage());
			retSet.appendLn(e.getMessage(), 1);
		}
		
		return retSet;
	}
	
	
	
	/** 
	 * Deletes a member from the local list and from the database instantly<br>
	 * @param membName (String) - a FULL (!) member name to delete
	 * @return 
	 * 		0 - OK<br>
	 * 		1 - FAIL
	 */
	protected ReturnSet deleteMember(String membName){
		//int retVal = 0; // OK
		ReturnSet retSet = new ReturnSet();
		String query;
		
		System.out.println("[INFO] Deleting a member: " + membName);
		
		for(String tablename : new String[] {
												DB_METADATA.tbl_FLOW_name, 
												DB_METADATA.tbl_PLAN_name,
												DB_METADATA.tbl_CATEG_name,
												DB_METADATA.tbl_WALLET_name,
												DB_METADATA.tbl_MEMBER_name
											} ){

			//* QUERY *//
			query = "DELETE from " + tablename + 
				" WHERE " +
					DB_METADATA.col_MEMBER_name + " = '" + uname + DB.delimiter + membName + "'";
			try {
				DB.ExecuteUpdate(query);
				retSet.set("[OK] Member has been deleted successfully");
				System.out.println("[OK] Member has been deleted successfully");
				//flush();
				members=null;
				pullMembersList(); // refreshing
			} catch (SQLException e) {
				System.out.println("[ERR] Failed to delete a member: " + e.getMessage());
				retSet.appendLn("Failed to delete a member", 1);
			}


		} //for
		
		return retSet;
	}
	
	
	
	protected ReturnSet updateName(String newName){
		ReturnSet retSet = new ReturnSet();
		String query;
		

		ReturnSet VALID;
		VALID = shared.validateField(newName, shared.ValidateRule_USERNAME);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;

		
		newName = newName.trim();
		
		query = "UPDATE " + DB_METADATA.tbl_USER_name + 
					" SET " + 
						DB_METADATA.col_USER_name + " = '" + newName + "'" +
					" WHERE " +
						DB_METADATA.col_USER_name + " = '" + uname + "'";
		
		try {
			if (DB.ExecuteUpdate(query) == 0)
				throw new SQLException("No rows have been updated");
			uname = newName;
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		return retSet;
	}
	
	protected ReturnSet updateEmail(String newEmail){
		ReturnSet retSet = new ReturnSet();
		String query;

		ReturnSet VALID;
		VALID = shared.validateField(newEmail, shared.ValidateRule_USEREMAIL);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;

		
		newEmail = newEmail.trim();
		
		query = "UPDATE " + DB_METADATA.tbl_USER_name + 
					" SET " + 
						DB_METADATA.col_USER_email + " = '" + newEmail + "'" +
					" WHERE " +
						DB_METADATA.col_USER_name + " = '" + uname + "'";
		
		try {
			if (DB.ExecuteUpdate(query) == 0)
				throw new SQLException("No rows have been updated");
			uemail = newEmail;
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		return retSet;
	}

	protected ReturnSet updateGecos(String newGecos){
		ReturnSet retSet = new ReturnSet();
		String query;

		ReturnSet VALID;
		VALID = shared.validateField(newGecos, shared.ValidateRule_USERGECOS);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;

		
		newGecos = newGecos.trim();
		
		query = "UPDATE " + DB_METADATA.tbl_USER_name + 
					" SET " + 
						DB_METADATA.col_USER_gecos + " = '" + newGecos + "'" +
					" WHERE " +
						DB_METADATA.col_USER_name + " = '" + uname + "'";
		
		try {
			if (DB.ExecuteUpdate(query) == 0)
				throw new SQLException("No rows have been updated");
			ugecos = newGecos;
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		return retSet;
	}

	protected ReturnSet updateUserPassword(String newEncrPW){
		ReturnSet retSet = new ReturnSet();
		String query;
		
		ReturnSet VALID;
		VALID = shared.validateField(newEncrPW, shared.ValidateRule_USERPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;

		query = "UPDATE " + DB_METADATA.tbl_USER_name + 
					" SET " + 
						DB_METADATA.col_USER_password + " = '" + newEncrPW + "'" +
					" WHERE " +
						DB_METADATA.col_USER_name + " = '" + uname + "'";
		
		try {
			if (DB.ExecuteUpdate(query) == 0)
				throw new SQLException("No rows have been updated");
			upw_hash = newEncrPW;
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		return retSet;
	}

	protected ReturnSet updateROOTPassword(String newEncrPW){
		ReturnSet retSet = new ReturnSet();
		String query;
		
		ReturnSet VALID;
		VALID = shared.validateField(newEncrPW, shared.ValidateRule_ROOTPW);
		if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
		
		if(retSet.getCode() > 0) return retSet;

		query = "UPDATE " + DB_METADATA.tbl_USER_name + 
					" SET " + 
						DB_METADATA.col_USER_root_password + " = '" + newEncrPW + "'" +
					" WHERE " +
						DB_METADATA.col_USER_name + " = '" + uname + "'";
		
		try {
			if (DB.ExecuteUpdate(query) == 0)
				throw new SQLException("No rows have been updated");
			upw_hash_root = newEncrPW;
		} catch (SQLException e) {
			retSet.appendLn(e.getMessage(), 1);
		}
		return retSet;
	}

	
	protected Member findMemberByName(String name){
		Member match = null;
		
		//pullMembersList(); // reloading members from DB
		if ( name != null &&  ! name.trim().isEmpty() && members != null){
			for(Member M: members)
				if(M.mname.equals(name)){
					match=M;
					break;
				}
		}
		
		return match;
	}
	
	
	
	
	
	
	protected class Member {
		
		protected	String 	mname 		= "";
		protected	String 	mname_full	= "";
		protected	String	memail		= "";
		protected	String	mpw_hash	= "";
		protected	String	mgecos		= "";
		protected	String	NO_CATEGORY = shared.NO_CATEGORY;
		protected	Wallet[] wallets;
		protected 	String[] category 	= new String[]{NO_CATEGORY};
		private		QryBuilder 	qry 	= new QryBuilder();
		
		
		Member(){
			// no construction -- using this one just as a structure with some simple methods

			loadCategories();
			loadWallets();
		}
		Member(String name, String email, String gecos, String pw_hash){
			this.mname 		= name;
			this.mname_full	= uname + DB.delimiter + name;
			this.memail 	= email;
			this.mgecos 	= gecos;
			this.mpw_hash 	= pw_hash;
			
			//loadCategories();
			//loadWallets();
		}
		
		protected ReturnSet loadWallets(){
			wallets = null;
			String query;
			ReturnSet retSet = new ReturnSet();
			ResultSet results;
			int rowNo;
			
			query = qry.select("*").from(DB_METADATA.tbl_WALLET_name)
						.where().x_eq_y(DB_METADATA.col_MEMBER_name, mname_full)
					.pack();
			
			qry.flush();
			
			try {
				results = DB.ExecuteQuery(query);
				
				if(results.last()){
					wallets = new Wallet[results.getRow()];
					//System.out.println("wallets[]'s size = " + results.getRow());
					results.beforeFirst();
				}
				
				while(results.next()){
					rowNo = results.getRow()-1;
					//System.out.println("rowNo=" + rowNo);
					//wallets[rowNo] = new Wallet();
					wallets[rowNo] = new Wallet(
											results.getString(DB_METADATA.col_WALLET_name)
											);
				}
				
			} catch (SQLException | NumberFormatException e) { //INFO This is Java 7 is required
				retSet.appendLn(e.getMessage(), 1);
				Dialog dialogFailLoadWallets = new Dialog(Dialog.TYPE_ERR, "Nepavyko įkelti");
				dialogFailLoadWallets.setText("Nepavyko įkelti nario '"+uname+"' piniginių.\nKlaida:\n" + e.getMessage());
				dialogFailLoadWallets.setVisible(true);
			}
			
			return retSet;
		}
		
		protected ReturnSet addWallet(String newName){
			
			ReturnSet retSet = new ReturnSet();
			String query;
			
			ReturnSet VALID;
			VALID = shared.validateField(newName, shared.ValidateRule_WALLETNAME);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;

			
			query = qry.insert().into(DB_METADATA.tbl_WALLET_name).columns_and_values(
					new String[]{
						DB_METADATA.col_WALLET_name, 
						DB_METADATA.col_MEMBER_name
						}, 
					new String[]{
						mname_full + DB.delimiter + newName,
						mname_full
						}
					).pack();
			
			qry.flush();
			
			try {
				DB.ExecuteUpdate(query);
				//Wallet wall = new Wallet(mname_full + DB.delimiter + newName, "0", "0");
				//Object[] temparr = shared.addToArrayEnd(wallets, wall);
				//wallets = Arrays.copyOf(temparr, temparr.length, Wallet[].class);
				loadWallets();
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			
			return retSet;
		}
		
		protected ReturnSet delWallet(String name){
			ReturnSet retSet = new ReturnSet();
			String query, query_plans, query_flows;
			String delName = this.mname_full + DB.delimiter + name;
			
			try {
				
				
				query = qry.delete().from(DB_METADATA.tbl_WALLET_name)
							.where().x_eq_y(DB_METADATA.col_WALLET_name, delName)
						.pack();
				qry.flush();
				
				query_plans = qry.delete().from(DB_METADATA.tbl_PLAN_name)
						.where().x_eq_y(DB_METADATA.col_WALLET_name, delName)
						.pack();
				qry.flush();
			
				query_flows = qry.delete().from(DB_METADATA.tbl_FLOW_name)
						.where().x_eq_y(DB_METADATA.col_WALLET_name, delName)
						.pack();
				qry.flush();
				
				DB.ExecuteUpdate(query);
				DB.ExecuteUpdate(query_plans);
				DB.ExecuteUpdate(query_flows);
				
				for(Wallet W : wallets)
					if(W.wname.equals(name)){
						//Arrays.copyOf(temparr, temparr.length, Flow[].class); 
						Object[] tempArr = shared.removeFromArray(wallets, W);
						wallets = Arrays.copyOf(tempArr, tempArr.length, Wallet[].class);
						break;
					}
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			
			return retSet;
		}
		
		
		protected ReturnSet loadCategories(){
			ReturnSet retSet = new ReturnSet();
			ResultSet results;
			shared.Panel_center.dropdownCategories.removeAllItems();

			shared.Panel_center.dropdownCategories.addItem(NO_CATEGORY);
			
			String query = qry.select(DB_METADATA.col_CATEGORY_name).from(DB_METADATA.tbl_CATEG_name)
					.where()
						.x_eq_y(DB_METADATA.col_MEMBER_name, mname_full).pack();
			qry.flush();
			
			
			try {
				results = DB.ExecuteQuery(query);
				if(results.last()){
					category = new String[results.getRow()+1];
					results.beforeFirst();
				} else {
					category = new String[]{NO_CATEGORY};
					throw new SQLException("Kategorijų nerasta");
				}
				
				
				
				category[0] = NO_CATEGORY;
				while(results.next()){
					category[results.getRow()] = new String(results.getString(DB_METADATA.col_CATEGORY_name));
					shared.Panel_center.dropdownCategories.addItem(category[results.getRow()]);
				}
				
				
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
				category = new String[]{NO_CATEGORY};
			}
			shared.Panel_center.FFilterPanel.ddnCateg.removeAllItems();
			for(String C : category){
				//System.out.println("CATEG: " + C);
				shared.Panel_center.FFilterPanel.ddnCateg.addItem(C);
			}
			return retSet;
		}
		
		protected ReturnSet addCategory(String name){
			ReturnSet retSet = new ReturnSet();
			int updated_rows = 0;
			
			ReturnSet VALID;
			VALID = shared.validateField(name, shared.ValidateRule_CATEGORYNAME);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;

			
			if(shared.arrayContains(category, name)){
				retSet.appendLn("Tokia kategorija jau egzistuoja", 1);
				return retSet;
			}
			
			String query = qry.insert().into(DB_METADATA.tbl_CATEG_name)
							.columns_and_values(
									new String[]{
										DB_METADATA.col_CATEGORY_name,
										DB_METADATA.col_MEMBER_name
										}, 
									new String[]{
										name,
										mname_full
										}
									)
							.pack();
			qry.flush();
			
			try {
				updated_rows = DB.ExecuteUpdate(query);
				if (updated_rows == 0) retSet.appendLn("0 entries have been updated", 1);
				loadCategories();
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			
			
			return retSet;
		}
		
		protected ReturnSet delCategory(String name){
			ReturnSet retSet = new ReturnSet();
			int updated_rows = 0;
			
			String query = qry.delete().from(DB_METADATA.tbl_CATEG_name)
							.where()
								.x_eq_y(DB_METADATA.col_MEMBER_name, mname_full)
						.and()
							.x_eq_y(DB_METADATA.col_CATEGORY_name, name)
						.pack();
			qry.flush();
			
			String query_POST = qry.update(DB_METADATA.tbl_FLOW_name).set().x_eq_y(
							DB_METADATA.col_CATEGORY_name, NO_CATEGORY)
					.where()
							.x_eq_y(DB_METADATA.col_MEMBER_name, mname_full)
						.and()
							.x_eq_y(DB_METADATA.col_CATEGORY_name, name)
					.pack();
			qry.flush();
			
			try {
				updated_rows += DB.ExecuteUpdate(query);
				if (updated_rows == 0) retSet.appendLn("0 entries have been updated", 1);
				updated_rows += DB.ExecuteUpdate(query_POST);
				if (updated_rows == 0) retSet.appendLn("0 entries have been updated", 1);
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			
			loadCategories();
			
			return retSet;
		}
		
		
		
		//@Deprecated
		protected ReturnSet updateName(String newName){
			
			ReturnSet retSet = new ReturnSet();
			String query;
			
			ReturnSet VALID;
			VALID = shared.validateField(newName, shared.ValidateRule_MEMBERNAME);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;

			
			newName = newName.trim();
			
			
			query = qry.update(DB_METADATA.tbl_MEMBER_name)
						.set()
							.x_eq_y(DB_METADATA.col_MEMBER_name, uname + DB.delimiter + newName)
						.where()
							.x_eq_y(DB_METADATA.col_MEMBER_name, mname_full)
						.pack();
			
			
			qry.flush();
			
			try {
				if (DB.ExecuteUpdate(query) == 0)
					throw new SQLException("No rows have been updated");
				mname = newName;
				mname_full = uname + DB.delimiter + newName;
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			return retSet;
		}
		
		protected ReturnSet updateEmail(String newEmail){
			ReturnSet retSet = new ReturnSet();
			String query;
			newEmail = newEmail.trim();
			
			ReturnSet VALID;
			VALID = shared.validateField(newEmail, shared.ValidateRule_MEMBEREMAIL);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;

			query = qry.update(DB_METADATA.tbl_MEMBER_name)
					.set()
						.x_eq_y(DB_METADATA.col_MEMBER_email, newEmail)
					.where()
						.x_eq_y(DB_METADATA.col_MEMBER_name, mname_full)
					.pack();
		
		
			qry.flush();
			
			
			try {
				if (DB.ExecuteUpdate(query) == 0)
					throw new SQLException("No rows have been updated");
				memail = newEmail;
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			return retSet;
		}
		
		protected ReturnSet updateGecos(String newGecos){
			ReturnSet retSet = new ReturnSet();
			String query;
			newGecos = newGecos.trim();
			
			ReturnSet VALID;
			VALID = shared.validateField(newGecos, shared.ValidateRule_MEMBERGECOS);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;

						
			query = qry.update(DB_METADATA.tbl_MEMBER_name)
					.set()
						.x_eq_y(DB_METADATA.col_MEMBER_gecos, newGecos)
					.where()
						.x_eq_y(DB_METADATA.col_MEMBER_name, mname_full)
					.pack();
		
		
			qry.flush();
			
			try {
				if (DB.ExecuteUpdate(query) == 0)
					throw new SQLException("No rows have been updated");
				mgecos = newGecos;
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			return retSet;
		}
		
		protected ReturnSet updatePassword(String newEncrPW){
			ReturnSet retSet = new ReturnSet();
			String query;
						
			ReturnSet VALID;
			VALID = shared.validateField(newEncrPW, shared.ValidateRule_MEMBERPW);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;

			query = qry.update(DB_METADATA.tbl_MEMBER_name)
					.set()
						.x_eq_y(DB_METADATA.col_MEMBER_password, newEncrPW)
					.where()
						.x_eq_y(DB_METADATA.col_MEMBER_name, mname_full)
					.pack();
		
		
			qry.flush();
			
			try {
				if (DB.ExecuteUpdate(query) == 0)
					throw new SQLException("No rows have been updated");
				mpw_hash = newEncrPW;
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			return retSet;
		}
		
		
	}
	
	
	protected class Wallet {
		/* This class and it's sub-classes act as structures only. Too bad JAVA doesn't have those lightweight tools... */
		 
		QryBuilder qry = new QryBuilder();
		
		protected static final String FILTER_NONE	 	= "0"; 	// 0000 0000
		protected static final String FILTER_BEFORE 	= "1"; 	// 0000 0001
		protected static final String FILTER_AFTER 		= "2"; 	// 0000 0010
		protected static final String FILTER_SIGN_BIT 	= "3"; 	// 0000 0100
		protected static final String FILTER_LESS_THAN	= "4"; 	// 0000 1000
		protected static final String FILTER_MORE_THAN	= "5"; 	// 0001 0000
		protected static final String FILTER_CATEGORY	= "6"; 	// 0010 0000
		protected static final String FILTER_PLAN		= "7"; 	// 0100 0000
		
		
		/**
		 * All expenses and incomes 
		 */
		protected static final String PLAN_TYPE_ALL	= "1";
		
		/**
		 * Only assigned expenses and incomes 
		 */
		protected static final String PLAN_TYPE_OLY	= "2";
		
		
		protected String 	wname_full 	= "";
			protected String  wname		= "";
			private String 	  uname 	= "";
			private String 	  mname 	= "";
		
		protected double 	wamount	 = 0;
		
		protected String[]	category;
		protected Plan[] 	plans;
		protected Flow[]	flows;
		
		protected String 	NO_PLAN;
		
		//Wallet(){}
		Wallet(String name_full) throws NumberFormatException{
			this.wname_full = name_full.trim();
			
			
			this.uname = name_full.split(DB.delimiter_rgx)[0].trim(); // USERNAME<==>MEMBERNAME<==>WALLETNAME.split('<==>')[0]
			this.mname = name_full.split(DB.delimiter_rgx)[1].trim(); // USERNAME<==>MEMBERNAME<==>WALLETNAME.split('<==>')[1]
			this.wname  = name_full.split(DB.delimiter_rgx)[2].trim(); // USERNAME<==>MEMBERNAME<==>WALLETNAME.split('<==>')[2]
			
			NO_PLAN = "Neplanuota"+" ["+wname+"]";
			
			wamount = countWalletFlowsAmount(false) - countWalletFlowsAmount(true);
			loadWalletPlans();
		}
		
		

		// Absolutely inefficient to load all... but this application is meant for educational purposes only so...  screw that.
		protected ReturnSet loadWalletPlans(){
			String query;
			ResultSet result;
			int rowNo;
			ReturnSet retSet = new ReturnSet();

			query = qry.select("*").from(DB_METADATA.tbl_PLAN_name)
						.where().x_eq_y(DB_METADATA.col_WALLET_name, this.wname_full)
					.pack();
			
			qry.flush();
			//System.out.println(query);
			try {
				result = DB.ExecuteQuery(query);
				
				if(result.last()){
					plans = new Plan[result.getRow()+1];
					plans[0] = makeEmptyPlan();
					result.beforeFirst();
				} else plans = new Plan[]{makeEmptyPlan()};
				
				
				
				
				while(result.next()){
					rowNo = result.getRow();
					plans[rowNo] = new Plan(result.getString(DB_METADATA.col_PLAN_name).split(DB.delimiter_rgx)[2]);

					plans[rowNo].comment 	= result.getString(DB_METADATA.col_PLAN_comment);
					//plans[rowNo].mname 		= result.getString(DB_METADATA.col_MEMBER_name);
					plans[rowNo].wname 		= result.getString(DB_METADATA.col_WALLET_name);
					plans[rowNo].type	 	= result.getString(DB_METADATA.col_PLAN_type);
					plans[rowNo].start 		= result.getString(DB_METADATA.col_PLAN_start_date);
					plans[rowNo].end		= result.getString(DB_METADATA.col_PLAN_end_date);
					
					plans[rowNo].est_in	 	= result.getString(DB_METADATA.col_PLAN_estim_inflow);
					plans[rowNo].est_out 	= result.getString(DB_METADATA.col_PLAN_estim_outflow);
					plans[rowNo].est_bal 	= result.getString(DB_METADATA.col_PLAN_estim_balance);
				}
				
			} catch (SQLException e) {
				plans = new Plan[]{makeEmptyPlan()};
				retSet.appendLn(e.getMessage(), 1);
			}
			
			return retSet;
		}

		protected ReturnSet addWalletPlan(String planName, String planComment, String planType, String planStart, String planEnd, String est_in, String est_out, String est_bal){
			ReturnSet retSet = new ReturnSet();
			String query;
			
			ReturnSet VALID;
			VALID = shared.validateField(planName, shared.ValidateRule_PLANNAME);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			VALID = shared.validateField(planStart, shared.ValidateRule_DATE);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			VALID = shared.validateField(planEnd, shared.ValidateRule_DATE);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			VALID = shared.validateField(est_in, shared.ValidateRule_AMOUNT);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			VALID = shared.validateField(est_out, shared.ValidateRule_AMOUNT);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			VALID = shared.validateField(est_bal, shared.ValidateRule_AMOUNT);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;

			
			query = qry.insert().into(DB_METADATA.tbl_PLAN_name)
						.columns_and_values(
							new String[]{
								DB_METADATA.col_PLAN_name,
								DB_METADATA.col_WALLET_name,
								//DB_METADATA.col_MEMBER_name,
								DB_METADATA.col_PLAN_type,
								DB_METADATA.col_PLAN_comment,
								DB_METADATA.col_PLAN_start_date,
								DB_METADATA.col_PLAN_end_date,
								DB_METADATA.col_PLAN_estim_balance,
								DB_METADATA.col_PLAN_estim_inflow,
								DB_METADATA.col_PLAN_estim_outflow,
								},
							new String[]{
								this.uname + DB.delimiter + this.mname + DB.delimiter + planName,
								wname_full,
								//uname + DB.delimiter + this.mname,
								planType,
								planComment,
								planStart,
								planEnd,
								est_bal ==null?"":est_bal,
								est_in  ==null?"":est_in,
								est_out ==null?"":est_out,
								}
						)
						.pack();
			System.out.println(query);
			qry.flush();
			
			try {
				
				// pushing to database
				DB.ExecuteUpdate(query);
				
				// appending to local array
				Plan[] newPlans = new Plan[plans!=null?plans.length:0 + 1]; // extending temp array
				
				loadWalletPlans();
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			
			return retSet;
		}
		
		protected ReturnSet __delWalletPlan(String planName){
			ReturnSet retSet = new ReturnSet();
			String query;
			
			query = qry.delete().from(DB_METADATA.tbl_PLAN_name)
						.where().x_eq_y(DB_METADATA.col_PLAN_name, uname + DB.delimiter + mname + DB.delimiter + planName)
					.pack();
			qry.flush();
			
			try {
				DB.ExecuteUpdate(query);
				for(int i=0; i<plans.length; i++)
					if(plans[i].name.equals(planName)){
						plans[i] = null; // unlinking deleted plan
						int newLength = plans.length-1; 
						int offset = 0;
						Plan[] newPlans = new Plan[newLength]; // creating temporary Plan array smaller by 1 element
						for(int j=0; j<plans.length; j++)	// copying non-null elements to temp array
							if(plans[j] == null)
								offset+=1;	// let's not leave a gap where null is located
							else
								newPlans[j-offset] = plans[j];
						plans = newPlans;
						newPlans = null; // unlinking temporary array - feed it to the GC
						break;
					}
						
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			
			return retSet;
		}
		
		private Plan makeEmptyPlan(){
			Plan P = new Plan(NO_PLAN);
			DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
			
			
			P.comment 	= "Nepriskirta jokiam planui";
			P.mname 	= mname;
			P.wname 	= wname;
			P.type	 	= PLAN_TYPE_OLY;
			//P.start 	= dateFormatter.format(new Date());
			P.start 	= "-";
			P.end		= P.start;
			P.est_in	= "0";
			P.est_out 	= "0";
			P.est_bal 	= "0";
			
			return P;
		}
		
		//@Deprecated
		private ReturnSet loadWalletFlows(String query){
			/*Loading ALL the flows is insane... there could be thousands or millions of them. Better use filters*/
			ReturnSet retSet = new ReturnSet();
			String catName;
	
			ResultSet result;
			int rowNo;
			
			try {
				flows = null; // removing previous results if any
				result = DB.ExecuteQuery(query);
				
				if(result.last()){
					flows = new Flow[result.getRow()];
					result.beforeFirst();
				}
				
				while(result.next()){
					rowNo = result.getRow()-1;
					//System.out.println("PlanName is '" + result.getString(DB_METADATA.col_PLAN_name)+"'");
					String planName = result.getString(DB_METADATA.col_PLAN_name);
					
					if(planName == null || planName.equals("NULL"))planName = NO_PLAN;
					else planName = planName.split(DB.delimiter_rgx)[2];
					
					catName = result.getString(DB_METADATA.col_CATEGORY_name);
					
					flows[rowNo] = new Flow(
											result.getString(DB_METADATA.col_FLOW_date),
											result.getString(DB_METADATA.col_FLOW_amount),
											result.getString(DB_METADATA.col_FLOW_comment),
											catName.equals("NULL")?shared.NO_CATEGORY:catName,
											//result.getString(DB_METADATA.col_PLAN_name).split(DB.delimiter_rgx)[2]
											planName
										);
					flows[rowNo].negative = (result.getString(DB_METADATA.col_FLOW_negative).equals("1"));
					
				}
				
			} catch (SQLException | NumberFormatException e) {
				
				retSet.appendLn(e.getMessage(), 1);
			}
			
			return retSet;
		}

		protected ReturnSet loadWalletFlows(String[][] FilterSet){
			ReturnSet retSet = new ReturnSet();
			String query = "";
			String checking_filter_;
			boolean needs_and   = true;
			
			qry.select("*").from(DB_METADATA.tbl_FLOW_name).where().x_eq_y(DB_METADATA.col_WALLET_name, wname_full);
			
			checking_filter_ = FILTER_NONE;
			if(FilterSet == null || FilterSet[0][0] == checking_filter_){
				query = qry.pack();
				qry.flush();
				loadWalletFlows(query);
				return retSet;
			}
			// ELSE
			try{
				
				for(String[] FSet : FilterSet){
				
					if(FSet[1] == null ) 
						continue;
					
					
					
					checking_filter_ = FILTER_BEFORE;
					if(FSet[0] == checking_filter_){
						if(needs_and) qry.and();
						qry.x_le_y(DB_METADATA.col_FLOW_date, FSet[1]);
						needs_and = true;
						
					}
					
					checking_filter_ = FILTER_AFTER;
					if(FSet[0] == checking_filter_){
						if(needs_and) qry.and();
						qry.x_ge_y(DB_METADATA.col_FLOW_date, FSet[1]);
						needs_and = true;
					}
					
					checking_filter_ = FILTER_SIGN_BIT;
					if(FSet[0] == checking_filter_){
						if(needs_and) qry.and();
						qry.x_eq_y(DB_METADATA.col_FLOW_negative, FSet[1]);
						needs_and = true;
					}
					
					checking_filter_ = FILTER_LESS_THAN;
					if(FSet[0] == checking_filter_){
						if(needs_and) qry.and();
						qry.x_le_y(DB_METADATA.col_FLOW_amount, FSet[1]);
						needs_and = true;
					}
					
					checking_filter_ = FILTER_MORE_THAN;
					if(FSet[0] == checking_filter_){
						if(needs_and) qry.and();
						qry.x_ge_y(DB_METADATA.col_FLOW_amount, FSet[1]);
						needs_and = true;
					}
					
					checking_filter_ = FILTER_CATEGORY;
					if(FSet[0] == checking_filter_){
						if(needs_and) qry.and();
						qry.x_eq_y(DB_METADATA.col_CATEGORY_name, FSet[1] == shared.NO_CATEGORY?"NULL":FSet[1]);
						needs_and = true;
					}
					
					checking_filter_ = FILTER_PLAN;
					if(FSet[0] == checking_filter_){
						if(needs_and) qry.and();
						qry.x_eq_y(DB_METADATA.col_PLAN_name, FSet[1]);	
						needs_and = true;
					}
				}
				
				query = qry.pack();
				qry.flush();
				System.out.println(query);
				retSet = loadWalletFlows(query);
			} catch(ArrayIndexOutOfBoundsException e){ // likely to happen if user forgets to add something to filterValues
				retSet.appendLn(e.getMessage(), 1);
			}
			
			
			return retSet;
		}
		
		protected ReturnSet addWalletFlow(String date, String amount, String comment, String category, String planName){
			ReturnSet retSet = new ReturnSet();
			String query, negative;
			
			ReturnSet VALID;
			VALID = shared.validateField(date, shared.ValidateRule_DATE);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			VALID = shared.validateField(amount, shared.ValidateRule_AMOUNT);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			VALID = shared.validateField(comment, shared.ValidateRule_FLOWCOMMENT);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;
	
			try {
				
				
				negative = amount.trim().charAt(0)=='-'?"1":"0";
				amount = amount.replace("-", "");
				
				query = qry.insert().into(DB_METADATA.tbl_FLOW_name)
							.columns_and_values(
								new String[]{
									DB_METADATA.col_MEMBER_name,
									DB_METADATA.col_FLOW_date,
									DB_METADATA.col_FLOW_amount,
									DB_METADATA.col_FLOW_negative,
									DB_METADATA.col_CATEGORY_name,
									DB_METADATA.col_FLOW_comment,
									DB_METADATA.col_WALLET_name,
									DB_METADATA.col_PLAN_name
								},
								new String[]{
									uname + DB.delimiter + mname,
									date.trim(),
									amount.trim(),
									negative.trim(),
									category.equals(shared.NO_CATEGORY)?"NULL":category.trim(),
									comment.trim(),
									this.wname_full,
									planName.equals(NO_PLAN)?"NULL":uname + DB.delimiter + this.mname + DB.delimiter + planName
								}
							)
						.pack();
				qry.flush();
				System.out.println(query);
				// pushing to database
				DB.ExecuteUpdate(query);
				// saving temp array
				Object[] temparr = shared.addToArrayEnd(flows, new Flow(date, amount, comment, category, planName));
				flows = Arrays.copyOf(temparr, temparr.length, Flow[].class); 
				
				
			} catch (SQLException | NumberFormatException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			
			return retSet;
		}

		protected ReturnSet __delWalletFlow(String date, String amount, String comment, String category){
			ReturnSet retSet = new ReturnSet();
			String query, negative;
			
			try {
				
				negative = amount.trim().charAt(0)=='-'?"1":"0";
				
				query = qry.delete().from(DB_METADATA.tbl_FLOW_name)
							.where()
									  .x_eq_y(DB_METADATA.col_FLOW_date		, date.trim()		)
								.and().x_eq_y(DB_METADATA.col_FLOW_amount	, amount.trim()		)
								.and().x_eq_y(DB_METADATA.col_FLOW_negative	, negative.trim()	)
								.and().x_eq_y(DB_METADATA.col_CATEGORY_name	, category.trim()	)
								.and().x_eq_y(DB_METADATA.col_FLOW_comment	, comment.trim()	)
								.and().x_eq_y(DB_METADATA.col_WALLET_name	, this.wname_full	)
								.and().x_eq_y(DB_METADATA.col_MEMBER_name	, this.mname		)
						.pack();
				qry.flush();
				
				DB.ExecuteUpdate(query);
				
				for(int i=0; i<flows.length; i++)
					if(
							flows[i].comment.equals(comment) &&
							flows[i].date.equals(date) &&
							flows[i].amount.equals(amount)
						){
						flows = shared.removeFromArray(flows, flows[i]);
						}
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			
			return retSet;
		}
		
		
		protected ReturnSet __updateName(String newName){
			ReturnSet retSet = new ReturnSet();
			String query;
			
			ReturnSet VALID;
			VALID = shared.validateField(newName, shared.ValidateRule_WALLETNAME);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;
	
			newName = newName.trim();
			
			query = qry.update(DB_METADATA.tbl_WALLET_name)
						.set()	.x_eq_y(DB_METADATA.col_WALLET_name, uname + DB.delimiter + mname + DB.delimiter + newName)
						.where().x_eq_y(DB_METADATA.col_WALLET_name, wname_full)
					.pack();
			
			qry.flush();
			
			try {
				if (DB.ExecuteUpdate(query) == 0)
					throw new SQLException("No rows have been updated");
				wname = newName;
				wname_full = uname + DB.delimiter + mname + DB.delimiter + newName;
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			return retSet;
		}
		
		protected ReturnSet __updateOwner(String newOwner){
			ReturnSet retSet = new ReturnSet();
			String query;
			
			ReturnSet VALID;
			VALID = shared.validateField(newOwner, shared.ValidateRule_MEMBERNAME);
			if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
			
			if(retSet.getCode() > 0) return retSet;
	
			
			newOwner = newOwner.trim();
			
			query = qry.update(DB_METADATA.tbl_WALLET_name)
					.set()	.x_eq_y(DB_METADATA.col_WALLET_name, uname + DB.delimiter + newOwner + DB.delimiter + wname)
							.x_eq_y(DB_METADATA.col_MEMBER_name, newOwner)
					.where().x_eq_y(DB_METADATA.col_WALLET_name, wname_full)
				.pack();
		
			qry.flush();
			
			try {
				if (DB.ExecuteUpdate(query) == 0)
					throw new SQLException("No rows have been updated");
				mname = newOwner;
				wname_full = uname + DB.delimiter + mname + DB.delimiter + wname;
				
			} catch (SQLException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			return retSet;
		}
		
		@Deprecated
		protected ReturnSet __updateBalance(String newAmount){
			ReturnSet retSet = new ReturnSet();
			String query;
			newAmount = newAmount.trim();

			query = qry.update(DB_METADATA.tbl_WALLET_name)
					.set()	.x_eq_y(DB_METADATA.col_WALLET_amount, newAmount)
					.where().x_eq_y(DB_METADATA.col_WALLET_name, wname_full)
				.pack();
		
			qry.flush();
			
			try {
				if (DB.ExecuteUpdate(query) == 0)
					throw new SQLException("No rows have been updated");
				wamount = Double.parseDouble(newAmount);				
			} catch (SQLException | NumberFormatException e) {
				retSet.appendLn(e.getMessage(), 1);
			}
			return retSet;
		}
		
		
		
		

		private long countWalletFlowsAmount(boolean negative){
			long amount = 0;
			ResultSet result;
			String query;
			QryBuilder qry = new QryBuilder();
			
			qry.select(DB_METADATA.col_FLOW_amount).from(DB_METADATA.tbl_FLOW_name)
				.where()
					.x_eq_y(DB_METADATA.col_WALLET_name, wname_full)
					.and().x_eq_y(DB_METADATA.col_FLOW_negative, negative?"1":"0");
			
			query = qry.pack();
			qry.flush();
			
			try {
				result = shared.user.DB.ExecuteQuery(query);
				
				while(result.next()){
					amount+=result.getDouble(DB_METADATA.col_FLOW_amount);
				}
			} catch (SQLException e) {
				System.out.println("SQL err: "+e.getMessage());
			}
			
			return amount;
		}
		
		
	}
		
	

	protected class Flow{
		protected String 	date 		= "0";
		protected String 	amount 		= "0";
		protected String	category 	= "";
		protected String 	comment 	= "";
		protected boolean	negative	= false;
		
		protected String	plan		= "";
		
		Flow(){}
		Flow(String date, String amount, String comment, String category, String plan){
			this.date 		= date;
			this.amount 	= amount;
			this.comment 	= comment;
			this.category	= category;
			this.plan		= plan;
		}
		
		
	}

	protected class Plan{
		protected String	name 	= ""; // *
		protected String	comment	= ""; // *
		protected String	type	= ""; // *
		protected String	mname	= ""; // *
		protected String	wname	= ""; // *
		protected String	start	= ""; // *
		protected String	end		= ""; // *
		protected String	est_in	= ""; // 
		protected String	est_out	= ""; // 
		protected String	est_bal	= ""; // 
		
		//Plan(){}
		Plan(String name){
			this.name 		= name;
			//this.comment	= comment;
		}
		
		
	}
	
	
	
	/**
	 * Class stores all the information related to the database<br>
	 * as well as DB-related methods<br>
	 * 
	 * @USAGE
	 *     1) create the object<br>
	 *     2) call object.init() to load drivers (catch exceptions)<br>
	 *     3) make sure object.{DBhost, DBname, DBuser, DBpswd} are correct<br>
	 *     4) call object.connect() to connect to the DB (catch exceptions)<br>
	 *     5) call object.ExecuteQuery(String query) or object.ExecuteUpdate(String query)<br>
	 * 
	 */
	protected class DATABASE {
		
		/**
		 * Object stores tables' and columns' names.<br>
		 * @EXAMPLE
		 *     NAMES.col_USER_email<br>
		 *     NAMES.tbl_PLAN_name
		 */
		private   Connection 	con;
		
		/**
		 * Delimiter used for some columns to separate different parts of column value.<br><br>
		 * Use <b>(String) delimiter_rgx</b>  instead when you need delimiter for String.split() - it has<br>
		 * escaped metachars
		 */
		protected final String		delimiter 		= "|||||";
		protected final String		delimiter_rgx 	= "\\|\\|\\|\\|\\|";
		
		protected String	DBhost  = "localhost";
		protected String	DBport  = "13306";
		//protected String	DBname  = "FINANSPLAN";
		protected String	DBname  = "FPLANNER";
		protected String	DBuser  = "fplanuser";
		protected String	DBpswd  = "SomePass";
		
		protected String	DBurl;
		
		protected final class DB_METADATA {
			

			// TABLES
			protected static final String	tbl_USER_name	= "TB_USERS";
			protected static final String	tbl_MEMBER_name	= "TB_MEMBERS"; 
			protected static final String	tbl_FLOW_name	= "TB_FLOW";
			protected static final String	tbl_PLAN_name	= "TB_PLAN";
			protected static final String	tbl_WALLET_name	= "TB_WALLET";
			protected static final String	tbl_CATEG_name	= "TB_CATEGORY";
			
			// COLUMNS
			protected static final String	col_USER_name			= "COL_UNAME";
			protected static final String	col_USER_password		= "COL_U_PSWD";
			protected static final String	col_USER_root_password	= "COL_U_ROOT_PSWD";
			protected static final String	col_USER_email			= "COL_U_EMAIL";
			protected static final String	col_USER_gecos			= "COL_U_GECOS";

			protected static final String	col_MEMBER_name			= "COL_MNAME";
			protected static final String	col_MEMBER_password		= "COL_M_PSWD";
			protected static final String	col_MEMBER_email		= "COL_M_EMAIL";
			protected static final String	col_MEMBER_gecos		= "COL_M_GECOS";

			protected static final String	col_FLOW_date			= "COL_FL_DATE";
			protected static final String	col_FLOW_amount			= "COL_FL_AMOUNT";
			protected static final String	col_FLOW_negative		= "COL_FL_NEGATIVE";
			protected static final String	col_FLOW_comment		= "COL_FL_COMMENT";


			protected static final String	col_PLAN_name			= "COL_PLNAME";
			protected static final String	col_PLAN_comment		= "COL_PL_COMMENT";
			protected static final String	col_PLAN_type			= "COL_PL_TYPE";
			protected static final String	col_PLAN_start_date		= "COL_PL_START";
			protected static final String	col_PLAN_end_date		= "COL_PL_END";
			protected static final String	col_PLAN_estim_inflow	= "COL_PL_EST_IN";
			protected static final String	col_PLAN_estim_outflow	= "COL_PL_EST_OUT";
			protected static final String	col_PLAN_estim_balance	= "COL_PL_EST_BAL";

			protected static final String	col_WALLET_name			= "COL_WNAME";
			//protected static final String	col_WALLET_bankAccount	= "COL_BANK_ACC";
			protected static final String	col_WALLET_visibility	= "COL_W_VISIBILITY";
			protected static final String	col_WALLET_amount		= "COL_W_AMOUNT";

			protected static final String	col_CATEGORY_name		= "COL_CATNAME";
				
			
			
			
			// COLUMNS
			protected static final int	colSize_USER_name			= 20;	// 20 symbols MAX for username
			protected static final int	colSize_USER_password		= 32;	// suppose it's an md5 checksum
			protected static final int	colSize_USER_root_password	= 32;	// same as above
			protected static final int	colSize_USER_email			= 50;	// let's say it's a long email
			protected static final int	colSize_USER_gecos			= 255;	// more info about the user - let the words flow
                                                                      
			protected static final int	colSize_MEMBER_name			= 45;	// username(20) + delimiter(5) + membername(20)
			protected static final int	colSize_MEMBER_password		= 32;	// like a md5 checksum
			protected static final int	colSize_MEMBER_email		= 50;	// leave some room for long emails
			protected static final int	colSize_MEMBER_gecos		= 255;	// member information
                                                                      
			protected static final int	colSize_FLOW_date			= 14;	// YYYYMMDD(8) + hhmmss(6)
			protected static final int	colSize_FLOW_amount			= 17;	// max: 99'999'999'999'999.99 - hoping we won't have richer users :)
			protected static final int	colSize_FLOW_negative		= 1;	// sign bit: 1 - negative amount of money (outflow); 0 - positive (inflow) 
			protected static final int	colSize_FLOW_comment		= 255;	// let the words flow!
                                                                      
                                                                      
			protected static final int	colSize_PLAN_name			= 64;	// memberName(45) + delimiter(5) + planName(14)
			protected static final int	colSize_PLAN_comment		= 255;	// blah blah...
			protected static final int	colSize_PLAN_type			= 5;	// up to 5 numbers-flags. If more needed - use bitflags 
			protected static final int	colSize_PLAN_start_date		= 14;	// Start date of the plan: YYYYMMDD(8) + hhmmss(6) 
			protected static final int	colSize_PLAN_end_date		= 14;	// End   date of the plan: YYYYMMDD(8) + hhmmss(6)
			protected static final int	colSize_PLAN_estim_inflow	= 17;	// max. 99'999'999'999'999.99
			protected static final int	colSize_PLAN_estim_outflow	= 17;	// ––//––
			protected static final int	colSize_PLAN_estim_balance	= 17;	// ––//––
                                                                      
			protected static final int	colSize_WALLET_name			= 70;	// memberName(45) + delimiter(5) + walletName(20)
			//protected static final int	colSize_WALLET_bankAccount	= 20;	// e.g. LT01 2222 3333 4444 5555
			protected static final int	colSize_WALLET_visibility	= 1;	// bitflag - 1 digit = 1 byte = 4 bits = 4 flags = 24 variations. That should be enough :)
			protected static final int	colSize_WALLET_amount		= 17;	// max: 99'999'999'999'999.99
                                                                      
			protected static final int	colSize_CATEGORY_name		= 70;	// memberName(45) + delimiter(5) + categoryName(20)
				
			
		}
		
		
		
		
		DATABASE(){ // constructor
			
		}
		
		/**
		 * Method loads the database driver
		 * @throws
		 * 		java.lang.ClassNotFoundException
		 */
		protected void init() throws ClassNotFoundException {
			Class.forName("com.mysql.jdbc.Driver");
		}
		
		
		/**
		 * Method connects to the database using .{DBhost, DBname, DBuser, DBpswd} variables.<br>
		 * Before calling this method make sure these are correct.
		 * @throws
		 * 		java.sql.SQLException
		 */
		protected void connect() throws SQLException{
			DBurl	= 	"jdbc"	+ ":" +
						"mysql"+ ":" +
						"//localhost/FPLANNER";
						//"//localhost/FPLANNER?"+
						//"user=" + DBuser +
						//"&password=" + DBpswd;
			con = DriverManager.getConnection(DBurl, DBuser, DBpswd);
			
		}
		
		/**
		 * Method executes a query that is expected to return some data from DB.<br>
		 * @param query (String) - a completely assembled query (no need for trailing semicolon)
		 * @return (java.sql.Statement) statement 
		 * @throws java.sql.SQLException
		 */
		protected ResultSet ExecuteQuery(String query) throws SQLException{
			Statement statement = con.createStatement(); 
			return  statement.executeQuery(query);
		}
		
		
		/**
		 * Method executes a query that is not expected to return any data from<br>
		 * database - just some metainfo, e.g. number of lines updated, etc.
		 * @param query (String) - a completely assembled query (no need for trailing semicolon)
		 * @return (int) metainfo
		 * @throws java.sql.SQLException
		 */
		protected int ExecuteUpdate(String query) throws SQLException{
			Statement statement = con.createStatement();
			return statement.executeUpdate(query);
		}
		
		
		
	}
	
	
}



