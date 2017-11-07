package canttouchthis.ui;

import java.awt.*;
import javax.swing.*;

public class LoginView extends JFrame {

    protected JTextField uNameField;
    protected JPasswordField passwordField;
    protected JButton loginButton;

    private ImageIcon _mchammer;
    private String _headerMessage;

    public LoginView(String headerMessage) {
        super("canttouchthis v1.0");
        this._headerMessage = headerMessage;

        // MC Hammer gif courtesy of ci.memecdn.com
        _mchammer = new ImageIcon("./assets/canttouchthis_crop.gif");

        initComponents();
    }

    private void initComponents() {
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 2;
        JLabel hammerLabel = new JLabel(_mchammer);
        pane.add(hammerLabel, c);

        // header text
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 10;
        c.ipady = 6;
        c.gridwidth = 2;
        JLabel headerLabel = new JLabel(this._headerMessage, JLabel.CENTER);
        makeHeaderStyle(headerLabel);
        pane.add(headerLabel, c);

        // Username label and field
        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 10;
        c.ipady = 2;
        c.weightx = 0.15;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        JLabel uNameLabel = new JLabel(" Username: ");
        pane.add(uNameLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.ipadx = 2;
        c.ipady = 2;
        c.weightx = 0.85;
        c.fill = GridBagConstraints.HORIZONTAL;
        uNameField = new JTextField();
        pane.add(uNameField, c);

        // Password label + field
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.15;
        c.ipadx = 10;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        JLabel passwordLabel = new JLabel(" Password: ");
        pane.add(passwordLabel, c);

        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 0.85;
        c.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField();
        pane.add(passwordField, c);

        // Login button
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        loginButton = new JButton("Login");
        pane.add(loginButton, c);

        pack();
    }

    private void makeHeaderStyle(JLabel lbl) {
        Font f = lbl.getFont();
        lbl.setFont(f.deriveFont(f.getStyle() | Font.BOLD, 28));
    }

}
