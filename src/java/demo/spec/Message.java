package demo.spec;

import java.util.List;

public interface Message {
    String getContent();
    String getOwner();
    void setContent(String new_content);
    List<Message> getAllComments();
    void put(String user, String msg);
    int comments_size();
}
