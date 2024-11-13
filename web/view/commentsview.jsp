<%@ page import="demo.spec.Message"%>
<%@ page import="demo.spec.UserAccess"%>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<head>
    <meta http-equiv="Expires" CONTENT="0">
    <meta http-equiv="Cache-Control" CONTENT="no-cache">
    <meta http-equiv="Pragma" CONTENT="no-cache">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>Message Wall</title>
</head>

<%
    HttpSession sessio = request.getSession();
    UserAccess userAccess = (UserAccess) sessio.getAttribute("useraccess");
    Message messageTocomment = (Message) sessio.getAttribute("messageTocomment");
    String currentUser =  userAccess.getUser();
    List<Message> comments =  messageTocomment.getAllComments();   

%>

<script>
    
    setInterval(my_function, 10000);
    function my_function() {
        document.getElementsByName('refresh_button')[0].click();
    }
    
</script>

<body>
    
    <h3>user: <em><%= currentUser%></em>
        <a href=logout.do>[Close session]</a></h3>

    <h2> <%=comments.size()%> Comments shown:</h2>

    <table width="50%" border="1" bordercolordark="#000000" bordercolorlight="#FFFFFF" cellpadding="3" cellspacing="0">

        <td width="14%" valign="center" align="middle">
            Comment
        </td>

        <td width="14%" valign="center" align="middle">
            Owner
        </td>

        

        <%
             if (comments != null && !comments.isEmpty()) {
                for (int i = 0; i < comments.size(); i++) {
                    Message comm = comments.get(i);
                    if (!"no content".equals(comm.getOwner())) {

        %>

        <tr> <font size="2" face="Verdana">

        <td width="14%" valign="center" align="middle">
            <%=  comm.getContent() %>
        </td>

        <td width="14%" valign="center" align="middle">
            <%= comm.getOwner() %>
        </td>


        </font> 
    </tr>

    <%  
                 }
                }
            } else { 
            
        %>
        <tr>
                    <td colspan="3" valign="center" align="middle">No messages available.</td>
                </tr>
        <%
            }
        %>

</table>

</br>

<HR WIDTH="100%" SIZE="2">


<form action="addcomment.do" method=POST>
    <input type="hidden" name="index" value="<%= request.getAttribute("index") %>">
    New comment<input type=text name=comm size=10>
    <input type=submit value="Add comment"></form>


<HR WIDTH="100%" SIZE="2">

<form action="refresh.do" method=POST>
    <input type=submit value="Refresh wall view message" name="refresh_button">
    <input type="hidden" name="webpage" value="commentsview"></form>
<form action="back.do" method=POST>
    <input type=submit value="Back" name="back"></form>

</body>