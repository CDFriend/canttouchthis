package canttouchthis.ui;

import canttouchthis.common.Message;
import canttouchthis.ui.ConversationModel;
import canttouchthis.ui.ConversationView;

/**
 * Manages events between a ConversationModel and a ConversationView.
 */
public class ConversationController {

    public ConversationModel model;
    public ConversationView view;

    public ConversationController(ConversationView view, ConversationModel model) {
        this.model = model;
        this.view = view;
        this.view.model = model;

        this.view.updateConversation();
    }

    public void addMessage(Message m) {
        this.model.addMessage(m);
        this.view.updateConversation();
    }

}
