package com.ndu.assetmanagementsystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipherlab.rfid.DeviceEvent;
import com.cipherlab.rfid.GeneralString;
import com.cipherlab.rfidapi.RfidManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper;
import com.ndu.assetmanagementsystem.sqlite.database.model.Asset;
import com.ndu.assetmanagementsystem.sqlite.utils.MyDividerItemDecoration;
import com.ndu.assetmanagementsystem.sqlite.utils.RecyclerTouchListener;
import com.ndu.assetmanagementsystem.sqlite.view.AssetsAdapter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import au.com.bytecode.opencsv.CSVWriter;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.ndu.assetmanagementsystem.MainActivity.DEPT_NAME;
import static com.ndu.assetmanagementsystem.NandurLibs.nduDialog;
import static com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper.DATABASE_NAME;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.ASSET_EXIST;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_CODE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_DESC;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_LOCATION;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_PIC;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_ASSET_RFID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.TABLE_NAME;

public class ScanAssetActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "rfid";
    private AssetsAdapter mAdapter;
    private List<Asset> assetList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noAssetView;

    private DatabaseHelper db;
    //RFID
    RfidManager mRfidManager = null;
    HashMap<String, String> asset = new HashMap<>();
    private String assetLocation;
    private String rfid;
    private int position;
    private File file;
    private Button buttResult;
    private Drawable dialogIcon;
    private Button buttAssetMap;
//    private ProgressBar spinner;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_asset);

        //[START Initialize]

        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        noAssetView = findViewById(R.id.empty_assets_view);
        buttResult = findViewById(R.id.button_result);
        buttAssetMap = findViewById(R.id.button_asset_map);
        dialogIcon = getResources().getDrawable(R.drawable.ic_info_outline_black_24dp);
