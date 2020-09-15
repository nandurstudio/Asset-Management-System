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

import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_CODE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_DESC;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_LOCATION;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_PIC;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_RFID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_STATUS;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_TIMESTAMP;

public class AssetDetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);
        TextInputEditText assetCode = findViewById(R.id.textInput_assetCode);
        TextInputEditText assetRfid = findViewById(R.id.textInput_assetRfid);
        TextInputEditText assetDesc = findViewById(R.id.textInput_assetDesc);
        TextInputEditText assetPic = findViewById(R.id.textInput_assetPic);
        TextInputEditText assetLocation = findViewById(R.id.textInput_assetLocation);
        TextInputEditText assetStatus = findViewById(R.id.textInput_assetStatus);
        TextView assetTimestamp = findViewById(R.id.textView_timestamp);

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

        String assetCodeSh = sharedPreferences.getString(COLUMN_ASSET_CODE, "");
        String assetRfidSh = sharedPreferences.getString(COLUMN_ASSET_RFID, "");
        String assetDescSh = sharedPreferences.getString(COLUMN_ASSET_DESC, "");
        String assetPicSh = sharedPreferences.getString(COLUMN_ASSET_PIC, "");
        String assetLocationSh = sharedPreferences.getString(COLUMN_ASSET_LOCATION, "");
        String assetStatusSh = sharedPreferences.getString(COLUMN_ASSET_STATUS, "");
        String assetTimestampSh = sharedPreferences.getString(COLUMN_TIMESTAMP, "");

        assetCode.setText(assetCodeSh);
        assetRfid.setText(assetRfidSh);
        assetDesc.setText(assetDescSh);
        assetPic.setText(assetPicSh);
        assetLocation.setText(assetLocationSh);
        assetStatus.setText(assetStatusSh);
        assetTimestamp.setText("Last sync: " + assetTimestampSh);

    }
}