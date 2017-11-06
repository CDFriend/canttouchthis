import java.security.*;
import javax.crypto.*;
public class KeyEstablishment{

	KeyPair key;


	public KeyEstablishment(){
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		key = keyGen.generateKeyPair();
	}


	public PrivateKey getPrivateKey(){
		return key.getPrivate();
	}


	public PublicKey getPublicKey(){
		return key.getPublic();
	}


	public static void main(String[] args){

		KeyEstablishment keyEs = new KeyEstablishment();
		PrivateKey privateKey = keyEs.getPrivateKey();
		PublicKey publicKey = keyEs.getPublicKey();

	}


}
