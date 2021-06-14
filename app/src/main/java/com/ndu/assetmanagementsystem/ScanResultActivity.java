package com.ndu.assetmanagementsystem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper;
import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelperV2;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

import au.com.bytecode.opencsv.CSVWriter;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.ndu.assetmanagementsystem.MainActivity.DEPT_NAME;
import static com.ndu.assetmanagementsystem.NandurLibs.nduDialog;
import static com.ndu.assetmanagementsystem.NandurLibs.toaster;
import static com.ndu.assetmanagementsystem.ScanAssetActivity.AMEN_MODE;
import static com.ndu.assetmanagementsystem.SettingsActivity.SettingsFragment.DATABASE_VERSION;
import static com.ndu.assetmanagementsystem.SettingsActivity.SettingsFragment.KEY_EXPORT_FILE_DIRECTORY;
import static com.ndu.assetmanagementsystem.SettingsActivity.SettingsFragment.SCAN_RESULT_INDICATOR;
import static com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper.DATABASE_NAME;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.COLUMN_DEPT_LOB;
import static com.ndu.assetmanagementsystem.sqlite.database.model.Asset.TABLE_NAME;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTLOBPENGGUNA;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.TABLE_NAME_V2;

public class ScanResultActivity extends AppCompatActivity {

    private static final String KEY_TRIGGER_FROM = "key_trigger_from";
    private static final String EXPORT_MENU = "export_menu";
    private static final String MAIL_BUTTON = "mail_button";
    private DatabaseHelper db;
    private DatabaseHelperV2 db_v2;
    private String dept;
    private SharedPreferences sharedPrefs;
    private File file;
    private String fileNameTxt;
    private String path;
    private final String TAG = ScanResultActivity.class.getSimpleName();
    private SharedPreferences.Editor editor;
    private String sharedDBVersion;
    private String toolbarTitle;
    public static String ASSET_EXIST;

    public ScanResultActivity() {
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.asset_result);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        db = new DatabaseHelper(this);
        db_v2 = new DatabaseHelperV2(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            dept = (String) bundle.get(DEPT_NAME);
            Log.d(TAG, "onCreate: dept" + dept);
            toolbarTitle = getResources().getString(R.string.asset_result) + " Of Asset " + dept.substring(1);
            toolbar.setTitle(toolbarTitle);
        }

        //update value
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();

        sharedDBVersion = sharedPrefs.getString(DATABASE_VERSION, "1");
        ASSET_EXIST = sharedPrefs.getString(SCAN_RESULT_INDICATOR,"B");

        //Initializing
        TextView totalAsset = findViewById(R.id.textView_total_asset);
        TextView readAbleAsset = findViewById(R.id.textView_readble_units);
        TextView unReadAbleAsset = findViewById(R.id.textView_unreadble_units);
        TextView percentageAsset = findViewById(R.id.textView_readble_result);
        Button butMailTo = findViewById(R.id.button_mailto);
        getResources().getDrawable(R.drawable.ic_info_outline_black_24dp);

        if (sharedDBVersion.equals(AMEN_MODE)) {
            totalAsset.setText(String.valueOf(db_v2.getAssetsCountByDeptLob(dept)));
            readAbleAsset.setText(String.valueOf(db_v2.getAssetsCountByExist(dept, ASSET_EXIST)));
        } else {
            totalAsset.setText(String.valueOf(db.getAssetsCountByDeptLob(dept)));
            readAbleAsset.setText(String.valueOf(db.getAssetsCountByExist(dept, ASSET_EXIST)));
        }

        try {
            double totalAssetInt = Double.parseDouble(totalAsset.getText().toString());
            double readAbleAssetInt = Double.parseDouble(readAbleAsset.getText().toString());

            double unRead = totalAssetInt - readAbleAssetInt;
            BigDecimal bdUnread = new BigDecimal(unRead).setScale(0, RoundingMode.HALF_EVEN);
            unReadAbleAsset.setText(String.valueOf(bdUnread));
            double percenTage = (readAbleAssetInt / totalAssetInt) * 100;
            String TAG = "ScanResultActivity";
            Log.d(TAG, "onCreate: " + percenTage);
            //https://stackoverflow.com/a/4826827/7772358
            BigDecimal bd = new BigDecimal(percenTage).setScale(2, RoundingMode.HALF_EVEN);
            bd.doubleValue();
            percentageAsset.setText(String.valueOf(bd));

        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(view -> finish());
        percentageAsset.setOnClickListener(view -> saveToFile());
        butMailTo.setOnClickListener(view -> {
            editor.putString(KEY_TRIGGER_FROM, MAIL_BUTTON);
            editor.apply();
            exportToPdf();
        });
    }

