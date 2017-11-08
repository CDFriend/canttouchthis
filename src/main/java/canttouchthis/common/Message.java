package canttouchthis.common;

import canttouchthis.common.auth.Identity;

import java.util.Date;
import java.io.Serializable;

/**
 * Container class for chat messages. This will likely change as more crypto
 * stuff gets added.
 */
public class Message implements Serializable {

    public final Date timestamp;
    public final String message;

    private Identity _senderIdent;

    public Message(String msg, long ts) {
        _senderIdent = null;
        timestamp = new Date(ts);
        message = msg;
    }

    public void setIdentity(Identity i) {
        this._senderIdent = i;
    }

    public String getSenderName() {
        return this._senderIdent.getUsername();
    }

}
