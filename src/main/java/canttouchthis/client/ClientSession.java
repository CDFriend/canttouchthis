package canttouchthis.client;

import canttouchthis.common.IChatSession;
import canttouchthis.common.Message;
import canttouchthis.common.KeyEstablishment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;

/**
 * Handles messaging and session establishment on the client side.
 */
public class ClientSession implements IChatSession {

    private InetAddress addr;
    private int port;
    private Socket connection;

    /**
     * Attempts to connect to the socket server on a given address and port.
     *
     * @param addr IP address of the ServerSession.
     * @param port Port the server is running on.
     * @throws UnknownHostException If the host address cannot be found.
     *
     * @return Whether or not the connection was successful.
     */
    public boolean connect(String addr, int port) throws UnknownHostException {
        this.addr = InetAddress.getByName(addr);
        this.port = port;

        KeyEstablishment es = new KeyEstablishment();
        Key privKey = es.getPrivateKey();
        Key pubKey = es.getPublicKey();

        //encode keys

        //System.out.println(pubByteStr);

        byte[] pubByte = pubKey.getEncoded();
        //String pubFormat = pubKey.getFormat();

        //System.out.println(pubFormat);

        try {
            this.connection = new Socket(this.addr, this.port);
            OutputStream socketOutputStream = connection.getOutputStream();
            socketOutputStream.write(pubByte);

            //wait for server to send their public key byte[]
            InputStream serverInputStream = connection.getInputStream();
            byte[] serverPubKeyByte = new byte[1024];
            int q = serverInputStream.read(serverPubKeyByte);

            //rebuild the public key from the opposite side
            Key serverPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(serverPubKeyByte));

            //establish KeyAgreement
            KeyAgreement keyAgree = new KeyAgreement(privKey);

            //creates a noneKey to use if we were doing this step multiple times. we are not.
            Key noneKey = keyAgree.doPhase(serverPublicKey, true);

            //Create shared secret to use
            Key sharedSecret = keyAgree.generateSecret("AES");

        }
        catch (IOException ex) {
            return false;
        }
        return true;
    }

    /**
     * Closes the connection with the server.
     */
    public void close() {
        try {
            this.connection.close();
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Sends a Message object to the server.
     *
     * @param m Message to be sent.
     * @throws IOException If an error is encountered sending over the websocket.
     */
    public void sendMessage(Message m) throws IOException {
        // TODO: encrypt object before writing
        ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
        oos.writeObject(m);
    }

    /**
     * Blocks until a new message is received from the server.
     *
     * @return New Message object from server.
     * @throws IOException If an error is encountered reading from the websocket stream.
     */
    public Message getNextMessage() throws IOException {
        // TODO decrypt object before reading
        ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());

        try {
            // TODO: what if we get sent an object that's not a Message?
            return (Message) ois.readObject();
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }

}
