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

import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_DEPT_LOB;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_DEPT_LOB_UPDATE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_FIXED_ASSET_CODE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_IMAGE_LINK;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_KETERANGAN;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_LOKASI_ASSET_BY_SYSTEM;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_LOKASI_UPDATE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_NAMA_ASSET;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_NAMA_PENANGGUNG_JAWAB;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_NAMA_PENGGUNA;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_NAMA_PENGGUNA_UPDATE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_NILAI_BELI;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_RFID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_STATUS;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_TANGGAL_BELI;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_TIMESTAMP;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_UNIT_AKTUAL;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_UNIT_SELISIH;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_UNIT_SISTEM;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.CREATE_TABLE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.TABLE_NAME;
//import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ID;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "assets_db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create assets table
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    // Upgrading db
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

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
//        values.put(COLUMN_ASSET_RFID, asset);
//
//        // insert row
//        long id = db.insert(TABLE_NAME, null, values);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Asset getAsset(long code) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{/*COLUMN_ID, */COLUMN_FIXED_ASSET_CODE, COLUMN_NAMA_ASSET, COLUMN_UNIT_SISTEM,
                        COLUMN_TANGGAL_BELI, COLUMN_NILAI_BELI, COLUMN_UNIT_AKTUAL, COLUMN_UNIT_SELISIH, COLUMN_STATUS,
                        COLUMN_DEPT_LOB, COLUMN_LOKASI_ASSET_BY_SYSTEM, COLUMN_LOKASI_UPDATE,
                        COLUMN_NAMA_PENGGUNA, COLUMN_NAMA_PENGGUNA_UPDATE, COLUMN_NAMA_PENANGGUNG_JAWAB, COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE,
                        COLUMN_RFID, COLUMN_KETERANGAN, COLUMN_IMAGE_LINK, COLUMN_TIMESTAMP},
                /*COLUMN_ID + "=?",*/
                COLUMN_FIXED_ASSET_CODE,
                new String[]{String.valueOf(code)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare asset object
        Asset asset = new Asset(
//                Objects.requireNonNull(cursor).getInt(cursor.getColumnIndex(COLUMN_ID)),
                Objects.requireNonNull(cursor).getString(cursor.getColumnIndex(COLUMN_FIXED_ASSET_CODE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_ASSET)),
                cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_SISTEM)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TANGGAL_BELI)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NILAI_BELI)),
                cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_AKTUAL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_SELISIH)),
                cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DEPT_LOB)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DEPT_LOB_UPDATE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_ASSET_BY_SYSTEM)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_UPDATE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENGGUNA)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENGGUNA_UPDATE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENANGGUNG_JAWAB)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_RFID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_KETERANGAN)),
                cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_LINK)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return asset;
    }

    public List<Asset> getAllAssets() {
        List<Asset> assets = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
//                Asset.COLUMN_TIMESTAMP + " DESC";
                /*COLUMN_ID*/COLUMN_LOKASI_ASSET_BY_SYSTEM + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Asset asset = new Asset();
//                asset.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                asset.setTxtFixedAssetCode(cursor.getString(cursor.getColumnIndex(COLUMN_FIXED_ASSET_CODE)));
                asset.setTxtNamaAsset(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_ASSET)));
                asset.setIntUnitSistem(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_SISTEM)));
                asset.setDtmTanggalBeli(cursor.getString(cursor.getColumnIndex(COLUMN_TANGGAL_BELI)));
                asset.setIntNilaiBeli(cursor.getString(cursor.getColumnIndex(COLUMN_NILAI_BELI)));
                asset.setIntUnitAktual(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_AKTUAL)));
                asset.setIntUnitSelisih(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_SELISIH)));
                asset.setTxtStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                asset.setTxtDeptLob(cursor.getString(cursor.getColumnIndex(COLUMN_DEPT_LOB)));
                asset.setTxtDeptLobUpdate(cursor.getString(cursor.getColumnIndex(COLUMN_DEPT_LOB_UPDATE)));
                asset.setTxtLokasiAssetBySystem(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_ASSET_BY_SYSTEM)));
                asset.setTxtLokasiUpdate(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_UPDATE)));
                asset.setTxtNamaPengguna(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENGGUNA)));
                asset.setTxtNamaPenggunaUpdate(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENGGUNA_UPDATE)));
                asset.setTxtNamaPenanggungJawab(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENANGGUNG_JAWAB)));
                asset.setTxtNamaPenanggungJawabUpdate(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE)));
                asset.setTxtRfid(cursor.getString(cursor.getColumnIndex(COLUMN_RFID)));
                asset.setTxtKeterangan(cursor.getString(cursor.getColumnIndex(COLUMN_KETERANGAN)));
                asset.setTxtImageLink(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_LINK)));
                asset.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));

                assets.add(asset);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return assets list
        return assets;
    }

    public List<Asset> getAllAssetsByDept(String assetLocation) {
        List<Asset> assets = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOKASI_ASSET_BY_SYSTEM + " LIKE '" + assetLocation + "' " + " ORDER BY " +
//                Asset.COLUMN_TIMESTAMP + " DESC";
                /*COLUMN_ID*/COLUMN_NAMA_PENGGUNA + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Asset asset = new Asset();
                asset.setTxtFixedAssetCode(cursor.getString(cursor.getColumnIndex(COLUMN_FIXED_ASSET_CODE)));
                asset.setTxtNamaAsset(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_ASSET)));
                asset.setIntUnitSistem(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_SISTEM)));
                asset.setDtmTanggalBeli(cursor.getString(cursor.getColumnIndex(COLUMN_TANGGAL_BELI)));
                asset.setIntNilaiBeli(cursor.getString(cursor.getColumnIndex(COLUMN_NILAI_BELI)));
                asset.setIntUnitAktual(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_AKTUAL)));
                asset.setIntUnitSelisih(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_SELISIH)));
                asset.setTxtStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                asset.setTxtDeptLob(cursor.getString(cursor.getColumnIndex(COLUMN_DEPT_LOB)));
                asset.setTxtDeptLobUpdate(cursor.getString(cursor.getColumnIndex(COLUMN_DEPT_LOB_UPDATE)));
                asset.setTxtLokasiAssetBySystem(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_ASSET_BY_SYSTEM)));
                asset.setTxtLokasiUpdate(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_UPDATE)));
                asset.setTxtNamaPengguna(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENGGUNA)));
                asset.setTxtNamaPenggunaUpdate(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENGGUNA_UPDATE)));
                asset.setTxtNamaPenanggungJawab(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENANGGUNG_JAWAB)));
                asset.setTxtNamaPenanggungJawabUpdate(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE)));
                asset.setTxtRfid(cursor.getString(cursor.getColumnIndex(COLUMN_RFID)));
                asset.setTxtKeterangan(cursor.getString(cursor.getColumnIndex(COLUMN_KETERANGAN)));
                asset.setTxtImageLink(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_LINK)));
                asset.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));

                assets.add(asset);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return assets list
        return assets;
    }

    public int getAssetsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getAssetsCountByLocation(String assetLocation) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOKASI_ASSET_BY_SYSTEM + " LIKE '" + assetLocation + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getAssetsCountByExist(String assetLocation, String assetExist) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOKASI_ASSET_BY_SYSTEM + " LIKE '" + assetLocation + "' AND " + COLUMN_STATUS + " LIKE '" + assetExist + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

