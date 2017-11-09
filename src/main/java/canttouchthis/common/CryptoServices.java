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
    public IvParameterSpec ivspec;

    public CryptoServices() {
        byte[] ivbytes = { 0x00, 0x1a, 0x0f, 0x10, 0x44, 0x5c, 0x2d, 0x7c,
                           0x22, 0x4f, 0x3a, 0x7c, 0x6a, 0x22, 0x1f, 0x13 };
        this.ivspec = new IvParameterSpec(ivbytes);
    }

    /**
     * Retrieve encryption cipher.
     * 
     * @param key Key passed to method to encrypt under.
     * @throws Exception For all possible exceptions (i.e. invalid key, bad padding, etc.)
     */
    public Cipher getEncryptCipher(Key key) throws Exception {
        Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, ivspec);

        return c;
    }


    /**
     * Retrieve decryption cipher.
     * 
     * @param key Key passed to method to encrypt under.
     * @throws Exception For all possible exceptions (i.e. invalid key, bad padding, etc.)
     */
    public Cipher getDecryptCipher(Key key) throws Exception {
        Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, ivspec);

        return c;
    }


    /**
     * Encrypt plaintext with AES.
     * 
     * @param plaintext Byte array containing message.
     * @param key Key passed to method to encrypt under.
     * @throws Exception For all possible exceptions (i.e. invalid key, bad padding, etc.)
     */
    public byte[] encryptSymmetric(byte[] plaintext, Key key) throws Exception {
        Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
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
    public String decryptSymmetric(byte[] ciphertext, Key key) throws Exception {
        Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, ivspec);
        byte[] newPlaintext = new byte[c.getOutputSize(ciphertext.length)];
        c.doFinal(ciphertext, 0, newPlaintext.length, newPlaintext);

        // find last non-zero byte (O(1) since there padding bits is upper bounded by blocksize)
        int lastCharInd = newPlaintext.length - 1;
        while (newPlaintext[lastCharInd] == 0)
            lastCharInd--;

        return new String(newPlaintext, 0, lastCharInd + 1);
    }
}

