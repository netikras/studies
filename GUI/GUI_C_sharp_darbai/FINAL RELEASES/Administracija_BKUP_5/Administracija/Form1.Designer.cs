using System;
using System.Text;
using System.Data;
using System.Drawing;
using System.Windows;
using System.Collections;
using System.Windows.Forms;
using System.ComponentModel;
//using System.Data.SqlClient;
using System.Data.SqlServerCe;


namespace Administracija
{
    partial class fMAIN
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


        #region Declaring vital variables

        private int sizeH;
        private int sizeW;
        private int sizeBorder;
        private int spacing;
        private string pathSeparator;
        private string NewLine;
        private Button bNULL;
        private Font myFont;

        #endregion



        #region Declaring GUI structures

        // Main panels
        Panel pFORM;
        Panel pTOP;
        Panel pMAIN;
        Panel pLEFT;
        Panel pRIGHT;

        // window-specific panels
        Panel pNEWleft;
        Panel pNEWright;
        Panel pBILLleft;
        Panel pBILLright;
        Panel pINFOleft;
        Panel pINFOright;

        Button[] btsTOP;

        #region Setting objects needed for bNEW
        Button[] bSubmit; // { [0]->left, [1]->right_calculate, [2]->right 
        //TextBox tbLaisvas;
        TextBox[] tbKaina;
        DateTimePicker[] datesCalendar; // { [0]->FROM, [1]->TO }  // left
        DateTimePicker[] datesCalendar2; // { [0]->FROM, [1]->TO } // right
        CheckBox chkbSmoking;
        CheckBox cbxRuko;
        ComboBox dropRukomas;
        ComboBox dropVietos;
        ComboBox dropNumeris;
        ComboBox dropLaisvas;

        TextBox[] tbNEWfields; // left
        Label[] lbNEWfields;  // left
        Label[] lbNEWroom;  // right
        #endregion

        #region Setting objects needed for bBill
        ComboBox dropBILLroom;
        TextBox[] tbsBill;
        Label[] lblsBill;
        TextBox tbPAYrBaras;
        TextBox tbPAYrKambarys;
        TextBox tbPAYrVISO;
        Button bPay;
        string[] sLabelsBILL = {
                              "Numeris:Numeris"
                              ,"Vardas:Vardas"
                              ,"Pavarde:Pavardė"
                              ,"Adresas:Adresas"
                              ,"Dokumentas:Dokumentas"
                              ,"DokID:Nokumento nr."
                              ,"MokKort:Mokėjimo kort. nr."
                              ,"Nuo:Apsistojęs nuo"
                              ,"Iki:Apsistojęs iki"
                              ,"Kaina:Numerio kaina"
                              ,"SumaBaras:Baro sąskaita"
                              ,"SumaKambarys:Kambario sąskaita"
                              };
        #endregion

        #region Setting objects needed for bINFO

        TextBox[] tbsINFOl;
        Label[] lblsINFOl;

        ComboBox dropINFOl;
        string[] sLabelsINFOl = { 
                                "VISO"
                                , "Rūkomi"
                                , "      1-viečiai"
                                , "      2-viečiai"
                                , "      3-viečiai"
                                , "Nerūkomi"
                                , "      1-viečiai"
                                , "      2-viečiai"
                                , "      3-viečiai"

                                };
        ComboBox[] dropINFOr;
        TextBox tbINFOr;
        Button bINFOr;
        Label[] lblsINFOr;
        string[] sLabelsINFOr = { 
                                  "Kambario nr."
                                , "--------------"
                                , "Rūkomas"
                                , "Vietos"
                                , "Kaina"
                                //," "
                                ,"Laisvas"
                                };

        #endregion

        #endregion



        #region Declaring values/variables for DB connection

        SqlCeConnection mySQLceConnection;
        DataTable myDataTableCustomers;
        DataTable myDataTableRooms;
        DataTable dtbl;

        #endregion


        #region Declaring and initiating STRING arrays for labels and textboxes (HANDLE WITH EXTREME CAUTION)

        private string[] sTBClientLeft = {
                            "Vardas"
                           ,"Pavarde"
                           ,"Adresas"
                           ,"Dokumentas"
                           ,"DokID"
                           ,"MokKort"
                           ,"Nuo"
                           ,"Iki"
                           ,"Ruko"
                           };
        private string[] sLBClientLeft = {
                            "Vardas"
                           ,"Pavardė"
                           ,"Adresas"
                           ,"Dokumentas"
                           ,"Dokumento nr."
                           ,"Mokėjimo kort. nr."
                           ,"Apsistoja nuo"
                           ,"Apsistoja iki"
                           ,"Rūko"
                           };

        private string[] sTBClientRight = {
                             "Rukomas"
                            ,"Vietos"
                            ,"Laisvas"
                            ,"Nuo"
                            ,"Iki"
                            ,"Numeris"
                            ,"Kaina"
                            };
        private string[] sLBClientRight = { 
                            "Rūkomas"
                           ,"Vietų sk."
                           ,"Laisvas kamb."
                           ,"Viešnagė NUO"
                           ,"Viešnagė IKI"
                           ,"Kambario numeris"
                           ,"Kaina"
                            };

        #endregion

