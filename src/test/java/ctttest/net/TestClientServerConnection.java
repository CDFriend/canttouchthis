package ctttest.net;

import canttouchthis.common.Message;
import canttouchthis.client.ClientSession;
import canttouchthis.server.ServerSession;

import java.io.IOException;
import java.net.UnknownHostException;

import junit.framework.*;

public class TestClientServerConnection extends TestCase {

    private class TryRecieveMessageClient extends Thread {
        ClientSession c;
        Message message = null;
        boolean success = false;
        public TryRecieveMessageClient(ClientSession c) {
            this.c = c;
        }

        public void run() {
            try {
                Thread.sleep(3000);
                c.connect();
                message = c.getNextMessage();
                success = true;
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            c.close();
        }
    }

    private class TryRecieveMessageServer extends Thread {
        ServerSession s;
        Message message = null;
        boolean success = false;
        public TryRecieveMessageServer(ServerSession s) {
            this.s = s;
        }

        public void run() {
            s.waitForConnection();
            try {
                message = s.getNextMessage();
                success = true;
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            s.close();
        }
    }

    public void testClientReceivesServerMessage() throws UnknownHostException {
        // SETUP
        ServerSession s = new ServerSession();
        ClientSession c = new ClientSession("127.0.0.1", ServerSession.DEFAULT_PORT);
        Message m = new Message("Alice", "Bob", 0, "This is a test!!!");

        // EXEC
        TryRecieveMessageClient cThread = new TryRecieveMessageClient(c);
        try {
            cThread.start();
            s.waitForConnection();
            s.sendMessage(m);
            cThread.join();
            s.close();
        }
        catch (IOException|InterruptedException ex) {
            assertTrue("Exception when sending message!", false);
        }

        Message recv = cThread.message;

        // VERIFY
        assertTrue("Error running client thread!", cThread.success);
        assertEquals(m.sender, recv.sender);
        assertEquals(m.reciever, recv.reciever);
        assertEquals(m.message, recv.message);
        assertTrue(m.timestamp.equals(recv.timestamp));
    }

    public void testServerRecievesClientMessage() throws UnknownHostException {
        // SETUP
        ServerSession s = new ServerSession();
        ClientSession c = new ClientSession("127.0.0.1", ServerSession.DEFAULT_PORT);
        Message m = new Message("Alice", "Bob", 0, "This is a test!!!");

        // EXEC
        TryRecieveMessageServer sThread = new TryRecieveMessageServer(s);
        try {
            sThread.start();
            Thread.sleep(3000);
            c.connect();
            c.sendMessage(m);
            sThread.join();
        }
        catch (IOException|InterruptedException ex) {
            assertTrue("Exception when sending message!", false);
        }

        Message recv = sThread.message;

        // VERIFY
        assertTrue("Error running client thread!", sThread.success);
        assertEquals(m.sender, recv.sender);
        assertEquals(m.reciever, recv.reciever);
        assertEquals(m.message, recv.message);
        assertTrue(m.timestamp.equals(recv.timestamp));
    }

}
