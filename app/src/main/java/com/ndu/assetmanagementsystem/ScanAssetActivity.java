package com.ndu.assetmanagementsystem;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipherlab.rfid.DeviceEvent;
import com.cipherlab.rfid.GeneralString;
import com.cipherlab.rfidapi.RfidManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
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
//      case R.id.action_text_to_qr:
//        goToShareToQR();
//        return true;
            case R.id.action_delete_database:
                deleteAssetDatabase();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_asset);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assetLocation = "%General";

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noAssetView = findViewById(R.id.empty_assets_view);

        db = new DatabaseHelper(this);


        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(ScanAssetActivity.this, "Storage Granted", Toast.LENGTH_SHORT).show();
                        //loadAssetList();
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            loadAssetList();

            assetList.addAll(db.getAllAssets());
            mAdapter.notifyDataSetChanged();
            toggleEmptyAssets();
            //showAssetDialog(false, null, -1);
        });

        //RFID
        try {
            db.createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assetList.addAll(db.getAllAssets());
        mRfidManager = RfidManager.InitInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(GeneralString.Intent_RFIDSERVICE_CONNECTED);
        filter.addAction(GeneralString.Intent_RFIDSERVICE_TAG_DATA);
        filter.addAction(GeneralString.Intent_RFIDSERVICE_EVENT);
        filter.addAction(GeneralString.Intent_FWUpdate_ErrorMessage);
        filter.addAction(GeneralString.Intent_FWUpdate_Percent);
        filter.addAction(GeneralString.Intent_FWUpdate_Finish);
        registerReceiver(myDataReceiver, filter);

        mAdapter = new AssetsAdapter(this, assetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyAssets();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
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

    private void updateStatusAsset(String assetStatus, int position, String rfid) {
        Asset asset = assetList.get(position);
        // updating asset text
        asset.setAsset_status(assetStatus);

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
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showAssetDialog(true, assetList.get(position), position);
                } else {
                    deleteAsset(position);
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
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Log.d(TAG, "start =============================================== ");
                    Log.d(TAG, "RFID Tag Exist: " + EPC);
                    int tagPos = mAdapter.getRfidPosition(EPC);
                    Log.d(TAG, "Asset Desc bfr: " + mAdapter.getAssetDesc(tagPos));
                    Log.d(TAG, "Asset Status bfr: " + mAdapter.getAssetStatus(tagPos));
                    Log.d(TAG, "Rfid Tag Position: " + tagPos);

                    updateStatusAsset("Asset Ada", tagPos, EPC);
                    recyclerView.scrollToPosition(tagPos);
                    Log.d(TAG, "Asset Desc: " + mAdapter.getAssetDesc(tagPos));
                    Log.d(TAG, "Asset Status: " + mAdapter.getAssetStatus(tagPos));
                    Log.d(TAG, "end =============================================== ");
//                    View v = Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(tagPos);
//                    TextView tv = Objects.requireNonNull(v).findViewById(R.id.assetDesc);
//                    String assetDesc = tv.getText().toString();
//                    Log.d(TAG, "Asset Desc: " + title);

                } else {
                    // Inserting record
                    Log.d(TAG, "onReceive: Data No Exist");
                    //createAsset(EPC);
                }
                mAdapter.notifyDataSetChanged();
                toggleEmptyAssets();
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

    private void loadAssetList() {
        /*https://www.tutlane.com/tutorial/android/android-xml-parsing-using-sax-parser*/
        /*https://stackoverflow.com/questions/15967896/how-to-parse-xml-file-from-sdcard-in-android*/
        try {
            ArrayList<HashMap<String, String>> userList = new ArrayList<>();
//            InputStream istream = getAssets().open("userdetails.xml");
            File file = new File("mnt/sdcard/Asset/AssetUpdate.xml");
            InputStream inputStreamSd = new FileInputStream(file.getPath());
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStreamSd);
            NodeList nList = doc.getElementsByTagName("asset");
            HashMap<String, String> user;
            for (int i = 0; i < nList.getLength(); i++) {
                if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    user = new HashMap<>();
                    Element elm = (Element) nList.item(i);
                    user.put(Asset.COLUMN_ASSET_CODE, getNodeValue(Asset.COLUMN_ASSET_CODE, elm));
                    user.put(Asset.COLUMN_ASSET_RFID, getNodeValue(Asset.COLUMN_ASSET_RFID, elm));
                    user.put(Asset.COLUMN_ASSET_DESC, getNodeValue(Asset.COLUMN_ASSET_DESC, elm));
                    user.put(Asset.COLUMN_ASSET_PIC, getNodeValue(Asset.COLUMN_ASSET_PIC, elm));
                    user.put(Asset.COLUMN_ASSET_LOCATION, getNodeValue(Asset.COLUMN_ASSET_LOCATION, elm));
                    userList.add(user);
                    //scan get position
                    if (db.checkIsItemCodeInDb(user.put(Asset.COLUMN_ASSET_CODE, getNodeValue(Asset.COLUMN_ASSET_CODE, elm)))) {
                        Log.d(TAG, "onReceive: Exist");
                        Log.d(TAG, "loadAssetList: ");
                    } else {
                        // Inserting record
                        Log.d(TAG, "onReceive: Data No Exist");
                        db.inputDataFromDom(Objects.requireNonNull(user));
                    }
                }

            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }
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
}