package canttouchthis.ui;

/**
 * Interface for handling Login events from a LoginView.
 */
public interface ILoginHandler {

    /**
     * Attempt to do something which may throw an error, and return a String error
     * message if an issue occurs.
     *
     * @return String null if the connection was accepted or an error message.
     */
    public String tryHandleLogin(String username, String password);

}
