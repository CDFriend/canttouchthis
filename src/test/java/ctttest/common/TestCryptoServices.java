package ctttest.common;

import canttouchthis.common.CryptoServices;

import java.security.*;
import javax.crypto.*;

import junit.framework.*;

public class TestCryptoServices extends TestCase {

    public void testEncryptDecrypt() throws Exception {
        String message = "Hi there! This is a test message.";

        //Generate random key for symmetric crypto
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        Key key = keyGen.generateKey();

        byte[] byteMessage = message.getBytes();

        /*
         * Pass message to CryptoServices, 
         * and then return its respective ciphertext back to it as well.
         */
        CryptoServices cs = new CryptoServices();
        byte[] ciphertext = cs.encryptSymmetric(byteMessage, key);
        byte[] newPlaintext = cs.decryptSymmetric(ciphertext, key);
        
        /*
         * Verify original message and decrypted plaintext are the same.
         */
        String plaintext = new String(newPlaintext);
        assertEquals(message, plaintext);
    }
}