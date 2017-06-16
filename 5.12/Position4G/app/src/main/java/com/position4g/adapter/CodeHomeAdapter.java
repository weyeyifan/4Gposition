package com.position4g.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.ViewHolder.CodeHolder;
import com.position4g.db.Constants;
import com.position4g.model.CodeBean;
import com.position4g.utils.DataUtils;
import com.position4g.utils.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class CodeHomeAdapter extends BaseAdapter {
    SimpleDateFormat format;
    List<CodeBean> datas = new ArrayList<>();

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
        CodeHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(MyApp.getInstance());
            convertView = inflater.inflate(R.layout.item_lv_code, parent, false);
            holder = new CodeHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CodeHolder) convertView.getTag();
        }
        holder.mImsi.setText(datas.get(position).getImsi());

        if (PreferenceUtils.getBoolean(Constants.TIMEFORMAT, false)) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time=datas.get(position).getUptime();
            String j=format.format(new Date(time*1000));
            holder.mTime.setText(j);
            holder.mTime.setTextSize(10);
        } else {
            format = new SimpleDateFormat("HH:mm:ss");

            long time=datas.get(position).getUptime();
            String j=format.format(new Date(time*1000));
            holder.mTime.setTextSize(12);
            holder.mTime.setText("    " + j);
            holder.mBbu.setText("  " + holder.mBbu.getText());
        }

        holder.mCount.setText(datas.get(position).getCount() + "");
        String bbu = DataUtils.getCellNumber(datas.get(position).getCellNumber() + "");
        holder.mBbu.setText(bbu == null ? "null" : bbu);
        holder.mOperator.setText(DataUtils.getOpera(holder.mImsi.getText().toString()));
        return convertView;
    }

    public void setData(List<CodeBean> list) {
        datas = list;
    }
}
