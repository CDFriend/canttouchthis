package canttouchthis.server;

import canttouchthis.common.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;



//Exceptions
import java.security.spec.InvalidKeySpecException;

//test


/**
 * Handles sending and recieving ChatMessage objects and key exchange on the
 * server side.
 */
public class ServerSession implements IChatSession {

    public static final int DEFAULT_PORT = 50000;

    public int port;

    private ServerSocket server;
    private Socket channel;

    private Key sharedSecret;

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
        byte[] pubByte = pubKey.getEncoded();

        try {

            server = new ServerSocket(port);
            s = server.accept();
            //Create public key exchange object
            //call for send
            //call for listen


            //receive client pub key in form of byte[]
            InputStream serverInputStream = s.getInputStream();
            DataInputStream dataIn = new DataInputStream(serverInputStream);
            int pubKeyLength = dataIn.readInt();
            byte[] clientPubKeyByte = new byte[pubKeyLength];
            dataIn.read(clientPubKeyByte, 0, pubKeyLength);

            //rebuild the public key from the opposite side
            KeyFactory fact = KeyFactory.getInstance("DH");

            Key clientPublicKey = fact.generatePublic(new X509EncodedKeySpec(clientPubKeyByte));

//            String str = Base64.getEncoder().encodeToString(clientPubKeyByte);
//            System.out.println(str.length());
//            System.out.println(str);



            //send our own length over
            OutputStream socketLengthStream = s.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(socketLengthStream);

            //System.out.println(pubByte.length);


            dataOut.writeInt(pubByte.length);
            //too soon? overwriting the write with 0?
            socketLengthStream.flush();

            //String str = Base64.getEncoder().encodeToString(pubByte);
            //System.out.println(str.length());
            //System.out.println(str);


            //Send our own pub key byte[]
            OutputStream socketOutputStream = s.getOutputStream();
            DataOutputStream data = new DataOutputStream(socketOutputStream);
            data.write(pubByte, 0, pubByte.length);


            //establish KeyAgreement
            KeyAgreement keyAgree = KeyAgreement.getInstance("DiffieHellman");
            keyAgree.init(privKey);

            //creates a noneKey to use if we were doing this step multiple times. we are not.
            keyAgree.doPhase(clientPublicKey, true);

            //Create shared secret to use
            byte[] bytey = keyAgree.generateSecret();

            // Truncate key at 16 bytes (128 bits) for AES
            sharedSecret = new SecretKeySpec(bytey, 0, 16, "AES");

            //System.out.println(sharedSecret.getEncoded());

            socketOutputStream.flush();


        }
        catch (IOException|NoSuchAlgorithmException|InvalidKeyException|InvalidKeySpecException ex) {
            ex.printStackTrace();
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
     * @param m ChatMessage object to be sent.
     * @throws IOException If an error occurs when writing the message to the websocket channel.
     */
    public void sendMessage(ChatMessage m) throws Exception {

        // pack ChatMessage into MessagePacket (include Digest)
        MessagePacket packet = new MessagePacket(m);

        // initialize encryption cipher
        Cipher c = (new CryptoServices()).getEncryptCipher(sharedSecret);
        CipherOutputStream cipherStream = new CipherOutputStream(channel.getOutputStream(), c);

        // initialize pipe and write to object output stream
        ObjectOutputStream oos = new ObjectOutputStream(cipherStream);
        oos.writeObject(packet);

    }

    /**
     * Blocks until a new ChatMessage object is received from the client.
     *
     * @return ChatMessage from the ClientSession.
     * @throws IOException If any errors occur when getting the socket input stream.
     */
    public ChatMessage getNextMessage() throws Exception {
        Cipher c = (new CryptoServices()).getDecryptCipher(sharedSecret);
        CipherInputStream cipherStream = new CipherInputStream(channel.getInputStream(), c);

        ObjectInputStream ois = new ObjectInputStream(cipherStream);

        try {
            return (ChatMessage) ((MessagePacket) ois.readObject()).getContent();
        }
        catch (ClassCastException ex) {
            System.out.printf("Got unexpected class from socket!");
            ex.printStackTrace(System.err);
            return null;
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }

}
