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

    public Identity senderIdent;

    public Message(String msg, long ts) {
        senderIdent = null;
        timestamp = new Date(ts);
        message = msg;
    }

    public void setIdentity(Identity i) {
        this.senderIdent = i;
    }

    public String getSenderName() {
        if (this.senderIdent != null) {
            return this.senderIdent.getUsername();
        }
        else {
            return "UNKNOWN";
        }
    }

}
