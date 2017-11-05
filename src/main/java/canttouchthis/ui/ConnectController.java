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

        if (e.getSource() == this._view.connectButton) {
            System.out.println("Connect clicked!");
        }

    }

}
