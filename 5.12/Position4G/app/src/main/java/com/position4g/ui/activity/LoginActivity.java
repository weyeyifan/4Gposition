package com.position4g.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.position4g.R;
import com.position4g.db.Constants;
import com.position4g.utils.PreferenceUtils;
import com.position4g.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.btn_login)
    Button   mBtnLogin;
    @BindView(R.id.cb_login)
    CheckBox mCbLogin;

    String username;
    String password;
    boolean defaultPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        PreferenceUtils.putString(Constants.USERNAME,"admin");
        defaultPassword=PreferenceUtils.getBoolean(Constants.DEFAULTPASSWORD,true);
        if(defaultPassword){
            PreferenceUtils.putString(Constants.PASSWORD,"admin");
        }
        if(PreferenceUtils.getBoolean(Constants.REMEMBERPASSWORD,false)){
            mEtUsername.setText(PreferenceUtils.getString(Constants.USERNAME,"admin"));
            mEtPassword.setText(PreferenceUtils.getString(Constants.PASSWORD,"admin"));
            mCbLogin.setChecked(true);
        }

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=mEtUsername.getText().toString().trim();
                password=mEtPassword.getText().toString().trim();
                        if(PreferenceUtils.getString(Constants.USERNAME).equals(username)&&PreferenceUtils.getString(Constants.PASSWORD).equals(password)){
                            if(mCbLogin.isChecked()){
                                PreferenceUtils.putBoolean(Constants.REMEMBERPASSWORD,true);
                            }else {
                                PreferenceUtils.putBoolean(Constants.REMEMBERPASSWORD,false);
                            }
                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            ToastUtil.show("帐号或密码错误!");
                        }
            }
        });
    }
}
