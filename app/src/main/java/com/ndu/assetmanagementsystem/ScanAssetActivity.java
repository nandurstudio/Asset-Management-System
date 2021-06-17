package com.ndu.assetmanagementsystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipherlab.rfid.DeviceEvent;
import com.cipherlab.rfid.GeneralString;
import com.cipherlab.rfidapi.RfidManager;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper;
import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelperV2;
import com.ndu.assetmanagementsystem.sqlite.database.model.Asset;
import com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2;
import com.ndu.assetmanagementsystem.sqlite.utils.MyDividerItemDecoration;
import com.ndu.assetmanagementsystem.sqlite.utils.RecyclerTouchListener;
import com.ndu.assetmanagementsystem.sqlite.view.AssetsAdapter;
import com.ndu.assetmanagementsystem.sqlite.view.AssetsAdapterV2;
import com.ndu.simpledialog.SimpleDialog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ir.androidexception.filepicker.dialog.SingleFilePickerDialog;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.ndu.assetmanagementsystem.MainActivity.ASSET_AREA;
import static com.ndu.assetmanagementsystem.MainActivity.DEPT_NAME;
import static com.ndu.assetmanagementsystem.MainActivity.DIV_AREA;
import static com.ndu.assetmanagementsystem.SettingsActivity.SettingsFragment.DATABASE_VERSION;
import static com.ndu.assetmanagementsystem.SettingsActivity.SettingsFragment.SCAN_RESULT_INDICATOR;
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
import static com.ndu.simpletoaster.SimpleToaster.toaster;

public class ScanAssetActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "rfid";
    private static final String XML_PATH = "xml_path";
    private static final String KEY_RFID_TAG = "key_rfid_tag";
    // --Commented out by Inspection (14-Jan-21 15:25):private static final String DEMO_MODE = "1";
    public static final String AMEN_MODE = "2";
    private AssetsAdapter mAdapter;
    private AssetsAdapterV2 mAdapterV2;
    private final List<Asset> assetList = new ArrayList<>();
    private final List<AssetV2> assetListV2 = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noAssetView;

    private DatabaseHelper db;
    private DatabaseHelperV2 db_v2;
    //RFID
    RfidManager mRfidManager = null;
    private String assetLocation;
    private Drawable dialogIcon;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private String sharedDBVersion;
    private String assetArea;
    private String assetCode;
    private String sharedTag;
    private String assetTag;
    private TextView registeredText;
    private TextView scannedText;
    private TextView percentageText;
    private String dept;
    private String div;
    private String scanResult;
    private String formattedDate;
//    private ProgressBar spinner;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_asset);

        //[START Initialize]

        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        noAssetView = findViewById(R.id.empty_assets_view);
        Button buttResult = findViewById(R.id.button_result);
        Button buttAssetMap = findViewById(R.id.button_asset_map);
        registeredText = findViewById(R.id.text_registered_asset);
        scannedText = findViewById(R.id.text_scanned_asset);
        percentageText = findViewById(R.id.text_precentage);
        dialogIcon = getResources().getDrawable(R.drawable.ic_info_outline_black_24dp);
