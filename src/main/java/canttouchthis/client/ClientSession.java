package canttouchthis.client;

import canttouchthis.common.IChatSession;
import canttouchthis.common.Message;
import canttouchthis.common.KeyEstablishment;
import canttouchthis.common.PublicKeyExchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;

//Exceptions
import java.security.spec.InvalidKeySpecException;

//test stuff

import java.util.Base64;


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
    public boolean connect(String addr, int port, boolean useConf) throws UnknownHostException {
        this.addr = InetAddress.getByName(addr);
        this.port = port;

        KeyEstablishment es = new KeyEstablishment();
        Key privKey = es.getPrivateKey();
        Key pubKey = es.getPublicKey();

        //encode public key
        byte[] pubByte = pubKey.getEncoded();



        try {

            this.connection = new Socket(this.addr, this.port);

            if (useConf == true){
              //if something, then call on public key exchange
              int length = pubByte.length;
              PublicKeyExchange pubKeyExchange = new PublicKeyExchange(pubKey, length, connection);
              pubKeyExchange.sendPublicKey();
              pubKeyExchange.receivePublicKey();
              Key sharedSecret = pubKeyExchange.establishKeyAgreement(privKey);

            }
            //send length




            OutputStream socketOutputStream = connection.getOutputStream();
            DataOutputStream data = new DataOutputStream(socketOutputStream);
            data.writeInt(length);

            socketOutputStream.flush();

            //Create public key exchange object
            //call for send

            OutputStream socketStream = connection.getOutputStream();
            DataOutputStream dataStream = new DataOutputStream(socketStream);
            dataStream.write(pubByte, 0, pubByte.length);



            //wait for server to send their public key byte[]
            InputStream serverInputStream = connection.getInputStream();
            DataInputStream dataIn = new DataInputStream(serverInputStream);
            int pubKeyLength = dataIn.readInt();
            byte[] serverPubKeyByte = new byte[pubKeyLength];
            dataIn.read(serverPubKeyByte, 0, pubKeyLength);


            //rebuild the public key from the opposite side
            X509EncodedKeySpec x509spec = new X509EncodedKeySpec(serverPubKeyByte);
            Key serverPublicKey = KeyFactory.getInstance("DiffieHellman").generatePublic(x509spec);

            //establish KeyAgreement - SunJCE
            KeyAgreement keyAgree = KeyAgreement.getInstance("DiffieHellman");
            keyAgree.init(privKey);
            keyAgree.doPhase(serverPublicKey, true);

            //Create shared secret to use
            byte[] bytey = keyAgree.generateSecret();

            Key sharedSecret = new SecretKeySpec(bytey, 0, bytey.length, "AES");

            socketOutputStream.flush();

        }
        catch (IOException|NoSuchAlgorithmException|InvalidKeyException|InvalidKeySpecException ex) {
            ex.printStackTrace();
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
