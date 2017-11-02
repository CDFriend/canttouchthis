package ctttest.net;

import canttouchthis.client.ClientSession;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

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
        WaitForConnection server = new WaitForConnection(50000);
        server.start();

        // EXEC
        sess.connect();

        // VERIFY
        boolean success = true;
        try {
            server.join();
        }
        catch (InterruptedException ex) {
            success = false;
        }

        assertTrue(success);
        assertTrue(server.success);
    }

    private class WaitForConnection extends Thread {
        int port;
        boolean success = false;
        public WaitForConnection(int port) {
            this.port = port;
        }

        public void run() {
            try {
                ServerSocket s = new ServerSocket(port);
                s.accept();
                s.close();
            }
            catch (IOException ex) {
                success = false;
            }

            success = true;
        }
    }

}
