package com.example.chris.atp_music_player.ui

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.example.chris.atp_music_player.*
import com.example.chris.atp_music_player.playback.PlaybackService
import com.example.chris.atp_music_player.playback.PlaybackRequest
import com.example.chris.atp_music_player.playback.PlaybackUpdate
import com.example.chris.atp_music_player.playback.SongUpdate
import kotlinx.android.synthetic.main.now_playing_view.view.*

class NowPlayingWidget : LinearLayout {
    private var canUpdateSeekbar = true
    private var mService: PlaybackService? = null
    private val seekbarHandler = Handler()
    private val updateSeekbar = Runnable { refreshSeekbar() }

    constructor(context: Context) : super(context);
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs);
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle);

    fun setService(service: PlaybackService) {
        this.mService = service
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()
        observe()
    }

    fun observe() {
        SongUpdate.observable().subscribe({ song -> updateNowPlayingView(song) })

        PlaybackUpdate.observable().subscribe { it ->
            when (it) {
                PlaybackUpdateType.PLAYING -> {
                    refreshSeekbar()
                    song_toggle_playback.setImageResource(R.drawable.ic_pause_white_36dp)
                    toggle_playback.setImageResource(R.drawable.ic_pause_white_36dp)
                }
                PlaybackUpdateType.PAUSED -> {
                    seekbarHandler.removeCallbacks(updateSeekbar)
                    song_toggle_playback.setImageResource(R.drawable.ic_play_arrow_white_36dp)
                    toggle_playback.setImageResource(R.drawable.ic_play_arrow_white_36dp)
                }
                PlaybackUpdateType.SHUFFLE_ON -> {
                    shuffle.setImageResource(R.drawable.ic_shuffle_cyan_36dp)
                    repeat.setImageResource(R.drawable.ic_replay_white_36dp)
                }
                PlaybackUpdateType.SHUFFLE_OFF -> {
                    shuffle.setImageResource(R.drawable.ic_shuffle_white_36dp)
                    repeat.setImageResource(R.drawable.ic_replay_white_36dp)
                }
                PlaybackUpdateType.REPEAT_ON -> {
                    repeat.setImageResource(R.drawable.ic_replay_cyan_36dp)
                    shuffle.setImageResource(R.drawable.ic_shuffle_white_36dp)
                }
                PlaybackUpdateType.REPEAT_OFF -> {
                    repeat.setImageResource(R.drawable.ic_replay_white_36dp)
                    shuffle.setImageResource(R.drawable.ic_shuffle_white_36dp)
                }
            }
        }

        forward.setOnClickListener { PlaybackRequest.post(PlaybackType.NEXT) }
        back.setOnClickListener { PlaybackRequest.post(PlaybackType.BACK) }
        shuffle.setOnClickListener { PlaybackRequest.post(PlaybackType.SHUFFLE) }
        repeat.setOnClickListener { PlaybackRequest.post(PlaybackType.REPEAT) }
        song_toggle_playback.setOnClickListener { PlaybackRequest.post(PlaybackType.TOGGLE_PLAYBACK) }
        toggle_playback.setOnClickListener { PlaybackRequest.post(PlaybackType.TOGGLE_PLAYBACK) }

        sliding_layout_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mService!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                canUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                canUpdateSeekbar = true
            }
        })
    }

    fun updateNowPlayingView(song: Song) {
        song_artist.text = song.artist
        song_title.text = song.title
        artist.text = song.artist
        title.text = song.title
        album.text = song.album

        Glide.with(context).load(song.albumArtUri()).into(album_art)
        refreshSeekbar()
    }

    fun refreshSeekbar() {
        if (!canUpdateSeekbar) {
            return
        }

        if (sliding_layout_seekbar.max != mService!!.duration) {
            sliding_layout_seekbar.max = mService!!.duration
        }
        seekbarHandler.removeCallbacks(updateSeekbar)
        sliding_layout_seekbar.progress = mService!!.progress
        seekbarHandler.postDelayed(updateSeekbar, 1000)
    }
}
