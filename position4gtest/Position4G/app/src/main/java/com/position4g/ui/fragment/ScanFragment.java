package com.position4g.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.position4g.R;
import com.position4g.service.SocketService;
import com.position4g.utils.ThreadUtils;
import com.position4g.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/16${Time}
 * @describe ${TODO}
 */

public class ScanFragment extends Fragment {
    @BindView(R.id.et_second)
    EditText mEtSecond;
    @BindView(R.id.btn_ok)
    Button   mBtnOk;
    int second;
    boolean scan = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scan) {
                    mBtnOk.setText("开启");
                    scan=false;
                    ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            SocketService.closeScan();
                        }
                    });
                } else {
                    if (mEtSecond.getText().toString().length() > 0) {
                        second = Integer.parseInt(mEtSecond.getText().toString().trim());
                    }
                    if ((second + "").length() == 0 || second < 15) {
                        ToastUtil.show("扫描周期不能少于15秒");
                    } else {
                        scan = true;
                        mBtnOk.setText("关闭");
                        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                            @Override
                            public void run() {
                                SocketService.openScan(second);
                            }
                        });
                    }
                }
            }
        });
    }
}
