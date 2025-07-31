package com.example.MemoSnap.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class SecurePrefsHelper {

    private static final String PREF_NAME = "memo_prefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_ID = "user_id";

    private static SecurePrefsHelper instance;
    private final SharedPreferences securePrefs;

    private SecurePrefsHelper(Context context) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        securePrefs = EncryptedSharedPreferences.create(
                context,
                PREF_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static SecurePrefsHelper getInstance(Context context) {
        if (instance == null) {
            try {
                instance = new SecurePrefsHelper(context.getApplicationContext());
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize SecurePrefsHelper", e);
            }
        }
        return instance;
    }

    // Token methods
    public void saveToken(String token) {
        securePrefs.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    public String getToken() {
        return securePrefs.getString(KEY_AUTH_TOKEN, null);
    }

    public void clearToken() {
        securePrefs.edit().remove(KEY_AUTH_TOKEN).apply();
    }

    // User ID methods
    public void saveUserId(String userId) {
        securePrefs.edit().putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return securePrefs.getString(KEY_USER_ID, null);
    }

    public void clearUserId() {
        securePrefs.edit().remove(KEY_USER_ID).apply();
    }

    // Clear everything
    public void clearAll() {
        securePrefs.edit().clear().apply();
    }
}
