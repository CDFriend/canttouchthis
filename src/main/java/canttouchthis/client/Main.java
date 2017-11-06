package canttouchthis.client;

import canttouchthis.common.Message;
import canttouchthis.ui.*;

class Main {

    public static void main(String[] args) {

        // Setup login view
        LoginController loginController = new LoginController(new LoginView());

        // Setup connection view
        ConnectController connectController = new ConnectController(new ConnectView());

        // Setup message view
        ConversationModel model = new ConversationModel();
        ConversationController conversationController = new ConversationController(new ConversationView(model), model);

        // When login button clicked...
        loginController.setLoginHandler(new ILoginHandler() {
            @Override
            public String tryHandleLogin(String username, String password) {
                // TODO: check auth
                loginController.hideView();
                connectController.showView();
                return null;
            }
        });

        // When connect button clicked...
        connectController.setConnectHandler(new IConnectHandler() {
            @Override
            public String tryHandleConnect(String host, int port, boolean checkInt, boolean useConf) {
                connectController.hideView();
                conversationController.showView();
                return null;
            }
        });


        // Start on login view
        loginController.showView();

    }

}
