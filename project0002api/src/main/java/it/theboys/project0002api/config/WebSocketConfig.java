//package it.theboys.project0002api.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.converter.DefaultContentTypeResolver;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.converter.MessageConverter;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.util.MimeTypeUtils;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//import java.util.List;
//
//import static it.theboys.project0002api.utils.constants.EndPoints.*;
//
//@Configuration
//@EnableWebSocketMessageBroker
//@EnableScheduling
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker( "/chat", "/lobby", "/game");
//        config.setApplicationDestinationPrefixes(WS);
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry
//                .addEndpoint(CHAT_WS)
//                .setAllowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")
//                .withSockJS();
//        registry
//                .addEndpoint(LOBBY_WS)
//                .setAllowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")
//                .withSockJS();
//        registry
//                .addEndpoint(GAME_WS)
//                .setAllowedOrigins("*")
//                .withSockJS();
//    }
//
//    @Override
//    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
//        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
//        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setObjectMapper(new ObjectMapper());
//        converter.setContentTypeResolver(resolver);
//        messageConverters.add(converter);
//        return false;
//    }
//}
