package com.position4g.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.position4g.MyApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @createAuthor zfb
 * @createTime 2017/3/30${Time}
 * @describe ${TODO}
 */

public class AddressDao {
    public static ArrayList<String> getProvince(){
        String path= MyApp.getInstance().getFilesDir().getAbsolutePath()+ "/address.db";
        SQLiteDatabase db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        String SQL="SELECT areaName FROM ProvinceCityZone LIMIT 31";//这个数据库31个省
        ArrayList<String> provinceList=new ArrayList<>();
        Cursor c=null;
        c=db.rawQuery(SQL,null);
        if(c!=null){
            while (c.moveToNext()){
                provinceList.add(c.getString(0));
            }
        }
        c.close();
        db.close();
        return provinceList;
    }
    public static HashMap<Integer,ArrayList<String>> getCity(){
        String path= MyApp.getInstance().getFilesDir().getAbsolutePath()+ "/address.db";
        SQLiteDatabase db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        String SQL="SELECT parentID,areaName FROM ProvinceCityZone LIMIT 340 OFFSET 31";//这个数据库31个省340个市

        Cursor c=null;
        c=db.rawQuery(SQL,null);
        Map<String,Integer> map=new HashMap();
        if(c!=null){
            while (c.moveToNext()){
                map.put(c.getString(1),c.getInt(0));
            }
        }
        HashMap<Integer,ArrayList<String>>tempMap=new HashMap<>();
        for(Map.Entry<String,Integer>entry:map.entrySet()){

            if(entry.getKey().equals("省直辖县级行政单位")){
                continue;
            }
            Integer i=entry.getValue();
            if(tempMap.get(i)==null){
                ArrayList<String> list=new ArrayList();
                list.add(entry.getKey());
                tempMap.put(i,list);
            }else {
                ArrayList<String> list=tempMap.get(i);
                list.add(entry.getKey());
                tempMap.put(i,list);
            }
        }

        c.close();
        db.close();
        return tempMap;
    }
}
