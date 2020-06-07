package com.ashtray.movie360.manager;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageManager {

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        Log.d("[gp]", "length = " + byteFormat.length);

        // Get the Base64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }
}
