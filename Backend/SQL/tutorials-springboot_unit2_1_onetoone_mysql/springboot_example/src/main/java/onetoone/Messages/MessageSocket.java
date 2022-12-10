package onetoone.Messages;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @Author: Chris Costa
 */
@Controller
@ServerEndpoint(value = "/chat/{friendshipid}")
public class MessageSocket {

    private static MessagesRepository msgRepo;

    /**
     * sets the repostory to be used in each message
     * @param rep
     */
    @Autowired
    public void setMessagesRepository(MessagesRepository rep){
        msgRepo = rep;
    }

    private static Map<Session, Integer> sessionIdMap = new Hashtable<>();
    private static Map<Integer, Session> IdSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(MessageSocket.class);


    /**
     * Opens the session in which the messaging is to take place in
     * @param session
     * @param friendshipid
     * @throws IOException
     */
    @ApiOperation(value = "Opens the session to begin messaging", response = void.class)
    @OnOpen
    public void onOpen(Session session, @PathParam("friendshipid") int friendshipid) throws IOException {
        logger.info("Connected");
        sessionIdMap.put(session, friendshipid);
        IdSessionMap.put(friendshipid, session);
        sendPastMessagesToUser(friendshipid, getChatHistory());
    }

    /**
     * Creates a message when one is sent and calls the broadcast method to send the message
     * @param message
     * @throws IOException
     */
    @ApiOperation(value = "Saves the message that was sent as a new message and reports to the logger", response = void.class)
    @OnMessage
    public void onMessage(String message) throws IOException {

        logger.info("Message: " + message);

        broadcast(message);
       // msgRepo.save(new Messages());
        // TODO add more
        //msgRepo.save(new Messages(message));

    }

    /**
     * Closes the session that was being used and logs a disconnect message when closing begins
     * @param session
     * @throws IOException
     */
    @ApiOperation(value = "Closes the session and removes it from the hashmap and broadcasts the message", response = void.class)
    @OnClose
    public void onClose(Session session) throws IOException{
        logger.info("Closed chat");
        int friendsId = sessionIdMap.get(session);
        sessionIdMap.remove(session);
        sessionIdMap.remove(friendsId);
        // For testing can remove
        String message = "disconnected";
        broadcast(message);
    }

    /**
     * Sends an error with the trace when one come about.
     * @param session
     * @param throwable
     */
    @ApiOperation(value = "Sends an error if one occured and prints the trace", response = void.class)
    @OnError
    public void onError(Session session, Throwable throwable){
        //logger.info("Error");
        logger.info("Error for " + session.getId() + " caused by: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * sends the past messages to the users.
     * @param friendsid
     * @param message
     */
    @ApiOperation(value = "Sends a message a the user with the given message that was sent and catches an error if occured", response = void.class)
    private void sendPastMessagesToUser(int friendsid, String message){
        try{
            if(message == null){
                message = "";
            }
            IdSessionMap.get(friendsid).getBasicRemote().sendText(message);
        } catch(IOException e){
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    /**
     * sends the messages to the users when one sends a message to the other user
     * @param message
     */
    @ApiOperation(value = "Broadcasts/sends the message to the user and prints error if one occurs", response = void.class)
    private void broadcast(String message){
        sessionIdMap.forEach((session, friendsId) ->{
            try{
                session.getBasicRemote().sendText(message);
            }catch(IOException e){
                logger.info("EXCEPTION: " + e.getMessage().toString());
                e.printStackTrace();
            }

        });
    }

    /**
     * Gets the chat history from all previous chats from the start of the friendship
     * @return s
     */
    @ApiOperation(value = "Gets the chat history from all past chats between the two users", response = String.class)
    private String getChatHistory(){
        List<Messages> messages = msgRepo.findAll();
        StringBuilder s = new StringBuilder();
        if(messages != null && messages.size() != 0){
            for(Messages message : messages){
                s.append("" + "\n");
            }
        }
        return s.toString();
    }

}