        private void setEnvironment() {  Form thisForm = this; /*
        private void setEnvironment(object sender, EventArgs e) {
            Form thisForm = sender as Form;
            //*/

            spacing = sizeW / 60;
            bNULL = new Button();
            bNULL.Visible = false;
            myFont = new Font(bNULL.Font.Name, bNULL.Font.SizeInPoints * 3 / 2);
            pathSeparator = System.IO.Path.DirectorySeparatorChar.ToString();
            NewLine = Environment.NewLine;

            mySQLceConnection = MySQL_makeConnection();

            #region Setting-up main panels

            pFORM = new Panel();
            pTOP = new Panel();
            pMAIN = new Panel();
            pLEFT = new Panel();
            pRIGHT = new Panel();

            pFORM.Parent = this;
            pTOP.Parent = pFORM;
            pMAIN.Parent = pFORM;
            pLEFT.Parent = pMAIN;
            pRIGHT.Parent = pMAIN;

            pFORM.Location = new Point(0,0);
            //pFORM.Size = thisForm.Size;
            pFORM.Size = new Size(
                    sizeW + sizeBorder,
                    sizeH + sizeBorder
                    );
            pTOP.Location = new Point(
                    spacing,
                    spacing
                    );
                pTOP.Size = new Size(
                        pFORM.Size.Width - spacing*2,
                        (pFORM.Size.Height-spacing*2)/9
                        );
            pMAIN.Location = new Point(
                    pTOP.Location.X,
                    pTOP.Location.Y + pTOP.Size.Height + spacing*2
                    );
                pMAIN.Size = new Size(
                        pTOP.Size.Width,
                        pFORM.Size.Height - pMAIN.Location.Y - spacing
                        );
            pLEFT.Location = new Point(
                    spacing,
                    spacing
                    );
                pLEFT.Size = new Size(
                        (pMAIN.Size.Width - spacing*3) / 2,
                        pMAIN.Size.Height - spacing*2
                        );
            pRIGHT.Location = new Point(
                    pLEFT.Location.X + pLEFT.Size.Width + spacing,
                    pLEFT.Location.Y
                    );
                 pRIGHT.Size = new Size(
                        (pMAIN.Size.Width - spacing * 3) - pLEFT.Size.Width,
                        pMAIN.Size.Height - spacing * 2
                        );


            pFORM.BorderStyle = BorderStyle.FixedSingle;
            pTOP.BorderStyle = BorderStyle.Fixed3D;
            pMAIN.BorderStyle = BorderStyle.Fixed3D;
            pLEFT.BorderStyle = BorderStyle.Fixed3D;
            pRIGHT.BorderStyle = BorderStyle.Fixed3D;

            #endregion


            #region Setting-up window-specific panels

            /*
                pNEWleft
                pNEWright
                pBILLleft
                pBILLright
                pINFOleft
                pINFOright
            */

            pNEWleft = new Panel();
            pNEWright = new Panel();
            pBILLleft = new Panel();
            pBILLright = new Panel();
            pINFOleft = new Panel();
            pINFOright = new Panel();
            
            /*
            pNEWleft.Parent = pMAIN;
            pNEWright.Parent = pMAIN;
            pBILLleft.Parent = pMAIN;
            pBILLright.Parent = pMAIN;
            pINFOleft.Parent = pMAIN;
            pINFOright.Parent = pMAIN;
            /*
            pNEWleft.Location = pLEFT.Location;
            pNEWright.Location = pRIGHT.Location;
            pBILLleft.Location = pLEFT.Location;
            pBILLright.Location = pRIGHT.Location;
            pINFOleft.Location = pLEFT.Location;
            pINFOright.Location = pRIGHT.Location;
            */

            pNEWleft.Parent = pLEFT;
            pNEWright.Parent = pRIGHT;
            pBILLleft.Parent = pLEFT;
            pBILLright.Parent = pRIGHT;
            pINFOleft.Parent = pLEFT;
            pINFOright.Parent = pRIGHT;
            
            pNEWleft.Visible = true;
            pNEWright.Visible = false;
            pBILLleft.Visible = false;
            pBILLright.Visible = false;
            pINFOleft.Visible = false;
            pINFOright.Visible = false;
            
            pNEWleft.Size = pLEFT.Size;
            pNEWright.Size = pRIGHT.Size;
            pBILLleft.Size = pLEFT.Size;
            pBILLright.Size = pRIGHT.Size;
            pINFOleft.Size = pLEFT.Size;
            pINFOright.Size = pRIGHT.Size;

            pNEWleft.BorderStyle = pLEFT.BorderStyle;
            pNEWright.BorderStyle = pRIGHT.BorderStyle;
            pBILLleft.BorderStyle = pLEFT.BorderStyle;
            pBILLright.BorderStyle = pRIGHT.BorderStyle;
            pINFOleft.BorderStyle = pLEFT.BorderStyle;
            pINFOright.BorderStyle = pRIGHT.BorderStyle;
            
            #endregion


            #region Setting-up NEW CLIENT panel
            bSubmit = new Button[3];

            #region LEFT

            lbNEWfields = new Label[sTBClientLeft.Length];
            tbNEWfields = new TextBox[sTBClientLeft.Length];
            datesCalendar = new DateTimePicker[2];
            chkbSmoking = new CheckBox();
            int j=0;
            
            for (int i = 0; i < sTBClientLeft.Length; i++) {
                lbNEWfields[i] = new Label();
                lbNEWfields[i].Name = sLBClientLeft[i];
                lbNEWfields[i].Parent = pNEWleft;
                lbNEWfields[i].Text = sLBClientLeft[i];
                lbNEWfields[i].Font = myFont;
                lbNEWfields[i].Size = new Size(
                    (pLEFT.Size.Width-spacing*2)/3
                   ,(pLEFT.Size.Height - spacing*(2+sLBClientLeft.Length)/2)/(sLBClientLeft.Length)
                    );
                lbNEWfields[i].Location = new Point(
                    spacing/2
                   ,spacing/2 + lbNEWfields[0].Size.Height*i
                    );

                
                if (sTBClientLeft[i] == "Nuo" || sTBClientLeft[i] == "Iki") {
                    datesCalendar[j] = new DateTimePicker();
                    if (j == 1) datesCalendar[j].Value = DateTime.Now.AddDays(1);
                    datesCalendar[j].Parent = lbNEWfields[i].Parent;
                    datesCalendar[j].Tag = j;
                    //datesCalendar[j].Font = myFont;
                    datesCalendar[j].Location = new Point(
                        lbNEWfields[i].Location.X + lbNEWfields[i].Size.Width + spacing
                       ,lbNEWfields[i].Location.Y
                        );
                    datesCalendar[j].Name = sTBClientLeft[i];
                    datesCalendar[j].Size = tbNEWfields[0].Size;
                    datesCalendar[j].ValueChanged += calendar_changed;
                    j++;

                    continue;
                    // * */
                }
                
                if (sTBClientLeft[i] == "Ruko") {
                    cbxRuko = new CheckBox();
                    cbxRuko.Name = sTBClientLeft[i];
                    cbxRuko.Parent = pNEWleft;
                    cbxRuko.CheckedChanged += checkbox_checked;
                    cbxRuko.Location = new Point(
                        lbNEWfields[i].Location.X + lbNEWfields[i].Size.Width + spacing
                       , lbNEWfields[i].Location.Y
                        );
                 /*   cbxRuko.Size = new Size(
                        lbNEWfields[i].Size.Height
                       ,lbNEWfields[i].Size.Height
                        );
                 // */
                    continue;
                }

                
                tbNEWfields[i] = new TextBox();
                tbNEWfields[i].Name = sTBClientLeft[i];
                tbNEWfields[i].Parent = lbNEWfields[i].Parent;
                tbNEWfields[i].Name = sTBClientLeft[i];
                tbNEWfields[i].Font = myFont;
                tbNEWfields[i].Location = new Point(
                    lbNEWfields[i].Location.X + lbNEWfields[i].Size.Width + spacing
                   ,lbNEWfields[i].Location.Y
                    );
                tbNEWfields[i].Size = lbNEWfields[i].Size;
            }

            bSubmit[0] = new Button();
            bSubmit[0].Parent = lbNEWfields[0].Parent;
            bSubmit[0].Text = "Tęsti >>";
            bSubmit[0].Location = new Point(
                    lbNEWfields[0].Location.X + lbNEWfields[0].Size.Width + spacing
                   , lbNEWfields[sLBClientLeft.Length - 1].Location.Y + lbNEWfields[sLBClientLeft.Length - 1].Height + spacing
                    );
            bSubmit[0].Name = "bSubmit_0";
            bSubmit[0].Click += clicked_button;
            bSubmit[0].Font = myFont;
            bSubmit[0].Size = tbNEWfields[0].Size;

            

            #endregion

            #region RIGHT
            /*
             {
             "Rukomas"
            ,"Vietos"
            ,"Laisvas"
            ,"Nuo"
            ,"Iki"
            ,"Numeris"
            ,"Kaina"
            };
             */

            lbNEWroom = new Label[sLBClientRight.Length];
            tbKaina = new TextBox[2];
            datesCalendar2 = new DateTimePicker[datesCalendar.Length];
            j=0;

            for (int i = 0; i < sLBClientRight.Length; i++)
            {

                #region setting-up label
                lbNEWroom[i] = new Label();
                lbNEWroom[i].Name = sLBClientRight[i];
                lbNEWroom[i].Parent = pNEWright;
                lbNEWroom[i].Text = sLBClientRight[i];
                lbNEWroom[i].Font = myFont;
                //lbNEWroom[i].Size = new Size(
                //    (pRIGHT.Size.Width - spacing * 2) / 3
                //   , (pRIGHT.Size.Height - spacing * (2 + sLBClientRight.Length) / 2) / sLBClientRight.Length
                //    );
                lbNEWroom[i].Size = lbNEWfields[0].Size;
                lbNEWroom[i].Location = new Point(
                    spacing / 2
                   , spacing / 2 + lbNEWroom[0].Size.Height * i
                    );
                #endregion

                #region Setting-up fields
                switch (sTBClientRight[i]) { 
                    case "Rukomas":
                        #region Rukomas
                        dropRukomas = new ComboBox();
                        dropRukomas.AllowDrop = true;
                        dropRukomas.DropDownStyle = ComboBoxStyle.DropDownList;
                        dropRukomas.Items.Add("Ne");
                        dropRukomas.Items.Add("Taip");
                        dropRukomas.SelectedIndex = 0;
                        dropRukomas.Font = myFont;
                        dropRukomas.Parent = pNEWright;
                        dropRukomas.Width = dropRukomas.Height * 3;
                        dropRukomas.Location = new Point(
                            lbNEWroom[i].Location.X + lbNEWroom[i].Size.Width + spacing
                           ,lbNEWroom[i].Location.Y
                        );
                        #endregion
                        break;
                    case "Vietos":
                        #region Vietos
                        dropVietos = new ComboBox();
                        dropVietos.AllowDrop = true;
                        dropVietos.DropDownStyle = ComboBoxStyle.DropDownList;
                        dropVietos.Items.Add("1");
                        dropVietos.Items.Add("2");
                        dropVietos.Items.Add("3");
                        dropVietos.SelectedIndex = 0;
                        dropVietos.Font = myFont;
                        dropVietos.Parent = pNEWright;
                        dropVietos.Width = dropVietos.Height * 2;
                        dropVietos.Location = new Point(
                            lbNEWroom[i].Location.X + lbNEWroom[i].Size.Width + spacing
                           ,lbNEWroom[i].Location.Y
                        );
                        #endregion
                        break;
                    case "Laisvas":
                        #region Laisvas
                        //lbNEWroom[i].Visible = false;
                        dropLaisvas = new ComboBox();
                        dropLaisvas.AllowDrop = true;
                        dropLaisvas.DropDownStyle = ComboBoxStyle.DropDownList;
                        dropLaisvas.Items.Add("Ne");
                        dropLaisvas.Items.Add("Taip");
                        dropLaisvas.SelectedIndex = 1;
                        dropLaisvas.Enabled = false;
                        dropLaisvas.Font = myFont;
                        dropLaisvas.Parent = pNEWright;
                        dropLaisvas.Width = dropLaisvas.Height * 3;
                        dropLaisvas.Location = new Point(
                            lbNEWroom[i].Location.X + lbNEWroom[i].Size.Width + spacing
                           ,lbNEWroom[i].Location.Y
                        );// */
                        #endregion
                        break;
                    case "Nuo":
                    case "Iki":
                        #region Nuo - Iki
                        datesCalendar2[j] = new DateTimePicker();
                        datesCalendar2[j].Parent = pNEWright;
                        datesCalendar2[j].Location = new Point(
                            lbNEWroom[i].Location.X + lbNEWroom[i].Size.Width + spacing
                           ,lbNEWroom[i].Location.Y
                           );
                        datesCalendar2[j].Tag = j;
                        datesCalendar2[j].Size = datesCalendar[j].Size;
                        datesCalendar2[j].Value = datesCalendar[j].Value;
                        datesCalendar2[j].Enabled = false;
                        j++;
                        #endregion
                        break;
                    case "Numeris":
                        #region Numeris
                        bSubmit[1] = new Button();
                        bSubmit[1].Parent = pNEWright;
                        bSubmit[1].Text = "Ieškoti";
                        bSubmit[1].Font = myFont;
                        bSubmit[1].Location = new Point(
                            lbNEWroom[i].Location.X + lbNEWroom[i].Size.Width + spacing
                           ,lbNEWroom[i].Location.Y
                        );
                        bSubmit[1].Width = lbNEWroom[i].Width/4*3;
                        bSubmit[1].Height = lbNEWroom[i].Height / 4 * 3;
                        bSubmit[1].Name = "bSubmit_1";
                        bSubmit[1].Click += clicked_button;

                        dropNumeris = new ComboBox();
                        dropNumeris.AllowDrop = true;
                        dropNumeris.DropDownStyle = ComboBoxStyle.DropDownList;
                        dropNumeris.Name = "dropNumeris";
                        dropNumeris.Enabled = false;
                        dropNumeris.Font = myFont;
                        dropNumeris.SelectedValueChanged += dropdown_picked;
                        dropNumeris.Parent = pNEWright;
                        dropNumeris.Width = dropNumeris.Height * 3;
                        dropNumeris.Location = new Point(
                            bSubmit[1].Location.X + bSubmit[1].Size.Width + spacing
                           ,lbNEWroom[i].Location.Y
                        );// */
                        bSubmit[1].Height = dropNumeris.Height;

                        bSubmit[1].Tag = dropNumeris;
                        #endregion
                        break;
                    case "Kaina":
                        #region Kaina
                        for(int k=0; k<2; k++){
                            tbKaina[k] = new TextBox();
                            tbKaina[k].Parent = pNEWright;
                            tbKaina[k].Location = new Point(
                                lbNEWroom[i].Location.X + lbNEWroom[i].Size.Width + spacing + (spacing/2 + tbKaina[k].Width)*k
                               ,lbNEWroom[i].Location.Y
                                );
                            //tbKaina[k].Enabled = false;
                            tbKaina[k].ReadOnly = true;
                            tbKaina[k].Height = tbNEWfields[0].Height;
                            tbKaina[k].Font = myFont;
                        }
                        

                        #endregion
                        break;
                    default:
                        break;
                }
                #endregion



            }
            //MessageBox.Show(datesCalendar2[0].Value.ToShortDateString());

            bSubmit[2] = new Button();
            bSubmit[2].Parent = lbNEWroom[0].Parent;
            bSubmit[2].Text = "Rezervuoti";
            bSubmit[2].Location = new Point(
                    lbNEWfields[0].Location.X + lbNEWfields[0].Size.Width + spacing
                   , lbNEWfields[sLBClientLeft.Length - 1].Location.Y + lbNEWfields[sLBClientLeft.Length - 1].Height + spacing
                    );
            bSubmit[2].Name = "bSubmit_2";
            bSubmit[2].Click += clicked_button;
            bSubmit[2].Font = myFont;
            bSubmit[2].Size = tbNEWfields[1].Size;

            #endregion

            #endregion

            #region Setting-up PAY CHECK panel
            

            #region LEFT
            dropBILLroom = new ComboBox();
            ComboBox dropBILLperson = new ComboBox();
            tbsBill = new TextBox[sLabelsBILL.Length+1];
            lblsBill = new Label[sLabelsBILL.Length+1];

            for (int i = 0; i < sLabelsBILL.Length; i++) {
                
                lblsBill[i] = new Label();
                lblsBill[i].Parent = pBILLleft;
                lblsBill[i].Size = lbNEWfields[0].Size;
                lblsBill[i].Size = new Size(
                    (pBILLleft.Size.Width - spacing * 2) / 3
                   , (pBILLleft.Size.Height - spacing * (3 + sLabelsBILL.Length) / 2) / (sLabelsBILL.Length)
                    );
                lblsBill[i].Location = new Point(
                    spacing / 2
                   , spacing + (spacing / 2 + lblsBill[0].Size.Height) * i 
                    );
                lblsBill[i].Text = sLabelsBILL[i].Split(':')[1];
                lblsBill[i].Font = myFont;


                if (i == 0) {
                    dropBILLroom.Parent = lblsBill[i].Parent;
                    dropBILLroom.SelectedValueChanged += dropdown_picked;
                    dropBILLroom.DropDownStyle = ComboBoxStyle.DropDownList;
                    dropBILLroom.Name = "dropBILLroom";
                    dropBILLroom.Location = new Point(
                        lblsBill[i].Location.X + lblsBill[i].Width + spacing
                        ,lblsBill[i].Location.Y
                        );
                    dropBILLroom.Font = myFont;
                    dropBILLroom.Width = 3 *dropBILLroom.Height;
                    dropBILLroom.Height = dropBILLroom.Height / 3 * 2;
                    for (int m = 0; m < 55; m++) dropBILLroom.Items.Add(m+1);

                    dropBILLperson.Parent = lblsBill[i].Parent;
                    dropBILLperson.SelectedValueChanged += dropdown_picked;
                    dropBILLperson.DropDownStyle = ComboBoxStyle.DropDownList;
                    dropBILLperson.Name = "dropBILLperson";
                    dropBILLperson.Location = new Point(
                        dropBILLroom.Location.X + dropBILLroom.Width + spacing/2
                        , dropBILLroom.Location.Y
                        );
                    dropBILLperson.Font = myFont;
                    dropBILLperson.Visible = false;
                    dropBILLperson.Width = 3 * dropBILLroom.Width;
                    dropBILLperson.Height = dropBILLroom.Height;

                    continue;
                }

                //*
                tbsBill[i] = new TextBox();
                tbsBill[i].Parent = pBILLleft;
                tbsBill[i].Location = new Point(
                    lblsBill[i].Location.X + lblsBill[i].Width +spacing
                   ,lblsBill[i].Location.Y
                    );
                //tbsBill[i].Size = lblsBill[i].Size;
                tbsBill[i].Size = new Size(
                    (pBILLleft.Width - tbsBill[i].Location.X)/3*2 - spacing
                   , lblsBill[i].Height
                   );
                tbsBill[i].ReadOnly = true;
                tbsBill[i].Font = myFont;
                tbsBill[i].Name = sLabelsBILL[i].Split(':')[0];
                //*/
            }
            dropBILLroom.SelectedIndex = 0;
            #endregion

            #region RIGHT
            tbPAYrBaras = new TextBox();
            tbPAYrKambarys = new TextBox();
            tbPAYrVISO = new TextBox();
            Label lblPAYrBaras = new Label();
            Label lblPAYrKambarys = new Label();
            Label lblPAYrViso = new Label();
            bPay = new Button();

            lblPAYrBaras.Parent = pBILLright;
            lblPAYrKambarys.Parent = pBILLright;
            lblPAYrViso.Parent = pBILLright;
            tbPAYrBaras.Parent = pBILLright;
            tbPAYrKambarys.Parent = pBILLright;
            tbPAYrVISO.Parent = pBILLright;
            bPay.Parent = pBILLright;

            lblPAYrViso.Text = "Mokėtina suma";
            lblPAYrBaras.Text = "Baro išlaidos";
            lblPAYrKambarys.Text = "Kambario nuoma";
            bPay.Text = "SUMOKĖTI";
            bPay.Click += clicked_button;

            bPay.Font = myFont;
            tbPAYrBaras.Font = myFont;
            tbPAYrVISO.Font = myFont;
            lblPAYrKambarys.Font = myFont;
            lblPAYrBaras.Font = myFont;
            tbPAYrKambarys.Font = myFont;
            lblPAYrViso.Font = myFont;

            bPay.Name = "bPay";
            tbPAYrKambarys.ReadOnly = true;
            tbPAYrVISO.ReadOnly = true;

            tbPAYrBaras.Name = "tbPAYrBaras";
            tbPAYrBaras.TextChanged += textbox_changed;


            lblPAYrBaras.Width =
                lblPAYrViso.Width =
                lblPAYrKambarys.Width =
                    lblsBill[0].Width;
            lblPAYrBaras.Height =
                lblPAYrViso.Height =
                lblPAYrKambarys.Height =
                    lblsBill[0].Height;

            lblPAYrBaras.Location = new Point(
                    spacing
                   ,spacing*2
                );
            lblPAYrKambarys.Location = new Point(
                    spacing
                   ,spacing + lblPAYrBaras.Location.Y + lblPAYrBaras.Height
                );
            lblPAYrViso.Location = new Point(
                    spacing
                   , spacing + lblPAYrKambarys.Location.Y + (lblPAYrKambarys.Height * 2 +spacing)
                );

            tbPAYrBaras.Size =
                tbPAYrKambarys.Size =
                tbPAYrVISO.Size =
                    new Size(
                        tbsBill[1].Width/2
                       ,tbsBill[1].Height
                        );
//                    tbsBill[1].Size;
            

            tbPAYrBaras.Location = new Point(
                    lblPAYrBaras.Location.X + lblPAYrBaras.Width +spacing
                   ,lblPAYrBaras.Location.Y
                );
            tbPAYrKambarys.Location = new Point(
                    tbPAYrBaras.Location.X
                   , lblPAYrKambarys.Location.Y
                );
            tbPAYrVISO.Location = new Point(
                    tbPAYrBaras.Location.X
                   , lblPAYrViso.Location.Y
                );

            bPay.Size = new Size(
                    pBILLright.Width - spacing*4
                   ,lblPAYrViso.Height*2
                );
            bPay.Location = new Point(
                    spacing*2
                   ,lblPAYrViso.Location.Y + (lblPAYrViso.Height+spacing)*2
                );

            #endregion

            #endregion

            #region Setting-up INFO panel

            #region LEFT

            tbsINFOl = new TextBox[sLabelsBILL.Length];

            pINFOleft.AutoScroll = true;
            dropINFOl = new ComboBox();
            lblsINFOl = new Label[sLabelsINFOl.Length];

            dropINFOl.Parent = pINFOleft;
            dropINFOl.Font = myFont;
            dropINFOl.Height = dropLaisvas.Height;
            dropINFOl.Width = (pINFOleft.Width - spacing)/2;
            dropINFOl.Location = new Point(
                    spacing
                  , spacing
                );
            dropINFOl.Items.Add("Laisvi kambariai");
            dropINFOl.Items.Add("Rezervuoti kambariai");
            dropINFOl.Items.Add("Užimti kambariai");
            dropINFOl.DropDownStyle = ComboBoxStyle.DropDownList;
            dropINFOl.Name = "dropINFOl";
            dropINFOl.SelectedIndex = 0;
            dropINFOl.SelectedValueChanged += dropdown_picked;

            for (int i = 0; i < sLabelsINFOl.Length; i++) {
                lblsINFOl[i] = new Label();
                lblsINFOl[i].Parent = pINFOleft;
                lblsINFOl[i].Text = sLabelsINFOl[i];
                lblsINFOl[i].Font = myFont;
                lblsINFOl[i].Width = lblsINFOl[i].Width * 3 / 2;
                lblsINFOl[i].Location = new Point(
                    spacing * 2
                   , dropINFOl.Height + dropINFOl.Location.Y +spacing + (spacing / 2 + lblsINFOl[i].Size.Height) * i
                    );
                //lblsINFOl[i].BorderStyle = BorderStyle.Fixed3D;

                if (i == 1 || i == 5) continue;
                tbsINFOl[i] = new TextBox();
                tbsINFOl[i].Parent = lblsINFOl[i].Parent;
                tbsINFOl[i].Size = new Size(
                        pINFOleft.Width - spacing - lblsINFOl[i].Location.X - lblsINFOl[i].Width
                      , lblsINFOl[i].Height
                    );
                tbsINFOl[i].Location = new Point(
                        lblsINFOl[i].Location.X + lblsINFOl[i].Width +spacing/2
                      , lblsINFOl[i].Location.Y
                    );
                tbsINFOl[i].ReadOnly = true;
                if (i == 0) tbsINFOl[i].Width = tbsINFOl[i].Height * 2;
            }
            #endregion

            #region RIGHT
            lblsINFOr = new Label[sLabelsINFOr.Length];
            dropINFOr = new ComboBox[sLabelsINFOr.Length];
            

            for (int i = 0; i < sLabelsINFOr.Length; i++) {
                lblsINFOr[i] = new Label();
                lblsINFOr[i].Parent = pINFOright;
                lblsINFOr[i].Text = sLabelsINFOr[i];
                lblsINFOr[i].Font = myFont;
                lblsINFOr[i].Width = lblsINFOr[i].Width * 3 / 2;
                lblsINFOr[i].Location = new Point(
                    spacing * 2
                   , spacing + (spacing / 2 + lblsINFOr[i].Size.Height) * i
                    );

                if (i == 1) continue;
                if (i == 4) {
                    tbINFOr = new TextBox();
                    tbINFOr.Parent = lblsINFOr[i].Parent;
                    tbINFOr.Location = new Point(
                            dropINFOr[0].Location.X,
                            lblsINFOr[i].Location.Y
                        );
                    tbINFOr.Height = dropINFOr[0].Height;
                    tbINFOr.Width = dropINFOr[0].Width;
                    tbINFOr.Font = myFont;
                    continue;
                }

                dropINFOr[i] = new ComboBox();
                dropINFOr[i].Parent = lblsINFOr[i].Parent;
                dropINFOr[i].DropDownStyle = ComboBoxStyle.DropDownList;
                dropINFOr[i].Location = new Point(
                        lblsINFOr[i].Location.X + lblsINFOr[i].Width +spacing
                      , lblsINFOr[i].Location.Y
                    );
                dropINFOr[i].Height = lblsINFOr[i].Height;
                dropINFOr[i].Width = dropINFOr[i].Height * 3;
                dropINFOr[i].Font = myFont;
                //dropINFOr[i].Items.Add(i);


            }
            
            for (int i = 1; i <= dropBILLroom.Items.Count; i++)
                dropINFOr[0].Items.Add(i);
            dropINFOr[0].SelectedIndex = 0;
            dropINFOr[0].Name = "dropINFOr_0";
            dropINFOr[0].SelectedValueChanged += dropdown_picked;
///*
            dropINFOr[2].Items.Add("Taip");
            dropINFOr[2].Items.Add("Ne");
            dropINFOr[2].SelectedIndex = 0;
            dropINFOr[2].Name = "dropINFOr_1";

            dropINFOr[3].Items.Add(1);
            dropINFOr[3].Items.Add(2);
            dropINFOr[3].Items.Add(3);
            dropINFOr[3].SelectedIndex = 0;
            dropINFOr[3].Name = "dropINFOr_2";

            dropINFOr[5].Items.Add("Taip");
            dropINFOr[5].Items.Add("Ne");
            dropINFOr[5].SelectedIndex = 0;
            dropINFOr[5].Name = "dropINFOr_3";
            dropINFOr[5].Enabled = false;
           // */

            bINFOr = new Button();
            bINFOr.Parent = lblsINFOr[0].Parent;
            bINFOr.Text = "Pakeisti";
            bINFOr.Location = new Point(
                    dropINFOr[0].Location.X
                   , lblsINFOr[sLabelsINFOr.Length - 1].Location.Y + lblsINFOr[sLabelsINFOr.Length - 1].Height*3
                );
            bINFOr.Font = myFont;
            bINFOr.Height = tbINFOr.Height * 2;
            bINFOr.Width = tbINFOr.Width * 2;
            bINFOr.Name = "bINFOr";
            bINFOr.Click += clicked_button;


            #endregion
            #endregion

        }


        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        
        private void InitializeComponent()
        {
            //setEnvironment();

            #region Getting window border size, height and width

            sizeH = //Screen.PrimaryScreen.Bounds.Height 
                 Screen.PrimaryScreen.WorkingArea.Height
                - SystemInformation.CaptionHeight;
            sizeBorder = Screen.PrimaryScreen.Bounds.Height 
                       - Screen.PrimaryScreen.WorkingArea.Height
                       - SystemInformation.CaptionHeight;
            sizeH -= sizeBorder;
            sizeW = Screen.PrimaryScreen.Bounds.Width - sizeBorder;

            #endregion

            setEnvironment();

            #region Form settings

            this.SuspendLayout();
            // 
            // fMAIN
            // 
            //this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.WindowState = FormWindowState.Maximized;
            this.MaximizeBox = false;
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(sizeW, sizeH);
            this.Name = "fMAIN";
            this.Text = "Viešbučio Administracija";
            this.Load += new System.EventHandler(this.fMAIN_Load); /*
            this.Load += new System.EventHandler(setEnvironment); //*/
            this.ResumeLayout(false);

            #endregion

            #region TOP Buttons

            string[] sBtsTopLabels = { 
                                    "Registracija",
                                    "Sąskaita",
                                    "Informacija",
                                     };
            string[] sBtsTopNames = { 
                                    "bNew",
                                    "bCheck",
                                    "bInfo",
                                     };
            int iBtsTopSizeW = (pTOP.Size.Width-spacing) / sBtsTopLabels.Length - spacing/2;
            int iBtsTopSizeH = pTOP.Size.Height - spacing;

            btsTOP = new Button[sBtsTopNames.Length];
            for (int i = 0; i < sBtsTopNames.Length; i++) {
               // MessageBox.Show(i.ToString() + "/" + (sBtsTopLabels.Length- 1).ToString());
                btsTOP[i] = new Button();
                btsTOP[i].Parent = pTOP;
                btsTOP[i].Text = sBtsTopLabels[i];
                btsTOP[i].Name = sBtsTopNames[i];
                btsTOP[i].Font = myFont;
                btsTOP[i].Click += clicked_button;
                btsTOP[i].Location = new Point(spacing/2 + (iBtsTopSizeW + spacing/2) * i,spacing/2);
                btsTOP[i].Size =
                    i == 0 ?
                        new Size(iBtsTopSizeW, iBtsTopSizeH)
                        : btsTOP[0].Size
                        ;
            
            } //for

            #endregion



        }

