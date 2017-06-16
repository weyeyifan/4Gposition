package com.position4g.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.position4g.R;
import com.position4g.adapter.CodeHomeAdapter;
import com.position4g.db.Constants;
import com.position4g.db.MemberDao;
import com.position4g.eventbusmessage.CodeMessage;
import com.position4g.model.CodeBean;
import com.position4g.model.MemberBean;
import com.position4g.service.SocketService;
import com.position4g.utils.DataUtils;
import com.position4g.utils.PreferenceUtils;
import com.position4g.utils.ThreadUtils;
import com.position4g.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.position4g.thread.ParamThread2.os2;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class Code_HomeFragment extends Fragment {
    public static CodeHomeAdapter adapter;
    public static boolean scroll =true;
    List<CodeBean>mDatas;
    @BindView(R.id.et_code_home)
    EditText mEtCodeHome;
    @BindView(R.id.btn_search_code_home)
    Button   mBtnSearchCodeHome;
    @BindView(R.id.btn_stop_code_home)
    Button   mBtnStopCodeHome;

    public static TextView mTvSumCodeHome;

    public static TextView mTvMinuteCodeHome;

    public static TextView mTvPeakCodeHome;

    String search;

    public static ListView mLvCodeHome;

    int j=0;

    //定时记录每分钟上号数量
    Handler mHandler =new Handler();
    Runnable sumRunnable;
    int countMinute;
    int peak;
    int beforeSum;
    int totalNum;

    MemberBean bean;

    Map<String,Integer>map=new HashMap<>();
    ArrayList<CodeBean>codeDatas=new ArrayList<>();

    String unix;
    //是否搜索状态
    boolean isSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_code_home, container, false);
        mLvCodeHome= (ListView) view.findViewById(R.id.lv_code_home);
        mTvPeakCodeHome= (TextView) view.findViewById(R.id.tv_peak_code_home);
        mTvMinuteCodeHome= (TextView) view.findViewById(R.id.tv_minute_code_home);
        mTvSumCodeHome= (TextView) view.findViewById(R.id.tv_sum_code_home);
        adapter = new CodeHomeAdapter();
        mLvCodeHome.setAdapter(adapter);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onStart() {
//        adapter.setData(codeDatas);
//        adapter.notifyDataSetChanged();
        mDatas=new ArrayList<>();
        super.onStart();
        mBtnSearchCodeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search=mEtCodeHome.getText().toString().trim();
                if(search.equals("")){
                    adapter.setData(codeDatas);
                    adapter.notifyDataSetChanged();
                    isSearch=false;
                }else{
                    isSearch=true;
                    mDatas.clear();
                    for(int i=0;i<codeDatas.size();i++){
                        if(codeDatas.get(i).getImsi().contains(search)){
                            mDatas.add(codeDatas.get(i));
                            adapter.setData(mDatas);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        mBtnStopCodeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll=!scroll;
                if(scroll){
                    mBtnStopCodeHome.setText("停止");
                    mLvCodeHome.smoothScrollToPosition(adapter.getCount()-1);
                }else{
                    mBtnStopCodeHome.setText("滚动");
                }
            }
        });

        mLvCodeHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean=new MemberBean();
                bean.setName("编号:"+codeDatas.get(position).getCount());
                bean.setImsi(codeDatas.get(position).getImsi());
                if(MemberLibFragment.tempDatas!=null){
                    if(MemberLibFragment.tempDatas.size()>=6){
                        ToastUtil.show("最多添加6名库内人员");
                        return;
                    }else{
                        for(int i=0;i<MemberLibFragment.tempDatas.size();i++){
                            if(MemberLibFragment.tempDatas.get(i).getImsi().equals(bean.getImsi())){
                                return;
                            }
                        }

                        MemberLibFragment.tempDatas.add(bean);
                        if(MemberLibFragment.adapter!=null){
                            MemberLibFragment.adapter.notifyDataSetChanged();
                            ToastUtil.show("入库成功!");
                        }
                        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                            @Override
                            public void run() {
                                MemberDao dao=new MemberDao();
                                dao.add(bean);
                                dao.close();
                                if(PreferenceUtils.getBoolean(Constants.LIANTONGLOCATION,false)){
                                    SocketService.openLocation("FDD");
                                }
                                if(PreferenceUtils.getBoolean(Constants.DIANXINLOCATION,false)){
                                    SocketService.openLocation("FDD");
                                }
                                if(PreferenceUtils.getBoolean(Constants.YIDONGLOCATION,false)){
                                    SocketService.openLocation("TDD");
                                }
                            }
                        });
                    }
                }
            }
        });

        sumRunnable=new Runnable() {
            @Override
            public void run() {
                    countMinute=totalNum-beforeSum;

                if(peak<countMinute){
                    peak=countMinute;
                    mTvPeakCodeHome.setText(peak+"");
                }
                beforeSum+=countMinute;
                mTvMinuteCodeHome.setText(countMinute+"");
                mHandler.postDelayed(this,60*1000);
            }
        };
        mHandler.postDelayed(sumRunnable,60*1000);
    }

    public static void setScroll(){
        if(scroll){
            mLvCodeHome.smoothScrollToPosition(adapter.getCount()-1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Subscribe
    public void onCodeListMessage(final CodeMessage event){

        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {

            @Override
            public void run() {
                unix=String.valueOf(System.currentTimeMillis());
                final  ArrayList<CodeBean>data= DataUtils.getCodeList(event.message);
                if(os2!=null&&data.size()>0){
                    try {
                        os2.flush();
                        String reply = "{\"upresult\":true,\"srcTime\":"+data.get(0).getUptime()+",\"time\":"+unix+"}";
                        PrintWriter pw = new PrintWriter(os2);
                        pw.write(reply);
                        pw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(data.size()>0){
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                if(codeDatas.size()>9000){
                                    for(int k=0;k<3000;k++){
                                        codeDatas.remove(0);
                                    }
                                }
                                for(int i=0;i<data.size();i++) {
                                    if (map.get(data.get(i).getImsi()) != null) {
                                        data.get(i).setCount(map.get(data.get(i).getImsi()));
                                    } else {
                                        data.get(i).setCount(j);
                                        map.put(data.get(i).getImsi(), j);
                                        j++;
                                    }
                                    codeDatas.add(data.get(i));

                                    totalNum++;
                                    if(isSearch){
                                        if(data.get(i).getImsi().contains(search)){
                                            mDatas.add(data.get(i));
                                        }
                                        adapter.setData(mDatas);
                                    }else{
                                        adapter.setData(codeDatas);
                                    }

                                    adapter.notifyDataSetChanged();
                                    setScroll();
                                    mTvSumCodeHome.setText(totalNum + "");
                                }
                            }
                        });

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
