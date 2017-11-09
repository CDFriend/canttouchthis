package canttouchthis.common;

import canttouchthis.common.auth.Authenticator;
import canttouchthis.common.auth.Identity;
import canttouchthis.ui.ConversationController;
import canttouchthis.ui.ISendHandler;

import java.io.IOException;
import java.io.EOFException;
import java.sql.SQLException;

public class MessageMonitorThread extends Thread {

    private IChatSession _session;
    private ConversationController _ui;
    private Identity _ident;
    private Authenticator _auth;

    private boolean _alive;

    public MessageMonitorThread(IChatSession sess, ConversationController controller,
                                Identity ident, Authenticator auth) {
        this._session = sess;
        this._ui = controller;
        this._ident = ident;
        this._auth = auth;
    }

    public void run() {
        // Send message when send button clicked
        IChatSession session = this._session;
        Identity ident = this._ident;
        ConversationController ui = this._ui;
        this._ui.setSendHandler(new ISendHandler() {
            @Override
            public void onMessageSend(ChatMessage m) {
                try {
                    m.setIdentity(ident);
                    ui.addMessage(m);
                    session.sendMessage(m);
                }
                catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }
            }
        });

        // Start checking for new messages
        this._alive = true;
        while (this._alive) {
            try {
                ChatMessage m = this._session.getNextMessage();

                // check message identity against database
                if (!(this._auth.verifyIdentity(m.senderIdent))){
                    this._ui.showWarning("Sender could not be verified!");
                }

                this._ui.addMessage(m);
            }
            catch (SQLException ex) {
                ex.printStackTrace(System.err);
                this._ui.showFatal("Error getting data from auth database!");
            }
            catch (EOFException ex) {
                this._ui.showFatal("Other user disconnected!");
            }
            catch (IOException ex) {
                ex.printStackTrace(System.err);
                this._ui.showFatal("Error in websocket transmission!");
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
                this._ui.showFatal("An error occurred: " + ex.getMessage());
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
