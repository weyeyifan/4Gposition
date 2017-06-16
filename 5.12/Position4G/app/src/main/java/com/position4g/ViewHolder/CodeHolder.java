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

public class CodeHolder {

    @BindView(R.id.count)
    public TextView mCount;
    @BindView(R.id.imsi)
    public TextView mImsi;
    @BindView(R.id.operator)
    public TextView mOperator;
    @BindView(R.id.qcellcore)
    public TextView mQcellcore;
    @BindView(R.id.bbu)
    public TextView mBbu;
    @BindView(R.id.time)
    public TextView mTime;

    public CodeHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
