package canttouchthis.common;

import java.io.IOException;

public interface IChatSession {

    /**
     * Blocks until a new message is received from the server.
     *
     * @return New ChatMessage object from server.
     * @throws IOException If an error is encountered reading from the websocket stream.
     */
    public MessagePacket getNextMessage() throws Exception;

    /**
     * Sends a ChatMessage object to the server.
     *
     * @param m ChatMessage to be sent.
     * @throws IOException If an error is encountered sending over the websocket.
     */
    public void sendMessage(ChatMessage m) throws Exception;

    public boolean usesConfidentiality();

    public boolean checksIntegrity();

}
