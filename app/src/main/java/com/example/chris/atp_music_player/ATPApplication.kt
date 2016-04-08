package com.example.chris.atp_music_player

import android.app.Application
import android.content.Intent

import com.example.chris.atp_music_player.playback.PlaybackService

class  ATPApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val intent = Intent(this, PlaybackService::class.java)
        startService(intent)
    }

    companion object {
        private var SUB_ACTIVITY_VISIBLE: Boolean = false

        fun willSubActivityBeVisible(): Boolean {
            return SUB_ACTIVITY_VISIBLE
        }

        fun subActivityWillBeVisible() {
            SUB_ACTIVITY_VISIBLE = true
        }

        fun subActivityWillDissapear() {
            SUB_ACTIVITY_VISIBLE = false
        }
    }
}
