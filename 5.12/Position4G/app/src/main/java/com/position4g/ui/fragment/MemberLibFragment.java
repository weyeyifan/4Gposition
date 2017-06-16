package com.position4g.ui.fragment;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.adapter.MemLibAdapter;
import com.position4g.db.Constants;
import com.position4g.db.MemberDao;
import com.position4g.model.MemberBean;
import com.position4g.service.SocketService;
import com.position4g.ui.dialog.AddLibDialog;
import com.position4g.utils.PreferenceUtils;
import com.position4g.utils.ThreadUtils;
import com.position4g.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${库内人员管理页面}
 */

public class MemberLibFragment extends Fragment {
    public static ArrayList<MemberBean> mControlDatas;
    public static MemLibAdapter         adapter;
    public static List<MemberBean>      tempDatas;


    MemberDao mDao;
    @BindView(R.id.lv_memlib_fragment)
    ListView             mLvMemlibFragment;
    @BindView(R.id.pb)
    ProgressBar          mPb;
    @BindView(R.id.btn_all)
    Button               mBtnAll;
    @BindView(R.id.btn_translate)
    Button               mBtnTranslate;
    @BindView(R.id.btn_delete)
    Button               mBtnDelete;
    @BindView(R.id.btn_cancel)
    Button               mBtnCancel;
    @BindView(R.id.control)
    LinearLayout         mControl;
    @BindView(R.id.fbtn_memlocal)
    FloatingActionButton mFbtnMemlocal;
    private TextView mTvIMSI;
    private Button mBtnCheck;
    private TextView mTvState;
    private TextView mTvPhone;
    /**
     * 以下为silencesms相关设定
     */
    final byte[] payload = new byte[]{0x0A, 0x06, 0x03, (byte) 0xB0, (byte) 0xAF, (byte) 0x82, 0x03, 0x06, 0x6A, 0x00, 0x05};
    PendingIntent sentPI;
    PendingIntent deliveryPI;
    final String SENT    = "pingsms.sent";
    final String DELIVER = "pingsms.deliver";

    final IntentFilter sentFilter        = new IntentFilter(SENT);
    final IntentFilter deliveryFilter    = new IntentFilter(DELIVER);
    final IntentFilter wapDeliveryFilter = new IntentFilter("android.provider.Telephony.WAP_PUSH_DELIVER");
    /**
     * 以上为silencesms相关设定
     */

    private AlertDialog.Builder mBuilder;

