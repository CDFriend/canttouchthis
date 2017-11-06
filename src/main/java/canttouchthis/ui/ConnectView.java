package canttouchthis.ui;

import canttouchthis.server.ServerSession;

import javax.swing.*;
import java.awt.*;

/**
 * Window for specifying options before connecting to a server (client-side).
 */
public class ConnectView extends JFrame {

    protected JTextField hostField;
    protected JSpinner portField;
    protected JCheckBox confBox;
    protected JCheckBox integBox;
    protected JButton connectButton;

    /**
     * Create and populate a new ConnectView.
     */
    public ConnectView() {
        super("canttouchthis v1.0");
        initComponents();
    }

    private void initComponents() {
        setResizable(false);
        setLayout(new GridBagLayout());
        Container pane = getContentPane();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        // "Connect to" label
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.WEST;
        JLabel connectTo = new JLabel("Connect to: ");
        setBold(connectTo);
        pane.add(connectTo, c);

        // Hostname and port
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        JLabel hostLbl = new JLabel("Host: ");
        pane.add(hostLbl, c);

        c.gridx = 1;
        c.gridy = 1;
        hostField = new JTextField(13);
        pane.add(hostField, c);

        c.gridx = 2;
        c.gridy = 1;
        JLabel portLbl = new JLabel("Port: ");
        pane.add(portLbl, c);

        c.gridx = 3;
        c.gridy = 1;
        portField = new JSpinner();
        SpinnerModel model = new SpinnerNumberModel(ServerSession.DEFAULT_PORT,
                                                    0,
                                                    ServerSession.DEFAULT_PORT + 10000,
                                                    1);
        portField.setModel(model);
        pane.add(portField, c);

        // "Security Options" label
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.WEST;
        JLabel securityOptions = new JLabel("Security Options: ");
        setBold(securityOptions);
        pane.add(securityOptions, c);

        // Security options boxes
        c.gridx = 0;
        c.gridy = 3;
        confBox = new JCheckBox("Confidentiality (AES encryption)", true);
        pane.add(confBox, c);

        c.gridx = 0;
        c.gridy = 4;
        integBox = new JCheckBox("Integrity (SHA-256 Digests)", true);
        pane.add(integBox, c);

        // Connect button
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        connectButton = new JButton("Connect");
        pane.add(connectButton, c);

        pack();
    }

    private void setBold(JLabel label) {
        Font f = label.getFont();
        label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
    }

}
