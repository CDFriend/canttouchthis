package canttouchthis.ui;

import canttouchthis.common.Message;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Model containing all messages to be shown in a ConversationView.
 */
public class ConversationModel {

    private final int INITIAL_CAPACITY = 200;

    private ArrayList<Message> messages;

    public ConversationModel() {
        messages = new ArrayList<>(INITIAL_CAPACITY);
    }

    public void addMessage(Message m) {
        messages.add(m);
    }

    public Iterator<Message> messages() {
        return messages.iterator();
    }

    public Message getLastMessage() {
        return messages.get(messages.size() - 1);
    }

}