    int checkPosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memlib, container, false);
        tempDatas = new ArrayList<>();
        sentPI = PendingIntent.getBroadcast(MyApp.getInstance(), 0x1337, new Intent(SENT), PendingIntent.FLAG_CANCEL_CURRENT);
        deliveryPI = PendingIntent.getBroadcast(MyApp.getInstance(), 0x1337, new Intent(DELIVER), PendingIntent.FLAG_CANCEL_CURRENT);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MemLibAdapter();
        mPb.setVisibility(View.VISIBLE);
        mDao = new MemberDao();
        mLvMemlibFragment.setAdapter(adapter);
        //从数据库读取数据
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                tempDatas = mDao.queryAllLibMember();
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setDatas(tempDatas);
                        adapter.notifyDataSetChanged();
                        mPb.setVisibility(View.GONE);
                    }
                });
            }
        });

        mBtnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setAll();
                for (int i = 0; i < tempDatas.size(); i++) {
                    tempDatas.get(i).setChecked(true);
                }
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDao = new MemberDao();
                mControlDatas = new ArrayList<>();
                ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < tempDatas.size(); i++) {
                            if (tempDatas.get(i).isChecked) {

                                final String imsi = tempDatas.get(i).getImsi();
                                tempDatas.remove(i);
                                --i;
                                mDao.deleteLib(imsi);
                            }
                        }
                        mDao.close();
                        for (int i = 0; i < tempDatas.size(); i++) {
                            tempDatas.get(i).setChecked(false);
                        }
                        if (PreferenceUtils.getBoolean(Constants.LIANTONGLOCATION, false)) {
                            SocketService.openLocation("FDD");
                        }
                        if (PreferenceUtils.getBoolean(Constants.DIANXINLOCATION, false)) {
                            SocketService.openLocation("FDD");
                        }
                        if (PreferenceUtils.getBoolean(Constants.YIDONGLOCATION, false)) {
                            SocketService.openLocation("TDD");
                        }
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });

        mLvMemlibFragment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.getEdit()) {
                    mControl.setVisibility(View.GONE);
                } else {
                    mControl.setVisibility(View.VISIBLE);
                }
                adapter.edit();
                return true;
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mControl.setVisibility(View.GONE);
                adapter.edit();
                for (int i = 0; i < tempDatas.size(); i++) {
                    tempDatas.get(i).setChecked(false);
                }
            }
        });

        mBtnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDao = new MemberDao();
                        mControlDatas = new ArrayList<>();
                        mControlDatas.addAll(tempDatas);
                        for (int i = 0; i < mControlDatas.size(); i++) {
                            if (mControlDatas.get(i).isChecked) {
                                for (int j = 0; j < MemberLocalFragment.tempDatas.size(); j++) {
                                    if (MemberLocalFragment.tempDatas.get(j).getImsi().equals(mControlDatas.get(i).getImsi())) {
                                        mControlDatas.remove(i);
                                        i--;
                                        break;
                                    }
                                }
                            } else {
                                mControlDatas.remove(i);
                            }
                        }
                        mDao.addAllLocal(mControlDatas);
                        mDao.close();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                MemberLocalFragment.tempDatas.addAll(mControlDatas);
                                MemberLocalFragment.refreshLib();
                                ToastUtil.show("移入本地成功!");
                            }
                        });
                    }
                });
            }
        });

        mFbtnMemlocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddLibDialog dialog = new AddLibDialog();
                dialog.setCancelable(false);
                dialog.show(fm, "");
            }
        });

        mLvMemlibFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkPosition=position;
                mBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                mBuilder.setTitle("详细信息");
                View contentView=getActivity().getLayoutInflater().inflate(R.layout.alert_member,null);
                mBuilder.setView(contentView);

                mTvIMSI = (TextView) contentView.findViewById(R.id.tv_imsi_alert);
                mTvIMSI.setText(tempDatas.get(position).getImsi());
                mTvPhone = (TextView) contentView.findViewById(R.id.tv_phone_alert);
                if(tempDatas.get(position).getPhone().toString().trim().length()>0){
                    mTvPhone.setText(tempDatas.get(position).getPhone());
                }
                mTvState = (TextView) contentView.findViewById(R.id.tv_state_alert);
                mBtnCheck = (Button) contentView.findViewById(R.id.btn_check);
                mTvState.setText(tempDatas.get(position).getState()==1?"在线":"N/A");

                mBtnCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tempDatas.get(checkPosition).setState(0);
                        adapter.notifyDataSetChanged();
                        send();

                    }
                });
                mBuilder.show();
            }
        });
    }

    /**
     * 以下为接受silencesms返回信息的广播接受者
     */
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            if (DELIVER.equalsIgnoreCase(intent.getAction())) {
                //实际上移动号码走到这一步就基本是在线了,不在线一般不会收到这个intent
                boolean delivered = false;
                if (intent.hasExtra("pdu")) {
                    byte[] pdu = (byte[]) intent.getExtras().get("pdu");
                    if (pdu != null && pdu.length > 1) {
                        String resultPdu = getLogBytesHex(pdu).trim();
                        delivered = "00".equalsIgnoreCase(resultPdu.substring(resultPdu.length() - 2));
                    }
                }
                mTvState.setText(delivered ? "在线" : "不在线");
                if(delivered){
                    tempDatas.get(checkPosition).setState(1);
                    adapter.notifyDataSetChanged();
                }
            }

        }
    };
    /**
     * 以上为接受silencesms返回信息的广播接受者
     */


    boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(MyApp.getInstance(),
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    1);
            return false;
        }
        return true;
    }

    public void send(){
        if (checkPermissions()) {//android 6.0以上发送短信需要运行时权限

            if(mTvPhone.getText().toString().trim().length()>0){
                /**
                 * 这一句调用了系统sms接口发送短信
                 */
                SmsManager.getDefault().sendDataMessage(mTvPhone.getText().toString().trim(), null, (short) 9200, payload, sentPI, deliveryPI);
                mTvState.setText("等待响应中....");
//                tempDatas.get(checkPosition).setState(0);
            }else {
               ToastUtil.show("此功能需要目标手机号!");
            }

        } else {
            ToastUtil.show("此功能需要读写短信权限!");
        }
    }

    private String getLogBytesHex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("0x%02X ", b));
        }
        return sb.toString();
    }

    public static void refreshLib() {
        adapter.setDatas(tempDatas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(br, sentFilter);
        getActivity().registerReceiver(br, deliveryFilter);
        getActivity().registerReceiver(br, wapDeliveryFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(br);
        super.onPause();
    }
}