        #endregion

        private SqlCeConnection MySQL_makeConnection(){
            try
            {
                string mySQLconnectionString = @"Data Source=LocalDB.sdf";
                mySQLceConnection = new SqlCeConnection(mySQLconnectionString);
                mySQLceConnection.Open();
                return mySQLceConnection;
            }
            catch(Exception e) { MessageBox.Show(
                "Failed to open SQL connection. Is the LocalDB.sdf file there?" + NewLine +NewLine +
                e.ToString()
                );
            return null;
            }
        }

        private DataTable MySQL_receiveQuery(SqlCeConnection connection ,string query) {
            // SELECT Numeris FROM Kambariai WHERE Rukomas='True' AND Laisvas='True' AND Vietos='2'
            try
            {
                DataTable dtb = new DataTable();
                SqlCeCommand cmd = new SqlCeCommand(query, connection);
                dtb.Load(cmd.ExecuteReader());
                return dtb;
            }
            catch (Exception e) {
                MessageBox.Show(
                    "Failed to retrieve data from the database for query:" + NewLine+query+NewLine +NewLine +
                    e.ToString()
                    );
                return null;
            }
        }

        private int MySQL_sendQuery(SqlCeConnection connection, string query){
            try
            {
                SqlCeCommand cmd = new SqlCeCommand(query, connection);
                cmd.ExecuteNonQuery();
                return 0;
            }
            catch (Exception e) {
                MessageBox.Show(
                    "Failed to send data to database. Please check if LocalDB.sdf file is available and restart application."
                    + NewLine + NewLine
                    + e.Message.ToString()
                    );
                return -1;
            }
        }



