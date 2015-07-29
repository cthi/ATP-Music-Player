package com.example.chris.atp_music_player.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.LibraryPagerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LibraryFragment extends Fragment {

    @InjectView(R.id.library_viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.library_tabs)
    PagerSlidingTabStrip mPagerTabs;

    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        ButterKnife.inject(this, view);

        getActivity().setTitle(R.string.menu_lib);

        initPager();
        initPagerTabs();

        return view;
    }

    public void initPager() {
        LibraryPagerAdapter adapter = new LibraryPagerAdapter(getActivity(),
                getChildFragmentManager());

        mViewPager.setAdapter(adapter);
    }

    public void initPagerTabs() {
        if (mPagerTabs != null) {
            mPagerTabs.setBackgroundColor(getResources().getColor(R.color.blue));
            mPagerTabs.setIndicatorHeight(4);
            mPagerTabs.setDividerColor(Color.TRANSPARENT);
            mPagerTabs.setTextColor(Color.WHITE);
            mPagerTabs.setIndicatorColor(Color.WHITE);
            mPagerTabs.setViewPager(mViewPager);
        }
    }
}
