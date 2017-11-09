package canttouchthis.ui;

import canttouchthis.common.ChatMessage;

/**
 * Implement this to handle a new message being sent from a ConversationView.
 */
public interface ISendHandler {

    /**
     * Called when the "send" button is pressed in the UI.
     * @param m ChatMessage object from the UI.
     */
    void onMessageSend(ChatMessage m);

}
