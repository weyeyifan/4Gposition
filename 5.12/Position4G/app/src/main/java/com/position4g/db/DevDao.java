package com.position4g.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.position4g.model.DevBean;

/**
 * @createAuthor zfb
 * @createTime 2017/3/31${Time}
 * @describe ${TODO}
 */

public class DevDao {
    private DevOpenHelper mDevOpenHelper;
    public DevDao(){
        mDevOpenHelper=new DevOpenHelper();
    }
    SQLiteDatabase mDatabase;

    //根据城市名查询
    public DevBean queryByCity(String city){
        mDatabase=mDevOpenHelper.getWritableDatabase();
        DevBean bean=new DevBean();
        Cursor cursor=mDatabase.rawQuery("SELECT * FROM "+DevDB.DBList.DEV_TABLE_NAME+" WHERE city =\""+city+"\"",null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                bean.city=cursor.getString(1);
                bean.freq_dianxin_b1=cursor.getString(2);
                bean.freq_dianxin_b3=cursor.getString(3);
                bean.bbu_dianxin=cursor.getInt(4);
                bean.freq_liantong_b1=cursor.getString(5);
                bean.freq_liantong_b3=cursor.getString(6);
                bean.bbu_liantong=cursor.getInt(7);

                bean.freq_yidong_b38=cursor.getString(8);
                bean.freq_yidong_b39=cursor.getString(9);
                bean.freq_yidong_b40=cursor.getString(10);

                bean.delay_b38=cursor.getString(11);

                bean.delay_b39=cursor.getString(12);

                bean.delay_b40=cursor.getString(13);

                bean.bbu_yidong=cursor.getInt(14);
            }
        }
        mDatabase.close();
        return bean;
    }

    public void close(){
        if(mDatabase!=null&&mDatabase.isOpen()){
            mDatabase.close();
        }
    }

    //保存城市信息
    public boolean add(DevBean bean){
        mDatabase=mDevOpenHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DevDB.DBList.CITY,bean.getCity());
        values.put(DevDB.DBList.FREQ_DIANXIN_B1,bean.getFreq_dianxin_b1());
        values.put(DevDB.DBList.FREQ_DIANXIN_B3,bean.getFreq_dianxin_b3());
        values.put(DevDB.DBList.BBU_DIANXIN,bean.getBbu_dianxin());
        values.put(DevDB.DBList.FREQ_LIANTONG_B1,bean.getFreq_liantong_b1());
        values.put(DevDB.DBList.FREQ_LIANTONG_B3,bean.getFreq_liantong_b3());
        values.put(DevDB.DBList.BBU_LIANTONG,bean.getBbu_liantong());
        values.put(DevDB.DBList.DELAY_B38,bean.getDelay_b38());
        values.put(DevDB.DBList.FREQ_YIDONG_B38,bean.getFreq_yidong_b38());
        values.put(DevDB.DBList.DELAY_B39,bean.getDelay_b39());
        values.put(DevDB.DBList.FREQ_YIDONG_B39,bean.getFreq_yidong_b39());
        values.put(DevDB.DBList.DELAY_B40,bean.getDelay_b40());
        values.put(DevDB.DBList.FREQ_YIDONG_B40,bean.getFreq_yidong_b40());
        values.put(DevDB.DBList.BBU_YIDONG,bean.getBbu_yidong());
        long insert=mDatabase.insert(DevDB.DBList.DEV_TABLE_NAME,null,values);
        return  insert!=-1;
    }
}
