package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageWall_and_RemoteLogin_Impl implements RemoteLogin, MessageWall {

  private List<Message> messages;
  private Map<String,String> userpss;
  private Map<String, UserAccess> activeUsers;

  public MessageWall_and_RemoteLogin_Impl() {
    messages = new ArrayList<Message>();
    userpss = new HashMap<String,String>();
    activeUsers = new HashMap<String,UserAccess>();
    
    userpss.put("Bernat", "1234");
    userpss.put("Jaume", "1234");
    
  }

  @Override
  public UserAccess connect(String usr, String passwd) {
      if(userpss.containsKey(usr) && userpss.get(usr).equals(passwd)){
          if(!activeUsers.containsKey(usr)){
              UserAccess_Impl new_user = new UserAccess_Impl(this,usr);
              activeUsers.put(usr,new_user );
          }
          return activeUsers.get(usr);
      }
     
      
      return null;
  }

  @Override
  public void put(String user, String msg) {
      Message_Impl new_msg = new Message_Impl(user,msg);
      messages.add(new_msg);
   
  }

  @Override
  public boolean delete(String user, int index) {
    if (index >= 0 && index < messages.size()) {
      Message_Impl message = (Message_Impl) messages.get(index);
      if (message.getOwner().equals(user)) {
        messages.remove(index);
        return true;
      }
    }
    return false;
  }

  @Override
  public Message getLast() {
    if (!messages.isEmpty()) {
      return messages.get(messages.size() - 1);
    }
    return null;
  }
  @Override
  public int getNumber() {
    return messages.size();
  }

  @Override
  public List<Message> getAllMessages() {
    return messages;
  }
  
  public boolean edit(int index, String newContent) {
    if (index >= 0 && index < messages.size()) {
        Message message = messages.get(index);
        if (message != null) {
            message.setContent(newContent); // Assuming `Message` has a `setContent` method
            return true;
        }
    }
    return false;
}

}
