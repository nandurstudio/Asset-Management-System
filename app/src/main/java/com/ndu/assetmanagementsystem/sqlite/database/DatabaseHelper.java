package com.ndu.assetmanagementsystem.sqlite.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ndu.assetmanagementsystem.sqlite.database.model.Asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_CODE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_LOCATION;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_RFID;
//import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ID;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "assets_db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create assets table
        sqLiteDatabase.execSQL(Asset.CREATE_TABLE);
    }

    // Upgrading db
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Asset.TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public long createAsset(String asset) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Asset.COLUMN_ASSET_RFID, asset);

        // insert row
        long id = db.insert(Asset.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Asset getAsset(long code) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Asset.TABLE_NAME,
                new String[]{/*COLUMN_ID, */Asset.COLUMN_ASSET_CODE, Asset.COLUMN_ASSET_RFID, Asset.COLUMN_ASSET_DESC, Asset.COLUMN_ASSET_PIC, Asset.COLUMN_ASSET_LOCATION, Asset.COLUMN_ASSET_STATUS, Asset.COLUMN_TIMESTAMP},
                /*COLUMN_ID + "=?",*/
                COLUMN_ASSET_CODE,
                new String[]{String.valueOf(code)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare asset object
        Asset asset = new Asset(
//                Objects.requireNonNull(cursor).getInt(cursor.getColumnIndex(COLUMN_ID)),
                Objects.requireNonNull(cursor).getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_CODE)),
                cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_RFID)),
                cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_DESC)),
                cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_PIC)),
                cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_LOCATION)),
                cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_STATUS)),
                cursor.getString(cursor.getColumnIndex(Asset.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return asset;
    }

    public List<Asset> getAllAssets() {
        List<Asset> assets = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Asset.TABLE_NAME + " ORDER BY " +
//                Asset.COLUMN_TIMESTAMP + " DESC";
                /*COLUMN_ID*/COLUMN_ASSET_LOCATION + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Asset asset = new Asset();
//                asset.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                asset.setAsset_code(cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_CODE)));
                asset.setAsset_rfid(cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_RFID)));
                asset.setAsset_desc(cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_DESC)));
                asset.setAsset_pic(cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_PIC)));
                asset.setAsset_location(cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_LOCATION)));
                asset.setAsset_status(cursor.getString(cursor.getColumnIndex(Asset.COLUMN_ASSET_STATUS)));
                asset.setTimestamp(cursor.getString(cursor.getColumnIndex(Asset.COLUMN_TIMESTAMP)));

                assets.add(asset);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return assets list
        return assets;
    }

    public int getAssetsCount() {
        String countQuery = "SELECT  * FROM " + Asset.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateAsset(Asset asset) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Asset.COLUMN_ASSET_RFID, asset.getAsset_rfid());

        // updating row
        return db.update(Asset.TABLE_NAME, values, /*COLUMN_ID*/COLUMN_ASSET_CODE + " = ?",
                /*new String[]{String.valueOf(asset.getId())});*/
                new String[]{asset.getAsset_code()});
    }

    public int updateStatusByRfid(Asset asset, String rfid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Asset.COLUMN_ASSET_STATUS, /*asset.getAsset_status()*/"Asset Ada");

        // updating row
        return db.update(Asset.TABLE_NAME, values, COLUMN_ASSET_RFID + " = ?",
                new String[]{rfid});
    }

    public void deleteAsset(Asset asset) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Asset.TABLE_NAME, /*COLUMN_ID*/COLUMN_ASSET_CODE + " = ?",
//                new String[]{String.valueOf(asset.getId())});
                new String[]{asset.getAsset_code()});
        db.close();
    }

    public long insertAsset(String asset) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Asset.COLUMN_ASSET_RFID, asset);

        // insert row
        long id = db.insert(Asset.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    /*https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists*/
    /*https://stackoverflow.com/questions/20838233/sqliteexception-unrecognized-token-when-reading-from-database*/
    public boolean checkIsRfidInDB(String rfid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + Asset.TABLE_NAME + " where " + Asset.COLUMN_ASSET_RFID + " = " + "'" + rfid + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsItemCodeInDb(String itemCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + Asset.TABLE_NAME + " where " + Asset.COLUMN_ASSET_CODE + " = " + "'" + itemCode + "'";
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

        values.put(Asset.COLUMN_ASSET_CODE, Vi.get(Asset.COLUMN_ASSET_CODE));
        values.put(Asset.COLUMN_ASSET_RFID, Vi.get(Asset.COLUMN_ASSET_RFID));
        values.put(Asset.COLUMN_ASSET_DESC, Vi.get(Asset.COLUMN_ASSET_DESC));
        values.put(Asset.COLUMN_ASSET_PIC, Vi.get(Asset.COLUMN_ASSET_PIC));
        values.put(Asset.COLUMN_ASSET_LOCATION, Vi.get(Asset.COLUMN_ASSET_LOCATION));
        values.put(Asset.COLUMN_ASSET_STATUS, Vi.get(Asset.COLUMN_ASSET_STATUS));
        //etc
        database.insert(Asset.TABLE_NAME, null, values);
        database.close();
    }

}
