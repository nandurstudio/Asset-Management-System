package com.ndu.assetmanagementsystem.sqlite.database.model;

/*https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/*/
public class AssetV2 {
    public static final String TABLE_NAME_V2 = "AssetV2";

    public static final String ASSET_EXIST = "B";

    //public static final String COLUMN_ID = "id";
    public static final String COLUMN_TXTFIXEDASSETCODE = "txtFixedAssetCode";
    public static final String COLUMN_TXTASSETDESCRIPTION = "txtAssetDescription";
    public static final String COLUMN_TXTASSETCATEGORY = "txtAssetCategory";
    public static final String COLUMN_TXTSUPERVISORID = "txtSupervisorID";
    public static final String COLUMN_DECACQUISITION = "decAcquisition";
    public static final String COLUMN_TXTNAME = "txtName";
    public static final String COLUMN_TXTNICK = "txtNick";
    public static final String COLUMN_TXTEMAIL = "txtEmail";
    public static final String COLUMN_TXTPENGGUNAID = "txtPenggunaID";
    public static final String COLUMN_TXTLOKASIPENGGUNA = "txtLokasiPengguna";
    public static final String COLUMN_TXTLOBPENGGUNA = "txtLOBPengguna";
    public static final String COLUMN_TXTAREA = "txtArea";
    public static final String COLUMN_TXTRFID = "txtRfid";
    public static final String COLUMN_TXTSTATUS = "txtStatus";
    public static final String COLUMN_TXTIMGLINK = "txtImgLink";
    public static final String COLUMN_TXTNOTES = "txtNotes";
    public static final String COLUMN_DTMTIMESTAMP = "dtmTimestamp";

    //private int id;
    private String txtFixedAssetCode;
    private String txtAssetDescription;
    private String txtAssetCategory;
    private String txtSupervisorID;
    private String decAcquisition;
    private String txtName;
    private String txtNick;
    private String txtEmail;
    private String txtPenggunaID;
    private String txtLokasiPengguna;
    private String txtLOBPengguna;
    private String txtArea;
    private String txtRfid;
    private String txtStatus;
    private String txtImgLink;
    private String txtNotes;
    private String dtmTimestamp;


    // Create table SQL query
    public static final String CREATE_TABLEV2 =
            "CREATE TABLE " + TABLE_NAME_V2 + "("
                    //+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TXTFIXEDASSETCODE + " TEXT PRIMARY KEY,"
                    + COLUMN_TXTASSETDESCRIPTION + " TEXT,"
                    + COLUMN_TXTASSETCATEGORY + " TEXT,"
                    + COLUMN_TXTSUPERVISORID + " TEXT,"
                    + COLUMN_DECACQUISITION + " DECIMAL(10,5),"
                    + COLUMN_TXTNAME + " TEXT,"
                    + COLUMN_TXTNICK + " TEXT,"
                    + COLUMN_TXTEMAIL + " TEXT,"
                    + COLUMN_TXTPENGGUNAID + " TEXT,"
                    + COLUMN_TXTLOKASIPENGGUNA + " TEXT,"
                    + COLUMN_TXTLOBPENGGUNA + " TEXT,"
                    + COLUMN_TXTAREA + " TEXT,"
                    + COLUMN_TXTRFID + " TEXT,"
                    + COLUMN_TXTSTATUS + " TEXT,"
                    + COLUMN_TXTIMGLINK + " TEXT,"
                    + COLUMN_TXTNOTES + " TEXT,"
                    + COLUMN_DTMTIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public AssetV2() {
    }

    public static String getColumnTxtarea() {
        return COLUMN_TXTAREA;
    }

    public static String getColumnTxtrfid() {
        return COLUMN_TXTRFID;
    }

    public static String getColumnTxtstatus() {
        return COLUMN_TXTSTATUS;
    }

    public static String getColumnTxtimglink() {
        return COLUMN_TXTIMGLINK;
    }

    public static String getColumnTxtnotes() {
        return COLUMN_TXTNOTES;
    }

    public String getTxtArea() {
        return txtArea;
    }

    public void setTxtArea(String txtArea) {
        this.txtArea = txtArea;
    }

    public String getTxtRfid() {
        return txtRfid;
    }

    public void setTxtRfid(String txtRfid) {
        this.txtRfid = txtRfid;
    }

    public String getTxtStatus() {
        return txtStatus;
    }

    public void setTxtStatus(String txtStatus) {
        this.txtStatus = txtStatus;
    }

    public String getTxtImgLink() {
        return txtImgLink;
    }

    public void setTxtImgLink(String txtImgLink) {
        this.txtImgLink = txtImgLink;
    }

    public String getTxtNotes() {
        return txtNotes;
    }

    public void setTxtNotes(String txtNotes) {
        this.txtNotes = txtNotes;
    }

    public static String getCreateTablev2() {
        return CREATE_TABLEV2;
    }