        #region Functions that are triggered by action handlers: private void action_triggered(object sender, EventArgs e){}

        private void clicked_button(object sender, EventArgs e)
        {
            Button pressed = sender as Button;
            switch (pressed.Name.ToString())
            {
                case "bNew":
                    flushAllFields();
                    
                    pNEWleft.Visible = true;
                    pNEWright.Visible = false;
                    pINFOleft.Visible = false;
                    pINFOright.Visible = false;
                    pBILLleft.Visible = false;
                    pBILLright.Visible = false;
                    
                    break;
                case "bCheck":
                    pNEWleft.Visible = false;
                    pNEWright.Visible = false;
                    pINFOleft.Visible = false;
                    pINFOright.Visible = false;
                    pBILLleft.Visible = true;
                    pBILLright.Visible = false;

                    break;
                case "bInfo":
                    pNEWleft.Visible = false;
                    pNEWright.Visible = false;
                    pINFOleft.Visible = true;
                    pINFOright.Visible = true;
                    pBILLleft.Visible = false;
                    pBILLright.Visible = false;

                    break;
                case "bSubmit_0":
                    #region bSubmit_0 -- Užpildžius naujo kliento duomenis pateikiama kambario parinkimo forma
                    bool missing = false;
                    for (int i = 0; i < 6; i++)
                        if (tbNEWfields[i].Text.Length == 0)
                            missing = true;
                    if (missing)
                    {
                        MessageBox.Show("Užpildykite visus laukus prieš spausdami \"Tęsti\"");
                        pNEWright.Visible = false;
                        break;
                    }

                    //parsePanelNEWto_Klientas();
                    pNEWright.Visible = true;
                    pNEWright.Refresh();
                    #endregion
                    break;
                case "bSubmit_1":
                    #region bSubmit_1 -- Parenkamas galimų numerių sąrašas pagal užpildytą naujo kliento formą
                    ComboBox dropRooms = pressed.Tag as ComboBox;

                    string QURY = "SELECT Numeris, Kaina FROM Kambariai WHERE "
                    +"("
                        +"Laisvas = '" + (dropLaisvas.SelectedItem == "Taip") + "' "
                        + "AND ("
                            + "Nuo >= '" + dateToInt(datesCalendar2[1]).ToString() + "' " // Ar naujakurys išsikels iki rezervacijos laiko
                            + "OR "
                            + "Iki <= '" +dateToInt(datesCalendar2[0]).ToString()+"' "  // Ar naujakurys įsikels po rezervacijos laiko
                            + "OR "
                            + "Iki IS NULL" // Tiek IKI tiek NUO null'inami kartu, tad pakanka patikrint vieną iš jų
                        + ")"
                    +") "
                    +"AND Rukomas = '" + (dropRukomas.SelectedItem == "Taip")+"' "
                    + "AND Vietos = '" + dropVietos.SelectedItem.ToString() + "' "
                    ;
                    //MessageBox.Show(QURY);
                    DataRow row;
                    dtbl = MySQL_receiveQuery(mySQLceConnection, QURY);
                    if (dtbl != null) {
                        dropNumeris.Items.Clear();
                        //MessageBox.Show(dtbl.Rows.Count.ToString());
                        if (dtbl.Rows.Count > 0) {
                            for (int i = 0; i < dtbl.Rows.Count; i++)
                            {
                                row = dtbl.Rows[i];
                                dropNumeris.Items.Add(row[0].ToString());
                            }
                            dropNumeris.SelectedIndex = 0;
                            bSubmit[2].Enabled = true;
                        }
                        else {
                            bSubmit[2].Enabled = false;
                        }
                    }

                    dropNumeris.Enabled = true;
                    pNEWright.Refresh();
                    #endregion
                    break;
                case "bSubmit_2":
                    #region bSubmit_2 -- naujo svečio įregistravimas
                    try
                    {
                        bool missing2 = false;
                        string[] from_to = new string[2];
                        string[] date1;
                        date1=datesCalendar2[0].Value.ToShortDateString().Split('.');
                        from_to[0] = date1[0]+date1[1]+date1[2];
                        date1=datesCalendar2[1].Value.ToShortDateString().Split('.');
                        from_to[1] = date1[0]+date1[1]+date1[2];

                        string QRY = "UPDATE Kambariai "
                                + "SET Laisvas = '" + (dropLaisvas.SelectedText == "Ne") + "', "
                                    + "Klientas = '" + tbNEWfields[0].Text + "," + tbNEWfields[1].Text + "', "
                                    + "Nuo = '" + from_to[0] + "', "
                                    + "Iki = '" + from_to[1] + "' "
                                + "WHERE Numeris = '" + dropNumeris.Text + "';"
                                ;
                        //MessageBox.Show(QRY); break;

                        string QRY2 = "INSERT INTO Klientai "
                            + "("
                                + "Vardas, "
                                + "Pavarde, "
                                + "Adresas, "
                                + "Dokumentas, "
                                + "DokID, "
                                + "MokKort, "
                                + "Nuo, "
                                + "Iki, "
                                + "Ruko, "
                                + "SumaBaras, "
                                + "SumaKambarys, "
                                + "Sumokejo, "
                                + "Kambarys"
                            + ") "
                            + "VALUES "
                            + "(";
                        for (int i = 0; i < 6; i++) {
                            if (tbNEWfields[i].Text.Trim().Length == 0) missing2 = true;
                            QRY2 += "'" + tbNEWfields[i].Text +"', ";
                        }

                        if (missing2) {
                            MessageBox.Show(
                                "Nepalikite tuščių laukelių. "
                                + NewLine
                                 +"Užpildykite tuščius laukelius prieš patvirtindami įrašą."
                                );
                            break;
                        }


                        QRY2 += "'" + dateToInt(datesCalendar2[0]) + "', ";
                        QRY2 += "'" + dateToInt(datesCalendar2[1]) + "', ";
                        QRY2 += "'" + cbxRuko.Checked + "', ";
                        QRY2 += "'', ";
                        QRY2 += "'" + tbKaina[1].Text + "', ";
                        QRY2 += "'" + (1==2) + "', "; // FALSE
                        QRY2 += "'" + dropNumeris.Text + "'";
                        QRY2 += ");";

//                        MessageBox.Show(QRY);
                        //MessageBox.Show(QRY2);
                       // break;
                        if (MySQL_sendQuery(mySQLceConnection, QRY2) == 0
                            && MySQL_sendQuery(mySQLceConnection, QRY) == 0)
                        {
                            MessageBox.Show(
                                    "Svečias įregistruotas sėkmingai"
                                    , "Informacija"
                                );
                            flushAllFields();
                        }
                        else
                            MessageBox.Show(
                                    "Nepavyko įregistruoti naujo svečio duomenų bazėje."
                                    +NewLine+NewLine
                                    +"Susisiekite su IT administratoriumi."
                                );
                    }
                    catch (Exception exc) {
                        MessageBox.Show(
                            "Failed to send data to database. Please check if LocalDB.sdf file is available and restart application." 
                            + NewLine + NewLine 
                            + exc.Message.ToString()
                            );
                    }

                    #endregion
                    break;
                case "bPay":
                    #region Apmokama numerio sąskaita
                    string mbxWarning = 
                        "Apmokėjus sąskaitą įrašas apie klientą bus pašalintas iš duomenų bazės."
                        +NewLine+NewLine
                        +"Ar tikrai norite tęsti?"
                        ;
                    DialogResult dialogResult = MessageBox.Show(mbxWarning, "Patvirtinkite", MessageBoxButtons.YesNo);
                    if (dialogResult == DialogResult.Yes) {
                        string QRY1 = "UPDATE Kambariai "
                                + "SET Laisvas = '" + (1==1) + "', "
                                    + "Klientas = NULL, "
                                    //+ "Nuo = '" + null + "', "
                                    + "Nuo = NULL, "
                                    + "Iki = NULL "
                                + "WHERE Numeris = '" + dropBILLroom.Text + "';"
                                ;

                        string QRY2 = "DELETE Klientai WHERE "
                            + "Kambarys = '" + dropBILLroom.Text + "';"
                            ;

                       /* MessageBox.Show(
                                 "Q1='"
                               + QRY1 + "'" + NewLine+NewLine
                               + "Q2='"
                               + QRY2 + "'"
                            ); //*/

                        if (MySQL_sendQuery(mySQLceConnection, QRY1) == 0 && MySQL_sendQuery(mySQLceConnection, QRY2) == 0)
                            MessageBox.Show(
                                    "Sąskaita apmokėta. Klientas pašalintas iš duomenų bazės."
                                    ,"Informacija"
                                );
                        else
                            MessageBox.Show(
                                    "Operacija nepavyko. Susisiekite su IT administratoriumi."
                                    , "Klaida"
                                );
                    }
                    #endregion
                    break;
                case "bINFOr":
                    #region Atnaujinti informaciją apie kambarius
                    QURY = "UPDATE Kambariai "
                                + "SET Laisvas = '" + (dropINFOr[5].SelectedText == "Taip") + "', "
                                    + "Rukomas = '" + (dropINFOr[2].SelectedText == "Taip") + "', "
                                    + "Vietos = '" + dropINFOr[3].Text + "', "
                                    + "Kaina = '" + tbINFOr.Text + "' "
                                + "WHERE Numeris = '" + dropINFOr[0].Text + "';"
                                ; 
                    if (0 == MySQL_sendQuery(mySQLceConnection, QURY)){
                        MessageBox.Show(
                                "Duomenys atnaujinti."
                                ,"Informacija"
                            );
                    }
                    else MessageBox.Show(
                              "Duomenų atnaujinti nepavyko. Susisiekite su savo IT administratoriumi."
                              , "Klaida"
                          );
                    #endregion
                    break;
                default:
                    pNEWleft.Visible = false;
                    pNEWright.Visible = false;
                    break;
            }
        }

