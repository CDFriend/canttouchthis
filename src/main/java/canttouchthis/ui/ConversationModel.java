package canttouchthis.ui;

import canttouchthis.common.ChatMessage;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Model containing all messages to be shown in a ConversationView.
 */
public class ConversationModel {

    private final int INITIAL_CAPACITY = 200;

    private ArrayList<ChatMessage> messages;

    public ConversationModel() {
        messages = new ArrayList<>(INITIAL_CAPACITY);
    }

    public void addMessage(ChatMessage m) {
        messages.add(m);
    }

    public Iterator<ChatMessage> messages() {
        return messages.iterator();
    }

    public ChatMessage getLastMessage() {
        return messages.get(messages.size() - 1);
    }

}
