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
    System.out.print(msg);
    messageWall.put(user,msg);
  }

  @Override
  public boolean delete(int index) {
    return messageWall.delete(user, index);
  }

  @Override
  public List<Message> getAllMessages() {
      return messageWall.getAllMessages();
  }
  public boolean edit(int index,String new_content){
      return messageWall.edit(index,new_content);
  }

}


