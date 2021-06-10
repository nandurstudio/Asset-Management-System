package com.ndu.assetmanagementsystem;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_AREA;
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

public class AssetDetailActivity extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);
        TextInputEditText txtFixedAssetCode = findViewById(R.id.textInput_txtFixedAssetCode);
        TextInputEditText txtNamaAsset = findViewById(R.id.textInput_txtNamaAsset);
        TextInputEditText intUnitSistem = findViewById(R.id.textInput_intUnitSistem);
        TextInputEditText dtmTanggalBeli = findViewById(R.id.textInput_dtmTanggalBeli);
        TextInputEditText intNilaiBeli = findViewById(R.id.textInput_intNilaiBeli);
        TextInputEditText intUnitAktual = findViewById(R.id.textInput_intUnitAktual);
        TextInputEditText intUnitSelisih = findViewById(R.id.textInput_intUnitSelisih);
        TextInputEditText txtStatus = findViewById(R.id.textInput_txtStatus);
        TextInputEditText txtDeptLob = findViewById(R.id.textInput_txtDeptLob);
        TextInputEditText txtDeptLobUpdate = findViewById(R.id.textInput_txtDeptLobUpdate);
        TextInputEditText txtLokasiAssetBySystem = findViewById(R.id.textInput_txtLokasiAssetBySystem);
        TextInputEditText txtLokasiUpdate = findViewById(R.id.textInput_txtLokasiUpdate);
        TextInputEditText txtNamaPengguna = findViewById(R.id.textInput_txtNamaPengguna);
        TextInputEditText txtNamaPenggunaUpdate = findViewById(R.id.textInput_txtNamaPenggunaUpdate);
        TextInputEditText txtNamaPenanggungJawab = findViewById(R.id.textInput_txtNamaPenanggungJawab);
        TextInputEditText txtNamaPenanggungJawabUpdate = findViewById(R.id.textInput_txtNamaPenanggungJawabUpdate);
        TextInputEditText txtKeterangan = findViewById(R.id.textInput_txtKeterangan);
        TextInputEditText txtRfid = findViewById(R.id.textInput_txtRfid);
        TextInputEditText txtImageLink = findViewById(R.id.textInput_txtImageLink);
        TextInputEditText txtAssetArea = findViewById(R.id.textInput_txtAssetArea);
        TextView timestamp = findViewById(R.id.textView_timestamp);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.asset_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        /*OnClick Handling*/
        toolbar.setNavigationOnClickListener(view -> finish());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AssetDetailActivity.this);

        String _txtFixedAssetCodeSh = sharedPreferences.getString(COLUMN_FIXED_ASSET_CODE, "");
        String _txtNamaAssetSh = sharedPreferences.getString(COLUMN_NAMA_ASSET, "");
        int _intUnitSistemSh = sharedPreferences.getInt(COLUMN_UNIT_SISTEM, 0);
        String _dtmTanggalBeliSh = sharedPreferences.getString(COLUMN_TANGGAL_BELI, "");
        int _intNilaiBeliSh = sharedPreferences.getInt(COLUMN_NILAI_BELI, 0);
        int _intUnitAktualSh = sharedPreferences.getInt(COLUMN_UNIT_AKTUAL, 0);
        int _intUnitSelisihSh = sharedPreferences.getInt(COLUMN_UNIT_SELISIH, 0);
        String _txtStatusSh = sharedPreferences.getString(COLUMN_STATUS, "");
        String _txtDeptLobSh = sharedPreferences.getString(COLUMN_DEPT_LOB, "");
        String _txtDeptLobUpdateSh = sharedPreferences.getString(COLUMN_DEPT_LOB_UPDATE, "");
        String _txtLokasiAssetBySystemSh = sharedPreferences.getString(COLUMN_LOKASI_ASSET_BY_SYSTEM, "");
        String _txtLokasiUpdateSh = sharedPreferences.getString(COLUMN_LOKASI_UPDATE, "");
        String _txtNamaPenggunaSh = sharedPreferences.getString(COLUMN_NAMA_PENGGUNA, "");
        String _txtNamaPenggunaUpdateSh = sharedPreferences.getString(COLUMN_NAMA_PENGGUNA_UPDATE, "");
        String _txtNamaPenanggungJawabSh = sharedPreferences.getString(COLUMN_NAMA_PENANGGUNG_JAWAB, "");
        String _txtNamaPenanggungJawabUpdateSh = sharedPreferences.getString(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE, "");
        String _txtKeteranganSh = sharedPreferences.getString(COLUMN_KETERANGAN, "");
        String _txtRfidSh = sharedPreferences.getString(COLUMN_RFID, "");
        String _txtImageLinkSh = sharedPreferences.getString(COLUMN_IMAGE_LINK, "");
        String _txtAssetAreaSh = sharedPreferences.getString(COLUMN_ASSET_AREA, "");
        String _timestampSh = sharedPreferences.getString(COLUMN_TIMESTAMP, "");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(_dtmTanggalBeliSh);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        System.out.println("Date :" + formatter.format(Objects.requireNonNull(date)));

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        txtFixedAssetCode.setText(_txtFixedAssetCodeSh);
        txtNamaAsset.setText(_txtNamaAssetSh);
        intUnitSistem.setText(String.valueOf(_intUnitSistemSh));
        dtmTanggalBeli.setText(formatter.format(date));
        intNilaiBeli.setText(formatRupiah.format((double) _intNilaiBeliSh));
        intUnitAktual.setText(String.valueOf(_intUnitAktualSh));
        intUnitSelisih.setText(String.valueOf(_intUnitSelisihSh));
        txtStatus.setText(_txtStatusSh);
        txtDeptLob.setText(_txtDeptLobSh);
        txtDeptLobUpdate.setText(_txtDeptLobUpdateSh);
        txtLokasiAssetBySystem.setText(_txtLokasiAssetBySystemSh);
        txtLokasiUpdate.setText(_txtLokasiUpdateSh);
        txtNamaPengguna.setText(_txtNamaPenggunaSh);
        txtNamaPenggunaUpdate.setText(_txtNamaPenggunaUpdateSh);
        txtNamaPenanggungJawab.setText(_txtNamaPenanggungJawabSh);
        txtNamaPenanggungJawabUpdate.setText(_txtNamaPenanggungJawabUpdateSh);
        txtKeterangan.setText(_txtKeteranganSh);
        txtRfid.setText(_txtRfidSh);
        txtImageLink.setText(_txtImageLinkSh);
        txtAssetArea.setText(_txtAssetAreaSh);
        timestamp.setText("Last sync: " + _timestampSh);

    }
}