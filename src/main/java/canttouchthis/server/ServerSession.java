package canttouchthis.server;

import canttouchthis.common.Message;
import canttouchthis.common.IChatSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Handles sending and recieving Message objects and key exchange on the
 * server side.
 */
public class ServerSession implements IChatSession {

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

    public String getAddress() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException ex) {
            return "127.0.0.1";
        }
    }

    /**
     * Begin listening on the desired port for websocket connections.
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

        //Get private and public keys for KeyAgreement
        KeyEstablishment es = new KeyEstablishment();
        Key privKey = es.getPrivateKey();
        Key pubKey = es.getPublicKey();

        try {
            server = new ServerSocket(port);
            s = server.accept();

            //KeyAgreement stuff - must send public key 
        }
        catch (IOException ex) {
            return false;
        }

        //TODO: check authentication and perform key exchange

        channel = s;
        return true;

    }

    /**
     * Closes the server session.  The server will continue to use its port until
     * this is called!.
     */
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
     * Sends a message object to the connected ClientSession.
     *
     * @param m Message object to be sent.
     * @throws IOException If an error occurs when writing the message to the websocket channel.
     */
    public void sendMessage(Message m) throws IOException {
        // TODO: encrypt object before writing
        ObjectOutputStream oos = new ObjectOutputStream(channel.getOutputStream());
        oos.writeObject(m);
    }

    /**
     * Blocks until a new Message object is received from the client.
     *
     * @return Message from the ClientSession.
     * @throws IOException If any errors occur when getting the socket input stream.
     */
    public Message getNextMessage() throws IOException {
        // TODO decrypt object before reading
        ObjectInputStream ois = new ObjectInputStream(channel.getInputStream());

        try {
            // TODO: what if we get sent an object that's not a Message?
            return (Message) ois.readObject();
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }

}
