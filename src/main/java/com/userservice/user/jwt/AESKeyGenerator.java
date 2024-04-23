package com.userservice.user.jwt;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESKeyGenerator {
    private String SECRET_KEY;

    public AESKeyGenerator(int keySize) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(keySize);
            SecretKey secretKey = keyGen.generateKey();
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            SECRET_KEY = encodedKey;
        } catch (NoSuchAlgorithmException e) { e.printStackTrace();}
    }

    public String getSecretKey() {
        return SECRET_KEY;
    }

}
