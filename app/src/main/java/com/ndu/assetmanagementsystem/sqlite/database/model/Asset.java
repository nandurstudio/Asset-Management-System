package com.ndu.assetmanagementsystem.sqlite.database.model;

/*https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/*/
public class Asset {
    public static final String TABLE_NAME = "Asset";

    public static final String ASSET_EXIST = "Asset Ada";

    //public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIXED_ASSET_CODE = "txtFixedAssetCode";
    public static final String COLUMN_NAMA_ASSET = "txtNamaAsset";
    public static final String COLUMN_UNIT_SISTEM = "intUnitSistem";
    public static final String COLUMN_TANGGAL_BELI = "dtmTanggalBeli";
    public static final String COLUMN_NILAI_BELI = "intNilaiBeli";
    public static final String COLUMN_UNIT_AKTUAL = "intUnitAktual";
    public static final String COLUMN_UNIT_SELISIH = "intUnitSelisih";
    public static final String COLUMN_STATUS = "txtStatus";
    public static final String COLUMN_DEPT_LOB = "txtDeptLob";
    public static final String COLUMN_DEPT_LOB_UPDATE = "txtDeptLobUpdate";
    public static final String COLUMN_LOKASI_ASSET_BY_SYSTEM = "txtLokasiAssetBySystem";
    public static final String COLUMN_LOKASI_UPDATE = "txtLokasiUpdate";
    public static final String COLUMN_NAMA_PENGGUNA = "txtNamaPengguna";
    public static final String COLUMN_NAMA_PENGGUNA_UPDATE = "txtNamaPenggunaUpdate";
    public static final String COLUMN_NAMA_PENANGGUNG_JAWAB = "txtNamaPenanggungJawab";
    public static final String COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE = "txtNamaPenanggungJawabUpdate";
    public static final String COLUMN_KETERANGAN = "txtKeterangan";
    public static final String COLUMN_RFID = "txtRfid";
    public static final String COLUMN_IMAGE_LINK = "txtImageLink";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //private int id;
    //sesuai asset
    private String txtFixedAssetCode;
    private String txtNamaAsset;
    private int intUnitSistem;
    private String dtmTanggalBeli;
    private int intNilaiBeli;
    private int intUnitAktual;
    private int intUnitSelisih;
    private String txtStatus;
    private String txtDeptLob;
    private String txtDeptLobUpdate;
    private String txtLokasiAssetBySystem;
    private String txtLokasiUpdate;
    private String txtNamaPengguna;
    private String txtNamaPenggunaUpdate;
    private String txtNamaPenanggungJawab;
    private String txtNamaPenanggungJawabUpdate;
    private String txtKeterangan;
    private String txtRfid;
    private String txtImageLink;
    private String timestamp;

    public static String getColumnRfid() {
        return COLUMN_RFID;
    }

    public String getTxtRfid() {
        return txtRfid;
    }

    public void setTxtRfid(String txtRfid) {
        this.txtRfid = txtRfid;
    }

