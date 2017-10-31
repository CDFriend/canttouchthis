package canttouchthis.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IntegrityChecking {

    private static final String A_DIG = "SHA-256";

    private byte[] generateMessageDigest(String msg) {

        MessageDigest algo;
        try {
            algo = MessageDigest.getInstance(A_DIG);
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.printf("Algorithm %s does not exist!\n", A_DIG);
            return null;
        }

        return algo.digest(msg.getBytes());

    }

}
