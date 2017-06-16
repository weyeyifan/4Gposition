package com.position4g.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.model.LocationBean;
import com.position4g.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class PositionAdapter extends BaseAdapter {
    protected List<LocationBean> mDatas = new ArrayList();

    public void setDatas(List<LocationBean> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MyApp.getInstance(), R.layout.item_lv_position, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDatas.size() > 0) {
            holder.mCellnum.setText(DataUtils.getCellNumber(mDatas.get(position).getPaNumber()+"").toString());
            holder.mTemp.setText(mDatas.get(position).getTemp()+"");
            holder.mWaveratio.setText(mDatas.get(position).getWaveRadio()+"");
            holder.mPower.setText(mDatas.get(position).getPower()+"");
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.cellnum)
        TextView mCellnum;
        @BindView(R.id.temp)
        TextView mTemp;
        @BindView(R.id.waveratio)
        TextView mWaveratio;
        @BindView(R.id.power)
        TextView mPower;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
