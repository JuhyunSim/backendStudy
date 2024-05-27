package com.zerobase.domain.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class Aes256Util {
    public static String alg = "AES/CBC/PKCS5Padding";
    private static final String KEY = "ZEROBASEKEY_ISZEROBASE_KEY";
    private static final String IV = KEY.substring(0, 16);

    public static String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(IV.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(encrypted);
        } catch(Exception e) {
            return null;
        }
    }

    public static String decrypt(String cipherText) {
        try {
            System.out.println("Decrypting: " + cipherText);

            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(IV.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            System.out.println("Cipher initialized for decryption");

            byte[] decodedBytes = Base64.decodeBase64(cipherText);
            System.out.println("Base64 decoded: " + new String(decodedBytes, StandardCharsets.UTF_8)); // Base64 디코딩 결과 로그

            byte[] decrypted = cipher.doFinal(decodedBytes);
            String decryptedString = new String(decrypted, StandardCharsets.UTF_8);
            System.out.println("Decryption successful: " + decryptedString);
            return decryptedString;
        } catch (Exception e) {
            System.err.println("Decryption failed: " + e.getMessage()); // 예외 메시지 로그
            e.printStackTrace();
            return null;
        }
    }
}
