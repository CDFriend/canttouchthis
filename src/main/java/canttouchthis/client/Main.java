package canttouchthis.client;

import canttouchthis.common.Message;
import canttouchthis.ui.ConversationController;
import canttouchthis.ui.ConversationModel;
import canttouchthis.ui.ConversationView;

class Main {

    public static void main(String[] args) {
        System.out.println("Hello Client!");

        ConversationModel m = new ConversationModel();

        ConversationView cv = new ConversationView(m);
        cv.setVisible(true);
        cv.updateConversation();

        Message msg = new Message("blorp", "bleep", 1, "Mwyaah!");
        ConversationController c = new ConversationController(cv, m);
        c.addMessage(msg);
        c.addMessage(msg);
        c.addMessage(msg);

    }

}
