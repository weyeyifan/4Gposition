package com.position4g.ui.fragment;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.adapter.TargetHomeAdapter;
import com.position4g.db.Constants;
import com.position4g.eventbusmessage.TargetListMessage;
import com.position4g.model.TargetBean;
import com.position4g.utils.DataUtils;
import com.position4g.utils.PreferenceUtils;
import com.position4g.utils.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.Typeface.DEFAULT_BOLD;
import static com.position4g.ui.fragment.HomeFragment.codeDatas;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class Target_HomeFragment extends Fragment {
    long time = System.currentTimeMillis();
    public static ArrayList<TargetBean> mDatas;
    static int selectedPosition = 0;
    @BindView(R.id.barchat)
    BarChart mBarChat;
    @BindView(R.id.tv_QCellCore_target_home)
    TextView mTvQCellCoreTargetHome;
    @BindView(R.id.tv_imsi_target_home)
    TextView mTvImsiTargetHome;
    @BindView(R.id.lv_target)
    ListView mLvTarget;

    TargetHomeAdapter mAdapter;

    String IMSI;

    List<BarEntry> value1 = new ArrayList<>();
    List<BarEntry> value2 = new ArrayList<>();

    BarDataSet set1;
    BarDataSet set2;

    Boolean selected = false;

    private XAxis xAxis; //X坐标轴
    private YAxis yAxis1; //Y坐标轴
    private YAxis yAxis2; //Y坐标轴
    BarData data;

    Float rsrp;
    Float delay;

    public static SimpleDateFormat format;

    int xLabel = 1;

    boolean itemChanged = false;
    //音频池
    SoundPool           mPool;
    //缓存声音文件id
    Map<Float, Integer> map;
    int                 poolID;

    public static boolean voiceOn;
    boolean voiceComplete = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        View view = inflater.inflate(R.layout.fragment_target_home, container, false);
        ButterKnife.bind(this, view);
        mDatas = new ArrayList<>();
        mAdapter = new TargetHomeAdapter();
        mLvTarget.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                mPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

                map = new HashMap<>();
                Field[] fields = R.raw.class.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    try {
                        int id = fields[i].getInt(R.raw.class);
                        String name = getResources().getResourceName(id);
                        name = name.substring(name.length() - 3);
                        Float num = Float.parseFloat(name);
                        poolID = mPool.load(MyApp.getInstance(), id, 1);
                        map.put(num, poolID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                mPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        //                        mPool.play(v01,1,1,0,0,1);
                        voiceOn = PreferenceUtils.getBoolean(Constants.VOICEON, true);
                        voiceComplete = true;

                    }
                });
            }
        });


        Description description = new Description();
        description.setText("");
        xAxis = mBarChat.getXAxis();
        yAxis1 = mBarChat.getAxisLeft();
        yAxis2 = mBarChat.getAxisRight();

        xAxis.setAxisMinimum(-0.5f);

        yAxis1.setAxisMinimum(0);
        yAxis1.setAxisMaximum(120);
        yAxis2.setAxisMinimum(0);
        yAxis2.setAxisMaximum(120);
        //        xAxis.setXOffset(0.3f);

        //        yAxis.setSpaceTop(120);
        xAxis.setEnabled(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X轴在下边

        xAxis.setDrawGridLines(false);//不要竖线网格

        mBarChat.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        mBarChat.setDescription(description);
        //        mBarChat.setTouchEnabled(false);
        mBarChat.setNoDataText("等待数据中");
        mBarChat.setNoDataTextColor(Color.RED);
        mBarChat.setNoDataTextTypeface(DEFAULT_BOLD);
        mBarChat.setDrawGridBackground(true);
        mBarChat.setDrawBorders(true);
        mBarChat.setTouchEnabled(false);


        //notifydatachannged,invalidate必须和此段代码在一起
        if (mBarChat.getData() != null && mBarChat.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChat.getData().getDataSetByIndex(0);
            set1.setValues(value1);
            set2 = (BarDataSet) mBarChat.getData().getDataSetByIndex(1);
            set2.setValues(value2);
            xAxis.setAxisMinimum(-0.5f);
            List<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);

            data = new BarData(dataSets);
            data.setBarWidth(0.1F);
            data.groupBars(0.3f, 0.25f, 0.05f);

            //            mBarChat.getData().notifyDataChanged();
            mBarChat.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(value1, "场强");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.BLUE);
            set1.setValueTextSize(6f);

            set2 = new BarDataSet(value2, "时延");
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.RED);
            set2.setValueTextSize(6f);

            //                    BarEntry barEntry11 = new BarEntry(1, 0 + 2);
            //                    BarEntry barEntry12 = new BarEntry(2, 0 + 3);
            //                    BarEntry barEntry13 = new BarEntry(3, 0 + 4);
            //
            //                    BarEntry barEntry21 = new BarEntry(1, 0 + 4);
            //                    BarEntry barEntry22 = new BarEntry(2, 0 + 3);
            //                    BarEntry barEntry23 = new BarEntry(3, 0 + 2);
            //
            //                    set1.addEntry(barEntry11);
            //                    set1.addEntry(barEntry12);
            //                    set1.addEntry(barEntry13);
            //
            //                    set2.addEntry(barEntry21);
            //                    set2.addEntry(barEntry22);
            //                    set2.addEntry(barEntry23);

            List<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);

            data = new BarData(dataSets);
            data.setBarWidth(0.1F);
            data.groupBars(0.3f, 0.25f, 0.05f);
            xAxis.setAxisMinimum(-0.5f);
            mBarChat.setData(data);
            mBarChat.notifyDataSetChanged();
        }

        mLvTarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = true;
                if (selectedPosition == position) {
                    itemChanged = false;
                } else {
                    itemChanged = true;
                }
                if (itemChanged) {
                    mBarChat.setData(null);
                    selectedPosition = position;
                    IMSI = mDatas.get(selectedPosition).getImsi();
                    mTvQCellCoreTargetHome.setText(DataUtils.getOpera(IMSI));
                    mTvImsiTargetHome.setText(IMSI);
                    mBarChat.clear();
                    itemChanged = false;
                    data.groupBars(0.3f, 0.25f, 0.05f);
                    //                    mBarChat.notifyDataSetChanged();
                    //                    mBarChat.invalidate();
                }
            }
        });

    }

    public void updateData(Float f1, Float f2) {


        BarEntry barEntry1 = new BarEntry(xLabel, f1);
        BarEntry barEntry2 = new BarEntry(xLabel, f2);
        xLabel++;

        if (mBarChat.getData() != null && mBarChat.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChat.getData().getDataSetByIndex(0);
            set1.setBarBorderWidth(0.1f);
            set1.setValueTextSize(6f);
            set2 = (BarDataSet) mBarChat.getData().getDataSetByIndex(1);
            set2.setBarBorderWidth(0.1f);
            set2.setValueTextSize(6f);
            //            value1.add(barEntry1);
            //            value2.add(barEntry2);
            if (set1.getEntryCount() > 8) {
                set1.removeEntry(0);
                set2.removeEntry(0);
            }
            set1.addEntry(barEntry1);
            set2.addEntry(barEntry2);

            List<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);

            data = new BarData(dataSets);
            data.groupBars(0.3f, 0.25f, 0.05f);
            mBarChat.setData(data);
            if (!PreferenceUtils.getBoolean(Constants.SHOWDELAY, true)) {
                set2.setVisible(false);
            }else {
                set2.setVisible(true);
            }
            mBarChat.setVisibleXRangeMaximum(20);
            mBarChat.setVisibleXRangeMinimum(20);
            xAxis.setAxisMinimum(-0.5f);
            mBarChat.getData().notifyDataChanged();

        } else {
            set1.clear();
            set2.clear();
            value1.clear();
            value2.clear();
            data.clearValues();
            set1 = new BarDataSet(value1, "场强");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.BLUE);
            set1.setBarBorderWidth(0.1f);
            set1.setValueTextSize(6f);
            set1.addEntry(barEntry1);

            set2 = new BarDataSet(value2, "时延");
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.RED);
            set2.setBarBorderWidth(0.1f);
            set2.setValueTextSize(6f);
            set2.addEntry(barEntry2);

            List<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);

            data = new BarData(dataSets);
            //            data.setBarWidth(0.2F);
            data.groupBars(0.3f, 0.25f, 0.05f);
            mBarChat.setData(data);
            mBarChat.setVisibleXRangeMaximum(20);

            xAxis.setAxisMinimum(-0.5f);
        }

        //        qCellCore=DataUtils.getOpera(imsi);

        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                //                mTvImsiTargetHome.setText(imsi);
                //                mTvQCellCoreTargetHome.setText(qCellCore);
                //                mBarChat.moveViewToX(data.getDataSetCount());
                mBarChat.notifyDataSetChanged();
                mBarChat.invalidate();
                //                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Subscribe
    public void onTargetListMessage(final TargetListMessage event) {

        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            boolean tag = false;

            @Override
            public void run() {
                //更新列表

                final ArrayList<TargetBean> data = DataUtils.getTargetList(event.message);
                if (data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        for (int j = 0; j < mDatas.size(); j++) {
                            tag = false;
                            if (mDatas.get(j) != null && mDatas.get(j).getImsi().equals(data.get(i).getImsi())) {
                                data.get(i).setCount(j);
                                mDatas.set(j, data.get(i));
                                tag = true;
                                break;
                            }
                        }
                        if (tag == false) {
                            data.get(i).setCount(mDatas.size());
                            mDatas.add(data.get(i));
                            tag = true;
                            IMSI = mDatas.get(0).getImsi();
                        }
                        //
                        if (data.get(i).getImsi().equals(IMSI)) {
                            rsrp = data.get(i).getRsrp();
                            delay = Float.parseFloat(data.get(i).getDelay());
                            updateData(rsrp, delay);
                        }
                        data.get(i).setTime(format.format(new Date()));
                        if (codeDatas.size() > 10000) {
                            for (int k = 0; k < 2000; k++) {
                                codeDatas.remove(0);
                            }
                        }
                        codeDatas.add(data.get(i));
                        if (voiceOn && voiceComplete && data.get(i).getImsi().equals(IMSI)) {
                            //                            if(map.get(data.get(i).getRsrp())!=null){
                            //                                mPool.play(map.get(data.get(i).getRsrp()),1,1,0,0,1);
                            //                            }
                            float j = data.get(i).getRsrp();
                            while (j > 0) {
                                if (map.get(j) != null) {
                                    mPool.play(map.get(j), 1, 1, 0, 0, 1);
                                    break;
                                }
                                j--;
                            }
                        }
                    }
                    if (mDatas.size() == 1) {
                        IMSI = mDatas.get(0).getImsi();
                    }

                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setDatas(mDatas);
                            mAdapter.notifyDataSetChanged();
//                            Code_HomeFragment.adapter.notifyDataSetChanged();
//                            Code_HomeFragment.setScroll();
                            mTvImsiTargetHome.setText(IMSI);
                            mTvQCellCoreTargetHome.setText(DataUtils.getOpera(IMSI));
                        }
                    });

                }
            }
        });
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mPool.release();
        super.onDestroy();
    }
}
