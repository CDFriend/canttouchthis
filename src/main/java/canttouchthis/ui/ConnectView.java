package canttouchthis.ui;

import canttouchthis.server.ServerSession;

import javax.swing.*;
import java.awt.*;

public class ConnectView extends JFrame {

    public ConnectView() {
        super();
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
        JTextField hostField = new JTextField(13);
        pane.add(hostField, c);

        c.gridx = 2;
        c.gridy = 1;
        JLabel portLbl = new JLabel("Port: ");
        pane.add(portLbl, c);

        c.gridx = 3;
        c.gridy = 1;
        JSpinner portNumField = new JSpinner();
        SpinnerModel model = new SpinnerNumberModel(ServerSession.DEFAULT_PORT,
                                                    0,
                                                    ServerSession.DEFAULT_PORT,
                                                    1);
        portNumField.setModel(model);
        pane.add(portNumField, c);

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
        JCheckBox confBox = new JCheckBox("Confidentiality (AES encryption)", true);
        pane.add(confBox, c);

        c.gridx = 0;
        c.gridy = 4;
        JCheckBox integBox = new JCheckBox("Integrity (SHA-256 Digests)", true);
        pane.add(integBox, c);

        // Connect button
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton connectButton = new JButton("Connect");
        pane.add(connectButton, c);

        pack();
    }

    private void setBold(JLabel label) {
        Font f = label.getFont();
        label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
    }

}
