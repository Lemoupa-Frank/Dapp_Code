package com.example.dvote.fabric_gateway.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    public static byte[] getBytesFromImageUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Unable to open InputStream for URI: " + uri);
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 150, 150);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Unable to open InputStream for URI: " + uri);
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    public static Byte[] convertToByteObjectArray(byte[] byteArray) {
        // Create a Byte[] array of the same length as the byte[] array
        Byte[] byteObjectArray = new Byte[byteArray.length];

        // Iterate through the byte[] array and convert each byte to Byte
        for (int i = 0; i < byteArray.length; i++) {
            byteObjectArray[i] = byteArray[i]; // Autoboxing
        }

        return byteObjectArray;
    }

    public static Bitmap byteObjectArrayToBitmap(Byte[] byteObjectArray) {
        if (byteObjectArray == null || byteObjectArray.length == 0) {
            return null;
        }

        byte[] byteArray = new byte[byteObjectArray.length];
        for (int i = 0; i < byteObjectArray.length; i++) {
            byteArray[i] = byteObjectArray[i]; // Unboxing
        }

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * Converts a Drawable to a Bitmap.
     *
     * @param drawable The Drawable to convert.
     * @return The resulting Bitmap.
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    /**
     * Converts a Bitmap to a byte array.
     *
     * @param bitmap The Bitmap to convert.
     * @return The resulting byte array.
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Converts a Drawable to a byte array.
     *
     * @param drawable The Drawable to convert.
     * @return The resulting byte array.
     */
    public static byte[] drawableToByteArray(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        return bitmapToByteArray(bitmap);
    }

    public static byte[] convertToByteArray(Byte[] byteObjectArray) {
        byte[] byteArray = new byte[byteObjectArray.length];
        for (int i = 0; i < byteObjectArray.length; i++) {
            byteArray[i] = byteObjectArray[i]; // Unboxing
        }
        return byteArray;
    }
}
