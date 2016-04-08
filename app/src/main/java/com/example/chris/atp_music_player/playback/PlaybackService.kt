package com.example.chris.atp_music_player.playback

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.support.v4.app.NotificationCompat.Builder
import android.support.v7.app.NotificationCompat
import android.widget.RemoteViews
import com.example.chris.atp_music_player.*
import com.example.chris.atp_music_player.playback.PlaybackRequest
import com.example.chris.atp_music_player.playback.PlaybackUpdate
import com.example.chris.atp_music_player.playback.SongRequest
import com.example.chris.atp_music_player.playback.SongUpdate
import com.example.chris.atp_music_player.ui.MainActivity
import java.io.IOException
import java.util.*

class PlaybackService : Service(), AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private var playbackState = 0
    private var currentPlaybackPosition = 0
    private var receiverRegistered = false
    private var repeatOn = false
    private var shuffleOn = false
    private var shouldNotAddToRecent = false

    private var musicIntentReceiver: BroadcastReceiver? = null
    private var audioNoisyIntentFilter: IntentFilter? = null
    private var binder: IBinder? = null

    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null

    private var notifManager: NotificationManager? = null
    private var notifView: RemoteViews? = null
    private var notifBuilder: Builder? = null

    private var songList: List<Song> = emptyList()
    var recentSongs: MutableList<Song> = arrayListOf()
    private var songPosition = 0

    inner class LocalBinder : Binder() {
        val service: PlaybackService
            get() = this@PlaybackService
    }

    private inner class MusicIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                pause()
            }
        }
    }

    override fun onCreate() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        binder = LocalBinder()

        audioNoisyIntentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        musicIntentReceiver = MusicIntentReceiver()

        recentSongs = LinkedList<Song>()

        PlaybackRequest.observable().subscribe {
            when (it) {
                PlaybackType.NEXT -> {
                    playNextSong()
                    PlaybackUpdate.post(PlaybackUpdateType.PLAYING)
                }
                PlaybackType.BACK -> {
                    playLastSong()
                    PlaybackUpdate.post(PlaybackUpdateType.PLAYING)
                }
                PlaybackType.TOGGLE_PLAYBACK -> {
                    togglePlayback()
                    if (isPlaying) {
                        PlaybackUpdate.post(PlaybackUpdateType.PLAYING)
                    } else {
                        PlaybackUpdate.post(PlaybackUpdateType.PAUSED)
                    }
                }
                PlaybackType.REPEAT -> {
                    toggleRepeat()
                    if (isRepeating) {
                        PlaybackUpdate.post(PlaybackUpdateType.REPEAT_ON)
                    } else {
                        PlaybackUpdate.post(PlaybackUpdateType.REPEAT_OFF)
                    }
                }
                PlaybackType.SHUFFLE -> {
                    toggleShuffle()
                    if (isShuffling) {
                        PlaybackUpdate.post(PlaybackUpdateType.SHUFFLE_ON)
                    } else {
                        PlaybackUpdate.post(PlaybackUpdateType.SHUFFLE_OFF)
                    }
                }
            }
        }

        SongRequest.observable().subscribe {
            if (it.remember) {
                play(it.songs, it.songPosition)
            } else {
                playDontQueue(it.songs, it.songPosition)
            }
            PlaybackUpdate.post(PlaybackUpdateType.PLAYING)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (null == intent) {
            stopSelf()
            return START_NOT_STICKY
        } else if (intent.action == null) {
            return START_STICKY
        } else if (intent.action == Constants.PLAYBACK_START_FOREGROUND) {
            notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifView = RemoteViews(packageName, R.layout.item_remote_notif)
            notifView!!.setImageViewResource(R.id.item_remote_notif_img, R.mipmap.ic_launcher)
            notifView!!.setTextViewText(R.id.item_remote_notif_title, lastSong!!.title)
            notifView!!.setTextViewText(R.id.item_remote_notif_artist, lastSong!!.artist)
            val openAppIntent = Intent(this, MainActivity::class.java)

            notifBuilder = NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(PendingIntent.getActivity(this, 0, openAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)).setContent(notifView)
            startForeground(FOREGROUND_SERVICE_ID, notifBuilder!!.build())

        } else if (intent.action == Constants.PLAYBACK_STOP_FOREGROUND) {
            stopForeground(true)
            notifView = null
            notifBuilder = null
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = binder

    override fun onDestroy() {
        releaseResources()
        unregisterMusicIntentReceiver()
    }

    private fun play(songList: List<Song>, position: Int) {
        if (requestAudioFocus()) {
            playbackState = STATE_LOADING
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                    mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    mediaPlayer!!.setWakeMode(this.applicationContext,
                            PowerManager.PARTIAL_WAKE_LOCK)
                    mediaPlayer!!.setOnCompletionListener(this)
                    mediaPlayer!!.setOnPreparedListener(this)
                    mediaPlayer!!.setOnErrorListener(this)
                } else {
                    mediaPlayer!!.reset()
                }

                this.songList = songList
                songPosition = position

                if (!shouldNotAddToRecent) {
                    recentSongs.add(songList[songPosition])
                }

                if (recentSongs.size > 20) {
                    recentSongs.removeAt(20)
                }

                mediaPlayer!!.setDataSource(this, Uri.parse(songList[songPosition].mediaLocation))
                mediaPlayer!!.prepareAsync()

                registerMusicIntentReceiver()

                if (null != notifView) {
                    notifView!!.setTextViewText(R.id.item_remote_notif_title, lastSong!!.title)
                    notifView!!.setTextViewText(R.id.item_remote_notif_artist, lastSong!!.artist)

                    notifManager!!.notify(FOREGROUND_SERVICE_ID, notifBuilder!!.build())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            releaseResources()
        }
    }

    private fun playDontQueue(songList: List<Song>, position: Int) {
        shouldNotAddToRecent = true
        play(songList, position)
        shouldNotAddToRecent = false
    }

    // 333
    fun skipToTime(position: Int) {
        if (isPlaying) {
            mediaPlayer!!.seekTo(position)
        }
        currentPlaybackPosition = position
    }

    // 333
    fun seekTo(progress: Int) {
        mediaPlayer!!.seekTo(progress * 1000)
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        playbackState = STATE_PLAYING
        mediaPlayer.start()
        SongUpdate.post(songList[songPosition])
    }

    override fun onError(mediaPlayer: MediaPlayer, i: Int, i2: Int): Boolean {
        return false
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        if (repeatOn) {
            playRepeat()
        } else if (shuffleOn) {
            playRandom()
        } else {
            playNext()
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mediaPlayer!!.setVolume(VOLUME_DUCK, VOLUME_DUCK)
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
            AudioManager.AUDIOFOCUS_LOSS -> stop()
            AudioManager.AUDIOFOCUS_GAIN -> mediaPlayer!!.setVolume(VOLUME_NORMAL, VOLUME_NORMAL)
        }
    }

    private fun releaseResources() {
        if (mediaPlayer != null) {
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    private fun requestAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager!!.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
    }

    private fun releaseAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager!!.abandonAudioFocus(this)
    }

    private fun registerMusicIntentReceiver() {
        if (!receiverRegistered) {
            registerReceiver(musicIntentReceiver, audioNoisyIntentFilter)
            receiverRegistered = true
        }
    }

    private fun unregisterMusicIntentReceiver() {
        if (receiverRegistered) {
            unregisterReceiver(musicIntentReceiver)
            receiverRegistered = false
        }
    }

    private fun playNextSong() {
        if (shuffleOn) {
            playRandom()
        } else if (repeatOn) {
            playRepeat()
        } else {
            playNext()
        }
    }

    private fun playLastSong() {
        if (shuffleOn) {
            playRandom()
        } else if (repeatOn) {
            playRepeat()
        } else {
            playLast()
        }
    }

    private fun playNext() {
        if (songPosition + 1 > songList.size - 1) {
            songPosition = 0
        } else {
            songPosition++
        }

        play(songList, songPosition)
    }

    private fun playLast() {
        if (songPosition - 1 < 0) {
            songPosition = songList.size - 1
        } else {
            songPosition--
        }

        play(songList, songPosition)
    }

    private fun playRepeat() {
        play(songList, songPosition)
    }

    private fun playRandom() {
        play(songList, Random().nextInt(songList.size))
    }

    private fun stop() {
        pause()
        releaseResources()

        playbackState = STATE_STOPPED
        currentPlaybackPosition = 0
    }

    private fun pause() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            currentPlaybackPosition = mediaPlayer!!.currentPosition
        }

        releaseAudioFocus()
        unregisterMusicIntentReceiver()

        playbackState = STATE_PAUSED
    }

    private fun resume() {
        if (requestAudioFocus()) {
            if (mediaPlayer == null && playbackState == STATE_PAUSED) {
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setWakeMode(this.applicationContext,
                        PowerManager.PARTIAL_WAKE_LOCK)
                mediaPlayer!!.setOnPreparedListener(this)
            } else {
                registerMusicIntentReceiver()

                if (playbackState == STATE_PAUSED) {
                    skipToTime(currentPlaybackPosition)
                }

                mediaPlayer!!.start()
            }

            playbackState = STATE_PLAYING
        }
    }

    private fun togglePlayback() {
        if (isPlaying) {
            pause()
        } else {
            resume()
        }
    }

    private fun toggleRepeat() {
        repeatOn = !repeatOn
        shuffleOn = false
    }

    private fun toggleShuffle() {
        shuffleOn = !shuffleOn
        repeatOn = false
    }

    val isShuffling: Boolean
        get() = shuffleOn

    val isRepeating: Boolean
        get() = repeatOn

    val progress: Int
        get() {
            if (null != mediaPlayer && mediaPlayer!!.isPlaying) {
                return mediaPlayer!!.currentPosition / 1000
            }
            return -1
        }

    val duration: Int
        get() {
            if (null != mediaPlayer && mediaPlayer!!.isPlaying) {
                return mediaPlayer!!.duration / 1000
            }
            return -1
        }

    val lastSong: Song?
        get() = if (songList.isEmpty()) null else songList[songPosition]

    val isPlaying: Boolean
        get() = playbackState == STATE_PLAYING || playbackState == STATE_LOADING

    companion object {
        val FOREGROUND_SERVICE_ID = 1
        val VOLUME_DUCK = 0.2f
        val VOLUME_NORMAL = 1.0f

        private val STATE_PAUSED = 0
        private val STATE_PLAYING = 1
        private val STATE_STOPPED = 2
        private val STATE_LOADING = 3
    }
}