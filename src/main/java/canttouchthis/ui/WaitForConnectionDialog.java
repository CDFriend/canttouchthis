package canttouchthis.ui;

import java.awt.*;
import javax.swing.*;

public class WaitForConnectionDialog extends JFrame {

    private String _host;
    private int _port;

    public WaitForConnectionDialog(String host, int port) {
        this._host = host;
        this._port = port;
        this.initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        Container pane = this.getContentPane();
        pane.add(new JLabel("Waiting for connections..."), c);

        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        JLabel hostLabel = new JLabel("Host: " + _host + ":" + _port);
        pane.add(hostLabel, c);

        pack();
    }
}
