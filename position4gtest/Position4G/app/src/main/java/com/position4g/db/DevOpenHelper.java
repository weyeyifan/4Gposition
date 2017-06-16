package com.position4g.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.position4g.MyApp;

/**
 * @createAuthor zfb
 * @createTime 2017/3/31${Time}
 * @describe ${TODO}
 */

public class DevOpenHelper extends SQLiteOpenHelper{

    public DevOpenHelper() {
        super(MyApp.getInstance(), DevDB.DB_NAME, null, DevDB.version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DevDB.DBList.CREATE_DEVTABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
