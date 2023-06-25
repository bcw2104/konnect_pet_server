package com.konnect.pet.utils;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Aes256Utils {

    public static String encrypt(String str, String key, String iv)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secureKey = new SecretKeySpec(Base64.decodeBase64(key), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.substring(0, 16).getBytes()));

        byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));

        return Base64.encodeBase64String(encrypted);
    }

    public static String decrypt(String str, String key, String iv)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secureKey = new SecretKeySpec(Base64.decodeBase64(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.substring(0, 16).getBytes("UTF-8")));

        byte[] byteStr = Base64.decodeBase64(str);

        return new String(cipher.doFinal(byteStr), "UTF-8");
    }

}
