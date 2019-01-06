package com.softnep.radiodailymail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.adapter.HomePagerAdapter;


/**
 * Created by ADMIN on 2017-11-07.
 */

public class Fragment_Home extends Fragment {

    View rootView;
    ViewPager viewPager;
    HomePagerAdapter home;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_home,container,false);

        viewPager=(ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout=(TabLayout) rootView.findViewById(R.id.tabs);

      //  home=new HomePagerAdapter(getFragmentManager());
        home=new HomePagerAdapter(getChildFragmentManager());
        home.addFragment(new Fragment_Pradesh7News(),getString(R.string.pradesh7news_nepali));
        home.addFragment(new Fragment_NationalNews(),getString(R.string.nationalnews));
        home.addFragment(new Fragment_InternationalNews(),getString(R.string.internationalnews));
        home.addFragment(new Fragment_PoliticalNews(),getString(R.string.politicalnews));
        home.addFragment(new Fragment_EntertainmentNews(),getString(R.string.entertainmentnews));
        home.addFragment(new Fragment_EconomicNews(),getString(R.string.economicnews));
        viewPager.setAdapter(home);
        viewPager.setOffscreenPageLimit(1);

        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }
}
