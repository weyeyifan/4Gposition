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

/**
 * @createAuthor zfb
 * @createTime 2017/3/16${Time}
 * @describe ${TODO}
 */

public class VersionFragment extends Fragment{
    TextView mTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_version,container,false);
        mTextView= (TextView) view.findViewById(R.id.tv_version);
        mTextView.setText("版本号:V1.0\n\n" +
                "更新内容:\n1.修改设备管理界面.\n2.修改页面图标.\n3.增加扫描模式.\n\n版权所有: 广州世炬网络科技有限公司");
        mTextView.setTextColor(Color.BLACK);
        return view;
    }
}
