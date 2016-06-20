package com.planner.netikras;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.planner.netikras.UserClass.Member;
import com.planner.netikras.UserClass.Wallet;
import com.planner.netikras.UserClass.DATABASE.DB_METADATA;
import com.planner.netikras.WindowAbout;
import com.planner.netikras.SharedClass.*;


public class Listeners implements ActionListener {

	protected SharedClass shared;

	
	public Listeners() {
		// constructor
	}
	
	public Listeners(SharedClass data) {
		// constructor
		shared = data;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		
		switch (command) {
			case "exit":
				System.out.println("exiting");
				System.exit(0);
				break;
				
			case "about":
				System.out.println("about");
				new WindowAbout(shared); // anonymous object - it will manage to survive itself
				break;
				
			case "DropDown":
				System.out.println("DropDown");
				@SuppressWarnings("unchecked")
				JComboBox<String> ddn = ((JComboBox<String>)e.getSource());
				
				switch(ddn.getName()){
					case "MemberList":
						//if(ddn.getItemCount() != 0){
						//	String membername = ddn.getSelectedItem().toString();
						//	System.out.println("membername=" + ddn.getSelectedItem());
						//	shared.user.loginMember(membername);
						//	System.out.println("Switching member to: " + shared.currentMemb);
						//}
						break;
					case "ddnSettMemb":
						
						if(ddn.getItemCount() != 0 && shared.user.members != null){
							Member M = shared.user.findMemberByName(ddn.getSelectedItem().toString());
							if(M != null){
								shared.Panel_center.tfMemberName.setText(M.mname);
								shared.Panel_center.tfMemberEmail.setText(M.memail);
								shared.Panel_center.tfMemberGecos.setText(M.mgecos);
								break;
							}
						}
						
						break;
					}
				
				break;
				
			case "closeFrame":
				if(shared.ActionObject != null){
					((JFrame)shared.ActionObject).dispose();
					System.out.println("Closing window: " + ((JFrame)shared.ActionObject).getName());
					shared.ActionObject = null;
					
				} else {
					System.out.println("Closing window: unnamed");
				}
				break;
				
			case "ButtonClick":
				//System.out.println("Button click");
				//ReturnSet validated = new ReturnSet();
				JButton button = (JButton)e.getSource();
				
				String bName = button.getName();
				
				switch (bName){
					case "LoginButton":
						System.out.println("LOGIN");
						//shared.Panel_center.view_login_screen();
						Dialog loginDialog = new Dialog(Dialog.TYPE_LOGIN, "Prisijunkite");
						loginDialog.setCallback(new CALLBACK() {
										public void function() 				{}
										public void function(String retVal) {}
			
										@Override
										public void function(String[] retVal) {
											String username, password, password_encr;
											ReturnSet retSet = new ReturnSet();
											username = retVal[0];
											password = retVal[1];
											
											System.out.println("Attempting to login with username: " + username);
											
											password_encr = shared.encryptPW(password);
			
											retSet = shared.user.login(username, password_encr);
											if (retSet.getCode() == 0){
												shared.Panel_bottom.setText("Logged in as: " + username + ". ");
												shared.Panel_center.view_main();
												shared.Panel_top.enableButtons();
												shared.Panel_bottom.append("Please choose what to do.");
											} else {
												System.out.println("FAILED to login");
												Dialog dialogFailed = new Dialog(Dialog.TYPE_ERR, "Nepavyko!");
												dialogFailed.setText("Nepavyko prisijungti.\n\nKlaida: \n" + retSet.getMessage());
												dialogFailed.setCallback(new CALLBACK(){
																public void function(String[] retVal)	{};
																public void function(String retVal) 	{};
																@Override
																public void function() {
																	System.out.println("Acknowledged");
																};
															});
												dialogFailed.setVisible(true);
											}
										}
			
									});
						loginDialog.setVisible(true);
						
						break;
						
					case "LogoutButton":
						System.out.println("LOGOUT");
						shared.user.logout();
						//shared.Panel_center.view_login_screen();
						shared.loggedOutActions();
						break;
						
					case "NewUser":
						System.out.println("NewUser");
						shared.Panel_center.view_new_user();
						break;
						
					case "SubmitNewUser":
						
						System.out.println("SubmitNewUser");
						String username_new, password_new, passwordROOT_new, email_new, geckos_new;
						
						username_new 	= shared.Panel_center.tfUsername.getText();
						password_new 	= new String(shared.Panel_center.tfPassword.getPassword());
						passwordROOT_new=new String(shared.Panel_center.tfPasswordROOT.getPassword());
						email_new 		= shared.Panel_center.tfEmail.getText();
						geckos_new 		= shared.Panel_center.tfGeckos.getText();
						//System.out.println(password_new);
						shared.Panel_center.tfPassword.setText("");
						shared.Panel_center.tfPasswordROOT.setText("");
						
						try {
							ReturnSet retSetSubmitUser = shared.user.addNewUser(
																username_new, 
																shared.encryptPW(password_new),
																shared.encryptPW(passwordROOT_new), 
																email_new, 
																geckos_new
															); 
							if (retSetSubmitUser.getCode() == 0) {
								shared.Panel_center.view_login_screen();
							} else {
								Dialog dialogFailedNewUser = new Dialog(Dialog.TYPE_ERR, "Nepavyko");
								dialogFailedNewUser.setText("Nepavyko sukurti naujo naudotojo vardu '" + username_new + "'.\nKlaidos tekstas:\n\n" + retSetSubmitUser.getMessage());
								dialogFailedNewUser.setVisible(true);
								
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
						break;

					case "SwitchMember":
						if(shared.user.members != null){
							String mname = shared.Panel_left.dropdown.getSelectedItem().toString();
							ReturnSet retSet = shared.user.loginMember(mname);
							if(retSet.getCode() == 0)
								shared.Panel_bottom.setText("Perjungtas narys: " + mname);
						 	else {
								Dialog dialogMemberSwitchFailed = new Dialog(Dialog.TYPE_ERR, "Nepavyko");
								dialogMemberSwitchFailed.setText("Nepavyko perjungti nario:\n" + mname + "\nKlaida:\n" + retSet.getMessage());
								dialogMemberSwitchFailed.setVisible(true);
						 	}
					 	} else {
					 		Dialog dialogMemberSwitchFailed = new Dialog(Dialog.TYPE_ERR, "Nepavyko");
							dialogMemberSwitchFailed.setText("Nepavyko rasti narių, priklausančių "+shared.currentAcc+" paskyrai");
							dialogMemberSwitchFailed.setVisible(true);
						}
						break;
					case "AddMember":
						String nmname, nmpw, nmpwe, nmemail, nmgecos;
						boolean alreadyExists = false;
						
						nmname 	= shared.Panel_center.tfMemberName.getText();
						nmpw 	= new String(shared.Panel_center.tfMemberPW.getPassword());
						nmpwe	= shared.encryptPW(nmpw);
						nmemail = shared.Panel_center.tfMemberEmail.getText();
						nmgecos = shared.Panel_center.tfMemberGecos.getText();
						
						if(shared.user.members != null && shared.user.findMemberByName(nmname) != null)
							alreadyExists=true;
							
						if(alreadyExists){
							Dialog dialogMemberExists = new Dialog(Dialog.TYPE_WARN, "Dėmesio!");
							dialogMemberExists.setText("Narys tokiu vardu ("+nmname+") jau egzistuoja");
							dialogMemberExists.setVisible(true);
						} else {
							ReturnSet retSetNewMember = shared.user.addNewMember(nmname, nmpwe, nmemail, nmgecos);
							if(retSetNewMember.getCode() == 0){
								shared.user.pullMembersList();
								shared.Panel_bottom.setText("Narys pridėtas sėkmingai.");
								shared.Panel_left.loggedIn();
								shared.Panel_center.view_acc_sett();
							} else {
								Dialog dialogMemberAddFailureDialog = new Dialog(Dialog.TYPE_ERR, "Nepavyko!");
								dialogMemberAddFailureDialog.setText("Nepavyko sukurti naujo nario.\n\nKlaidos pranešimas:\n" + retSetNewMember.getMessage());
								dialogMemberAddFailureDialog.setVisible(true);
							}
						}
						
						break;
					case "ModMember":
						break;
					case "DelMember":
						final String rmmname = shared.Panel_center.dropdownMembers.getSelectedItem().toString();
						Member M = shared.user.findMemberByName(rmmname);
						if(M != null){
							Dialog dialogDelMember = new Dialog(Dialog.TYPE_LOGIN, "Patvirtinkite trinimą ROOT slaptažodžiu");
							dialogDelMember.tf.setText(shared.user.uname);
							dialogDelMember.tf.setEditable(false);
							dialogDelMember.setCallback(new CALLBACK() {
											public void function(String retVal) {}
											public void function() 				{}
											@Override
											public void function(String[] retVal) {
												ReturnSet retSetDeleted;
												if(shared.encryptPW(retVal[1]).equals(shared.user.upw_hash_root)){
													retSetDeleted = shared.user.deleteMember(rmmname);
													if(retSetDeleted.getCode() == 0){
														shared.Panel_center.view_acc_sett();
													} else {
														Dialog dialogFailedDeleteMember = new Dialog(Dialog.TYPE_ERR, "Nepavyko");
														dialogFailedDeleteMember.setText("Paskyros nario pašalinti nepavyko.\n\nKlaidos tekstas:\n" + retSetDeleted.getMessage());
														dialogFailedDeleteMember.setVisible(true);
														
													}
												}
											}
								
									});
							dialogDelMember.setVisible(true);
						}
						break;
					case "MainView":
						System.out.println("Switching to MainView");
						shared.Panel_center.view_main();
						break;
						
					case "WalletView":
						System.out.println("Switching to WalletView");
						//shared.user.loginMember(shared.Panel_left.dropdown.getSelectedItem().toString());
						shared.Panel_center.view_wallet(shared.currentMemb);
						break;
						
					case "CategView":
						System.out.println("Switching to CategoriesView");
						shared.Panel_center.view_categ();
						break;
						
					case "PlannerView":
						System.out.println("Switching to PlannerView");
						shared.Panel_center.view_planner();
						break;
						
					case "LoaningView":
						System.out.println("Switching to LoaningView");
						shared.Panel_center.view_loaning();
						break;
						
					case "AccountSettings":
						//System.out.println("Switching to AccountSettings");
						
						Dialog dialogRootPW = new Dialog(Dialog.TYPE_LOGIN,"ROOT slaptažodis");
						dialogRootPW.tf.setText(shared.currentAcc);
						dialogRootPW.tf.setEditable(false);
						dialogRootPW.setCallback(new CALLBACK() {
										public void function(String retVal) {}
										public void function() 				{}							
										@Override
										public void function(String[] retVal) {
											String password, username;
											username = retVal[0];
											password = retVal[1];
											
											ReturnSet retSet;
											
											retSet = shared.user.loginROOT(username, shared.encryptPW(password));
											
											if (retSet.getCode() == 0){
												shared.Panel_center.view_acc_sett();
											} else {
												Dialog dialogFailed = new Dialog(Dialog.TYPE_ERR, "Nepavyko!");
												dialogFailed.setText("Nepavyko prisijungti.\n\nKlaida: \n" + retSet.getMessage());
												dialogFailed.setCallback(new CALLBACK(){
																public void function(String[] retVal) 	{};
																public void function(String retVal) 	{};
																@Override
																public void function() {
																	System.out.println("Acknowledged");
																};
															});
												dialogFailed.setVisible(true);
												
											}
											
										}
									});
						dialogRootPW.setVisible(true);
						
						break;
						
					case "MemberSettings":
						System.out.println("Switching to MemberSettings");
						shared.Panel_center.view_memb_sett(shared.currentMemb);
						break;
						
					case "Login":
						String username = shared.Panel_center.tfUsername.getText();
						String password = new String(shared.Panel_center.tfPassword.getPassword());
						String password_encr = shared.encryptPW(password);
						
						shared.Panel_center.tfPassword.setText("");
						shared.Panel_center.tfPasswordROOT.setText("");
						
						System.out.println("Attempting to login with username: " + username);
						
						password_encr = shared.encryptPW(password);
						
						int RetVal = shared.user.login(username, password_encr).getCode();
						//System.out.println(RetVal);
						if (RetVal == 0){
							shared.Panel_bottom.setText("Logged in as: " + username + ". ");
							shared.Panel_center.view_main();
							shared.Panel_top.enableButtons();
							shared.Panel_bottom.append("Please choose what to do.");
						}
						
					break;
					case "settManageMembers":
						System.out.println("settManageMembers");
						break;
					case "settAccountInfo":
						System.out.println("settAccountInfo");
						//shared.Panel_center.view_acc_sett_ACCINFO();
						break;
						
					case "AccFormReset":
						System.out.println("RESET");
						shared.Panel_center.view_acc_sett();
						break;
						
					case "AccFormApply":
						
						String updatedFields="", npw, npwr, npwe, npwre, nemail, ngecos;
						
						nemail = shared.Panel_center.tfEmail.getText();
						ngecos = shared.Panel_center.tfGeckos.getText();

						npw = new String(shared.Panel_center.tfPassword.getPassword());
						npwr= new String(shared.Panel_center.tfPasswordROOT.getPassword());
						
						npwe = shared.encryptPW(npw);
						npwre= shared.encryptPW(npwr);
						
						if(! nemail.isEmpty() && ! nemail.equals(shared.user.uemail)) {
							updatedFields = updatedFields + "\n  - email";
						}
						if(! ngecos.isEmpty() && ! ngecos.equals(shared.user.ugecos)) {
							updatedFields = updatedFields + "\n  - gecos";
						}
						if(! npw.isEmpty() && ! npwe.equals(shared.user.upw_hash)) {
							//System.out.println("(! " + npw+".isEmpty() && ! " + npwe+".equals("+shared.user.upw_hash+"))");
							updatedFields = updatedFields + "\n  - password";
						}
						if(! npwr.isEmpty() && ! npwre.equals(shared.user.upw_hash_root)) {
							updatedFields = updatedFields + "\n  - ROOT password";
						}
						
						
						
						if(updatedFields != ""){ // If there's something to update
							Dialog dialogConfirmUpdate = new Dialog(Dialog.TYPE_QUESTION, "Patvirtinkite");
							dialogConfirmUpdate.setText("Ar tikrai norite pritaikyti pakeitimus?\n" + updatedFields);
							dialogConfirmUpdate.setCallback(new CALLBACK() {
											public void function(String[] retVal) 	{}
											public void function() 					{}
											@Override
											public void function(String retVal) {
												if (retVal == "YES"){
													ReturnSet retSet = new ReturnSet();
													
													shared.user.uemail = shared.Panel_center.tfEmail.getText();
													shared.user.ugecos= shared.Panel_center.tfGeckos.getText();
													
													shared.user.upw_hash = shared.encryptPW(new String (shared.Panel_center.tfPassword.getPassword()));
													shared.user.upw_hash_root = shared.encryptPW(new String (shared.Panel_center.tfPasswordROOT.getPassword()));
													
													retSet = shared.user.updateUser();
													
													if (retSet.getCode() == 0){
														System.out.println("Updated!");
														shared.Panel_bottom.setText("Pakeitimai išsaugoti!");
													} else {
														System.out.println("Failed to update: " + retSet.getMessage());
														Dialog dialogUpdateFailed = new Dialog(Dialog.TYPE_ERR, "Klaida!");
														dialogUpdateFailed.setText("Pakeitimų išsaugoti nepavyko!\n\nKlaida: \n" + retSet.getMessage());
														dialogUpdateFailed.setVisible(true);
													}
												}
											}
										});
							dialogConfirmUpdate.setVisible(true);
						} else {
							shared.Panel_bottom.setText("Niekas nepakeista.");
						}
						break;
						
					case "PurgeUser":
						
						Dialog dialogPurgeUser = new Dialog(Dialog.TYPE_QUESTION, "Ar tikrai?");
						dialogPurgeUser.setText("Ar tikrai norite pašalinti dabartinį vartotoją: '" + shared.user.uname + "' ?");
						dialogPurgeUser.setCallback(new CALLBACK() {
										public void function(String[] retVal) 	{}
										public void function() 					{}
										@Override
										public void function(String retVal) {
											if(retVal == "YES"){
												Dialog dialogRootPW = new Dialog(Dialog.TYPE_LOGIN,"ROOT slaptažodis");
												dialogRootPW.tf.setText(shared.currentAcc);
												dialogRootPW.tf.setEditable(false);
												dialogRootPW.setCallback(new CALLBACK() {
																public void function() 				{}
																public void function(String retVal) {}
																
																@Override
																public void function(String[] retVal) {
																	if(shared.encryptPW(retVal[1]).equals(shared.user.upw_hash_root)){
																		shared.user.deleteUser();
																		shared.loggedOutActions();
																	}
																
																}
															});
												dialogRootPW.setVisible(true);
												}
										}
									});
						dialogPurgeUser.setVisible(true);
						
						break;
						
					case "showFilters":
						//shared.Panel_center.FFilterPanel.setVisible(true);
						shared.Panel_center.FFilterPanel.setVisible(!shared.Panel_center.FFilterPanel.isVisible());
						break;
						
					case "addNewFlow":
						// button
						// bname
						
						CALLBACK callbNewFlow = new CALLBACK() {
							public void function(String retVal) {}
							public void function() 				{}
							
							@Override
							public void function(String[] retVal) {
								ReturnSet retSet = new ReturnSet();
								String date, amount, comment, category, planName, date_unform;
								
								date 	= retVal[0].trim();
								amount 	= retVal[1].trim();
								comment = retVal[2].trim();
								category= retVal[3].trim();
								planName= retVal[4].trim();
								date_unform = date.replace("-", "").replace(":", "").replace(" ", "");
								
								ReturnSet VALID;
								VALID = shared.validateField(date_unform, shared.ValidateRule_DATE);
								if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
								VALID = shared.validateField(amount, shared.ValidateRule_AMOUNT);
								if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
								VALID = shared.validateField(comment, shared.ValidateRule_FLOWCOMMENT);
								if(VALID.getCode() > 0)	retSet.append(VALID.getMessage(), VALID.getCode());
								
								if(retSet.getCode() == 0) {

									retSet = shared.currentWallet.addWalletFlow(
											date_unform, 
											amount, 
											comment, 
											category,
											planName
											);
								}
								
								if(retSet.getCode() > 0) {
									Dialog dialogErr = new Dialog(Dialog.TYPE_ERR, "Klaida");
									dialogErr.setText("Forma užpildyta neteisingai:\n\n" + retSet.getMessage());
									dialogErr.setVisible(true);
									return;
								}
								
							}
							
						};
						
						String[] plans_list;
					
						plans_list = new String[shared.currentWallet.plans.length];
						for(int i=0; i<plans_list.length; i++)
							plans_list[i] = shared.currentWallet.plans[i].name;
					
						
						Dialog dialogNewFlow = new Dialog(Dialog.TYPE_FORM, "Naujas įrašas");
						dialogNewFlow.addTextField("*Data", "YYYY-MM-DD hh:mm");
						dialogNewFlow.addTextField("*Suma", "xxx.yy");
						dialogNewFlow.addDropDownMenu("*Kategorija", shared.currentMemb.category == null?new String[]{""}:shared.currentMemb.category);
						dialogNewFlow.addDropDownMenu("* Planas", plans_list);
						//dialogNewFlow.addTextField("*Kategorija");
						dialogNewFlow.addTextField("*Komentaras", null);
						dialogNewFlow.setCallback(callbNewFlow);
						dialogNewFlow.ShowForm();
						
						
						break;
					
					case "newCateg":
						
						CALLBACK callbk_new_categ = new CALLBACK() {
							public void function(String retVal) {}
							public void function() 				{}
							
							@Override
							public void function(String[] retVal) {
								ReturnSet retSet = new ReturnSet();
								String cat_name = retVal[0];
								if(cat_name == null || cat_name.trim().isEmpty()){
									Dialog dialogBadCategName = new Dialog(Dialog.TYPE_ERR, "Netinkamas pavadinimas");
									dialogBadCategName.setText("Kategorijos pavadinimas neatitinka reikalavimų");
									dialogBadCategName.setVisible(true);
									return;
								}
								
								
								retSet = shared.currentMemb.addCategory(cat_name);
								
								if(retSet.getCode() != 0){
									Dialog dialogBadCategName = new Dialog(Dialog.TYPE_ERR, "Klaida");
									dialogBadCategName.setText("Kuriant naują kategoriją įvyko klaida:\n\n" + retSet.getMessage());
									dialogBadCategName.setVisible(true);
								}
							}
							
						};
						
						Dialog dialogNewCateg = new Dialog(Dialog.TYPE_FORM, "Nauja kategorija");
						dialogNewCateg.addTextField("Pavadinimas", null);
						dialogNewCateg.setCallback(callbk_new_categ);
						dialogNewCateg.ShowForm();
						break;
						
					case "delCateg":
						String catName = "";
						catName = shared.Panel_center.dropdownCategories.getSelectedItem().toString();
						
						CALLBACK callbk_question_del_categ = new CALLBACK() {
							public void function(String[] retVal) {}
							public void function() {}
							
							@Override
							public void function(String retVal) {
								if(retVal == "YES"){
									ReturnSet retSet = new ReturnSet();
									retSet = shared.currentMemb.delCategory(shared.Panel_center.dropdownCategories.getSelectedItem().toString());
									if(retSet.getCode() != 0){
										Dialog dialogDelCateg = new Dialog(Dialog.TYPE_ERR, "Klaida");
										dialogDelCateg.setText("Kategorijos ištrinti nepavyko.\n\n" + retSet.getMessage());
										dialogDelCateg.setVisible(true);
									}
								}
							}

						};
						
						Dialog dialogDelCategQuestion = new Dialog(Dialog.TYPE_QUESTION, "Patvirtinkite");
						dialogDelCategQuestion.setText("Ar tikrai norite ištrinti kategoriją '"+catName+"'?");
						dialogDelCategQuestion.setCallback(callbk_question_del_categ);
						dialogDelCategQuestion.setVisible(true);
						
						break;
					case "newPlan":
						
						CALLBACK callbkNewPlan = new CALLBACK() {
							ReturnSet retSetNewPlan = new ReturnSet();
							
							public void function() 				{}
							public void function(String retVal) {}
							
							@Override
							public void function(String[] retVal) {

								Wallet W;
									for(int i=0; i<shared.currentMemb.wallets.length; i++){
										System.out.println("Testing wallet to match name " + retVal[7] + " : "+ shared.currentMemb.wallets[i].wname);
										if(shared.currentMemb.wallets[i].wname.equals(retVal[7])){ // if selected wallet found
											W=shared.currentMemb.wallets[i];
											System.out.println("Trying to add a new plan");
											retSetNewPlan = W.addWalletPlan(
												retVal[0], 
												retVal[6], 
												retVal[8] == "Bendras"? W.PLAN_TYPE_ALL:W.PLAN_TYPE_OLY, 
												retVal[1].replace("-",  "").replace(" ", ""), 
												retVal[2].replace("-",  "").replace(" ", ""), 
												retVal[3].replace(",", ".").replace(" ", ""),
												retVal[4].replace(",", ".").replace(" ", ""),
												retVal[5].replace(",",  "").replace(" ", "")
											);
											
											if (retSetNewPlan.getCode() != 0) {
												Dialog dialogNewPlanFailed = new Dialog(Dialog.TYPE_ERR, "Klaida");
												dialogNewPlanFailed.setText("Nepavyko sukurti naujo plano.\n\n" + retSetNewPlan.getMessage());
												dialogNewPlanFailed.setVisible(true);
											}
											return;
										}
									}
							}
						};
						
						
						String[] wallets_names = new String[shared.currentMemb.wallets.length];
							for(int i=0; i<shared.currentMemb.wallets.length; i++)
								wallets_names[i] = shared.currentMemb.wallets[i].wname;
						String[] plan_types = {"Fiksuotas", "Bendras"};
							
						Dialog dialogNewPlan = new Dialog(Dialog.TYPE_FORM, "Naujas planas");
						dialogNewPlan.addTextField("*Plano pavadinimas", null);			// [0]
						dialogNewPlan.addDropDownMenu("*Piniginė", wallets_names);		// [1]
						dialogNewPlan.addDropDownMenu("*Plano modelis", plan_types);	// [2]
						dialogNewPlan.addTextField("*Pradžios data", "YYYY-MM-DD");		// [3]
						dialogNewPlan.addTextField("*Pabaigos data", "YYYY-MM-DD");		// [4]
						dialogNewPlan.addTextField("Planuojamos išlaidos", "xxx.yy");	// [5]
						dialogNewPlan.addTextField("Planuojamos įplaukos", "xxx.yy");	// [6]
						dialogNewPlan.addTextField("Planuojamas balansas", "xxx.yy");	// [7]
						dialogNewPlan.addTextField("Komentaras...", null);				// [8]
						dialogNewPlan.setCallback(callbkNewPlan);
						dialogNewPlan.ShowForm();
						break;
					case "delPlan":
						break;
						
						
						
						
						
						
						
					default:
						break;
				}
				
				break;
				
			default:
				break;
		}
		
	}
	
	
	
}
