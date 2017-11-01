package canttouchthis.server;

import canttouchthis.common.Message;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSession {

    public static final int DEFAULT_PORT = 50000;

    public int port;

    private ServerSocket server;
    private Socket channel;

    /**
     * Create a server on the default port.
     */
    public ServerSession() {
        this(DEFAULT_PORT);
    }

    /**
     * Create a server on a custom port.
     * @param port Port number to listen on.
     */
    public ServerSession(int port) {
        this.port = port;
    }

    public void close() {
        try {
            this.channel.close();
            this.server.close();
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Block until a connection with a client has been established.
     * Initializes server and channel objects.
     *
     * @return True if the connection was successfully established and false if
     * it failed.
     */
    public boolean waitForConnection() {

        // Create a new server object. May throw an IOException if there is
        // an error opening the port.

        // Block until a TCP connection is established.
        Socket s;
        try {
            server = new ServerSocket(port);
            s = server.accept();
        }
        catch (IOException ex) {
            return false;
        }

        //TODO: check authentication and perform key exchange

        channel = s;
        return true;

    }

    public void sendMessage(Message m) throws IOException {
        // TODO: encrypt object before writing
        ObjectOutputStream oos = new ObjectOutputStream(channel.getOutputStream());
        oos.writeObject(m);
    }

}
