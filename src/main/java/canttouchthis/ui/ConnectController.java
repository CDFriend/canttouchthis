package canttouchthis.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Asynchronously handles dataflow to and from a given ConnectView.
 */
public class ConnectController implements ActionListener {

    private ConnectView _view;
    private IConnectHandler _connectHandler;

    /**
     * Create a new controller for a ConnectView.
     *
     * @param view ConnectView to be controlled.
     */
    public ConnectController (ConnectView view) {
        this._view = view;
        this._view.connectButton.addActionListener(this);
    }

    /**
     * Sets a handler for a "connect" event.
     *
     * @param handler IConnectHandler object for connecting to a server given a host, port and
     *                security options.
     */
    public void setConnectHandler(IConnectHandler handler) {
        this._connectHandler = handler;
    }

    /**
     * Issues an event showing the ConnectView.
     */
    public void showView() {
        ConnectView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(true);
            }
        });
    }

    /**
     * Issues an event hiding the ConnectView.
     */
    public void hideView() {
        ConnectView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(false);
            }
        });
    }

    /**
     * Handles events from the Connect button, passing them to the ConnectHandler.
     *
     * @param e Swing connect event.
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this._view.connectButton && this._connectHandler != null) {

            String host = this._view.hostField.getText();
            int port = (int) this._view.portField.getValue();
            boolean useConf = this._view.confBox.isSelected();
            boolean checkInt = this._view.integBox.isSelected();

            String err = this._connectHandler.tryHandleConnect(host, port, checkInt, useConf);

            if (err != null) {
                JOptionPane.showMessageDialog(this._view, "Connect failed: " + err);
            }

        }

    }

}
