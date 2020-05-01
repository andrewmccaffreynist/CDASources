<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="validation" scope="session" class="gov.nist.mu.validation.ValidationHelper" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% validation.processRequest(request, true); %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= request.getSession().getServletContext().getInitParameter("webpageTitle") %></title>
    </head>
    <body>
        <h1>NIST CDA Guideline Validation Report</h1>
        <!--   <h1>NIST CDA Guideline Validation</h1> -->
        
        <blockquote style="background-color: rgb(200, 200, 200)">
            <h3>File submitted:&nbsp;<font style="font-style: italic">"<%= validation.getFileName() %>"</font></h3>
        </blockquote>
        <blockquote style="background-color: rgb(200, 200, 200)">
            <h4>Detail level:&nbsp;<font style="font-style: italic"><%= validation.getDetailLevel() %></font></h4>
        </blockquote>
        <%
            int cpt = 0;
            if(validation.hasSchematronErrors()) {
                java.util.Iterator schematronIt = validation.getSchematronErrors().iterator();
                while(schematronIt.hasNext()) {
                    cpt++;
                    schematronIt.next();
                }
            }
        %>
        <%
            int i = validation.getIndexTest();
        %>
        <blockquote style="background-color: rgb(200, 200, 200)">
            <h4>Test:&nbsp;<font style="font-style: italic"><%= validation.getRunnableDocumentTypeAt(i).getDisplayName() %></font></h4>
            
            <table border="1" width="100%">
                <tr>
                    <td><b>Dependancies</b></td>
                    <td><b>Description</b></td>
                    <!--<td><b>Errors</b></td>
                <td><b>Warnings</b></td>
                <td><b>Notes</b></td>
        -->
                </tr>
                <tr>
                    <td><a href="#<%= Integer.toString(validation.getNumberOfDependenciesAt(i) + 1)%>"><%=validation.getRunnableDocumentTypeAt(i).getDisplayName()%></a></td>
                    <td><%=validation.getRunnableDocumentTypeAt(i).getDescription()%></td>
                    <!--<td>0</td>
                <td>0</td>
                <td>0</td>
        -->
                </tr>
                <%
                    for(int j = 0; j < validation.getNumberOfDependenciesAt(i); j++) {
                        out.println("<tr>");
                        out.println("<td><a href=\"#" + Integer.toString(j + 1) + "\">" + validation.getDependenciesNameAt(i, j) + "</a></td>");
                        out.println("<td>" + validation.getDependenciesDescriptionAt(i, j) + "</td>");
                        //out.println("<td>0</td>");
                        //out.println("<td>0</td>");
                        //out.println("<td>0</td>");
                        out.println("</tr>");
                    }
                %>
            </table>
        </blockquote>
        
        <% if(validation.hasTopErrorMessage()) { %>        
            Cannot validate. Error.
            <br />
            <%= validation.getTopErrorMessage() %>
        <% } else { %>
        <blockquote style="background-color: rgb(200, 200, 200)">
            <h2><font color="red"><a id="1"></a>CDA R2:</font></h2>
            <%
                out.println("<h3>Errors: " + Integer.toString(validation.getNumberSchemaErrors()) + "</h3>");
                if(validation.hasSchemaErrors()) {
            %>
            <ul>
                <%
                    java.util.Iterator it = validation.getSchemaErrors().iterator();
                %>
                <%
                    java.util.Iterator it2 = validation.getLinesSchemaErrors().iterator();
                %>
                <%
                    while(it.hasNext()) {
                %>
                        <li><a href="validationXml.jsp?line=<%=it2.next()%>#error"><%=it.next()%></a></li>
                <%
                    }
                %>
            </ul>
            <%
            }
            out.println("<h3>Warnings: " + Integer.toString(validation.getNumberSchemaWarnings()) + "</h3>");
            if(validation.hasSchemaWarnings()) {
            %>
            <ul>
                <%
                        java.util.Iterator it = validation.getSchemaWarnings().iterator();
                        java.util.Iterator it2 = validation.getLinesSchemaErrors().iterator();
                        while(it.hasNext()) {
                %>
                            <li><a href="validationXml.jsp?line=<%=it2.next()%>#error"><%=it.next()%></a></li>
                <%
                        }
                %>
            </ul>
            <%
            }
            %>
        </blockquote>
        
        <%
            if(validation.hasSchematronErrors()) {
                java.util.Iterator schematronIt = validation.getSchematronErrors().iterator();
        %>
        <%
                    int count = 1;
                    while(schematronIt.hasNext()) {
        %>
        <blockquote style="background-color: rgb(200, 200, 200)">
            <a id="<%=++count%>"></a><%=schematronIt.next()%>
         </blockquote>
        <%
                    }
        %>
        <%
            }
        %>
        <%
            }
        %>
        <input
            type="button"
            value="Back to the top"
            onclick="javascript:window.location.href = '#top';"
            >
    </body>
</html>
