package canttouchthis.server;

import java.io.IOException;
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

    /**
     * Block until a connection with a client has been established.
     * Initializes server and channel objects.
     */
    public void waitForConnection() throws IOException {

        // Create a new server object. May throw an IOException if there is
        // an error opening the port.
        server = new ServerSocket(port);

        // Block until a TCP connection is established.
        Socket s = server.accept();

        //TODO: check authentication and perform key exchange

        channel = s;

    }

}
