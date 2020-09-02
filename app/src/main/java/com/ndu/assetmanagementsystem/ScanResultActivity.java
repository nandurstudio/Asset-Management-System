package com.ndu.assetmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.ndu.assetmanagementsystem.MainActivity.DEPT_NAME;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.ASSET_EXIST;

public class ScanResultActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView totalAsset;
    private String assetLocation;
    private String TAG = "ScanResultActivity";
    private TextView readAbleAsset;
    private TextView unReadAbleAsset;
    private TextView percentageAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.assets_result);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            assetLocation = (String) bundle.get(DEPT_NAME);
            toolbar.setTitle(getResources().getString(R.string.asset_result) + " Of " + assetLocation.substring(1));
        }

        //Initializing
        totalAsset = findViewById(R.id.textView_total_asset);
        readAbleAsset = findViewById(R.id.textView_readble_units);
        unReadAbleAsset = findViewById(R.id.textView_unreadble_units);
        percentageAsset = findViewById(R.id.textView_readble_result);

        totalAsset.setText(String.valueOf(db.getAssetsCountByLocation(assetLocation)));
        readAbleAsset.setText(String.valueOf(db.getAssetsCountByExist(assetLocation, ASSET_EXIST)));

        try {

            double totalAssetInt = Double.parseDouble(totalAsset.getText().toString());
            double readAbleAssetInt = Double.parseDouble(readAbleAsset.getText().toString());

            double unRead = totalAssetInt - readAbleAssetInt;
            BigDecimal bdUnread = new BigDecimal(unRead).setScale(0, RoundingMode.HALF_EVEN);
            unReadAbleAsset.setText(String.valueOf(bdUnread));
            double percenTage = (readAbleAssetInt / totalAssetInt) * 100;
            Log.d(TAG, "onCreate: " + percenTage);
            //https://stackoverflow.com/a/4826827/7772358
            BigDecimal bd = new BigDecimal(percenTage).setScale(2, RoundingMode.HALF_EVEN);
            percenTage = bd.doubleValue();
            percentageAsset.setText(String.valueOf(bd));

        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}