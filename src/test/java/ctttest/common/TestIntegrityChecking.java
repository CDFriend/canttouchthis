package ctttest.common;

import canttouchthis.common.IntegrityChecking;

import java.security.MessageDigest;
import junit.framework.*;

public class TestIntegrityChecking extends TestCase {

    private final String message = "This is a message!";

    /**
     * Length of message digest should be 256 bits (32 bytes).
     */
    public void testLengthOfHash() {

        byte[] hash = IntegrityChecking.generateMessageDigest(message.getBytes());

        assertEquals(32, hash.length);

    }

    /**
     * Digest for the same message should be the same.
     */
    public void testHashOfSameMessageIsSame() {

        byte[] hash = IntegrityChecking.generateMessageDigest(message.getBytes());
        byte[] hash2 = IntegrityChecking.generateMessageDigest(message.getBytes());

        assertTrue(MessageDigest.isEqual(hash, hash2));

    }

    public void testHashOfDifferentMessageIsDifferent() {

        byte[] hash = IntegrityChecking.generateMessageDigest(message.getBytes());
        byte[] hash2 = IntegrityChecking.generateMessageDigest((message + "; badstuff").getBytes());

        assertTrue(!MessageDigest.isEqual(hash, hash2));

    }

}
