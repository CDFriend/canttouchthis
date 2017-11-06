package canttouchthis.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginController implements ActionListener {

    private LoginView _view;

    private ILoginHandler _loginHandler;

    public LoginController(LoginView view) {
        this._view = view;
        this._view.loginButton.addActionListener(this);
    }

    public void setLoginHandler(ILoginHandler handler) {
        this._loginHandler = handler;
    }

    public void showView() {
        LoginView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(true);
            }
        });
    }

    public void hideView() {
        LoginView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(false);
            }
        });
    }

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
