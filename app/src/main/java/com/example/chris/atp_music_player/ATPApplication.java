package com.example.chris.atp_music_player;

import android.app.Application;
import android.content.Intent;

import com.example.chris.atp_music_player.services.LocalPlaybackService;

public class ATPApplication extends Application {
    private static boolean SUB_ACTIVITY_VISIBLE;

    @Override
    public void onCreate() {
        super.onCreate();


        Intent intent = new Intent(this, LocalPlaybackService.class);
        startService(intent);
    }

    public static boolean willSubActivityBeVisible() {
        return SUB_ACTIVITY_VISIBLE;
    }

    public static void subActivityWillBeVisible() {
        SUB_ACTIVITY_VISIBLE = true;
    }

    public static void subActivityWillDissapear() {
        SUB_ACTIVITY_VISIBLE = false;
    }
}
