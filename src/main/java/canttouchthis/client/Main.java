package canttouchthis.client;

import canttouchthis.common.Authenticator;
import canttouchthis.common.Message;
import canttouchthis.common.MessageMonitorThread;
import canttouchthis.ui.*;

import javax.swing.*;
import java.net.UnknownHostException;
import java.sql.SQLException;

class Main {

    public static void main(String[] args) {

        ClientSession session = new ClientSession();

        String dbfile = "auth_db.sqlite";


        // Setup login view
        LoginView lView = new LoginView("canttouchthis Client v1.0");
        LoginController loginController = new LoginController(lView);

        // Setup connection view
        ConnectController connectController = new ConnectController(new ConnectView());

        // Setup message view
        ConversationModel model = new ConversationModel();
        ConversationController conversationController = new ConversationController(new ConversationView(model), model);


        try {
            Authenticator auth = new Authenticator(dbfile);

            // When login button clicked...
            loginController.setLoginHandler(new ILoginHandler() {
                @Override
                public String tryHandleLogin(String username, String password) {

                    try {
                        if (auth.checkAuth(username, password, Authenticator.UserType.USERTYPE_CLIENT)) {
                            loginController.hideView();
                            connectController.showView();
                            return null;
                        } else {
                            return "Invalid username or password!";
                        }
                    }
                    catch (SQLException ex) {
                        return "Error looking up credentials: " + ex.getCause();
                    }

                }
            });
        }
        catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(null,"Could not find authentication database: "
                    + dbfile);
            return;
        }

        // When connect button clicked...
        connectController.setConnectHandler(new IConnectHandler() {
            @Override
            public String tryHandleConnect(String host, int port, boolean checkInt, boolean useConf) {

                // TODO: sanitize URLs

                // TODO: establish keys and encrypt messages. Beary insecure!
                try {
                    if (session.connect(host, port)) {
                        // connection was successful
                        connectController.hideView();
                        conversationController.showView();

                        // once conversation starts, start message monitor thread
                        MessageMonitorThread mm = new MessageMonitorThread(session, conversationController);
                        mm.start();

                        return null;
                    }
                    else {
                        // connection was unsuccessful
                        return "Error connecting to host " + host + ":" + port;
                    }
                }
                catch (UnknownHostException ex) {
                    return "Could not find host " + host + ":" + port;
                }

            }
        });


        // Start on login view
        loginController.showView();

    }

}
