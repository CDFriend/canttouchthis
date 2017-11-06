package canttouchthis.ui;

import canttouchthis.common.Message;

/**
 * Implement this to handle a new message being sent from a ConversationView.
 */
public interface ISendHandler {

    /**
     * Called when the "send" button is pressed in the UI.
     * @param m Message object from the UI.
     */
    void onMessageSend(Message m);

}
