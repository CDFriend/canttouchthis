package canttouchthis.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ConnectController implements ActionListener {

    private ConnectView _view;
    private IConnectHandler _connectHandler;

    public ConnectController (ConnectView view) {
        this._view = view;
        this._view.connectButton.addActionListener(this);
    }

    public void setConnectHandler(IConnectHandler handler) {
        this._connectHandler = handler;
    }

    public void showView() {
        ConnectView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(true);
            }
        });
    }

    public void hideView() {
        ConnectView view = this._view;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(false);
            }
        });
    }

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
