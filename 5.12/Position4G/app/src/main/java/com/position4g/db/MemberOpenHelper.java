package com.position4g.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.position4g.MyApp;

/**
 * @createAuthor zfb
 * @createTime 2017/3/28${Time}
 * @describe ${TODO}
 */

public class MemberOpenHelper extends SQLiteOpenHelper {
    public MemberOpenHelper() {
        super(MyApp.getInstance(), MemberDB.DB_NAME, null, MemberDB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MemberDB.MemberList.CREATE_LocalTABLE_SQL);
        db.execSQL(MemberDB.MemberList.CREATE_LibTABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
