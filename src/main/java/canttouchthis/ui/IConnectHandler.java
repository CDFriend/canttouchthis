package canttouchthis.ui;

public interface IConnectHandler {

    /**
     * Attempt to establish a connection and return the result.
     * @return String null if the connection was accepted or an error message.
     */
    public String tryConnect();

}
