package com.planner.netikras;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale.Category;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.table.AbstractTableModel;

import com.planner.netikras.SharedClass.CALLBACK;
import com.planner.netikras.SharedClass.Dialog;
import com.planner.netikras.SharedClass.FUNCTION;
import com.planner.netikras.SharedClass.QryBuilder;
import com.planner.netikras.SharedClass.ReturnSet;
import com.planner.netikras.UserClass.Member;
import com.planner.netikras.UserClass.Plan;
import com.planner.netikras.UserClass.Wallet;
import com.planner.netikras.UserClass.DATABASE.DB_METADATA;


public class PanelCenterClass extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2499954702262691712L;

	protected SharedClass shared;
	
	protected int GAP;
	
	// USER details
	JTextField 			tfUsername;
	JPasswordField 		tfPassword;
	JPasswordField 		tfPasswordROOT;
	JTextField			tfEmail;
	JTextField			tfGeckos;
	
	// MEMBER details
	JTextField			tfMemberName;
	JTextField 			tfMemberEmail;
	JPasswordField 		tfMemberPW;
	JTextArea			tfMemberGecos;
	
	JScrollPane			scrlTable;
	JTable				tblMain;
	TblModel			tmMain;
	
	JComboBox<String> dropdownMembers;
	JComboBox<String> dropdownCategories;
	
	Dimension dimTF;
	
	FlowFilters FFilterPanel;
	
	PlannerClass PlannerPanel;
	
	
	/*-------------------- VIEWS ----------------------*/
	
	public PanelCenterClass(SharedClass data) {
		// constructor
		shared = data;
		
		dimTF = new Dimension(200, 20);
		
		tfUsername 		= new JTextField();
		tfPassword 		= new JPasswordField();
		tfPasswordROOT	= new JPasswordField();
		tfEmail			= new JTextField();
		tfGeckos		= new JTextField();
		
		tfMemberName		= new JTextField();
		tfMemberEmail		= new JTextField();
		tfMemberPW 			= new JPasswordField();
		tfMemberGecos		= new JTextArea();
		 
		FFilterPanel	= new FlowFilters();
		PlannerPanel	= new PlannerClass(shared);
		
		 tfUsername.setPreferredSize(dimTF);
		 tfPassword.setPreferredSize(dimTF); 	
		 tfPasswordROOT.setPreferredSize(dimTF);	
		 tfEmail.setPreferredSize(dimTF);		
		 tfGeckos.setPreferredSize(dimTF);
		 
		 tfMemberName.setPreferredSize(dimTF); 	
		 tfMemberEmail.setPreferredSize(dimTF);
		 tfMemberPW.setPreferredSize(dimTF);
		 
		 tfMemberGecos.setLineWrap(true);
		 tfMemberGecos.setWrapStyleWord(true);
		 
		scrlTable = new JScrollPane();
		 
		dropdownCategories = new JComboBox<String>();
		 
		 
		//shared.currentView = "MainView";
		
		//view_main();
		view_login_screen();
	}
	
	
	protected PanelCenterClass view_main() {
		
		setLayout(new GridBagLayout());
		//setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		shared.currentView = "Pagrindinis";
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		

		removeAll();
		
		add(new JLabel("Pasirinkite ir prijunkite savo narį ARBA sukurkite naują, jei esate administratorius."));
		revalidate();
		repaint();
		return this;
	}


	protected PanelCenterClass view_wallet(Member MEMBER) {

		removeAll();
		
		shared.currentView = "Piniginės";
		
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		
		scrlTable.removeAll();
		setLayout(new GridLayout());
		
		System.out.println(MEMBER);
		
		JPanel panWallets = new JPanel();
		
		if(MEMBER == null)
			panWallets.add(new JLabel("Neaktyvuotas joks narys"));
		else if(MEMBER.wallets == null)
			panWallets.add(new JLabel("Narys neturi sukurtų piniginių"));
		else {
			for(Wallet W : MEMBER.wallets)
				panWallets.add(new WalletSubPanel(W));
		}
		
		
		//panWallets.setLayout(new BoxLayout(panWallets, BoxLayout.PAGE_AXIS));
		panWallets.setLayout(new GridLayout(10, 1));
		scrlTable = new JScrollPane(panWallets);
		
		//scrlTable.setViewportView(panWallets);
		
		scrlTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrlTable.setSize(getSize());
		//System.out.println(getSize());
		scrlTable.revalidate();
		scrlTable.repaint();
		scrlTable.setVisible(true);
		add(scrlTable);

		revalidate();
		repaint();
		return this;
		
	}
	
	
	protected PanelCenterClass view_categ() {
		ReturnSet retSet;
		removeAll();
		
		shared.currentView = "Kategorijos";
		
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		
		JPanel panCategs = new JPanel();
		JScrollPane scroller;
		
		JPanel panCateg;
		JLabel lblCateg;
		JCheckBox cbxCateg;
		
		
		JButton btnNewCateg = new JButton();
		btnNewCateg.setActionCommand("ButtonClick");
		btnNewCateg.setName("newCateg");
		btnNewCateg.addActionListener(shared.aListener);
		
		JButton btnDelCateg = new JButton();
		btnNewCateg.setActionCommand("ButtonClick");
		btnNewCateg.setName("delCateg");
		btnNewCateg.addActionListener(shared.aListener);
		
		
		retSet = shared.currentMemb.loadCategories();
		
		if(retSet.getCode() != 0){
			com.planner.netikras.SharedClass.Dialog dialogBadCategName = new com.planner.netikras.SharedClass.Dialog(com.planner.netikras.SharedClass.Dialog.TYPE_ERR, "Klaida");
			dialogBadCategName.setText("Įkeliant kategorijas įvyko klaida:\n\n" + retSet.getMessage());
			dialogBadCategName.setVisible(true);
			return this;
		}
		
		for(String cat : shared.currentMemb.category){
			panCateg = new JPanel();
			lblCateg = new JLabel(cat);
			cbxCateg = new JCheckBox();
			cbxCateg.setName(cat);
			
			panCateg.add(cbxCateg);
			panCateg.add(lblCateg);
			
			panCategs.add(panCateg);
		}
		
		scroller = new JScrollPane(panCategs);
		
		
		add(scroller);
		add(btnNewCateg);
		add(btnDelCateg);
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scroller)
				.addGroup(layout.createSequentialGroup()
					.addComponent(btnNewCateg)
					.addComponent(btnDelCateg)
				)
			)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addComponent(scroller)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(btnNewCateg)
				.addComponent(btnDelCateg)
			)
		);
		
		setLayout(layout);
		
		revalidate();
		repaint();
		return this;
		
	}
	
	
	protected PanelCenterClass view_planner() {
		
		removeAll();
		
		shared.currentView = "Planuotojas";
		
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		PlannerPanel.loadAllMemberPlans(shared.currentMemb);
		setLayout(new GridLayout());
		add(PlannerPanel);
		
		revalidate();
		repaint();
		return this;
		
	}
	
	
	protected PanelCenterClass view_loaning() {
		
		removeAll();
		
		shared.currentView = "Skolinimasis";
		
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		
		JButton but2 = new JButton("Wallet");
		but2.addActionListener(shared.aListener);
		but2.setActionCommand("ButtonClick");
		but2.setName("WalletView");
		
		add(but2);
		
		revalidate();
		repaint();
		return this;
		
	}
	
	
	protected void view_flows(Wallet wallet){
		removeAll();
		
		
		JButton btnFilters = new JButton("Filtrai");
		JButton btnNewFlow = new JButton("Pridėti");
		btnNewFlow.addActionListener(shared.aListener);
		btnFilters.addActionListener(shared.aListener);
		btnNewFlow.setActionCommand("ButtonClick");
		btnFilters.setActionCommand("ButtonClick");
		btnNewFlow.setName("addNewFlow");
		btnFilters.setName("showFilters");
		
		JPanel panBTNS = new JPanel();
		panBTNS.add(btnNewFlow);
		panBTNS.add(btnFilters);
		
		shared.currentView = "Pervedimai ["+wallet.wname+"]";
		shared.currentWallet = wallet;
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		
		FFilterPanel.assignWallet(wallet);
		FFilterPanel.loadFilteredWallet();
		

		tmMain = new TblModel(wallet);  //  ––┐
		tblMain = new JTable(tmMain);   //  <–┘  ––––––┐   
		tblMain.createDefaultColumnsFromModel(); //    |
												 //    |
		scrlTable = new JScrollPane(tblMain);    // <––┘
		scrlTable.setBorder(BorderFactory.createLoweredBevelBorder());
		
		tmMain.setTable(tblMain);
		
		//tmMain.REFRESH_TABLE();

		scrlTable.setPreferredSize(getSize());
		
		add(scrlTable);
		add(FFilterPanel);
		
		
		FFilterPanel.ddnPlan.removeAllItems();
		for(Plan P : shared.currentWallet.plans)
			FFilterPanel.ddnPlan.addItem(P.name);
		
		
		GroupLayout layout = new GroupLayout(this);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scrlTable)
				.addGroup(layout.createSequentialGroup()
					.addComponent(btnNewFlow)
					.addComponent(btnFilters)
				)
			)
			
			.addComponent(FFilterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addGroup(layout.createSequentialGroup()
					.addComponent(scrlTable)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnNewFlow)
						.addComponent(btnFilters)
					)
				)
				
				.addComponent(FFilterPanel)
			)
		);
		
		setLayout(layout);
		
		revalidate();
		repaint();	
		
		
		
		
	}

	
	protected PanelCenterClass view_login_screen(){ 
		
		removeAll();
		
		shared.currentView = "Prisijungimas";
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		
		JPanel panLogin 		= new JPanel();
		
		panLogin.setLayout(new BoxLayout(panLogin, BoxLayout.Y_AXIS));
		
		JLabel lblUsername 		= new JLabel("Vartotojo vardas *");
		JLabel lblPassword 		= new JLabel("Vartotojo slaptažodis *");
		
		
		JButton btnLogin = new JButton("Jungtis");
		btnLogin.addActionListener(shared.aListener);
		btnLogin.setActionCommand("ButtonClick");
		btnLogin.setName("Login");
		btnLogin.setMnemonic(KeyEvent.VK_ENTER);
		
		panLogin.add(lblUsername);
		panLogin.add(tfUsername); 
		panLogin.add(new JLabel(" "));
		panLogin.add(lblPassword);
		panLogin.add(tfPassword);
		panLogin.add(btnLogin);
		
		// */
		
		
		

		GroupLayout layout = new GroupLayout(panLogin);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		

		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(lblUsername)
							.addComponent(lblPassword)
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(tfUsername)
							.addComponent(tfPassword)
						)
					)
					.addComponent(btnLogin)
			);
			
			layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(tfUsername)
					)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(tfPassword)
					)
					.addComponent(btnLogin)
			);
		
		panLogin.setLayout(layout);
		
		
		
		add(panLogin);
		revalidate();
		repaint();
		return this;
	}
		
	
	protected PanelCenterClass view_new_user(){
		removeAll();
		shared.currentView = "Naujas naudotojas";
		
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		
		JPanel panNewUser = new JPanel();
		panNewUser.setLayout(new BoxLayout(panNewUser, BoxLayout.PAGE_AXIS));
		
		JButton btnLogin = new JButton("Kurti");
		btnLogin.addActionListener(shared.aListener);
		btnLogin.setActionCommand("ButtonClick");
		btnLogin.setName("SubmitNewUser");
		btnLogin.setMnemonic(KeyEvent.VK_ENTER);
		
		JLabel lblUsername 		= new JLabel("Prisijungimo vardas *");
		JLabel lblPassword 		= new JLabel("Slaptažodis *");
		JLabel lblPasswordROOT	= new JLabel("Administravimo slaptažodis *");
		JLabel lblEmail 		= new JLabel("El.p. adresas *");
		JLabel lblGeckos 		= new JLabel("Komentaras");
		
		panNewUser.add(lblUsername);
		panNewUser.add(tfUsername);
		panNewUser.add(lblPassword);
		panNewUser.add(tfPassword);
		panNewUser.add(lblPasswordROOT);
		panNewUser.add(tfPasswordROOT);
		panNewUser.add(lblEmail);
		panNewUser.add(tfEmail);
		panNewUser.add(lblGeckos);
		panNewUser.add(tfGeckos);
		panNewUser.add(btnLogin);
		
		add(panNewUser);
		
		revalidate();
		repaint();
		
		return this;
	}

	
	/*-------------------- SETTINGS ----------------------*/
	
	

	protected PanelCenterClass view_acc_sett() {

		removeAll();
		
		shared.currentView = "Paskyros nustatymai";
		
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		setLayout( new BorderLayout());

		JTabbedPane panTabbedAccSett = new JTabbedPane();
		
		JPanel panSettAcc 		= new JPanel();
		JPanel panSettMemb	 	= new JPanel();
		JPanel panButtons1		= new JPanel();
		JPanel panButtons2		= new JPanel();
		{ // panSettAcc
					JLabel lblPassword 		= new JLabel("Keisti slaptažodį");
					JLabel lblPasswordROOT 	= new JLabel("Keisti ROOT slaptažodį");
					JLabel lblEmail 		= new JLabel("El. pašto adresas");
					JLabel lblGeckos 		= new JLabel("Papildoma informacija");
					
					JButton btnApply		= new JButton("Pritaikyti");
					JButton btnReset		= new JButton("Atstatyti");
					JButton btnPurge		= new JButton("Pašalinti sąsk.");
					
					tfEmail.		setText(shared.user.uemail);
					tfGeckos.		setText(shared.user.ugecos);
					tfPassword.		setText("");
					tfPasswordROOT.	setText("");
					
					btnReset.addActionListener(shared.aListener);
					btnReset.setActionCommand("ButtonClick");
					btnReset.setName("AccFormReset");
					
					btnApply.addActionListener(shared.aListener);
					btnApply.setActionCommand("ButtonClick");
					btnApply.setName("AccFormApply");
					
					btnPurge.addActionListener(shared.aListener);
					btnPurge.setActionCommand("ButtonClick");
					btnPurge.setName("PurgeUser");
					
					
					panButtons1.add(btnReset);
					panButtons1.add(btnApply);
					panButtons2.add(btnPurge);
					
					GroupLayout layout1 = new GroupLayout(panSettAcc);
					
					layout1.setAutoCreateGaps(true);
					layout1.setAutoCreateContainerGaps(true);
					
					layout1.setHorizontalGroup(
						layout1.createSequentialGroup()
							.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblPassword)
								.addComponent(lblPasswordROOT)
								.addComponent(lblEmail)
								.addComponent(lblGeckos)
								.addComponent(panButtons2)
							)
							.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(tfPassword)
								.addComponent(tfPasswordROOT)
								.addComponent(tfEmail)
								.addComponent(tfGeckos)
								.addComponent(panButtons1)
							)
					);
					
					layout1.setVerticalGroup(
						layout1.createSequentialGroup()
							.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblPassword)
								.addComponent(tfPassword)
							)
							.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblPasswordROOT)
								.addComponent(tfPasswordROOT)
							)
							.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblEmail)
								.addComponent(tfEmail)
							)
							.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblGeckos)
								.addComponent(tfGeckos)
							)
							.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(panButtons2)
									.addComponent(panButtons1)
							)
					);
					
					
					
					
					panSettAcc.setLayout(layout1);
		}
		{// panSettMemb
			
			JPanel panSettMemberInfo = new JPanel();
			//JPanel panSettMemberWallets = new JPanel();
			
			JButton btnDelMemb = new JButton("Pašalinti");
			JButton btnAddMemb = new JButton("Pridėti");
			JButton btnModMemb = new JButton("Redaguoti");
			
			
			JLabel	lblSettMName 	= new JLabel("Vardas:");
			JLabel	lblSettMEmail 	= new JLabel("El. paštas:");
			JLabel	lblSettMPswd 	= new JLabel("Slaptažodis");
			JLabel	lblSettMGecos 	= new JLabel("Aprašymas");
			
			btnAddMemb.addActionListener(shared.aListener);
			btnAddMemb.setActionCommand("ButtonClick");
			btnAddMemb.setName("AddMember");
			
			btnModMemb.addActionListener(shared.aListener);
			btnModMemb.setActionCommand("ButtonClick");
			btnModMemb.setName("ModMember");
			
			btnDelMemb.addActionListener(shared.aListener);
			btnDelMemb.setActionCommand("ButtonClick");
			btnDelMemb.setName("DelMember");
			
			
			dropdownMembers = new JComboBox<String>();
			dropdownMembers.addActionListener(shared.aListener);
			dropdownMembers.setActionCommand("DropDown");
			dropdownMembers.setName("ddnSettMemb");
			dropdownMembers.setEditable(false);
			
			panSettMemberInfo.setBorder(BorderFactory.createTitledBorder("Nario informacija"));
			//panSettMemberWallets.setBorder(BorderFactory.createTitledBorder("Nario piniginės"));
			
			GroupLayout layoutSettMemb = new GroupLayout(panSettMemb);
			GroupLayout layoutInfo = new GroupLayout(panSettMemberInfo);
			
			layoutInfo.setAutoCreateGaps(true);
			layoutInfo.setAutoCreateContainerGaps(true);
			
			
			layoutSettMemb.setAutoCreateGaps(true);
			layoutSettMemb.setAutoCreateContainerGaps(true);
			
			
			layoutInfo.setHorizontalGroup(
				layoutInfo.createSequentialGroup()
					.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblSettMName)
						.addComponent(lblSettMEmail)
						.addComponent(lblSettMPswd)
						.addComponent(lblSettMGecos)
						//.addComponent(panSettMemberWallets)
					)
					.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(tfMemberName)
						.addComponent(tfMemberEmail)
						.addComponent(tfMemberPW)
						.addComponent(tfMemberGecos)
						//.addComponent(panSettMemberWallets)
					)
			);
			
			layoutInfo.setVerticalGroup(
				layoutInfo.createSequentialGroup()
					.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblSettMName)
						.addComponent(tfMemberName)
					)
					.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblSettMEmail)
						.addComponent(tfMemberEmail)
					)
					.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblSettMPswd)
						.addComponent(tfMemberPW)
					)
					.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblSettMGecos)
						.addComponent(tfMemberGecos)
					)
					//.addComponent(panSettMemberWallets)
			);
			
			
			layoutSettMemb.setHorizontalGroup(
					layoutSettMemb.createSequentialGroup()
					.addGroup(layoutSettMemb.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(dropdownMembers)
						.addGroup(layoutSettMemb.createSequentialGroup()
							.addComponent(panSettMemberInfo)
							.addGroup(layoutSettMemb.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(btnAddMemb, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnModMemb, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnDelMemb, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							)
							
						)
					)
			);
			
			layoutSettMemb.setVerticalGroup(
					layoutSettMemb.createSequentialGroup()
					.addComponent(dropdownMembers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
				              GroupLayout.PREFERRED_SIZE)
					
					.addGroup(layoutSettMemb.createParallelGroup(GroupLayout.Alignment.BASELINE)
					//	.addGroup(layoutSettMemb.createSequentialGroup()
							.addComponent(panSettMemberInfo)
							.addGroup(layoutSettMemb.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layoutSettMemb.createSequentialGroup()
									.addComponent(btnAddMemb)
									.addComponent(btnModMemb)
									.addComponent(btnDelMemb)
								)
							)
						//)
					)
			);
			
			if (shared.user.members != null)
				for(Member memb: shared.user.members){
					dropdownMembers.addItem(memb.mname);
				}
			
			panSettMemberInfo.setLayout(layoutInfo);
			panSettMemb.setLayout(layoutSettMemb);
			
		}
		
		
		
		
		panTabbedAccSett.addTab("Vartotojo nustatymai", panSettAcc);
		panTabbedAccSett.addTab("Narių nustatymai", panSettMemb);

		add(panTabbedAccSett);
		revalidate();
		repaint();
		return this;
		
	}


	protected JPanel view_memb_sett(final Member MEMBER) {
		
		removeAll();
		
		shared.currentView = "Nario nustatymai";
		
		setBorder(BorderFactory.createTitledBorder(shared.currentView));
		
		
		ActionListener LocalAL = new ActionListener() {
			com.planner.netikras.SharedClass.Dialog dialog;
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				String txt;
				switch(btn.getName()){
					case "ModMember":
						ReturnSet retSetModM = new ReturnSet();
						
						txt = tfMemberEmail.getText().trim();
						if( ! txt.isEmpty() && ! txt.equals(MEMBER.memail))
							retSetModM.appendLn(MEMBER.updateEmail(txt).getMessage(), retSetModM.getCode()+1);
						
						txt = tfMemberGecos.getText().trim();
						if( ! txt.isEmpty() && ! txt.equals(MEMBER.mgecos))
							retSetModM.appendLn(MEMBER.updateGecos(txt).getMessage(), retSetModM.getCode()+1);
						
						txt = new String(tfMemberPW.getPassword()).trim();
						if( ! txt.isEmpty() && ! shared.encryptPW(txt).equals(MEMBER.mpw_hash))
							retSetModM.appendLn(MEMBER.updatePassword(shared.encryptPW(txt)).getMessage(), retSetModM.getCode()+1);
						
						
						if(retSetModM.getCode() != 0){
							dialog = new com.planner.netikras.SharedClass.Dialog(com.planner.netikras.SharedClass.Dialog.TYPE_ERR, "");
							dialog.setText("Nepavyko pakeisti įrašo.\n\nKlaida:\n" + retSetModM.getMessage());
							dialog.setVisible(true);
						}
						
						break;
					case "AddWallet":
						ReturnSet retSetAddW = new ReturnSet();
						
						CALLBACK callb = new CALLBACK() {
							public void function(String retVal) {}
							public void function() 				{}
							
							@Override
							public void function(String[] retVal) {
								ReturnSet retSetAddingWallet = new ReturnSet();
								if( ! retVal[0].trim().isEmpty()){
									retSetAddingWallet.appendLn(MEMBER.addWallet(retVal[0]).getMessage(), retSetAddingWallet.getCode()*2);
								} else {
									retSetAddingWallet.append("Netinkamas piniginės pavadinimas", 1);
								}
								
								if(retSetAddingWallet.getCode() != 0){
									dialog = new com.planner.netikras.SharedClass.Dialog(com.planner.netikras.SharedClass.Dialog.TYPE_ERR, "Klaida!");
									dialog.setText(retSetAddingWallet.getMessage());
									dialog.setVisible(true);
								}
								
							}
						};
						
						dialog = new com.planner.netikras.SharedClass.Dialog(com.planner.netikras.SharedClass.Dialog.TYPE_FORM, "Nauja piniginė");
						dialog.addTextField("Pavadinimas*:  ", null);
						dialog.setCallback(callb);
						dialog.ShowForm();
						
						if(retSetAddW.getCode() != 0){
							dialog = new com.planner.netikras.SharedClass.Dialog(com.planner.netikras.SharedClass.Dialog.TYPE_ERR, "Klaida");
							dialog.setText("Nepavyko sukurti naujos piniginės.\n\nKlaida:\n" + retSetAddW.getMessage());
							dialog.setVisible(true);
						}
						break;
				}
			}
		};
		
		
		
		JPanel panMemberSettings = new JPanel();
		
		JPanel panSettMemberInfo 	= new JPanel();
		JPanel panSettMemberWallets = new JPanel();
		JPanel panSettMemberButtons = new JPanel();
		
		JButton btnModMemb 		= new JButton("Redaguoti     ");
		JButton btnaddWallet 	= new JButton("Kurti piniginę");
		JButton btnaddCategory	= new JButton("Pridėti kateg.");
		JButton btndelCategory	= new JButton("Pašalinti kateg.");
		
		
		JLabel	lblSettMName 	= new JLabel("Vardas:     ");
		JLabel	lblSettMEmail 	= new JLabel("El. paštas: ");
		JLabel	lblSettMPswd 	= new JLabel("Slaptažodis:");
		JLabel	lblSettMGecos 	= new JLabel("Aprašymas:  ");
		JLabel	lblCategory		= new JLabel("Kategorijos:");
		
		btnModMemb.addActionListener(LocalAL);
		btnModMemb.setActionCommand("ButtonClick");
		btnModMemb.setName("ModMember");
		
		
		//btnaddWallet.addActionListener(shared.aListener);
		btnaddWallet.addActionListener(LocalAL);
		btnaddWallet.setActionCommand("ButtonClick");
		btnaddWallet.setName("AddWallet");
		
		btnaddCategory.addActionListener(shared.aListener);
		btnaddCategory.setActionCommand("ButtonClick");
		btnaddCategory.setName("newCateg");
		
		btndelCategory.addActionListener(shared.aListener);
		btndelCategory.setActionCommand("ButtonClick");
		btndelCategory.setName("delCateg");
		
		panSettMemberInfo.setBorder(BorderFactory.createTitledBorder("Nario informacija"));
		panSettMemberWallets.setBorder(BorderFactory.createTitledBorder("Nario piniginės"));
		panSettMemberButtons.setBorder(BorderFactory.createTitledBorder("Veiksmai"));
		
		panSettMemberButtons.setLayout(new BoxLayout(panSettMemberButtons, BoxLayout.Y_AXIS));
		panSettMemberButtons.add(btnModMemb);
		panSettMemberButtons.add(btnaddWallet);
		panSettMemberButtons.add(Box.createVerticalGlue());
		
		tfMemberName.setText(MEMBER.mname);
		tfMemberEmail.setText(MEMBER.memail);
		tfMemberGecos.setText(MEMBER.mgecos);
		
		
		GroupLayout layoutInfo = new GroupLayout(panSettMemberInfo);
		
		layoutInfo.setAutoCreateGaps(true);
		layoutInfo.setAutoCreateContainerGaps(true);
		
		
		layoutInfo.setHorizontalGroup(
			layoutInfo.createSequentialGroup()
				.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(lblSettMName)
					.addComponent(lblSettMEmail)
					.addComponent(lblSettMPswd)
					.addComponent(lblSettMGecos)
					.addComponent(lblCategory)
					//.addComponent(panSettMemberWallets)
				)
				.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(tfMemberName)
					.addComponent(tfMemberEmail)
					.addComponent(tfMemberPW)
					.addComponent(tfMemberGecos)
					//.addComponent(panSettMemberWallets)
					.addComponent(dropdownCategories, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGroup(layoutInfo.createSequentialGroup()
						.addComponent(btnaddCategory)
						.addComponent(btndelCategory)
					)
				)
		);
		
		layoutInfo.setVerticalGroup(
			layoutInfo.createSequentialGroup()
				.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(lblSettMName)
					.addComponent(tfMemberName)
				)
				.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(lblSettMEmail)
					.addComponent(tfMemberEmail)
				)
				.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(lblSettMPswd)
					.addComponent(tfMemberPW)
				)
				.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(lblSettMGecos)
					.addComponent(tfMemberGecos)
				)
				.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(lblCategory)
					.addComponent(dropdownCategories)
				)
				.addGroup(layoutInfo.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btnaddCategory)
					.addComponent(btndelCategory)
				)
				//.addComponent(panSettMemberWallets)
		);
		

		
		
		setLayout(new GridLayout());
		panSettMemberInfo.setLayout(layoutInfo);
		
		
		panMemberSettings.setLayout(new BoxLayout(panMemberSettings, BoxLayout.LINE_AXIS));
		panMemberSettings.add(panSettMemberInfo);
		panMemberSettings.add(panSettMemberButtons);
		
		
		add(panMemberSettings);
		
		revalidate();
		repaint();
		return panMemberSettings;
		
	}
	
	
	
	/*=================================================================*/
	/*====================== SECONDARY METHODS ========================*/
	/*=================================================================*/
	
	/**
	 * Will be used to list flows only 
	 */
	private class TblModel extends AbstractTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7033706459676715355L;
		String[] colNames = {"Data", "Suma", "Kategorija", "Planas", "Komentaras"};
		
		private JTable TABLE;
		private String[] columnNames;
		private Wallet wallet;

		private Object[][] elements = {}; // strings and a button

		public TblModel(Wallet wallet) {
			this.wallet = wallet;
			this.columnNames = colNames;
		}
		
		public TblModel(String[] columnTitles) {
			this.columnNames = columnTitles;
		}
		
		protected void setTable(JTable table){
			this.TABLE = table;
		}
		
		
		protected void REFRESH_TABLE(){

			String[] newDataRow;
			
			if(wallet.flows == null) {
				elements=new Object[0][0];
				fireTableDataChanged();
				TABLE.revalidate();
				TABLE.repaint();
				return;
			}
			
			//elements=new Object[wallet.flows.length][columnNames.length];
			elements=new Object[0][0];
			

			
			for(int i=0; i<wallet.flows.length; i++){
				newDataRow = new String[columnNames.length];

				String DATE = "";
				DATE = DATE + ""+wallet.flows[i].date.substring(0, 4) + "-";
				DATE = DATE + ""+wallet.flows[i].date.substring(4, 6) + "-";
				DATE = DATE + ""+wallet.flows[i].date.substring(6, 8) + " ";
				DATE = DATE + ""+wallet.flows[i].date.substring(8, 10) + ":";
				DATE = DATE + ""+wallet.flows[i].date.substring(10);
				//newDataRow[0] = ""+wallet.flows[i].date;
				newDataRow[0] = DATE;
				newDataRow[1] = (wallet.flows[i].negative?"-":"")+wallet.flows[i].amount;
				newDataRow[2] = ""+wallet.flows[i].category;
				newDataRow[3] = ""+wallet.flows[i].plan;
				newDataRow[4] = ""+wallet.flows[i].comment;
				
				addElementSet(newDataRow);
			}
			
			fireTableDataChanged();
			TABLE.revalidate();
			TABLE.repaint();
			
		}
				
		private void addElementSet(String[] subSet){
			elements = shared.addToArrayFront( elements, subSet );
		}
		
		
		protected Object[] getRow(int rowIndex){
			if(elements != null && elements[rowIndex] != null){
				return elements[rowIndex];
			}
			return null;
		}
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			
			if(
					elements 			!= null 	&& 
					elements.length 	> rowIndex 	&& 
					elements[rowIndex] 	!= null 	&& 
					columnNames.length 	> columnIndex
				) {//&& elements[rowIndex][columnIndex] != null)
				return elements[rowIndex][columnIndex];
			}
			return null;
		}
		
		@Override
		public int getRowCount() {
			if(elements != null)
				return elements.length;
			return 0;
		}
		
		@Override
		public int getColumnCount() {
			//return columnNames == null?0:columnNames.length;
			return columnNames.length;
		}
		
		@Override
		public String getColumnName(int col) {
	        return columnNames[col];
		}
		
		
	}
	
	
	private class WalletSubPanel extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2233725064412953294L;
		
		Wallet wallet;
		
				
		JLabel WLblBalance;
		
		JTextField WTFieldName;
		JTextField WTFieldBalance;
		
		
		/*These two arrays must go together.*/
		private final String[] mItems= {"Pinigų srautai", "Planai", "Ištrinti(!)" };
		
		private FUNCTION[] F = new FUNCTION[] {
				new FUNCTION(){public void run(){view_flows(wallet);};},
				new FUNCTION(){public void run(){SeeWalletPlans();};},
				//new FUNCTION(){public void run(){/*view_edit_wallet(wallet)*/;};},
				new FUNCTION(){public void run(){deleteWallet();};}
		};
		
		JPopupMenu popup;
		JMenuItem mItem;
		
		
		
		
		WalletSubPanel(Wallet wallet) {
			
			this.wallet = wallet;
			LocalAL MenuAL =  new LocalAL(wallet);
			PopupListener pl = new PopupListener();
			popup = new JPopupMenu();
			//for(String Item : mItems){
			for(int i=0; i<mItems.length; i++){
				mItem = new JMenuItem(mItems[i]);
				mItem.addActionListener(MenuAL);
				mItem.setName(""+i);
				mItem.setActionCommand("MenuItem");
				popup.add(mItem);
			}
			addMouseListener(pl);
			
			WTFieldName = new JTextField();
			WTFieldName.setText(wallet.wname);
			WTFieldName.setEditable(false);
			WTFieldName.setAlignmentX(CENTER_ALIGNMENT);
			WTFieldName.setFont(new Font(WTFieldName.getFont().getName(), Font.BOLD, WTFieldName.getFont().getSize()*3/2));
			WTFieldName.setBorder(null);
			
			WLblBalance = new JLabel("Piniginės balansas");
			

			WTFieldBalance = new JTextField();
			WTFieldBalance.setText("" + wallet.wamount);
			WTFieldBalance.setEditable(false);
			
			
			GroupLayout layout = new GroupLayout(this);
			
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addComponent(WTFieldName)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(WLblBalance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(WTFieldBalance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					)
			);
			
			layout.setVerticalGroup(
					layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(WTFieldName)
							.addGroup(layout.createSequentialGroup()
		                        .addComponent(WLblBalance)
		                        .addComponent(WTFieldBalance)
			        		)
		        		)
			);
			
			
			add(WLblBalance);
			
			setLayout(layout);
			setBorder(BorderFactory.createLoweredSoftBevelBorder());
			
		}
		
		
		private void deleteWallet(){
			CALLBACK callbkDeleteWallet = new CALLBACK() {
				public void function(String[] retVal) {}
				public void function() {}
				
				@Override
				public void function(String retVal) {
					if(retVal == "YES"){
						shared.currentMemb.delWallet(wallet.wname);
						shared.Panel_center.view_wallet(shared.currentMemb);
					}
				}
			};
			
			Dialog dialogDeleteWalletConfirm = new Dialog(Dialog.TYPE_QUESTION, "Ar tikrai?");
			dialogDeleteWalletConfirm.setText("Ar tikrai norite ištrinti piniginę? Kartu bus pašalinti visi susieti planai ir piniginės operacijos");
			dialogDeleteWalletConfirm.setCallback(callbkDeleteWallet);
			dialogDeleteWalletConfirm.setVisible(true);
		}
		
		private void SeeWalletPlans(){
			view_planner();
			shared.Panel_center.PlannerPanel.loadWalletPlans(wallet);
		}
		

		
		class LocalAL implements ActionListener {

			Wallet W;
			LocalAL(Wallet W){
				this.W = W;
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				switch(e.getActionCommand()){
				
				case "MenuItem":
					JMenuItem mItem = (JMenuItem)e.getSource();
					
					int id = Integer.parseInt(mItem.getName());
					
					mItem = (JMenuItem) e.getSource();
					//System.out.println("MenuItem: ["+ mItems[id] + "]" + W.wname);
					F[id].run();
					break;
				
				}
				
			}
		};
		
		class PopupListener extends MouseAdapter {
		    public void mousePressed(MouseEvent e) {
		      maybeShowPopup(e);
		    }

		    public void mouseReleased(MouseEvent e) {
		      maybeShowPopup(e);
		    }

		    private void maybeShowPopup(MouseEvent e) {
		      if (e.isPopupTrigger())
		        popup.show(((JPanel) e.getComponent()), e.getX(), e.getY());
		    }
		  }
		
	}
	
	
	
	class FlowFilters extends JPanel{

		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4911439241051567687L;

		/** 	Sketch:
		 * 
		 * [x] [__before______[v]       [_YYYY_[v]  [_MM_[v]  [_DD_[v]
		 * 											[_hh_[v]  [_mm_[v]
		 * [ ] [__less_than___[v]       [__________________]
		 * [x] [__CATEG - fun_[v]
		 * [x] [__PLAN - May__[v]
		 * [x] O - income     0 - outcome
		 * 
		 *     [|< BACK|]     [|FILTER|]
		 *     
		 * */
		
		GroupLayout layout;
		
		Wallet wallet;
		com.planner.netikras.SharedClass.Dialog dialog;
		
		LocalAL AL;
		
		String[] strsYrs;
		String[] strsMths;
		String[] strDays;
		String[] strsHrs;
		String[] strsMins;
		
		Dimension dimPrefSzTINY;
		
		JPanel panRadios;
		
		
		
		JButton btnBack;
		JButton btnFilter;
		
		
		JCheckBox cbxEnableDateBefore;
		JCheckBox cbxEnableDateAfter;
		JCheckBox cbxEnableAmountLess;
		JCheckBox cbxEnableAmountMore;
		JCheckBox cbxEnableCategory;
		JCheckBox cbxEnablePlan;
		JCheckBox cbxEnableSign;
		
		JLabel lblDateBefore;
		JLabel lblDateAfter;
		JLabel lblAmountLess;
		JLabel lblAmountMore;
		JLabel lblCategory;
		JLabel lblPlan;
		

		JComboBox<String> ddnAmountLess;
		JComboBox<String> ddnAmountMore;
		protected JComboBox<String> ddnCateg; // dynamic
		protected JComboBox<String> ddnPlan;  // dynamic
		
		JTextField tfAmountLess;
		JTextField tfAmountMore;
		
		
		ButtonGroup  radioButtons;
		JRadioButton radioNegative;
		JRadioButton radioPositive;

		JComboBox<String>[] ddnsDATEbefore;
		JComboBox<String>[] ddnsDATEafter;
		
		FlowFilters(){
			AL = new LocalAL();
			
			dimPrefSzTINY = new Dimension(20, 20);
			
			tfAmountLess	= new JTextField();
			tfAmountMore	= new JTextField();
			
			initLBLs();
			initCBXs();
			initDDNs();
			initRADs();
			initBTNs();
			
			addCBXs();
			addDDNs();
			add(tfAmountLess);
			add(tfAmountMore);
			addRADs();
			addBTNs();
			
			
			initLayout();
			
			addLayout();
			
			CBXChecked(cbxEnableDateAfter	, true );
			CBXChecked(cbxEnableDateBefore	, true );
			CBXChecked(cbxEnableAmountMore	, false);
			CBXChecked(cbxEnableAmountLess	, false);
			CBXChecked(cbxEnableCategory	, false);
			CBXChecked(cbxEnablePlan		, false);
			CBXChecked(cbxEnableSign		, false);
						
		}
		
		class LocalAL implements ActionListener {

			LocalAL(){}
			@Override
			public void actionPerformed(ActionEvent e) {
				
				switch(e.getActionCommand()){
					case "cbx":
						JCheckBox cbx = (JCheckBox) e.getSource();
						boolean isChecked = cbx.isSelected();
						
						CBXChecked(cbx, isChecked);
						break;
					case "btn":
						JButton btn = (JButton)e.getSource();
						
						if(btn.getName() == "BACK"){
							setVisible(false);
						} else
							if(btn.getName() == "FILTER"){
								loadFilteredWallet();
								shared.Panel_center.tmMain.REFRESH_TABLE();
								//System.out.println(returnArray());
							}
						break;
				}
			}
			
		} 
		
		
		
		private void initLBLs(){
			lblDateBefore = new JLabel("Atlikti iki:");
			lblDateAfter  = new JLabel("Atlikti po :");
			
			lblAmountLess = new JLabel("Mažiau  nei:");
			lblAmountMore = new JLabel("Daugiau nei:");
			
			lblCategory	  = new JLabel("Kategorija:");
			lblPlan		  = new JLabel("Planas:");
		}
		
		
		private void initDDNs(){
			
			int years_from = 1950;
			int years_to = 2200;
			
			SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
			String[] today = formatter.format( new java.util.Date() ).split("-");

			int init_year = Integer.parseInt(today[0]) - years_from;
			int init_mnth = Integer.parseInt(today[1]) - 1;
			int init_day  = Integer.parseInt(today[2]);
			
			if(init_mnth == 0){
				init_mnth = 12;
				init_year -= 1;
			}

			ddnsDATEbefore = new JComboBox[]{
							new JComboBox<String>(), 
							new JComboBox<String>(), 
							new JComboBox<String>(), 
							new JComboBox<String>(), 
							new JComboBox<String>()
						};
			
			ddnsDATEafter = new JComboBox[]{
							new JComboBox<String>(), 
							new JComboBox<String>(), 
							new JComboBox<String>(), 
							new JComboBox<String>(), 
							new JComboBox<String>()
						};
			
			
			ddnCateg = new JComboBox<String>();
			ddnPlan = new JComboBox<String>();
			
			//YMD hm
			for(int i=years_from; i<years_to; i++) {
				ddnsDATEbefore[0].addItem(""+i); // YEARS
				ddnsDATEafter [0].addItem(""+i); // YEARS
			}
			
			
			String value;
			for(int i=0; i<60; i++){
				value = (i<10?"0":"")+i;
				
				if(i>0){
					if(i<13) {
						ddnsDATEbefore[1].addItem(value); // MONTHS
						ddnsDATEafter [1].addItem(value); // MONTHS
					}
					if(i<32) {
						ddnsDATEbefore[2].addItem(value); // DAYS
						ddnsDATEafter [2].addItem(value); // DAYS
					}
				} 
				
				if(i<24) {
					ddnsDATEbefore[3].addItem(value); // HOURS
					ddnsDATEafter[3].addItem(value); // HOURS
				}
				ddnsDATEbefore[4].addItem(value); // MINUTES
				ddnsDATEafter [4].addItem(value); // MINUTES
				
			}
			
			
			// Setting default selections
			ddnsDATEbefore[0].setSelectedIndex( Integer.parseInt(today[0]) - years_from );	// current year
			ddnsDATEafter [0].setSelectedIndex( init_year );								// current year, unless currently it's January. Then it's the last year
			
			ddnsDATEbefore[1].setSelectedIndex( Integer.parseInt(today[1])-1 ); 			// current month
			ddnsDATEafter [1].setSelectedIndex( init_mnth - 1);								// previous month
			
			ddnsDATEbefore[2].setSelectedIndex( Integer.parseInt(today[2]) -1 );			// current day
			ddnsDATEafter [2].setSelectedIndex( init_day - 1);								// current day last month
			
			ddnsDATEbefore[3].setSelectedIndex( ddnsDATEbefore[3].getItemCount() -1 );		// 11pm
			ddnsDATEafter [3].setSelectedIndex( 0 );										// 0pm
			
			ddnsDATEbefore[4].setSelectedIndex( ddnsDATEbefore[4].getItemCount()-1 );		// 59 minutes
			ddnsDATEafter [4].setSelectedIndex( 0 );										// 00 minutes
			
		}
		
		private void addDDNs(){
			add(lblDateBefore);
			add(lblDateAfter );
			add(lblAmountLess);
			add(lblAmountMore);
			
			for(JComboBox<String> DDN : ddnsDATEbefore){
				DDN.setPreferredSize(dimPrefSzTINY);
				add(DDN);
			}
			for(JComboBox<String> DDN : ddnsDATEafter){
				DDN.setPreferredSize(dimPrefSzTINY);
				add(DDN);
			}
		}
		
		
		
		private void initCBXs(){
			cbxEnableDateBefore	= new JCheckBox();
			cbxEnableDateAfter	= new JCheckBox();
			cbxEnableAmountLess	= new JCheckBox();
			cbxEnableAmountMore	= new JCheckBox();
			cbxEnableCategory 	= new JCheckBox();
			cbxEnablePlan 		= new JCheckBox();
			cbxEnableSign 		= new JCheckBox();
			
			cbxEnableDateBefore	.addActionListener(AL);
			cbxEnableDateAfter 	.addActionListener(AL);
			cbxEnableAmountLess	.addActionListener(AL);
			cbxEnableAmountMore .addActionListener(AL);
			cbxEnableCategory 	.addActionListener(AL);
			cbxEnablePlan 		.addActionListener(AL);
			cbxEnableSign 		.addActionListener(AL);
			
			cbxEnableDateBefore	.setActionCommand("cbx");
			cbxEnableDateAfter 	.setActionCommand("cbx");
			cbxEnableAmountLess	.setActionCommand("cbx");
			cbxEnableAmountMore	.setActionCommand("cbx");
			cbxEnableCategory 	.setActionCommand("cbx");
			cbxEnablePlan 		.setActionCommand("cbx");
			cbxEnableSign 		.setActionCommand("cbx");
			
			cbxEnableDateBefore	.setName("dateBefore");
			cbxEnableDateAfter	.setName("dateAfter");
			cbxEnableAmountLess	.setName("amountLess");
			cbxEnableAmountMore	.setName("amountMore");
			cbxEnableCategory 	.setName("category");
			cbxEnablePlan 		.setName("plan");
			cbxEnableSign 		.setName("sign");
			
		}
		
		private void addCBXs(){
			add(cbxEnableDateBefore	);
			add(cbxEnableDateAfter	);
			add(cbxEnableAmountLess	);
			add(cbxEnableAmountMore	);
			add(cbxEnableCategory 	);
			add(cbxEnablePlan 		);
			add(cbxEnableSign 		);
		}
		
		
		
		private void initRADs(){
			radioButtons  = new ButtonGroup();               
			radioNegative = new JRadioButton("Iš piniginės");
			radioPositive = new JRadioButton("Į piniginę");
						
			panRadios = new JPanel();
			panRadios.setLayout(new BoxLayout(panRadios, BoxLayout.PAGE_AXIS));
			
			radioPositive.setSelected(true);
			
			radioButtons.add(radioNegative);
			radioButtons.add(radioPositive);
			
			panRadios.add(radioNegative);
			panRadios.add(radioPositive);
		}

		private void addRADs(){
			add(panRadios);
		}
		
		
		
		private void initBTNs(){
			btnBack   = new JButton("Slėpti");
			btnFilter = new JButton("Filtruoti");
			
			btnBack  .addActionListener(AL);
			btnFilter.addActionListener(AL);
			
			btnBack  .setActionCommand("btn");
			btnFilter.setActionCommand("btn");
			
			btnBack  .setName("BACK");
			btnFilter.setName("FILTER");
			
			
		}

		private void addBTNs(){
			add(btnBack);
			add(btnFilter);
		}
		
		
		
		private void initLayout(){
			
			layout = new GroupLayout(this);
			
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						// Col #0 - Checkboxes
						.addComponent(cbxEnableDateAfter)
						.addComponent(cbxEnableDateBefore)
						.addComponent(cbxEnableAmountMore)
						.addComponent(cbxEnableAmountLess)
						.addComponent(cbxEnableCategory)
						.addComponent(cbxEnablePlan)
						.addComponent(cbxEnableSign)
					)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						// Col #1 - labels, radios
						.addComponent(lblDateAfter)
						.addComponent(lblDateBefore)
						.addComponent(lblAmountMore)
						.addComponent(lblAmountLess)
						.addComponent(lblCategory)
						.addComponent(lblPlan)
						.addComponent(panRadios)
						.addComponent(btnBack)
					)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						// Col #2 - YYYY dropdowns, textfields, Cat/Plan dropdowns
						.addComponent(ddnsDATEafter [0])
						.addComponent(ddnsDATEbefore[0])
						.addComponent(tfAmountMore)
						.addComponent(tfAmountLess)
						.addComponent(ddnCateg)
						.addComponent(ddnPlan)
						.addComponent(btnFilter)

					)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						// Col #3 - MM/hh dropdowns only
						.addComponent(ddnsDATEafter [1])//, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(ddnsDATEafter [3])
						.addComponent(ddnsDATEbefore[1])
						.addComponent(ddnsDATEbefore[3])
					)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						// Col #4 - DD/mm dropdowns only
						.addComponent(ddnsDATEafter [2])
						.addComponent(ddnsDATEafter [4])
						.addComponent(ddnsDATEbefore[2])
						.addComponent(ddnsDATEbefore[4])
					)
			);
			
			layout.setVerticalGroup(
					layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// Row #1 - Date After Line #1
							.addComponent(cbxEnableDateAfter)
							.addComponent(lblDateAfter)
							.addComponent(ddnsDATEafter[0])
							.addComponent(ddnsDATEafter[1])
							.addComponent(ddnsDATEafter[2])
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// SemiRow #1 - Date After Line #2
							.addComponent(ddnsDATEafter[3])
							.addComponent(ddnsDATEafter[4])
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// Row #2 - Date Before line #1
							.addComponent(cbxEnableDateBefore)
							.addComponent(lblDateBefore)
							.addComponent(ddnsDATEbefore[0])
							.addComponent(ddnsDATEbefore[1])
							.addComponent(ddnsDATEbefore[2])
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// SemiRow #2 - Date Before line #2
							.addComponent(ddnsDATEbefore[3])
							.addComponent(ddnsDATEbefore[4])
						)
						
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// Row #4 - Amount more than
							.addComponent(cbxEnableAmountMore)
							.addComponent(lblAmountMore)
							.addComponent(tfAmountMore)
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// Row #3 - Amount less than
							.addComponent(cbxEnableAmountLess)
							.addComponent(lblAmountLess)
							.addComponent(tfAmountLess)
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// Row #5 - Category
							.addComponent(lblCategory)
							.addComponent(cbxEnableCategory)
							.addComponent(ddnCateg)
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// Row #6 - Plan
							.addComponent(lblPlan)
							.addComponent(cbxEnablePlan)
							.addComponent(ddnPlan)
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// Row #7 - Positive/negative sign
							.addComponent(cbxEnableSign)
							.addComponent(panRadios)
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// Row #8 - Buttons
							.addComponent(cbxEnableSign)
							.addComponent(panRadios)
							.addComponent(btnBack)
							.addComponent(btnFilter)
						)
				);
			
		}

		private void addLayout(){
			setLayout(layout);
		}

		
		private void CBXChecked(JCheckBox cbx, boolean isChecked){
			
			cbx.setSelected(isChecked);
			
			switch(cbx.getName()){
				case "dateBefore":
					//ddnDate.setEnabled(isChecked);
					for(JComboBox<String> D : ddnsDATEbefore)
						D.setEnabled(isChecked);
					lblDateBefore.setEnabled(isChecked);
					break;
				case "dateAfter":
					//ddnDate.setEnabled(isChecked);
					for(JComboBox<String> D : ddnsDATEafter)
						D.setEnabled(isChecked);
					lblDateAfter.setEnabled(isChecked);
					break;
				case "amountLess":
					//ddnAmount.setEnabled(isChecked);
					tfAmountLess .setEnabled(isChecked);
					lblAmountLess.setEnabled(isChecked);
					break;
				case "amountMore":
					//ddnAmount.setEnabled(isChecked);
					tfAmountMore .setEnabled(isChecked);
					lblAmountMore.setEnabled(isChecked);
					break;
				case "category":
					ddnCateg.setEnabled(isChecked);
					lblCategory.setEnabled(isChecked);
					break;
				case "plan":
					//if(wallet != null && wallet.plans != null)
					//	for(Plan P: wallet.plans)
					//		ddnCateg.addItem(P.name);
					ddnPlan.setEnabled(isChecked);
					lblPlan.setEnabled(isChecked);
					break;
				case "sign":
					radioNegative.setEnabled(isChecked);
					radioPositive.setEnabled(isChecked);
					break;
		}
		}
		
		
		protected void assignWallet(Wallet wallet){
			this.wallet = wallet;
		}
		
		protected void loadFilteredWallet(){
			ReturnSet retSet = new ReturnSet();
			String value;
			String[] retVal = new String[7];
			
			String[][] FILTER_RULE;
			
			if(wallet == null) return;
			
			if(cbxEnableDateAfter.isSelected()){
				
				value =  ddnsDATEafter[0].getSelectedItem().toString();
				value += ddnsDATEafter[1].getSelectedItem().toString();
				value += ddnsDATEafter[2].getSelectedItem().toString();
				value += ddnsDATEafter[3].getSelectedItem().toString();
				value += ddnsDATEafter[4].getSelectedItem().toString();
				
				retVal[0] = value;
			}

			if(cbxEnableDateBefore.isSelected()){
				
				value =  ddnsDATEbefore[0].getSelectedItem().toString();
				value += ddnsDATEbefore[1].getSelectedItem().toString();
				value += ddnsDATEbefore[2].getSelectedItem().toString();
				value += ddnsDATEbefore[3].getSelectedItem().toString();
				value += ddnsDATEbefore[4].getSelectedItem().toString();
				
				retVal[1] = value;
			}
			
			if(cbxEnableAmountMore.isSelected()){
				
				value = tfAmountMore.getText().trim().replace(',', '.');
				
				retVal[2] = value;
			}
			
			if(cbxEnableAmountLess.isSelected()){
				
				value = tfAmountLess.getText().trim().replace(',', '.');
				
				retVal[3] = value;
				
			}
						
			if(cbxEnableCategory.isSelected()){
				retVal[4] = ddnCateg.getSelectedItem().toString();
			}
			
			if(cbxEnablePlan.isSelected()){
				
				retVal[5] = ddnPlan.getSelectedItem().toString().equals(wallet.NO_PLAN)?"NULL":shared.currentMemb.mname_full+shared.user.DB.delimiter+ddnPlan.getSelectedItem().toString();
			}
			
			if(cbxEnableSign.isSelected()){
				
				retVal[6] = radioNegative.isSelected()?"1":"0";
			}
			
			
			
			FILTER_RULE = new String[][]{
						{wallet.FILTER_AFTER	, retVal[0]},
						{wallet.FILTER_BEFORE	, retVal[1]},
						{wallet.FILTER_MORE_THAN, retVal[2]},
						{wallet.FILTER_LESS_THAN, retVal[3]},
						{wallet.FILTER_CATEGORY	, retVal[4]},
						{wallet.FILTER_PLAN		, retVal[5]},
						{wallet.FILTER_SIGN_BIT	, retVal[6]}
					};
			
			retSet = wallet.loadWalletFlows(FILTER_RULE);
			
			if(retSet.getCode() != 0){
				dialog = new com.planner.netikras.SharedClass.Dialog(com.planner.netikras.SharedClass.Dialog.TYPE_ERR, "Klaida");
				dialog.setText("Klaida filtruojant:\n\n" + retSet.getMessage());
				dialog.setVisible(true);
			}
			
			//return retVal;
		}

		
	}
	
	
	protected PanelCenterClass loggedOut(){
		view_login_screen();
		return this;
	}
	protected PanelCenterClass loggedIn(){
		view_main();
		return this;
	}
}



	class PlannerClass extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 23176702980916809L;
		private SharedClass	shared;
		private Plan[]		plans;
		private JButton		btnNewPlan;
		private JButton		btnDelPlan;
		
		protected JComboBox<String> dropdownPlansList = new JComboBox<String>();
		
		
		private String[]	label_L_text = {
										"Nuo:",
										"Iki:",
										"Planuoj. išlaidos:",
										"Planuoj. pajamos :",
										"Planuoj. balansas:",
										"Trukmė (d.):"
										};
		private String[]	label_R_text = {
										"Praėjo (d.):",
										"Liko   (d.):",
										"Išlaidos:",
										"Pajamos :",
										"Balansas:",
										"Vidut. išl./d.:",
										"Vidut. paj./d.:",
										"Prognozė (išl.):",
										"Prognozė (paj.):",
										};
		
		private JLabel[] 		LABELS_L;
		private JTextField[]	TFIELDS_L;
		private JLabel[] 		LABELS_R;
		private JTextField[]	TFIELDS_R;
		
		PlannerClass(SharedClass shared){
			this.shared = shared; 
			
			LABELS_L = new JLabel[label_L_text.length];
			LABELS_R = new JLabel[label_R_text.length];
			TFIELDS_L = new JTextField[label_L_text.length];
			TFIELDS_R = new JTextField[label_R_text.length];
			
			for(int i=0; i<label_L_text.length; i++){
				LABELS_L[i]  = new JLabel(label_L_text[i]);
				TFIELDS_L[i] = new JTextField();
				
				TFIELDS_L[i].setEditable(false);
			}
			
			for(int i=0; i<label_R_text.length; i++){
				LABELS_R[i]  = new JLabel(label_R_text[i]);
				TFIELDS_R[i] = new JTextField();
				
				TFIELDS_R[i].setEditable(false);
			}
			
			btnNewPlan = new JButton("Kurti naują");
			btnNewPlan.setActionCommand("ButtonClick");
			btnNewPlan.setName("newPlan");
			btnNewPlan.addActionListener(shared.aListener);
			
			btnDelPlan = new JButton("Naikinti");
			btnDelPlan.setActionCommand("ButtonClick");
			btnDelPlan.setName("delPlan");
			btnDelPlan.addActionListener(shared.aListener);
			
			dropdownPlansList.addActionListener( new ActionListener() {@Override public void actionPerformed(ActionEvent e) {FillInPlan();} } );
			
			AssembleForm();
			
		}
		
		protected void loadWalletPlans(Wallet wlt){
			System.out.println("Loading single wallet plans " + wlt.wname);
			plans=null;
			dropdownPlansList.removeAllItems();
			
			wlt.loadWalletPlans();
			plans = wlt.plans;
			if(plans != null)
				for(int i=0; i<plans.length; i++)
					dropdownPlansList.addItem(plans[i].name);
		}
		
		protected void loadAllMemberPlans(Member memb){
			plans=null;
			dropdownPlansList.removeAllItems();
			System.out.println("Loading multiple wallet plans ");
			if(memb.wallets != null && memb.wallets.length > 0){
				for(Wallet W : memb.wallets){
					W.loadWalletPlans();
					if(W.plans != null) 
						for(int i=0; i<W.plans.length; i++) {
							dropdownPlansList.addItem(W.plans[i].name);
							Object[] tempArr = shared.addToArrayEnd(this.plans, W.plans[i]);
							this.plans = Arrays.copyOf(tempArr, tempArr.length, Plan[].class);
						}
				}
			}
		}
		
		
		private void AssembleForm(){
			GroupLayout layout = new GroupLayout(this);
			
			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);
			
			/*-------------Creating horizontal groups------------*/
			ParallelGroup parallelGroupL_labels = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			for(JLabel item : LABELS_L)
				parallelGroupL_labels.addComponent(item);
			
			ParallelGroup parallelGroupR_labels = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			for(JLabel item : LABELS_R)
				parallelGroupR_labels.addComponent(item);
			
			ParallelGroup parallelGroupL_tfields = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			for(JTextField item : TFIELDS_L)
				parallelGroupL_tfields.addComponent(item);
			
			ParallelGroup parallelGroupR_tfields = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			for(JTextField item : TFIELDS_R)
				parallelGroupR_tfields.addComponent(item);
			
			
			/*-------------Creating vertical groups------------*/
			
			SequentialGroup seqGroupVertical = layout.createSequentialGroup();
			seqGroupVertical.addComponent(dropdownPlansList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);

			int len = (LABELS_L.length > LABELS_R.length?LABELS_L.length:LABELS_R.length); // that many parallel groups we will have
			
			ParallelGroup[] parallGroupsVert = new ParallelGroup[len];
			for(int i=0; i<len; i++){
				parallGroupsVert[i] = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
				
				if(i < LABELS_L.length){
					parallGroupsVert[i].addComponent(LABELS_L[i]);
					parallGroupsVert[i].addComponent(TFIELDS_L[i]);
				}
				
				if(i < LABELS_R.length){
					parallGroupsVert[i].addComponent(LABELS_R[i]);
					parallGroupsVert[i].addComponent(TFIELDS_R[i]);
				}
			}
			
			for(int i=0; i<parallGroupsVert.length; i++){
				seqGroupVertical.addGroup(parallGroupsVert[i]);
			}
			seqGroupVertical.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnNewPlan)
						.addComponent(btnDelPlan)
				);
			
			
			
			
			/*-------------Adding groups to layout------------*/
			layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(dropdownPlansList)
					.addGroup(layout.createSequentialGroup()
							.addGroup(parallelGroupL_labels)
							.addGroup(parallelGroupL_tfields
								.addComponent(btnNewPlan)
							)
						
							.addGroup(parallelGroupR_labels)
							.addGroup(parallelGroupR_tfields
								.addComponent(btnDelPlan)
							)
					)
				)
			);
			
			layout.setVerticalGroup(seqGroupVertical);			
			
			
			setLayout(layout);
		}
		
		protected void FillInPlan(){
			if(dropdownPlansList.getItemCount() < 1){return;}
			
			String PlanName = dropdownPlansList.getSelectedItem().toString();
			DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
			Date TODAY = new Date();
			//Date START;
			//Date END;
			int duration = 0, started_days_ago=0, ends_days_after=0, days_passed_perc=0, days_left_perc=0;
			double spent=0, earned=0, balance=0, spent_perc=0, earned_perc=0, balance_perc=0, earned_per_day=0, spent_per_day=0, est_out=0, est_in=0, est_balance=0;
			Calendar calNow = Calendar.getInstance();
			calNow.setTime(TODAY);
			Calendar calStarted;
			Calendar calEnds;
			Calendar calYday;
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			if(plans == null) {
				for(JTextField TF : TFIELDS_L)
					TF.setText("");
				for(JTextField TF : TFIELDS_R)
					TF.setText("");
				return;
			}
			
			for(Plan P : plans){
				
				if(P.name.equals(PlanName)){
					calStarted = Calendar.getInstance();
					calEnds = Calendar.getInstance();
					calYday = calNow.getInstance();
					calYday.add(Calendar.DAY_OF_MONTH, -1); // exclude today

					
					if(P.start == "-"){
						calStarted = Calendar.getInstance();
						calEnds    = calStarted;
						
						duration = 0;
						
						started_days_ago = 0; 
						ends_days_after  = 0;
						
						days_passed_perc = 0;
						days_left_perc   = 0;
						
						
						earned  = countUnplannedFlowsAmount(false);
						spent   = countUnplannedFlowsAmount(true );
						balance = earned - spent;
						balance_perc = (earned - spent) / Double.parseDouble(P.est_bal)*100;
						
						earned_per_day = 0;
						spent_per_day  = 0;
						
						spent_perc  = 0;
						earned_perc = 0;
						
					} else {
						
						try {
							calStarted = Calendar.getInstance();
							calStarted.setTime(dateFormatter.parse(P.start));
							calEnds = Calendar.getInstance();
							calEnds.setTime(dateFormatter.parse(P.end));
							
							duration = (int) shared.daysBetween(calStarted, calEnds);
							
							started_days_ago = (int) shared.daysBetween(calStarted, calYday);
							ends_days_after  = (int) shared.daysBetween(calNow,     calEnds);
							
							days_passed_perc = duration>0?started_days_ago*100/duration:0;
							days_passed_perc = days_passed_perc > 100?100:days_passed_perc;
							
							days_left_perc = duration>0?ends_days_after *100/duration:0;
							days_left_perc = days_left_perc > 100? 100 : days_left_perc; 
							
						} catch (ParseException e) {
							System.out.println("Date conversion error: " + e.getMessage());
						}
						
						if(P.type.equals(Wallet.PLAN_TYPE_OLY)){
							earned = countPlanOLYFlowsAmount(false);
							spent  = countPlanOLYFlowsAmount(true );
						} else {
							earned = countPlanALLFlowsAmount(false, P.start, P.end);
							spent  = countPlanALLFlowsAmount(true,  P.start, P.end);
						}
						balance = earned - spent;
						balance_perc = (earned - spent) / Double.parseDouble(P.est_bal)*100;
						
						earned_per_day = started_days_ago>0?earned/started_days_ago:earned;
						spent_per_day  = started_days_ago>0?spent /started_days_ago:spent;
						
						est_out = Double.parseDouble(P.est_out);
						est_in  = Double.parseDouble(P.est_in );
						
						spent_perc = est_out>0?spent * 100 / est_out :100;
						spent_perc = spent_perc > 100? 100 : spent_perc;
						
						earned_perc = est_in>0?earned * 100 / est_in :100;
						earned_perc = earned_perc > 100? 100 : earned_perc;
					
					}
					
				
					
					
							
					TFIELDS_L[0].setText(P.start);
					TFIELDS_L[1].setText(P.end);
					TFIELDS_L[2].setText(P.est_out);
					TFIELDS_L[3].setText(P.est_in);
					TFIELDS_L[4].setText(P.est_bal);
					TFIELDS_L[5].setText(""+duration);
					
					TFIELDS_R[0].setText(""+started_days_ago + " (" + df.format(days_passed_perc)+"%)"); // praėjo dienų
					TFIELDS_R[1].setText(""+ends_days_after  + " (" + df.format(days_left_perc  )+"%)"); // liko dienų
					TFIELDS_R[2].setText(""+spent  + " (" +df.format(spent_perc  )+ "%)"); // išlaidos
					TFIELDS_R[3].setText(""+earned + " (" +df.format(earned_perc )+ "%)"); // pajamos
					TFIELDS_R[4].setText(""+balance+ " (" +df.format(balance_perc)+ "%)"); // balansas
					TFIELDS_R[5].setText(""+df.format(spent_per_day )); // išl. per diena
					TFIELDS_R[6].setText(""+df.format(earned_per_day)); // paj. per dieną
					TFIELDS_R[7].setText(""+df.format(spent_per_day*ends_days_after)); // išl. prognozė
					TFIELDS_R[8].setText(""+df.format(earned_per_day*ends_days_after)); // paj. prognozė
					
					
					return;
				}
			}
			
		}
		
		
		private long countPlanOLYFlowsAmount(boolean negative){
			QryBuilder qry = new QryBuilder();
			String query;
			
			boolean dummyPlan = false;
			String dummyWalletName = "";
			
			for(Wallet wallet : shared.currentMemb.wallets) {
				if(wallet.NO_PLAN.equals(dropdownPlansList.getSelectedItem().toString())){ 
					dummyPlan=true;
					dummyWalletName = wallet.wname_full;
				}
			}
			
			if(dummyPlan)
				query = qry.select(DB_METADATA.col_FLOW_amount).from(DB_METADATA.tbl_FLOW_name)
				.where()
							.x_eq_y(DB_METADATA.col_PLAN_name, "NULL")
					.and()	.x_eq_y(DB_METADATA.col_WALLET_name, dummyWalletName)
					.and()	.x_eq_y(DB_METADATA.col_FLOW_negative, negative?"1":"0").pack();
			else
				query = qry.select(DB_METADATA.col_FLOW_amount).from(DB_METADATA.tbl_FLOW_name)
						.where()
									.x_eq_y(DB_METADATA.col_PLAN_name, shared.currentMemb.mname_full + shared.user.DB.delimiter + dropdownPlansList.getSelectedItem().toString())
							.and()	.x_eq_y(DB_METADATA.col_FLOW_negative, negative?"1":"0").pack();
			//System.out.println(query);
			return countFlowsAmount(query);
		}
		
		private long countPlanALLFlowsAmount(boolean negative, String after_yyyyMMdd, String before_yyyyMMdd){
			QryBuilder qry = new QryBuilder();
			String query;
			
			query = qry.select(DB_METADATA.col_FLOW_amount).from(DB_METADATA.tbl_FLOW_name)
				.where()
							.x_eq_y(DB_METADATA.col_MEMBER_name, shared.currentMemb.mname_full)
							.x_ge_y(DB_METADATA.col_FLOW_date, after_yyyyMMdd+"0000")
					.and()	.x_le_y(DB_METADATA.col_FLOW_date, before_yyyyMMdd+"0000")
					.and()	.x_eq_y(DB_METADATA.col_FLOW_negative, negative?"1":"0")
				.pack();
			//System.out.println(query);
			return countFlowsAmount(query);
		
		}
		
		private long countUnplannedFlowsAmount(boolean negative){
			QryBuilder qry = new QryBuilder();
			String query;
			
			query = qry.select(DB_METADATA.col_FLOW_amount).from(DB_METADATA.tbl_FLOW_name)
				.where()
						  .x_eq_y(DB_METADATA.col_MEMBER_name, shared.currentMemb.mname_full)
					.and().x_eq_y(DB_METADATA.col_FLOW_negative, negative?"1":"0")
					.and().bracket_L()
						.x_eq_y(DB_METADATA.col_PLAN_name, "NULL")
						.or().x_eq_y(DB_METADATA.col_PLAN_name, null)
						.bracket_R()
				.pack();
			System.out.println(query);
			return countFlowsAmount(query);
		
		}
		
		private long countFlowsAmount(String query){
			long amount = 0;
			ResultSet result;
			
			
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
