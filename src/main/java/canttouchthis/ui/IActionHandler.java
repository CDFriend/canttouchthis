package canttouchthis.ui;

public interface IActionHandler {

    /**
     * Attempt to do something which may throw an error, and return a String error
     * message if an issue occurs.
     *
     * @return String null if the connection was accepted or an error message.
     */
    public String tryHandleAction();

}
