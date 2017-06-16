package com.position4g.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.position4g.R;
import com.position4g.model.ItemModelOfDrawerList;

import java.util.List;

/**
 * drawerlayout的listview适配器
 */
public class DrawerListAdapter extends BaseAdapter {


    private Context                     mContext;
    private List<ItemModelOfDrawerList> list;
    boolean mTag=false;

    public void setTag(boolean tag){
        mTag=tag;
    }

    public DrawerListAdapter(Context context, List<ItemModelOfDrawerList> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_drawer_lv, parent, false);
            holder = new ViewHolder();
//            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_list_drawer);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_list_drawer);
            holder.iv= (ImageView) convertView.findViewById(R.id.iv_circle);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if(position==4){
            if(mTag){
                holder.iv.setVisibility(View.VISIBLE);
            }else{
                holder.iv.setVisibility(View.GONE);
            }
        }

        ItemModelOfDrawerList item = list.get(position);
//        Glide.with(mContext).load(item.resId).into(holder.imageView);
        holder.textView.setText(item.tab);

        return convertView;
    }

    public class ViewHolder {
//        private ImageView imageView;
        private TextView textView;
        private ImageView iv;
    }
}