//        spinner = (ProgressBar) findViewById(R.id.progressBar);
        db = new DatabaseHelper(this);
        db_v2 = new DatabaseHelperV2(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        scanResult = preferences.getString(SCAN_RESULT_INDICATOR, "B");
        Log.d(TAG, "onCreate: SCAN_RESULTINDICATOR: " + scanResult);

        toolbar.setTitle(getResources().getString(R.string.assets_list));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        sharedDBVersion = preferences.getString(DATABASE_VERSION, "1");

        /*OnClick Handling*/
        toolbar.setNavigationOnClickListener(view -> finish());
        buttResult.setOnClickListener(view -> goToResult());
        buttAssetMap.setOnClickListener(view -> startSound());

        /*Receive data from MainActivity*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            dept = (String) bundle.get(DEPT_NAME);
            div = (String) bundle.get(DIV_AREA);
            assetArea = (String) bundle.get(ASSET_AREA);
            assetLocation = "%" + dept;
            toolbar.setTitle(getResources().getString(R.string.assets_list) + " " + dept);
            Log.d(TAG, "onCreate assetLocation: " + assetLocation);
            Log.d(TAG, "onCreate assetArea: " + assetArea);
            Log.d(TAG, "onCreate div: " + div);
            Log.d(TAG, "onCreate dept: " + dept);
        }

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        formattedDate = df.format(c);

        /*Storage permission*/
        runDexter();

        /*Create Asset table in Asset.db*/
        try {
            if (sharedDBVersion.equals(AMEN_MODE)) {
                db_v2.createTable();
            } else {
                db.createTable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Todo: Hidden scanned asset
        /*Add all assets in Asset.db to recyclerView*/
        assetListV2.addAll(db_v2.getAllAssetsByDept(assetLocation));
        //assetListV2.addAll(db_v2.getAllAssetsByExist());
        mAdapterV2 = new AssetsAdapterV2(assetListV2);

        assetList.addAll(db.getAllAssetsByDept(assetLocation));
        mAdapter = new AssetsAdapter(assetList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));

        if (sharedDBVersion.equals(AMEN_MODE)) {
            recyclerView.setAdapter(mAdapterV2);
            toggleEmptyAssetsV2();
        } else {
            recyclerView.setAdapter(mAdapter);
            toggleEmptyAssets();
        }

        /*RFID Section*/
        mRfidManager = RfidManager.InitInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(GeneralString.Intent_RFIDSERVICE_CONNECTED);
        filter.addAction(GeneralString.Intent_RFIDSERVICE_TAG_DATA);
        filter.addAction(GeneralString.Intent_RFIDSERVICE_EVENT);
        filter.addAction(GeneralString.Intent_FWUpdate_ErrorMessage);
        filter.addAction(GeneralString.Intent_FWUpdate_Percent);
        filter.addAction(GeneralString.Intent_FWUpdate_Finish);
        registerReceiver(myDataReceiver, filter);

        /*live count*/
        liveCountAll();

        /*
          On long press on RecyclerView item, open alert dialog
          with options to choose
          Edit and Delete
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    clearSharedPrefV2();
                    try {
                        final AssetV2 assetV2 = assetListV2.get(position);
                        String assetCode = assetV2.getTxtFixedAssetCode();
                        String assetDesc = assetV2.getTxtAssetDescription();
                        String assetCategory = assetV2.getTxtAssetCategory();
                        String assetSupervisorID = assetV2.getTxtSupervisorID();
                        int assetAcquisition = assetV2.getDecAcquisition();
                        String assetPicName = assetV2.getTxtName();
                        String assetPicNick = assetV2.getTxtNick();
                        String assetPicEmail = assetV2.getTxtEmail();
                        String assetUser = assetV2.getTxtPenggunaID();
                        String assetLocation = assetV2.getTxtLokasiPengguna();
                        String assetLob = assetV2.getTxtLOBPengguna();
                        String assetArea = assetV2.getTxtArea();
                        String assetRfid = assetV2.getTxtRfid();
                        String assetStatus = assetV2.getTxtStatus();

                        String assetImgLink = assetV2.getTxtImgLink();
                        String assetNotes = assetV2.getTxtNotes();
                        String assetTimestamp = assetV2.getDtmTimestamp();

                        editor.putString(COLUMN_TXTFIXEDASSETCODE, assetCode);
                        editor.putString(COLUMN_TXTASSETDESCRIPTION, assetDesc);
                        editor.putString(COLUMN_TXTASSETCATEGORY, assetCategory);
                        editor.putString(COLUMN_TXTSUPERVISORID, assetSupervisorID);
                        editor.putInt(COLUMN_DECACQUISITION, assetAcquisition);
                        editor.putString(COLUMN_TXTNAME, assetPicName);
                        editor.putString(COLUMN_TXTNICK, assetPicNick);
                        editor.putString(COLUMN_TXTEMAIL, assetPicEmail);
                        editor.putString(COLUMN_TXTPENGGUNAID, assetUser);
                        editor.putString(COLUMN_TXTLOKASIPENGGUNA, assetLocation);
                        editor.putString(COLUMN_TXTLOBPENGGUNA, assetLob);
                        editor.putString(COLUMN_TXTAREA, assetArea);
                        editor.putString(COLUMN_TXTRFID, assetRfid);
                        editor.putString(COLUMN_TXTSTATUS, assetStatus);
                        editor.putString(COLUMN_TXTIMGLINK, assetImgLink);
                        editor.putString(COLUMN_TXTNOTES, assetNotes);
                        editor.putString(COLUMN_DTMTIMESTAMP, assetTimestamp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    clearSharedPref();
                    final Asset asset = assetList.get(position);
                    String txtFixedAssetCode = asset.getTxtFixedAssetCode();
                    String txtNamaAsset = asset.getTxtNamaAsset();
                    int intUnitSistem = asset.getIntUnitSistem();
                    String dtmTanggalBeli = asset.getDtmTanggalBeli();
                    int intNilaiBeli = asset.getIntNilaiBeli();
                    int intUnitAktual = asset.getIntUnitAktual();
                    int intUnitSelisih = asset.getIntUnitSelisih();
                    String txtStatus = asset.getTxtStatus();
                    String txtDeptLob = asset.getTxtDeptLob();
                    String txtDeptLobUpdate = asset.getTxtDeptLobUpdate();
                    String txtLokasiAssetBySystem = asset.getTxtLokasiAssetBySystem();
                    String txtLokasiUpdate = asset.getTxtLokasiUpdate();
                    String txtNamaPengguna = asset.getTxtNamaPengguna();
                    String txtNamaPenggunaUpdate = asset.getTxtNamaPenggunaUpdate();
                    String txtNamaPenanggungJawab = asset.getTxtNamaPenanggungJawab();
                    String txtNamaPenanggungJawabUpdate = asset.getTxtNamaPenanggungJawabUpdate();
                    String txtKeterangan = asset.getTxtKeterangan();
                    String txtRfid = asset.getTxtRfid();
                    String txtImageLink = asset.getTxtImageLink();
                    String txtAssetArea = asset.getTxtAssetArea();
                    String timestamp = asset.getTimestamp();

                    editor.putString(COLUMN_FIXED_ASSET_CODE, txtFixedAssetCode);
                    editor.putString(COLUMN_NAMA_ASSET, txtNamaAsset);
                    editor.putInt(COLUMN_UNIT_SISTEM, intUnitSistem);
                    editor.putString(COLUMN_TANGGAL_BELI, dtmTanggalBeli);
                    editor.putInt(COLUMN_NILAI_BELI, intNilaiBeli);
                    editor.putInt(COLUMN_UNIT_AKTUAL, intUnitAktual);
                    editor.putInt(COLUMN_UNIT_SELISIH, intUnitSelisih);
                    editor.putString(COLUMN_STATUS, txtStatus);
                    editor.putString(COLUMN_DEPT_LOB, txtDeptLob);
                    editor.putString(COLUMN_DEPT_LOB_UPDATE, txtDeptLobUpdate);
                    editor.putString(COLUMN_LOKASI_ASSET_BY_SYSTEM, txtLokasiAssetBySystem);
                    editor.putString(COLUMN_LOKASI_UPDATE, txtLokasiUpdate);
                    editor.putString(COLUMN_NAMA_PENGGUNA, txtNamaPengguna);
                    editor.putString(COLUMN_NAMA_PENGGUNA_UPDATE, txtNamaPenggunaUpdate);
                    editor.putString(COLUMN_NAMA_PENANGGUNG_JAWAB, txtNamaPenanggungJawab);
                    editor.putString(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE, txtNamaPenanggungJawabUpdate);
                    editor.putString(COLUMN_KETERANGAN, txtKeterangan);
                    editor.putString(COLUMN_RFID, txtRfid);
                    editor.putString(COLUMN_IMAGE_LINK, txtImageLink);
                    editor.putString(COLUMN_ASSET_AREA, txtAssetArea);
                    editor.putString(COLUMN_TIMESTAMP, timestamp);
                }
                editor.apply();
                goToDetail();
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    @SuppressLint("SetTextI18n")
    private void liveCountAll() {
        if (sharedDBVersion.equals(AMEN_MODE)) {
            registeredText.setText(getResources().getString(R.string.asset_with_rfid_tag) + ": " + db_v2.getAssetsCountByTag("%"));
            scannedText.setText(getResources().getString(R.string.scanned_asset) + ": " + db_v2.getAssetsCountByExist("%", scanResult));
            double totalAssetInt = db_v2.getAssetsCountByTag("%");
            double readAbleAssetInt = db_v2.getAssetsCountByExist("%", scanResult);
            try {
                double percenTage = (readAbleAssetInt / totalAssetInt) * 100;
                BigDecimal bd = new BigDecimal(percenTage).setScale(0, RoundingMode.HALF_EVEN);
                bd.doubleValue();
                percentageText.setText(getResources().getString(R.string.percentage_text) + ": " + bd + getResources().getString(R.string.percentage));
            } catch (Exception e) {
                e.printStackTrace();
                percentageText.setText(getResources().getString(R.string.percentage_text) + ": 0%");
            }

        } else {
            registeredText.setText(getResources().getString(R.string.asset_with_rfid_tag) + ": " + db.getAssetsCountByTag("%"));
            scannedText.setText(getResources().getString(R.string.scanned_asset) + ": " + db.getAssetsCountByExist("%", scanResult));
            double totalAssetInt = db.getAssetsCountByTag("%");
            double readAbleAssetInt = db.getAssetsCountByExist("%", scanResult);
            try {
                double percenTage = (readAbleAssetInt / totalAssetInt) * 100;
                BigDecimal bd = new BigDecimal(percenTage).setScale(0, RoundingMode.HALF_EVEN);
                bd.doubleValue();
                percentageText.setText(getResources().getString(R.string.percentage_text) + ": " + bd + getResources().getString(R.string.percentage));
            } catch (Exception e) {
                e.printStackTrace();
                percentageText.setText(getResources().getString(R.string.percentage_text) + ": 0%");
            }
        }
    }

    private void clearSharedPref() {
        preferences.edit().remove(COLUMN_FIXED_ASSET_CODE).apply();
        preferences.edit().remove(COLUMN_NAMA_ASSET).apply();
        preferences.edit().remove(COLUMN_UNIT_SISTEM).apply();
        preferences.edit().remove(COLUMN_TANGGAL_BELI).apply();
        preferences.edit().remove(COLUMN_NILAI_BELI).apply();
        preferences.edit().remove(COLUMN_UNIT_AKTUAL).apply();
        preferences.edit().remove(COLUMN_UNIT_SELISIH).apply();
        preferences.edit().remove(COLUMN_STATUS).apply();
        preferences.edit().remove(COLUMN_DEPT_LOB).apply();
        preferences.edit().remove(COLUMN_DEPT_LOB_UPDATE).apply();
        preferences.edit().remove(COLUMN_LOKASI_ASSET_BY_SYSTEM).apply();
        preferences.edit().remove(COLUMN_LOKASI_UPDATE).apply();
        preferences.edit().remove(COLUMN_NAMA_PENGGUNA).apply();
        preferences.edit().remove(COLUMN_NAMA_PENGGUNA_UPDATE).apply();
        preferences.edit().remove(COLUMN_NAMA_PENANGGUNG_JAWAB).apply();
        preferences.edit().remove(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE).apply();
        preferences.edit().remove(COLUMN_KETERANGAN).apply();
        preferences.edit().remove(COLUMN_RFID).apply();
        preferences.edit().remove(COLUMN_IMAGE_LINK).apply();
        preferences.edit().remove(COLUMN_ASSET_AREA).apply();
        preferences.edit().remove(COLUMN_TIMESTAMP).apply();
    }

    private void clearSharedPrefV2() {
        preferences.edit().remove(COLUMN_TXTFIXEDASSETCODE).apply();
        preferences.edit().remove(COLUMN_TXTASSETDESCRIPTION).apply();
        preferences.edit().remove(COLUMN_TXTASSETCATEGORY).apply();
        preferences.edit().remove(COLUMN_TXTSUPERVISORID).apply();
        preferences.edit().remove(COLUMN_DECACQUISITION).apply();
        preferences.edit().remove(COLUMN_TXTNAME).apply();
        preferences.edit().remove(COLUMN_TXTNICK).apply();
        preferences.edit().remove(COLUMN_TXTEMAIL).apply();
        preferences.edit().remove(COLUMN_TXTPENGGUNAID).apply();
        preferences.edit().remove(COLUMN_TXTLOKASIPENGGUNA).apply();
        preferences.edit().remove(COLUMN_TXTLOBPENGGUNA).apply();
        preferences.edit().remove(COLUMN_TXTAREA).apply();
        preferences.edit().remove(COLUMN_TXTRFID).apply();
        preferences.edit().remove(COLUMN_TXTSTATUS).apply();
        preferences.edit().remove(COLUMN_TXTIMGLINK).apply();
        preferences.edit().remove(COLUMN_TXTNOTES).apply();
        preferences.edit().remove(COLUMN_DTMTIMESTAMP).apply();
    }

    private void goToDetail() {
        Intent intent;
        if (sharedDBVersion.equals(AMEN_MODE)) {
            intent = new
                    Intent(ScanAssetActivity.this, AssetDetailV2Activity.class);
        } else {
            intent = new
                    Intent(ScanAssetActivity.this, AssetDetailActivity.class);
        }
        startActivity(intent);
    }

    private void runDexter() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        toaster(ScanAssetActivity.this, getResources().getString(R.string.storage_granted), 0);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        toaster(ScanAssetActivity.this, getResources().getString(R.string.storage_denied), 0);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_asset_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic
        if (sharedDBVersion.equals(AMEN_MODE)) {
            mAdapterV2.filter(query, db_v2, assetLocation);
        } else {
            mAdapter.filter(query, db, assetLocation);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (sharedDBVersion.equals(AMEN_MODE)) {
            mAdapterV2.filter(query, db_v2, assetLocation);
        } else {
            mAdapter.filter(query, db, assetLocation);
        }
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                goToSetting();
                return true;

/*            case R.id.action_delete_database:
                String title = getResources().getString(R.string.action_delete_database) + "?";
                String msg = "Ini akan menghapus data asset!";
                nduDialog(this,
                        title,
                        msg,
                        true,
                        dialogIcon,
                        "Yes", "Cancel",
                        (DialogInterface dialog, int which) -> {
                            if (which == BUTTON_POSITIVE) {
                                openFilePicker();
                            }
                            dialog.cancel();
                        });
                return true;*/

            case R.id.action_pull_assets:
                //https://stackoverflow.com/questions/23408756/create-a-general-class-for-custom-dialog-in-java-android
                SimpleDialog.showDialog(this,
                        getResources().getString(R.string.action_refresh_assets) + "?",
                        getResources().getString(R.string.msg_refresh_notice),
                        true,
                        dialogIcon,
                        getResources().getString(R.string.button_yes),
                        getResources().getString(R.string.button_cancel),
                        (DialogInterface dialog, int which) -> {
                            if (which == BUTTON_POSITIVE) {
                                dialog.cancel();
                                openFilePicker();
                                //progressDialog.dismiss();
                            }
                            dialog.cancel();
                        });
                liveCountAll();
                return true;

            case R.id.action_sort_show_all:
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    showAllAssetsInDbV2();
                } else {
                    showAllAssetsInDb();
                }
                return true;

            case R.id.action_sort_by_scanned:
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    shortByScannedV2();
                } else {
                    sortByScanned();
                }
                return true;

            case R.id.action_sort_by_unscanned:
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    shortByunScannedV2();
                } else {
                    sortByUnScanned();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shortByunScannedV2() {

    }

    private void shortByScannedV2() {

    }

    private void sortByScanned() {
        assetList.clear();
        assetList.addAll(db.getAllAssetsByScanned(scanResult));
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
        liveCountAll();
    }

    private void sortByUnScanned() {
        assetList.clear();
        assetList.addAll(db.getAllAssetsByUnscanned());
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
        liveCountAll();
    }

    private void openFilePicker() {
//        Intent intentfile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intentfile.addCategory(Intent.CATEGORY_OPENABLE);
//        intentfile.setType("text/xml");
//        //https://stackoverflow.com/questions/35915602/selecting-a-specific-type-of-file-in-android
//        startActivityForResult(intentfile, PICKFILE_RESULT_CODE);

        if (permissionGranted()) {
            SingleFilePickerDialog singleFilePickerDialog = new SingleFilePickerDialog(this,
                    () -> toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_canceled), 0),
                    files -> {
                        toaster(ScanAssetActivity.this, files[0].getPath(), 0);
                        editor.putString(XML_PATH, files[0].getPath());
                        editor.apply();
                        if (sharedDBVersion.equals(AMEN_MODE)) {
                            deleteAssetDatabaseV2();
                            toaster(this, sharedDBVersion, 0);
                            pullDataAsyncTaskV2 taskv2 = new pullDataAsyncTaskV2();
                            taskv2.execute();
                        } else {
                            pullDataAsyncTask task = new pullDataAsyncTask();
                            deleteAssetDatabase();
                            task.execute();
                        }
                    });
            singleFilePickerDialog.show();
        } else {
            requestPermission();
        }
    }

    private boolean permissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private void refreshAssetList() {
        assetList.addAll(db.getAllAssetsByDept(assetLocation));
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
    }

    private void refreshAssetListV2() {
        assetListV2.addAll(db_v2.getAllAssetsByDept(assetLocation));
        mAdapterV2.notifyDataSetChanged();
        toggleEmptyAssetsV2();
    }

    private void showAllAssetsInDb() {
        assetList.clear();
        assetList.addAll(db.getAllAssets());
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
        liveCountAll();
    }

    private void showAllAssetsInDbV2() {
        assetListV2.clear();
        assetListV2.addAll(db_v2.getAllAssets());
        mAdapterV2.notifyDataSetChanged();
        toggleEmptyAssetsV2();
        liveCountAll();
    }

    private void goToSetting() {
        Intent settingsIntent = new
                Intent(ScanAssetActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void deleteAssetDatabase() {
        Log.d(TAG, "deleteAssetDatabase: true");
        db.dropTable();
        assetList.clear();
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
    }

    private void deleteAssetDatabaseV2() {
        db_v2.dropTable();
        assetListV2.clear();
        mAdapterV2.notifyDataSetChanged();
        toggleEmptyAssetsV2();
    }

    private void goToResult() {
        Intent intent = new
                Intent(ScanAssetActivity.this, ScanResultActivity.class);
        intent.putExtra(DEPT_NAME, assetLocation);
        startActivity(intent);
    }

    /**
     * Inserting new asset in db
     * and refreshing the list
     */
    private void createAsset(String asset) {
        // inserting asset in db and getting
        // newly inserted asset id
        long id = db.insertAsset(asset);

        // get the newly inserted asset from db
        Asset a = db.getAsset(id);

        if (a != null) {
            // adding new asset to array list at 0 position
            assetList.add(0, a);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyAssets();
        }
    }

/*    private void createAssetV2(String asset) {
        long id = db_v2.insertAsset(asset);
        AssetV2 a = db_v2.getAsset(id);
        if (a != null) {
            assetListV2.add(0, a);
            mAdapterV2.notifyDataSetChanged();
            toggleEmptyAssetsV2();
        }
    }*/

    /**
     * Updating asset in db and updating
     * item in the list by its position
     */
    private void updateAsset(String rfid, int position) {
        Asset asset = assetList.get(position);
        // updating asset text
        asset.setTxtRfid(rfid);

        // updating asset in db
        db.updateAsset(asset);

        // refreshing the list
        assetList.set(position, asset);
        mAdapter.notifyItemChanged(position);

        toggleEmptyAssets();
    }

    private void updateAssetV2(String rfid, int position, String area, String nick) {
        AssetV2 assetV2 = assetListV2.get(position);
        assetV2.setTxtRfid(rfid);
        assetV2.setTxtArea(area);
        assetV2.setTxtNick(nick);
        db_v2.updateAsset(assetV2);
        db_v2.updateAreaByRfid(assetV2, rfid);
        db_v2.updateNickByRfid(assetV2, rfid);
        assetListV2.set(position, assetV2);
        mAdapterV2.notifyItemChanged(position);
        toggleEmptyAssetsV2();
    }

    private void updateNickPic(String rfid, int position, String nick) {
        AssetV2 assetV2 = assetListV2.get(position);
        assetV2.setTxtNick(nick);
        db_v2.updateAsset(assetV2);
        db_v2.updateNickByRfid(assetV2, rfid);
        assetListV2.set(position, assetV2);
        mAdapterV2.notifyItemChanged(position);
        toggleEmptyAssetsV2();
    }

    private void updateStatusAsset(int position, String rfid, int unitActual, int unitSelisih,
                                   String deptLob, String locationUpdate, String areaAsset, String timestamp) {
        Asset asset = assetList.get(position);
        // updating asset text
        asset.setTxtStatus(scanResult);
        asset.setIntUnitAktual(unitActual);
        asset.setIntUnitSelisih(unitActual - unitSelisih);
        asset.setTxtDeptLobUpdate(deptLob);
        asset.setTxtLokasiUpdate(locationUpdate);
        asset.setTxtAssetArea(areaAsset);
        asset.setTimestamp(timestamp);

        // updating asset in db
        db.updateStatusByRfid(asset, rfid);

        // refreshing the list
        assetList.set(position, asset);
        mAdapter.notifyItemChanged(position);

        toggleEmptyAssets();
    }

    private void updateStatusAssetV2(int position, String rfid) {
        AssetV2 asset = assetListV2.get(position);
        asset.setTxtStatus(scanResult);
        db_v2.updateStatusByRfid(asset, rfid);
        assetListV2.set(position, asset);
        mAdapterV2.notifyItemChanged(position);
        toggleEmptyAssetsV2();
    }

    private void updateStatusAreaV2(int position, String rfid, String area) {
        AssetV2 asset = assetListV2.get(position);
        asset.setTxtArea(area);
        db_v2.updateAreaByRfid(asset, rfid);
        assetListV2.set(position, asset);
        mAdapterV2.notifyItemChanged(position);
        toggleEmptyAssetsV2();
    }

    /**
     * Deleting aaset from SQLite and removing the
     * item from the list by its position
     */
    private void deleteAsset(int position) {
        // deleting the asset from db
        db.deleteAsset(assetList.get(position));

        // removing the asset from the list
        assetList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyAssets();
    }

/*    private void deleteAssetV2(int position) {
        db_v2.deleteAsset(assetListV2.get(position));
        assetListV2.remove(position);
        mAdapterV2.notifyItemRemoved(position);
        toggleEmptyAssetsV2();
    }*/

    private void deleteTagByItemCodeV2(int position) {
        AssetV2 assetV2 = assetListV2.get(position);
        assetV2.setTxtRfid("");
        db_v2.deleteTagByItemCode(assetV2.getTxtFixedAssetCode());
        mAdapterV2.notifyDataSetChanged();
        toggleEmptyAssetsV2();
    }

    private void deleteTagByItemCode(int position) {
        Asset asset = assetList.get(position);
        asset.setTxtRfid("");
        db.deleteTagByItemCode(asset.getTxtFixedAssetCode());
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
    }

    private void deleteStatusByItemCode(int position) {
        Asset asset = assetList.get(position);
        asset.setTxtStatus("");
        db.deleteStatusByItemCode(asset.getTxtFixedAssetCode());
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
    }

    private void deleteStatusByItemCodeV2(int position) {
        AssetV2 assetV2 = assetListV2.get(position);
        assetV2.setTxtStatus("");
        db_v2.deleteStatusByItemCode(assetV2.getTxtFixedAssetCode());
        mAdapterV2.notifyDataSetChanged();
        toggleEmptyAssetsV2();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{getResources().getString(R.string.msg_edit_tag), getResources().getString(R.string.msg_remove_tag)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.msg_choose_option));
        builder.setItems(colors, (dialog, which) -> {
            if (which == 0) {
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    showAssetDialogV2(assetListV2.get(position), position);
                } else {
                    showAssetDialog(assetList.get(position), position);
                }
            } else {
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    final AssetV2 assetV2 = assetListV2.get(position);
                    assetCode = assetV2.getTxtFixedAssetCode();
                    assetTag = assetV2.getTxtRfid();
                } else {
                    final Asset asset = assetList.get(position);
                    assetCode = asset.getTxtFixedAssetCode();
                    assetTag = asset.getTxtRfid();
                }
                if (assetTag.equals("")) {
                    toaster(this, getResources().getString(R.string.msg_asset_no_tag), 0);
                } else {
                    SimpleDialog.showDialog(this,
                            getResources().getString(R.string.remove_rfid_tag) + "?",
                            getResources().getString(R.string.msg_asset_tag_delete) + " " + assetCode + "!",
                            true,
                            dialogIcon,
                            getResources().getString(R.string.button_yes),
                            getResources().getString(R.string.button_cancel),
                            (DialogInterface dialogInterface, int whichOne) -> {
                                if (whichOne == BUTTON_POSITIVE) {
                                    dialogInterface.cancel();
                                    if (sharedDBVersion.equals(AMEN_MODE)) {
                                        //deleteAssetV2(position);
                                        //delete tag only
                                        deleteTagByItemCodeV2(position);
                                        deleteStatusByItemCodeV2(position);
                                    } else {
                                        deleteTagByItemCode(position);
                                        deleteStatusByItemCode(position);
                                        //deleteAsset(position);
                                    }
                                    liveCountAll();
                                    toaster(this, getResources().getString(R.string.msg_asset_tag_removed) + " " + assetCode, 0);
                                    //progressDialog.dismiss();
                                }
                                dialogInterface.cancel();
                            });
                }
            }
        });
        builder.show();
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a asset.
     * when shouldUpdate=true, it automatically displays old asset and changes the
     * button text to UPDATE
     */
    private void showAssetDialog(final Asset asset, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.asset_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ScanAssetActivity.this);
        alertDialogBuilderUserInput.setView(view);

        //final EditText inputRfidAsset = view.findViewById(R.id.editText_assetRfid);
        final TextInputEditText inputPic = view.findViewById(R.id.textInput_txtNamaPengguna);
        final TextInputEditText inputRfidNumber = view.findViewById(R.id.textInput_rfid_tag_number);
        inputPic.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getResources().getString(R.string.lbl_edit_asset_title));

        sharedTag = preferences.getString(KEY_RFID_TAG, "");
        //Cek apakah aset tidak null
        if (asset != null) {
            //Jika asset null atau "",
            if (!asset.getTxtRfid().equals("")) {
                //Dapatkan no RFID dari database
                inputRfidNumber.setText(asset.getTxtRfid());
                Log.d(TAG, "showAssetDialog: !null " + asset.getTxtRfid());
                Log.d(TAG, "showAssetDialog: " + asset.getTxtNamaPengguna());
            } else {
                Log.d(TAG, "showAssetDialog: null " + sharedTag);
                //Dapatkan no RFID dari sharedTag/RFID tag yang belum terdaftar pada asset
                inputRfidNumber.setText(sharedTag);
            }
            //Dapatkan nama nick PIC dari database. contoh: NDU
            inputPic.setText(asset.getTxtNamaPengguna());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                //Jika user klik Update
                .setPositiveButton(getResources().getString(R.string.update_header),
                        //Munculkan dialog box
                        (dialogBox, id) -> {

                        })
                //Jika user klik Cancel
                .setNegativeButton(getResources().getString(R.string.button_cancel),
                        //Hilangkan dialog box
                        (dialogBox, id) -> dialogBox.cancel());

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(BUTTON_POSITIVE).setOnClickListener(v -> {
            // Show toast message when no text is entered
            //Jika kolom input RFID tag kosong
            if (TextUtils.isEmpty(Objects.requireNonNull(inputRfidNumber.getText()).toString())) {
                //Munculkan peringatan "RFID tag tidak boleh kosong!"
                inputRfidNumber.setError(getResources().getString(R.string.rfid_empty_error_message));
                return;
            } else {
                alertDialog.dismiss();
            }

            //Cek apakah menggunakan AMEN_MODE
            if (sharedDBVersion.equals(AMEN_MODE)) {
                //Jika asset tidak null
                if (asset != null) {
                    //Jika RFID tag yang belum terdaftar tidak ada di database
                    if (!db_v2.checkIsRfidInDB(sharedTag)) {
                        //Update RFID tag menggunakan RFID tag yang belum terdaftar
                        //Update Area
                        //Update PIC nick
                        updateAssetV2(inputRfidNumber.getText().toString(), position, assetArea, Objects.requireNonNull(inputPic.getText()).toString().toUpperCase());
                        toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_tag_pic_updated), 0);
                    } else {
                        //Jika sudah terdaftar, munculkan peringatan "Tag sudah terpakai!"
                        toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_tag_in_use), 0);
                        //Update PIC saja
                        updateNickPic(inputRfidNumber.getText().toString(), position, Objects.requireNonNull(inputPic.getText()).toString().toUpperCase());
                        Log.d(TAG, "showAssetDialog: " + inputPic.getText().toString());
                        toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_pic_updated), 0);
                        alertDialog.dismiss();
                    }
                } else {
                    // create new asset
                    createAsset(inputRfidNumber.getText().toString());
                }
            } else {
                if (asset != null) {
                    if (!db.checkIsRfidInDB(sharedTag)) {
                        // update asset by it's id
                        updateAsset(inputRfidNumber.getText().toString(), position);
                    } else {
                        toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_tag_in_use), 0);
                        alertDialog.dismiss();
                    }
                } else {
                    // create new asset
                    createAsset(inputRfidNumber.getText().toString());
                }
            }

        });
    }

    private void showAssetDialogV2(final AssetV2 assetV2, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.asset_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ScanAssetActivity.this);
        alertDialogBuilderUserInput.setView(view);

        //final EditText inputRfidAsset = view.findViewById(R.id.editText_assetRfid);
        final TextInputEditText inputPic = view.findViewById(R.id.textInput_txtNamaPengguna);
        final TextInputEditText inputRfidNumber = view.findViewById(R.id.textInput_rfid_tag_number);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getResources().getString(R.string.lbl_edit_asset_title));

        sharedTag = preferences.getString(KEY_RFID_TAG, "");
        if (assetV2 != null) {
            if (!assetV2.getTxtRfid().equals("")) {
                //inputRfidAsset.setText(asset.getAsset_rfid());
                inputRfidNumber.setText(assetV2.getTxtRfid());
                Log.d(TAG, "showAssetDialog: !null " + assetV2.getTxtRfid());
                Log.d(TAG, "showAssetDialog: " + assetV2.getTxtNick());
            } else {
                Log.d(TAG, "showAssetDialog: null " + sharedTag);
                //inputRfidAsset.setText(sharedTag);
                inputRfidNumber.setText(sharedTag);
            }
            inputPic.setText(assetV2.getTxtNick());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.btn_update),
                        (dialogBox, id) -> {

                        })
                .setNegativeButton(getResources().getString(R.string.button_cancel),
                        (dialogBox, id) -> dialogBox.cancel());

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(BUTTON_POSITIVE).setOnClickListener(v -> {
            // Show toast message when no text is entered
            if (TextUtils.isEmpty(Objects.requireNonNull(inputRfidNumber.getText()).toString())) {
                inputRfidNumber.setError(getResources().getString(R.string.rfid_empty_error_message));
                return;
            } else {
                alertDialog.dismiss();
            }

            // check if user updating asset
            if (assetV2 != null) {
                if (!db_v2.checkIsRfidInDB(sharedTag)) {
                    // update asset by it's id
                    updateAssetV2(inputRfidNumber.getText().toString(), position, assetArea, Objects.requireNonNull(inputPic.getText()).toString().toUpperCase());
                    toaster(this, getResources().getString(R.string.msg_tag_pic_updated), 0);
                } else {
                    toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_tag_in_use), 0);
                    updateNickPic(inputRfidNumber.getText().toString(), position, Objects.requireNonNull(inputPic.getText()).toString().toUpperCase());
                    alertDialog.dismiss();
                }
                liveCountAll();
            } else {
                // create new asset
                createAsset(inputRfidNumber.getText().toString());
            }
        });
    }

    /**
     * Toggling list and empty assets view
     */
    private void toggleEmptyAssets() {
        // you can check assetsList.size() > 0

        if (db.getAssetsCount() > 0) {
            noAssetView.setVisibility(View.GONE);
        } else {
            noAssetView.setVisibility(View.VISIBLE);
        }
        liveCountAll();
    }

    /**
     * Toggling list and empty assets view
     */
    private void toggleEmptyAssetsV2() {
        // you can check assetsList.size() > 0

        if (db_v2.getAssetsCount() > 0) {
            noAssetView.setVisibility(View.GONE);
        } else {
            noAssetView.setVisibility(View.VISIBLE);
        }
    }

    private final BroadcastReceiver myDataReceiver = new BroadcastReceiver() {

//        private final ProgressDialog dialog = new ProgressDialog(ScanAssetActivity.this);

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.requireNonNull(intent.getAction()).equals(GeneralString.Intent_RFIDSERVICE_CONNECTED)) {
                String PackageName = intent.getStringExtra("PackageName");

                // / make sure this AP does already connect with RFID service (after call RfidManager.InitInstance(this)
                //String ver = "";
                //ver = mRfidManager.GetServiceVersion();
                String api_ver = mRfidManager.GetAPIVersion();
//                tv1.setText(PackageName + "," + ver + " , " + api_ver);

                Toast.makeText(ScanAssetActivity.this, "Intent_RFIDSERVICE_CONNECTED", Toast.LENGTH_SHORT).show();
                startSound();
            } else if (intent.getAction().equals(GeneralString.Intent_RFIDSERVICE_TAG_DATA)) {
                /*s
                 * type :0=Normal scan (Press Trigger Key to receive the data) ; 1=Inventory EPC ; 2=Inventory ECP TID ; 3=Reader tag ; 5=Write tag
                 * response : 0=RESPONSE_OPERATION_SUCCESS ; 1=RESPONSE_OPERATION_FINISH ; 2=RESPONSE_OPERATION_TIMEOUT_FAIL ; 6=RESPONSE_PASSWORD_FAIL ; 7=RESPONSE_OPERATION_FAIL ;251=DEVICE_BUSY
                 * */

                int type = intent.getIntExtra(GeneralString.EXTRA_DATA_TYPE, -1);
                int response = intent.getIntExtra(GeneralString.EXTRA_RESPONSE, -1);
                double data_rssi = intent.getDoubleExtra(GeneralString.EXTRA_DATA_RSSI, 0);

                String PC = intent.getStringExtra(GeneralString.EXTRA_PC);
                String EPC = intent.getStringExtra(GeneralString.EXTRA_EPC);
                String TID = intent.getStringExtra(GeneralString.EXTRA_TID);
                String ReadData = intent.getStringExtra(GeneralString.EXTRA_ReadData);
                int EPC_length = intent.getIntExtra(GeneralString.EXTRA_EPC_LENGTH, 0);
                int TID_length = intent.getIntExtra(GeneralString.EXTRA_TID_LENGTH, 0);
                int ReadData_length = intent.getIntExtra(GeneralString.EXTRA_ReadData_LENGTH, 0);

                String Data = "response = " + response + " , EPC = " + EPC + "\r TID = " + TID;

//                tv1.setText(Data);
//                Log.w(TAG, "++++ [Intent_RFIDSERVICE_TAG_DATA] ++++");
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] type=" + type + ", response=" + response + ", data_rssi=" + data_rssi);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] PC=" + PC);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] EPC=" + EPC);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] EPC_length=" + EPC_length);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] TID=" + TID);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] TID_length=" + TID_length);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] ReadData=" + ReadData);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] ReadData_length=" + ReadData_length);


                //scan get position
                if (sharedDBVersion.equals(AMEN_MODE)) {

                    if (db_v2.checkIsRfidInDB(EPC)) {
//
//                    this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    this.dialog.setMessage("Scanning Asset...");
//                    this.dialog.show();
//                    spinner.setVisibility(View.VISIBLE);
                        Log.d(TAG, "start =============================================== ");
                        Log.d(TAG, "RFID Tag Exist: " + EPC);
                        int tagPos = mAdapterV2.getRfidPosition(EPC);
                        Log.d(TAG, "Asset Desc bfr: " + mAdapterV2.getAssetDesc(tagPos));
                        Log.d(TAG, "Asset Status bfr: " + mAdapterV2.getAssetStatus(tagPos));
                        Log.d(TAG, "Rfid Tag Position: " + tagPos);

                        updateStatusAssetV2(tagPos, EPC);
                        updateStatusAreaV2(tagPos, EPC, assetArea);
                        recyclerView.scrollToPosition(tagPos);

                        Log.d(TAG, "Asset Desc: " + mAdapterV2.getAssetDesc(tagPos));
                        Log.d(TAG, "Asset Status: " + mAdapterV2.getAssetStatus(tagPos));
                        Log.d(TAG, "end =============================================== ");
                    } else {
                        // Inserting record to sharedPref
                        editor.putString(KEY_RFID_TAG, EPC);
                        editor.apply();
                        Log.d(TAG, "onReceive: Tag " + EPC + " unregistered, saved to sharedPref");

                        //createAsset(EPC);
                    }
                    mAdapterV2.notifyDataSetChanged();
                    toggleEmptyAssetsV2();
                    liveCountAll();
                } else {
                    if (db.checkIsRfidInDB(EPC)) {
//
//                    this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    this.dialog.setMessage("Scanning Asset...");
//                    this.dialog.show();
//                    spinner.setVisibility(View.VISIBLE);
                        Log.d(TAG, "start =============================================== ");
                        Log.d(TAG, "RFID Tag Exist: " + EPC);
                        int tagPos = mAdapter.getRfidPosition(EPC);
                        Log.d(TAG, "Asset Desc bfr: " + mAdapter.getAssetDesc(tagPos));
                        Log.d(TAG, "Asset Status bfr: " + mAdapter.getAssetStatus(tagPos));
                        Log.d(TAG, "Rfid Tag Position: " + tagPos);

                        updateStatusAsset(tagPos, EPC, 1, 1, dept, div, assetArea, formattedDate);
                        recyclerView.scrollToPosition(tagPos);
                        assetList.clear();
                        assetList.addAll(db.getAllAssetsByUnscanned());

                        Log.d(TAG, "Asset Desc: " + mAdapter.getAssetDesc(tagPos));
                        Log.d(TAG, "Asset Status: " + mAdapter.getAssetStatus(tagPos));
                        Log.d(TAG, "Asset Dept: " + dept);
                        Log.d(TAG, "Asset Div: " + div);
                        Log.d(TAG, "Asset Area: " + assetArea);
                        Log.d(TAG, "Timestamp: " + formattedDate);
                        Log.d(TAG, "end =============================================== ");
                    } else {
                        // Inserting record to sharedPref
                        editor.putString(KEY_RFID_TAG, EPC);
                        editor.apply();
                        Log.d(TAG, "onReceive: Tag " + EPC + " unregistered, saved to sharedPref");

                        //createAsset(EPC);
                    }
                    mAdapter.notifyDataSetChanged();
                    toggleEmptyAssets();
                }
