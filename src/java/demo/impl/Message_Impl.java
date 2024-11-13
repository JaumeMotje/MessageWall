package demo.impl;

import demo.spec.Message;
import java.util.ArrayList;
import java.util.List;

public class Message_Impl implements Message, java.io.Serializable {

  private String user, message;
  private List<Message> comments;

  public Message_Impl(String usr, String msg) {
    user = usr;
    message = msg;
    comments = new ArrayList<Message>();
  }

  @Override
  public String getContent() {
    return message;
  }

  @Override
  public String getOwner() {
    return user;
  }
  
  public void setContent(String new_content){
      message = new_content;
  }
  
  public List<Message> getAllComments() {
    return comments;
  }
  
  public void put(String usr, String comm) {
      Message_Impl new_comm = new Message_Impl(usr,comm);
      comments.add(new_comm);
   
  }
  
  public int comments_size(){
      return comments.size();
  }
}
