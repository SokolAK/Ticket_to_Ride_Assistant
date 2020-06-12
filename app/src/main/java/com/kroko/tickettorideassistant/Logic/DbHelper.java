package com.kroko.TicketToRideAssistant.Logic;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.AcousticEchoCanceler;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHelper extends SQLiteOpenHelper {
    private final String DB_NAME;
    private final int DB_VERSION;
    private final String OUTFILE_NAME;
    private final Context mContext;

    private DbHelper(Context mContext, String DB_NAME, int DB_VERSION) {
        super(mContext, DB_NAME, null, DB_VERSION);
        this.mContext = mContext;
        this.DB_NAME = DB_NAME;
        this.DB_VERSION = DB_VERSION;
        OUTFILE_NAME = mContext.getDatabasePath(DB_NAME).getPath() ;
        //this.DB_PATH = "/data/data/" + mContext.getPackageName() + "/databases/";
    }

    public static DbHelper getInstance(Context mContext, String DB_NAME, int DB_VERSION) {
            return new DbHelper(mContext, DB_NAME, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void checkDatabase() {
        //String path = DB_PATH + DB_NAME;
        //SQLiteDatabase.openDatabase(path, null, 0);
        this.getReadableDatabase();
        copyDatabase();
    }

    public void copyDatabase() {
        try {
            InputStream io = mContext.getAssets().open(DB_NAME);
            //String outFileName = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(OUTFILE_NAME);
            int length;
            byte[] buffer = new byte[1024];
            while ((length = io.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            io.close();
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext, "Database not found!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase openDatabase() {
        //String outFileName = DB_PATH + DB_NAME;
        return SQLiteDatabase.openDatabase(OUTFILE_NAME, null, 0);
    }
}
