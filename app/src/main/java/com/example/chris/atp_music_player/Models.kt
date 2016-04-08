package com.example.chris.atp_music_player

import io.realm.RealmObject

data class Album(val title: String,
                 val id: Int)

data class Artist(val name: String,
                  val id: Int,
                  val albumId: Int)

data class Genre(val title: String,
                 val id: Int)

data class SongRequestModel(val songs: List<Song>,
                            val songPosition: Int,
                            val remember: Boolean)

open class Song(open var title: String = "",
                open var artist: String = "",
                open var mediaLocation: String = "",
                open var album: String = "",
                open var albumId: Int = 0) : RealmObject() {}

enum class PlaybackType {
    TOGGLE_PLAYBACK, NEXT, BACK, SHUFFLE, REPEAT,
}

enum class PlaybackUpdateType {
    PLAYING, PAUSED, SHUFFLE_ON, SHUFFLE_OFF, REPEAT_ON, REPEAT_OFF
}