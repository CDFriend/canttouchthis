package canttouchthis.ui;

import java.awt.Dimension;
import javax.swing.*;

public class ConversationView extends JFrame {

    public ConversationView() {
        super("canttouchthis");

        setPreferredSize(new Dimension(640, 480));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
    }

}
