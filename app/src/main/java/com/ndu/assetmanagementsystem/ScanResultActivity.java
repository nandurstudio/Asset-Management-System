package com.ndu.assetmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

import static com.ndu.assetmanagementsystem.MainActivity.DEPT_NAME;
import static com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper.DATABASE_NAME;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.ASSET_EXIST;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.TABLE_NAME;

public class ScanResultActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView totalAsset;
    private String assetLocation;
    private String TAG = "ScanResultActivity";
    private TextView readAbleAsset;
    private TextView unReadAbleAsset;
    private TextView percentageAsset;
    private Button butExport;

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
        butExport = findViewById(R.id.button_export_csv);

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
        butExport.setOnClickListener(view -> exportDataTXls());
    }

    private void exportDataTXls() {
        ExportDatabaseToCSVTask task = new ExportDatabaseToCSVTask();
        task.execute();
    }

    public class ExportDatabaseToCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(ScanResultActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args) {

            File dbFile = getDatabasePath(DATABASE_NAME);
            //AABDatabaseManager dbhelper = new AABDatabaseManager(getApplicationContext());
            DatabaseHelper db = new DatabaseHelper(ScanResultActivity.this);
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
                Toast.makeText(ScanResultActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();
                CSVToExcelConverter task = new CSVToExcelConverter();
                task.execute();
            } else {
                Toast.makeText(ScanResultActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    CSV to XLS
    public class CSVToExcelConverter extends AsyncTask<String, Void, Boolean> {


        private final ProgressDialog dialog = new ProgressDialog(ScanResultActivity.this);

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
                Toast.makeText(ScanResultActivity.this, "file is built!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ScanResultActivity.this, "file fail to build", Toast.LENGTH_SHORT).show();
            }
        }
    }
}