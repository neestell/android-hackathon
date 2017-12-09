package com.rosberry.hackathon;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Created by Evgeniy Nagibin on 09/12/2017.
 */

public class Decryptor {

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private KeyStore keyStore;
    private Storage storage;

    Decryptor(Storage storage) throws Exception {
        initKeyStore();
        this.storage = storage;
    }

    private void initKeyStore() throws Exception {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
    }

    String decryptDataWithAES(final String alias) throws Exception {
        String base64InitVector = storage.getString(alias.concat("_initVector"), null);
        String base64Encryption = storage.getString(alias.concat("_encryption"), null);

        if (TextUtils.isEmpty(base64InitVector) || TextUtils.isEmpty(base64Encryption))
            throw new NullPointerException("Not found initialization vector or encryption data for specified alias");

        byte[] encryptionIv = Base64.decode(base64InitVector, Base64.NO_WRAP);
        byte[] encryptedData = Base64.decode(base64Encryption, Base64.NO_WRAP);

        Log.d("AndroidKeyStore", "encrypted data: " + Arrays.toString(encryptedData) + " encrypted iv: "
                + Arrays.toString(encryptionIv));
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        final GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec);

        return new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8);
    }

    private SecretKey getSecretKey(final String alias) throws Exception {
        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
    }
}
