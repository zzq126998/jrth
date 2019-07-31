/**
 * 说明：加密解密
 * 创建：雨中磐石  from www.rili123.cn
 * 时间：2014-11-28
 * 邮箱：qqqq2900@126.com
 * QQ：290802026/1073744729
 * */

package com.lib;

import android.util.Base64;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Jiami {
	
	public static String encrypt(String cleartext){
		byte[] rawKey;
		try {
			rawKey = getRawKey("rock".getBytes());
			byte[] result = encrypt(rawKey, cleartext.getBytes());
			return toHex(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cleartext;
	}
	public static String decrypt(String encrypted) {
		byte[] rawKey;
		try {
			rawKey = getRawKey("rock".getBytes());
			byte[] enc = toByte(encrypted);
			byte[] result = decrypt(rawKey, enc);
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypted;
	}

	public static String md5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());
			byte[] m = md5.digest();//加密
			return toHex(m);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}
	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}
	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}
	public static byte[] toByte(String hexString) {
		int len = hexString.length()/2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
		result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
		return result;
	}
	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2*buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}
	private final static String HEX = "0123456789ABCDEF";
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
	}

	public static String base64encode(String str)
	{
		String strBase64	= str;
		try {
			strBase64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
			strBase64 = strBase64.substring(0, strBase64.length() - 1);
			strBase64 = strBase64.replace("+", "!");
			strBase64 = strBase64.replace("/", ".");
			strBase64 = strBase64.replace("=", ":");
		}catch (Exception e){
		}
		return strBase64;
	}

	public static String base64decode(String str)
	{
        String strBase64	= str;
        try {
            strBase64 = strBase64.replace("!", "+");
            strBase64 = strBase64.replace(".", "/");
            strBase64 = strBase64.replace(":", "=");
            strBase64 = new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT));
        }catch (Exception e){

        }
        return strBase64;
	}
}