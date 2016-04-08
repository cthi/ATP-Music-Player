package com.example.chris.atp_music_player.playback

import com.example.chris.atp_music_player.PlaybackType
import com.example.chris.atp_music_player.PlaybackUpdateType
import com.example.chris.atp_music_player.Song
import com.example.chris.atp_music_player.SongRequestModel
import rx.Observable
import rx.subjects.PublishSubject

object SongUpdate {
    private val publishSubject: PublishSubject<Song> = PublishSubject.create()
    fun post(song: Song) = publishSubject.onNext(song)
    fun observable(): Observable<Song> = publishSubject
}

object SongRequest {
    private val publishSubject: PublishSubject<SongRequestModel> = PublishSubject.create()
    fun post(request: SongRequestModel) = publishSubject.onNext(request)
    fun observable(): Observable<SongRequestModel> = publishSubject
}

object PlaybackRequest {
    private val publishSubject: PublishSubject<PlaybackType> = PublishSubject.create()
    fun post(request: PlaybackType) = publishSubject.onNext(request)
    fun observable(): Observable<PlaybackType> = publishSubject
}

object PlaybackUpdate {
    private val publishSubject: PublishSubject<PlaybackUpdateType> = PublishSubject.create()
    fun post(update: PlaybackUpdateType) = publishSubject.onNext(update)
    fun observable(): Observable<PlaybackUpdateType> = publishSubject
}