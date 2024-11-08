package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.UserAccess;

import java.util.List;

public class UserAccess_Impl implements UserAccess {
    private final String user; // The username associated with this access
    private final MessageWall messageWall; // The MessageWall instance to interact with

    public UserAccess_Impl(String user, MessageWall messageWall) {
        this.user = user;
        this.messageWall = messageWall;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public Message getLast() {
        // Returns the last message posted by any user on the wall
        return messageWall.getLast();
    }

    @Override
    public int getNumber() {
        // Returns the total number of messages on the wall
        return messageWall.getNumber();
    }

    @Override
    public void put(String msg) {
        // Posts a new message to the wall under the current user's name
        messageWall.put(user, msg);
    }

    @Override
    public boolean delete(int index) {
        // Attempts to delete the message at the specified index if it belongs to the user
        return messageWall.delete(user, index);
    }

    @Override
    public List<Message> getAllMessages() {
        // Returns a list of all messages on the wall
        return messageWall.getAllMessages();
    }
}
