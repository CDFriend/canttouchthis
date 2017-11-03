package canttouchthis.client;

import canttouchthis.common.Message;
import canttouchthis.ui.ConversationController;
import canttouchthis.ui.ConversationModel;
import canttouchthis.ui.ConversationView;

class Main {

    private enum ClientState {
        LOGIN,
        CONNECT,
        CHAT
    }

    public static void main(String[] args) {

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
