package com.forkmang.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Mobile on 26-09-2017.
 */

public class StorePrefrence {

    private SharedPreferences prefencs;
    private SharedPreferences.Editor editor;


    public StorePrefrence(Context context) {
        prefencs = context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
    }

    public void setString(String key , String value) {
        editor = prefencs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return prefencs.getString(key, "");
    }


    public void setInt(String key , Integer value) {
        editor = prefencs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        //Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public void setImage(String key,Bitmap yourbitmap)
    {
        editor = prefencs.edit();
        editor.putString(key, encodeTobase64(yourbitmap));
        editor.commit();
    }

    public Bitmap getImage(String key)
    {
        String input = prefencs.getString(key, "");
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void clear()
    {
        SharedPreferences.Editor editor = prefencs.edit();
        editor.clear();
        editor.commit();
    }

    public Integer getInt(String key) {
        return prefencs.getInt(key, 0);
    }


    public boolean getBoolean(String key) {
        return prefencs.getBoolean(key,false);
    }

    public void setBoolean(String key, boolean value) {

        editor = prefencs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getData(String id) {
        return prefencs.getString(id, "");
    }
    public void setData(String id, String val) {
        editor = prefencs.edit();
        editor.putString(id, val);
        editor.commit();
    }
    public String getCoordinates(String id) {
        return prefencs.getString(id, "0");
    }
}
