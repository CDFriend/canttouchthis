package canttouchthis.ui;

import canttouchthis.common.Message;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manages events between a ConversationModel and a ConversationView.
 */
public class ConversationController implements ActionListener {

    protected ConversationModel _model;
    protected ConversationView _view;

    private ISendHandler sendHandler;

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

    public void setSendHandler(ISendHandler sendHandler) {
        this.sendHandler = sendHandler;
    }

    public void addMessage(Message m) {
        this._model.addMessage(m);
        this._view.updateConversation();
    }

    public void showView() {
        ConversationView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(true);
            }
        });
    }

    public void hideView() {
        ConversationView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(false);
            }
        });
    }

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
