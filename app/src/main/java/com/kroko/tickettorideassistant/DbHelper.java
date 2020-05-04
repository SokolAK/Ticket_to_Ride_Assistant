package com.kroko.TicketToRideAssistant;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.AcousticEchoCanceler;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

class DbHelper extends SQLiteOpenHelper {

    private String DB_NAME;
    private String DB_PATH;
    private int DB_VERSION;
    private Context mContext;

    public DbHelper(Context mContext, String DB_NAME, int DB_VERSION) {
        super(mContext, DB_NAME, null, DB_VERSION);
        this.mContext = mContext;
        this.DB_NAME = DB_NAME;
        this.DB_VERSION = DB_VERSION;
        this.DB_PATH = "/data/data/" + mContext.getPackageName() + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void checkDatabase() {
        try{
            String path = DB_PATH + DB_NAME;
            SQLiteDatabase.openDatabase(path,null,0);
        }
        catch (Exception e) {}

        this.getReadableDatabase();
        copyDatabase();
    }

    public void copyDatabase() {
        try{
            InputStream io = mContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            int length;
            byte[] buffer = new byte[1024];
            while((length = io.read(buffer)) > 0) {
                outputStream.write(buffer,length,0);
            }
            io.close();
            outputStream.flush();
            outputStream.close();
        }
        catch(Exception e) {}
    }

    public void openDatabase() {
        String path = DB_PATH + DB_NAME;
        SQLiteDatabase.openDatabase(path,null,0);

    }
}
