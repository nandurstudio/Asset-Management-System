package com.ndu.assetmanagementsystem.sqlite.database.model;

/*https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/*/
public class Asset {
    public static final String TABLE_NAME = "Asset";

    public static final String ASSET_EXIST = "Asset Ada";

    //public static final String COLUMN_ID = "id";
    public static final String COLUMN_ASSET_CODE = "asset_code";
    public static final String COLUMN_ASSET_RFID = "asset_rfid";
    public static final String COLUMN_ASSET_DESC = "asset_desc";
    public static final String COLUMN_ASSET_PIC = "asset_pic";
    public static final String COLUMN_ASSET_LOCATION = "asset_location";
    public static final String COLUMN_ASSET_STATUS = "asset_status";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //private int id;
    private String asset_code;
    private String asset_rfid;
    private String asset_desc;
    private String asset_pic;
    private String asset_location;
    private String asset_status;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    //+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ASSET_CODE + " TEXT PRIMARY KEY,"
                    + COLUMN_ASSET_RFID + " TEXT,"
                    + COLUMN_ASSET_DESC + " TEXT,"
                    + COLUMN_ASSET_PIC + " TEXT,"
                    + COLUMN_ASSET_LOCATION + " TEXT,"
                    + COLUMN_ASSET_STATUS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Asset() {
    }

    public Asset(/*int id, */String asset_code, String asset_rfid, String asset_desc, String asset_pic, String asset_location, String asset_status, String timestamp) {
        //this.id = id;
        this.asset_code = asset_code;
        this.asset_rfid = asset_rfid;
        this.asset_desc = asset_desc;
        this.asset_pic = asset_pic;
        this.asset_location = asset_location;
        this.asset_status = asset_status;
        this.timestamp = timestamp;
    }

    /**/

    /*public static String getColumnId() {
        return COLUMN_ID;
    }*/

    public static String getColumnAssetCode() {
        return COLUMN_ASSET_CODE;
    }

    public static String getColumnAssetRfid() {
        return COLUMN_ASSET_RFID;
    }

    public static String getColumnAssetDesc() {
        return COLUMN_ASSET_DESC;
    }

    public static String getColumnAssetPic() {
        return COLUMN_ASSET_PIC;
    }

    public static String getColumnAssetStatus() {
        return COLUMN_ASSET_STATUS;
    }

    public static String getColumnTimestamp() {
        return COLUMN_TIMESTAMP;
    }

    public static String getColumnAssetLocation() {
        return COLUMN_ASSET_LOCATION;
    }


/*    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public String getAsset_code() {
        return asset_code;
    }

    public void setAsset_code(String asset_code) {
        this.asset_code = asset_code;
    }

    public String getAsset_rfid() {
        return asset_rfid;
    }

    public void setAsset_rfid(String asset_rfid) {
        this.asset_rfid = asset_rfid;
    }

    public String getAsset_desc() {
        return asset_desc;
    }

    public void setAsset_desc(String asset_desc) {
        this.asset_desc = asset_desc;
    }

    public String getAsset_pic() {
        return asset_pic;
    }

    public void setAsset_pic(String asset_pic) {
        this.asset_pic = asset_pic;
    }

    public String getAsset_location() {
        return asset_location;
    }

    public void setAsset_location(String asset_location) {
        this.asset_location = asset_location;
    }

    public String getAsset_status() {
        return asset_status;
    }

    public void setAsset_status(String asset_status) {
        this.asset_status = asset_status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
