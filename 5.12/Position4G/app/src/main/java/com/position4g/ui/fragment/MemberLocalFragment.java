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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.position4g.R;
import com.position4g.adapter.MemLocalAdapter;
import com.position4g.db.Constants;
import com.position4g.db.MemberDao;
import com.position4g.model.MemberBean;
import com.position4g.service.SocketService;
import com.position4g.ui.dialog.AddMemberDialog;
import com.position4g.utils.PreferenceUtils;
import com.position4g.utils.ThreadUtils;
import com.position4g.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @createAuthor zfb
 * @createTime 2017/3/17${Time}
 * @describe ${TODO}
 */

public class MemberLocalFragment extends Fragment {

    public static ArrayList<MemberBean> mControlDatas;
    public static MemLocalAdapter       adapter;
    public static ArrayList<MemberBean> tempDatas;
    public static ArrayList<MemberBean> tempSearchDatas;

    String search;

    @BindView(R.id.et_member)
    EditText             mEtMember;
    @BindView(R.id.btn_search_member)
    Button               mBtnSearchMember;
    @BindView(R.id.lv_memlocal_fragment)
    ListView             mLvMemlocalFragment;
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
    @BindView(R.id.pb)
    ProgressBar          mPb;

    MemberDao mDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memlocal, container, false);
        tempDatas = new ArrayList<>();
        tempSearchDatas = new ArrayList<>();
        ButterKnife.bind(this, view);
        mFbtnMemlocal.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDao = new MemberDao();
        mPb.setVisibility(View.VISIBLE);
        adapter = new MemLocalAdapter();
        mLvMemlocalFragment.setAdapter(adapter);
        //从数据库读取数据
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                tempDatas = mDao.queryAllLocalMember();
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
                ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < tempDatas.size(); i++) {
                            if (tempDatas.get(i).isChecked) {

                                final String imsi = tempDatas.get(i).getImsi();
                                tempDatas.remove(i);
                                --i;
                                mDao.delete(imsi);
                            }
                        }
                        mDao.close();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < tempDatas.size(); i++) {
                                    tempDatas.get(i).setChecked(false);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });

        mBtnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDao = new MemberDao();
                mControlDatas = new ArrayList<>();
                mControlDatas.addAll(tempDatas);
                for (int i = 0; i < mControlDatas.size(); i++) {
                    if (mControlDatas.get(i).isChecked) {
                        for (int j = 0; j < MemberLibFragment.tempDatas.size(); j++) {
                            if (MemberLibFragment.tempDatas.get(j).getImsi().equals(mControlDatas.get(i).getImsi())) {
                                mControlDatas.remove(i);
                                i--;
                                break;
                            }
                        }
                    } else {
                        mControlDatas.remove(i);
                        i--;
                    }
                }
                ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        if ((MemberLibFragment.tempDatas.size() + mControlDatas.size()) > 6) {
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show("库内人员上限6名");
                                }
                            });

                        } else {

                            mDao.addAll(mControlDatas);
                            if (PreferenceUtils.getBoolean(Constants.LIANTONGLOCATION, false)) {
                                SocketService.openLocation("FDD");
                            }
                            if (PreferenceUtils.getBoolean(Constants.DIANXINLOCATION, false)) {
                                SocketService.openLocation("FDD");
                            }
                            if (PreferenceUtils.getBoolean(Constants.YIDONGLOCATION, false)) {
                                SocketService.openLocation("TDD");
                            }
                            mDao.close();
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    MemberLibFragment.tempDatas.addAll(mControlDatas);
                                    MemberLibFragment.refreshLib();
                                    ToastUtil.show("入库成功!");

                                }
                            });
                        }
                    }
                });


            }
        });

        mLvMemlocalFragment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

        mFbtnMemlocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddMemberDialog dialog = new AddMemberDialog();
                dialog.setCancelable(false);
                dialog.show(fm, "");
            }
        });

        mBtnSearchMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = mEtMember.getText().toString().trim();
                if (search.equals("")) {
                    adapter.setDatas(tempDatas);
                    adapter.notifyDataSetChanged();
                } else {
                    tempSearchDatas.clear();
                    for (int i = 0; i < tempDatas.size(); i++) {
                        if (tempDatas.get(i).getImsi().contains(search)) {
                            tempSearchDatas.add(tempDatas.get(i));
                            adapter.setDatas(tempSearchDatas);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    public static void refreshLib() {
        adapter.setDatas(tempDatas);
        adapter.notifyDataSetChanged();
    }
}
