<%-- 
    Document   : sender
    Created on : Mar 2, 2010, 11:42:52 AM
    Author     : mccaffrey
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sending Sample Documents</title>
    </head>
    <body>

        <table>
            <form action="senderFinal.jsp" method="post">

                <tr>
                    <td>
                        Hostname/IP:
                    </td>
                    <td>
                        <input type="text" name="hostname" value="" />
                    </td>
                </tr>

                <tr>
                    <td>
                        Port:
                    </td>
                    <td>
                        <input type="text" name="port" value="" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Path:
                    </td>
                    <td>
                        <input type="text" name="path" value="" />
                    </td>
                </tr>


                <tr><td>Document Type:</td>
                    <td>
                        <input type="radio" name="docType" value="ccd" CHECKED />CCD<br />
                        <input type="radio" name="docType" value="ccr" />CCR
                    </td>
                </tr>

                <tr><td>Protocol:</td>
                    <td>
                        <input type="radio" name="protocol" value="rest" CHECKED />REST (over HTTP)<br />
                        <input type="radio" name="protocol" value="soap" />SOAP (over HTTP)
                    </td>
                </tr>
                <tr><td><input type="submit" value="Send" name="Send"></td></tr>

            </form>

        </table>

    </body>
</html>
