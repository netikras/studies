
using System;
using System.Text;
//using System.Windows;
using System.Net;
using System.Net.Sockets;
//using System.Windows.Controls;
//using System.Threading;
using System.Threading;
//using System.Windows.Threading;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;


namespace Padavėjai
{
    partial class Padavejai
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }



        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        /// 



        private SocketPermission permission;
        private Socket sListener;
        private IPEndPoint ipEndPoint;
        private Socket handler;

        private Panel pMAIN;
        private Panel pTopButtons;
        private Panel pWorkSpace;
        private Panel pClients;
        private Panel pDetails;

        private int tablesCount=0;
        private int zonesCount=0;
        private int tablesPerZone=0;
        private int masterPORT=0;
        private bool singleZoneMode=false;
        private int zoneID = 0;
        private string masterIP="";



        private Button bNULL;
        private Font myFont;
        private Color bDefaultColor;
        private int sizeW;
        private int sizeH;
        private int spacing;
        private string pathSeparator;
        private string NewLine;

        private Color myColorBlue = Color.Blue;
        private Color myColorGreen = Color.Green;
        private Color myColorRed = Color.Red;
        private Color myColorNormal = Color.White;
        private Color myColorLeaving = Color.DeepSkyBlue;


        private TextBox[] tbClient;
        private Button[] bViewClient;
        private Button[] bZones;
        private TextBox[] tbTitleBar;
        private TextBox tbTotalPrice;
        private Button bDisarm;
        private Button bPay;
        private Button bReset;

        private ZONE[] ZONES_LIST;

        //private string clientIP = "192.168.1.12";
        //private int clientPORT = 1999;

        private TextBox tbStatus;


        protected void setEnvironment(){
            sizeH = Screen.PrimaryScreen.Bounds.Height;
            sizeW = Screen.PrimaryScreen.Bounds.Width;
            spacing = sizeW / 60;
            bNULL = new Button();
            bNULL.Visible = false;
            myFont = new Font(bNULL.Font.Name, bNULL.Font.SizeInPoints * 3 / 2);
            bDefaultColor = bNULL.BackColor;
            //MessageBox.Show(bNULL.BackColor.Name);
            pathSeparator = System.IO.Path.DirectorySeparatorChar.ToString();
            NewLine = Environment.NewLine;

            myColorNormal = this.BackColor;

            string ERRMSG = "";
            string[] config = null;

            // reading configuration file
            try
            {
                config = System.IO.File.ReadAllLines("config.txt");
            }
            catch (System.IO.IOException e) {
                ERRMSG += e.ToString();
                MessageBox.Show("ERROR: " + ERRMSG);
                Environment.Exit(1);
            }
            string[] configSub;
            for (int i = 0; i < config.Length; i++ ) {
                configSub = config[i].Split('=');
                if (configSub.Length == 2){
                    switch (configSub[0]){
                        case "TablesInZone": tablesPerZone = Convert.ToInt32(configSub[1]); break;
                        case "ZonesCount": zonesCount = Convert.ToInt32(configSub[1]); break;
                        case "ZoneID": zoneID = Convert.ToInt32(configSub[1]); break;
                        case "MasterPORT": masterPORT = Convert.ToInt32(configSub[1]); break;
                        case "StaticMasterIP": masterIP = configSub[1]; break;
                        case "SingleZoneMode": try { singleZoneMode = Convert.ToBoolean(configSub[1]); }
                            catch (Exception e) { MessageBox.Show("Nesuprantu 'SingleZoneMode' reikšmės '"+configSub[1]+ "'"+NewLine+NewLine + e.ToString()); }; break;
                        default: break;
                    }
                }
            }

            if (
                (zonesCount == 0 && singleZoneMode == false) ||
                (zoneID == 0 && singleZoneMode == true) || 
                tablesPerZone == 0 || 
                masterPORT == 0 /*||
                staticMasterIP == ""*/
                ) {
                ERRMSG += "review configuration file. Vital variables currently are:" +
                    "\n\tSingleZoneMode=" + singleZoneMode +
                    "\n\tZonesCount=" + zonesCount +
                    "\n\tTablesInZone=" + tablesPerZone +
                    "\n\tMasterPORT=" + masterPORT +
                    "\n\tstaticMasterIP=" + masterIP +
                    "\n\tZoneID=" + zoneID +
                    "";
                MessageBox.Show("ERROR: " + ERRMSG);
                Environment.Exit(1);
            }
            tablesCount = zonesCount * tablesPerZone;
            tbClient = new TextBox[tablesPerZone];
            bViewClient = new Button[tablesPerZone];
            if (!singleZoneMode) bZones = new Button[zonesCount];
                else zonesCount = 1;
            ZONES_LIST = new ZONE[zonesCount];
        }

        protected void InitializeComponent()
        {
            setEnvironment();



            this.SuspendLayout();
            // 
            // Padavejai
            // 
            //this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.FormBorderStyle = FormBorderStyle.None;
            this.ClientSize = new Size(sizeW, sizeH);
            this.Name = "Padavejai";
            this.Text = "Padavėjai";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            

            this.pMAIN = new Panel();

            pMAIN.Size = this.Size;
            pMAIN.Parent = this;
            pMAIN.Location = new Point(0,0);
            pMAIN.AutoScroll = true;


            tbStatus = new TextBox();
            tbStatus.Parent = this.pMAIN;
            tbStatus.Location = new Point(spacing, spacing);
            tbStatus.Size = new Size(spacing * 10, spacing * 5);
            tbStatus.Text = "lala";
            tbStatus.Multiline = true;

            Button buttn = new Button();
            buttn.Text = "Connect";
            buttn.Location = new Point(tbStatus.Location.X + tbStatus.Size.Width + 10, tbStatus.Location.Y + tbStatus.Size.Height + 10);
            //buttn.Click += Create_Socket;
            buttn.Parent = pMAIN;
            buttn.Visible = false;
            tbStatus.Visible = false;





            pTopButtons = new Panel();
            pTopButtons.Parent = pMAIN;
            pTopButtons.Location = new Point(spacing, spacing);
            pTopButtons.Size = new Size(pMAIN.Size.Width - spacing*2, pMAIN.Size.Height/6);
            pTopButtons.BorderStyle = BorderStyle.Fixed3D;

            pWorkSpace = new Panel();
            pWorkSpace.Parent = pMAIN;
            pWorkSpace.Location = new Point(pTopButtons.Location.X, 
                pTopButtons.Location.Y + pTopButtons.Size.Height + spacing*3);
            pWorkSpace.Size = new Size(pTopButtons.Size.Width, 
                pMAIN.Size.Height - pWorkSpace.Location.Y - spacing);
            pWorkSpace.BorderStyle = BorderStyle.Fixed3D;

            pClients = new Panel();
            pClients.Parent = pWorkSpace;
            pClients.Location = new Point(spacing, spacing);
            pClients.Size = new Size(pWorkSpace.Size.Width/3 - spacing*2,
                pWorkSpace.Size.Height-spacing*2);
            pClients.BorderStyle = BorderStyle.Fixed3D;
            pClients.AutoScroll = true;



            for (int i = 0; i < zonesCount; i++)
            {
                if(zonesCount > 1) ZONES_LIST[i] = new ZONE(i, tablesCount);
                else               ZONES_LIST[i] = new ZONE(zoneID-1, tablesCount);
                bZones[i] = new Button();
                bZones[i].Parent = pTopButtons;
                bZones[i].Tag = i;
                bZones[i].Click += clicked_bZones;
                bZones[i].Location = new Point(pTopButtons.Size.Width / zonesCount * (i) + spacing / 2, spacing);
                bZones[i].Size =
                    i == 0 ?
                    new Size(pTopButtons.Size.Width / zonesCount - spacing,
                        pTopButtons.Size.Height - spacing * 2)
                    : bZones[i - 1].Size;
                //bZones[i].Text = "Zona #" + (i + 1);
                bZones[i].Text = "Zona #" + (ZONES_LIST[i].getZoneID()+1);
                bZones[i].TextAlign = ContentAlignment.MiddleCenter;
                bZones[i].Font = myFont;
            }




            pDetails = new Panel();
            pDetails.Parent = pWorkSpace;
            pDetails.Location = new Point(pClients.Location.X + pClients.Size.Width + spacing,
                pClients.Location.Y);
            pDetails.Size = new Size(pWorkSpace.Size.Width - pDetails.Location.X - spacing,
                pClients.Size.Height - bZones[0].Size.Height - spacing);
            pDetails.BorderStyle = BorderStyle.Fixed3D;
            pDetails.AutoScroll = true;

            //pDetails.Visible = false;

                    //private TextBox tbTotalPrice;
                    //private Button bDisarm;
                    //private Button bPay;
            tbTotalPrice = new TextBox();
            tbTotalPrice.Parent = pWorkSpace;
            tbTotalPrice.Font = myFont;
            //tbTotalPrice.Location = new Point();

            tbTotalPrice.Location = new Point(pWorkSpace.Size.Width / 6*5 - spacing/2,
                pDetails.Size.Height + spacing + spacing/2);
            tbTotalPrice.Size = new Size( pWorkSpace.Size.Width - tbTotalPrice.Location.X - spacing ,
                spacing);

            tbTotalPrice.BackColor = this.BackColor;

            bPay = new Button();
            bPay.Parent = pWorkSpace;
            bPay.Location = new Point(tbTotalPrice.Location.X, 
                tbTotalPrice.Location.Y + tbTotalPrice.Size.Height + spacing/2);
            bPay.Size = new Size(tbTotalPrice.Size.Width,
                pWorkSpace.Size.Height - bPay.Location.Y - spacing/2);
            bPay.Enabled = false;
            bPay.Text = "Apmokėti";
            bPay.Font = myFont;
            bPay.Click += clicked_bPay;

            bDisarm = new Button();
            bDisarm.Parent = pWorkSpace;
            bDisarm.Size = new Size(bPay.Size.Width,
                bPay.Size.Height + spacing/2 + tbTotalPrice.Size.Height);
            bDisarm.Location = new Point(bPay.Location.X - spacing - bDisarm.Size.Width,
                tbTotalPrice.Location.Y);
            bDisarm.Text = "Išj. skambutį";
            bDisarm.Font = myFont;
            bDisarm.Enabled = false;
            bDisarm.Click += clicked_bDisarm;

            bReset = new Button();
            bReset.Parent = pWorkSpace;
            bReset.Location = new Point(bDisarm.Location.X - spacing - bDisarm.Size.Width,
                bDisarm.Location.Y);
            bReset.Size = bDisarm.Size;
            bReset.Font = myFont;
            bReset.Text = "Išvalyti staliuką";
            bReset.Enabled = false;
            bReset.Click += clicked_bReset;


                for (int i = 0; i < tablesPerZone; i++)
                {

                    tbClient[i] = new TextBox();
                    tbClient[i].Parent = pClients;
                    tbClient[i].Location = new Point(
                        spacing / 2,
                        i == 0 ? spacing / 2 : tbClient[i - 1].Location.Y + tbClient[i - 1].Size.Height);

                    tbClient[i].Size =
                        i == 0 ? new Size(pClients.Size.Width / 4 * 3 - spacing - SystemInformation.VerticalScrollBarWidth,
                            tbClient[i].Size.Height * 2)
                        : tbClient[i - 1].Size;
                    tbClient[i].BackColor = pClients.BackColor;
                    tbClient[i].ReadOnly = true;
                    tbClient[i].Multiline = true;
                    tbClient[i].Font = myFont;
                    tbClient[i].Text = "#" + (i + 1);

                    bViewClient[i] = new Button();
                    bViewClient[i].Tag = i;
                    bViewClient[i].Parent = pClients;
                    bViewClient[i].Location = new Point(spacing + tbClient[i].Size.Width,
                        tbClient[i].Location.Y);
                    bViewClient[i].Size =
                        i == 0
                        ? new Size(pClients.Size.Width - bViewClient[i].Location.X - spacing / 2 - SystemInformation.VerticalScrollBarWidth,
                            tbClient[i].Size.Height)
                        : bViewClient[i - 1].Size;
                    bViewClient[i].Text = "Rodyti";
                    bViewClient[i].TextAlign = ContentAlignment.MiddleCenter;
                    bViewClient[i].Font = myFont;
                    bViewClient[i].Click += clicked_bViewClient;
                }

                Create_Socket();

        }



        protected class ZONE {
            private int ZoneID;
            private int TablesCount;
            private TABLE[] TABLE_LIST;

            // CONSTRUCTOR
            public ZONE(int ZoneID, int TablesCount) {
                this.ZoneID = ZoneID;
                this.TablesCount = TablesCount;

                TABLE_LIST = new TABLE[TablesCount];

                for(int i=0; i<TablesCount; i++){
                    TABLE_LIST[i] = new TABLE(i+1);
                }

            }

            public int getZoneID() {
                return this.ZoneID;
            }

            public void addOrder(int TableID, string meal, double price, int quantity) {
                TABLE_LIST[TableID].addOrder(meal,price,quantity);
            }

            public string[] getTableOrder(int TableID, int OrderID) {
                return TABLE_LIST[TableID].getOrderDetailsString(OrderID);
            }

            public int getOrdersCount(int TableID) {
                return TABLE_LIST[TableID].getOrdersCount();
            }

            public double getTablePrice(int TableID) { 
                return TABLE_LIST[TableID].getTotalSum();
            }

            public string getTableState(int TableID){return TABLE_LIST[TableID].TableState();}

            public void setTableStateIDLE(int TableID) {TABLE_LIST[TableID].TableState("idle");}
            public void setTableStateBUSY(int TableID) { TABLE_LIST[TableID].TableState("busy"); }
            public void setTableStatePAY(int TableID) { TABLE_LIST[TableID].TableState("pay"); }
            public void setTableStateALARM(int TableID) { TABLE_LIST[TableID].TableState("alarm"); }//stateLEAVING
            public void setTableStateLEAVING(int TableID) { TABLE_LIST[TableID].TableState("leaving"); }
            public void setTableStatePREVIOUS(int TableID) { TABLE_LIST[TableID].TableStatePrev(); }

            public void RESET_TABLE(int TableID) {
                TABLE_LIST[TableID] = new TABLE(TableID+1);
                
            }

            public int alarmState(int tableID) { return TABLE_LIST[tableID].alarmState(); }
            public void alarmState(int tableID, int state) { TABLE_LIST[tableID].alarmState(state); }

            public void statePAY(int tableID, int state) { TABLE_LIST[tableID].statePAY(state); }
            public int statePAY(int tableID) { return TABLE_LIST[tableID].statePAY(); }


            private class TABLE
            {
                private int tableID;
                private double PriceToPay=0;
                private Order[] ORDER_LIST;
                private string STATE;
                private int ALARM=0;
                private int state_PAY=0;
                private string STATE_prev="idle";

                    /* STATE could be:
                     *  idle    -   empty table
                     *  alarm   -   customer is alarming to approach
                     *  pay     -   customer is waiting for a bill
                     *  busy    -   customer has ordered something already
                     *  leaving -   customer has paid for his visit and is about to leave
                     
                     */

                // CONSTRUCTOR
                public TABLE(int TableID) {
                    this.tableID = TableID;
                    this.STATE = "idle";
                    this.state_PAY = 0;
                    this.ALARM = 0;
                }

                public void statePAY(int state) { this.state_PAY = state; }
                public int statePAY() { return this.state_PAY; }

                public int alarmState() { return this.ALARM; }
                public void alarmState(int state) { this.ALARM = state ; }

                public string TableState() { return this.STATE; }
                public void TableState(string setState) {  
                    this.STATE = setState;
                    if (setState == "leaving" || setState == "idle" || setState == "busy") this.STATE_prev = setState;

                }
                public void TableStatePrev() { this.STATE = this.STATE_prev; }

                public int getTableID() { return this.tableID; }

                public int getOrdersCount() { return ORDER_LIST == null ? 0 : ORDER_LIST.Length; }


                public void addOrder(string meal, double price, int quantity)
                {
                    if (ORDER_LIST == null) // very first order
                    {
                        ORDER_LIST = new Order[1];
                        ORDER_LIST[0] = new Order(meal, price, quantity);
                        this.PriceToPay += price * quantity;
                        this.STATE = "busy";
                        this.STATE_prev = "busy";
                       // MessageBox.Show(this.getOrderDetailsString(this.getOrdersCount() - 1)[0]);
                        return;
                    }
                    Order[] ORDER_LIST2 = new Order[ORDER_LIST.Length + 1];
                    for (int i = 0; i < ORDER_LIST.Length; i++)
                    {
                        ORDER_LIST2[i] = ORDER_LIST[i];
                    }
                    ORDER_LIST2[ORDER_LIST.Length] = new Order(meal, price, quantity);
                    ORDER_LIST = ORDER_LIST2;
                    this.PriceToPay += price * quantity;
                    ORDER_LIST2 = null;

                   //MessageBox.Show(this.getOrderDetailsString(this.getOrdersCount()-1)[0]);
                }

                public string[] getOrderDetailsString(int orderNumber)
                {
                    return ORDER_LIST[orderNumber].getOrderString();
                }

                public double getTotalSum() {
                    return this.PriceToPay;
                }

                private class Order
                {
                    private string mealName;
                    private double mealPrice;
                    private int quantity;
                    private double totalPrice;
                    private bool LOCKED;

                    // CONSTRUCTOR
                    public Order(string name, double price, int quantity)
                    {
                        this.LOCKED = false;
                        this.mealName = name;
                        this.mealPrice = price;
                        this.quantity = quantity;
                        this.totalPrice = mealPrice * quantity;
                    }

                    public bool Locked() { return LOCKED; }
                    public void Locked(bool status) { this.LOCKED = status; }


                    public string getName() {return this.mealName;}

                    public double getPrice() {return this.mealPrice;}

                    public int getQuantity() {return this.quantity;}

                    public double getOrderPrice() {return this.totalPrice;}

                    public string[] getOrderString()
                    { // [3]: name, price, amount
                        string[] mealString = { this.mealName, this.mealPrice.ToString(), this.quantity.ToString() };
                        return mealString;
                    }
                }

            }


        
        }


        private void parseMessage(string msg) { 
            string[] msgArr;
            int localZoneID = 1;
            int localTableID = 0;
            
            msgArr = msg.Split(':');
            
            try {
                if (zonesCount > 1) localZoneID = Convert.ToInt32(msgArr[0]);
                localTableID = Convert.ToInt32(msgArr[1]);
            }
            catch (Exception e) { 
                MessageBox.Show(
                "ERROR: Could not understand message format : " + msg
                ); return;
            }

            if (msgArr.Length < 3)
            {
                MessageBox.Show(
                "ERROR: Could not understand message format : " + msg
                ); return;
            }


            switch(msgArr[2]){
                case "alarm":
                    ZONES_LIST[localZoneID-1].setTableStateALARM(localTableID-1);
                    bZones[localZoneID-1].BackColor = myColorRed;
                    ZONES_LIST[localZoneID - 1].alarmState(localTableID-1, 1); // enabling alarm
                    //MessageBox.Show(NewLine + localTableID + NewLine + localZoneID);
                    return;
                    break;
                case "pay":
                    ZONES_LIST[localZoneID-1].setTableStatePAY(localTableID-1);
                    bZones[localZoneID-1].BackColor = myColorGreen;
                    ZONES_LIST[localZoneID - 1].statePAY(localTableID-1, 1);
                    return;
                    break;
            }

            string[] ordersArr = msgArr[2].Split('#');

            ZONES_LIST[localZoneID - 1].RESET_TABLE(localTableID-1);

            ZONES_LIST[localZoneID - 1].setTableStateBUSY(localTableID - 1);
            
            for (int i = 0; i < ordersArr.Length - 2; i++) {
                ZONES_LIST[localZoneID - 1].addOrder(
                    localTableID - 1, 
                    ordersArr[i], 
                    Convert.ToDouble(ordersArr[++i]), 
                    Convert.ToInt32(ordersArr[++i])
                    );
            }
            bZones[localZoneID - 1].PerformClick();

        }

        private void clicked_bPay(object sender, EventArgs e) {
            Button buttn = sender as Button;
            string[] localZoneTable = buttn.Tag.ToString().Split(':'); // [ZONE_ID][TABLE_ID]
            if (localZoneTable.Length == 1) return;
            int localZoneID = Convert.ToInt32(localZoneTable[0]);
            int localTableID = Convert.ToInt32(localZoneTable[1]);
            ZONE localZONE = ZONES_LIST[localZoneID];

            localZONE.setTableStateLEAVING(localTableID);
            //pWorkSpace.Refresh();
            bZones[localZoneID].PerformClick();
            bViewClient[localTableID].PerformClick();
            // any other stuff desired: print check, etc...

        }

        private void clicked_bDisarm(object sender, EventArgs e) {
            Button buttn = sender as Button;
            string[] localZoneTable = buttn.Tag.ToString().Split(':'); // [ZONE_ID][TABLE_ID]
            if (localZoneTable.Length == 1) return;
            int localZoneID = Convert.ToInt32(localZoneTable[0]);
            int localTableID = Convert.ToInt32(localZoneTable[1]);
            ZONE localZONE = ZONES_LIST[localZoneID];

            localZONE.alarmState(localTableID, 0);
            localZONE.setTableStatePREVIOUS(localTableID);
            tbClient[localTableID].BackColor = this.BackColor;
            bZones[localZoneID].BackColor = Color.DarkGray;
            bZones[localZoneID].UseVisualStyleBackColor = true;
            //pClients.Refresh();
            bZones[localZoneID].PerformClick();
            bViewClient[localTableID].PerformClick();
            bDisarm.Enabled = false;
            //MessageBox.Show(NewLine + localTableID + NewLine + localZoneID);
        }

        private void clicked_bReset(object sender, EventArgs e) {
            Button buttn = sender as Button;
            string[] localZoneTable = buttn.Tag.ToString().Split(':'); // [ZONE_ID][TABLE_ID]
            if (localZoneTable.Length == 1) return;
            int localZoneID = Convert.ToInt32(localZoneTable[0]);
            int localTableID = Convert.ToInt32(localZoneTable[1]);
            ZONE localZONE = ZONES_LIST[localZoneID];
            
            localZONE.RESET_TABLE(localTableID);
            bZones[localZoneID].PerformClick();
            bViewClient[localTableID].PerformClick();

        }

        private void clicked_bZones(object sender, EventArgs e) { 
            Button buttn = sender as Button;
            int localZoneID = Convert.ToInt32(buttn.Tag);
            string state="";

            for (int i = 0; i < zonesCount; i++) {  //bZones[i].BackColor = bDefaultColor; 
                if (bZones[i].BackColor == Color.DarkGray) {
                    bZones[i].BackColor = SystemColors.Control;
                    bZones[i].UseVisualStyleBackColor = true;
                }
            }
            bZones[localZoneID].BackColor = Color.DarkGray;

            for (int i = 0; i < tablesPerZone; i++) {
                state = ZONES_LIST[localZoneID].getTableState(i);
                bViewClient[i].Tag = localZoneID + ":" + i;  // zoneid : tableid
                tbClient[i].Text = "#"+(i+1)+"\t" + state;
                switch(state){
                    case "idle":
                        tbClient[i].BackColor = myColorNormal;
                        break;
                    case "alarm":
                        tbClient[i].BackColor = myColorRed;
                        break;
                    case "busy":
                        tbClient[i].BackColor = myColorBlue;
                        break;
                    case "pay":
                        tbClient[i].BackColor = myColorGreen;
                        break;
                    case "leaving":
                        tbClient[i].BackColor = myColorLeaving;
                        break;
                }
                state="";
                //MessageBox.Show(ZONES_LIST[zoneID].getTableState(i));
                

            }

        }
            



        private void clicked_bViewClient(object sender, EventArgs e) {
            Button buttn = sender as Button;
            string[] localZoneTable = buttn.Tag.ToString().Split(':'); // [ZONE_ID][TABLE_ID]
            if (localZoneTable.Length == 1) return;
            int localZoneID = Convert.ToInt32(localZoneTable[0]);
            int localTableID = Convert.ToInt32(localZoneTable[1]);
            ZONE localZONE = ZONES_LIST[localZoneID];
            int localOrdersCnt = localZONE.getOrdersCount(localTableID);
            string[] DetailsTITLE = {"Patiekalas","Vnt. kaina", "Kiekis", "Tarp. suma"};
            TextBox[,] tbDetailsMatrix = new TextBox[
                localOrdersCnt,
                DetailsTITLE.Length];
            
            pDetails.Controls.Clear();

            tbTitleBar = new TextBox[DetailsTITLE.Length];
            tbTitleBar[0] = new TextBox();
            tbTitleBar[0].Parent = pDetails;
            tbTitleBar[0].Location = new Point(spacing / 2, 0);
            tbTitleBar[0].Size = new Size((pDetails.Size.Width) / 3 - spacing, tbTitleBar[0].Size.Height * 2);
            tbTitleBar[0].Text = DetailsTITLE[0];
            tbTitleBar[0].BackColor = Color.Gray;
            tbTitleBar[0].ReadOnly = true;
            tbTitleBar[0].TextAlign = HorizontalAlignment.Center;
            tbTitleBar[0].Font = myFont;

            for (int i = 1; i < DetailsTITLE.Length; i++)
            {
                tbTitleBar[i] = new TextBox();
                tbTitleBar[i].Size = new Size((pDetails.Size.Width - tbTitleBar[0].Size.Width - spacing * 2) / (DetailsTITLE.Length - 1), tbTitleBar[i].Size.Height * 2);
                tbTitleBar[i].Location = new Point(tbTitleBar[i - 1].Location.X + tbTitleBar[i - 1].Size.Width);
                tbTitleBar[i].Text = DetailsTITLE[i];
                tbTitleBar[i].Parent = pDetails;
                tbTitleBar[i].BackColor = Color.Gray;
                tbTitleBar[i].ReadOnly = true;
                tbTitleBar[i].TextAlign = HorizontalAlignment.Center;
                tbTitleBar[i].Font = myFont;
            }


           /* MessageBox.Show(
                localOrdersCnt + NewLine +
                DetailsTITLE.Length +NewLine +
                ZONES_LIST[localZoneID].getOrdersCount(localTableID) + NewLine
                );
            * */
            for (int i = 0; i < localOrdersCnt; i++) {
                for (int j = 0; j < DetailsTITLE.Length; j++) {
                    tbDetailsMatrix[i, j] = new TextBox();
                    tbDetailsMatrix[i, j].Parent = pDetails;
                    tbDetailsMatrix[i, j].ReadOnly = true;
                    tbDetailsMatrix[i, j].Font = myFont;
                    tbDetailsMatrix[i, j].BackColor = (i % 2 == 0) ? Color.LightGray : Color.WhiteSmoke;
                    tbDetailsMatrix[i, j].Location = new Point(tbTitleBar[j].Location.X, tbTitleBar[j].Size.Height * (i + 1));
                    tbDetailsMatrix[i, j].Size = tbTitleBar[j].Size;
                }

                tbDetailsMatrix[i, 0].Text = localZONE.getTableOrder(localTableID ,i)[0];
                tbDetailsMatrix[i, 1].Text = localZONE.getTableOrder(localTableID, i)[1];
                tbDetailsMatrix[i, 2].Text = localZONE.getTableOrder(localTableID, i)[2];
                tbDetailsMatrix[i, 3].Text = (Convert.ToDouble(localZONE.getTableOrder(localTableID, i)[1]) * Convert.ToDouble(localZONE.getTableOrder(localTableID, i)[2])).ToString();
            }

            tbTotalPrice.Text = "Viso: \t" + localZONE.getTablePrice(localTableID).ToString() + "   LT";

            bPay.Tag = localZoneID + ":" + localTableID;
          //  bPay.Enabled = true;
          //  if(localZONE.getTablePrice(localTableID) == 0 || localZONE.getTableState(localTableID) == "leaving") // has not ordered anything
          //      bPay.Enabled = false;

            bDisarm.Tag = localZoneID + ":" + localTableID;
          //  bDisarm.Enabled = false;
            //if (localZONE.alarmState(localTableID) == 1) // is ready to pay
          //  if (localZONE.getTableState(localTableID) == "alarm") // is ready to pay
          //      bDisarm.Enabled = true;
            //MessageBox.Show(localZONE.statePAY(localTableID).ToString() +NewLine + localTableID + NewLine + localZoneID);

            bReset.Tag = localZoneID + ":" + localTableID;
           // bReset.Enabled = false;
            //if (localZONE.statePAY(localTableID) == 2) // has already paid
          //  if (localZONE.getTableState(localTableID) == "leaving") // has already paid
          //  {
          //      bReset.Enabled = true;
          //      bPay.Enabled = false;
          //  }

            if (localZONE.getTablePrice(localTableID) == 0) bPay.Enabled = false;

            switch (localZONE.getTableState(localTableID)) {
                case "leaving":
                    bReset.Enabled = true;
                    bPay.Enabled = false;
                    bDisarm.Enabled = false;
                    break;
                case "alarm":
                    bReset.Enabled = false;
                    bPay.Enabled = false;
                    bDisarm.Enabled = true;
                    break;
                case "pay":
                    bReset.Enabled = false;
                    bPay.Enabled = true;
                    bDisarm.Enabled = false;
                    break;
                case "idle":
                    bReset.Enabled = false;
                    bPay.Enabled = false;
                    bDisarm.Enabled = false;
                    break;
                case "busy":
                    bReset.Enabled = false;
                    bPay.Enabled = false;
                    bDisarm.Enabled = false;
                    break;
            }


            
        }



        //Socket senderSock;


        //private void Connect_Click(object sender, RoutedEventArgs e)
        //private void Create_Socket(object sender, EventArgs e)

        private void Create_Socket()
        {
            try
            {
                // Creates one SocketPermission object for access restrictions
                permission = new SocketPermission(
                NetworkAccess.Accept,     // Allowed to accept connections 
                TransportType.Tcp,        // Defines transport types 
                "",                       // The IP addresses of local host 
                SocketPermission.AllPorts // Specifies all ports 
                );

                // Listening Socket object 
                sListener = null;

                // Ensures the code to have permission to access a Socket 
                permission.Demand();

                // Resolves a host name to an IPHostEntry instance 
                //IPHostEntry ipHost = Dns.GetHostEntry("");
                //IPHostEntry ipHost = Dns.GetHostEntry(clientIP);
                IPHostEntry ipHost = Dns.GetHostEntry(masterIP);

                // Gets first IP address associated with a localhost 
                IPAddress ipAddr = ipHost.AddressList[0];

                // Creates a network endpoint 
                //ipEndPoint = new IPEndPoint(ipAddr, clientPORT);
                ipEndPoint = new IPEndPoint(IPAddress.Any, masterPORT);

                // Create one Socket object to listen the incoming connection 
                sListener = new Socket(
                    ipAddr.AddressFamily,
                    SocketType.Stream,
                    ProtocolType.Tcp
                    );
                
                // Associates a Socket with a local endpoint 
                sListener.Bind(ipEndPoint);

                tbStatus.Text = "Server started.";

                //Start_Button.IsEnabled = false;
                //StartListen_Button.IsEnabled = true;
                setSocketToListen(sListener);
            }
            catch (Exception exc) { MessageBox.Show(
                "ERROR: Cannot open/bind socket"+
                NewLine+
                "\tIP: \t" + masterIP +
                "\tPort:\t" + masterPORT +
                NewLine+
                exc.ToString()); }
        }


        protected void setSocketToListen(Socket sListener) {
            try
            {
                // Places a Socket in a listening state and specifies the maximum 
                // Length of the pending connections queue 
                sListener.Listen(10);

                // Begins an asynchronous operation to accept an attempt 
                AsyncCallback aCallback = new AsyncCallback(AcceptCallback);
                sListener.BeginAccept(aCallback, sListener);

                tbStatus.Text = "Server is now listening on " + ipEndPoint.Address + " port: " + ipEndPoint.Port;

            }
            catch (Exception exc) {
                MessageBox.Show(
                    "ERROR: set socket to LISTEN" +
                    NewLine +
                    "\tIP: \t" + masterIP +
                    "\tPort:\t" + masterPORT +
                    NewLine +
                    exc.ToString());
            }
        }



        protected void AcceptCallback(IAsyncResult ar)
        {
            Socket listener = null;

            // A new Socket to handle remote host communication 
            Socket handler = null;
            try
            {
                // Receiving byte array 
                byte[] buffer = new byte[1024];
                // Get Listening Socket object 
                listener = (Socket)ar.AsyncState;
                // Create a new socket 
                handler = listener.EndAccept(ar);

                // Using the Nagle algorithm 
                handler.NoDelay = false;

                // Creates one object array for passing data 
                object[] obj = new object[2];
                obj[0] = buffer;
                obj[1] = handler;

                // Begins to asynchronously receive data 
                handler.BeginReceive(
                    buffer,        // An array of type Byt for received data 
                    0,             // The zero-based position in the buffer  
                    buffer.Length, // The number of bytes to receive 
                    SocketFlags.None,// Specifies send and receive behaviors 
                    new AsyncCallback(ReceiveCallback),//An AsyncCallback delegate 
                    obj            // Specifies infomation for receive operation 
                    );

                // Begins an asynchronous operation to accept an attempt 
                AsyncCallback aCallback = new AsyncCallback(AcceptCallback);
                listener.BeginAccept(aCallback, listener);
            }
            catch (Exception exc) {
                MessageBox.Show(
                    "ERROR: Cannot accept callback from socket" +
                    NewLine +
                    "\tIP: \t" + masterIP +
                    "\tPort:\t" + masterPORT +
                    NewLine +
                    exc.ToString());
            }
        }



        protected void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                // Fetch a user-defined object that contains information 
                object[] obj = new object[2];
                obj = (object[])ar.AsyncState;

                // Received byte array 
                byte[] buffer = (byte[])obj[0];

                // A Socket to handle remote host communication. 
                handler = (Socket)obj[1];

                // Received message 
                string content = string.Empty;


                // The number of bytes received. 
                int bytesRead = handler.EndReceive(ar);

                if (bytesRead > 0){
                    content += Encoding.UTF8.GetString(buffer, 0, bytesRead);
                    }
                    else {
                        // Continues to asynchronously receive data
                        byte[] buffernew = new byte[1024];
                        obj[0] = buffernew;
                        obj[1] = handler;
                        handler.BeginReceive(buffernew, 0, buffernew.Length,
                            SocketFlags.None,
                            new AsyncCallback(ReceiveCallback), obj);
                    }

                    //this.Dispatcher.BeginInvoke(DispatcherPriority.Normal, (ThreadStart)delegate()
                    {
                        //tbStatus.Text += "\n"+content;
                        parseMessage(content);
                    }
                    //);}
            }
            catch (Exception exc) {
                MessageBox.Show(
                    "ERROR: Cannot receive callback from socket" +
                    NewLine +
                    "\tIP: \t" + masterIP +
                    "\tPort:\t" + masterPORT +
                    NewLine +
                    exc.ToString());
            }
        }

    }
}

