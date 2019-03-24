package com.horner.xsm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;  

import javax.crypto.Cipher;  
import javax.crypto.KeyGenerator;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;

import android.content.Intent;
import android.util.Log;

import com.horner.xsm.constants.Constants;
import com.horner.xsm.service.PlayService;



/**
 * @author  作者 : sun
 * @date 创建时间：2016-2-16 下午4:12:34    
 * @return 
 * @description 
 */

public class AESUtils {
	public static byte[] encryptVoice(String seed, byte[] clearbyte)  
            throws Exception {  
        byte[] rawKey = getRawKey(seed.getBytes());  
        byte[] result = encrypt(rawKey, clearbyte);  
        return result;  
    }  
  
    public static byte[] decryptVoice(String seed, byte[] encrypted)  
            throws Exception {  
        byte[] rawKey = getRawKey(seed.getBytes());  
        byte[] result = decrypt(rawKey, encrypted);  
        return result;  
    }  
  
    private static byte[] getRawKey(byte[] seed) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");  
        sr.setSeed(seed);  
        kgen.init(128, sr);  
        SecretKey skey = kgen.generateKey();  
        byte[] raw = skey.getEncoded();  
        return raw;  
    }  
  
    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {  
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(  
                new byte[cipher.getBlockSize()]));  
        byte[] encrypted = cipher.doFinal(clear);  
        return encrypted;  
    }  
  
    private static byte[] decrypt(byte[] raw, byte[] encrypted)  
            throws Exception {  
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(  
                new byte[cipher.getBlockSize()]));  
        byte[] decrypted = cipher.doFinal(encrypted);  
        return decrypted;  
    } 
    
    public static String traversalFile(String path) {
    	String maxName = null;
		File files = new File(path);
		String filePath = null;
		long maxSize = 0;
		for (File file : files.listFiles()) {
			if (file.exists()) {
				String name = file.getName();
				Log.i("info","named的妹子；"+name);
				
				if (name.endsWith(".mp3")) {
					if(file.length() > maxSize){
						Log.i("info",file.getName()+"==="+file.length());
						maxSize = file.length();
						maxName = file.getName();
						Log.i("info","maxNmae的价格为:"+maxName);
					}else {
						
					}
					filePath = path + File.separator + maxName;
					Log.i("info", "filePath的路径为;" + filePath    );
					//读取文件之前必须先解密,然后才能读
					//FileEnDecryptManager.Initdecrypt(filePath);
				}
			}
		}
		 return filePath;
	}
    
    //加密
    public static void encryptVedios(String filePath) {
    	File oldFile = new File(filePath);
    	FileInputStream fis = null;
    	FileOutputStream fos = null;
    	try {
    		fis = new FileInputStream(oldFile);
    		byte[] oldByte = new byte[(int) oldFile.length()];  
            fis.read(oldByte); // 读取  
            byte[] newByte = AESUtils.encryptVoice(Constants.XSM, oldByte);  
            // 加密  
            fos = new FileOutputStream(oldFile);  
            fos.write(newByte); 
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  
    }
    //解密
    public static void decryptVedios(String filePath) {

		File oldFile = new File(filePath);

		byte[] oldByte = new byte[(int) oldFile.length()];

		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(oldFile);
			fis.read(oldByte);
			byte[] newByte = AESUtils.decryptVoice(Constants.XSM, oldByte);
			// 解密
			fos = new FileOutputStream(oldFile);
			fos.write(newByte);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
}
