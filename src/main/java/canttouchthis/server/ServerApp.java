package canttouchthis.server;

import canttouchthis.common.auth.*;
import canttouchthis.common.MessageMonitorThread;
import canttouchthis.ui.*;

import javax.swing.*;
import java.sql.SQLException;

class ServerApp {

    private static final String DB_FILE = "auth_db.sqlite";

    public ServerSession sess;

    public Identity ident;
    public Authenticator auth;

    public LoginController loginController;
    public ConversationController conversationController;

    public static void main(String[] args) {

        ServerApp app = new ServerApp();

        // Start login and chat views
        app.loginController = new LoginController(new LoginView());

        ConversationModel conversationModel = new ConversationModel();
        app.conversationController = new ConversationController(new ConversationView(conversationModel),
                                                                conversationModel);

        app.loginController.showView();

        // Setup server session
        app.sess = new ServerSession();
        WaitForConnectionDialog waitDialog = new WaitForConnectionDialog(app.sess.getAddress(), app.sess.port);

        /*
         * Shows a window saying that a connection is being established, waits for a connection
         * and opens a chat window once a connection has been established.
         */
        Thread waitForConThread = new Thread(new Runnable() {
            @Override
            public void run() {
                waitDialog.setVisible(true);
                app.sess.waitForConnection();
                waitDialog.setVisible(false);
                app.conversationController.showView();

                // once conversation starts, start message thread
                MessageMonitorThread mm = new MessageMonitorThread(app.sess,
                                                                   app.conversationController,
                                                                   app.ident,
                                                                   app.auth);
                mm.start();
            }
        });

        try {
            app.auth = new Authenticator(DB_FILE);

            // When login button clicked...
            app.loginController.setLoginHandler(new ILoginHandler() {
                @Override
                public String tryHandleLogin(String username, String password) {

                    try {
                        app.ident = app.auth.checkAuth(username, password, Authenticator.UserType.USERTYPE_SERVER);
                        if (app.ident != null) {
                            app.loginController.hideView();
                            waitForConThread.start();
                            return null;
                        } else {
                            return "Invalid username or password!";
                        }
                    } catch (SQLException ex) {
                        return "Error looking up credentials: " + ex.getCause();
                    }

                }
            });
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Could not find authentication database: "
                    + DB_FILE);
            return;
        }

    }

}
