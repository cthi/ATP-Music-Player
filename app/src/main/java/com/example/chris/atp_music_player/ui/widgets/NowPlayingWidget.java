package com.example.chris.atp_music_player.ui.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NowPlayingWidget extends LinearLayout {
    @InjectView(R.id.sliding_layout_song_title)
    TextView mTitle;
    @InjectView(R.id.sliding_layout_song_artist)
    TextView mArtist;
    @InjectView(R.id.sliding_layout_action_ico)
    ImageView mActionImage;
    @InjectView(R.id.sliding_layout_top_artist)
    TextView mTopArtist;
    @InjectView(R.id.sliding_layout_top_title)
    TextView mTopTitle;
    @InjectView(R.id.sliding_layout_top_album)
    TextView mTopAlbum;
    @InjectView(R.id.sliding_layout_top_song_img)
    ImageView mTopImage;

    @InjectView(R.id.sliding_layout_top_repeat)
    ImageView mTopRepeat;
    @InjectView(R.id.sliding_layout_top_back)
    ImageView mTopBack;
    @InjectView(R.id.sliding_layout_top_fwrd)
    ImageView mTopForward;
    @InjectView(R.id.sliding_layout_top_play_pause)
    ImageView mTopPlayPause;
    @InjectView(R.id.sliding_layout_top_shuffle)
    ImageView mTopShuffle;

    @InjectView(R.id.sliding_layout_seekbar)
    SeekBar mSeekbar;

    private volatile boolean canUpdateSeekbar = true;
    private LocalPlaybackService mService;
    private final Handler mSeekbarPosHandler = new Handler();
    private final Runnable mUpdateSeekbarPos = new Runnable() {
        @Override
        public void run() {
            refreshSeekbar();
        }
    };

    public NowPlayingWidget(Context context) {
        super(context);
    }

    public NowPlayingWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NowPlayingWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setService(LocalPlaybackService service) {
        this.mService = service;
    }

    @Override
    public void onFinishInflate() {
        ButterKnife.inject(this);

        initClickListeners();
    }

    public void initClickListeners() {
        mActionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService.isPlaying()) {
                    mService.pause();
                    mSeekbarPosHandler.removeCallbacks(mUpdateSeekbarPos);
                    mActionImage.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    mTopPlayPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                } else {
                    mService.resume();
                    refreshSeekbar();
                    mActionImage.setImageResource(R.drawable.ic_pause_white_36dp);
                    mTopPlayPause.setImageResource(R.drawable.ic_pause_white_36dp);
                }
            }
        });

        mTopPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService.isPlaying()) {
                    mService.pause();
                    mSeekbarPosHandler.removeCallbacks(mUpdateSeekbarPos);
                    mActionImage.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    mTopPlayPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                } else {
                    mService.resume();
                    refreshSeekbar();
                    mActionImage.setImageResource(R.drawable.ic_pause_white_36dp);
                    mTopPlayPause.setImageResource(R.drawable.ic_pause_white_36dp);
                }
            }
        });

        mTopForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mService.playNextSong();
                updateNowPlayingView(mService.getLastSong());
            }
        });

        mTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mService.playLastSong();
                updateNowPlayingView(mService.getLastSong());
            }
        });

        mTopShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService.isShuffling()) {
                    mTopShuffle.setImageResource(R.drawable.ic_shuffle_white_36dp);
                } else {
                    mTopShuffle.setImageResource(R.drawable.ic_shuffle_cyan_36dp);
                }
                mTopRepeat.setImageResource(R.drawable.ic_replay_white_36dp);

                mService.toggleShuffle();
            }
        });

        mTopRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService.isRepeating()) {
                    mTopRepeat.setImageResource(R.drawable.ic_replay_white_36dp);
                } else {
                    mTopRepeat.setImageResource(R.drawable.ic_replay_cyan_36dp);
                }
                mTopShuffle.setImageResource(R.drawable.ic_shuffle_white_36dp);

                mService.toggleRepeat();
            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                canUpdateSeekbar = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                canUpdateSeekbar = true;
            }
        });
    }

    public void updateNowPlayingView(Song song) {
        mArtist.setText(song.getArtist());
        mTitle.setText(song.getTitle());
        mTopArtist.setText(song.getArtist());
        mTopTitle.setText(song.getTitle());
        mTopAlbum.setText(song.getAlbum());

        if (mService.isPlaying()) {
            mActionImage.setImageResource(R.drawable.ic_pause_white_36dp);
            mTopPlayPause.setImageResource(R.drawable.ic_pause_white_36dp);
        } else {
            mActionImage.setImageResource(R.drawable.ic_play_arrow_white_36dp);
            mTopPlayPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
        }

        Glide.with(getContext()).load(AlbumArtUtils.albumArtUriFromId(song.getAlbumId())).into(mTopImage);
        refreshSeekbar();
    }

    public void refreshSeekbar() {
        if (!canUpdateSeekbar) {
            return;
        }

        if (mSeekbar.getMax() != mService.getDuration()) {
            mSeekbar.setMax(mService.getDuration());
        }
        mSeekbarPosHandler.removeCallbacks(mUpdateSeekbarPos);
        mSeekbar.setProgress(mService.getProgress());
        mSeekbarPosHandler.postDelayed(mUpdateSeekbarPos, 1000);
    }
}
