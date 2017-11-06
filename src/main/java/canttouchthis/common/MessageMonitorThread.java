package canttouchthis.common;

import canttouchthis.ui.ConversationController;

import java.io.IOException;

public class MessageMonitorThread extends Thread {

    private IChatSession _session;
    private ConversationController _ui;

    private boolean _alive;

    public MessageMonitorThread(IChatSession sess, ConversationController controller) {
        this._session = sess;
        this._ui = controller;
    }

    public void run() {
        this._alive = true;
        while (this._alive) {
            try {
                Message m = this._session.getNextMessage();
                this._ui.addMessage(m);
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
