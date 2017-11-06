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

    /**
     * Sends a Message object to the server.
     *
     * @param m Message to be sent.
     * @throws IOException If an error is encountered sending over the websocket.
     */
    public void sendMessage(Message m) throws IOException;

}
