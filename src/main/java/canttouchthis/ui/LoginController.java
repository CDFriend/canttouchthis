package canttouchthis.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Controller for a LoginView window.
 *
 * Asynchronously handles updating the UI using the swing event queue.
 */
public class LoginController implements ActionListener {

    private LoginView _view;

    private ILoginHandler _loginHandler;

    /**
     * Create a new controller for a given LoginView.
     * @param view LoginView to be controlled.
     */
    public LoginController(LoginView view) {
        this._view = view;
        this._view.loginButton.addActionListener(this);
    }

    /**
     * Sets an ILoginHandler to handle login attempts. Handler should return an error
     * message if the login fails and null if the login succeeds.
     *
     * @param handler ILoginHandler to deal with Login attempts.
     */
    public void setLoginHandler(ILoginHandler handler) {
        this._loginHandler = handler;
    }

    /**
     * Issues an event to show the LoginView.
     */
    public void showView() {
        LoginView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(true);
            }
        });
    }

    /**
     * Issues an event to hide the LoginView.
     */
    public void hideView() {
        LoginView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(false);
            }
        });
    }

    /**
     * Delegates send events to the LoginHandler, if one is present.
     *
     * @param e Swing ButtonPress event.
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this._view.loginButton && this._loginHandler != null) {

            String uname = this._view.uNameField.getText();
            String pwd = new String(this._view.passwordField.getPassword());
            String err = this._loginHandler.tryHandleLogin(uname, pwd);

            if (err != null) {
                // display error message
                JOptionPane.showMessageDialog(this._view, "Login failed: " + err);

                // clear password field
                this._view.passwordField.setText("");
            }

        }

    }

}
