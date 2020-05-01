<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="validation" scope="session" class="gov.nist.hitsp.validation.ValidationHelper" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% validation.processRequest(request,true); %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= request.getSession().getServletContext().getInitParameter("webpageTitle") %></title>
    </head>
    <body>
        
     <!--   <h1>NIST CDA Guideline Validation</h1> -->
        
        <% if(validation.hasTopErrorMessage()) { %>
        
        Cannot validate.  Error.<br />
        <%= validation.getTopErrorMessage() %>
        
        <% } else { %>        
        
        <blockquote style="background-color: rgb(200,200,200)">
            <!-- CDA R2 Errors: -->
            <%= validation.getSchemaName() %> Errors:
            <% if(validation.hasSchemaErrors()) { %>
            <ul>
                <% java.util.Iterator it = validation.getSchemaErrors().iterator(); %>
                <% while(it.hasNext()) { %>
                <li><%= (String) it.next() %></li>
                <% } %>
            </ul>
            <% } else { %>
            <i>No errors!</i>
            <% } %>
        </blockquote>
        
        <blockquote style="background-color: rgb(200,200,200)">
            <%= validation.getSchemaName() %> Warnings:
            <% if(validation.hasSchemaWarnings()) { %>
            <ul>
                <% java.util.Iterator it = validation.getSchemaWarnings().iterator(); %>
                <% while(it.hasNext()) { %>
                <li><%= (String) it.next() %></li>
                <% } %>
            </ul>
            <% } else { %>
            <i>No warnings!</i>
            <% } %>
        </blockquote>
        
        <% if (validation.hasSchematronErrors()) { %>
        <% java.util.Iterator schematronIt = validation.getSchematronErrors().iterator(); %>
        <% while (schematronIt.hasNext()) { %>
        <blockquote style="background-color: rgb(200,200,200)">
            <%= (String) schematronIt.next() %>           
        </blockquote>
        <% } %>
        <% } %>
        
        
        <% } %>
        
    </body>
</html>
