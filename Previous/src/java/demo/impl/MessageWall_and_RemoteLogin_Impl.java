package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageWall_and_RemoteLogin_Impl implements MessageWall, RemoteLogin {
    private final List<Message> messages; // List to store all messages
    private final Map<String, String> userCredentials; // Map to store user credentials (username and password)
    private final Map<String, UserAccess> activeUsers; // Map to store active UserAccess instances for each user

    public MessageWall_and_RemoteLogin_Impl() {
        this.messages = new ArrayList<Message>(); 
        this.userCredentials = new HashMap<String, String>();
        this.activeUsers = new HashMap<String, UserAccess>();
        
        // Example credentials (in a real application, these would be stored securely)
        userCredentials.put("bernat", "bernat");
        userCredentials.put("jaume", "jaume");
        userCredentials.put("admin", "admin");
        userCredentials.put("", "");
    }

    // --- Implementation of RemoteLogin Method ---

    @Override
    public UserAccess connect(String usr, String passwd) {
        // Check if user credentials are valid
        if (userCredentials.containsKey(usr) && userCredentials.get(usr).equals(passwd)) {
            // Check if the user is already in activeUsers; if not, create a new UserAccess_Impl
            if (!activeUsers.containsKey(usr)) {
                activeUsers.put(usr, new UserAccess_Impl(usr, this));
            }
            return activeUsers.get(usr);
        }
        return null; // Return null if login fails
    }
    
    // --- Implementation of MessageWall Methods ---

    @Override
    public void put(String user, String msgContent) {
        Message message = new Message_Impl(user, msgContent); // Assume Message has user and content parameters
        messages.add(message);
    }

    @Override
    public boolean delete(String user, int index) {
        // Check if the index is valid and the message belongs to the requesting user
        if (index >= 0 && index < messages.size()) {
            Message messageToDelete = messages.get(index);
            if (messageToDelete.getOwner().equals(user)) {
                messages.remove(index); // Remove the message if it belongs to the user
                return true;
            }
        }
        return false; // Return false if index is out of bounds or message does not belong to user
    }

    @Override
    public Message getLast() {
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1); // Return the last message
        } else {
            return new Message_Impl("no content","no content");
        }
    }

    @Override
    public int getNumber() {
        return messages.size();
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<Message>(messages); // Return a copy of messages to prevent external modification
    }

}
