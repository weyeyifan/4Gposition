package com.position4g.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.position4g.R;
import com.position4g.db.MemberDao;
import com.position4g.model.MemberBean;
import com.position4g.ui.fragment.MemberLibFragment;
import com.position4g.utils.ThreadUtils;
import com.position4g.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.position4g.R.id.rb_male;

/**
 * @createAuthor zfb
 * @createTime 2017/3/28${Time}
 * @describe ${TODO}
 */

public class AddLibDialog extends DialogFragment {

    @BindView(R.id.tv_name)
    EditText    mTvName;
    @BindView(R.id.tv_imsi)
    EditText    mTvImsi;
    @BindView(rb_male)
    RadioButton mRbMale;
    @BindView(R.id.rb_female)
    RadioButton mRbFemale;
    @BindView(R.id.rg_gender)
    RadioGroup  mRgGender;
    @BindView(R.id.tv_phone)
    EditText    mTvPhone;
    @BindView(R.id.tv_remark)
    EditText    mTvRemark;

    MemberDao mDao;
    boolean add;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mDao = new MemberDao();
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogfragment_addmem, null);
        //必须在此处注解,默认生成报空指针异常
        ButterKnife.bind(this, view);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("创建",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
//                                String reg = "^[0-9]*$";//数字的正则
//                                Pattern pattern = Pattern.compile(reg);
                                if (mTvName.getText().toString().trim().length() == 0) {
                                    ToastUtil.show("名字不能为空");
                                } else if (mTvImsi.getText().toString().length() != 15) {
                                    ToastUtil.show("请输入15位imsi");
                                }else{
                                    boolean tag=true;
                                        for(int i = 0; i< MemberLibFragment.tempDatas.size(); i++){
                                            if(MemberLibFragment.tempDatas.get(i).getImsi().equals(mTvImsi.getText().toString())){
                                                tag=false;
                                            }
                                        }
                                    if(tag){
                                        if(MemberLibFragment.tempDatas.size()>5){
                                            ToastUtil.show("最多添加6名库内人员");
                                        }else {
                                            ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MemberBean memberBean = new MemberBean();
                                                    memberBean.setImsi(mTvImsi.getText().toString());
                                                    memberBean.setName(mTvName.getText().toString().trim());
                                                    memberBean.setGender(mRbMale.isChecked()?0:1);
                                                    memberBean.setPhone(mTvPhone.getText().toString().trim());
                                                    MemberLibFragment.tempDatas.add(memberBean);

                                                    add=mDao.addLib(memberBean);
                                                    ThreadUtils.runOnMainThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            MemberLibFragment.adapter.notifyDataSetChanged();
                                                            if(add){
                                                                ToastUtil.show("添加成功!");
                                                            }else{
                                                                ToastUtil.show("添加失败!");
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }else{
                                        ToastUtil.show("不能添加重复的IMSI号码");
                                    }

                                }

                            }
                        }).setNegativeButton("取消", null);
        //在dialogfragment里写这一句是没用的,需要在外面设置
        //        builder.setCancelable(false);
        return builder.create();
    }
}
