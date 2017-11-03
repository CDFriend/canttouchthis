package canttouchthis.client;

import canttouchthis.common.Message;
import canttouchthis.ui.ConversationController;
import canttouchthis.ui.ConversationModel;
import canttouchthis.ui.ConversationView;
import canttouchthis.ui.LoginView;

import javax.swing.*;

class Main {

    private enum ClientState {
        LOGIN,
        CONNECT,
        CHAT
    }

    public static void main(String[] args) {

        LoginView loginView = new LoginView();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loginView.setVisible(true);
            }
        });

        // Start in LOGIN state
        ClientState appState = ClientState.LOGIN;

        while (true) {

            if (appState == ClientState.LOGIN) {

            }

            else if (appState == ClientState.CONNECT) {

            }

            else {

            }

        }

    }

}
