package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.UserAccess;
import java.util.List;

public class UserAccess_Impl implements UserAccess {

  private String user;
  private MessageWall messageWall;

  public UserAccess_Impl(MessageWall mw, String usr) {
    messageWall = mw;
    user = usr;
  }

  @Override
  public String getUser() {
        return user; 
  }

  @Override
  public Message getLast() {
    List<Message> messages = messageWall.getAllMessages();
    messages.add(new Message_Impl("no content","no content"));
    if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1); 
        }
        return null; 
  }

  @Override
  public int getNumber() {
    return messageWall.getNumber();
  }

  @Override
  public void put(String msg) {
    messageWall.put("no content","no content");
  }

  @Override
  public boolean delete(int index) {
    return messageWall.delete(user, index);
  }

  @Override
  public List<Message> getAllMessages() {
      return messageWall.getAllMessages();
  }

}


