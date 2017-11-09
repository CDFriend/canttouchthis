package canttouchthis.client;

import canttouchthis.common.CryptoServices;
import canttouchthis.common.IChatSession;
import canttouchthis.common.Message;
import canttouchthis.common.KeyEstablishment;

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
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

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

    private Key sharedSecret;

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
        byte[] pubByte = pubKey.getEncoded();
        //System.out.println(pubKey.getFormat());

        //String str = Base64.getEncoder().encodeToString(pubByte);
        //System.out.println(str.length());
        //System.out.println(str);


        try {

            this.connection = new Socket(this.addr, this.port);


            //send length
            int length = pubByte.length;

            //if something, then call on public key exchange
            //PublicKeyExchange(pubKey, length)

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
            //System.out.println(pubKeyLength);

            byte[] serverPubKeyByte = new byte[pubKeyLength];
            dataIn.read(serverPubKeyByte, 0, pubKeyLength);


            //String str = Base64.getEncoder().encodeToString(serverPubKeyByte);
            //System.out.println(str.length());
            //System.out.println(str);


            //rebuild the public key from the opposite side
            X509EncodedKeySpec x509spec = new X509EncodedKeySpec(serverPubKeyByte);
            Key serverPublicKey = KeyFactory.getInstance("DiffieHellman").generatePublic(x509spec);

            //establish KeyAgreement - SunJCE
            KeyAgreement keyAgree = KeyAgreement.getInstance("DiffieHellman");
            keyAgree.init(privKey);
            keyAgree.doPhase(serverPublicKey, true);

            //Create shared secret to use
            byte[] bytey = keyAgree.generateSecret();

            // Truncate secret key to 16 bytes (for AES symmetric encryption)
            sharedSecret = new SecretKeySpec(bytey, 0, 16, "AES");

            //System.out.println(sharedSecret.getEncoded());

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
    public void sendMessage(Message m) throws Exception {

        // initialize cipher
        Cipher c = (new CryptoServices()).getEncryptCipher(sharedSecret);
        CipherOutputStream cipherStream = new CipherOutputStream(connection.getOutputStream(), c);

        // pipe object output into encryption cipher
        ObjectOutputStream oos = new ObjectOutputStream(cipherStream);
        oos.writeObject(m);

    }

    /**
     * Blocks until a new message is received from the server.
     *
     * @return New Message object from server.
     * @throws IOException If an error is encountered reading from the websocket stream.
     */
    public Message getNextMessage() throws Exception {

        Cipher c = (new CryptoServices()).getDecryptCipher(sharedSecret);
        CipherInputStream cipherStream = new CipherInputStream(connection.getInputStream(), c);
        ObjectInputStream ois = new ObjectInputStream(cipherStream);

        try {
            // TODO: what if we get sent an object that's not a Message?
            return (Message) ois.readObject();
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }

}
