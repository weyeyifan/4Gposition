package com.position4g.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.position4g.R;
import com.position4g.service.SocketService;
import com.position4g.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class DeviceOutputFragment extends Fragment {
    @BindView(R.id.tv1_fr_decice)
    TextView mTv1FrDecice;
    @BindView(R.id.sb1_fr_device)
    SeekBar  mSb1FrDevice;
    @BindView(R.id.btn1_fr_device)
    Button   mBtn1FrDevice;
    @BindView(R.id.tv3_fr_decice)
    TextView mTv3FrDecice;
    @BindView(R.id.sb3_fr_device)
    SeekBar  mSb3FrDevice;
    @BindView(R.id.btn3_fr_device)
    Button   mBtn3FrDevice;
    @BindView(R.id.tv38_fr_decice)
    TextView mTv38FrDecice;
    @BindView(R.id.sb38_fr_device)
    SeekBar  mSb38FrDevice;
    @BindView(R.id.btn38_fr_device)
    Button   mBtn38FrDevice;
    @BindView(R.id.tv39_fr_decice)
    TextView mTv39FrDecice;
    @BindView(R.id.sb39_fr_device)
    SeekBar  mSb39FrDevice;
    @BindView(R.id.btn39_fr_device)
    Button   mBtn39FrDevice;
    @BindView(R.id.tv40_fr_decice)
    TextView mTv40FrDecice;
    @BindView(R.id.sb40_fr_device)
    SeekBar  mSb40FrDevice;
    @BindView(R.id.btn40_fr_device)
    Button   mBtn40FrDevice;
    @BindView(R.id.btn_reset_fr_device)
    Button   mBtnResetFrDevice;

    private int minProgress=17;

    int cellNum;
    int value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_output, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        init();
    }

    private void initEvent() {
        SeekBar.OnSeekBarChangeListener seeckListener= new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()){
                    case R.id.sb1_fr_device:
                        mTv1FrDecice.setText(minProgress+progress+"dBm");
                        break;
                    case R.id.sb3_fr_device:
                        mTv3FrDecice.setText(minProgress+progress+"dBm");
                        break;
                    case R.id.sb38_fr_device:
                        mTv38FrDecice.setText(minProgress+progress+"dBm");
                        break;
                    case R.id.sb39_fr_device:
                        mTv39FrDecice.setText(minProgress+progress+"dBm");
                        break;
                    case R.id.sb40_fr_device:
                        mTv40FrDecice.setText(minProgress+progress+"dBm");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        mSb1FrDevice.setOnSeekBarChangeListener(seeckListener);
        mSb3FrDevice.setOnSeekBarChangeListener(seeckListener);
        mSb38FrDevice.setOnSeekBarChangeListener(seeckListener);
        mSb39FrDevice.setOnSeekBarChangeListener(seeckListener);
        mSb40FrDevice.setOnSeekBarChangeListener(seeckListener);

        mBtn1FrDevice.setOnClickListener(mListener);
        mBtn3FrDevice.setOnClickListener(mListener);
        mBtn38FrDevice.setOnClickListener(mListener);
        mBtn39FrDevice.setOnClickListener(mListener);
        mBtn40FrDevice.setOnClickListener(mListener);

        mBtnResetFrDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            init();
            }
        });
    }

    View.OnClickListener mListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn1_fr_device:
                    cellNum=6;
                    value=mSb1FrDevice.getProgress();
                    break;
                case R.id.btn3_fr_device:
                    cellNum=7;
                    value=mSb3FrDevice.getProgress();
                    break;
                case R.id.btn38_fr_device:
                    cellNum=1;
                    value=mSb38FrDevice.getProgress();
                    break;
                case R.id.btn39_fr_device:
                    cellNum=2;
                    value=mSb39FrDevice.getProgress();
                    break;
                case R.id.btn40_fr_device:
                    cellNum=3;
                    value=mSb40FrDevice.getProgress();
                    break;
            }
            PreferenceUtils.putInt("value"+cellNum,value);
            SocketService.updateBBUOutput(cellNum,value+minProgress);
        }
    };

    public void init(){
        mSb1FrDevice.setProgress(PreferenceUtils.getInt("value"+6,0));
        mSb3FrDevice.setProgress(PreferenceUtils.getInt("value"+7,0));
        mSb38FrDevice.setProgress(PreferenceUtils.getInt("value"+1,0));
        mSb39FrDevice.setProgress(PreferenceUtils.getInt("value"+2,0));
        mSb40FrDevice.setProgress(PreferenceUtils.getInt("value"+3,0));
    }
}
