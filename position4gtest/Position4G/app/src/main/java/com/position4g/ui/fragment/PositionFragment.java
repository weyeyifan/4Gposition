package com.position4g.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.position4g.R;
import com.position4g.adapter.PositionAdapter;
import com.position4g.eventbusmessage.LocationMessage;
import com.position4g.model.LocationBean;
import com.position4g.model.PaTemperBean;
import com.position4g.utils.DataUtils;
import com.position4g.utils.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @createAuthor zfb
 * @createTime 2017/3/16${Time}
 * @describe ${TODO}
 */

public class PositionFragment extends Fragment {
    ListView mListView;
    public static List<LocationBean> mDatas;
    PositionAdapter                    adapter;
    List<PaTemperBean.PaPowerBean>     power;
    List<PaTemperBean.PatempBean>      temp;
    List<PaTemperBean.PaWaveRatioBean> waveratio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position, container, false);
        power = new ArrayList();
        temp = new ArrayList();
        waveratio = new ArrayList();
        mListView = (ListView) view.findViewById(R.id.lv_position);
        adapter = new PositionAdapter();
        mListView.setAdapter(adapter);
        return view;
    }

    @Subscribe
    public void onLocation(final LocationMessage event) {
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<PaTemperBean> data = DataUtils.getPaTemBean(event.message);
                if (data.size() > 0) {
                    mDatas.clear();
                    for (int i = 0; i < data.size(); i++) {
                        power = data.get(i).getPaPower();
                        temp = data.get(i).getPatemp();
                        waveratio = data.get(i).getPaWaveRatio();
                        for (int j = 0; j < power.size(); j++) {
                            LocationBean bean = new LocationBean();
                            bean.setPaNumber(power.get(j).getPaNumber());
                            bean.setPower(power.get(j).getPower());
                            bean.setTemp(temp.get(j).getTemp());
                            bean.setWaveRadio(waveratio.get(j).getWaveRadio());
                            mDatas.add(bean);
                        }

                    }
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setDatas(mDatas);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDatas = new ArrayList<>();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
