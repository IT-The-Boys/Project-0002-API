package it.theboys.project0002api.storage;

//<<<<<<< HEAD
//import it.theboys.project0002api.model.chat.ChatRoom;
//import it.theboys.project0002api.model.database.User;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ChatStorage {
//    private static Map<String, ChatRoom> roomList;
//    private static ChatStorage instance;
//
//    private ChatStorage() {
//        roomList = new HashMap<>();
//        roomList.put("global", new ChatRoom());
//=======
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

//<<<<<<< HEAD
//    public List<String> getRoomList(){
//        return new ArrayList<>(roomList.keySet());
//    }
//
//    public Map<String, ChatRoom> getChatMap(){
//        return roomList;
//    }
//
//    public String createChatRoom(String roomName, String roomImage){
//        ChatRoom room=new ChatRoom(roomName, roomImage);
//        roomList.put(room.getRoomId(), room);
//        return room.getRoomId();
//    }
//    public String createPrivateRoom(String roomName, String roomImage, List<User> allowedList){
//        ChatRoom room=new ChatRoom(roomName, roomImage, allowedList);
//        roomList.put(room.getRoomId(), room);
//        return room.getRoomId();
//    }
//
//=======
    public Map<String, Chat> getChats() {
        return chats;
    }

    public void setChat(Chat chat) {
        chats.put(chat.getChatId(), chat);
    }
}
