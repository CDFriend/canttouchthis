package canttouchthis.ui;

import java.awt.*;
import javax.swing.*;

public class LoginView extends JFrame {

    private final int LOGINVIEW_WIDTH = 200;
    private final int LOGINVIEW_HEIGHT = 100;

    public LoginView() {
        super();
        initComponents();
    }

    private void initComponents() {
        setResizable(false);
        setPreferredSize(new Dimension(LOGINVIEW_WIDTH, LOGINVIEW_HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        // Username label and field
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 2;
        c.ipady = 2;
        c.weightx = 0.15;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        JLabel uNameLabel = new JLabel("Username: ");
        pane.add(uNameLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 2;
        c.ipady = 2;
        c.weightx = 0.85;
        c.fill = GridBagConstraints.HORIZONTAL;
        JTextField uNameField = new JTextField();
        pane.add(uNameField, c);

        // Password label + field
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.15;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        JLabel passwordLabel = new JLabel("Password: ");
        pane.add(passwordLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.85;
        c.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField passwordField = new JPasswordField();
        pane.add(passwordField, c);

        // Login button
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        pane.add(loginButton, c);

        pack();
    }

}
