package it.theboys.project0002api.storage;


import java.util.HashMap;
import java.util.Map;

import it.theboys.project0002api.model.Chat;

public class ChatStorage {

    private static Map<String, Chat> chats;
    private static ChatStorage instance;

    private ChatStorage() {
        chats = new HashMap<>();

    }

    public static synchronized ChatStorage getInstance() {
        if (instance == null) {
            instance = new ChatStorage();
        }
        return instance;
    }

    public Map<String, Chat> getChats() {
        return chats;
    }

    public void setChat(Chat chat) {
        chats.put(chat.getChatId(), chat);
    }

}
