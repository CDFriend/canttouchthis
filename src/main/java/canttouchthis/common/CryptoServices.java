/*
 * CryptoServices.java
 * 
 * Provides encryption services for messages sent and received
 * by client and server.
 * 
 */

package canttouchthis.common;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

public class CryptoServices {

    /* 
     * Initialize constructor
     * for initialization vector for use in encryption/decryption with Cipher
     */
    public SecureRandom randomIVGenerator;
    public byte[] iv;
    public IvParameterSpec ivspec;

    public CryptoServices() {
        this.randomIVGenerator = new SecureRandom();
        this.iv = new byte[16];
        randomIVGenerator.nextBytes(iv);
        this.ivspec = new IvParameterSpec(iv);
    }

    /**
     * Encrypt plaintext with AES.
     * 
     * @param plaintext Byte array containing message.
     * @param key Key passed to method to encrypt under.
     * @throws Exception For all possible exceptions (i.e. invalid key, bad padding, etc.)
     */
    public byte[] encryptSymmetric(byte[] plaintext, Key key) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, ivspec);
        byte[] ciphertext = new byte[c.getOutputSize(plaintext.length)];
        c.doFinal(plaintext, 0, plaintext.length, ciphertext);

        return ciphertext;
    }

    /**
     * Decrypt ciphertext with AES.
     * 
     * @param ciphertext Byte array containing message.
     * @param key Key passed to method to encrypt under.
     * @throws Exception For all possible exceptions (i.e. invalid key, bad padding, etc.)
     */
    public byte[] decryptSymmetric(byte[] ciphertext, Key key) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, ivspec);
        byte[] newPlaintext = new byte[c.getOutputSize(ciphertext.length)];
        c.doFinal(ciphertext, 0, newPlaintext.length, newPlaintext);

        return newPlaintext;
    }
}

