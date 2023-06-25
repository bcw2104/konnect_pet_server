package com.konnect.pet.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Utils {

	public static String encrypt(String text) throws NoSuchAlgorithmException {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");

	    md.update(text.getBytes());

	    return bytesToHex(md.digest());
	}

	private static String bytesToHex(byte[] bytes) {
	    StringBuilder builder = new StringBuilder();
	    for (byte b : bytes) {
	        builder.append(String.format("%02x", b));
	    }
	    return builder.toString();
	}
}
