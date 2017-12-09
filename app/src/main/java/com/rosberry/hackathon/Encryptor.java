package com.rosberry.hackathon;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by Evgeniy Nagibin on 09/12/2017.
 */

public class Encryptor {

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private byte[] initVector;
    private byte[] encryption;
    private Storage storage;

    Encryptor(Storage storage) {
        this.storage = storage;
    }

    byte[] encryptTextWithAES(String alias, String textToEncrypt, String transformation, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias, algorithm));

        initVector = cipher.getIV();
        encryption = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));
        String base64InitVector = Base64.encodeToString(initVector, Base64.NO_WRAP);
        String base64Encryption = Base64.encodeToString(encryption, Base64.NO_WRAP);
        storage.putString(alias.concat("_initVector"), base64InitVector);
        storage.putString(alias.concat("_encryption"), base64Encryption);
        return encryption;
    }

    private SecretKey getSecretKey(String alias, String algorithm) throws Exception {

        KeyGenerator keyGenerator;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator = KeyGenerator.getInstance(algorithm, ANDROID_KEY_STORE);
            keyGenerator.init(new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setKeyValidityEnd(new Date(System.currentTimeMillis() + 10000))
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
        } else {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
            keyGenerator.init(SecureRandom.getInstance(KeyProperties.KEY_ALGORITHM_AES));
        }
        return keyGenerator.generateKey();
    }

    byte[] getInitVector() {
        return initVector;
    }

    byte[] getEncryption() {
        return encryption;
    }
}
