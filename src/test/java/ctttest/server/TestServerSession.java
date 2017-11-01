package ctttest.server;

import canttouchthis.server.ServerSession;

import java.io.IOException;
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

    /**
     * Server should accept incoming connections.
     * @throws UnknownHostException If localhost cannot be found.
     */
    public void testServerAcceptsConnection() throws UnknownHostException {

        InetAddress addr = InetAddress.getLocalHost();
        TryConnect conThread = new TryConnect(addr, ServerSession.DEFAULT_PORT);

        conThread.start();
        boolean success = session.waitForConnection();

        assertTrue(success);

    }

    private class TryConnect extends Thread {
        InetAddress addr;
        int port;

        boolean success;

        public TryConnect(InetAddress addr, int port) {
            this.addr = addr;
            this.port = port;
        }

        public void run() {
            try {
                Socket s = new Socket(addr, port);
                s.close();
            }
            catch (IOException ex) {
                success = false;
            }
            success = true;
        }
    }

}
