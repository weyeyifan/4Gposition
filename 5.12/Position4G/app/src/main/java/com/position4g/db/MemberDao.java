package com.position4g.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.position4g.model.MemberBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @createAuthor zfb
 * @createTime 2017/3/28${Time}
 * @describe ${TODO}
 */

public class MemberDao {
    private MemberOpenHelper mOpenHelper;

    public MemberDao(){
        mOpenHelper=new MemberOpenHelper();
    }
    SQLiteDatabase database;
    //事件结束后手动关闭database,我也很无奈
    /**
     * 添加本地人员
     */
    public boolean add(MemberBean bean){
        database=mOpenHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(MemberDB.MemberList.NAME,bean.getName());
        values.put(MemberDB.MemberList.IMSI,bean.getImsi());
        values.put(MemberDB.MemberList.GENDER,bean.getGender());
        values.put(MemberDB.MemberList.PHONE_NUMBER,bean.getPhone());
        values.put(MemberDB.MemberList.REMARK,bean.getRemark());

        long insert=database.insert(MemberDB.MemberList.Local_TABLE_NAME,null,values);
//        database.close();
        return insert!=-1;
    }
    /**
     * 添加入库人员
     */
    public boolean addLib(MemberBean bean){
        database=mOpenHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(MemberDB.MemberList.NAME,bean.getName());
        values.put(MemberDB.MemberList.IMSI,bean.getImsi());
        values.put(MemberDB.MemberList.GENDER,bean.getGender());
        values.put(MemberDB.MemberList.PHONE_NUMBER,bean.getPhone());
        values.put(MemberDB.MemberList.REMARK,bean.getRemark());

        long insert=database.insert(MemberDB.MemberList.Lib_TABLE_NAME,null,values);
        //        database.close();
        return insert!=-1;
    }

    /**
     * 批量添加入库人员
     * @param list
     */
    public void addAll(ArrayList<MemberBean>list){
        database=mOpenHelper.getWritableDatabase();
        MemberBean bean;
        ContentValues values;
        for(int i=0;i<list.size();i++){
            bean=list.get(i);
            values=new ContentValues();
            values.put(MemberDB.MemberList.NAME,bean.getName());
            values.put(MemberDB.MemberList.IMSI,bean.getImsi());
            values.put(MemberDB.MemberList.GENDER,bean.getGender());
            values.put(MemberDB.MemberList.PHONE_NUMBER,bean.getPhone());
            values.put(MemberDB.MemberList.REMARK,bean.getRemark());
            database.insert(MemberDB.MemberList.Lib_TABLE_NAME,null,values);
        }
    }

    /**
     * 批量添加本地人员
     * @param list
     */
    public void addAllLocal(ArrayList<MemberBean>list){
        database=mOpenHelper.getWritableDatabase();
        MemberBean bean;
        ContentValues values;
        for(int i=0;i<list.size();i++){
            bean=list.get(i);
            values=new ContentValues();
            values.put(MemberDB.MemberList.NAME,bean.getName());
            values.put(MemberDB.MemberList.IMSI,bean.getImsi());
            values.put(MemberDB.MemberList.GENDER,bean.getGender());
            values.put(MemberDB.MemberList.PHONE_NUMBER,bean.getPhone());
            values.put(MemberDB.MemberList.REMARK,bean.getRemark());
            database.insert(MemberDB.MemberList.Local_TABLE_NAME,null,values);
        }
    }

    //根据imsi删除本地人员
    public boolean delete(String imsi){
        database=mOpenHelper.getWritableDatabase();
        int delete=database.delete(MemberDB.MemberList.Local_TABLE_NAME,"imsi=?",new String[]{imsi});
//        database.close();
        return delete!=0;
    }
    //根据imsi删除库内人员
    public boolean deleteLib(String imsi){
        database=mOpenHelper.getWritableDatabase();
        int delete=database.delete(MemberDB.MemberList.Lib_TABLE_NAME,"imsi=?",new String[]{imsi});
        //        database.close();
        return delete!=0;
    }
    //删除所有本地人员
    public void deleteAll(){
        database=mOpenHelper.getWritableDatabase();
        database.execSQL("delete from memberlist");
        database.close();
    }
    //删除所有入库人员
    public void deleteLibAll(){
        database=mOpenHelper.getWritableDatabase();
        database.execSQL("delete from memberliblist");
        database.close();
    }

    //查询所有本地人员

    public ArrayList<MemberBean> queryAllLocalMember(){
        database=mOpenHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from "+MemberDB.MemberList.Local_TABLE_NAME,null);
        ArrayList<MemberBean>beans=new ArrayList<>();
        if(cursor!=null){
            while (cursor.moveToNext()){
                MemberBean bean=new MemberBean();
                //第一位是自然增长的主键值id
                bean.name=cursor.getString(1);
                bean.imsi=cursor.getString(2);
                bean.gender=cursor.getInt(3);
                bean.phone=cursor.getString(4);
                bean.remark=cursor.getString(5);
                beans.add(bean);
            }
            cursor.close();
        }
        database.close();
        return beans;
    }

    //查询所有库内人员
    public List<MemberBean> queryAllLibMember(){
        database=mOpenHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from "+MemberDB.MemberList.Lib_TABLE_NAME,null);
        List<MemberBean>beans=new ArrayList<>();
        if(cursor!=null){
            while (cursor.moveToNext()){
                MemberBean bean=new MemberBean();
                //第一位0是自然增长的主键值id,从第二位name开始取
                bean.name=cursor.getString(1);
                bean.imsi=cursor.getString(2);
                bean.gender=cursor.getInt(3);
                bean.phone=cursor.getString(4);
                bean.remark=cursor.getString(5);
                beans.add(bean);
            }
            cursor.close();
        }
        database.close();
        return beans;
    }
//
//    //更新
//    public boolean update(String imsi,MemberBean bean){
//        database=mOpenHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(MemberDB.MemberList.NAME,bean.name);
//        values.put(MemberDB.MemberList.IMSI,imsi);
//        values.put(MemberDB.MemberList.GENDER,bean.gender);
//        values.put(MemberDB.MemberList.PHONE_NUMBER,bean.phone);
//        values.put(MemberDB.MemberList.REMARK,bean.remark);
//        //失败返回0条修改
//        int result=database.update(MemberDB.MemberList.Local_TABLE_NAME,values,"imsi=?",new String[]{imsi});
//        database.close();
//        return result!=0;
//    }

    public void close(){
        if(database!=null&&database.isOpen()){
            database.close();
        }
    }
}
