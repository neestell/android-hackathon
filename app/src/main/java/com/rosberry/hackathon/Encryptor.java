package com.rosberry.hackathon;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by Evgeniy Nagibin on 09/12/2017.
 */

public class Encryptor {

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    private Storage storage;

    Encryptor(Storage storage) {
        this.storage = storage;
    }

    byte[] encryptTextWithAES(String alias, String textToEncrypt) throws Exception {

        alias = alias.concat("_AES");

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias, KeyProperties.KEY_ALGORITHM_AES));

        byte[] initVector = cipher.getIV();
        byte[] encryption = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));
        String base64InitVector = Base64.encodeToString(initVector, Base64.NO_WRAP);
        String base64Encryption = Base64.encodeToString(encryption, Base64.NO_WRAP);
        storage.putString(alias.concat("_initVector"), base64InitVector);
        storage.putString(alias.concat("_encryption"), base64Encryption);

        Log.d("AndroidKeyStore", "aliases: " + getAllAliasesInTheKeystore());
        return encryption;
    }

    private ArrayList<String> getAllAliasesInTheKeystore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        return Collections.list(keyStore.aliases());
    }

    private SecretKey getSecretKey(String alias, String algorithm) throws Exception {

        KeyGenerator keyGenerator;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator = KeyGenerator.getInstance(algorithm, ANDROID_KEY_STORE);
            keyGenerator.init(new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
        } else {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
            keyGenerator.init(SecureRandom.getInstance(KeyProperties.KEY_ALGORITHM_AES));
        }
        return keyGenerator.generateKey();
    }

    byte[] encryptTextWithRSA(String alias, String textToEncrypt) {
        alias = alias.concat("_RSA");
        Key publicKey = null;
        Key privateKey = null;
        try {
            getAllAliasesInTheKeystore();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA);
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Encode the original data with RSA private key
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, privateKey);
            encodedBytes = c.doFinal(textToEncrypt.getBytes());
            String base64PublicKey = Base64.encodeToString(publicKey.getEncoded(), Base64.NO_WRAP);
            String base64Encrypted = Base64.encodeToString(encodedBytes, Base64.NO_WRAP);

            storage.putString(alias.concat("_publicKey"), base64PublicKey);
            storage.putString(alias.concat("_encryption"), base64Encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return encodedBytes;
    }
}
