package ctttest.net;

import canttouchthis.client.ClientSession;
import canttouchthis.common.ChatMessage;
import canttouchthis.common.MessagePacket;
import canttouchthis.server.ServerSession;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Utility threads used for testing ClientSession and ServerSession.
 */
public class NetUtilityThreads {

    private static final String SERVER_ADDR = "127.0.0.1";
    private static final int SERVER_PORT = 50000;

    /**
     * Attempts to connect a ClientSession to a server (after a brief delay), then
     * waits for a message to arrive from the server..
     */
    protected static class TryRecieveMessageClient extends Thread {
        ClientSession c;
        ChatMessage message = null;
        boolean success = false;

        public TryRecieveMessageClient(ClientSession c) {
            this.c = c;
        }

        public void run() {
            try {
                Thread.sleep(3000);
                c.connect(SERVER_ADDR, SERVER_PORT);
                message = (ChatMessage) c.getNextMessage().getContent();
                success = true;
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            c.close();
        }
    }

    /**
     * Starts a ServerSession and waits for a client to connect. Then, waits for the
     * ClientSession to send a ChatMessage.
     */
    protected static class TryRecieveMessageServer extends Thread {
        ServerSession s;
        ChatMessage message = null;
        boolean success = false;

        public TryRecieveMessageServer(ServerSession s) {
            this.s = s;
        }

        public void run() {
            s.waitForConnection();
            try {
                message = (ChatMessage) s.getNextMessage().getContent();
                success = true;
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            s.close();
        }
    }

    /**
     * Generic websocket server (not ServerSession). Starts the server, waits for a connection
     * and (optionally) waits to receive a ChatMessage.
     */
    protected static class WaitForConnection extends Thread {
        int port;
        ChatMessage message;
        boolean success = false;
        boolean tryReadMessage;

        public WaitForConnection(int port, boolean tryReadMessage) {
            this.port = port;
            this.tryReadMessage = tryReadMessage;
        }

        public void run() {
            try {
                ServerSocket s = new ServerSocket(port);
                Socket sock = s.accept();

                if (tryReadMessage) {
                    ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                    message = (ChatMessage) ois.readObject();
                }

                s.close();
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
                success = false;
            }

            success = true;
        }
    }

    /**
     * Generic websocket client (not ClientSession). Attempts to connect to a server at
     * a given address and port (after 3s delay) and optionally tries to get a message
     * from the server.
     */
    protected static class TryConnect extends Thread {
        InetAddress addr;
        int port;
        boolean tryReadMessage;
        ChatMessage message = null;

        boolean success;

        public TryConnect(InetAddress addr, int port, boolean tryReadMessage) {
            this.addr = addr;
            this.port = port;
            this.tryReadMessage = tryReadMessage;
        }

        public void run() {
            Socket s;
            try {
                Thread.sleep(3000);
                s = new Socket(addr, port);

                // try to get a message from the server
                if (tryReadMessage) {
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                    message = (ChatMessage) ois.readObject();
                }

                s.close();
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
                success = false;
            }
            success = true;
        }
    }

}