//                if (this.dialog.isShowing()) {
//                    this.dialog.dismiss();
//                }
//                spinner.setVisibility(View.GONE);
            }
            //Intent_RFIDSERVICE_EVENT
            else if (intent.getAction().equals(GeneralString.Intent_RFIDSERVICE_EVENT)) {
                int event = intent.getIntExtra(GeneralString.EXTRA_EVENT_MASK, -1);
                Log.d(TAG, "[Intent_RFIDSERVICE_EVENT] DeviceEvent=" + event);
                if (event == DeviceEvent.LowBattery.getValue()) {
                    Log.i(GeneralString.TAG, "LowBattery ");
                } else if (event == DeviceEvent.PowerSavingMode.getValue()) {
                    Log.i(GeneralString.TAG, "PowerSavingMode ");
                } else if (event == DeviceEvent.OverTemperature.getValue()) {
                    Log.i(GeneralString.TAG, "OverTemperature ");
                } else if (event == DeviceEvent.ScannerFailure.getValue()) {
                    Log.i(GeneralString.TAG, "ScannerFailure ");
                }

            } else if (intent.getAction().equals(GeneralString.Intent_FWUpdate_ErrorMessage)) {
                String mse;
                mse = intent.getStringExtra(GeneralString.FWUpdate_ErrorMessage);
                if (mse != null) {
                    Log.d(TAG, mse);
                    Toast.makeText(ScanAssetActivity.this, mse, Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "Intent_FWUpdate_ErrorMessage");
            } else if (intent.getAction().equals(GeneralString.Intent_FWUpdate_Percent)) {
                int i = intent.getIntExtra(GeneralString.FWUpdate_Percent, 0);
                if (i >= 0) {
//                    tv1.setText(Integer.toString(i));
                    toaster(ScanAssetActivity.this, Integer.toString(i), 0);
                }
                Log.d(TAG, "Intent_FWUpdate_Percent");
            } else if (intent.getAction().equals(GeneralString.Intent_FWUpdate_Finish)) {
                Log.d(TAG, "Intent_FWUpdate_Finish");
                Toast.makeText(ScanAssetActivity.this, "Intent_FWUpdate_Finish", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void startSound() {
        final MediaPlayer soundBeeps = MediaPlayer.create(this, R.raw.five_beeps);
        soundBeeps.start();
    }

    /*https://stackoverflow.com/a/8018905/7772358*/
    public void displayExceptionMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    protected String getNodeValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Node node = nodeList.item(0);
        if (node != null) {
            if (node.hasChildNodes()) {
                Node child = node.getFirstChild();
                while (child != null) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myDataReceiver);
        mRfidManager.Release();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * sub-class of AsyncTask
     */
    @SuppressLint("StaticFieldLeak")
    protected class pullDataAsyncTask extends AsyncTask<Context, Integer, String> {
        /*https://stackoverflow.com/questions/6450275/android-how-to-work-with-asynctasks-progressdialog*/
        private final ProgressDialog dialog = new ProgressDialog(ScanAssetActivity.this);
        private int totalAsset = 0;

        // -- run intensive processes here
        // -- notice that the datatype of the first param in the class definition matches the param passed to this
        // method
        // -- and that the datatype of the last param in the class definition matches the return type of this method
        @Override
        protected String doInBackground(Context... params) {
            // -- on every iteration
            // -- runs a while loop that causes the thread to sleep for 50 milliseconds
            // -- publishes the progress - calls the onProgressUpdate handler defined below
            // -- and increments the counter variable i by one

            /*https://www.tutlane.com/tutorial/android/android-xml-parsing-using-sax-parser*/
            /*https://stackoverflow.com/questions/15967896/how-to-parse-xml-file-from-sdcard-in-android*/
            try {
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") ArrayList<HashMap<String, String>> userList = new ArrayList<>();
                /*Input from android asset folder*/
                //InputStream istream = getAssets().open("userdetails.xml");

                /*Input from mnt/sdcard*/
                String pathFile = preferences.getString(XML_PATH, "mnt/sdcard/Asset/Asset.xml");
                //File file = new File("mnt/sdcard/Asset/AssetUpdate.xml");
                File file = new File(Objects.requireNonNull(pathFile));
                InputStream inputStreamSd = new FileInputStream(file.getPath());
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputStreamSd);
                NodeList nList = doc.getElementsByTagName("asset");
                HashMap<String, String> asset;
                totalAsset = nList.getLength();

                for (int i = 0; i < nList.getLength(); i++) {
                    if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                        asset = new HashMap<>();
                        Element elm = (Element) nList.item(i);
                        asset.put(COLUMN_FIXED_ASSET_CODE, getNodeValue(COLUMN_FIXED_ASSET_CODE, elm));
                        asset.put(COLUMN_NAMA_ASSET, getNodeValue(COLUMN_NAMA_ASSET, elm));
                        asset.put(COLUMN_UNIT_SISTEM, getNodeValue(COLUMN_UNIT_SISTEM, elm));
                        asset.put(COLUMN_TANGGAL_BELI, getNodeValue(COLUMN_TANGGAL_BELI, elm));
                        asset.put(COLUMN_NILAI_BELI, getNodeValue(COLUMN_NILAI_BELI, elm));
                        asset.put(COLUMN_UNIT_AKTUAL, getNodeValue(COLUMN_UNIT_AKTUAL, elm));
                        asset.put(COLUMN_UNIT_SELISIH, getNodeValue(COLUMN_UNIT_SELISIH, elm));
                        asset.put(COLUMN_STATUS, getNodeValue(COLUMN_STATUS, elm));
                        asset.put(COLUMN_DEPT_LOB, getNodeValue(COLUMN_DEPT_LOB, elm));
                        asset.put(COLUMN_DEPT_LOB_UPDATE, getNodeValue(COLUMN_DEPT_LOB_UPDATE, elm));
                        asset.put(COLUMN_LOKASI_ASSET_BY_SYSTEM, getNodeValue(COLUMN_LOKASI_ASSET_BY_SYSTEM, elm));
                        asset.put(COLUMN_LOKASI_UPDATE, getNodeValue(COLUMN_LOKASI_UPDATE, elm));
                        asset.put(COLUMN_NAMA_PENGGUNA, getNodeValue(COLUMN_NAMA_PENGGUNA, elm));
                        asset.put(COLUMN_NAMA_PENGGUNA_UPDATE, getNodeValue(COLUMN_NAMA_PENGGUNA_UPDATE, elm));
                        asset.put(COLUMN_NAMA_PENANGGUNG_JAWAB, getNodeValue(COLUMN_NAMA_PENANGGUNG_JAWAB, elm));
                        asset.put(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE, getNodeValue(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE, elm));
                        asset.put(COLUMN_KETERANGAN, getNodeValue(COLUMN_KETERANGAN, elm));
                        asset.put(COLUMN_RFID, getNodeValue(COLUMN_RFID, elm));
                        asset.put(COLUMN_IMAGE_LINK, getNodeValue(COLUMN_IMAGE_LINK, elm));
                        asset.put(COLUMN_ASSET_AREA, getNodeValue(COLUMN_ASSET_AREA, elm));
                        asset.put(COLUMN_TIMESTAMP, getNodeValue(COLUMN_TIMESTAMP, elm));
                        userList.add(asset);
                        //scan get position
                        if (db.checkIsItemCodeInDb(asset.put(COLUMN_FIXED_ASSET_CODE, getNodeValue(COLUMN_FIXED_ASSET_CODE, elm)))) {
                            Log.d(TAG, "onReceive: Exist");
                            Log.d(TAG, "loadAssetList: ");
                        } else {
                            // Inserting record
                            Log.d(TAG, "onReceive: Data No Exist" + i);
                            db.inputDataFromDom(Objects.requireNonNull(asset));
                            publishProgress(i);
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                return "No File";
                //displayExceptionMessage(e.getMessage());
            }
            return "COMPLETE!";
        }

        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute()");
            super.onPreExecute();
            this.dialog.setMessage(getResources().getString(R.string.importing_data_asset));
            this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.dialog.setCancelable(false);
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.show();
        }

        // -- called from the publish progress
        // -- notice that the datatype of the second param gets passed to this method
        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                double valuesDb = Double.parseDouble(String.valueOf((values[0])));
                double totalAssetDb = Double.parseDouble(String.valueOf(totalAsset));
                double percenTage = (valuesDb / totalAssetDb) * 100;
                Log.d(TAG, "onCreate: " + percenTage);
                BigDecimal bd = new BigDecimal(percenTage).setScale(2, RoundingMode.HALF_EVEN);
                bd.doubleValue();
//                this.dialog.setMessage("Importing data asset " + (values[0]) + "/" + totalAsset + " (" + bd + "%)");
                this.dialog.setMessage(getResources().getString(R.string.importing_data_asset) + ", " + getResources().getString(R.string.msg_please_wait));
                this.dialog.setMax(totalAsset);
                this.dialog.setProgress((values[0]));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // -- called if the cancel button is pressed
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "onCancelled()");
            this.dialog.setMessage(getResources().getString(R.string.msg_canceled));
        }

        // -- called as soon as doInBackground method completes
        // -- notice that the third param gets passed to this method
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "onPostExecute(): " + result);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result.equals("COMPLETE!")) {
                this.dialog.setMessage(result);
                toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_import_complete), 0);
            } else if (result.equals("No File")) {
                toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_no_asset_xml_in_directory), 0);
            } else {
                toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_import_failed), 0);
            }
            refreshAssetList();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected class pullDataAsyncTaskV2 extends AsyncTask<Context, Integer, String> {
        /*https://stackoverflow.com/questions/6450275/android-how-to-work-with-asynctasks-progressdialog*/
        private final ProgressDialog dialog = new ProgressDialog(ScanAssetActivity.this);
        private int totalAsset = 0;

        // -- run intensive processes here
        // -- notice that the datatype of the first param in the class definition matches the param passed to this
        // method
        // -- and that the datatype of the last param in the class definition matches the return type of this method
        @Override
        protected String doInBackground(Context... params) {
            // -- on every iteration
            // -- runs a while loop that causes the thread to sleep for 50 milliseconds
            // -- publishes the progress - calls the onProgressUpdate handler defined below
            // -- and increments the counter variable i by one

            /*https://www.tutlane.com/tutorial/android/android-xml-parsing-using-sax-parser*/
            /*https://stackoverflow.com/questions/15967896/how-to-parse-xml-file-from-sdcard-in-android*/
            try {
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") ArrayList<HashMap<String, String>> userList = new ArrayList<>();
                /*Input from android asset folder*/
                //InputStream istream = getAssets().open("userdetails.xml");

                /*Input from mnt/sdcard*/
                String pathFile = preferences.getString(XML_PATH, "mnt/sdcard/Asset/Asset.xml");
                //File file = new File("mnt/sdcard/Asset/AssetUpdate.xml");
                File file = new File(Objects.requireNonNull(pathFile));
                InputStream inputStreamSd = new FileInputStream(file.getPath());
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputStreamSd);
                NodeList nList = doc.getElementsByTagName("asset");
                HashMap<String, String> asset;
                totalAsset = nList.getLength();

                for (int i = 0; i < nList.getLength(); i++) {
                    if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                        asset = new HashMap<>();
                        Element elm = (Element) nList.item(i);
                        asset.put(COLUMN_TXTFIXEDASSETCODE, getNodeValue(COLUMN_TXTFIXEDASSETCODE, elm));
                        asset.put(COLUMN_TXTASSETDESCRIPTION, getNodeValue(COLUMN_TXTASSETDESCRIPTION, elm));
                        asset.put(COLUMN_TXTASSETCATEGORY, getNodeValue(COLUMN_TXTASSETCATEGORY, elm));
                        asset.put(COLUMN_TXTSUPERVISORID, getNodeValue(COLUMN_TXTSUPERVISORID, elm));
                        asset.put(COLUMN_DECACQUISITION, getNodeValue(COLUMN_DECACQUISITION, elm));
                        asset.put(COLUMN_TXTNAME, getNodeValue(COLUMN_TXTNAME, elm));
                        asset.put(COLUMN_TXTNICK, getNodeValue(COLUMN_TXTNICK, elm));
                        asset.put(COLUMN_TXTEMAIL, getNodeValue(COLUMN_TXTEMAIL, elm));
                        asset.put(COLUMN_TXTPENGGUNAID, getNodeValue(COLUMN_TXTPENGGUNAID, elm));
                        asset.put(COLUMN_TXTLOKASIPENGGUNA, getNodeValue(COLUMN_TXTLOKASIPENGGUNA, elm));
                        asset.put(COLUMN_TXTLOBPENGGUNA, getNodeValue(COLUMN_TXTLOBPENGGUNA, elm));
                        asset.put(COLUMN_TXTAREA, getNodeValue(COLUMN_TXTAREA, elm));
                        asset.put(COLUMN_TXTRFID, getNodeValue(COLUMN_TXTRFID, elm));
                        asset.put(COLUMN_TXTSTATUS, getNodeValue(COLUMN_TXTSTATUS, elm));
                        asset.put(COLUMN_TXTIMGLINK, getNodeValue(COLUMN_TXTIMGLINK, elm));
                        asset.put(COLUMN_TXTNOTES, getNodeValue(COLUMN_TXTNOTES, elm));

                        userList.add(asset);
                        //scan get position
                        if (db_v2.checkIsItemCodeInDb(asset.put(COLUMN_TXTFIXEDASSETCODE, getNodeValue(COLUMN_TXTFIXEDASSETCODE, elm)))) {
                            Log.d(TAG, "onReceive: Exist");
                            Log.d(TAG, "loadAssetList: ");
                        } else {
                            // Inserting record
                            Log.d(TAG, "onReceive: Data No Exist" + i);
                            db_v2.inputDataFromDom(Objects.requireNonNull(asset));
                            publishProgress(i);
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                return "No File";
                //displayExceptionMessage(e.getMessage());
            }
            return "COMPLETE!";
        }

        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute()");
            super.onPreExecute();
            this.dialog.setMessage(getResources().getString(R.string.importing_data_asset));
            this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.dialog.setCancelable(false);
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.show();
        }

        // -- called from the publish progress
        // -- notice that the datatype of the second param gets passed to this method
        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                double valuesDb = Double.parseDouble(String.valueOf((values[0])));
                double totalAssetDb = Double.parseDouble(String.valueOf(totalAsset));
                double percenTage = (valuesDb / totalAssetDb) * 100;
                Log.d(TAG, "onCreate: " + percenTage);
                BigDecimal bd = new BigDecimal(percenTage).setScale(2, RoundingMode.HALF_EVEN);
                bd.doubleValue();
//                this.dialog.setMessage("Importing data asset " + (values[0]) + "/" + totalAsset + " (" + bd + "%)");
                this.dialog.setMessage(getResources().getString(R.string.importing_data_asset) + ", " + getResources().getString(R.string.msg_please_wait));
                this.dialog.setMax(totalAsset);
                this.dialog.setProgress((values[0]));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // -- called if the cancel button is pressed
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "onCancelled()");
            this.dialog.setMessage("Cancelled!");
        }

        // -- called as soon as doInBackground method completes
        // -- notice that the third param gets passed to this method
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "onPostExecute(): " + result);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result.equals("COMPLETE!")) {
                this.dialog.setMessage(result);
                toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_import_complete), 0);
            } else if (result.equals("No File")) {
                toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_import_failed), 0);
            } else {
                toaster(ScanAssetActivity.this, getResources().getString(R.string.msg_import_failed), 0);
            }
            refreshAssetListV2();
        }
    }
}