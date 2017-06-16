package com.position4g.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.position4g.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @createAuthor zfb
 * @createTime 2017/3/16${Time}
 * @describe ${TODO}
 */

public class DeviceFragment extends Fragment{
    TabLayout                tab;
    ViewPager            vp;
    FragmentPagerAdapter mFragmentPagerAdapter;
    private String         tabs[]         = {"设备参数", "同步模式与功率"};
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_device,container,false);
        tab = (TabLayout) view.findViewById(R.id.tablayout_device);
        List<String> deviceChannels = Arrays.asList(tabs);
        vp = (ViewPager) view.findViewById(R.id.vp_device);
        mFragmentsList.add(new DeviceParamFragment());
        mFragmentsList.add(new DeviceSyncFragment());
        mFragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentsList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentsList.size();
            }
        };
        vp.setAdapter(mFragmentPagerAdapter);
        vp.setOffscreenPageLimit(1);

        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.addTab(tab.newTab().setText(deviceChannels.get(0)));
        tab.addTab(tab.newTab().setText(deviceChannels.get(1)));

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tab);
        vp.addOnPageChangeListener(listener);
        return view;
    }
}
