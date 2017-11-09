package ctttest.common;

import canttouchthis.common.Message;
import canttouchthis.common.CryptoServices;

import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
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

    /**
     * Checks that a CipherOutputStream using our CryptoUtils ciphers actually encrypts an
     * object from an ObjectOutputStream.
     *
     * To verify this manually, run at the end of the string:
     *      System.out.println(unencryptedString);
     *      System.out.println(encryptedString);
     *
     * @throws Exception If any issues are encountered initializing the cipher.
     */
    public void testCipherStreamsActuallyEncrypt() throws Exception {

        // Generate random key for symmetric crypto
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        Key key = keyGen.generateKey();

        // Create generic message
        Message m = new Message("I am a walrus.", 12);

        // Create two pipelines: one uses the encryption cipher, one doesn't
        ByteArrayOutputStream byteStreamNoEncrypt = new ByteArrayOutputStream();
        ObjectOutputStream oosNoEncrypt = new ObjectOutputStream(byteStreamNoEncrypt);

        Cipher c = (new CryptoServices()).getEncryptCipher(key);
        ByteArrayOutputStream byteStreamEncrypted = new ByteArrayOutputStream();
        CipherOutputStream cos = new CipherOutputStream(byteStreamEncrypted, c);
        ObjectOutputStream oosEncrypted = new ObjectOutputStream(cos);

        // EXEC: Write the message to the encrypted and unencrypted pipeline
        oosEncrypted.writeObject(m);
        oosNoEncrypt.writeObject(m);

        // Get byte strings from the end of the pipe
        String unencryptedString = new String(byteStreamNoEncrypt.toByteArray());
        String encryptedString = new String(byteStreamEncrypted.toByteArray());

        assertFalse("Encrypted and unencrypted streams should not be the same.",
                unencryptedString.equals(encryptedString));

    }

}