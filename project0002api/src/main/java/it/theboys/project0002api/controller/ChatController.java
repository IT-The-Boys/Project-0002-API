package it.theboys.project0002api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.theboys.project0002api.controller.dto.ConnectRequest;
import it.theboys.project0002api.exception.InvalidChatException;
import it.theboys.project0002api.exception.InvalidParamException;
import it.theboys.project0002api.exception.NotFoundException;
import it.theboys.project0002api.model.Chat;
import it.theboys.project0002api.model.Player;
import it.theboys.project0002api.model.Send;
import it.theboys.project0002api.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    //Do we need a start for chat??
    @PostMapping("/start")
    public ResponseEntity<Chat> start(@RequestBody Player player){
        log.info("start chat request: {}", player);
        return ResponseEntity.ok(chatService.createChat(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Chat> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidChatException{
        log.info("connect request: {}", request);
        return ResponseEntity.ok(chatService.connectToChat(request.getPlayer(), request.getChatId()));
    }

    @PostMapping("/send")
    public ResponseEntity<Chat> send(@RequestBody Send request) throws NotFoundException, InvalidChatException{
        log.info("send: {}", request);
        Chat chat = chatService.send(request);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chat.getChatId(), chat);
        return ResponseEntity.ok(chat);
    }

    
}
