package ctttest.net;

import ctttest.net.NetUtilityThreads.*;

import canttouchthis.client.ClientSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.ServerSocket;

import canttouchthis.common.Message;
import junit.framework.*;

public class TestClientSession extends TestCase {

    ClientSession sess;

    public void setUp() throws Exception {
        super.setUp();
        sess = new ClientSession("127.0.0.1", 50000);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        sess.close();
    }

    public void testClientConnectsToServer() {
        // SETUP
        WaitForConnection server = new WaitForConnection(50000, false);
        server.start();

        // EXEC
        boolean success = true;
        try {
            Thread.sleep(3000);
            sess.connect();
            server.join();
        }
        catch (InterruptedException ex) {
            success = false;
        }

        // VERIFY
        assertTrue(success);
        assertTrue(server.success);
    }

    public void testMessageSerialization() {
        // SETUP
        Message m = new Message("Alice", "Bob", 0, "This is a test!!!");
        WaitForConnection server = new WaitForConnection(50000, true);
        server.start();

        // EXEC
        boolean success = true;
        try {
            Thread.sleep(3000);
            sess.connect();
            sess.sendMessage(m);
            server.join();
        }
        catch (IOException|InterruptedException ex) {
            success = false;
        }

        Message recv = server.message;

        // VERIFY
        assertTrue(success);
        assertTrue(server.success);
        assertEquals(m.sender, recv.sender);
        assertEquals(m.reciever, recv.reciever);
        assertEquals(m.message, recv.message);
        assertTrue(m.timestamp.equals(recv.timestamp));
    }

}
