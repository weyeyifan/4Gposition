package com.position4g.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.position4g.R;
import com.position4g.adapter.DrawerListAdapter;
import com.position4g.eventbusmessage.DeviceTypeMessage;
import com.position4g.model.DevTypeBean;
import com.position4g.model.ItemModelOfDrawerList;
import com.position4g.service.SocketService;
import com.position4g.ui.fragment.DeviceFragment;
import com.position4g.ui.fragment.Device_HomeFragment;
import com.position4g.ui.fragment.HomeFragment;
import com.position4g.ui.fragment.MemberFragment;
import com.position4g.ui.fragment.PasswordFragment;
import com.position4g.ui.fragment.PositionFragment;
import com.position4g.ui.fragment.ScanFragment;
import com.position4g.ui.fragment.Setting_Fragment;
import com.position4g.ui.fragment.VersionFragment;
import com.position4g.ui.fragment.WarnFragment;
import com.position4g.utils.DataUtils;
import com.position4g.utils.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Toolbar      toolbar;
    DrawerLayout mDrawerLayout;
    LinearLayout ll_drawer;

    private FragmentManager fragmentManager;
    ListView lv_drawer;
    Fragment frHome;//主页
    Fragment frDevice;//设备管理
    Fragment frMember;//人员管理
    Fragment frPosition;//定位状态
    Fragment frWarn;//告警信息
    Fragment frScan;//扫描模式
    Fragment frPassword;//修改密码
    Fragment frSetting;//修改密码
    Fragment frVersion;//版本信息
    private Fragment currentFragment;
    private int                         currentFragmentId = 0;
    private List<ItemModelOfDrawerList> items             = new ArrayList<>();
    DrawerListAdapter mAdapter;

    PowerManager          pm;
    PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "keeplive");
        wl.acquire();
        initView();
        initData();
    }


    private void initView() {
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv_drawer = (ListView) findViewById(R.id.lv_drawer);
        ll_drawer = (LinearLayout) findViewById(R.id.ll_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        frHome = new HomeFragment();
        frDevice = new DeviceFragment();
        frMember = new MemberFragment();
        frPosition = new PositionFragment();
        frWarn = new WarnFragment();
        frScan = new ScanFragment();
        frPassword = new PasswordFragment();
        frSetting = new Setting_Fragment();
        frVersion = new VersionFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ll_content, frHome, "Home")
                .add(R.id.ll_content, frDevice, "Device")
                .add(R.id.ll_content, frMember, "Member")
                .add(R.id.ll_content, frPosition, "Position")
                .add(R.id.ll_content, frWarn, "Warn")
                .add(R.id.ll_content, frScan, "Scan")
                .add(R.id.ll_content, frPassword, "Password")
                .add(R.id.ll_content, frSetting, "Setting")
                .add(R.id.ll_content, frVersion, "Version")
                .hide(frDevice)
                .hide(frMember)
                .hide(frPosition)
                .hide(frWarn)
                .hide(frScan)
                .hide(frPassword)
                .hide(frSetting)
                .hide(frVersion)
                .commit();

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("主页");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mToggle.syncState();
        mDrawerLayout.addDrawerListener(mToggle);
    }

    private void initData() {
        EventBus.getDefault().register(this);
        items.add(new ItemModelOfDrawerList("主页"));
        items.add(new ItemModelOfDrawerList("设备管理"));
        items.add(new ItemModelOfDrawerList("人员管理"));
        items.add(new ItemModelOfDrawerList("定位状态"));
        items.add(new ItemModelOfDrawerList("告警信息"));
        items.add(new ItemModelOfDrawerList("扫描模式"));
        items.add(new ItemModelOfDrawerList("修改密码"));
        items.add(new ItemModelOfDrawerList("设置"));
        items.add(new ItemModelOfDrawerList("版本信息"));
        items.add(new ItemModelOfDrawerList("退出"));

        mAdapter = new DrawerListAdapter(this, items);
        lv_drawer.setAdapter(mAdapter);

        lv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchFragment(position);
                if (position == 4) {
                    mAdapter.setTag(false);
                    mAdapter.notifyDataSetChanged();
                }
                if(position==9){
                    System.exit(0);
                }
            }
        });
        initAddressDb();
    }

    public void setWarnTag() {
        mAdapter.setTag(true);
        mAdapter.notifyDataSetChanged();
    }

    public void initAddressDb() {
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String path = getFilesDir().getAbsolutePath() + "/address.db";
                File file = new File(path);
                InputStream is = null;
                FileOutputStream fos = null;
                if (file.exists()) {
                    return;
                }
                try {
                    is = getResources().getAssets().open("address.db");
                    fos = new FileOutputStream(file);
                    int len = 0;
                    byte[] bf = new byte[4 * 1024];
                    while ((len = is.read(bf)) > 0) {
                        fos.write(bf, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        Intent intent1 = new Intent(this, SocketService.class);
        stopService(intent1);
        wl.release();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitleTextColor(Color.BLACK);
        currentFragment = fragmentManager.findFragmentByTag("Home");
    }

    /**
     * 根据点击左侧drawerlayout的条目位置,打开对应的页面
     */
    private void switchFragment(int position) {
        mDrawerLayout.closeDrawer(ll_drawer);//点击后关闭左侧抽屉
        if (currentFragmentId == position) {
            return;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment toFragment = null;
        switch (position) {
            case 0:
                toolbar.setTitle("首页");
                toFragment = fragmentManager.findFragmentByTag("Home");
                break;
            case 1:
                toFragment = fragmentManager.findFragmentByTag("Device");
                toolbar.setTitle("设备管理");
                break;
            case 2:
                toFragment = fragmentManager.findFragmentByTag("Member");
                toolbar.setTitle("人员管理");
                break;
            case 3:
                toFragment = fragmentManager.findFragmentByTag("Position");
                toolbar.setTitle("定位状态");
                break;
            case 4:
                toFragment = fragmentManager.findFragmentByTag("Warn");
                toolbar.setTitle("告警信息");
                break;
            case 5:
                toFragment = fragmentManager.findFragmentByTag("Scan");
                toolbar.setTitle("扫描模式");
                break;
            case 6:
                toFragment = fragmentManager.findFragmentByTag("Password");
                toolbar.setTitle("修改密码");
                break;
            case 7:
                toFragment = fragmentManager.findFragmentByTag("Setting");
                toolbar.setTitle("设置");
                break;
            case 8:
                toFragment = fragmentManager.findFragmentByTag("Version");
                toolbar.setTitle("版本信息");
                break;
            case 9:
                toolbar.setTitle("退出");
                break;
        }
        ft.hide(currentFragment).show(toFragment).commit();
        currentFragment = toFragment;

        currentFragmentId = position;
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(ll_drawer)) {
            mDrawerLayout.closeDrawer(ll_drawer);
            return;
        }

        //        long now = System.currentTimeMillis();
        //        if ((now - firstPressTime) > 2000) {
        //            ToastUtil.show( "再按一次退出程序");
        //            firstPressTime = now;
        //        } else {
        //            Intent intent1 = new Intent(this, SocketService.class);
        //            stopService(intent1);
        //            finish();
        //            System.exit(0);
        //        }
        moveTaskToBack(false);
    }

    @Subscribe
    public void onDevTypeMessage(final DeviceTypeMessage event) {
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<DevTypeBean> data = DataUtils.getDeviceTypeBean(event.message);
                if (data.size() > 0) {
                    DevTypeBean bean = data.get(data.size() - 1);
                    Device_HomeFragment.setDevBean(bean);
                }
            }
        });
    }
}
