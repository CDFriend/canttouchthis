package canttouchthis.ui;

/**
 * Interface for handling Connect events from a ConnectView.
 */
public interface IConnectHandler {

    /**
     * Attempt to do something which may throw an error, and return a String error
     * message if an issue occurs.
     *
     * @return String null if the connection was accepted or an error message.
     */
    public String tryHandleConnect(String host, int port, boolean checkInt, boolean useConf);

}
