package com.position4g.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.model.MemberBean;
import com.position4g.ui.fragment.MemberLocalFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class MemLocalAdapter extends BaseAdapter {
    List<MemberBean> datas =new ArrayList<>();
    public static boolean editing=false;
    boolean allChoose=false;

    public void setAll(){
        allChoose=true;
        notifyDataSetChanged();
    }

    public void edit(){
        if(editing){
            editing=false;
            notifyDataSetChanged();
        }else{
            editing=true;
            notifyDataSetChanged();
        }
    }
    public boolean getEdit(){
        return editing;
    }
    public  void setDatas(List<MemberBean> mDatas) {
        datas = mDatas;
    }

    @Override
    public int getCount() {
        if(datas.size()>0){
            return datas.size();
        }
        return 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MyApp.getInstance(), R.layout.item_lv_mem, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(editing){
            holder.mCb.setVisibility(View.VISIBLE);
            holder.mCb.setChecked(datas.get(position).isChecked());
        }else {
            allChoose=false;
            holder.mCb.setChecked(false);
            holder.mCb.setVisibility(View.GONE);
        }
        if(allChoose){
            holder.mCb.setChecked(true);
        }
        holder.mTvName.setText(datas.get(position).getName());

        holder.mTvImsi.setText(datas.get(position).getImsi());
        holder.mCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberLocalFragment.tempDatas.get(position).setChecked(!MemberLocalFragment.tempDatas.get(position).isChecked());
            }
        });
        holder.mTvGender.setText(datas.get(position).getGender()==0?"男":"女");
        holder.mTvPhone.setText(datas.get(position).getPhone());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_imsi)
        TextView mTvImsi;
        @BindView(R.id.tv_gender)
        TextView mTvGender;
        @BindView(R.id.tv_phone)
        TextView mTvPhone;
        @BindView(R.id.cb)
        CheckBox mCb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
