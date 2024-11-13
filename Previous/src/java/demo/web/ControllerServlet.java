package demo.web;

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

        // Get RemoteLogin from the servlet context
        RemoteLogin remoteLogin = getRemoteLogin();

        // Handle login
        if (serv_path.equals("/login.do")) {
            String username = request.getParameter("user");
            String password = request.getParameter("password");

            // Authenticate user
            UserAccess userAccess = remoteLogin.connect(username, password);

            if (userAccess != null) {
                session.setAttribute("useraccess", userAccess);
                return "/wallview";  // Redirect to the message wall view
            } else {
                // If login fails, redirect back to login with an error message
                request.setAttribute("errorMessage", "Invalid username or password.");
                return "/login.html";
            }

        // Handle posting a new message
        } else if (serv_path.equals("/put.do")) {
            UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");

            if (userAccess != null) {
                String message = request.getParameter("msg");

                if (message != null && !message.trim().isEmpty()) {
                    userAccess.put(message);  // Post message
                }
                return "/wallview";  // Redirect to the message wall view
            } else {
                return "/error-no-user_access.html"; // Redirect if user session is not found
            }

        // Handle refreshing the message wall
        } else if (serv_path.equals("/refresh.do")) {
            return "/wallview"; // Simply refresh the view

        // Handle logout
        } else if (serv_path.equals("/logout.do")) {
            session.invalidate();  // Invalidate the session to log out
            return "/goodbye.html"; // Redirect to goodbye page

        // Handle deleting a message
        } else if (serv_path.equals("/delete.do")) {
            UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");

            if (userAccess != null) {
                String indexParam = request.getParameter("index");
                try {
                    int index = Integer.parseInt(indexParam);  // Parse message index to delete
                    userAccess.delete(index);  // Attempt to delete the message
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid message index.");
                }
                return "/wallview"; // Redirect to the message wall view
            } else {
                return "/error-no-user_access.html"; // Redirect if user session is not found
            }

        // Default action for unrecognized paths
        } else {
            return "/wallview";
        }
    }

    // Retrieve RemoteLogin instance from the servlet context
    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }

    // Forward request to the specified view
    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '" + view + "'");
        }
        dispatcher.forward(request, response);
    }
}
