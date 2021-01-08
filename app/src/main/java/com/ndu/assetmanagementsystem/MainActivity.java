package com.ndu.assetmanagementsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import static com.ndu.assetmanagementsystem.NandurLibs.dialogInfoVersionName;
import static com.ndu.assetmanagementsystem.NandurLibs.shareApp;
import static com.ndu.assetmanagementsystem.NandurLibs.versCode;
import static com.ndu.assetmanagementsystem.NandurLibs.versName;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_ADMOB = "Admob";
    static final String DEPT_NAME = "dept_name";
    static final String ASSET_AREA = "asset_area";
    private static final String DEPT_GENERAL = "General";
    private static final String DEPT_PRD = "Production";
    private static final String DEPT_QA = "QA";
    private static final String DEPT_PPC_PREP = "Preparation";
    private static final String DEPT_EM = "EM";
    private static final String DEPT_HRGA = "HRGA";
    private static final String ALL_DEPT = "All Dept";
    private static final String DIV_PLANT = "Plant";
    private static final String DIV_KN = "KN";
    private static final String AREA_OFFICE_PLANT = "Office Plant";
    private static final String AREA_PRODUKSI_BASIC_CARE = "Produksi Basic Care";
    private static final String AREA_PRODUKSI_HIGH_CARE = "Produksi High Care";
    private static final String AREA_PREPARASI_BASIC_CARE = "Preparasi Basic Care";
    private static final String AREA_PREPARASI_HIGH_CARE = "Preparasi High Care";
    private static final String AREA_ENGINEERING_MAINTENANCE = "Engineering Maintenance";
    private DrawerLayout drawer;
    private Handler handler;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private Button buttAms;
    private String TAG = "MainActivity";
    private Spinner spinnerDynamic;
    private String deptName;
    private String floorStage;
    private Spinner spinnerArea;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());*/
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        handler = new Handler();
        buttAms = findViewById(R.id.buttonAms);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Spinner https://stackoverflow.com/a/29778386/7772358
        Spinner spinnerShp = findViewById(R.id.spinnerShp);
        spinnerArea = findViewById(R.id.spinner_area);
        spinnerDynamic = findViewById(R.id.spinner_dynamic);


        ArrayAdapter<CharSequence> arrayAdapterFloor = ArrayAdapter
                .createFromResource(this, R.array.area_array,
                        R.layout.spinner_row);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> arrayAdapterShp = ArrayAdapter
                .createFromResource(this, R.array.shp_array,
//                        android.R.layout.simple_spinner_item
                        R.layout.spinner_row);

        ArrayAdapter<CharSequence> arrayAdapterPlant = ArrayAdapter
                .createFromResource(this, R.array.plant_array,
                        R.layout.spinner_row);


        ArrayAdapter<CharSequence> arrayAdapterKN = ArrayAdapter
                .createFromResource(this, R.array.kn_array,
                        R.layout.spinner_row);

        // Specify the layout to use when the list of choices appears
        arrayAdapterShp
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
/*        String[] items = new String[]{"Chai Latte", "Green Tea", "Black Tea"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);*/

        // Apply the adapter to the spinner
        spinnerArea.setAdapter(arrayAdapterFloor);
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("floor", (String) parent.getItemAtPosition(position));
                if (parent.getItemAtPosition(position).equals(AREA_OFFICE_PLANT)) {
                    floorStage = AREA_OFFICE_PLANT;
                } else if (parent.getItemAtPosition(position).equals(AREA_PRODUKSI_BASIC_CARE)) {
                    floorStage = AREA_PRODUKSI_BASIC_CARE;
                } else if (parent.getItemAtPosition(position).equals(AREA_PRODUKSI_HIGH_CARE)) {
                    floorStage = AREA_PRODUKSI_HIGH_CARE;
                } else if (parent.getItemAtPosition(position).equals(AREA_PREPARASI_BASIC_CARE)) {
                    floorStage = AREA_PREPARASI_BASIC_CARE;
                } else if (parent.getItemAtPosition(position).equals(AREA_PREPARASI_HIGH_CARE)) {
                    floorStage = AREA_PREPARASI_HIGH_CARE;
                } else if (parent.getItemAtPosition(position).equals(AREA_ENGINEERING_MAINTENANCE)) {
                    floorStage = AREA_ENGINEERING_MAINTENANCE;
                } else {
                    floorStage = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerShp.setAdapter(arrayAdapterShp);
        spinnerShp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals(DIV_PLANT)) {
                    Log.d(TAG, "onItemSelected: " + DIV_PLANT);
                    spinnerDynamic.setAdapter(arrayAdapterPlant);
                } else {
                    spinnerDynamic.setAdapter(arrayAdapterKN);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDynamic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                if (parent.getItemAtPosition(position).equals("General")) {
                    deptName = DEPT_GENERAL;
                } else if (parent.getItemAtPosition(position).equals("Production")) {
                    deptName = DEPT_PRD;
                } else if (parent.getItemAtPosition(position).equals("Quality Assurance")) {
                    deptName = DEPT_QA;
                } else if (parent.getItemAtPosition(position).equals("PPC & Preparation")) {
                    deptName = DEPT_PPC_PREP;
                } else if (parent.getItemAtPosition(position).equals("Engineering Maintenace")) {
                    deptName = DEPT_EM;
                } else if (parent.getItemAtPosition(position).equals("HRGA")) {
                    deptName = DEPT_HRGA;
                } else {
                    deptName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //getVersionName
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versName = pInfo.versionName;
            versCode = pInfo.versionCode;
            Log.d("MyApp", "Version Name : " + versName + "\n Version Code : " + versCode);
        } catch (
                PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("MyApp", "PackageManager Catch : " + e.toString());
        }

        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        // find MenuItem you want to change
        MenuItem nav_appversion = menu.findItem(R.id.nav_version_name);
        // set new title to the MenuItem
        nav_appversion.setTitle(versName);

        buttAms.setOnClickListener(view ->

                goToAms());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            goToSetting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToSetting() {
        Intent settingsIntent = new
                Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void goToAms() {
        Intent intent = new
                Intent(MainActivity.this, ScanAssetActivity.class);
        intent.putExtra(DEPT_NAME, deptName);
        intent.putExtra(ASSET_AREA, floorStage);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_tools:
                closeDrawer();
                handler.postDelayed(this::goToSetting, 250);
                break;
            case R.id.nav_share:
                closeDrawer();
                handler.postDelayed(() -> shareApp(this,
                        getResources().getString(R.string.menu_send),
                        getResources().getString(R.string.app_name),
                        getResources().getString(R.string.version_title),
                        versName,
                        getResources().getString(R.string.build_title),
                        versCode), 250);
                break;
            case R.id.nav_version_name:
                closeDrawer();
                handler.postDelayed(() -> dialogInfoVersionName(MainActivity.this), 250);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }  //super.onBackPressed();

    }
}