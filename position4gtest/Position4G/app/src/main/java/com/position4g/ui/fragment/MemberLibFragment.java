package com.position4g.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

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
 * @describe ${TODO}
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memlib, container, false);
        tempDatas = new ArrayList<>();

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

    }

    public static void refreshLib() {
        adapter.setDatas(tempDatas);
        adapter.notifyDataSetChanged();
    }

}
