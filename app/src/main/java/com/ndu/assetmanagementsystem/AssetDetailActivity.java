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

    @SuppressLint("SetTextI18n")
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
        String _intUnitSistemSh = sharedPreferences.getString(COLUMN_UNIT_SISTEM, "");
        String _dtmTanggalBeliSh = sharedPreferences.getString(COLUMN_TANGGAL_BELI, "");
        String _intNilaiBeliSh = sharedPreferences.getString(COLUMN_NILAI_BELI, "");
        String _intUnitAktualSh = sharedPreferences.getString(COLUMN_UNIT_AKTUAL, "");
        String _intUnitSelisihSh = sharedPreferences.getString(COLUMN_UNIT_SELISIH, "");
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
        String _timestampSh = sharedPreferences.getString(COLUMN_TIMESTAMP, "");

        txtFixedAssetCode.setText(_txtFixedAssetCodeSh);
        txtNamaAsset.setText(_txtNamaAssetSh);
        intUnitSistem.setText(_intUnitSistemSh);
        dtmTanggalBeli.setText(_dtmTanggalBeliSh);
        intNilaiBeli.setText(_intNilaiBeliSh);
        intUnitAktual.setText(_intUnitAktualSh);
        intUnitSelisih.setText(_intUnitSelisihSh);
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
        timestamp.setText("Last sync: " + _timestampSh);

    }
}