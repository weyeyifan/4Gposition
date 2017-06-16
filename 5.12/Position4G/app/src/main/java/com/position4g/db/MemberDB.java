package com.position4g.db;

/**
 * @createAuthor zfb
 * @createTime 2017/3/28${Time}
 * @describe ${TODO}
 */

public interface MemberDB {
    String DB_NAME = "member.db";
    int    VERSION = 1;

    interface MemberList {
        String Local_TABLE_NAME      = "memberlist";
        String Lib_TABLE_NAME        = "memberliblist";
        String ID                    = "id";
        String NAME                  = "name";
        String IMSI                  = "imsi";
        String GENDER                = "gender";
        String PHONE_NUMBER          = "phone";
        String REMARK                = "remark";
        String CREATE_LocalTABLE_SQL = "create table " + Local_TABLE_NAME + "(" + ID + " integer primary key autoincrement," + NAME + " text," + IMSI + " text,"  + GENDER + " integer," + PHONE_NUMBER + " text," + REMARK + " text)";
        String CREATE_LibTABLE_SQL      = "create table " + Lib_TABLE_NAME + "(" + ID + " integer primary key autoincrement," + NAME + " text," + IMSI + " text," + GENDER + " integer," + PHONE_NUMBER + " text," + REMARK + " text)";
    }
}
