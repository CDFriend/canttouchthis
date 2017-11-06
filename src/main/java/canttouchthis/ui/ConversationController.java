package canttouchthis.ui;

import canttouchthis.common.Message;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manages events between a ConversationModel and a ConversationView.
 * Delegates all UI tasks to the swing event queue.
 */
public class ConversationController implements ActionListener {

    protected ConversationModel _model;
    protected ConversationView _view;

    private ISendHandler sendHandler;

    /**
     * Create a new controller for a given ConversationView and ConversationModel.
     *
     * @param view UI elements to display the conversation.
     * @param model Data structure containing all messages in the conversation.
     */
    public ConversationController(ConversationView view, ConversationModel model) {
        this._model = model;
        this._view = view;
        this._view.model = model;

        // must explicitly set send handler using setSendHandler()
        this.sendHandler = null;

        // add action listener for send button
        this._view.sendButton.addActionListener(this);

        this._view.updateConversation();
    }

    /**
     * Sets a handler for a message send event from the UI.
     *
     * @param sendHandler ISendHandler object which processes a Message object.
     */
    public void setSendHandler(ISendHandler sendHandler) {
        this.sendHandler = sendHandler;
    }

    /**
     * Adds a message to the model and updates the conversation view.
     *
     * @param m New message to be added.
     */
    public void addMessage(Message m) {
        this._model.addMessage(m);
        this._view.updateConversation();
    }

    /**
     * Issues an event telling the UI thread to show the chat view.
     */
    public void showView() {
        ConversationView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(true);
            }
        });
    }

    /**
     * Issues an event telling the UI thread to hide the chat view.
     */
    public void hideView() {
        ConversationView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(false);
            }
        });
    }

    /**
     * Handles events from the send button. Creates a message and sends it to the
     * assigned SendHandler, if available.
     *
     * @param e Event coming from send button.
     */
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        // if send button was pressed and handler is set
        if (source == _view.sendButton && this.sendHandler != null) {

            // TODO: make more descriptive identities
            Message m = new Message("Alice", "Bob", System.currentTimeMillis(),
                    _view.sendField.getText());

            this.addMessage(m);

            this.sendHandler.onMessageSend(m);

        }

    }

}
