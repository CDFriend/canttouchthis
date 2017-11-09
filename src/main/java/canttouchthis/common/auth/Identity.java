package canttouchthis.common.auth;

import java.io.Serializable;
import java.util.Base64;
import java.util.Random;


/**
 * Datastructure containing a user's ident. Also carries a nonce
 * which can be used to verify an ident against an authentication
 * database.
 */
public class Identity implements Serializable {

    private String _uname;
    protected byte[] _nonce;

    protected Identity (String username, String nonce) {
        this._uname = username;
        this._nonce = Base64.getDecoder().decode(nonce);
    }

    public String getUsername() {
        return _uname;
    }

    // Create an identity with an incorrect nonce (for testing purposes).
    public static Identity generateBadIdent() {
        // generate random nonce (not in database)
        byte[] b = new byte[32];
        new Random().nextBytes(b);

        return new Identity("admin", Base64.getEncoder().encodeToString(b));
    }
}
