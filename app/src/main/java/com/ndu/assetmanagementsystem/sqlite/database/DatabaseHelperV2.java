package com.ndu.assetmanagementsystem.sqlite.database;

import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_DECACQUISITION;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_DTMTIMESTAMP;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTAREA;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTASSETCATEGORY;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTASSETDESCRIPTION;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTEMAIL;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTFIXEDASSETCODE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTIMGLINK;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTLOBPENGGUNA;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTLOKASIPENGGUNA;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTNAME;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTNICK;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTNOTES;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTPENGGUNAID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTRFID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTSTATUS;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTSUPERVISORID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.CREATE_TABLEV2;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.TABLE_NAME_V2;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Department.COLUMN_DEPT_CODE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Department.COLUMN_DEPT_ID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Department.COLUMN_DEPT_NAME;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Department.TABLE_NAME_DEPT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2;
import com.ndu.assetmanagementsystem.sqlite.database.model.Department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ID;

public class DatabaseHelperV2 extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "assets_dbv2";

    public DatabaseHelperV2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create assets table
        sqLiteDatabase.execSQL(CREATE_TABLEV2);
    }

    // Upgrading db
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_V2);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public long createAsset(String asset) {
//        // get writable database as we want to write data
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        // `id` and `timestamp` will be inserted automatically.
//        // no need to add them
//        values.put(COLUMN_TXTRFID, asset);
//
//        // insert row
//        long id = db.insert(TABLE_NAME_V2, null, values);
//
//        // close db connection
//        db.close();
//
//        // return newly inserted row id
//        return id;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

    public void dropTable() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_V2);
        onCreate(db);
    }

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public AssetV2 getAsset(long code) {
//        // get readable database as we are not inserting anything
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_NAME_V2,
//                new String[]{/*COLUMN_ID, */COLUMN_TXTFIXEDASSETCODE,
//                        COLUMN_TXTASSETDESCRIPTION,
//                        COLUMN_TXTASSETCATEGORY,
//                        COLUMN_TXTSUPERVISORID,
//                        COLUMN_DECACQUISITION,
//                        COLUMN_TXTNAME,
//                        COLUMN_TXTNICK,
//                        COLUMN_TXTEMAIL,
//                        COLUMN_TXTPENGGUNAID,
//                        COLUMN_TXTLOKASIPENGGUNA,
//                        COLUMN_TXTLOBPENGGUNA,
//                        COLUMN_TXTAREA,
//                        COLUMN_TXTRFID,
//                        COLUMN_TXTSTATUS,
//                        COLUMN_TXTIMGLINK,
//                        COLUMN_TXTNOTES,
//                        COLUMN_DTMTIMESTAMP},
//                /*COLUMN_ID + "=?",*/
//                COLUMN_TXTFIXEDASSETCODE,
//                new String[]{String.valueOf(code)}, null, null, null, null);
//
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        // prepare asset object
//        AssetV2 asset_v2 = new AssetV2(
////                Objects.requireNonNull(cursor).getInt(cursor.getColumnIndex(COLUMN_ID)),
//                Objects.requireNonNull(cursor).getString(cursor.getColumnIndex(COLUMN_TXTFIXEDASSETCODE)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTASSETDESCRIPTION)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTASSETCATEGORY)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTSUPERVISORID)),
//                cursor.getInt(cursor.getColumnIndex(COLUMN_DECACQUISITION)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTNAME)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTNICK)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTEMAIL)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTPENGGUNAID)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTLOKASIPENGGUNA)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTLOBPENGGUNA)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTAREA)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTRFID)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTSTATUS)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTIMGLINK)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_TXTNOTES)),
//                cursor.getString(cursor.getColumnIndex(COLUMN_DTMTIMESTAMP)));
//
//        // close the db connection
//        cursor.close();
//
//        return asset_v2;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

    @SuppressLint("Range")
    public List<AssetV2> getAllAssets() {
        List<AssetV2> assets = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_V2 + " ORDER BY " +
//                Asset.COLUMN_TIMESTAMP + " DESC";
                /*COLUMN_ID*/COLUMN_TXTLOBPENGGUNA + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AssetV2 asset = new AssetV2();
//                asset.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                asset.setTxtFixedAssetCode(cursor.getString(cursor.getColumnIndex(COLUMN_TXTFIXEDASSETCODE)));
                asset.setTxtAssetDescription(cursor.getString(cursor.getColumnIndex(COLUMN_TXTASSETDESCRIPTION)));
                asset.setTxtAssetCategory(cursor.getString(cursor.getColumnIndex(COLUMN_TXTASSETCATEGORY)));
                asset.setTxtSupervisorID(cursor.getString(cursor.getColumnIndex(COLUMN_TXTSUPERVISORID)));
                asset.setDecAcquisition(cursor.getInt(cursor.getColumnIndex(COLUMN_DECACQUISITION)));
                asset.setTxtName(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNAME)));
                asset.setTxtNick(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNICK)));
                asset.setTxtEmail(cursor.getString(cursor.getColumnIndex(COLUMN_TXTEMAIL)));
                asset.setTxtPenggunaID(cursor.getString(cursor.getColumnIndex(COLUMN_TXTPENGGUNAID)));
                asset.setTxtLokasiPengguna(cursor.getString(cursor.getColumnIndex(COLUMN_TXTLOKASIPENGGUNA)));
                asset.setTxtLOBPengguna(cursor.getString(cursor.getColumnIndex(COLUMN_TXTLOBPENGGUNA)));
                asset.setTxtArea(cursor.getString(cursor.getColumnIndex(COLUMN_TXTAREA)));
                asset.setTxtRfid(cursor.getString(cursor.getColumnIndex(COLUMN_TXTRFID)));
                asset.setTxtStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TXTSTATUS)));
                asset.setTxtImgLink(cursor.getString(cursor.getColumnIndex(COLUMN_TXTIMGLINK)));
                asset.setTxtNotes(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNOTES)));
                asset.setDtmTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_DTMTIMESTAMP)));

                assets.add(asset);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return assets list
        return assets;
    }

    @SuppressLint("Range")
    public List<AssetV2> getAllAssetsByDept(String assetLocation) {
        List<AssetV2> assets = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_V2 + " WHERE " + COLUMN_TXTLOBPENGGUNA + " LIKE '" + assetLocation + "' " + " ORDER BY " +
//                Asset.COLUMN_TIMESTAMP + " DESC";
                /*COLUMN_ID*/COLUMN_TXTNICK + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AssetV2 asset = new AssetV2();
//                asset.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                asset.setTxtFixedAssetCode(cursor.getString(cursor.getColumnIndex(COLUMN_TXTFIXEDASSETCODE)));
                asset.setTxtAssetDescription(cursor.getString(cursor.getColumnIndex(COLUMN_TXTASSETDESCRIPTION)));
                asset.setTxtAssetCategory(cursor.getString(cursor.getColumnIndex(COLUMN_TXTASSETCATEGORY)));
                asset.setTxtSupervisorID(cursor.getString(cursor.getColumnIndex(COLUMN_TXTSUPERVISORID)));
                asset.setDecAcquisition(cursor.getInt(cursor.getColumnIndex(COLUMN_DECACQUISITION)));
                asset.setTxtName(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNAME)));
                asset.setTxtNick(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNICK)));
                asset.setTxtEmail(cursor.getString(cursor.getColumnIndex(COLUMN_TXTEMAIL)));
                asset.setTxtPenggunaID(cursor.getString(cursor.getColumnIndex(COLUMN_TXTPENGGUNAID)));
                asset.setTxtLokasiPengguna(cursor.getString(cursor.getColumnIndex(COLUMN_TXTLOKASIPENGGUNA)));
                asset.setTxtLOBPengguna(cursor.getString(cursor.getColumnIndex(COLUMN_TXTLOBPENGGUNA)));
                asset.setTxtArea(cursor.getString(cursor.getColumnIndex(COLUMN_TXTAREA)));
                asset.setTxtRfid(cursor.getString(cursor.getColumnIndex(COLUMN_TXTRFID)));
                asset.setTxtStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TXTSTATUS)));
                asset.setTxtImgLink(cursor.getString(cursor.getColumnIndex(COLUMN_TXTIMGLINK)));
                asset.setTxtNotes(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNOTES)));
                asset.setDtmTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_DTMTIMESTAMP)));

                assets.add(asset);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return assets list
        return assets;
    }

    public List<Department> getAllDept() {
        List<Department> departmentList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_DEPT + " WHERE " + COLUMN_DEPT_NAME+ " ORDER BY " +
//                Asset.COLUMN_TIMESTAMP + " DESC";
                /*COLUMN_ID*/COLUMN_DEPT_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Department dept = new Department();
//                asset.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                dept.setTxtDepartementCode(String.valueOf(cursor.getColumnIndex(COLUMN_DEPT_CODE)));
                dept.setTxtDepartementName(String.valueOf(cursor.getColumnIndex(COLUMN_DEPT_NAME)));

                departmentList.add(dept);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return assets list
        return departmentList;
    }

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public List<AssetV2> getAllAssetsByExist() {
//        List<AssetV2> assets = new ArrayList<>();
//
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME_V2 + " WHERE " + COLUMN_TXTSTATUS + " NOT LIKE '" + "%Asset Ada%" + "' " + " ORDER BY " +
////                Asset.COLUMN_TIMESTAMP + " DESC";
//                /*COLUMN_ID*/COLUMN_TXTNICK + " ASC";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                AssetV2 asset = new AssetV2();
////                asset.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
//                asset.setTxtFixedAssetCode(cursor.getString(cursor.getColumnIndex(COLUMN_TXTFIXEDASSETCODE)));
//                asset.setTxtAssetDescription(cursor.getString(cursor.getColumnIndex(COLUMN_TXTASSETDESCRIPTION)));
//                asset.setTxtAssetCategory(cursor.getString(cursor.getColumnIndex(COLUMN_TXTASSETCATEGORY)));
//                asset.setTxtSupervisorID(cursor.getString(cursor.getColumnIndex(COLUMN_TXTSUPERVISORID)));
//                asset.setDecAcquisition(cursor.getInt(cursor.getColumnIndex(COLUMN_DECACQUISITION)));
//                asset.setTxtName(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNAME)));
//                asset.setTxtNick(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNICK)));
//                asset.setTxtEmail(cursor.getString(cursor.getColumnIndex(COLUMN_TXTEMAIL)));
//                asset.setTxtPenggunaID(cursor.getString(cursor.getColumnIndex(COLUMN_TXTPENGGUNAID)));
//                asset.setTxtLokasiPengguna(cursor.getString(cursor.getColumnIndex(COLUMN_TXTLOKASIPENGGUNA)));
//                asset.setTxtLOBPengguna(cursor.getString(cursor.getColumnIndex(COLUMN_TXTLOBPENGGUNA)));
//                asset.setTxtArea(cursor.getString(cursor.getColumnIndex(COLUMN_TXTAREA)));
//                asset.setTxtRfid(cursor.getString(cursor.getColumnIndex(COLUMN_TXTRFID)));
//                asset.setTxtStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TXTSTATUS)));
//                asset.setTxtImgLink(cursor.getString(cursor.getColumnIndex(COLUMN_TXTIMGLINK)));
//                asset.setTxtNotes(cursor.getString(cursor.getColumnIndex(COLUMN_TXTNOTES)));
//                asset.setDtmTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_DTMTIMESTAMP)));
//
//                assets.add(asset);
//            } while (cursor.moveToNext());
//        }
//
//        // close db connection
//        db.close();
//
//        // return assets list
//        return assets;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

    public int getAssetsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_V2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getAssetsCountByDeptLob(String dept) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_V2 + " WHERE " + COLUMN_TXTLOBPENGGUNA + " LIKE '" + dept + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getAssetsCountByTag(String dept) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_V2 + " WHERE " + COLUMN_TXTRFID + " IS NOT NULL AND " + COLUMN_TXTRFID + " != \"\" AND " + COLUMN_TXTLOBPENGGUNA + " LIKE '" + dept + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getAssetsCountByExist(String assetLocation, String assetExist) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_V2 + " WHERE " + COLUMN_TXTLOBPENGGUNA + " LIKE '" + assetLocation + "' AND " + COLUMN_TXTSTATUS + " LIKE '" + assetExist + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public int getAssetsCountByStatusNull(String assetLocation) {
//        String countQuery = "SELECT  * FROM " + TABLE_NAME_V2 + " WHERE " + COLUMN_TXTLOBPENGGUNA + " LIKE '" + assetLocation + "'" + " & " + COLUMN_TXTSTATUS + " IS NULL ";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//        // return count
//        return count;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public int getScannedAssetsCount() {
//        String countQuery = "SELECT  COUNT (*) FROM " + TABLE_NAME_V2 +
//                " WHERE " + COLUMN_TXTSTATUS + " = " + "'" + ASSET_EXIST + "'";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//        // return count
//        return count;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

//    SELECT COUNT(*) FROM Asset WHERE asset_rfid IS NOT NULL and asset_status = 'Asset Ada';

    public void updateAsset(AssetV2 asset) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TXTRFID, asset.getTxtRfid());

        // updating row
        db.update(TABLE_NAME_V2, values, /*COLUMN_ID*/COLUMN_TXTFIXEDASSETCODE + " = ?",
                /*new String[]{String.valueOf(asset.getId())});*/
                new String[]{asset.getTxtFixedAssetCode()});
    }

    public void updateStatusByRfid(AssetV2 asset, String rfid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TXTSTATUS, asset.getTxtStatus());

        // updating row
        db.update(TABLE_NAME_V2, values, COLUMN_TXTRFID + " = ?",
                new String[]{rfid});
    }

    public void updateAreaByRfid(AssetV2 asset, String rfid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TXTAREA, asset.getTxtArea());

        // updating row
        db.update(TABLE_NAME_V2, values, COLUMN_TXTRFID + " = ?",
                new String[]{rfid});
    }

    public void updateNickByRfid(AssetV2 assetV2, String rfid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TXTNICK, assetV2.getTxtNick());

        // updating row
        db.update(TABLE_NAME_V2, values, COLUMN_TXTRFID + " = ?",
                new String[]{rfid});
    }

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public int updateNameByRfid(AssetV2 assetV2, String rfid) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_TXTNAME, assetV2.getTxtName());
//
//        // updating row
//        return db.update(TABLE_NAME_V2, values, COLUMN_TXTRFID + " = ?",
//                new String[]{rfid});
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public void deleteAsset(AssetV2 asset) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME_V2, /*COLUMN_ID*/COLUMN_TXTFIXEDASSETCODE + " = ?",
////                new String[]{String.valueOf(asset.getId())});
//                new String[]{asset.getTxtFixedAssetCode()});
//        db.close();
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public long insertAsset(String asset) {
//        // get writable database as we want to write data
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        // `id` and `timestamp` will be inserted automatically.
//        // no need to add them
//        values.put(COLUMN_TXTRFID, asset);
//
//        // insert row
//        long id = db.insert(TABLE_NAME_V2, null, values);
//
//        // close db connection
//        db.close();
//
//        // return newly inserted row id
//        return id;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

    /*https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists*/
    /*https://stackoverflow.com/questions/20838233/sqliteexception-unrecognized-token-when-reading-from-database*/
    public boolean checkIsRfidInDB(String rfid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME_V2 + " where " + COLUMN_TXTRFID + " = " + "'" + rfid + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public boolean checkIsStatusUpdated(String status) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String Query = "Select * from " + TABLE_NAME_V2 + " where " + COLUMN_TXTSTATUS + " = " + "'" + status + "'";
//        Cursor cursor = db.rawQuery(Query, null);
//        if (cursor.getCount() <= 0) {
//            cursor.close();
//            return true;
//        }
//        cursor.close();
//        return false;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:26)

    public boolean checkIsItemCodeInDb(String itemCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME_V2 + " where " + COLUMN_TXTFIXEDASSETCODE + " = " + "'" + itemCode + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void inputDataFromDom(HashMap<String, String> Vi) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TXTFIXEDASSETCODE, Vi.get(COLUMN_TXTFIXEDASSETCODE));
        values.put(COLUMN_TXTASSETDESCRIPTION, Vi.get(COLUMN_TXTASSETDESCRIPTION));
        values.put(COLUMN_TXTASSETCATEGORY, Vi.get(COLUMN_TXTASSETCATEGORY));
        values.put(COLUMN_TXTSUPERVISORID, Vi.get(COLUMN_TXTSUPERVISORID));
        values.put(COLUMN_DECACQUISITION, Vi.get(COLUMN_DECACQUISITION));
        values.put(COLUMN_TXTNAME, Vi.get(COLUMN_TXTNAME));
        values.put(COLUMN_TXTNICK, Vi.get(COLUMN_TXTNICK));
        values.put(COLUMN_TXTEMAIL, Vi.get(COLUMN_TXTEMAIL));
        values.put(COLUMN_TXTPENGGUNAID, Vi.get(COLUMN_TXTPENGGUNAID));
        values.put(COLUMN_TXTLOKASIPENGGUNA, Vi.get(COLUMN_TXTLOKASIPENGGUNA));
        values.put(COLUMN_TXTLOBPENGGUNA, Vi.get(COLUMN_TXTLOBPENGGUNA));
        values.put(COLUMN_TXTAREA, Vi.get(COLUMN_TXTAREA));
        values.put(COLUMN_TXTRFID, Vi.get(COLUMN_TXTRFID));
        values.put(COLUMN_TXTSTATUS, Vi.get(COLUMN_TXTSTATUS));
        values.put(COLUMN_TXTIMGLINK, Vi.get(COLUMN_TXTIMGLINK));
        values.put(COLUMN_TXTNOTES, Vi.get(COLUMN_TXTNOTES));
        //etc
        database.insert(TABLE_NAME_V2, null, values);
        database.close();
    }

    public void createTable() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
    }

    public void deleteTagByItemCode(String itemCode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TXTRFID, "");

        // updating row
        db.update(TABLE_NAME_V2, values, COLUMN_TXTFIXEDASSETCODE + " = ?",
                new String[]{itemCode});
    }

    public void deleteStatusByItemCode(String itemCode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TXTSTATUS, "");

        // updating row
        db.update(TABLE_NAME_V2, values, COLUMN_TXTFIXEDASSETCODE + " = ?",
                new String[]{itemCode});
    }
}
