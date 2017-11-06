package canttouchthis.server;

import canttouchthis.ui.*;

class Main {

    public static void main(String[] args) {

        // Start login and chat views
        LoginController loginController = new LoginController(new LoginView());

        ConversationModel conversationModel = new ConversationModel();
        ConversationController conversationController =
                new ConversationController(new ConversationView(conversationModel), conversationModel);

        loginController.showView();

        // Setup server session
        ServerSession session = new ServerSession();
        WaitForConnectionDialog waitDialog = new WaitForConnectionDialog(session.getAddress(), session.port);

        /*
         * Shows a window saying that a connection is being established, waits for a connection
         * and opens a chat window once a connection has been established.
         */
        Thread waitForConThread = new Thread(new Runnable() {
            @Override
            public void run() {
                waitDialog.setVisible(true);
                session.waitForConnection();
                waitDialog.setVisible(false);
                conversationController.showView();
            }
        });

        // After checking auth, start waiting for a connection.
        loginController.setLoginHandler(new ILoginHandler() {
            @Override
            public String tryHandleLogin(String username, String password) {
                // TODO: check auth
                loginController.hideView();
                waitForConThread.start();
                return null;
            }
        });

    }

}
