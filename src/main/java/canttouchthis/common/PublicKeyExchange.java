package canttouchthis.common;

import java.io.IOException;
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

public class PublicKeyExchange{

  OutputStream socketStream;
  DataOutputStream dataStream;
  InputStream connectionInputStream;
  DataInputStream dataIn;
  X509EncodedKeySpec x509spec;
  byte[] otherPubKeyByte;
  Key otherPublicKey;
  KeyAgreement keyAgree;
  byte[] generatedSecretByte;
  Socket conn;
  int length;
  Key publicKey;
  Key sharedSecret;

  public PublicKeyExchange(Key publicKey, int length, Socket conn){
    this.length = length;
    this.publicKey = publicKey;
    this.conn = conn;
  }

  public void sendPublicKey(){
      sendPublicKeyLength();
      //Create public key exchange object
      //call for send
      byte[] pubByte = publicKey.getEncoded();

      try{
        socketStream = conn.getOutputStream();
        dataStream = new DataOutputStream(socketStream);
        dataStream.write(pubByte, 0, length);
      }
      catch(IOException ex){
        ex.printStackTrace();
        System.exit(-1);
      }

  }

  private void sendPublicKeyLength(){

    try{
      socketStream = conn.getOutputStream();
      dataStream = new DataOutputStream(socketStream);
      dataStream.writeInt(length);
      socketStream.flush();
    }
    catch(IOException ex){
      ex.printStackTrace();
      System.exit(-1);
    }
  }

  public void receivePublicKey(){
    try{
      connectionInputStream = conn.getInputStream();
      dataIn = new DataInputStream(connectionInputStream);
      int pubKeyLength = dataIn.readInt();
      otherPubKeyByte = new byte[pubKeyLength];
      dataIn.read(otherPubKeyByte, 0, pubKeyLength);
      x509spec = new X509EncodedKeySpec(otherPubKeyByte);
      otherPublicKey = KeyFactory.getInstance("DiffieHellman").generatePublic(x509spec);
      }
      catch (IOException|NoSuchAlgorithmException|InvalidKeySpecException ex){
        ex.printStackTrace();
        System.exit(-1);
      }

  }

  public Key establishKeyAgreement(Key privKey){
      //establish KeyAgreement
      try{
        keyAgree = KeyAgreement.getInstance("DiffieHellman");
        keyAgree.init(privKey);
        keyAgree.doPhase(otherPublicKey, true);

      //Create shared secret to use
        byte[] generatedSecretByte = keyAgree.generateSecret();
        sharedSecret = new SecretKeySpec(generatedSecretByte, 0, generatedSecretByte.length, "AES");
        socketStream.flush();

      }
      catch (NoSuchAlgorithmException|IOException|InvalidKeyException ex){
        ex.printStackTrace();
        System.exit(-1);
      }
      return sharedSecret;
  }

/*
  catch (NoSuchAlgorithmException|InvalidKeyException|InvalidKeySpecException ex) {
      ex.printStackTrace();
      return false;
  }
*/






}
