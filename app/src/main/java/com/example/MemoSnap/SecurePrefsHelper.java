package com.example.MemoSnap;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class SecurePrefsHelper {

    private static final String PREF_NAME = "memo_prefs";
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

    // User email
    public void saveEmail(String email) {
        securePrefs.edit().putString("user_email", email).apply();
    }

    public String getEmail() {
        return securePrefs.getString("user_email", null);
    }

    public void clearEmail() {
        securePrefs.edit().remove("user_email").apply();
    }

    // Password hash
    public void savePasswordHash(String hash) {
        securePrefs.edit().putString("password_hash", hash).apply();
    }

    public String getPasswordHash() {
        return securePrefs.getString("password_hash", null);
    }

    public void clearPasswordHash() {
        securePrefs.edit().remove("password_hash").apply();
    }

    public void clearAll() {
        securePrefs.edit().clear().apply();
    }
}