        private void calendar_changed(object sender, EventArgs e)
        {
            DateTimePicker calendar = sender as DateTimePicker;
            int calendarID = Convert.ToInt32(calendar.Tag);
            string[] date1, date2;
            int d1, d2;
            date1 = datesCalendar[0].Value.ToShortDateString().Split('.');
            date2 = datesCalendar[1].Value.ToShortDateString().Split('.');
            d1=Convert.ToInt32(date1[0] + date1[1] +date1[2]);
            d2=Convert.ToInt32(date2[0] + date2[1] +date2[2]);
            if (d1 >= d2) {
                MessageBox.Show(
                    "Netinkama kliento apsistojimo ar išvykimo data."
                    );
                datesCalendar[calendarID].Value = datesCalendar2[calendarID].Value;
                //datesCalendar[1].Value = datesCalendar2[1].Value;
                //datesCalendar[0].Value = datesCalendar2[0].Value;
                
                return;
            }
            datesCalendar2[calendarID].Value = calendar.Value;
            datesCalendar2[calendarID].Refresh();

        }

        private void checkbox_checked(object sender, EventArgs e)
        {
            CheckBox cBox = sender as CheckBox;
            //MessageBox.Show(cBox.CheckState.ToString());
            if (cBox.Checked == true) dropRukomas.SelectedIndex = 1;
            else dropRukomas.SelectedIndex = 0;
        }

