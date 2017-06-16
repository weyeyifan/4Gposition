package com.position4g.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.position4g.R;
import com.position4g.eventbusmessage.WarnMessage;
import com.position4g.model.WarnBean;
import com.position4g.ui.activity.HomeActivity;
import com.position4g.utils.DataUtils;
import com.position4g.utils.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/16${Time}
 * @describe ${TODO}
 */

public class WarnFragment extends Fragment {
    String warnState;
    String warnContent;
    @BindView(R.id.tv_warnstate)
    TextView mTvWarnstate;
    @BindView(R.id.tv_warncontent)
    TextView mTvWarncontent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warn, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Subscribe
    public void onWarnMessage(final WarnMessage event) {
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<WarnBean> datas = DataUtils.getWarnBean(event.message);
                if (datas.size() > 0) {
                    WarnBean bean = datas.get(datas.size() - 1);
                    switch (bean.getAlarmType()) {
                        case 1:
                            warnState = "新增告警";
                            break;
                        case 2:
                            warnState = "已解决";
                            break;
                    }
                    switch (bean.getData().getAlarmCode()) {
                        case 1:
                            warnContent = "重启";
                            break;
                        case 2:
                            warnContent = "MAC_PHY_ABNORMAL";
                            break;
                        case 3:
                            warnContent = "断线";
                            break;
                    }
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvWarnstate.setText(warnState);
                            mTvWarncontent.setText(warnContent);
                            if(warnState.equals("新增告警")){
                                mTvWarnstate.setTextColor(Color.RED);
                            }else {
                                mTvWarnstate.setTextColor(Color.GREEN);
                            }
                            ((HomeActivity)getActivity()).setWarnTag();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
