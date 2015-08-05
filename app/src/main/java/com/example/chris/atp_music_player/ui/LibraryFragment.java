package com.example.chris.atp_music_player.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.LibraryPagerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LibraryFragment extends Fragment {
    private final String PAGE_NUM = "page_num";

    @InjectView(R.id.library_viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.library_tabs)
    TabLayout mPagerTabs;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ButterKnife.inject(this, view);

        getActivity().setTitle(R.string.menu_lib);

        initPager(savedInstanceState);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_NUM, mViewPager.getCurrentItem());
    }

    public void initPager(Bundle bundle) {
        LibraryPagerAdapter adapter = new LibraryPagerAdapter(getActivity(),
                getChildFragmentManager());

        mViewPager.setAdapter(adapter);

        if (null != bundle) {
            mViewPager.setCurrentItem(bundle.getInt(PAGE_NUM));
        }

        mPagerTabs.setupWithViewPager(mViewPager);
    }
}
