package com.position4g.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.model.SyncBean;
import com.position4g.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/16${Time}
 * @describe ${TODO}
 */

public class DeviceHomeAdapter extends BaseAdapter {

    List<SyncBean> mDatas=new ArrayList<>();

    public void setDatas(List<SyncBean> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return (mDatas!=null)?mDatas.size():0;

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
        MyViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MyApp.getInstance(), R.layout.item_lv_device, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (MyViewHolder) convertView.getTag();
        }


        if(mDatas.size()>position&&mDatas.get(position)!=null){
            final SyncBean bean=mDatas.get(position);
            if(bean!=null){
                if(bean.getBbu()!=null){
                    holder.mTvCellnum.setText(bean.getBbu());
                }
                if(bean.getBbuStatus()!=null){
                    switch (bean.getBbuStatus()){
                        case "0":
                            holder.mTvState.setText("不可用");
                            holder.mTvPosition.setText("否");
                            break;
                        case "1":
                            holder.mTvState.setText("可用");
                            holder.mTvPosition.setText("否");
                            break;
                        case "2":
                            holder.mTvState.setText("可用");
                            holder.mTvPosition.setText("是");
                            break;
                    }
                }

                holder.mTvSync.setText(DataUtils.getSyncState(bean.getSyncstatus()));
            }
        }
        return convertView;
    }

    static class MyViewHolder {
        @BindView(R.id.tv_cellnum)
        TextView mTvCellnum;
        @BindView(R.id.tv_state)
        TextView mTvState;
        @BindView(R.id.tv_sync)
        TextView mTvSync;
        @BindView(R.id.tv_position)
        TextView mTvPosition;

        MyViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
