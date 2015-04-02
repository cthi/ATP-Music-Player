package com.example.chris.atp_music_player.utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

public class AlbumArtUtils {
    public static Bitmap getAlbumArtFromPath(String albumId, Context context) {
        Uri artworkUri =  Uri.parse("content://media/external/audio/albumart");
        Uri result = ContentUris.withAppendedId(artworkUri, Integer.parseInt(albumId));
        InputStream in;
        try {
            in = context.getContentResolver().openInputStream(result);
            return BitmapFactory.decodeStream(in);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
