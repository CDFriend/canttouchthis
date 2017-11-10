package canttouchthis.client;

import canttouchthis.common.auth.*;
import canttouchthis.common.MessageMonitorThread;
import canttouchthis.ui.*;

import javax.swing.*;
import java.net.UnknownHostException;
import java.sql.SQLException;

class ClientApp {

    public static final String DB_FILE = "auth_db.sqlite";

    public ClientSession sess;

    public LoginController loginController;
    public ConnectController connectController;
    public ConversationController conversationController;

    public Authenticator auth;
    public Identity ident;

    public static void main(String[] args) {

        ClientApp app = new ClientApp();
        app.sess = new ClientSession();

        // Setup login view
        LoginView lView = new LoginView("canttouchthis Client v1.0");
        app.loginController = new LoginController(lView);

        // Setup connection view
        app.connectController = new ConnectController(new ConnectView());

        // Setup message view
        ConversationModel model = new ConversationModel();
        app.conversationController = new ConversationController(new ConversationView(model), model);


        try {
            // setup auth db
            app.auth = new Authenticator(DB_FILE);

            // When login button clicked...
            app.loginController.setLoginHandler(new ILoginHandler() {
                @Override
                public String tryHandleLogin(String username, String password) {

                    try {
                        app.ident = app.auth.checkAuth(username, password, Authenticator.UserType.USERTYPE_CLIENT);
                        if (app.ident != null) {
                            app.loginController.hideView();
                            app.connectController.showView();
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
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Could not find authentication database: "
                    + DB_FILE);
            return;
        }

        // When connect button clicked...
        app.connectController.setConnectHandler(new IConnectHandler() {
            @Override
            public String tryHandleConnect(String host, int port, boolean checkInt, boolean useConf) {

                // TODO: sanitize URLs

                // TODO: establish keys and encrypt messages. Beary insecure!
                try {
                    if (app.sess.connect(host, port, useConf)) {
                        // connection was successful
                        app.connectController.hideView();
                        app.conversationController.showView();

                        // once conversation starts, start message monitor thread
                        MessageMonitorThread mm = new MessageMonitorThread(app.sess,
                                                                           app.conversationController,
                                                                           app.ident,
                                                                           app.auth);
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
        app.loginController.showView();

    }

}
