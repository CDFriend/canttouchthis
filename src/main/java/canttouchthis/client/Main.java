package canttouchthis.client;

import canttouchthis.common.Message;
import canttouchthis.ui.ConversationView;
import canttouchthis.ui.ConversationModel;

class Main {

    public static void main(String[] args) {
        System.out.println("Hello Client!");

        Message msg = new Message("blorp", "bleep", 1, "Mwyaah!");
        ConversationModel m = new ConversationModel();
        m.addMessage(msg);
        m.addMessage(msg);
        m.addMessage(msg);

        ConversationView cv = new ConversationView(m);
        cv.setVisible(true);
        cv.updateConversation();

    }

}
