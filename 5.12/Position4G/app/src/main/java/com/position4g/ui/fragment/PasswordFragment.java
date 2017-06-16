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
import com.position4g.db.Constants;
import com.position4g.utils.PreferenceUtils;
import com.position4g.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/4/7${Time}
 * @describe ${TODO}
 */

public class PasswordFragment extends Fragment {
    @BindView(R.id.et_oldpassword)
    EditText mEtOldpassword;
    @BindView(R.id.et_newpassword)
    EditText mEtNewpassword;
    @BindView(R.id.btn_updatepassword)
    Button   mBtnUpdatepassword;
    String oldPassword;
    String newPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnUpdatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword= PreferenceUtils.getString(Constants.PASSWORD,"admin");
                if(oldPassword.equals(mEtOldpassword.getText().toString().trim())){
                    newPassword=mEtNewpassword.getText().toString().trim();
                    if(newPassword.length()==0){
                        ToastUtil.show("密码不能为空!");
                    }else{
                        PreferenceUtils.putString(Constants.PASSWORD,newPassword);
                        PreferenceUtils.putBoolean(Constants.DEFAULTPASSWORD,false);
                        ToastUtil.show("修改成功!");
                    }
                }else{
                    ToastUtil.show("修改失败!");
                }
            }
        });

    }
}
