package canttouchthis.client;

import canttouchthis.common.Message;
import canttouchthis.ui.ConversationView;

class Main {

    public static void main(String[] args) {
        System.out.println("Hello Client!");

        ConversationView cv = new ConversationView();
        cv.setVisible(true);

        Message m = new Message("blorp", "bleep", 1, "Mwyaah!");
        cv.renderMessage(m);
    }

}
