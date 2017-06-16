package com.position4g.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.position4g.R;
import com.position4g.adapter.DeviceHomeAdapter;
import com.position4g.db.Constants;
import com.position4g.eventbusmessage.DevSyncMessage;
import com.position4g.eventbusmessage.ResultMessage;
import com.position4g.model.BBuBean;
import com.position4g.model.DevTypeBean;
import com.position4g.model.PaTemperBean;
import com.position4g.model.ResultBean;
import com.position4g.model.SyncBean;
import com.position4g.service.SocketService;
import com.position4g.utils.DataUtils;
import com.position4g.utils.PreferenceUtils;
import com.position4g.utils.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class Device_HomeFragment extends Fragment {
    ListView           mListView;
    List<PaTemperBean> paTempdatas;
    List<BBuBean>      bbudatas;
    public static List<SyncBean>         mDatas;
    public static Map<Integer, SyncBean> tempDatas;
    static        DevTypeBean            mBean;
    Button btn1;
    static Button btn2;
    static Button btn3;
    static Button btn4;
    boolean btn1On = true;
    boolean btn2On = true;
    boolean btn3On = true;
    static boolean btn4On = true;
    Map<Integer, String> bbuCount;
    Map<String, Integer> syncNum;
    DeviceHomeAdapter    mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDatas = new ArrayList<>();
        bbudatas = new ArrayList<>();
        paTempdatas = new ArrayList<>();
        bbuCount = new HashMap<>();
        syncNum = new HashMap<>();
        tempDatas = new HashMap<>();
        View view = inflater.inflate(R.layout.fragment_device_home, container, false);
        mListView = (ListView) view.findViewById(R.id.lv_device);
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        btn3 = (Button) view.findViewById(R.id.btn3);
        btn4 = (Button) view.findViewById(R.id.btn4);
        DeviceHomeAdapter adapter = new DeviceHomeAdapter();
        mListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        mAdapter = new DeviceHomeAdapter();
        mListView.setAdapter(mAdapter);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn1On) {
                    SocketService.openLocation("TDD");
                    btn1.setText("关闭移动");
                    PreferenceUtils.putBoolean(Constants.YIDONGLOCATION,true);
                } else {
                    SocketService.closeLocation("TDD");
                    btn1.setText("开启移动");
                    PreferenceUtils.putBoolean(Constants.YIDONGLOCATION,false);
                }
                btn1On = !btn1On;
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn2On) {
                    SocketService.openLocation("FDD");
                    btn2.setText("关闭电信");
                    PreferenceUtils.putBoolean(Constants.DIANXINLOCATION,true);
                } else {
                    SocketService.closeLocation("FDD");
                    btn2.setText("开启电信");
                    PreferenceUtils.putBoolean(Constants.DIANXINLOCATION,false);
                }
                btn2On = !btn2On;
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn3On) {
                    SocketService.openLocation("FDD");
                    btn3.setText("关闭联通");
                    PreferenceUtils.putBoolean(Constants.LIANTONGLOCATION,true);
                } else {
                    SocketService.closeLocation("FDD");
                    btn3.setText("开启联通");
                    PreferenceUtils.putBoolean(Constants.LIANTONGLOCATION,false);
                }
                btn3On = !btn3On;
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn4On) {
                    //切换至电信
                    SocketService.changeLocation(9);
                    //                    btn4.setText("切换联通");
                } else {
                    //切换至联通
                    SocketService.changeLocation(8);
                    //                    btn4.setText("切换电信");
                }
                //                btn4On=!btn4On;
            }
        });
    }

    @Subscribe
    public void onSyncMessage(final DevSyncMessage event) {
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            List<BBuBean> tempbbudatas = new ArrayList<BBuBean>();
            List<PaTemperBean> temppaTempdatas = new ArrayList<PaTemperBean>();
            boolean tag = false;

            @Override
            public void run() {
                tempbbudatas.clear();
                tempbbudatas.addAll(DataUtils.getBBUBean(event.message));
                if (tempbbudatas.size() > 0) {
                    bbudatas.clear();
                    bbudatas.addAll(tempbbudatas);
                }
                temppaTempdatas.clear();
                temppaTempdatas.addAll(DataUtils.getPaTemBean(event.message));
                if (temppaTempdatas.size() > 0) {
                    paTempdatas.clear();
                    paTempdatas.addAll(temppaTempdatas);
                }
                if (bbudatas.size() > 0) {
                    if (paTempdatas.size() > 0) {
                        BBuBean.BbuStatusBean mBean = bbudatas.get(bbudatas.size() - 1).getBbuStatus();
                        List<PaTemperBean.SyncInfoBean> syncList = paTempdatas.get(paTempdatas.size() - 1).getSyncInfo();
                        if (mBean.getBbu1() != null) {
                            bbuCount.put(1, mBean.getBbu1());
                        }
                        if (mBean.getBbu2() != null) {
                            bbuCount.put(2, mBean.getBbu2());
                        }
                        if (mBean.getBbu3() != null) {
                            bbuCount.put(3, mBean.getBbu3());
                        }
                        if (mBean.getBbu6() != null) {
                            bbuCount.put(6, mBean.getBbu6());
                        }
                        if (mBean.getBbu7() != null) {
                            bbuCount.put(7, mBean.getBbu7());
                        }

                        for (int j = 0; j < syncList.size(); j++) {
                            if (syncList.get(j).getCellNumber().equals("1")) {
                                syncNum.put("1", syncList.get(j).getSyncstatus());
                            }
                            if (syncList.get(j).getCellNumber().equals("2")) {
                                syncNum.put("2", syncList.get(j).getSyncstatus());
                            }
                            if (syncList.get(j).getCellNumber().equals("3")) {
                                syncNum.put("3", syncList.get(j).getSyncstatus());
                            }
                            if (syncList.get(j).getCellNumber().equals("6")) {
                                syncNum.put("6", syncList.get(j).getSyncstatus());
                            }
                            if (syncList.get(j).getCellNumber().equals("7")) {
                                syncNum.put("7", syncList.get(j).getSyncstatus());
                            }
                        }

                        if (bbuCount.get(1) != null) {
                            SyncBean syncBean = new SyncBean();
                            syncBean.setBbu("B38");
                            syncBean.setBbuStatus(mBean.getBbu1());
                            syncBean.setLocation(mBean.getBbu1());
                            syncBean.setSyncstatus(syncNum.get("1"));
                            tempDatas.put(1, syncBean);
                        }
                        if (bbuCount.get(2) != null) {
                            SyncBean syncBean = new SyncBean();
                            syncBean.setBbu("B39");
                            syncBean.setBbuStatus(mBean.getBbu2());
                            syncBean.setLocation(mBean.getBbu2());
                            syncBean.setSyncstatus(syncNum.get("2"));
                            tempDatas.put(2, syncBean);
                        }
                        if (bbuCount.get(3) != null) {
                            SyncBean syncBean = new SyncBean();
                            syncBean.setBbu("B40");
                            syncBean.setBbuStatus(mBean.getBbu3());
                            syncBean.setLocation(mBean.getBbu3());
                            syncBean.setSyncstatus(syncNum.get("3"));
                            tempDatas.put(3, syncBean);
                        }
                        if (bbuCount.get(6) != null) {
                            SyncBean syncBean = new SyncBean();
                            syncBean.setBbu("B1");
                            syncBean.setBbuStatus(mBean.getBbu6());
                            syncBean.setLocation(mBean.getBbu6());
                            syncBean.setSyncstatus(syncNum.get("6"));
                            tempDatas.put(6, syncBean);
                        }
                        if (bbuCount.get(7) != null) {
                            SyncBean syncBean = new SyncBean();
                            syncBean.setBbu("B3");
                            syncBean.setBbuStatus(mBean.getBbu7());
                            syncBean.setLocation(mBean.getBbu7());
                            syncBean.setSyncstatus(syncNum.get("7"));
                            tempDatas.put(7, syncBean);
                        }
                        mDatas.clear();
                        if (tempDatas.get(1) != null) {
                            mDatas.add(tempDatas.get(1));
                        }
                        if (tempDatas.get(2) != null) {
                            mDatas.add(tempDatas.get(2));
                        }
                        if (tempDatas.get(3) != null) {
                            mDatas.add(tempDatas.get(3));
                        }
                        if (tempDatas.get(6) != null) {
                            mDatas.add(tempDatas.get(6));
                        }
                        if (tempDatas.get(7) != null) {
                            mDatas.add(tempDatas.get(7));
                        }


                    }

                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setDatas(mDatas);
                            mAdapter.notifyDataSetInvalidated();
                        }
                    });
                }

            }
        });
    }

    @Subscribe
    public void onResultMessage(final ResultMessage event) {
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<ResultBean> list = DataUtils.getResult(event.message);
                if (list.size() > 0) {
                    ResultBean bean = list.get(0);
                    if (bean.getCode().equals(4615)) {
                        switch (bean.getResult()) {
                            case "4176":
                            case "4177":
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (btn4On) {
                                            btn4.setText("切换联通");
                                            btn2.setClickable(true);
                                            btn2.setBackgroundResource(R.drawable.button);
                                            btn3.setClickable(false);
                                            btn3.setBackgroundResource(R.drawable.button_unclick);
                                        } else {
                                            btn4.setText("切换电信");
                                            btn2.setClickable(false);
                                            btn2.setBackgroundResource(R.drawable.button_unclick);
                                            btn3.setClickable(true);
                                            btn3.setBackgroundResource(R.drawable.button);
                                        }
                                        btn4On = !btn4On;
                                    }
                                });
                                break;
                            case "4178":
                                break;
                        }
                    }
                }
            }
        });
    }


    public static void setDevBean(DevTypeBean bean) {
        mBean = bean;
        final int type = bean.getDevType();
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (type == 8) {
                    btn2.setClickable(false);
                    btn2.setBackgroundResource(R.drawable.button_unclick);
                    btn3.setClickable(true);
                    btn3.setBackgroundResource(R.drawable.button);
                    btn4.setText("切换电信");
                    btn4On = true;
                } else if (type == 9) {
                    btn3.setClickable(false);
                    btn3.setBackgroundResource(R.drawable.button_unclick);
                    btn2.setClickable(true);
                    btn2.setBackgroundResource(R.drawable.button);
                    btn4.setText("切换联通");
                    btn4On = false;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