        private void dropdown_picked(object sender, EventArgs e) {
            ComboBox dropList = sender as ComboBox;

            switch (dropList.Name.ToString()) {
                case "dropNumeris":
                    #region Pasirenkamas kambarys apgyvendinimui
                    DataRow row;
                    string[] date1, date2;
                    double duration = (datesCalendar2[1].Value - datesCalendar2[0].Value).Days;
                    date1 = datesCalendar2[0].Value.ToShortDateString().Split('.');
                    date2 = datesCalendar2[1].Value.ToShortDateString().Split('.');
                    //duration = Convert.ToInt32(date2[2]) - Convert.ToInt32(date1[2]);

                    for (int i = 0; i < dtbl.Rows.Count; i++) {
                        row = dtbl.Rows[i];
                        if (row[0].ToString() == dropNumeris.Text.ToString()) {
                            tbKaina[0].Text = row[1].ToString();
                            tbKaina[1].Text = (Convert.ToDouble(row[1].ToString())*duration).ToString();
                        }
                    }
                    #endregion
                        break;
              //  case "dropBILLperson":
                      //  string QRY = "SELECT Pavarde, Vardas FROM Klientai WHERE Kambarys = '" + dropList.Text + "'";
                      //  dtbl = MySQL_receiveQuery(mySQLceConnection, QRY);
                    //if(dtbl.Rows)
                //        break;
                case "dropBILLroom":
                        #region Pasirenkamas kambarys, už kurį norima apmokėti sąskaitą
                        string ROOM = dropBILLroom.Text;

                        string QRYc = "SELECT * FROM Klientai WHERE "
                            + "Kambarys = '" + ROOM + "'"
                            ;
                        string QRYr = "SELECT Vietos, Kaina FROM Kambariai WHERE "
                            +"Numeris = '" +ROOM+"'"
                            ;

                        DataTable dtC = MySQL_receiveQuery(mySQLceConnection, QRYc);
                        DataTable dtR = MySQL_receiveQuery(mySQLceConnection, QRYr);

                        #region Tikrinama, ar duomenų bazėje įrašyta, kad kambarys apgyvendintas

                        if (dtC != null && dtR != null && dtC.Rows.Count > 0)
                        {
                            DataRow rowC, rowR;

                            rowR = dtR.Rows[dtR.Rows.Count - 1];

                            rowC = dtC.Rows[dtC.Rows.Count-1];
                           // MessageBox.Show("tbsBill=" + tbsBill.Length.ToString()+NewLine+ "dtC.Columns.Count=" + dtC.Columns.Count.ToString());
                            for (int i = 1; i < dtC.Columns.Count-1; i++) {
                                if (sLabelsBILL[i].Split(':')[0] == "Kaina") {
                                    tbsBill[i].Text = rowR[1].ToString();
                                    continue;
                                }
                                tbsBill[i].Text = rowC[i-1].ToString();
                            } //*/
                            pBILLright.Visible = true;
                            tbPAYrKambarys.Text = tbsBill[tbsBill.Length-2].Text;
                            tbPAYrBaras.Text = tbsBill[tbsBill.Length - 3].Text;
                            tbPAYrVISO.Text = (
                                     Convert.ToDouble(tbPAYrKambarys.Text)
                                   + Convert.ToDouble(tbPAYrBaras.Text) 
                                ).ToString();

                        }
                        else {
                            for (int i = 1; i < sLabelsBILL.Length; i++)
                                tbsBill[i].Clear();
                            tbsBill[1].Text = ROOM + " tuščias";
                            //tbPAYrKambarys.Text = "";
                            pBILLright.Visible = false;
                        }

                        #endregion
                        #endregion
                        break;
                case "dropINFOl":
                        #region Surenkamas sąrašas laisvų/užimtų/rezervuotų kambarių
                        string QRY = "SELECT Numeris FROM Kambariai WHERE ";

                        string[] today = DateTime.Today.ToShortDateString().Split('.');
                        string TODAY = today[0] + today[1] + today[2];                          

                        switch (dropList.Text) {
                            case "Laisvi kambariai":
                                QRY = QRY + "("
                                    + "Nuo > '" + TODAY + "' " // Ar naujakurys išsikels iki rezervacijos laiko
                                    + "OR "
                                    + "Iki <= '" + TODAY + "' "  // Ar naujakurys įsikels po rezervacijos laiko
                                    + "OR "
                                    + "Iki IS NULL" // Tiek IKI tiek NUO null'inami kartu, tad pakanka patikrint vieną iš jų
                                    + ") ";
                                break;
                            case "Rezervuoti kambariai":
                                QRY = QRY + "("
                                    + "Nuo > '" + TODAY + "' " // Ar naujakurys išsikels iki rezervacijos laiko
                                    + ") ";
                                break;
                            case "Užimti kambariai":
                                QRY = QRY + "("
                                    + "Nuo <= '" + TODAY + "' " // Ar naujakurys išsikels iki rezervacijos laiko
                                    + ") ";
                                break;
                            default:
                                break;
                        }
                        DataTable dt;

                        int INDEX = 2;
                        int TOTAL = 0;
                        for (int i = 0; i < 2; i++) { // 2 klasės: rūkomi ir nerūkomi
                            for (int j = 0; j < 3; j++) { // 3 kambarių talpos: 1-,2-,3-viečiai
                                tbsINFOl[INDEX].Text = string.Empty;
                                dt=MySQL_receiveQuery(
                                        mySQLceConnection
                                       ,QRY + " AND Rukomas = '" + (INDEX < 5) +"' AND Vietos = '"+(j+1)+"'"
                                    );
                                TOTAL += dt.Rows.Count;
                                for (int r = 0; r < dt.Rows.Count; r++) {
                                    row = dt.Rows[r];
                                    tbsINFOl[INDEX].Text = tbsINFOl[INDEX].Text + (r>0?", ":"") + row[0].ToString();
                                    
                                }
                                    INDEX++;
                            }
                            INDEX++;
                        }
                        tbsINFOl[0].Text = TOTAL.ToString();
                            #endregion
                        break;
                case "dropINFOr_0":
                        #region Pateikti informacija apie kambarį REDAGAVIMUI
                        QRY = "";
                        QRY="SELECT Rukomas, Vietos, Kaina, Laisvas FROM Kambariai WHERE Numeris = '"+dropINFOr[0].Text+"'";
                        dt = MySQL_receiveQuery(mySQLceConnection, QRY);
                        if (dt.Rows.Count > 0)
                        {
                            row = dt.Rows[0];

                            if (row[0].ToString() == "True") // Rūkomas?
                                dropINFOr[2].SelectedIndex = 0;
                            else dropINFOr[2].SelectedIndex = 1;

                            dropINFOr[3].SelectedIndex = (Convert.ToInt32(row[1].ToString()) - 1); // vietų skaičius

                            tbINFOr.Text = row[2].ToString(); // kaina

                            if (row[3].ToString() == "True") // Laisvas?
                                dropINFOr[5].SelectedIndex = 0;
                            else dropINFOr[5].SelectedIndex = 1;


                        }
                        else MessageBox.Show("Nepavyko gauti duomenų iš duomenų bazės.");
                        #endregion
                        break;
                default:
                    break;
            }
        }

