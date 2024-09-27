package com.example.dvote.fabric_gateway.data;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class cache_user {
    /**
     * Store a valid pin in the apps  cache directory, or retrieves a stored pin in cache
     *
     * @param editor sharedpreference editor
     * @param pin    Pin of registered use
     * @param sh     sharedpreferences if you wish to store a pin in cache then add a non null instance of this parameter
     */
    public static long cache_pin(@Nullable SharedPreferences.Editor editor, @Nullable Long pin, @Nullable SharedPreferences sh) {
        if (editor != null && pin != null) {
            editor.putLong("user_pin", pin);
            editor.apply();
        } else {
            assert sh != null;
            pin = sh.getLong("user_pin", 0);
        }
        return pin;
    }

}
