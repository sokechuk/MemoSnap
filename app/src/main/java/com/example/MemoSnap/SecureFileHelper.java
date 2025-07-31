package com.example.MemoSnap;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SecureFileHelper {

    private final Context context;

    public SecureFileHelper(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveSecureFile(String filename, String data) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        File file = new File(context.getFilesDir(), filename);
        EncryptedFile encryptedFile = new EncryptedFile.Builder(
                context,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build();

        try (OutputStream outputStream = encryptedFile.openFileOutput()) {
            outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public String readSecureFile(String filename) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        File file = new File(context.getFilesDir(), filename);
        EncryptedFile encryptedFile = new EncryptedFile.Builder(
                context,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build();

        try (FileInputStream inputStream = encryptedFile.openFileInput()) {
            byte[] data = new byte[(int) file.length()];
            inputStream.read(data);
            return new String(data, StandardCharsets.UTF_8);
        }
    }
}
