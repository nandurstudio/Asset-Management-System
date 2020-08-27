package com.ndu.assetmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cipherlab.rfid.DeviceEvent;
import com.cipherlab.rfid.GeneralString;
import com.cipherlab.rfidapi.RfidManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper;
import com.ndu.assetmanagementsystem.sqlite.database.model.Asset;
import com.ndu.assetmanagementsystem.sqlite.utils.MyDividerItemDecoration;
import com.ndu.assetmanagementsystem.sqlite.utils.RecyclerTouchListener;
import com.ndu.assetmanagementsystem.sqlite.view.AssetsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "rfid";
    private AssetsAdapter mAdapter;
    private List<Asset> assetList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noAssetView;

    private DatabaseHelper db;
    //RFID
    RfidManager mRfidManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noAssetView = findViewById(R.id.empty_assets_view);

        db = new DatabaseHelper(this);

        assetList.addAll(db.getAllAssets());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAssetDialog(false, null, -1);
            }
        });

        //RFID

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
    private void updateAsset(String asset, int position) {
        Asset a = assetList.get(position);
        // updating asset text
        a.setAsset_code(asset);

        // updating asset in db
        db.updateAsset(a);

        // refreshing the list
        assetList.set(position, a);
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

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputAsset = view.findViewById(R.id.assetCode);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_asset_title) : getString(R.string.lbl_edit_asset_title));

        if (shouldUpdate && asset != null) {
            inputAsset.setText(asset.getAsset_code());
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
                if (TextUtils.isEmpty(inputAsset.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter asset!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating asset
                if (shouldUpdate && asset != null) {
                    // update asset by it's id
                    updateAsset(inputAsset.getText().toString(), position);
                } else {
                    // create new asset
                    createAsset(inputAsset.getText().toString());
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

                Toast.makeText(MainActivity.this, "Intent_RFIDSERVICE_CONNECTED", Toast.LENGTH_SHORT).show();
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
                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] EPC=" + EPC);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] EPC_length=" + EPC_length);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] TID=" + TID);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] TID_length=" + TID_length);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] ReadData=" + ReadData);
//                Log.d(TAG, "[Intent_RFIDSERVICE_TAG_DATA] ReadData_length=" + ReadData_length);


                //scan get position
                if (db.CheckIsDataAlreadyInDBorNot(EPC)) {
                    Log.d(TAG, "onReceive: Exist");
                } else {
                    // Inserting record
                    Log.d(TAG, "onReceive: Data No Exist");
                    createAsset(EPC);
                }
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
                    Toast.makeText(MainActivity.this, mse, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Intent_FWUpdate_Finish", Toast.LENGTH_SHORT).show();
            }

        }
    };

}