//        spinner = (ProgressBar) findViewById(R.id.progressBar);
        db = new DatabaseHelper(this);

        toolbar.setTitle(R.string.assets_list);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        /*OnClick Handling*/
        toolbar.setNavigationOnClickListener(view -> finish());
        buttResult.setOnClickListener(view -> goToResult());
        buttAssetMap.setOnClickListener(view -> startSound());

        /*Receive data from MainActivity*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String dept = (String) bundle.get(DEPT_NAME);
            assetLocation = "%" + dept;
            toolbar.setTitle(getResources().getString(R.string.assets_list) + " " + dept);
            Log.d(TAG, "onCreate: " + assetLocation);
        }

        /*Storage permission*/
        runDexter();

        /*Create Asset table in Asset.db*/
        try {
            db.createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Add all assets in Asset.db to recyclerView*/
        assetList.addAll(db.getAllAssetsByDept(assetLocation));
        mAdapter = new AssetsAdapter(this, assetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        toggleEmptyAssets();

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

        /*
          On long press on RecyclerView item, open alert dialog
          with options to choose
          Edit and Delete
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void runDexter() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(ScanAssetActivity.this, "Storage Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScanAssetActivity.this, "Storage Denied", Toast.LENGTH_SHORT).show();
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
        mAdapter.filter(query, db, assetLocation);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.filter(query, db, assetLocation);
        return false;
    }

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

            case R.id.action_delete_database:
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
                                deleteAssetDatabase();
                            }
                            dialog.cancel();
                        });
                return true;

            case R.id.action_pull_assets:
                //https://stackoverflow.com/questions/23408756/create-a-general-class-for-custom-dialog-in-java-android
                nduDialog(this,
                        getResources().getString(R.string.action_pull_assets) + "?",
                        "Ini akan merefresh list dan semua data scan yang belum terexport akan hilang!",
                        true,
                        dialogIcon,
                        "Yes",
                        "Cancel",
                        (DialogInterface dialog, int which) -> {
                            if (which == BUTTON_POSITIVE) {
                                dialog.cancel();

                                pullDataAsyncTask task = new pullDataAsyncTask();
                                task.execute();
                                //progressDialog.dismiss();
                            }
                            dialog.cancel();
                        });
                return true;

            case R.id.action_sort_show_all:
                showAllAssetsInDb();
                return true;

            case R.id.action_export_csv:
                ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
                task.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshAssetList() {
        assetList.addAll(db.getAllAssetsByDept(assetLocation));
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
    }

    private void showAllAssetsInDb() {
        assetList.clear();
        assetList.addAll(db.getAllAssets());
        mAdapter.notifyDataSetChanged();
        toggleEmptyAssets();
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

    /**
     * Updating asset in db and updating
     * item in the list by its position
     */
    private void updateAsset(String rfid, int position) {
        Asset a = assetList.get(position);
        // updating asset text
        a.setAsset_rfid(rfid);

        // updating asset in db
        db.updateAsset(a);

        // refreshing the list
        assetList.set(position, a);
        mAdapter.notifyItemChanged(position);

        toggleEmptyAssets();
    }

    private void updateStatusAsset(int position, String rfid) {
        Asset asset = assetList.get(position);
        // updating asset text
        asset.setAsset_status(ASSET_EXIST);

        // updating asset in db
        db.updateStatusByRfid(asset, rfid);

        // refreshing the list
        assetList.set(position, asset);
        mAdapter.notifyItemChanged(position);

        toggleEmptyAssets();
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

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, (dialog, which) -> {
            if (which == 0) {
                showAssetDialog(true, assetList.get(position), position);
            } else {
                deleteAsset(position);
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
    private void showAssetDialog(final boolean shouldUpdate, final Asset asset, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.asset_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ScanAssetActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputRfidAsset = view.findViewById(R.id.assetRfid);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_asset_title) : getString(R.string.lbl_edit_asset_title));

        if (shouldUpdate && asset != null) {
            inputRfidAsset.setText(asset.getAsset_rfid());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", (dialogBox, id) -> {

                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(BUTTON_POSITIVE).setOnClickListener(v -> {
            // Show toast message when no text is entered
            if (TextUtils.isEmpty(inputRfidAsset.getText().toString())) {
                Toast.makeText(ScanAssetActivity.this, "Enter Rfid Tag Number!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                alertDialog.dismiss();
            }

            // check if user updating asset
            if (shouldUpdate && asset != null) {
                // update asset by it's id
                updateAsset(inputRfidAsset.getText().toString(), position);
            } else {
                // create new asset
                createAsset(inputRfidAsset.getText().toString());
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
    }

    private final BroadcastReceiver myDataReceiver = new BroadcastReceiver() {

//        private final ProgressDialog dialog = new ProgressDialog(ScanAssetActivity.this);

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.requireNonNull(intent.getAction()).equals(GeneralString.Intent_RFIDSERVICE_CONNECTED)) {
                String PackageName = intent.getStringExtra("PackageName");

                // / make sure this AP does already connect with RFID service (after call RfidManager.InitInstance(this)
                String ver = "";
                ver = mRfidManager.GetServiceVersion();
                String api_ver = mRfidManager.GetAPIVersion();
//                tv1.setText(PackageName + "," + ver + " , " + api_ver);

                Toast.makeText(ScanAssetActivity.this, "Intent_RFIDSERVICE_CONNECTED", Toast.LENGTH_SHORT).show();
                startSound();
            } else if (intent.getAction().equals(GeneralString.Intent_RFIDSERVICE_TAG_DATA)) {
                /*
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

                    updateStatusAsset(tagPos, EPC);
                    recyclerView.scrollToPosition(tagPos);

                    Log.d(TAG, "Asset Desc: " + mAdapter.getAssetDesc(tagPos));
                    Log.d(TAG, "Asset Status: " + mAdapter.getAssetStatus(tagPos));
                    Log.d(TAG, "end =============================================== ");
                } else {
                    // Inserting record
                    Log.d(TAG, "onReceive: Data No Exist");
                    //createAsset(EPC);
                }
                mAdapter.notifyDataSetChanged();
                toggleEmptyAssets();
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
                String mse = "";
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
        // TODO Auto-generated method stub
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
                ArrayList<HashMap<String, String>> userList = new ArrayList<>();
                /*Input from android asset folder*/
                //InputStream istream = getAssets().open("userdetails.xml");

                /*Input from mnt/sdcard*/
                File file = new File("mnt/sdcard/Asset/AssetUpdate.xml");
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
                        asset.put(COLUMN_ASSET_CODE, getNodeValue(COLUMN_ASSET_CODE, elm));
                        asset.put(COLUMN_ASSET_RFID, getNodeValue(COLUMN_ASSET_RFID, elm));
                        asset.put(COLUMN_ASSET_DESC, getNodeValue(COLUMN_ASSET_DESC, elm));
                        asset.put(COLUMN_ASSET_PIC, getNodeValue(COLUMN_ASSET_PIC, elm));
                        asset.put(COLUMN_ASSET_LOCATION, getNodeValue(COLUMN_ASSET_LOCATION, elm));
                        userList.add(asset);
                        //scan get position
                        if (db.checkIsItemCodeInDb(asset.put(COLUMN_ASSET_CODE, getNodeValue(COLUMN_ASSET_CODE, elm)))) {
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
                displayExceptionMessage(e.getMessage());
            }
            return "COMPLETE!";
        }

        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() {
            Log.i("makemachine", "onPreExecute()");
            super.onPreExecute();
            this.dialog.setMessage("Importing data asset...");
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
                percenTage = bd.doubleValue();
//                this.dialog.setMessage("Importing data asset " + (values[0]) + "/" + totalAsset + " (" + bd + "%)");
                this.dialog.setMessage("Importing data asset, Please wait!");
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
            Log.i("makemachine", "onCancelled()");
            this.dialog.setMessage("Cancelled!");
        }

        // -- called as soon as doInBackground method completes
        // -- notice that the third param gets passed to this method
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("makemachine", "onPostExecute(): " + result);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result.equals("COMPLETE!")) {
                this.dialog.setMessage(result);
                Toast.makeText(ScanAssetActivity.this, "Import succeed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ScanAssetActivity.this, "Import failed", Toast.LENGTH_SHORT).show();
            }
            refreshAssetList();
        }
    }

    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(ScanAssetActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args) {

            File dbFile = getDatabasePath(DATABASE_NAME);
            //AABDatabaseManager dbhelper = new AABDatabaseManager(getApplicationContext());
            DatabaseHelper db = new DatabaseHelper(ScanAssetActivity.this);
            System.out.println(dbFile);  // displays the data base path in your logcat

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "Asset.csv");
            try {
                if (file.createNewFile()) {
                    System.out.println("File is created!");
                    System.out.println("myfile.csv " + file.getAbsolutePath());
                } else {
                    System.out.println("File already exists.");
                }
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                //SQLiteDatabase db = dbhelper.getWritableDatabase();
                Cursor curCSV = db.getReadableDatabase().rawQuery("select * from " + TABLE_NAME, null);
                csvWrite.writeNext(curCSV.getColumnNames());
                while (curCSV.moveToNext()) {
                    String[] arrStr = {
                            curCSV.getString(0), curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(3), curCSV.getString(4),
                            curCSV.getString(5), curCSV.getString(6)};
                    /*curCSV.getString(3),curCSV.getString(4)};*/
                    csvWrite.writeNext(arrStr);
                }
                csvWrite.close();
                curCSV.close();
        /*String data="";
        data=readSavedData();
        data= data.replace(",", ";");
        writeData(data);*/
                return true;
            } catch (SQLException | IOException sqlEx) {
                Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(ScanAssetActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();
                CSVToExcelConverter task = new CSVToExcelConverter();
                task.execute();
            } else {
                Toast.makeText(ScanAssetActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    CSV to XLS
    public class CSVToExcelConverter extends AsyncTask<String, Void, Boolean> {


        private final ProgressDialog dialog = new ProgressDialog(ScanAssetActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting to excel...");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ArrayList arList = null;
            ArrayList al = null;

            //File dbFile= new File(getDatabasePath("database_name").toString());
            File dbFile = getDatabasePath(DATABASE_NAME);
            String yes = dbFile.getAbsolutePath();

            String inFilePath = Environment.getExternalStorageDirectory().toString() + "/Asset.csv";
            String outFilePath = Environment.getExternalStorageDirectory().toString() + "/Asset.xls";
            String thisLine;
            int count = 0;

            try {

                FileInputStream fis = new FileInputStream(inFilePath);
                DataInputStream myInput = new DataInputStream(fis);
                int i = 0;
                arList = new ArrayList();
                while ((thisLine = myInput.readLine()) != null) {
                    al = new ArrayList();
                    String[] strar = thisLine.split(",");
                    for (int j = 0; j < strar.length; j++) {
                        al.add(strar[j]);
                    }
                    arList.add(al);
                    System.out.println();
                    i++;
                }
            } catch (Exception e) {
                System.out.println("shit");
            }

            try {
                HSSFWorkbook hwb = new HSSFWorkbook();
                HSSFSheet sheet = hwb.createSheet(TABLE_NAME);
                for (int k = 0; k < arList.size(); k++) {
                    ArrayList ardata = (ArrayList) arList.get(k);
                    HSSFRow row = sheet.createRow((short) 0 + k);
                    for (int p = 0; p < ardata.size(); p++) {
                        HSSFCell cell = row.createCell((short) p);
                        String data = ardata.get(p).toString();
                        if (data.startsWith("=")) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            data = data.replaceAll("\"", "");
                            data = data.replaceAll("=", "");
                            cell.setCellValue(data);
                        } else if (data.startsWith("\"")) {
                            data = data.replaceAll("\"", "");
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(data);
                        } else {
                            data = data.replaceAll("\"", "");
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(data);
                        }
                        //*/
                        // cell.setCellValue(ardata.get(p).toString());
                    }
                    System.out.println();
                }
                FileOutputStream fileOut = new FileOutputStream(outFilePath);
                hwb.write(fileOut);
                fileOut.close();
                System.out.println("Your excel file has been generated");
            } catch (Exception ex) {
                ex.printStackTrace();
            } //menu method ends
            return true;
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(ScanAssetActivity.this, "file is built!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ScanAssetActivity.this, "file fail to build", Toast.LENGTH_SHORT).show();
            }
        }
    }
}