<%-- 
    Document   : senderFinal
    Created on : Mar 3, 2010, 4:01:17 PM
    Author     : mccaffrey
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="sender" class="gov.nist.mu.sender.web.SenderHelper"/>
<% sender.processRequest(request); %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insert something meaningful here.</title>
    </head>
    <body>
        <h1>There it goes!</h1>

        Message = <%= sender.getMessage() %>

    </body>
</html>
