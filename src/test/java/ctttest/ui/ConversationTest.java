package ctttest.ui;

import junit.framework.*;

import canttouchthis.ui.ConversationModel;
import canttouchthis.ui.ConversationView;
import canttouchthis.ui.ConversationController;

import canttouchthis.common.Message;

public class ConversationTest extends TestCase {

    ConversationView cv;
    ConversationModel cm;
    ConversationController cc;

    protected void setUp() {

        cm = new ConversationModel();
        cv = new ConversationView(cm);
        cc = new ConversationController(cv, cm);

    }

    /**
     * Check that adding a message in the controller adds to the model.
     */
    public void testAddMessage() {
        // SETUP
        Message m = generateDummyMessage();

        // EXEC
        cc.addMessage(m);

        // VERIFY
        assertTrue(cm.getLastMessage() == m);
    }

    private Message generateDummyMessage() {
        return new Message("Alice", "Bob", 0, "Ta-daa!!!");
    }

}
