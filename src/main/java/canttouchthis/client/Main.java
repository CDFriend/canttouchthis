package canttouchthis.client;

import canttouchthis.common.Message;
import canttouchthis.ui.ConversationController;
import canttouchthis.ui.ConversationModel;
import canttouchthis.ui.ConversationView;
import canttouchthis.ui.LoginView;
import canttouchthis.ui.ConnectView;

import javax.swing.*;

class Main {

    private enum ClientState {
        LOGIN,
        CONNECT,
        CHAT
    }

    public static void main(String[] args) {

        ConnectView connectView = new ConnectView();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connectView.setVisible(true);
            }
        });

        // Start in LOGIN state
        ClientState appState = ClientState.LOGIN;

    }

}