    private void exportToPdf() {
        createPdfAsyncTask task = new createPdfAsyncTask();
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scan_result_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
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

            case R.id.action_export_xls:
                String title = getResources().getString(R.string.action_export_csv) + "?";
                String msg = "Mungkin agak lama, tunggu saja!";
                nduDialog(this,
                        title,
                        msg,
                        true,
                        getDrawable(R.drawable.ic_excel_24px),
                        "Yes", "Cancel",
                        (DialogInterface dialog, int which) -> {
                            if (which == BUTTON_POSITIVE) {
                                exportToXls();
                            }
                            dialog.cancel();
                        });
                return true;

            case R.id.action_export_pdf:
                //https://stackoverflow.com/questions/23408756/create-a-general-class-for-custom-dialog-in-java-android
                nduDialog(this,
                        getResources().getString(R.string.action_export_pdf) + "?",
                        "Mungkin agak lama, tunggu saja!",
                        true,
                        getDrawable(R.drawable.ic_pdf_24px),
                        "Yes",
                        "Cancel",
                        (DialogInterface dialog, int which) -> {
                            if (which == BUTTON_POSITIVE) {
                                dialog.cancel();
                                editor.putString(KEY_TRIGGER_FROM, EXPORT_MENU);
                                editor.apply();
                                exportToPdf();
                                //progressDialog.dismiss();
                            }
                            dialog.cancel();
                        });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToSetting() {
        Intent settingsIntent = new
                Intent(ScanResultActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    // Serializes an object and saves it to a file
    public void saveToFile() {

        ExportDataAsXmlTask task = new ExportDataAsXmlTask();
        task.execute();
//        ExportDatabaseFileTask task = new ExportDatabaseFileTask();
//        task.execute();
        /*List<Asset> assets = new ArrayList<>();
        XmlSerializer serializer = Xml.newSerializer();
        File newxmlfile = new File("/storage/emulated/0/Podcasts/new.xml");
        try {
            newxmlfile.createNewFile();
        } catch (IOException e) {
            Log.e("IOException", "Exception in create new File(");
        }
        FileOutputStream fileos = null;
        try {
            fileos = new FileOutputStream(newxmlfile);

        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.toString());
        }
        try {
            serializer.setOutput(fileos, "UTF-8");
            //<?xml version="1.0" encoding="UTF-8" standalone="true"?>
            serializer.startDocument("UTF-8", true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            //<assets xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            serializer.startTag("", "assets");

            for (Asset asset : assets) {
                serializer.startTag("", "asset");
                serializer.attribute("", "date", asset.getTimestamp());

                serializer.startTag("", COLUMN_ASSET_CODE);
                serializer.text(asset.getAsset_code());
                serializer.endTag("", COLUMN_ASSET_CODE);

                serializer.startTag("", COLUMN_ASSET_DESC);
                serializer.text(asset.getAsset_desc());
                serializer.endTag("", COLUMN_ASSET_DESC);

                serializer.startTag("", COLUMN_ASSET_PIC);
                serializer.text(asset.getAsset_pic());
                serializer.endTag("", COLUMN_ASSET_PIC);

                serializer.startTag("", COLUMN_ASSET_LOCATION);
                serializer.text(asset.getAsset_location());
                serializer.endTag("", COLUMN_ASSET_LOCATION);

                serializer.endTag("", "asset");
            }
            serializer.endTag("", "assets");
            serializer.endDocument();
            serializer.flush();
            if (fileos != null) {
                fileos.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void exportToXls() {
        //known bugs: Jika Android fresh instal, tidak ada value disini dan akan error Read-only file system
        //jadi harus di statmentkan defValuenya ke /storage/emulated/0/AssetV2
        String filepathEM = Environment.getExternalStorageDirectory().getPath();
        File filex = new File(filepathEM);
        if (!filex.exists()) {
            filex.mkdirs();
        }
        String defaultValue = filex.getAbsolutePath() + "/";
        Log.d(TAG, "exportToXls: defaultValue is " + defaultValue);
        path = sharedPrefs.getString(KEY_EXPORT_FILE_DIRECTORY, defaultValue);

        File exportDir = new File(path, "");

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        Log.d(TAG, "exportToXls: " + exportDir.toString());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(ScanResultActivity.this);
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());
        String currentDateNumber = currentDate.format(cal.getTime());
        edittext.setText(toolbarTitle + "_" + currentDateNumber + " " + month_name);
        edittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                ((EditText) v).selectAll();
        });
        alert.setMessage(R.string.export_result_message);
        alert.setTitle(R.string.export_result_title);
        alert.setView(edittext);
        alert.setPositiveButton(R.string.button_yes, (dialog, whichButton) -> {
            //What ever you want to do with the value
            //Editable YouEditTextValue = edittext.getText();
            //OR
            fileNameTxt = edittext.getText().toString();
            file = new File(exportDir, fileNameTxt + ".csv");
            ExportDatabaseToCSVTask task = new ExportDatabaseToCSVTask();
            task.execute();
        });

        alert.setNegativeButton(R.string.button_cancel, (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });
        alert.show();

    }

    @SuppressLint("StaticFieldLeak")
    public class ExportDatabaseToCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(ScanResultActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading_exporting_database));
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File is created!");
                    System.out.println("myfile.csv " + file.getAbsolutePath());
                } else {
                    System.out.println("File already exists.");
                    //toaster(ScanResultActivity.this, "File already exist", 1);
                    file.delete();
                }
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                //SQLiteDatabase db = dbhelper.getWritableDatabase();
                Cursor curCSV;
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    curCSV = db_v2.getReadableDatabase().rawQuery("select * from " + TABLE_NAME_V2 + " where " + COLUMN_TXTLOBPENGGUNA + " LIKE '" + dept + "'", null);
                } else {
                    curCSV = db.getReadableDatabase().rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_DEPT_LOB + " LIKE '" + dept + "'", null);
                }
                csvWrite.writeNext(curCSV.getColumnNames());
                while (curCSV.moveToNext()) {
                    String[] arrStr = {
                            curCSV.getString(0), curCSV.getString(1),
                            curCSV.getString(2), curCSV.getString(3),
                            curCSV.getString(4), curCSV.getString(5),
                            curCSV.getString(6), curCSV.getString(7),
                            curCSV.getString(8), curCSV.getString(9),
                            curCSV.getString(10), curCSV.getString(11),
                            curCSV.getString(12), curCSV.getString(13),
                            curCSV.getString(14), curCSV.getString(15),
                            curCSV.getString(16), curCSV.getString(17),
                            curCSV.getString(18), curCSV.getString(19),
                            curCSV.getString(20)};
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
                //Toast.makeText(ScanResultActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();
                System.out.println("Export DB to CSV Succceed");
                CSVToExcelConverter task = new CSVToExcelConverter();
                task.execute();
            } else {
                System.out.println("Export DB to CSV Failed");
                //Toast.makeText(ScanResultActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    CSV to XLS
    @SuppressLint("StaticFieldLeak")
    public class CSVToExcelConverter extends AsyncTask<String, Void, Boolean> {

        private final ProgressDialog dialog = new ProgressDialog(ScanResultActivity.this);
        private String inFilePath;

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting to excel...");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ArrayList<ArrayList<String>> arList = null;
            ArrayList<String> al;

            inFilePath = path + "/" + fileNameTxt + ".csv";
            String outFilePath = path + "/" + fileNameTxt + ".xls";
            String thisLine;

            try {

                FileInputStream fis = new FileInputStream(inFilePath);
                DataInputStream myInput = new DataInputStream(fis);
                arList = new ArrayList<>();
                while ((thisLine = myInput.readLine()) != null) {
                    al = new ArrayList<>();
                    String[] strar = thisLine.split(",");
                    Collections.addAll(al, strar);
                    arList.add(al);
                    System.out.println();
                }
            } catch (Exception e) {
                System.out.println("shit");
            }

            try {
                HSSFWorkbook hwb = new HSSFWorkbook();
                HSSFSheet sheet = hwb.createSheet(TABLE_NAME);
                for (int k = 0; k < Objects.requireNonNull(arList).size(); k++) {
                    ArrayList<String> ardata = arList.get(k);
                    HSSFRow row = sheet.createRow(k);
                    for (int p = 0; p < ardata.size(); p++) {
                        HSSFCell cell = row.createCell((short) p);
                        String data = ardata.get(p);
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
                Uri uri = Uri.parse(inFilePath);
                File fdelete = new File(uri.getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + uri.getPath());
                    } else {
                        System.out.println("file not Deleted :" + uri.getPath());
                    }
                }
                Toast.makeText(ScanResultActivity.this, "File " + fileNameTxt + ".xls is built!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ScanResultActivity.this, "File fail to build", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //
    @SuppressLint("StaticFieldLeak")
    private class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(ScanResultActivity.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        // automatically done on worker thread (separate from UI thread)
        protected Boolean doInBackground(final String... args) {

            File dbFile =
                    new File(Environment.getDataDirectory() + "/data/com.ndu.assetmanagementsystem.ams/databases/assets_db");

            File exportDir = new File(Environment.getExternalStorageDirectory(), "exampledata");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, dbFile.getName());

            try {
                file.createNewFile();
                this.copyFile(dbFile, file);
                return true;
            } catch (IOException e) {
                Log.e(ScanResultActivity.class.getSimpleName(), e.getMessage(), e);
                return false;
            }
        }

        // can use UI thread here
        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(ScanResultActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ScanResultActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }

        void copyFile(File src, File dst) throws IOException {
            try (FileChannel inChannel = new FileInputStream(src).getChannel(); FileChannel outChannel = new FileOutputStream(dst).getChannel()) {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class ExportDataAsXmlTask extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(ScanResultActivity.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database as XML...");
            //this.dialog.show();
        }

        // automatically done on worker thread (separate from UI thread)
        protected String doInBackground(final String... args) {
            SQLiteDatabase database;
            if (sharedDBVersion.equals(AMEN_MODE)) {
                database = db_v2.getReadableDatabase();
            } else {
                database = db.getReadableDatabase();
            }
            DataXmlExporter dm = new DataXmlExporter(database);
            Log.d("TAG", "doInBackground: " + Arrays.toString(args));
            try {
                String exportFileName = "args";
                dm.export(DATABASE_NAME, exportFileName);
            } catch (IOException e) {
                Log.e(ScanResultActivity.class.getSimpleName(), e.getMessage(), e);
                return e.getMessage();
            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final String errMsg) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (errMsg == null) {
                toaster(ScanResultActivity.this, "Export succesful", 0);
            } else {
                Toast.makeText(ScanResultActivity.this, "Export failed - " + errMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendToEmail() {
        //TODO: Tambahkan selection excel atau pdf dan amen mmode atau demo mode
        //https://stackoverflow.com/questions/9974987/how-to-send-an-email-with-a-file-attachment-in-android
        TextInputEditText editTextMail = findViewById(R.id.textInput_mailTo);
        if (TextUtils.isEmpty(Objects.requireNonNull(editTextMail.getText()).toString())) {
            editTextMail.setError(getString(R.string.email_empty_error_message));
        } else {
            String[] mailto = {Objects.requireNonNull(editTextMail.getText()).toString()};
            String fileName = TABLE_NAME_V2 + " " + dept.substring(1) + ".pdf";
            File fileLocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + TABLE_NAME_V2 + File.separator + fileName);
            Uri uri = Uri.fromFile(fileLocation);
            Log.d(TAG, "sendToEmail: " + uri);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, fileName);
            emailIntent.putExtra(Intent.EXTRA_CC, "");
            emailIntent.putExtra(Intent.EXTRA_BCC, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Laporan " + fileName);
            emailIntent.setType("application/pdf");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(emailIntent, "Send email using:"));
        }
    }

    /**
     * sub-class of AsyncTask
     */
    @SuppressLint("StaticFieldLeak")
    protected class createPdfAsyncTask extends AsyncTask<Context, Integer, String> {
        /*https://stackoverflow.com/questions/6450275/android-how-to-work-with-asynctasks-progressdialog*/
        private final ProgressDialog dialog = new ProgressDialog(ScanResultActivity.this);
        //long totalAsset = DatabaseUtils.queryNumEntries(db.getReadableDatabase(), TABLE_NAME);
        final long totalAssetLocV2 = db_v2.getAssetsCountByDeptLob(dept);
        final long totalAssetLoc = db.getAssetsCountByDeptLob(dept);

        // -- run intensive processes here
        // -- notice that the datatype of the first param in the class definition matches the param passed to this
        // method
        // -- and that the datatype of the last param in the class definition matches the return type of this method
        @SuppressLint("Recycle")
        @Override
        protected String doInBackground(Context... params) {
            // -- on every iteration
            // -- runs a while loop that causes the thread to sleep for 50 milliseconds
            // -- publishes the progress - calls the onProgressUpdate handler defined below
            // -- and increments the counter variable i by one

            /*https://www.tutlane.com/tutorial/android/android-xml-parsing-using-sax-parser*/
            /*https://stackoverflow.com/questions/15967896/how-to-parse-xml-file-from-sdcard-in-android*/
            try {
                String dir;
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    dir = Environment.getExternalStorageDirectory() + File.separator + TABLE_NAME_V2;
                } else {
                    dir = Environment.getExternalStorageDirectory() + File.separator + TABLE_NAME;
                }
                File folder = new File(dir);
                folder.mkdirs();
                File file;
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    file = new File(dir, TABLE_NAME_V2 + " " + dept.substring(1) + ".pdf");
                } else {
                    file = new File(dir, TABLE_NAME + " " + dept.substring(1) + ".pdf");
                }
                SQLiteDatabase database;
                @SuppressLint("Recycle") Cursor c1;
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    database = db_v2.getWritableDatabase();
                    c1 = database.rawQuery("SELECT * FROM " + TABLE_NAME_V2 + " WHERE " + COLUMN_TXTLOBPENGGUNA + " LIKE '" + dept + "'", null);
                } else {
                    database = db.getWritableDatabase();
                    c1 = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DEPT_LOB + " LIKE '" + dept + "'", null);
                }
                Document document = new Document(PageSize.A4.rotate());  // create the document
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//                HeaderAndFooter event = new HeaderAndFooter();
//                writter.setPageEvent(event);

                // add header and footer
                HeaderFooterPageEvent event = new HeaderFooterPageEvent();
                writer.setPageEvent(event);

                // add meta-data to pdf
                document.addAuthor("NDU - Nandang Duryat");
                document.addCreationDate();
                document.addCreator("kalbenutritionals.com");
                document.addTitle("Asset List Kalbe Nutritionals");
                document.addSubject("Asset list generated by RFID System AMS");
                document.addKeywords("rfid, ams, pdf, android");
                document.addLanguage(Locale.ENGLISH.getLanguage());
                document.addHeader("type", "Asset, RFID");
                document.open();

                Paragraph p3 = new Paragraph();
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    p3.add("List of " + TABLE_NAME_V2 + " " + dept.substring(1) + "\n");
                    p3.add("Total " + totalAssetLocV2 + " Assets\n");
                } else {
                    p3.add("List of " + TABLE_NAME + " " + dept.substring(1) + "\n");
                    p3.add("Total " + totalAssetLoc + " Assets\n");
                }
                document.add(p3);
                PdfPTable table;
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    table = new PdfPTable(15);
                    table.addCell("NO");
                    table.addCell("Fixed Asset Code");//4
                    table.addCell("Asset Description");//5
                    table.addCell("Asset Category");//4
                    table.addCell("NIK");//2
                    table.addCell("Responsibility Name");//4
                    table.addCell("Nick Name");//2
                    table.addCell("E-mail");//4
                    table.addCell("User Name");//2
                    table.addCell("User Location");//4
                    table.addCell("User LOB");//4
                    table.addCell("Area");//4
                    table.addCell("RFID Tag");//4
                    table.addCell("Status");//3
                    table.addCell("Notes");//5

                    table.setWidthPercentage(100);
                    table.setWidths(new int[]{2, 4, 5, 4, 2, 4, 2, 4, 2, 4, 4, 4, 4, 3, 5});
                    table.setHeaderRows(1);
                    for (int i = 0; i < totalAssetLocV2; i++) {
                        if (c1.moveToNext()) {

                            table.addCell(String.valueOf(i + 1));
                            table.addCell(c1.getString(0));
                            table.addCell(c1.getString(1));
                            table.addCell(c1.getString(2));
                            table.addCell(c1.getString(3));
                            table.addCell(c1.getString(5));
                            table.addCell(c1.getString(6));
                            table.addCell(c1.getString(7));
                            table.addCell(c1.getString(8));
                            table.addCell(c1.getString(9));
                            table.addCell(c1.getString(10));
                            table.addCell(c1.getString(11));
                            table.addCell(c1.getString(12));
                            table.addCell(c1.getString(13));
                            table.addCell(c1.getString(15));
                        }
                        publishProgress(i);
                    }
                } else {
                    table = new PdfPTable(18);
                    table.addCell("NO");
/*                    table.addCell(COLUMN_FIXED_ASSET_CODE);
                    table.addCell(COLUMN_NAMA_ASSET);
                    table.addCell(COLUMN_UNIT_SISTEM);
                    table.addCell(COLUMN_TANGGAL_BELI);
                    table.addCell(COLUMN_NILAI_BELI);
                    table.addCell(COLUMN_UNIT_AKTUAL); //EDIT
                    table.addCell(COLUMN_UNIT_SELISIH);
                    table.addCell(COLUMN_STATUS); //EDIT
                    table.addCell(COLUMN_DEPT_LOB);
                    table.addCell(COLUMN_DEPT_LOB_UPDATE); //EDIT
                    table.addCell(COLUMN_LOKASI_ASSET_BY_SYSTEM);
                    table.addCell(COLUMN_LOKASI_UPDATE);
                    table.addCell(COLUMN_NAMA_PENGGUNA);
                    table.addCell(COLUMN_NAMA_PENGGUNA_UPDATE); //EDIT
                    table.addCell(COLUMN_NAMA_PENANGGUNG_JAWAB);
                    table.addCell(COLUMN_NAMA_PENANGGUNG_JAWAB_UPDATE); //EDIT
                    table.addCell(COLUMN_KETERANGAN);*/
//                    table.addCell(COLUMN_RFID);
//                    table.addCell(COLUMN_IMAGE_LINK);
//                    table.addCell(COLUMN_ASSET_AREA);
//                    table.addCell(COLUMN_TIMESTAMP);

                    table.addCell("Fixed Asset Code");
                    table.addCell("Nama Asset");
                    table.addCell("Unit Sistem");
                    table.addCell("Tanggal Beli");
                    table.addCell("Nilai Beli");
                    table.addCell("Unit Aktual");
                    table.addCell("Unit Selisih");
                    table.addCell("Status");
                    table.addCell("Departmen/LOB");
                    table.addCell("Departmen/LOB Update");
                    table.addCell("Lokasi Asset By Sistem");
                    table.addCell("Lokasi Update");
                    table.addCell("Nama Pengguna");
                    table.addCell("Nama Pengguna Update");
                    table.addCell("Nama Penanggung Jawab");
                    table.addCell("Nama Penanggung Jawab Update");
                    table.addCell("Keterangan");

                    table.setWidthPercentage(100);
                    table.setWidths(new int[]{2, 4, 5, 2, 4, 4, 2, 2, 2, 4, 4, 4, 4, 3, 5, 4, 4, 4/*, 4, 2, 2*/});
                    table.setHeaderRows(1);
                    for (int i = 0; i < totalAssetLoc; i++) {
                        if (c1.moveToNext()) {
                            String fixedAssetCode = c1.getString(0);
                            String namaAsset = c1.getString(1);
                            String unitSistem = c1.getString(2);
                            String tanggalBeli = c1.getString(3);
                            String nilaiBeli = c1.getString(4);
                            String unitAktual = c1.getString(5);
                            String unitSelisih = c1.getString(6);
                            String status = c1.getString(7);
                            String deptLob = c1.getString(8);
                            String deptLobUpdate = c1.getString(9);
                            String lokasiAssetBySystem = c1.getString(10);
                            String lokasiUpdate = c1.getString(11);
                            String namaPengguna = c1.getString(12);
                            String namaPenggunaUpdate = c1.getString(13);
                            String namaPenanggungJawab = c1.getString(14);
                            String namaPenanggungJawabUpdate = c1.getString(15);
                            String keterangan = c1.getString(16);
                            String rfid = c1.getString(17);
                            String imageLink = c1.getString(18);
                            String assetArea = c1.getString(19);
                            String timestamp = c1.getString(20);
                            table.addCell(String.valueOf(i + 1));
                            table.addCell(fixedAssetCode);
                            table.addCell(namaAsset);
                            table.addCell(unitSistem);
                            table.addCell(tanggalBeli);
                            table.addCell(nilaiBeli);
                            table.addCell(unitAktual);
                            table.addCell(unitSelisih);
                            table.addCell(status);
                            table.addCell(deptLob);
                            table.addCell(deptLobUpdate);
                            table.addCell(lokasiAssetBySystem);
                            table.addCell(lokasiUpdate);
                            table.addCell(namaPengguna);
                            table.addCell(namaPenggunaUpdate);
                            table.addCell(namaPenanggungJawab);
                            table.addCell(namaPenanggungJawabUpdate);
                            table.addCell(keterangan);
//                            table.addCell(rfid);
//                            table.addCell(imageLink);
//                            table.addCell(assetArea);
//                            table.addCell(timestamp);
                        }
                        publishProgress(i);
                    }
                }

                document.add(table);
                document.addCreationDate();
                document.close();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
                return "No File";
            }
            return "COMPLETE!";
        }

        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute()");
            super.onPreExecute();
            this.dialog.setTitle("Export Asset To .pdf");
            this.dialog.setMessage("Preparing asset, Please wait!");
            this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.dialog.setIndeterminate(true);
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
                Log.d(TAG, "onProgressUpdate: " + values[0]);
                double totalAssetDb;
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    totalAssetDb = Double.parseDouble(String.valueOf(totalAssetLocV2));
                } else {
                    totalAssetDb = Double.parseDouble(String.valueOf(totalAssetLoc));
                }
                double percenTage = (valuesDb / totalAssetDb) * 100;
                Log.d(TAG, "onCreate: " + percenTage);
                BigDecimal bd = new BigDecimal(percenTage).setScale(2, RoundingMode.HALF_EVEN);
                bd.doubleValue();
                Log.d(TAG, "onProgressUpdate: " + bd);
//                this.dialog.setMessage("Importing data asset " + (values[0]) + "/" + totalAsset + " (" + bd + "%)");
                if (sharedDBVersion.equals(AMEN_MODE)) {
                    this.dialog.setTitle("Exporting " + (int) totalAssetLocV2 + " Assets to .pdf");
                    this.dialog.setMax((int) totalAssetLocV2);
                } else {
                    this.dialog.setTitle("Exporting " + (int) totalAssetLoc + " Assets to .pdf");
                    this.dialog.setMax((int) totalAssetLoc);
                }
                this.dialog.setMessage("It will take a while, Please wait!");
                this.dialog.setProgress(values[0]);

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
                String triggerFrom = sharedPrefs.getString(KEY_TRIGGER_FROM, "");
                if (triggerFrom != null && triggerFrom.equals(EXPORT_MENU)) {
                    Toast.makeText(ScanResultActivity.this, "Export complete", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScanResultActivity.this, "Export complete", Toast.LENGTH_SHORT).show();
                    sendToEmail();
                }
            } else if (result.equals("No File")) {
                Toast.makeText(ScanResultActivity.this, "No Asset file in the database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ScanResultActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}