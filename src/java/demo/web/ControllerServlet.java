package demo.web;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String view = perform_action(request);
        forwardRequest(request, response, view);
    }

    protected String perform_action(HttpServletRequest request)
            throws IOException, ServletException {

        String serv_path = request.getServletPath();
        HttpSession session = request.getSession();

        if (serv_path.equals("/login.do")) {
           String username = request.getParameter("user");
            String password = request.getParameter("password");
            
            
            RemoteLogin remoteLogin = getRemoteLogin();
            UserAccess userAccess = remoteLogin.connect(username, password);
            
          
            

            if (userAccess != null) {
                // Si la autenticación es exitosa, guarda UserAccess en la sesión
                session.setAttribute("useraccess", userAccess);
                
                return "/view/wallview.jsp";
            } 
                
                
                return "/login.html";
           

        } else if (serv_path.equals("/put.do")) {
            
            
            String message = request.getParameter("msg");
            
            UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");
            String username = request.getParameter("user");
            
           
                userAccess.put(message);
                return "/view/wallview.jsp";
            
            
            
            
      
        } else if (serv_path.equals("/refresh.do")) {
           String web = request.getParameter("webpage");
           if(web.contentEquals("wallview")){
               return "/view/wallview.jsp";
           }else if(web.contentEquals("editview")){
               return "/view/editMessage.jsp";
           }else if(web.contentEquals("commentsview")){
               return "/view/commentsview.jsp";
           }
           return "/view/wallview.jsp"; 
        } else if (serv_path.equals("/back.do")) {
           return "/view/wallview.jsp";
           
        } else if (serv_path.equals("/logout.do")) {
            session.invalidate();
            return "/goodbye.html";
        } else if (serv_path.equals("/delete.do")) {
             UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");
             String username = userAccess.getUser();
                
             int index = Integer.parseInt(request.getParameter("index"));

             boolean success = userAccess.delete(index);
             if (!success) {
                request.setAttribute("errorMessage", "Failed to delete the message. You might not have permission or the index is invalid.");
            }
            return  "/view/wallview.jsp";
        } else if (serv_path.equals("/edit.do")) {
            
            UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");
            int index = Integer.parseInt(request.getParameter("index"));
            if (userAccess != null) {
                Message messageToEdit = userAccess.getAllMessages().get(index);
                request.setAttribute("message", messageToEdit);
                request.setAttribute("index", index);
                 return "/view/editMessage.jsp";
            }
            return  "/view/wallview.jsp";
        } else if (serv_path.equals("/updateMessage.do")) {

            int index = Integer.parseInt(request.getParameter("index"));
            String newContent = request.getParameter("content");
            UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");

            if (userAccess != null && newContent != null && !newContent.trim().isEmpty()) {
                userAccess.getAllMessages().get(index).setContent(newContent);
               
                    }
                    return  "/view/wallview.jsp";
        }
        else if (serv_path.equals("/comment.do")) {
            
            UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");
            int index = Integer.parseInt(request.getParameter("index"));
            if (userAccess != null) {
                Message messageTocomment = userAccess.getAllMessages().get(index);
                session.setAttribute("messageTocomment", messageTocomment);
                
                request.setAttribute("index", index);
                 return "/view/commentsview.jsp";
            }
            return  "/view/wallview.jsp";
         } else if (serv_path.equals("/addcomment.do")) {
            
            String comment = request.getParameter("comm");
       
            int index = Integer.parseInt(request.getParameter("index"));
            UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");
            System.out.println("Addcomment");
            request.setAttribute("index", index);
            if (userAccess != null && comment != null && !comment.trim().isEmpty()) {
                userAccess.getAllMessages().get(index).put(userAccess.getUser(),comment);
            }
            
            return "/view/commentsview.jsp";
            

        } else {
            return  "/view/wallview.jsp";
        }
    }

    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }

    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        System.out.print(view);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '" + view + "'");
        }
        dispatcher.forward(request, response);
    }
}
