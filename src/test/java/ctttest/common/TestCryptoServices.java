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

        /*
         * Pass message to CryptoServices, 
         * and then return its respective ciphertext back to it as well.
         */
        CryptoServices cs = new CryptoServices();
        byte[] ciphertext = cs.encryptSymmetric(message.getBytes(), key);
        String newPlainText = cs.decryptSymmetric(ciphertext, key);

        assertEquals(message, newPlainText);
    }
}