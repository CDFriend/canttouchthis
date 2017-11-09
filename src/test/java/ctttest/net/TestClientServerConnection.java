package ctttest.net;

import canttouchthis.common.ChatMessage;
import ctttest.net.NetUtilityThreads.*;

import canttouchthis.client.ClientSession;
import canttouchthis.server.ServerSession;

import java.net.UnknownHostException;

import junit.framework.*;

/**
 * Tests ensuring that ClientSession and ServerSession can exchange messages.
 */
public class TestClientServerConnection extends TestCase {

    /**
     * Checks that a ClientSession can connect to a ServerSession.
     *
     * @throws UnknownHostException If localhost cannot be found.
     */
    public void testClientReceivesServerMessage() throws UnknownHostException {
        // SETUP
        ServerSession s = new ServerSession();
        ClientSession c = new ClientSession();
        ChatMessage m = new ChatMessage("This is a test!!!", 0);

        // EXEC
        TryRecieveMessageClient cThread = new TryRecieveMessageClient(c);
        try {
            cThread.start();
            s.waitForConnection();
            s.sendMessage(m);
            cThread.join();
            s.close();
        }
        catch (Exception ex) {
            assertTrue("Exception when sending message!", false);
        }

        ChatMessage recv = cThread.message;

        // VERIFY
        assertTrue("Error running client thread!", cThread.success);
        assertEquals(m.message, recv.message);
        assertTrue(m.timestamp.equals(recv.timestamp));
    }

    /**
     * Checks that a ServerSession can send a ChatMessage to a ClientSession.
     *
     * @throws UnknownHostException If localhost cannot be found.
     */
    public void testServerRecievesClientMessage() throws UnknownHostException {
        // SETUP
        ServerSession s = new ServerSession();
        ClientSession c = new ClientSession();
        ChatMessage m = new ChatMessage("This is a test!!!", 0);

        // EXEC
        TryRecieveMessageServer sThread = new TryRecieveMessageServer(s);
        try {
            sThread.start();
            Thread.sleep(3000);
            c.connect("127.0.0.1", 50000);
            c.sendMessage(m);
            sThread.join();
        }
        catch (Exception ex) {
            assertTrue("Exception when sending message!", false);
        }

        ChatMessage recv = sThread.message;

        // VERIFY
        assertTrue("Error running client thread!", sThread.success);
        assertEquals(m.message, recv.message);
        assertTrue(m.timestamp.equals(recv.timestamp));
    }

}