    public AssetV2(/*int id, */String txtFixedAssetCode,
                               String txtAssetDescription,
                               String txtAssetCategory,
                               String txtSupervisorID,
                               String decAcquisition,
                               String txtName,
                               String txtNick,
                               String txtEmail,
                               String txtPenggunaID,
                               String txtLokasiPengguna,
                               String txtLOBPengguna,
                               String txtArea,
                               String txtRfid,
                               String txtStatus,
                               String txtImgLink,
                               String txtNotes,
                               String dtmTimestamp) {
        //this.id = id;
        this.txtFixedAssetCode = txtFixedAssetCode;
        this.txtAssetDescription = txtAssetDescription;
        this.txtAssetCategory = txtAssetCategory;
        this.txtSupervisorID = txtSupervisorID;
        this.decAcquisition = decAcquisition;
        this.txtName = txtName;
        this.txtNick = txtNick;
        this.txtEmail = txtEmail;
        this.txtPenggunaID = txtPenggunaID;
        this.txtLokasiPengguna = txtLokasiPengguna;
        this.txtLOBPengguna = txtLOBPengguna;
        this.txtArea = txtArea;
        this.txtRfid = txtRfid;
        this.txtStatus = txtStatus;
        this.txtImgLink = txtImgLink;
        this.txtNotes = txtNotes;
        this.dtmTimestamp = dtmTimestamp;
    }

    public static String getTableNameV2() {
        return TABLE_NAME_V2;
    }

    public static String getAssetExist() {
        return ASSET_EXIST;
    }

    public static String getColumnTxtfixedassetcode() {
        return COLUMN_TXTFIXEDASSETCODE;
    }

    public static String getColumnTxtassetdescription() {
        return COLUMN_TXTASSETDESCRIPTION;
    }

    public static String getColumnTxtassetcategory() {
        return COLUMN_TXTASSETCATEGORY;
    }

    public static String getColumnTxtsupervisorid() {
        return COLUMN_TXTSUPERVISORID;
    }

    public static String getColumnDecacquisition() {
        return COLUMN_DECACQUISITION;
    }

    public static String getColumnTxtname() {
        return COLUMN_TXTNAME;
    }

    public static String getColumnTxtnick() {
        return COLUMN_TXTNICK;
    }

    public static String getColumnTxtemail() {
        return COLUMN_TXTEMAIL;
    }

    public static String getColumnTxtpenggunaid() {
        return COLUMN_TXTPENGGUNAID;
    }

    public static String getColumnTxtlokasipengguna() {
        return COLUMN_TXTLOKASIPENGGUNA;
    }

    public static String getColumnTxtlobpengguna() {
        return COLUMN_TXTLOBPENGGUNA;
    }

    public static String getColumnDtmtimestamp() {
        return COLUMN_DTMTIMESTAMP;
    }

    public String getTxtFixedAssetCode() {
        return txtFixedAssetCode;
    }

    public void setTxtFixedAssetCode(String txtFixedAssetCode) {
        this.txtFixedAssetCode = txtFixedAssetCode;
    }

    public String getTxtAssetDescription() {
        return txtAssetDescription;
    }

    public void setTxtAssetDescription(String txtAssetDescription) {
        this.txtAssetDescription = txtAssetDescription;
    }

    public String getTxtAssetCategory() {
        return txtAssetCategory;
    }

    public void setTxtAssetCategory(String txtAssetCategory) {
        this.txtAssetCategory = txtAssetCategory;
    }

    public String getTxtSupervisorID() {
        return txtSupervisorID;
    }

    public void setTxtSupervisorID(String txtSupervisorID) {
        this.txtSupervisorID = txtSupervisorID;
    }

    public String getDecAcquisition() {
        return decAcquisition;
    }

    public void setDecAcquisition(String decAcquisition) {
        this.decAcquisition = decAcquisition;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtNick() {
        return txtNick;
    }

    public void setTxtNick(String txtNick) {
        this.txtNick = txtNick;
    }

    public String getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(String txtEmail) {
        this.txtEmail = txtEmail;
    }

    public String getTxtPenggunaID() {
        return txtPenggunaID;
    }

    public void setTxtPenggunaID(String txtPenggunaID) {
        this.txtPenggunaID = txtPenggunaID;
    }

    public String getTxtLokasiPengguna() {
        return txtLokasiPengguna;
    }

    public void setTxtLokasiPengguna(String txtLokasiPengguna) {
        this.txtLokasiPengguna = txtLokasiPengguna;
    }

    public String getTxtLOBPengguna() {
        return txtLOBPengguna;
    }

    public void setTxtLOBPengguna(String txtLOBPengguna) {
        this.txtLOBPengguna = txtLOBPengguna;
    }

    public String getDtmTimestamp() {
        return dtmTimestamp;
    }

    public void setDtmTimestamp(String dtmTimestamp) {
        this.dtmTimestamp = dtmTimestamp;
    }
}
