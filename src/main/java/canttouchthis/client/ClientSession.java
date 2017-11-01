package canttouchthis.client;

import canttouchthis.common.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSession {

    private InetAddress addr;
    private int port;
    private Socket connection;

    public ClientSession(String addr, int port) throws UnknownHostException {
        this.addr = InetAddress.getByName(addr);
        this.port = port;
    }

    public boolean connect() {
        try {
            this.connection = new Socket(this.addr, this.port);
        }
        catch (IOException ex) {
            return false;
        }
        return true;
    }

    public void close() {
        try {
            this.connection.close();
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void sendMessage(Message m) throws IOException {
        // TODO: encrypt object before writing
        ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
        oos.writeObject(m);
    }

}
