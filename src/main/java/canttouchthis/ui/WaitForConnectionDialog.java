package canttouchthis.ui;

import java.awt.*;
import javax.swing.*;

/**
 * Simple window to show user when a server is waiting for a connection.
 *
 * Note: no controller for this window - everything called here will be done on the
 *       same thread!
 */
public class WaitForConnectionDialog extends JFrame {

    private String _host;
    private int _port;

    /**
     * Create a dialog showing that a server is waiting for a connection.
     *
     * Message:
     *   Waiting for connections...
     *   Host: ${HOSTNAME}:${PORT}
     *
     * @param host String hostname for the server.
     * @param port Port number where the server is running.
     */
    public WaitForConnectionDialog(String host, int port) {
        this._host = host;
        this._port = port;
        this.initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        getRootPane().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        c.gridy = 0;
        c.ipadx = 5;
        c.ipady = 3;
        c.anchor = GridBagConstraints.WEST;
        Container pane = this.getContentPane();
        pane.add(new JLabel("Waiting for connections..."), c);

        c.gridy = 1;
        c.ipadx = 5;
        c.ipady = 3;
        c.anchor = GridBagConstraints.WEST;
        JLabel hostLabel = new JLabel("Host: " + _host + ":" + _port);
        pane.add(hostLabel, c);

        pack();
    }
}
