package it.theboys.project0002api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
import it.theboys.project0002api.model.ChatMessage;
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

    // Right I just call the service when the game starts so I dont think we need
    // the postmapping for start and connect
    @PostMapping("/start")
    public ResponseEntity<Chat> start(@RequestBody Player player) {
        log.info("start chat request: {}", player);
        return ResponseEntity.ok(chatService.createChat(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Chat> connect(@RequestBody ConnectRequest request)
            throws InvalidParamException, InvalidChatException {
        log.info("connect request: {}", request);
        return ResponseEntity.ok(chatService.connectToChat(request.getPlayer(), request.getChatId()));
    }

    //Get the chat tested and working
    // Room chat
    @PostMapping("/send")
    public ResponseEntity<Chat> send(@RequestBody Send request) throws NotFoundException, InvalidChatException {
        log.info("send: {}", request);
        Chat chat = chatService.send(request);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chat.getChatId(), chat);
        return ResponseEntity.ok(chat);
    }

    // Global chat
    @PostMapping("/send/global")
    public ResponseEntity<Chat> sendGlobal(@RequestBody Send request) throws NotFoundException, InvalidChatException {
        log.info("send: {}", request);
        Chat chat = chatService.send(request);
        simpMessagingTemplate.convertAndSend("/topic/chat/global", chat);
        return ResponseEntity.ok(chat);
    }

    //Temporary shit from previous project
    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

}

