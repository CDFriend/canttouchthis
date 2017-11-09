/*package canttouchthis.common;

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

public class PublicKeyExchange{


  public PublicKeyExchange(Key publicKey, int length){
    this.length = length;
    this.publicKey = publicKey;
  }

  public void sendPublicKey(){
    sendPublicKeyLength(length);
  }

  private void sendPublicKeyLength(){
    OutputStream socketOutputStream = connection.getOutputStream();
    DataOutputStream data = new DataOutputStream(socketOutputStream);
    data.writeInt(length);
    socketOutputStream.flush();
  }

  public void receivePublicKey(){

  }




//Create public key exchange object
//call for send

OutputStream socketStream = connection.getOutputStream();
DataOutputStream dataStream = new DataOutputStream(socketStream);
dataStream.write(pubByte, 0, pubByte.length);

//wait for server to send their public key byte[]
InputStream serverInputStream = connection.getInputStream();
byte[] serverPubKeyByte = new byte[2048];
int q = serverInputStream.read(serverPubKeyByte);

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

System.out.println(sharedSecret.getEncoded());

socketOutputStream.flush();



//server






//Create public key exchange object
//call for send
//call for listen


//receive client pub key in form of byte[]
InputStream serverInputStream = s.getInputStream();
DataInputStream dataIn = new DataInputStream(serverInputStream);

int pubKeyLength = dataIn.readInt();

//System.out.println(read);

byte[] clientPubKeyByte = new byte[pubKeyLength];

dataIn.read(clientPubKeyByte, 0, pubKeyLength);


//must trim padding off of the string (i.e. AAAAAAAAA...AAA on the end of the sent string)
String str = Base64.getEncoder().encodeToString(clientPubKeyByte);
//String result = str.substring(0, 1084);

//System.out.println(str);

//Send our own pub key byte[]
OutputStream socketOutputStream = s.getOutputStream();
DataOutputStream data = new DataOutputStream(socketOutputStream);
data.write(pubByte, 0, pubByte.length);

//rebuild the public key from the opposite side
KeyFactory fact = KeyFactory.getInstance("DH");


Key clientPublicKey = fact.generatePublic(new X509EncodedKeySpec(clientPubKeyByte));

//establish KeyAgreement
KeyAgreement keyAgree = KeyAgreement.getInstance("DiffieHellman");
keyAgree.init(privKey);

//creates a noneKey to use if we were doing this step multiple times. we are not.
keyAgree.doPhase(clientPublicKey, true);

//Create shared secret to use
byte[] bytey = keyAgree.generateSecret();

Key sharedSecret = new SecretKeySpec(bytey, 0, bytey.length, "AES");

System.out.println(sharedSecret.getEncoded());

socketOutputStream.flush();











//Create public key exchange object
//call for send
//call for listen


//receive client pub key in form of byte[]
InputStream serverInputStream = s.getInputStream();
DataInputStream dataIn = new DataInputStream(serverInputStream);

int pubKeyLength = dataIn.readInt();

//System.out.println(read);

byte[] clientPubKeyByte = new byte[pubKeyLength];

dataIn.read(clientPubKeyByte, 0, pubKeyLength);


//must trim padding off of the string (i.e. AAAAAAAAA...AAA on the end of the sent string)
String str = Base64.getEncoder().encodeToString(clientPubKeyByte);
//String result = str.substring(0, 1084);

//System.out.println(str);

//Send our own pub key byte[]
OutputStream socketOutputStream = s.getOutputStream();
DataOutputStream data = new DataOutputStream(socketOutputStream);
data.write(pubByte, 0, pubByte.length);

//rebuild the public key from the opposite side
KeyFactory fact = KeyFactory.getInstance("DH");


Key clientPublicKey = fact.generatePublic(new X509EncodedKeySpec(clientPubKeyByte));

//establish KeyAgreement
KeyAgreement keyAgree = KeyAgreement.getInstance("DiffieHellman");
keyAgree.init(privKey);

//creates a noneKey to use if we were doing this step multiple times. we are not.
keyAgree.doPhase(clientPublicKey, true);

//Create shared secret to use
byte[] bytey = keyAgree.generateSecret();

Key sharedSecret = new SecretKeySpec(bytey, 0, bytey.length, "AES");

System.out.println(sharedSecret.getEncoded());

socketOutputStream.flush();











}*/
