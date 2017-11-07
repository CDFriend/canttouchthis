package canttouchthis.server;

import canttouchthis.common.Authenticator;
import canttouchthis.common.MessageMonitorThread;
import canttouchthis.ui.*;

import javax.swing.*;
import java.sql.SQLException;

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

                // once conversation starts, start message thread
                MessageMonitorThread mm = new MessageMonitorThread(session, conversationController);
                mm.start();
            }
        });

        String dbfile = "auth_db.sqlite";
        try {
            Authenticator auth = new Authenticator(dbfile);

            // When login button clicked...
            loginController.setLoginHandler(new ILoginHandler() {
                @Override
                public String tryHandleLogin(String username, String password) {

                    try {
                        if (auth.checkAuth(username, password, Authenticator.UserType.USERTYPE_SERVER)) {
                            loginController.hideView();
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
                    + dbfile);
            return;
        }

    }

}
