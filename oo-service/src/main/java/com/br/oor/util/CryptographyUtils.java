package com.br.oor.util;

import org.springframework.beans.factory.annotation.Value;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Created by maiconoliveira on 21/12/15.
 */
public class CryptographyUtils {
    private Cipher cipher;
    private byte[] encryptKey;
    private KeySpec keySpec;
    private SecretKeyFactory secretKeyFactory;
    private SecretKey secretKey;

    //TODO melhorar
    private String key = "blablabla";


    public static CryptographyUtils newInstance() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
            return new CryptographyUtils();
    }
    private CryptographyUtils() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        //TODO esconder chave
        String key = "http://www.olhaorole.com.br";
        //encryptKey = MessageUtil.getMessage(key).getBytes("UTF-8" );
        encryptKey = key.getBytes("UTF-8" );
        cipher = Cipher.getInstance( "DESede" );
        keySpec = new DESedeKeySpec( encryptKey );
        secretKeyFactory = SecretKeyFactory.getInstance( "DESede" );
        secretKey = secretKeyFactory.generateSecret( keySpec );
     }

     public String encrypt( String value ) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

         cipher.init( Cipher.ENCRYPT_MODE, secretKey );
         byte[] cipherText = cipher.doFinal( value.getBytes( "UTF-8" ) );
         BASE64Encoder encoder = new BASE64Encoder();
         return encoder.encode( cipherText );
     }
     public String decrypt( String value ) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
         cipher.init( Cipher.DECRYPT_MODE, secretKey );
         BASE64Decoder dec = new BASE64Decoder();
         byte[] decipherText = cipher.doFinal( dec.decodeBuffer( value ) );
         return new String( decipherText );
     }

}
