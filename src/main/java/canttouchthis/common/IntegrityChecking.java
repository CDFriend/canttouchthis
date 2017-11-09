package canttouchthis.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IntegrityChecking {

    private static final String A_DIG = "SHA-256";

    /**
     * Generates a SHA-256 digest hash for a given message.
     *
     * @param msg ChatMessage to create digest for.
     * @return Byte sequence representing the hash.
     */
    public static byte[] generateMessageDigest(byte[] msg) {

        MessageDigest algo;
        try {
            algo = MessageDigest.getInstance(A_DIG);
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.printf("Algorithm %s does not exist!\n", A_DIG);
            return null;
        }

        return algo.digest(msg);

    }

    /**
     * Checks whether or not a string message and its provided digest match.
     * If this function returns false, the message was likely modified in
     * transit.
     *
     * @param msg ChatMessage text.
     * @param providedDigest SHA-256 digest provided along with the message.
     * @return Whether or not the hashed text and its provided digest match.
     */
    public static boolean checkDigest(byte[] msg, byte[] providedDigest) {

        // Generate a digest for the String message. This should be the same
        // as the provided digest if the message has not been modified.
        byte[] msgDigest = generateMessageDigest(msg);

        return MessageDigest.isEqual(msgDigest, providedDigest);

    }

}
