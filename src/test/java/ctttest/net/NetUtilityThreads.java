package ctttest.net;

import canttouchthis.client.ClientSession;
import canttouchthis.common.Message;
import canttouchthis.server.ServerSession;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NetUtilityThreads {

    protected static class TryRecieveMessageClient extends Thread {
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

    protected static class TryRecieveMessageServer extends Thread {
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

    protected static class WaitForConnection extends Thread {
        int port;
        Message message;
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
                    message = (Message) ois.readObject();
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

    protected static class TryConnect extends Thread {
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
            Socket s;
            try {
                Thread.sleep(3000);
                s = new Socket(addr, port);

                // try to get a message from the server
                if (tryReadMessage) {
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                    message = (Message) ois.readObject();
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
