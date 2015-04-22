package com.example.chris.atp_music_player.utils;

import android.content.Context;

public class ResourceUtils {
    public static int getDrawableResourceId(String name, Context context) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
