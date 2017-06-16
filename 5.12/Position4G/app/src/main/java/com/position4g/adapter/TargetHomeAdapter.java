package com.position4g.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.ViewHolder.TargetHolder;
import com.position4g.model.TargetBean;
import com.position4g.utils.DataUtils;

import java.util.ArrayList;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class TargetHomeAdapter extends BaseAdapter{
//    SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ArrayList<TargetBean>datas=new ArrayList<>();

    public void setDatas(ArrayList<TargetBean>mDatas){
        datas=mDatas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TargetHolder holder;
        if (convertView == null) {
            LayoutInflater inflater= LayoutInflater.from(MyApp.getInstance());
            convertView = inflater.inflate(R.layout.item_lv_target,parent,false);
            holder = new TargetHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TargetHolder) convertView.getTag();
        }
        holder.mImsi.setText(datas.get(position).getImsi());
        holder.mRsrp.setText(String.valueOf(datas.get(position).getRsrp()));
        holder.mFreq.setText(datas.get(position).getFreq());
        holder.mDelay.setText(String.valueOf(datas.get(position).getDelay()));
        holder.mBbu.setText(DataUtils.getCellNumber(datas.get(position).getBbu()));
        holder.mTime.setText(datas.get(position).getTime());
        holder.mCount.setText(datas.get(position).getCount()+"");
        return convertView;
    }
}