    public static String getColumnTimestamp() {
        return COLUMN_TIMESTAMP;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getAssetExist() {
        return ASSET_EXIST;
    }

    public static String getColumnFixedAssetCode() {
        return COLUMN_FIXED_ASSET_CODE;
    }

    public static String getColumnNamaAsset() {
        return COLUMN_NAMA_ASSET;
    }

    public static String getColumnUnitSistem() {
        return COLUMN_UNIT_SISTEM;
    }

    public static String getColumnTanggalBeli() {
        return COLUMN_TANGGAL_BELI;
    }

    public static String getColumnNilaiBeli() {
        return COLUMN_NILAI_BELI;
    }

    public static String getColumnUnitAktual() {
        return COLUMN_UNIT_AKTUAL;
    }

    public static String getColumnUnitSelisih() {
        return COLUMN_UNIT_SELISIH;
    }

    public static String getColumnStatus() {
        return COLUMN_STATUS;
    }

    public static String getColumnDeptLob() {
        return COLUMN_DEPT_LOB;
    }

    public static String getColumnDeptLobUpdate() {
        return COLUMN_DEPT_LOB_UPDATE;
    }

    public static String getColumnLokasiAssetBySystem() {
        return COLUMN_LOKASI_ASSET_BY_SYSTEM;
    }

    public static String getColumnLokasiUpdate() {
        return COLUMN_LOKASI_UPDATE;
    }

    public static String getColumnNamaPengguna() {
        return COLUMN_NAMA_PENGGUNA;
    }

    public static String getColumnNamaPenggunaUpdate() {
        return COLUMN_NAMA_PENGGUNA_UPDATE;
    }

    public static String getColumnNamaPenanggungJawab() {
        return COLUMN_NAMA_PENANGGUNG_JAWAB;
    }

    public static String getColumnNamaPenanggungJawabUpdate() {
        return COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE;
    }

    public static String getColumnKeterangan() {
        return COLUMN_KETERANGAN;
    }

    public static String getColumnImageLink() {
        return COLUMN_IMAGE_LINK;
    }

    public String getTxtFixedAssetCode() {
        return txtFixedAssetCode;
    }

    public void setTxtFixedAssetCode(String txtFixedAssetCode) {
        this.txtFixedAssetCode = txtFixedAssetCode;
    }

    public String getTxtNamaAsset() {
        return txtNamaAsset;
    }

    public void setTxtNamaAsset(String txtNamaAsset) {
        this.txtNamaAsset = txtNamaAsset;
    }

    public int getIntUnitSistem() {
        return intUnitSistem;
    }

    public void setIntUnitSistem(int intUnitSistem) {
        this.intUnitSistem = intUnitSistem;
    }

    public String getDtmTanggalBeli() {
        return dtmTanggalBeli;
    }

    public void setDtmTanggalBeli(String dtmTanggalBeli) {
        this.dtmTanggalBeli = dtmTanggalBeli;
    }

    public int getIntNilaiBeli() {
        return intNilaiBeli;
    }

    public void setIntNilaiBeli(int intNilaiBeli) {
        this.intNilaiBeli = intNilaiBeli;
    }

    public int getIntUnitAktual() {
        return intUnitAktual;
    }

    public void setIntUnitAktual(int intUnitAktual) {
        this.intUnitAktual = intUnitAktual;
    }

    public int getIntUnitSelisih() {
        return intUnitSelisih;
    }

    public void setIntUnitSelisih(int intUnitSelisih) {
        this.intUnitSelisih = intUnitSelisih;
    }

    public String getTxtStatus() {
        return txtStatus;
    }

    public void setTxtStatus(String txtStatus) {
        this.txtStatus = txtStatus;
    }

    public String getTxtDeptLob() {
        return txtDeptLob;
    }

    public void setTxtDeptLob(String txtDeptLob) {
        this.txtDeptLob = txtDeptLob;
    }

    public String getTxtDeptLobUpdate() {
        return txtDeptLobUpdate;
    }

    public void setTxtDeptLobUpdate(String txtDeptLobUpdate) {
        this.txtDeptLobUpdate = txtDeptLobUpdate;
    }

    public String getTxtLokasiAssetBySystem() {
        return txtLokasiAssetBySystem;
    }

    public void setTxtLokasiAssetBySystem(String txtLokasiAssetBySystem) {
        this.txtLokasiAssetBySystem = txtLokasiAssetBySystem;
    }

    public String getTxtLokasiUpdate() {
        return txtLokasiUpdate;
    }

    public void setTxtLokasiUpdate(String txtLokasiUpdate) {
        this.txtLokasiUpdate = txtLokasiUpdate;
    }

    public String getTxtNamaPengguna() {
        return txtNamaPengguna;
    }

    public void setTxtNamaPengguna(String txtNamaPengguna) {
        this.txtNamaPengguna = txtNamaPengguna;
    }

    public String getTxtNamaPenggunaUpdate() {
        return txtNamaPenggunaUpdate;
    }

    public void setTxtNamaPenggunaUpdate(String txtNamaPenggunaUpdate) {
        this.txtNamaPenggunaUpdate = txtNamaPenggunaUpdate;
    }

    public String getTxtNamaPenanggungJawab() {
        return txtNamaPenanggungJawab;
    }

    public void setTxtNamaPenanggungJawab(String txtNamaPenanggungJawab) {
        this.txtNamaPenanggungJawab = txtNamaPenanggungJawab;
    }

    public String getTxtNamaPenanggungJawabUpdate() {
        return txtNamaPenanggungJawabUpdate;
    }

    public void setTxtNamaPenanggungJawabUpdate(String txtNamaPenanggungJawabUpdate) {
        this.txtNamaPenanggungJawabUpdate = txtNamaPenanggungJawabUpdate;
    }

    public String getTxtKeterangan() {
        return txtKeterangan;
    }

    public void setTxtKeterangan(String txtKeterangan) {
        this.txtKeterangan = txtKeterangan;
    }

    public String getTxtImageLink() {
        return txtImageLink;
    }

    public void setTxtImageLink(String txtImageLink) {
        this.txtImageLink = txtImageLink;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

    //note
    /*    B  : assets ada, kondisinya BAIK
    RA  : asset ada, kondisinya RUSAK DAPAT DIPERBAIKI
    RD : asset ada, kondisinya RUSAK TIDAK DAPAT DIPERBAIKI
    TK : asset TIDAK KETEMU
    I : asset Idle, kondisinya Baik tetapi tidak digunakan*/


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    //+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FIXED_ASSET_CODE + " TEXT PRIMARY KEY,"
                    + COLUMN_NAMA_ASSET + " TEXT,"
                    + COLUMN_UNIT_SISTEM + " TEXT,"
                    + COLUMN_TANGGAL_BELI + " DATETIME,"
                    + COLUMN_NILAI_BELI + " TEXT,"
                    + COLUMN_UNIT_AKTUAL + " TEXT,"
                    + COLUMN_UNIT_SELISIH + " TEXT,"
                    + COLUMN_STATUS + " TEXT,"
                    + COLUMN_DEPT_LOB + " TEXT,"
                    + COLUMN_DEPT_LOB_UPDATE + " TEXT,"
                    + COLUMN_LOKASI_ASSET_BY_SYSTEM + " TEXT,"
                    + COLUMN_LOKASI_UPDATE + " TEXT,"
                    + COLUMN_NAMA_PENGGUNA + " TEXT,"
                    + COLUMN_NAMA_PENGGUNA_UPDATE + " TEXT,"
                    + COLUMN_NAMA_PENANGGUNG_JAWAB + " TEXT,"
                    + COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE + " TEXT,"
                    + COLUMN_KETERANGAN + " TEXT,"
                    + COLUMN_RFID + " TEXT,"
                    + COLUMN_IMAGE_LINK + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Asset() {
    }

    public Asset(
            String txt_fixed_asset_code,
            String txt_nama_asset,
            int int_unit_sistem,
            String dtm_tanggal_beli,
            int int_nilai_beli,
            int int_unit_aktual,
            int int_unit_selisih,
            String txt_status,
            String txt_dept_lob,
            String txt_dept_lob_update,
            String txt_lokasi_asset_by_system,
            String txt_lokasi_update,
            String txt_nama_pengguna,
            String txt_nama_pengguna_update,
            String txt_nama_penanggung_jawab,
            String txt_nama_penanggung_jawab_update,
            String txt_rfid,
            String txt_keterangan,
            String txt_image_link,
            String timestamp) {
        //this.id = id;
        this.txtFixedAssetCode = txt_fixed_asset_code;
        this.txtNamaAsset = txt_nama_asset;
        this.intUnitSistem = int_unit_sistem;
        this.dtmTanggalBeli = dtm_tanggal_beli;
        this.intNilaiBeli = int_nilai_beli;
        this.intUnitAktual = int_unit_aktual;
        this.intUnitSelisih = int_unit_selisih;
        this.txtStatus = txt_status;
        this.txtDeptLob = txt_dept_lob;
        this.txtDeptLobUpdate = txt_dept_lob_update;
        this.txtLokasiAssetBySystem = txt_lokasi_asset_by_system;
        this.txtLokasiUpdate = txt_lokasi_update;
        this.txtNamaPengguna = txt_nama_pengguna;
        this.txtNamaPenggunaUpdate = txt_nama_pengguna_update;
        this.txtNamaPenanggungJawab = txt_nama_penanggung_jawab;
        this.txtNamaPenanggungJawabUpdate = txt_nama_penanggung_jawab_update;
        this.txtRfid = txt_rfid;
        this.txtKeterangan = txt_keterangan;
        this.txtImageLink = txt_image_link;
        this.timestamp = timestamp;
    }
}
