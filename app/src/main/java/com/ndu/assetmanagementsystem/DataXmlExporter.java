package com.ndu.assetmanagementsystem;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DataXmlExporter {

    private static final String DATASUBDIRECTORY = "exampledata";

    private SQLiteDatabase db;
    private XmlBuilder xmlBuilder;

    public DataXmlExporter(SQLiteDatabase db) {
        this.db = db;
    }

    public void export(String dbName, String exportFileNamePrefix) throws IOException {
        Log.i("MyApplication.APP_NAME", "exporting database - " + dbName + " exportFileNamePrefix=" + exportFileNamePrefix);

        this.xmlBuilder = new XmlBuilder();
        this.xmlBuilder.start(dbName);

        // get the tables
        String sql = "select * from Asset";
        @SuppressLint("Recycle") Cursor c = this.db.rawQuery(sql, new String[0]);
        Log.d("MyApplication.APP_NAME", "select * from sqlite_master, cur size " + c.getCount());
        if (c.moveToFirst()) {
            do {
                try {
                    String tableName = c.getString(c.getColumnIndex("Asset"));
                    Log.d("MyApplication.APP_NAME", "table name " + tableName);

                    // skip metadata, sequence, and uidx (unique indexes)
                    if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")
                            && !tableName.startsWith("uidx")) {
                        this.exportTable(tableName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        }
        String xmlString = this.xmlBuilder.end();
        this.writeToFile(xmlString, exportFileNamePrefix + ".xml");
        Log.i("MyApplication.APP_NAME", "exporting database complete");
    }

    private void exportTable(final String tableName) {
        Log.d("MyApplication.APP_NAME", "exporting table - " + tableName);
        this.xmlBuilder.openTable(tableName);
        String sql = "select * from " + tableName;
        Cursor c = this.db.rawQuery(sql, new String[0]);
        if (c.moveToFirst()) {
            int cols = c.getColumnCount();
            do {
                this.xmlBuilder.openRow();
                for (int i = 0; i < cols; i++) {
                    this.xmlBuilder.addColumn(c.getColumnName(i), c.getString(i));
                }
                this.xmlBuilder.closeRow();
            } while (c.moveToNext());
        }
        c.close();
        this.xmlBuilder.closeTable();
    }

    private void writeToFile(String xmlString, String exportFileName) throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(), DATASUBDIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, exportFileName);
        file.createNewFile();

        ByteBuffer buff = ByteBuffer.wrap(xmlString.getBytes());
        try (FileChannel channel = new FileOutputStream(file).getChannel()) {
            channel.write(buff);
        }
    }

    static class XmlBuilder {
        private static final String OPEN_XML_STANZA = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"true\"?>";
        private static final String CLOSE_WITH_TICK = "'>";
        private static final String DB_OPEN = "<database name='";
        private static final String DB_CLOSE = "</database>";
        private static final String TABLE_OPEN = "<table name='";
        private static final String TABLE_CLOSE = "</table>";
        private static final String ROW_OPEN = "<row>";
        private static final String ROW_CLOSE = "</row>";
        private static final String COL_OPEN = "<col name='";
        private static final String COL_CLOSE = "</col>";
        private static final String TAG_ASSET_OPEN = "<asset>";

        private final StringBuilder sb;

        public XmlBuilder() {
            this.sb = new StringBuilder();
        }

        void start(String dbName) {
            //<?xml version="1.0" encoding="utf-8"?>
            this.sb.append(OPEN_XML_STANZA);
            //
            this.sb.append(DB_OPEN).append(dbName).append(CLOSE_WITH_TICK);
        }

        String end() {
            this.sb.append(DB_CLOSE);
            return this.sb.toString();
        }

        void openTable(String tableName) {
            this.sb.append(TABLE_OPEN).append(tableName).append(CLOSE_WITH_TICK);
        }

        void closeTable() {
            this.sb.append(TABLE_CLOSE);
        }

        void openRow() {
            this.sb.append(ROW_OPEN);
        }

        void closeRow() {
            this.sb.append(ROW_CLOSE);
        }

        void addColumn(final String name, final String val) {
            this.sb.append(COL_OPEN).append(name).append(CLOSE_WITH_TICK).append(val).append(COL_CLOSE);
        }
    }
}
