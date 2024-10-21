package demo.web;

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

            // Obtiene la instancia de RemoteLogin
            RemoteLogin remoteLogin = getRemoteLogin();
            UserAccess userAccess = remoteLogin.connect(username, password);

            // Intenta autenticar al usuario
            

            if (userAccess != null) {
                // Si la autenticación es exitosa, guarda UserAccess en la sesión
                session.setAttribute("useraccess", userAccess);
               System.out.print(userAccess.getUser());
                return "/wallview";
            } else {
                // Si falla, redirige de nuevo a login con un mensaje de error
                request.setAttribute("errorMessage", "Invalid username or password.");
                return "/login.jsp";
            }

        } else if (serv_path.equals("/put.do")) {
            //...
            return "/wallview";
        } else if (serv_path.equals("/refresh.do")) {
            //...
            return "wallview";
        } else if (serv_path.equals("/logout.do")) {
            //...
            return "/goodbye.html";
        } else if (serv_path.equals("/delete.do")) {
            //...
            return "/wallview";
        } else {
            return "/wallview";
        }
    }

    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }

    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '" + view + "'");
        }
        dispatcher.forward(request, response);
    }
}
