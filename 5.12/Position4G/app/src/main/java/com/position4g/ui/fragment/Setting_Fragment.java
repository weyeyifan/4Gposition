package com.position4g.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.position4g.R;
import com.position4g.db.Constants;
import com.position4g.service.SocketService;
import com.position4g.utils.PreferenceUtils;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/4/10${Time}
 * @describe ${TODO}
 */

public class Setting_Fragment extends Fragment {
    @BindView(R.id.btn_timeformat)
    Button mBtnTimeformat;
    @BindView(R.id.btn_voice)
    Button mBtnVoice;
    @BindView(R.id.btn_delay)
    Button mBtnDelay;
    @BindView(R.id.btn_save)
    Button mBtnSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(PreferenceUtils.getBoolean(Constants.TIMEFORMAT,false)){
            mBtnTimeformat.setBackgroundResource(R.drawable.on);
        }else {
            mBtnTimeformat.setBackgroundResource(R.drawable.off);
        }
        if(PreferenceUtils.getBoolean(Constants.VOICEON,true)){
            mBtnVoice.setBackgroundResource(R.drawable.on);
        }else {
            mBtnVoice.setBackgroundResource(R.drawable.off);
        }
        if(PreferenceUtils.getBoolean(Constants.SHOWDELAY,true)){
            mBtnDelay.setBackgroundResource(R.drawable.on);
        }else {
            mBtnDelay.setBackgroundResource(R.drawable.off);
        }
        mBtnTimeformat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceUtils.getBoolean(Constants.TIMEFORMAT,true)){
                    PreferenceUtils.putBoolean(Constants.TIMEFORMAT,false);
                    mBtnTimeformat.setBackgroundResource(R.drawable.off);
                    Target_HomeFragment.format=new SimpleDateFormat("HH:mm:ss");
                }else {
                    PreferenceUtils.putBoolean(Constants.TIMEFORMAT,true);
                    mBtnTimeformat.setBackgroundResource(R.drawable.on);
                    Target_HomeFragment.format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if(Code_HomeFragment.adapter!=null){
                    Code_HomeFragment.adapter.notifyDataSetChanged();
                }
            }
        });
        mBtnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceUtils.getBoolean(Constants.VOICEON,false)){
                    PreferenceUtils.putBoolean(Constants.VOICEON,false);
                    mBtnVoice.setBackgroundResource(R.drawable.off);
                    Target_HomeFragment.voiceOn=false;
                }else {
                    PreferenceUtils.putBoolean(Constants.VOICEON,true);
                    mBtnVoice.setBackgroundResource(R.drawable.on);
                    Target_HomeFragment.voiceOn=true;
                }

            }
        });
        mBtnDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceUtils.getBoolean(Constants.SHOWDELAY,true)){
                    PreferenceUtils.putBoolean(Constants.SHOWDELAY,false);
                    mBtnDelay.setBackgroundResource(R.drawable.off);
                }else {
                    PreferenceUtils.putBoolean(Constants.SHOWDELAY,true);
                    mBtnDelay.setBackgroundResource(R.drawable.on);
                }

            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceUtils.getBoolean(Constants.POWER_SAVE,false)){
                    PreferenceUtils.putBoolean(Constants.POWER_SAVE,false);
                    mBtnSave.setBackgroundResource(R.drawable.off);
                    if(Device_HomeFragment.tempDatas.get(1)!=null&&!Device_HomeFragment.tempDatas.get(1).getLocation().equals("2")){
                        SocketService.OpenBBU(1);
                    }
                    if(Device_HomeFragment.tempDatas.get(2)!=null&&!Device_HomeFragment.tempDatas.get(2).getLocation().equals("2")){
                        SocketService.OpenBBU(2);
                    }
                    if(Device_HomeFragment.tempDatas.get(3)!=null&&!Device_HomeFragment.tempDatas.get(3).getLocation().equals("2")){
                        SocketService.OpenBBU(3);
                    }
                    if(Device_HomeFragment.tempDatas.get(6)!=null&&!Device_HomeFragment.tempDatas.get(6).getLocation().equals("2")){
                        SocketService.OpenBBU(6);
                    }
                    if(Device_HomeFragment.tempDatas.get(7)!=null&&!Device_HomeFragment.tempDatas.get(7).getLocation().equals("2")){
                        SocketService.OpenBBU(7);
                    }
                }else {
                    PreferenceUtils.putBoolean(Constants.POWER_SAVE,true);
                    mBtnSave.setBackgroundResource(R.drawable.on);
                    if(Device_HomeFragment.tempDatas.get(1)!=null&&!Device_HomeFragment.tempDatas.get(1).getLocation().equals("2")){
                        SocketService.CloseBBU(1);
                    }
                    if(Device_HomeFragment.tempDatas.get(2)!=null&&!Device_HomeFragment.tempDatas.get(2).getLocation().equals("2")){
                        SocketService.CloseBBU(2);
                    }
                    if(Device_HomeFragment.tempDatas.get(3)!=null&&!Device_HomeFragment.tempDatas.get(3).getLocation().equals("2")){
                        SocketService.CloseBBU(3);
                    }
                    if(Device_HomeFragment.tempDatas.get(6)!=null&&!Device_HomeFragment.tempDatas.get(6).getLocation().equals("2")){
                        SocketService.CloseBBU(6);
                    }
                    if(Device_HomeFragment.tempDatas.get(7)!=null&&!Device_HomeFragment.tempDatas.get(7).getLocation().equals("2")){
                        SocketService.CloseBBU(7);
                    }
                }

            }
        });
    }
}
