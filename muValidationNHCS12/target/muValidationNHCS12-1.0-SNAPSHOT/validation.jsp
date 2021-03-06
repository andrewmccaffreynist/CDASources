<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css" media="screen"/>
        <title>Meaningful Use: HL7 National Health Care Surveys Release 1.2, DSTU -- BETA</title>
    </head>
    
    <body>

        <h2>National Health Care Surveys Release 1.2, DSTU Validation -- BETA</h2>

        <h3>Download</h3>
        
        <ul>
            <li><i>Coming soon...</i></li>
        </ul>       

        <h3>On-line Version</h3>


           <form method="post" enctype="multipart/form-data" action="validationResultXml.jsp" name="myform">
            <table class="standard" >
                <tr bgcolor="#EEEEFF">
                    <td>
                        Step 1.
                    </td>
                    <td>
                        Upload your National Health Care Surveys 1.2 XML document for validation: <input type="file" id="file" name="file" size="50" />
                   </td>
                </tr>
                <tr bgcolor="#CCCCDD">
                    <td>
                        Step 2.
                    </td>
                    <td>
         <input type="submit" value="Validate" name="validate">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
