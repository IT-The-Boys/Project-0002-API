package it.theboys.project0002api.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import it.theboys.project0002api.model.Chat;
import it.theboys.project0002api.model.Player;
import it.theboys.project0002api.model.Send;
import it.theboys.project0002api.storage.ChatStorage;
import lombok.AllArgsConstructor;
import it.theboys.project0002api.exception.InvalidChatException;
import it.theboys.project0002api.exception.InvalidParamException;
import it.theboys.project0002api.exception.NotFoundException;

@Service
@AllArgsConstructor
public class ChatService {

    //createChat and connectToChat is is called from GameServerService
    //Not from the ChatController
    public Chat createChat(Player player) {
        Chat chat = new Chat();
        chat.setChatId(UUID.randomUUID().toString());
        chat.addPlayer(player);
        ChatStorage.getInstance().setChat(chat);
        return chat;
    }

    public Chat connectToChat(Player player, String chatId) throws InvalidParamException, InvalidChatException {
        if (!ChatStorage.getInstance().getChats().containsKey(chatId)) {
            throw new InvalidParamException("Chat with provided id doesn't exist");
        }
        Chat chat = ChatStorage.getInstance().getChats().get(chatId);

        chat.addPlayer(player);
        ChatStorage.getInstance().setChat(chat);
        return chat;
    }

    //Get the chat tested and working
    public Chat send(Send send) throws NotFoundException, InvalidChatException {
        if (!ChatStorage.getInstance().getChats().containsKey(send.getChatId())) {
            throw new NotFoundException("Chat not found");
        }

        Chat chat = ChatStorage.getInstance().getChats().get(send.getChatId());

        ChatStorage.getInstance().setChat(chat);
        return chat;
    }

}
