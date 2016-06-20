using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;



namespace Staliukas
{
    partial class MainWindow
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




        private int sizeW = Screen.PrimaryScreen.Bounds.Width;
        private int sizeH = Screen.PrimaryScreen.Bounds.Height;
        private int spacing;

        protected Basket myBasket;
        private Panel pTOP;
        private Panel panel1;
        private Panel pMAIN;
        private Panel pMENU;
        private Panel pDrinks;


        //pathSeparator adds compatibility for platforms other than Windows
        protected string pathSeparator = System.IO.Path.DirectorySeparatorChar.ToString();

        protected Panel[] pMeal;
        protected PictureBox[] pBox;
        protected TextBox[] tbPrice;
        protected TextBox[] tbAbout;
        protected TextBox[] tbName;
        protected NumericUpDown[] spinner;
        protected Button[] bSubmit;
        protected Button[] btsTop;
        protected Button[] bRemove;
        Button[] btsMenu;
        protected Button bSubmitBasket;
        protected Button bNULL = new Button();
        protected TextBox tbStatus = new TextBox();
        private TextBox tbSummary;
        private Label lbWAIT;
        private Button btWAIT;

        protected Font myFont;

        protected string pathToMaster = "", masterIP="";
        private int tableID = 0;
        private int zoneID = 0;
        private int masterPORT = 0;



