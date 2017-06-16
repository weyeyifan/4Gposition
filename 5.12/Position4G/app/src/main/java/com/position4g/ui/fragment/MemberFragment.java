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
import java.util.List;

/**
 * @createAuthor zfb
 * @createTime 2017/3/16${Time}
 * @describe ${TODO}
 */

public class MemberFragment extends Fragment{
    TabLayout            tab;
    ViewPager            vp;
    FragmentPagerAdapter mFragmentPagerAdapter;
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_member,container,false);
        tab = (TabLayout) view.findViewById(R.id.tablayout_member);
        vp = (ViewPager) view.findViewById(R.id.vp_member);
        mFragmentsList.add(new MemberLibFragment());
        mFragmentsList.add(new MemberLocalFragment());
        mFragmentPagerAdapter=new FragmentPagerAdapter(getChildFragmentManager()) {
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
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.addTab(tab.newTab().setText("库内人员"));
        tab.addTab(tab.newTab().setText("本地人员"));
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
