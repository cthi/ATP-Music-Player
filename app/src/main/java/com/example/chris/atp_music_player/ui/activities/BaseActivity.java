package com.example.chris.atp_music_player.ui.activities;

import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;

import com.example.chris.atp_music_player.R;

public class BaseActivity extends ActionBarActivity {

    public int getColorPrimary(){
        TypedArray tmp = obtainStyledAttributes(new TypedValue().data, new int[] { R.attr.colorPrimary });
        int color = tmp.getColor(0, 0);
        tmp.recycle();

        return color;
    }
}
