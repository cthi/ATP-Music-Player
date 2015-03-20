package com.example.chris.atp_music_player.ui.activities;

import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;

import com.example.chris.atp_music_player.R;

public class BaseActivity extends ActionBarActivity {

    public int getColorPrimary(){

        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary,new TypedValue(), true);
        return value.data;
    }
}
