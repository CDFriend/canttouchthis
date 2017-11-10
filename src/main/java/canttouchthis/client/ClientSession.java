package canttouchthis.client;

import canttouchthis.common.*;

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


/**
 * Handles messaging and session establishment on the client side.
 */
public class ClientSession implements IChatSession {

    private InetAddress addr;
    private int port;
    private Socket connection;

    private Key sharedSecret;
    boolean useConf;
    boolean useInt; 

    /**
     * Attempts to connect to the socket server on a given address and port.
     *
     * @param addr IP address of the ServerSession.
     * @param port Port the server is running on.
     * @throws UnknownHostException If the host address cannot be found.
     *
     * @return Whether or not the connection was successful.
     */
    public boolean connect(String addr, int port, boolean useConf, boolean useInt) throws UnknownHostException {
        this.addr = InetAddress.getByName(addr);
        this.port = port;
        this.useConf = useConf;
        this.useInt = useInt;

        KeyEstablishment es = new KeyEstablishment();
        Key privKey = es.getPrivateKey();
        Key pubKey = es.getPublicKey();

        //encode keys
        byte[] pubByte = pubKey.getEncoded();

        int flags = 0;
        if (useConf) {
            flags = flags | 1;
        }
        if (useInt) {
            flags = flags | 2;
        }

        // TODO: send flags number to server

          try {
              this.connection = new Socket(this.addr, this.port);
              connection.getOutputStream().write(flags);

              if (useConf == true) {

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
                  //if we got a length of 0, then we didn't get a key, so we don't use encryption
                  if (pubKeyLength == 0) {

                      return true;
                  }


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

                  // Truncate secret key to 16 bytes (for AES symmetric encryption)
                  sharedSecret = new SecretKeySpec(bytey, 0, 16, "AES");

                  //System.out.println(sharedSecret.getEncoded());

                  socketOutputStream.flush();
              }

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
     * Sends a ChatMessage object to the server.
     *
     * @param m ChatMessage to be sent.
     * @throws IOException If an error is encountered sending over the websocket.
     */
    public void sendMessage(ChatMessage m) throws Exception {

        // pack ChatMessage into a MessagePacket (include digest)
        MessagePacket packet = new MessagePacket(m);

        if (useConf == true){
          // initialize cipher
          Cipher c = (new CryptoServices()).getEncryptCipher(sharedSecret);
          CipherOutputStream cipherStream = new CipherOutputStream(connection.getOutputStream(), c);

          // pipe object output into encryption cipher
          ObjectOutputStream oos = new ObjectOutputStream(cipherStream);
          oos.writeObject(packet);
        }
        else {
          ObjectOutputStream nonEncryptedOutput = new ObjectOutputStream(connection.getOutputStream());
          nonEncryptedOutput.writeObject(packet);
        }
    }

    /**
     * Blocks until a new message is received from the server.
     *
     * @return New ChatMessage object from server.
     * @throws IOException If an error is encountered reading from the websocket stream.
     */
    public MessagePacket getNextMessage() throws Exception {

      if (useConf == true){
        Cipher c = (new CryptoServices()).getDecryptCipher(sharedSecret);
        CipherInputStream cipherStream = new CipherInputStream(connection.getInputStream(), c);
        ObjectInputStream ois = new ObjectInputStream(cipherStream);

        try {
            return (MessagePacket) ois.readObject();
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
    else {
      ObjectInputStream nonEncryptedInput = new ObjectInputStream(connection.getInputStream());
      return (MessagePacket) nonEncryptedInput.readObject();
    }
  }

  public boolean usesConfidentiality() {
        return useConf;
  }

  public boolean checksIntegrity() {
        return useInt;
  }

}
