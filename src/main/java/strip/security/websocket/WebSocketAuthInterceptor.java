// package strip.security.websocket;

// import java.util.List;

// import org.springframework.messaging.Message;
// import org.springframework.messaging.MessageChannel;
// import org.springframework.messaging.simp.stomp.StompCommand;
// import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
// import org.springframework.messaging.support.ChannelInterceptor;
// import org.springframework.security.core.Authentication;
// import org.springframework.stereotype.Component;
// import strip.security.jwt.WebSocketTokenProvider;

// @Component
// public class WebSocketAuthInterceptor implements ChannelInterceptor {

//     private final WebSocketTokenProvider tokenProvider;

//     public WebSocketAuthInterceptor(WebSocketTokenProvider tokenProvider) {
//         this.tokenProvider = tokenProvider;
//     }

//     @Override
//     public Message<?> preSend(Message<?> message, MessageChannel channel) {
//         StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

//         if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//             List<String> tokenList = accessor.getNativeHeader("access_token");
//             if (tokenList != null && !tokenList.isEmpty()) {
//                 String token = tokenList.get(0);
//                 if (tokenProvider.validateToken(token)) {
//                     Authentication authentication = tokenProvider.getAuthentication(token);
//                     accessor.setUser(authentication); // ✅ đây là điểm quan trọng
//                     System.out.println("✅ Authenticated user: " + authentication.getName());
//                 } else {
//                     System.out.println("❌ Invalid token");
//                 }
//             } else {
//                 System.out.println("❌ No access_token header");
//             }
//         }

//         return message;
//     }
// }