// --Commented out by Inspection START (14-Jan-21 15:26):
//    public int getAssetsCountByStatusNull(String assetLocation) {
//        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_ASSET_LOCATION + " LIKE '" + assetLocation + "'" + " & " + COLUMN_ASSET_STATUS + " IS NULL ";
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
//        String countQuery = "SELECT  COUNT (*) FROM " + TABLE_NAME +
//                " WHERE " + COLUMN_ASSET_STATUS + " = " + "'" + ASSET_EXIST + "'";
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

    public void updateAsset(Asset asset) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RFID, asset.getTxtRfid());

        // updating row
        db.update(TABLE_NAME, values, /*COLUMN_ID*/COLUMN_FIXED_ASSET_CODE + " = ?",
                /*new String[]{String.valueOf(asset.getId())});*/
                new String[]{asset.getTxtFixedAssetCode()});
    }

    public void updateStatusByRfid(Asset asset, String rfid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, asset.getTxtStatus());

        // updating row
        db.update(TABLE_NAME, values, COLUMN_RFID + " = ?",
                new String[]{rfid});
    }

    public void deleteAsset(Asset asset) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, /*COLUMN_ID*/COLUMN_FIXED_ASSET_CODE + " = ?",
//                new String[]{String.valueOf(asset.getId())});
                new String[]{asset.getTxtFixedAssetCode()});
        db.close();
    }

    public long insertAsset(String asset) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_RFID, asset);

        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    /*https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists*/
    /*https://stackoverflow.com/questions/20838233/sqliteexception-unrecognized-token-when-reading-from-database*/
    public boolean checkIsRfidInDB(String rfid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COLUMN_RFID + " = " + "'" + rfid + "'";
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
//        String Query = "Select * from " + TABLE_NAME + " where " + COLUMN_ASSET_STATUS + " = " + "'" + status + "'";
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
        String Query = "Select * from " + TABLE_NAME + " where " + COLUMN_FIXED_ASSET_CODE + " = " + "'" + itemCode + "'";
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

        values.put(COLUMN_FIXED_ASSET_CODE, Vi.get(COLUMN_FIXED_ASSET_CODE));
        values.put(COLUMN_NAMA_ASSET, Vi.get(COLUMN_NAMA_ASSET));
        values.put(COLUMN_UNIT_SISTEM, Vi.get(COLUMN_UNIT_SISTEM));
        values.put(COLUMN_TANGGAL_BELI, Vi.get(COLUMN_TANGGAL_BELI));
        values.put(COLUMN_NILAI_BELI, Vi.get(COLUMN_NILAI_BELI));
        values.put(COLUMN_UNIT_AKTUAL, Vi.get(COLUMN_UNIT_AKTUAL));
        values.put(COLUMN_UNIT_SELISIH, Vi.get(COLUMN_UNIT_SELISIH));
        values.put(COLUMN_STATUS, Vi.get(COLUMN_STATUS));
        values.put(COLUMN_DEPT_LOB, Vi.get(COLUMN_DEPT_LOB));
        values.put(COLUMN_DEPT_LOB_UPDATE, Vi.get(COLUMN_DEPT_LOB_UPDATE));
        values.put(COLUMN_LOKASI_ASSET_BY_SYSTEM, Vi.get(COLUMN_LOKASI_ASSET_BY_SYSTEM));
        values.put(COLUMN_LOKASI_UPDATE, Vi.get(COLUMN_LOKASI_UPDATE));
        values.put(COLUMN_NAMA_PENGGUNA, Vi.get(COLUMN_NAMA_PENGGUNA));
        values.put(COLUMN_NAMA_PENGGUNA_UPDATE, Vi.get(COLUMN_NAMA_PENGGUNA_UPDATE));
        values.put(COLUMN_NAMA_PENANGGUNG_JAWAB, Vi.get(COLUMN_NAMA_PENANGGUNG_JAWAB));
        values.put(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE, Vi.get(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE));
        values.put(COLUMN_RFID, Vi.get(COLUMN_RFID));
        values.put(COLUMN_KETERANGAN, Vi.get(COLUMN_KETERANGAN));
        values.put(COLUMN_IMAGE_LINK, Vi.get(COLUMN_IMAGE_LINK));
        values.put(COLUMN_TIMESTAMP, Vi.get(COLUMN_TIMESTAMP));

        //etc
        database.insert(TABLE_NAME, null, values);
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
        values.put(COLUMN_RFID, "");

        // updating row
        db.update(TABLE_NAME, values, COLUMN_FIXED_ASSET_CODE + " = ?",
                new String[]{itemCode});
    }
}
