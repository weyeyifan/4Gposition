package com.position4g.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.db.AddressDao;
import com.position4g.db.DevDao;
import com.position4g.model.DevBean;
import com.position4g.service.SocketService;
import com.position4g.utils.PreferenceUtils;
import com.position4g.utils.ThreadUtils;
import com.position4g.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.position4g.R.id.city;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class DeviceParamFragment extends Fragment {


    ArrayList<String>                   provinceList;
    HashMap<Integer, ArrayList<String>> cityMap;
    ArrayList<String>                   cityList;
    @BindView(R.id.btn_update)
    Button      mBtnUpdate;
    @BindView(R.id.btn_cancel)
    Button      mBtnCancel;
    @BindView(R.id.sp_province)
    Spinner     mSpProvince;
    @BindView(city)
    Spinner     mCity;
    @BindView(R.id.tv_city)
    TextView    mTvCity;
    @BindView(R.id.cb_ison)
    CheckBox    mCbIsOn;
    @BindView(R.id.tv_ison)
    TextView    mTvIson;
    @BindView(R.id.et_freq10)
    EditText    mEtFreq10;
    @BindView(R.id.rb_10)
    RadioButton mRb10;
    @BindView(R.id.rb_11)
    RadioButton mRb11;
    @BindView(R.id.et_freq20)
    EditText    mEtFreq20;
    @BindView(R.id.rb_20)
    RadioButton mRb20;
    @BindView(R.id.rb_21)
    RadioButton mRb21;
    @BindView(R.id.et_delay_b38)
    EditText    mEtDelayB38;
    @BindView(R.id.et_delay_b39)
    EditText    mEtDelayB39;
    @BindView(R.id.et_delay_b40)
    EditText    mEtDelayB40;
    @BindView(R.id.et_freq30)
    EditText    mEtFreq30;
    @BindView(R.id.rb_30)
    RadioButton mRb30;
    @BindView(R.id.rb_31)
    RadioButton mRb31;
    @BindView(R.id.rb_32)
    RadioButton mRb32;
    @BindView(R.id.rb_1cancel)
    RadioButton mRb1Cancel;
    @BindView(R.id.rb_2cancel)
    RadioButton mRb2Cancel;
    @BindView(R.id.rb_3cancel)
    RadioButton mRb3Cancel;
    @BindView(R.id.et_freq11)
    EditText    mEtFreq11;
    @BindView(R.id.et_freq21)
    EditText    mEtFreq21;
    @BindView(R.id.et_freq31)
    EditText    mEtFreq31;
    @BindView(R.id.et_freq32)
    EditText    mEtFreq32;

    ArrayAdapter<String> provinceAdapter;
    ArrayAdapter<String> cityAdapter;
    //当前选中的省
    String               selectedProvince;
    //当前选中的市
    String               selectedCity;
    int                  selectedProvincePosition;
    int                  selectedCityPosition;
    //当前激活的市
    String activeCity = "null";

    DevDao mDao;
    boolean tag = true;
    boolean update;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_param, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activeCity = PreferenceUtils.getString("activecity", "null");
        selectedCity = activeCity;
        if (activeCity.length() == 0) {
            activeCity = "null";
        }
        initEvent();
        initData(selectedCity);

    }

    private void initEvent() {
        mCbIsOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTvIson.setText(isChecked ? "激活" : "未激活");
            }
        });
        mBtnUpdate.setOnClickListener(updateListener);
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                provinceList = AddressDao.getProvince();
                cityMap = AddressDao.getCity();

                provinceAdapter = new ArrayAdapter<>(MyApp.getInstance(), R.layout.myspinner, provinceList);

                mSpProvince.setOnItemSelectedListener(provinceListener);

                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mSpProvince.setAdapter(provinceAdapter);
                        selectedProvincePosition = PreferenceUtils.getInt("province", 0);
                        mSpProvince.setSelection(selectedProvincePosition);

                    }
                });
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(selectedCity);
            }
        });
    }

    //一级菜单选择监听器
    AdapterView.OnItemSelectedListener provinceListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cityList = cityMap.get(position + 2);
            cityAdapter = new ArrayAdapter<>(MyApp.getInstance(), R.layout.myspinner, cityList);
            mCity.setAdapter(cityAdapter);
            mCity.setOnItemSelectedListener(cityListener);
            selectedProvince = provinceList.get(position);
            selectedProvincePosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    //二级菜单选择监听器
    AdapterView.OnItemSelectedListener cityListener     = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedCity = cityList.get(position);
            selectedCityPosition = position;
            mTvCity.setText(selectedProvince + "-" + selectedCity);
            if (tag) {
                selectedCityPosition = PreferenceUtils.getInt("city", 0);
                mCity.setSelection(selectedCityPosition);
                tag = false;
            }
            initData(selectedCity);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void initData(final String city) {

        if (city != "null") {
            ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                @Override
                public void run() {
                    mDao = new DevDao();
                    final DevBean bean = mDao.queryByCity(city);
                    mDao.close();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (city.equals(activeCity)) {
                                mCbIsOn.setChecked(true);
                            } else {
                                mCbIsOn.setChecked(false);
                            }
                            if (bean.getFreq_dianxin_b1() != null) {
                                mEtFreq10.setText(bean.freq_dianxin_b1);
                                mEtFreq11.setText(bean.freq_dianxin_b3);
                                switch (bean.bbu_dianxin) {
                                    case 6:
                                        mRb10.setChecked(true);
                                        break;
                                    case 7:
                                        mRb11.setChecked(true);
                                        break;
                                    default:
                                        mRb1Cancel.setChecked(true);
                                        break;
                                }
                                mEtFreq20.setText(bean.freq_liantong_b1);
                                mEtFreq21.setText(bean.freq_liantong_b3);
                                switch (bean.bbu_liantong) {
                                    case 6:
                                        mRb20.setChecked(true);
                                        break;
                                    case 7:
                                        mRb21.setChecked(true);
                                        break;
                                    default:
                                        mRb2Cancel.setChecked(true);
                                        break;
                                }
                                mEtFreq30.setText(bean.freq_yidong_b38);
                                mEtFreq31.setText(bean.freq_yidong_b39);
                                mEtFreq32.setText(bean.freq_yidong_b40);
                                mEtDelayB38.setText(bean.delay_b38);
                                mEtDelayB39.setText(bean.delay_b39);
                                mEtDelayB40.setText(bean.delay_b40);
                                switch (bean.bbu_yidong) {
                                    case 1:
                                        mRb30.setChecked(true);
                                        break;
                                    case 2:
                                        mRb31.setChecked(true);
                                        break;
                                    case 3:
                                        mRb32.setChecked(true);
                                        break;
                                    default:
                                        mRb3Cancel.setChecked(true);
                                        break;
                                }
                            } else {
                                mEtFreq10.setText("100");
                                mEtFreq11.setText("1825");
                                mRb11.setChecked(true);

                                mEtFreq20.setText("500");
                                mEtFreq21.setText("1650");
                                mRb21.setChecked(true);

                                mEtFreq30.setText("37900,38098");
                                mEtFreq31.setText("37900,38098");
                                mEtFreq32.setText("38950");
                                mEtDelayB38.setText("0");
                                mEtDelayB39.setText("0");
                                mEtDelayB40.setText("0");

                                mRb3Cancel.setChecked(true);
                            }
                        }
                    });
                }
            });
        }
    }

    View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mEtFreq10.length() > 0 && mEtFreq11.length() > 0 && mEtFreq20.length() > 0 && mEtFreq21.length() > 0 && mEtFreq30.length() > 0 && mEtFreq31.length() > 0 && mEtFreq32.length() > 0 && mEtDelayB38.length() > 0 && mEtDelayB39.length() > 0 && mEtDelayB40.length() > 0) {
                mCbIsOn.setChecked(true);
                PreferenceUtils.putInt("province", selectedProvincePosition);
                PreferenceUtils.putInt("city", selectedCityPosition);

                DevBean bean = new DevBean();
                bean.setCity(selectedCity);

                bean.setFreq_dianxin_b1(mEtFreq10.getText().toString());
                bean.setFreq_dianxin_b3(mEtFreq11.getText().toString());
                if (mRb10.isChecked()) {
                    bean.setBbu_dianxin(6);
                } else if (mRb11.isChecked()) {
                    bean.setBbu_dianxin(7);
                }

                bean.setFreq_liantong_b1(mEtFreq20.getText().toString());
                bean.setFreq_liantong_b3(mEtFreq21.getText().toString());
                if (mRb20.isChecked()) {
                    bean.setBbu_liantong(6);
                } else if (mRb21.isChecked()) {
                    bean.setBbu_liantong(7);
                }

                bean.setDelay_b38(mEtDelayB38.getText().toString());
                bean.setDelay_b39(mEtDelayB39.getText().toString());
                bean.setDelay_b40(mEtDelayB40.getText().toString());

                bean.setFreq_yidong_b38(mEtFreq30.getText().toString());
                bean.setFreq_yidong_b39(mEtFreq31.getText().toString());
                bean.setFreq_yidong_b40(mEtFreq32.getText().toString());
                if (mRb30.isChecked()) {
                    bean.setBbu_yidong(1);
                } else if (mRb31.isChecked()) {
                    bean.setBbu_yidong(2);
                } else if (mRb32.isChecked()) {
                    bean.setBbu_yidong(3);
                }
                final DevBean tempBean = bean;
                ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDao = new DevDao();
                        update = mDao.add(tempBean);
                        mDao.close();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                if (update) {
                                    ToastUtil.show("修改成功!");
                                } else {
                                    ToastUtil.show("修改失败!");
                                }
                            }
                        });
                        SocketService.updateDev(tempBean);
                    }
                });
                activeCity = selectedCity;
                PreferenceUtils.putString("activecity", activeCity);
            } else {
                ToastUtil.show("请填写完整信息");
            }
        }
    };
}
