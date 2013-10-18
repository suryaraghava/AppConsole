package com.audiumcorp.webportal.admin;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DesEncrypter
{

    Cipher ecipher;
    Cipher dcipher;
    byte salt[] = {
        -87, -101, -56, 50, 86, 53, -29, 3
    };
    int iterationCount;
    public static final DesEncrypter NAGASAKTI = new DesEncrypter("NagaSakti");

    private DesEncrypter(String passPhrase)
    {
        iterationCount = 19;
        try
        {
            java.security.spec.KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            java.security.spec.AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(1, key, paramSpec);
            dcipher.init(2, key, paramSpec);
        }
        catch(InvalidAlgorithmParameterException invalidalgorithmparameterexception) { }
        catch(InvalidKeySpecException invalidkeyspecexception) { }
        catch(NoSuchPaddingException nosuchpaddingexception) { }
        catch(NoSuchAlgorithmException nosuchalgorithmexception) { }
        catch(InvalidKeyException invalidkeyexception) { }
    }

    public String encrypt(String str)
    {
    	
        byte enc[];
        try{
        byte utf8[] = str.getBytes("UTF8");
        enc = ecipher.doFinal(utf8);
        return (new BASE64Encoder()).encode(enc);
       
        }
        catch(Exception e)
        {}
        return null;
    }

    public String decrypt(String str)
    {
        byte utf8[];
        try{
        byte dec[] = (new BASE64Decoder()).decodeBuffer(str);
        utf8 = dcipher.doFinal(dec);
        return new String(utf8, "UTF8");
        }
        catch(Exception e)
        {}
        return null;
    }

    public static void main(String args[])
    {
        String encrypted = NAGASAKTI.encrypt("rajit@spinsci.com");
        System.out.println(encrypted);
        String decrypted = NAGASAKTI.decrypt("ZTVAJfnHBrvTDK2rX3CbKB8y2v2/8mRj");
        System.out.println(decrypted);
    }

}
