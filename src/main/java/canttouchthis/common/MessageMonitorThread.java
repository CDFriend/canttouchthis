package canttouchthis.common;

import canttouchthis.common.auth.Identity;
import canttouchthis.ui.ConversationController;
import canttouchthis.ui.ISendHandler;

import java.io.IOException;
import java.io.EOFException;

public class MessageMonitorThread extends Thread {

    private IChatSession _session;
    private ConversationController _ui;
    private Identity _ident;

    private boolean _alive;

    public MessageMonitorThread(IChatSession sess, ConversationController controller,
                                Identity ident) {
        this._session = sess;
        this._ui = controller;
        this._ident = ident;
    }

    public void run() {
        // Send message when send button clicked
        IChatSession session = this._session;
        Identity ident = this._ident;
        ConversationController ui = this._ui;
        this._ui.setSendHandler(new ISendHandler() {
            @Override
            public void onMessageSend(Message m) {
                try {
                    m.setIdentity(ident);
                    ui.addMessage(m);
                    session.sendMessage(m);
                }
                catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        });

        // Start checking for new messages
        this._alive = true;
        while (this._alive) {
            try {
                Message m = this._session.getNextMessage();
                this._ui.addMessage(m);
            }
            catch (EOFException ex) {
                this._ui.showDisconnect();
            }
            catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    public void kill() {
        this._alive = false;
        try {
            join();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
    }

}
