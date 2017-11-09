package canttouchthis.common;

import java.io.IOException;
import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * Contains a serializable object and a SHA-256 digest, which can be checked
 * after the packet has been transmitted. If the packet content and its digest
 * do not match, the packet may have been intercepted!
 */
public class MessagePacket implements Serializable {

    private Serializable _content;
    private byte[] _digest;

    public MessagePacket(Serializable s) throws IOException {

        this._content = s;

        // create digest for message content
        this._digest = genDigest(this._content);

    }

    public Serializable getContent() {
        return this._content;
    }

    public boolean checkMessageDigest() throws IOException {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutStream);
        oos.writeObject(this._content);

        return IntegrityChecking.checkDigest(byteOutStream.toByteArray(), this._digest);
    }

    private byte[] genDigest(Serializable s) throws IOException {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutStream);
        oos.writeObject(s);

        return IntegrityChecking.generateMessageDigest(byteOutStream.toByteArray());
    }

}
