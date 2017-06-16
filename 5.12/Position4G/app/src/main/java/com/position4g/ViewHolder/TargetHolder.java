package com.position4g.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.position4g.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class TargetHolder {

    @BindView(R.id.count)
    public TextView mCount;
    @BindView(R.id.imsi)
    public TextView mImsi;
    @BindView(R.id.time)
    public TextView mTime;
    @BindView(R.id.rsrp)
    public TextView mRsrp;
    @BindView(R.id.freq)
    public TextView mFreq;
    @BindView(R.id.delay)
    public TextView mDelay;
    @BindView(R.id.bbu)
    public TextView mBbu;

    public TargetHolder(View itemView) {
        ButterKnife.bind(this,itemView);
    }
}
