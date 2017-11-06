package canttouchthis.common;

import java.io.IOException;

public interface IChatSession {

    /**
     * Blocks until a new message is received from the server.
     *
     * @return New Message object from server.
     * @throws IOException If an error is encountered reading from the websocket stream.
     */
    public Message getNextMessage() throws IOException;

}