        private void textbox_changed(object sender, EventArgs e) {
            TextBox tb = sender as TextBox;

            switch (tb.Name) { 
                case "tbPAYrBaras":
                    #region tbPAYrBaras -- įvedamos baro išlaidos 
                    #region Jei laukelis ištrintas
                    if (tbPAYrBaras.Text.Trim().Length == 0) { // if nothing is entered
                        tbPAYrBaras.BackColor = dropBILLroom.BackColor;
                        //tbPAYrBaras.Text = tbsBill[tbsBill.Length-3].Text;
                        tbPAYrVISO.Text = (
                                     Convert.ToDouble(tbPAYrKambarys.Text)
                                   + Convert.ToDouble(tbsBill[tbsBill.Length-3].Text)
                                ).ToString();
                        break;
                    }
                    #endregion

                    double VISO;
                    #region Tikrinama, ar įvestas skaičius. Jei taip - apskaičiuojama bendra suma
                    try
                    {
                        VISO = Convert.ToDouble(tbPAYrBaras.Text.Replace('.', ','));
                        tbPAYrBaras.BackColor = dropBILLroom.BackColor;
                        tbPAYrVISO.Text = (
                                         Convert.ToDouble(tbPAYrKambarys.Text)
                                       + VISO
                                    ).ToString();
                    }
                    catch (Exception exc) {
                        tbPAYrBaras.BackColor = Color.IndianRed;
                                tbPAYrVISO.Text = (
                                     Convert.ToDouble(tbPAYrKambarys.Text)
                                   + Convert.ToDouble(tbsBill[tbsBill.Length - 3].Text)
                                ).ToString();
                    }
                    #endregion
                    #endregion
                    break;
                default:
                    break;
            }
        }


        #endregion

        private void flushAllFields() {
            for (int i = 0; i < 6; i++) 
                tbNEWfields[i].Text = string.Empty;
            //MessageBox.Show(datesCalendar[1].Value.CompareTo(DateTime.Today).ToString());
            if(datesCalendar[1].Value.CompareTo(DateTime.Today) <= 0)
                datesCalendar[1].Value = DateTime.Today.AddDays(1);
            datesCalendar[0].Value = DateTime.Today;
            datesCalendar[1].Value = DateTime.Today.AddDays(1);
            cbxRuko.Checked = false;
            dropRukomas.SelectedIndex = 0;
            dropNumeris.Items.Clear();
            dropLaisvas.SelectedIndex = 1;
            dropVietos.SelectedIndex = 0;

        }

        private int dateToInt(DateTimePicker date) {
            string[] date1;
            date1 = date.Value.ToShortDateString().Split('.');
            return Convert.ToInt32(date1[0] + date1[1] + date1[2]);
        }

        }

    //}
}

