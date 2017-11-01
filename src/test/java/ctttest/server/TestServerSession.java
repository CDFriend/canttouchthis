package ctttest.server;

import canttouchthis.common.Message;
import canttouchthis.server.ServerSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.*;

public class TestServerSession extends TestCase {

    ServerSession session;

    public void setUp() throws Exception {
        super.setUp();
        session = new ServerSession();
    }

    public void tearDown() {
        session.close();
    }

    /**
     * Server should accept incoming connections.
     * @throws UnknownHostException If localhost cannot be found.
     */
    public void testServerAcceptsConnection() throws UnknownHostException {

        InetAddress addr = InetAddress.getLocalHost();
        TryConnect conThread = new TryConnect(addr, ServerSession.DEFAULT_PORT, false);

        conThread.start();
        boolean success = session.waitForConnection();

        try {
            conThread.join();
        }
        catch (InterruptedException ex) {
            assertTrue("Connection thread interrupted!", false);
        }

        assertTrue(success);

    }

    /**
     * Checks that messages are serialized properly and can be read by a client.
     */
    public void testMessageSerialization() throws UnknownHostException {
        // SETUP
        Message m = new Message("Alice", "Bob", 0, "This is a test!!!");
        TryConnect conThread = new TryConnect(InetAddress.getLocalHost(), ServerSession.DEFAULT_PORT, true);

        // EXEC
        conThread.start();
        assertTrue(session.waitForConnection());
        boolean success = true;
        try {
            session.sendMessage(m);
        }
        catch (IOException ex) {
            success = false;
        }

        try {
            conThread.join();
        }
        catch (InterruptedException ex) {
            assertTrue("Connection thread interrupted!", false);
        }

        Message recv = conThread.message;

        // VERIFY
        assertTrue(success);
        assertEquals(m.sender, recv.sender);
        assertEquals(m.reciever, recv.reciever);
        assertEquals(m.message, recv.message);
        assertTrue(m.timestamp.equals(recv.timestamp));
    }

    private class TryConnect extends Thread {
        InetAddress addr;
        int port;
        boolean tryReadMessage;
        Message message = null;

        boolean success;

        public TryConnect(InetAddress addr, int port, boolean tryReadMessage) {
            this.addr = addr;
            this.port = port;
            this.tryReadMessage = tryReadMessage;
        }

        public void run() {
            try {
                Socket s = new Socket(addr, port);

                // try to get a message from the server
                if (tryReadMessage) {
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                    message = (Message) ois.readObject();
                }
                else {
                    s.close();
                }
            }
            catch (IOException|ClassNotFoundException ex) {
                success = false;
            }
            success = true;
        }
    }

}
