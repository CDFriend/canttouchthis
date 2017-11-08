package canttouchthis.ui;

import canttouchthis.common.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * Manages events between a ConversationModel and a ConversationView.
 * Delegates all UI tasks to the swing event queue.
 */
public class ConversationController implements ActionListener, KeyListener {

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
        this._view.sendField.addKeyListener(this);

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
        this._view.renderMessage(m);
    }

    /**
     * Shows a warning in the chat view.
     * @param message Message to be shown to the user.
     */
    public void showWarning(String message) {
        this._view.renderWarningMessage(message);
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
     * Show a message before closing the application.
     */
    public void showFatal(String msg) {
        JOptionPane.showMessageDialog(this._view,msg);
        System.exit(0);
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
        if (source == _view.sendButton || source == _view.sendField && this.sendHandler != null) {
            onSend();
        }

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            onSend();
        }
    }

    public void keyTyped(KeyEvent e) { }

    public void keyReleased(KeyEvent e) { }

    private void onSend() {
        Message m = new Message(_view.sendField.getText(), System.currentTimeMillis());

        // clear message field
        this._view.sendField.setText("");

        // pass to handler
        this.sendHandler.onMessageSend(m);
    }

}
