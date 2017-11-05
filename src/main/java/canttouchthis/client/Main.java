package canttouchthis.client;

import canttouchthis.common.Message;
import canttouchthis.ui.*;

import javax.swing.*;

class Main {

    private enum ClientState {
        LOGIN,
        CONNECT,
        CHAT
    }

    public static void main(String[] args) {

        ConnectView connectView = new ConnectView();
        ConnectController cc = new ConnectController(connectView);

        cc.showView();

        // Start in LOGIN state
        ClientState appState = ClientState.LOGIN;

    }

}
