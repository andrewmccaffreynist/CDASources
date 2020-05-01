<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="validation" scope="session" class="gov.nist.mu.validation.ValidationHelper" />
<% validation.processRequest(request,false); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css" media="screen"/>
        <title><%= request.getSession().getServletContext().getInitParameter("webpageTitle") %></title>
    </head>
    
    <body>
        <!-- <h1>NIST CDA Guideline Validation</h1> -->
        <!--
        <form method="post" enctype="multipart/form-data" action="validationResult.jsp" name="myform">
           -->

           <form method="post" enctype="multipart/form-data" action="validationResultXml.jsp" name="myform">
            <table class="standard" width="500">
                <tr>
                    <td>
                        1.
                    </td>
                    <td>
                Upload the file for validation:
                <input type="file" id="file" name="file" size="50" />
                   </td>
                </tr>
                <tr>
                    <td> 
                    2.
                    </td>
                    <td>
                Please select the level of detail:
                   </td>
                </tr>
                <tr><td></td><td><input type="radio" name="resultDetail" value="all" CHECKED />Everything (Errors, Warnings, Notes)</td></tr>
                <tr><td></td><td><input type="radio" name="resultDetail" value="errorsWarnings" />Errors and Warnings only</td></tr>
                <tr><td></td><td><input type="radio" name="resultDetail" value="errors" />Errors Only</td></tr>
            <tr>
                <td>3.</td>
                <td>
            What would you like this file to validate to?  
            <input type="submit" value="Validate" name="validate">  
            </td>
        </tr>
            <tr>

                <td colspan="2">
            <table class="standard">
                <tr>
                    <th></th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Dependencies (if applicable)</th>
                </tr>
                <% for (int i = 0; i < validation.getNumberOfRunnable(); i++ ) { %>
                <% gov.nist.hitsp.validation.DocumentType docType = validation.getRunnableDocumentTypeAt(i); %>
                
                  <tr <% if ((i % 2) == 0 ) { %> class="even" <% } else { %> class="odd" <% } %>>
                    <td>
                        <input type="radio" name="documentType" value="<%= docType.getId() %>" 
                               <% if (i == 0) { %>
                               CHECKED
                               <% } %>
                               />
                    </td>
                    <td><%= docType.getDisplayName() %></td>
                    <td><%= docType.getDescription() %></td>
                    <td>
                        <% if (validation.getNumberOfDependenciesAt(i) > 0) { %>
                        <ul>
                            <% } %>
                            <% for(int j = 0; j< validation.getNumberOfDependenciesAt(i); j++) { %>
                            <li><%= validation.getDependenciesNameAt(i,j) %></li>
                            <% } %>
                            <% if (validation.getNumberOfDependenciesAt(i) > 0) { %>
                        </ul>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </table>    
        </td>
    </tr>
    <tr><td></td>
         <td align="center">  
         <input type="submit" value="Validate" name="validate">
          </td>
       </tr>
          <tr>
              <td colspan="2">
                  <%= request.getSession().getServletContext().getInitParameter("footnote1") %>
                <!--
                If your selection has required dependencies, then they will be run 
                automatically.  For example, a HITSP/C32 must also conform to the rules of
                CDA R2 and CCD.  Therefore, selecting HITSP/C32 will cause two 
                additional rule sets to execute.  The results from all executing
                rule sets shall be displayed on the results page.
                -->
            </td>
          <tr>
        </table>

        </form>
    </body>
</html>
