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
    
    //Dont know if needed or if we build this in to something else
    public Chat createChat(Player player){
        Chat chat = new Chat();
        chat.setChatId(UUID.randomUUID().toString());
        chat.setPlayer1(player);
        ChatStorage.getInstance().setChat(chat);
        return chat;
    }

    public Chat connectToChat(Player player2, String chatId) throws InvalidParamException, InvalidChatException{
        if(!ChatStorage.getInstance().getChats().containsKey(chatId)){
            throw new InvalidParamException("Chat with provided id doesn't exist");
        }
        Chat chat = ChatStorage.getInstance().getChats().get(chatId);

        if(chat.getPlayer2() != null){
            throw new InvalidChatException("Chat is not valid anymore");
        }

        chat.setPlayer2(player2);
        ChatStorage.getInstance().setChat(chat);
        return chat;
    }

    public Chat send(Send send) throws NotFoundException, InvalidChatException {
        if(!ChatStorage.getInstance().getChats().containsKey(send.getChatId())){
            throw new NotFoundException("Chat not found");
        }

        Chat chat = ChatStorage.getInstance().getChats().get(send.getChatId());
        
        ChatStorage.getInstance().setChat(chat);
        return chat;
    }



}