        protected void InitializeComponent()
        {
            bNULL.Visible = false;
            myFont = new Font(bNULL.Font.Name, bNULL.Font.SizeInPoints*3/2);
            //string tableID="", pathToMaster="";
            string ERRMSG = "";
            string[] config = null;

            // reading configuration file
            try
            {
                config = System.IO.File.ReadAllLines("config.txt");
            }
            catch (System.IO.IOException e) {
                ERRMSG += e.Message.ToString();
                MessageBox.Show("ERROR: " + ERRMSG);
                Environment.Exit(1);
            }
            string[] configSub;
            for (int i = 0; i < config.Length; i++ ) {
                configSub = config[i].Split('=');
                if (configSub.Length == 2){
                    switch (configSub[0]){
                        case "TableID": tableID = Convert.ToInt32(configSub[1]); break;
                        case "ZoneID": zoneID = Convert.ToInt32(configSub[1]); break;
                        case "MasterPath": pathToMaster = configSub[1]; break;
                        case "MasterIP": masterIP = configSub[1]; break;
                        case "MasterPORT": masterPORT = Convert.ToInt32(configSub[1]); break;
                        default: break;
                    }
                }
            }

            if (tableID == 0 || zoneID == 0 || pathToMaster == "" || masterIP == "" || masterPORT == 0) {
                ERRMSG += "review configuration file. Vital variables currently are:" +
                    "\n\tTableID=" + tableID +
                    "\n\tZoneID=" + zoneID +
                    "\n\tMasterPath=" + pathToMaster +
                    "\n\tMasterIP=" + masterIP +
                    "\n\tMasterPORT=" + masterPORT +
                    "";
                MessageBox.Show("ERROR: " + ERRMSG);
                Environment.Exit(1);
            }

            tbStatus.Visible = false;
            tbStatus.Parent = this;

            //string tableID = System.IO.File.ReadAllText("TableID.txt");
            myBasket = new Basket(zoneID, tableID);
            //int sizeW = 1024;
            //int sizeH = 600;

             spacing = sizeW/60;

            //8,5 , 15 //
            Size buttSize = new Size(sizeW / 8, sizeH / 15);

            { // Creating Panels
                this.pTOP = new Panel();    // Banner panel
                this.panel1 = new Panel();  // Main container for workspace
                this.pMENU = new Panel();   // Panel for buttons
                this.pDrinks = new Panel();
                this.pMAIN = new Panel();   // Panel for pictures & other data

                this.pTOP.Location = new Point(spacing, spacing);
                this.pTOP.Name = "pTOP";
                this.pTOP.BorderStyle = BorderStyle.Fixed3D;
                this.pTOP.Size = new Size(sizeW - spacing * 2, 2* spacing + buttSize.Height);
                this.pTOP.TabIndex = 2;

                this.panel1.BorderStyle = BorderStyle.Fixed3D;
                this.panel1.Controls.Add(this.pMENU);
                this.panel1.Controls.Add(this.pMAIN);
                this.panel1.Location = new Point(spacing, pTOP.Location.Y + pTOP.Size.Height + spacing *2);
                this.panel1.Name = "panel1";
                this.panel1.Size = new Size(sizeW - spacing * 2, sizeH - panel1.Location.Y - spacing);
                this.panel1.TabIndex = 3;
                
                this.pMENU.Location = new Point(spacing, spacing);
                this.pMENU.BorderStyle = BorderStyle.Fixed3D;
                this.pMENU.Name = "pMENU";
                this.pMENU.Size = new Size(2 * spacing + buttSize.Width, panel1.Size.Height - 2 * spacing);
                this.pMENU.TabIndex = 3;
                this.pMENU.Paint += new PaintEventHandler(this.pMENU_Paint);
                this.pMENU.Visible = true;

                this.pMAIN.BorderStyle = BorderStyle.FixedSingle;
                this.pMAIN.Location = new Point(pMENU.Location.X + pMENU.Size.Width + spacing, pMENU.Location.Y);
                this.pMAIN.Name = "pMAIN";
                this.pMAIN.Size = new Size(panel1.Size.Width - spacing - pMAIN.Location.X, pMENU.Size.Height);
                this.pMAIN.TabIndex = 2;
                this.pMAIN.VerticalScroll.Enabled = true;
                this.pMAIN.AutoScroll = true;
                this.pMAIN.VerticalScroll.Visible = true;

                this.pDrinks.Location = this.pMENU.Location;
                this.pDrinks.Size = this.pMENU.Size;
                this.pDrinks.Name = "pDrinks";
                this.pDrinks.TabIndex = this.pMENU.TabIndex;
                this.pDrinks.Parent = this.pMENU.Parent;
                this.pDrinks.Visible = false;

                this.panel1.Controls.Add(this.pMENU);
                this.panel1.Controls.Add(this.pMAIN);

            }


            lbWAIT = new Label();
            lbWAIT.Font = myFont;
            lbWAIT.BackColor = this.BackColor;
            lbWAIT.Text = "";
            lbWAIT.AutoSize = true;
            lbWAIT.Parent = pMAIN;

            btWAIT = new Button();
            btWAIT.Font = myFont;
            btWAIT.Text = "NAUJAS KLIENTAS";
            btWAIT.Parent = pTOP;
            btWAIT.Location = new Point(spacing, spacing);
            btWAIT.Visible = false;
            btWAIT.Name = "RESET";
            btWAIT.Click += buttonClicked;
            
            

            this.pTOP.SuspendLayout();
            this.panel1.SuspendLayout();
            this.pMENU.SuspendLayout();
            this.SuspendLayout();

            tbSummary = new TextBox();


            // These two are vitally related to each other. Modify both of them respectively if any changes needed
            String[] btsTopLabels = { "Krepšelis", "Iškviesti","Apmokėti" };   // for use in FRONTend
            String[] btsTopNames = { "bBasket", "bAlarm","bPay" };          // for use in BACKend

            btsTop = new Button[btsTopLabels.Length];
            for (int i = btsTopLabels.Length - 1; i >= 0; i--)
            {

                btsTop[i] = new Button();
                btsTop[i].Parent = pTOP;
                btsTop[i].Size = buttSize;
                btsTop[i].Font = myFont;
                if(i == btsTopLabels.Length - 1)
                    btsTop[i].Location = new Point(pTOP.Location.X + pTOP.Size.Width - spacing * 2 - buttSize.Width, pTOP.Location.Y);
                else
                    btsTop[i].Location = new Point(btsTop[i + 1].Location.X - spacing - buttSize.Width, pTOP.Location.Y);

                btsTop[i].Text = btsTopLabels[i];
                btsTop[i].Click += buttonClicked;
                btsTop[i].Name = btsTopNames[i];
                pTOP.Controls.Add(btsTop[i]);
            }
            btsTop[2].Enabled = false;
            btWAIT.Size = new Size(btsTop[0].Size.Width*2, btsTop[0].Size.Height);

            // These two are vitally related to each other. Modify both of them respectively if any changes needed
            String[] btsMenuLabels = { "Užkandėlės", "Sriubos", "Salotos", "Karštieji", "Desertai", "Gėrimai" };
            String[] btsMenuNames = { "bSnacks", "bSoups", "bSalads", "bHot", "bDessert", "bDrinks" };

            btsMenu = new Button[btsMenuLabels.Length];
            for (int i=0; i < btsMenuLabels.Length;i++) {
                btsMenu[i] = new Button();
                btsMenu[i].Parent = pMENU;
                btsMenu[i].Size = buttSize;
                btsMenu[i].Font = myFont;

                if (i == 0)
                    btsMenu[i].Location = new Point(pMENU.Location.X, pMENU.Location.Y + spacing);
                else
                    btsMenu[i].Location = new Point(pMENU.Location.X, btsMenu[i-1].Location.Y + buttSize.Height + spacing);

                btsMenu[i].Text = btsMenuLabels[i];
                btsMenu[i].Click += buttonClicked;
                btsMenu[i].Name = btsMenuNames[i];
                pMENU.Controls.Add(btsMenu[i]);
                
            }

            // These two are vitally related to each other. Modify both of them respectively if any changes needed
            String[] btsDrinksLabels = { "Karšti", "Gaivinantys", "Alkoholiniai", "<- Atgal" };
            String[] btsDrinksNames = { "bDrinksHot", "bDrinksFresh", "bDrinksAlco", "bBackToMenu" };

            Button[] btsDrinks = new Button[btsDrinksLabels.Length];
            for (int i = 0; i < btsDrinksLabels.Length; i++)
            {
                btsDrinks[i] = new Button();
                btsDrinks[i].Parent = pDrinks;
                btsDrinks[i].Size = buttSize;
                btsDrinks[i].Font = myFont;

                if (i == 0)
                    btsDrinks[i].Location = new Point(pDrinks.Location.X, pDrinks.Location.Y + spacing);
                else
                    btsDrinks[i].Location = new Point(pDrinks.Location.X, btsDrinks[i - 1].Location.Y + buttSize.Height + spacing);

                btsDrinks[i].Text = btsDrinksLabels[i];
                btsDrinks[i].Click += buttonClicked;
                btsDrinks[i].Name = btsDrinksNames[i];
                pDrinks.Controls.Add(btsDrinks[i]);

            }


            // 
            // MainWindow
            // 
            //this.AutoScaleDimensions = new SizeF(6F, 13F);
            this.AutoScaleMode = AutoScaleMode.Font;
            this.ClientSize = new Size(sizeW, sizeH);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.pTOP);
            this.FormBorderStyle = FormBorderStyle.None;
            this.MaximizeBox = false;
            this.Name = "MainWindow";
            this.Text = "Pagrindinis";
            this.Load += new EventHandler(this.Form1_Load);
            this.pTOP.ResumeLayout(false);
            this.pTOP.PerformLayout();
            this.panel1.ResumeLayout(false);
            this.pMENU.ResumeLayout(false);
            this.ResumeLayout(false);

            

        }
        

        private void buttonClicked(object sender, EventArgs e) {
            Button btn = sender as Button;
            //{ "bSnacks", "bSoups", "bSalads", "bHot", "bDessert", "bDrinks" }
            switch(btn.Name){
                case "RESET":
                    btsMenu[0].PerformClick();
                    btsTop[0].Enabled = true;
                    lbWAIT.Text = "";
                    for (int i = 0; i < btsMenu.Length; i++) btsMenu[i].Enabled = true;
                    btWAIT.Visible = false;
                        break;
                case "bBasket":
                    showBasket();
                    pMAIN.Visible = true;
                    break;
                case "bAlarm":
                    sendToServer(zoneID + ":" + tableID+":alarm", masterIP, Convert.ToInt32(masterPORT));
                    break;
                case "bPay":

                    if (sendToServer(zoneID + ":" + tableID + ":pay", masterIP, Convert.ToInt32(masterPORT)) == 0) {
                        bSubmitBasket.Enabled = false;
                        string sumToPay = Convert.ToString(tbSummary.Text);
                        //TextBox tbWAIT = new TextBox();
                        lbWAIT.Text = "Prašau palaukti, prie jūsų prieis padavėjas." +
                                Environment.NewLine +
                                sumToPay;
                        myBasket = new Basket(zoneID, tableID);
                        pMAIN.Controls.Clear();
                        lbWAIT.Parent = pMAIN;
                        lbWAIT.Visible = true;
                        btWAIT.Visible = true;
                        for (int i = 0; i < btsMenu.Length; i++) btsMenu[i].Enabled = false;
                        btsTop[0].Enabled = btsTop[2].Enabled = false;
                    }
                    else MessageBox.Show("KLAIDA: Nepavyko nusiųsti užklausos");
                    break;
                case "bSnacks":
                    show_details("Preapp");
                    break;
                case "bSoups":
                    show_details("Soups");
                    break;
                case "bSalads":
                    show_details("Salad");
                    break;
                case "bHot":
                    show_details("Hot");
                    break;
                case "bDessert":
                    show_details("Dessert");
                    break;
                case "bDrinks":
                    pMENU.Visible = false;
                    pDrinks.Visible = true;
                    break;
                case "bDrinksFresh":
                    show_details("Drinks"+pathSeparator+"Cold");
                    break;
                case "bDrinksHot":
                    show_details("Drinks" + pathSeparator + "Hot");
                    break;
                case "bDrinksAlco":
                    show_details("Drinks" + pathSeparator + "Alco");
                    break;
                case "bBackToMenu":
                    pDrinks.Visible = false;
                    pMENU.Visible = true;
                    break;
                case "bToBasket":
                    NumericUpDown spinn = btn.Tag as NumericUpDown;
                    string spintag = spinn.Tag as string;
                    string[] order_price = spintag.Split(':');
                    if (spinn.Value == 0) {
                        MessageBox.Show("Jei norite pridėti patiekalą \""+order_price[0]+ "\" į krepšelį, turite pasirinkti užsakomų porcijų kiekį");
                        break;
                    }
                    //MessageBox.Show("Adding to Basket: " + spinn.Tag + ":" + spinn.Value);
                    myBasket.addOrder(order_price[0], Convert.ToDouble(order_price[1]), Convert.ToInt32(spinn.Value));
                    spintag = null;
                    order_price = null;
                    break;
                case "bRemove":
                    myBasket.removeOrder(Convert.ToInt32(btn.Tag));
                    showBasket();
                    pMAIN.Visible = true;
                    break;
                case "bSubmitBasket":
                    if (myBasket.submitBasket(masterIP, masterPORT) == 0)
                    {
                        btsTop[2].Enabled = true;
                    }
                    else {
                        MessageBox.Show("Iškilo nesklandumų siunčiant užsakymą.");
                    }
                    showBasket();
                    break;
                default:
                    MessageBox.Show("Cannot find such button: " + btn.Name);
                    break;

            }

        }




        protected string asciiToUtf8(string str) {
            string str_utf8=string.Empty;
            byte[] utf8_bytes = new byte[str.Length];
            for (int i = 0; i < str.Length; i++) {
                utf8_bytes[i] = (byte)str[i];
            }
            str_utf8 = Encoding.UTF8.GetString(utf8_bytes, 0, str.Length);
            return str_utf8;
        }

        
        private void show_details(string foldr)
        {
            pMAIN.Controls.Clear();

            pMeal = null;
            pBox = null;
            string[] lines = System.IO.File.ReadAllLines("data" + pathSeparator + foldr + pathSeparator + "META.txt", Encoding.UTF8);

            pMeal = new Panel[lines.Length];
            pBox = new PictureBox[lines.Length];
            tbPrice = new TextBox[lines.Length];
            tbAbout = new TextBox[lines.Length];
            tbName = new TextBox[lines.Length];
            spinner = new NumericUpDown[lines.Length];
            bSubmit = new Button[lines.Length];
            string[] details;

            for (int i = 0; i < lines.Length; i++) {
                details = lines[i].Split(':'); //[0]=name; [1]=price; [2]=picture.jpg; [3]=info
                pMeal[i] = new Panel(); pMeal[i].Visible = false;
                pBox[i] = new PictureBox();
                tbPrice[i] = new TextBox();
                tbAbout[i] = new TextBox();
                tbName[i] = new TextBox();
                spinner[i] = new NumericUpDown();
                bSubmit[i] = new Button();

                bSubmit[i].Name = "bToBasket";
                bSubmit[i].Font = myFont;
                
                
                pMeal[i].BackColor = Color.WhiteSmoke;
                pMeal[i].Name = details[2];
                pMeal[i].Tag = details[1];
                
                pMeal[i].Size = new Size(pMAIN.Size.Width - spacing * 3, pMAIN.Size.Height / 2);
                pBox[i].Parent = pMeal[i];
                pMeal[i].Parent = pMAIN;
                tbPrice[i].Parent = pMeal[i];
                tbAbout[i].Parent = pMeal[i];
                tbName[i].Parent = pMeal[i];
                spinner[i].Parent = pMeal[i];
                bSubmit[i].Parent = pMeal[i];

                tbName[i].Text = details[0];
                tbPrice[i].Text = details[1];
                bSubmit[i].Text = "Į krepšelį";
                tbAbout[i].Text = details[3];
                tbAbout[i].Multiline = true; tbAbout[i].ScrollBars = ScrollBars.Vertical;
                bSubmit[i].Click += buttonClicked;

                spinner[i].Minimum = 0;
                spinner[i].Maximum = 20;

                string picpath = System.IO.Path.GetDirectoryName(Application.ExecutablePath) + pathSeparator + "data" + pathSeparator + foldr + pathSeparator + details[2] + ".jpg";
                
                if (i == 0)
                {
                    pMeal[i].Location = new Point(spacing, spacing);
                }
                else
                {
                    pMeal[i].Location = new Point(spacing, pMeal[i - 1].Location.Y + pMeal[i - 1].Size.Height + spacing);
                }

                pBox[i].Location = new Point(spacing, spacing);
                pBox[i].Size = new Size(pMeal[i].Size.Width / 2, pMeal[i].Size.Height - spacing * 2);
                pBox[i].BorderStyle = BorderStyle.Fixed3D;
                pBox[i].Image = Image.FromFile(picpath);
                
                pBox[i].SizeMode = PictureBoxSizeMode.Zoom;

                spinner[i].Tag = details[0] + ":" + details[1];
                bSubmit[i].Tag = spinner[i];
                
                
                tbName[i].Location = new Point(pBox[i].Location.X + pBox[i].Size.Width + spacing, pBox[i].Location.Y);

                tbAbout[i].Location = new Point(tbName[i].Location.X, tbName[i].Location.Y + tbName[i].Size.Height + spacing);
                tbAbout[i].Size = new Size((pMeal[i].Size.Width - pBox[i].Size.Width - spacing * 3) , pBox[i].Size.Height / 2);

                tbName[i].Size = new Size(tbAbout[i].Size.Width, tbName[i].Size.Height);

                spinner[i].Font = myFont;
                tbName[i].Font = myFont;
                tbAbout[i].Font = myFont;
                tbPrice[i].Font = myFont;
                tbPrice[i].Location = new Point(tbAbout[i].Location.X, tbAbout[i].Location.Y + tbAbout[i].Size.Height + spacing  );
                tbPrice[i].Size = new Size(tbPrice[i].Size.Width / 2, tbPrice[i].Size.Height*2);
                spinner[i].Location = new Point(tbPrice[i].Location.X + tbPrice[i].Size.Width +spacing, tbPrice[i].Location.Y);
                spinner[i].Size = tbPrice[i].Size;
                bSubmit[i].Location = new Point(spinner[i].Location.X + spinner[i].Size.Width + spacing , spinner[i].Location.Y);
                bSubmit[i].Size = new Size(bSubmit[i].Size.Width*2, tbPrice[i].Size.Height);

                tbName[i].ReadOnly = true; tbName[i].BackColor = pMeal[i].BackColor; tbName[i].BorderStyle = BorderStyle.None;
                tbAbout[i].ReadOnly = true; tbAbout[i].BackColor = pMeal[i].BackColor; tbAbout[i].BorderStyle = BorderStyle.None;
                tbPrice[i].ReadOnly = true; tbPrice[i].BackColor = pMeal[i].BackColor;

                details = null;
                picpath = null;



                pMeal[i].Visible = true;
            }
            
        }








        protected class Basket {
            private int tableID = 0;
            private int zoneID = 0;
            private Order[] ORDER_LIST;
            private bool LOCKED;
            private System.Net.Sockets.Socket soc;
            
            // constructor
            public Basket(int zoneID, int tableID) {
                this.tableID = tableID;
                this.zoneID = zoneID;
                this.LOCKED = false;
            }


            public bool isLocked() { return this.LOCKED; }
            public bool isLocked(int orderID) { return ORDER_LIST[orderID].Locked(); }

            public int getTableID() {return this.tableID;}

            public int getOrdersCount() {return ORDER_LIST == null ? 0 :ORDER_LIST.Length;}


            public void addOrder(string meal, double price, int quantity)
            {
                if (ORDER_LIST == null) {
                    ORDER_LIST = new Order[1];
                    ORDER_LIST[0] = new Order(meal, price, quantity);
                    return;
                }

                Order[] ORDER_LIST2 = new Order[ORDER_LIST.Length+1];
                for(int i=0; i<ORDER_LIST.Length; i++){
                    ORDER_LIST2[i] = ORDER_LIST[i];
                }
                ORDER_LIST2[ORDER_LIST.Length] = new Order(meal, price, quantity);
                ORDER_LIST = ORDER_LIST2;
                ORDER_LIST2 = null;
            }


            public string[] getOrderDetailsString(int orderNumber) {
                return ORDER_LIST[orderNumber].getOrderString();
            }

            public void removeOrder(int orderNumber){
                Order[] ORDER_LIST2 = new Order[ORDER_LIST.Length-1];

                if (orderNumber < 0 || orderNumber > ORDER_LIST.Length - 1) // out of scope
                    return;
                else
                if (orderNumber == ORDER_LIST.Length - 1) // last
                        for (int i = 0; i < orderNumber; i++) 
                        ORDER_LIST2[i] = ORDER_LIST[i];
                else
                if(orderNumber == 0)
                        for(int i=1; i<ORDER_LIST.Length; i++)
                        ORDER_LIST2[i-1] = ORDER_LIST[i];
                else {

                    for (int i = 0; i < ORDER_LIST.Length-1; i++)
                        if (i >= orderNumber ) ORDER_LIST2[i] = ORDER_LIST[i + 1];
                        else ORDER_LIST2[i] = ORDER_LIST[i];

                    }

                ORDER_LIST = new Order[ORDER_LIST2.Length];
                    ORDER_LIST = ORDER_LIST2;
            }


            public int submitBasket(string masterIP, int masterPORT) {
                string[] orderDetails;
                string BUFFER = this.zoneID + ":" + this.tableID + ":";
                string MSG = "";
                int RetVal = 0;
                try {
                    for (int i = 0; i < this.getOrdersCount(); i++)
                    {   //[3]: name, price, amount
                        orderDetails = this.getOrderDetailsString(i);
                        //BUFFER += orderDetails[0] + "#" + orderDetails[1] + "#" + orderDetails[2] + "\n";
                        BUFFER += orderDetails[0] + "#" + orderDetails[1] + "#" + orderDetails[2] + "#";
                    }
                    MSG = "Užsakymas patvirtintas";
                    if (this.sendToServer(BUFFER, masterIP, masterPORT) != 0) {
                        MSG = "KLAIDA: užsakymo išsiųsti nepavyko. Patikrinkite ryšį su serveriu.";
                        RetVal = 1;
                    }
                    else LockBasket();
                    MessageBox.Show(MSG);
                }
                catch (Exception e) { MessageBox.Show(e.Message.ToString()); RetVal = 1; }
                return RetVal;
            }

            private void LockBasket() {
                for (int i = 0; i < this.getOrdersCount(); i++)
                    ORDER_LIST[i].Locked(true);
                }


            private int openClientSocket(string IP, int PORT) {

                try
                {
                    soc = new System.Net.Sockets.Socket(
                            System.Net.Sockets.AddressFamily.InterNetwork,
                            System.Net.Sockets.SocketType.Stream,
                            System.Net.Sockets.ProtocolType.Tcp);
                    System.Net.IPAddress ipAdd = System.Net.IPAddress.Parse(IP);
                    System.Net.IPEndPoint remoteEP = new System.Net.IPEndPoint(ipAdd, PORT);
                    soc.Connect(remoteEP);

                    return 0;
                }
                catch (Exception e) { MessageBox.Show(e.ToString()); return -1; }
            }

            private int sendToServer(string DATA, string IP, int PORT) {

                if(openClientSocket(IP, PORT) != 0) return -1;


                byte[] byData = System.Text.Encoding.UTF8.GetBytes(DATA);
                soc.Send(byData);
                soc.Close();
                return 0;
            }

            private class Order {
                private string mealName;
                private double mealPrice;
                private int quantity;
                private double totalPrice;
                private bool LOCKED;

                public Order(string name, double price, int quantity) {
                    this.LOCKED = false;
                    this.mealName = name;
                    this.mealPrice = price;
                    this.quantity = quantity;
                    this.totalPrice = mealPrice * quantity;
                }

                public bool Locked() { return LOCKED; }
                public void Locked(bool status) { this.LOCKED = status; }


                public string getName() {
                    return this.mealName;
                }

                public double getPrice() {
                    return this.mealPrice;
                }

                public int getQuantity() {
                    return this.quantity;
                }

                public double getOrderPrice() {
                    return this.totalPrice;
                }

                public string[] getOrderString() { // [3]: name, price, amount
                    string[] mealString = { this.mealName, this.mealPrice.ToString(), this.quantity.ToString()};
                    return mealString;
                }
            }

        } // Basket




        private void showBasket() {
            Panel pBasket = new Panel();
            pBasket.Visible = false;
            string[] columnTitles = { "Patiekalo pavadinimas", "Porcijų sk.", "Porcijos kaina", "Tarpinė suma", "Šalinti" };
            TextBox[] tbTitleBar = new TextBox[columnTitles.Length];
            TextBox[] tbSingleOrderData = new TextBox[columnTitles.Length-1]; // viena pozicija lieka mygtukui

            TextBox[,] tbOrdersMatrix = new TextBox[myBasket.getOrdersCount(), columnTitles.Length - 1];
            
            bSubmitBasket = new Button();
            
            bRemove = new Button[myBasket.getOrdersCount()];

            pMAIN.Controls.Clear();
            //pMAIN.Visible = false;

            tbTitleBar[0] = new TextBox();

            pBasket.Parent = pMAIN;
            pBasket.AutoScroll = true;
            pBasket.Location = new Point(spacing, tbTitleBar[0].Location.Y + tbTitleBar[0].Size.Height);
            pBasket.Size = new Size(pMAIN.Size.Width - spacing*2, pMAIN.Size.Height - tbTitleBar[0].Size.Height * 4);


            tbTitleBar[0].Parent = pBasket;
            tbTitleBar[0].Location = new Point(spacing/2, 0);
            tbTitleBar[0].Size = new Size((pBasket.Size.Width )/ 3 - spacing, tbTitleBar[0].Size.Height*2);
            tbTitleBar[0].Text = columnTitles[0];
            tbTitleBar[0].BackColor = Color.Gray;
            tbTitleBar[0].ReadOnly = true;
            tbTitleBar[0].TextAlign = HorizontalAlignment.Center;
            tbTitleBar[0].Font = myFont;

            for (int i = 1; i < columnTitles.Length; i++) {
                tbTitleBar[i] = new TextBox();
                tbTitleBar[i].Size = new Size((pBasket.Size.Width - tbTitleBar[0].Size.Width - spacing*2)  / (columnTitles.Length - 1), tbTitleBar[i].Size.Height*2);
                tbTitleBar[i].Location = new Point(tbTitleBar[i-1].Location.X + tbTitleBar[i-1].Size.Width);
                tbTitleBar[i].Text = columnTitles[i];
                tbTitleBar[i].Parent = pBasket;
                tbTitleBar[i].BackColor = Color.Gray;
                tbTitleBar[i].ReadOnly = true;
                tbTitleBar[i].TextAlign = HorizontalAlignment.Center;
                tbTitleBar[i].Font = myFont;
            }

            if (myBasket.getOrdersCount() < 1)
            {
                MessageBox.Show("Krepšelis tuščias");
                return;
            }

            
            tbSummary.Parent = bSubmitBasket.Parent = pMAIN;
            tbSummary.Location = new Point( tbTitleBar[1].Location.X/2, pBasket.Location.Y + pBasket.Size.Height + spacing / 2);
            tbSummary.Size = new Size(tbTitleBar[1].Location.X/2 + tbTitleBar[1].Size.Width*2 , pMAIN.Size.Height - tbSummary.Location.Y);
            tbSummary.BackColor = pMAIN.BackColor;
            tbSummary.Text = "Galutinė suma:\t";
            tbSummary.Font = myFont;
            //tbSummary.BorderStyle = BorderStyle.None;
            //---
            bSubmitBasket.Location = new Point(tbTitleBar[3].Location.X + spacing, tbSummary.Location.Y);
            bSubmitBasket.Size = new Size((tbTitleBar[3].Size.Width-spacing)*2, tbSummary.Size.Height);
            bSubmitBasket.Text = "Pateikti užsakymą";
            bSubmitBasket.BackColor = Color.LightGreen;
            bSubmitBasket.Name = "bSubmitBasket";
            bSubmitBasket.Click += buttonClicked;
            bSubmitBasket.Font = myFont;

            //bSubmitBasket.Font = myFont;


            double totalPrice = 0;
            string[] ORDER;
            for (int i = 0; i < myBasket.getOrdersCount(); i++)
            {
                ORDER = myBasket.getOrderDetailsString(i);
                bRemove[i] = new Button();
                bRemove[i].Parent = pBasket;
                bRemove[i].Text = "Pašalinti";
                bRemove[i].Name = "bRemove";
                for (int j = 0; j < columnTitles.Length - 1; j++)
                {
                    
                    tbOrdersMatrix[i, j] = new TextBox();
                    tbOrdersMatrix[i, j].Font = myFont;
                    tbOrdersMatrix[i, j].Parent = pBasket;
                    tbOrdersMatrix[i, j].ReadOnly = true;
                    tbOrdersMatrix[i, j].Location = new Point(tbTitleBar[j].Location.X, tbTitleBar[j].Size.Height * (i+1) );
                    tbOrdersMatrix[i, j].Size = tbTitleBar[j].Size;
                    tbOrdersMatrix[i, j].BackColor = (i % 2 == 0) ? Color.LightGray : Color.WhiteSmoke;

                }
                bRemove[i].Tag = i;
                bRemove[i].Size = tbTitleBar[columnTitles.Length - 1].Size;
                bRemove[i].Location = new Point(tbTitleBar[columnTitles.Length - 1].Location.X, tbOrdersMatrix[i,0].Location.Y);
                bRemove[i].Click += buttonClicked;
                if (myBasket.isLocked(i)) { bRemove[i].Enabled = false; bRemove[i].Text = "Užsakyta"; }


                tbOrdersMatrix[i, 0].Text = i+1 + "\t" + ORDER[0]; // eil. nr ir pavadinimas
                tbOrdersMatrix[i, 1].Text = ORDER[2]; // porcijų skaičius
                tbOrdersMatrix[i, 2].Text = ORDER[1]; // vienos porcijos kaina
                tbOrdersMatrix[i, 3].Text = (Convert.ToDouble(ORDER[1]) * Convert.ToDouble(ORDER[2])).ToString(); // tarpinė suma
                totalPrice+=Convert.ToDouble(ORDER[1]) * Convert.ToDouble(ORDER[2]);
            }

            tbSummary.Text += totalPrice + " Lt";
            tbSummary.ReadOnly = true;
            pBasket.BorderStyle = BorderStyle.Fixed3D;
            pBasket.Visible = true;
            pMAIN.Visible = true;
        } // showBasket








        protected int sendToServer(string DATA, string IP, int PORT) {

            try
            {
                System.Net.Sockets.Socket soc = new System.Net.Sockets.Socket(
                        System.Net.Sockets.AddressFamily.InterNetwork,
                        System.Net.Sockets.SocketType.Stream,
                        System.Net.Sockets.ProtocolType.Tcp);
                System.Net.IPAddress ipAdd = System.Net.IPAddress.Parse(IP);
                System.Net.IPEndPoint remoteEP = new System.Net.IPEndPoint(ipAdd, PORT);
                soc.Connect(remoteEP);

                byte[] byData = System.Text.Encoding.UTF8.GetBytes(DATA);
                soc.Send(byData);
                soc.Close();
                return 0;
            }
            catch (Exception e) {MessageBox.Show(e.ToString()); return -1;}
        }


        private int sendDataFromFILE(string FILE, string IP, int PORT) {
            System.IO.StreamReader sr = new System.IO.StreamReader(FILE);

            System.Net.Sockets.TcpClient tcpClient = new System.Net.Sockets.TcpClient();
            tcpClient.Connect(new System.Net.IPEndPoint(System.Net.IPAddress.Parse(IP), PORT));

            byte[] buffer = new byte[1500];
            long bytesSent = 0;

            while (bytesSent < sr.BaseStream.Length)
            {
                int bytesRead = sr.BaseStream.Read(buffer, 0, 1500);
                tcpClient.GetStream().Write(buffer, 0, bytesRead);
                Console.WriteLine(bytesRead + " bytes sent.");

                bytesSent += bytesRead;
            }

            tcpClient.Close();

            return 0;

            //Console.WriteLine("finished");
            //Console.ReadLine();
        
        }






        System.Net.Sockets.Socket sListener;
        System.Net.Sockets.Socket handler;
        System.Net.IPEndPoint ipEndPoint;

        // Listening sockets will be needed to receive instructions from MASTER.

        private void Create_Socket(object sender, EventArgs e)
        {
            try
            {
                System.Net.SocketPermission permission;

                permission = new System.Net.SocketPermission(
                System.Net.NetworkAccess.Accept,
                System.Net.TransportType.Tcp,
                "",
                System.Net.SocketPermission.AllPorts
                );

                sListener = null;

                permission.Demand();

                System.Net.IPHostEntry ipHost = System.Net.Dns.GetHostEntry(masterIP);

                System.Net.IPAddress ipAddr = ipHost.AddressList[0];

                ipEndPoint = new System.Net.IPEndPoint(System.Net.IPAddress.Any, masterPORT);

                sListener = new System.Net.Sockets.Socket(
                    ipAddr.AddressFamily,
                    System.Net.Sockets.SocketType.Stream,
                    System.Net.Sockets.ProtocolType.Tcp
                    );

                sListener.Bind(ipEndPoint);

                tbStatus.Text = "Server started.";

                setSocketToListen(sListener);
            }
            catch (Exception exc) { MessageBox.Show(exc.ToString()); }
        }


        protected void setSocketToListen(System.Net.Sockets.Socket sListener)
        {
            try {
                sListener.Listen(10);

                AsyncCallback aCallback = new AsyncCallback(AcceptCallback);
                sListener.BeginAccept(aCallback, sListener);

                tbStatus.Text = "Server is now listening on " + ipEndPoint.Address + " port: " + ipEndPoint.Port;

            }
            catch (Exception exc) { MessageBox.Show(exc.ToString()); }
        }


        protected void AcceptCallback(IAsyncResult ar)
        {
            System.Net.Sockets.Socket listener = null;

            System.Net.Sockets.Socket handler = null;
            try {
                byte[] buffer = new byte[1024];
                
                listener = (System.Net.Sockets.Socket)ar.AsyncState;

                handler = listener.EndAccept(ar);


                handler.NoDelay = false;

                object[] obj = new object[2];
                obj[0] = buffer;
                obj[1] = handler;

                handler.BeginReceive(
                    buffer,
                    0,
                    buffer.Length,
                    System.Net.Sockets.SocketFlags.None,
                    new AsyncCallback(ReceiveCallback),
                    obj);

                AsyncCallback aCallback = new AsyncCallback(AcceptCallback);
                listener.BeginAccept(aCallback, listener);
            }
            catch (Exception exc) { MessageBox.Show(exc.ToString()); }
        }


        protected void ReceiveCallback(IAsyncResult ar) {
            try {
                object[] obj = new object[2];
                obj = (object[])ar.AsyncState;

                byte[] buffer = (byte[])obj[0];

                handler = (System.Net.Sockets.Socket)obj[1];

                string content = string.Empty;


                int bytesRead = handler.EndReceive(ar);

                if (bytesRead > 0) content += Encoding.UTF8.GetString(buffer, 0, bytesRead);
                else{
                    byte[] buffernew = new byte[1024];
                    obj[0] = buffernew;
                    obj[1] = handler;
                    handler.BeginReceive(buffernew, 0, buffernew.Length,
                        System.Net.Sockets.SocketFlags.None,
                        new AsyncCallback(ReceiveCallback), obj);
                }

                    tbStatus.Text += "\n" + content;
            }
            catch (Exception exc) { MessageBox.Show(exc.ToString()); }
        }


    }
}

