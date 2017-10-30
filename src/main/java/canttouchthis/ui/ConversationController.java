package canttouchthis.ui;

import canttouchthis.common.Message;
import canttouchthis.ui.ConversationModel;
import canttouchthis.ui.ConversationView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Manages events between a ConversationModel and a ConversationView.
 */
public class ConversationController implements ActionListener {

    protected ConversationModel model;
    protected ConversationView view;

    private ISendHandler sendHandler;

    public ConversationController(ConversationView view, ConversationModel model) {
        this.model = model;
        this.view = view;
        this.view.model = model;

        // must explicitly set send handler using setSendHandler()
        this.sendHandler = null;

        // add action listener for send button
        this.view.sendButton.addActionListener(this);

        this.view.updateConversation();
    }

    public void addMessage(Message m) {
        this.model.addMessage(m);
        this.view.updateConversation();
    }

    public void setSendHandler(ISendHandler sendHandler) {
        this.sendHandler = sendHandler;
    }

    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        // if send button was pressed and handler is set
        if (source == view.sendButton && this.sendHandler != null) {

            // TODO: make more descriptive identities
            Message m = new Message("Alice", "Bob", System.currentTimeMillis(),
                    view.sendField.getText());

            this.sendHandler.onMessageSend(m);

        }

    }

}
