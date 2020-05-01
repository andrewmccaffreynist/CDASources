<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css" media="screen"/>
        <title>Meaningful Use: HL7 National Health Care Surveys Release 1.1, DSTU</title>
    </head>
    
    <body>

        <h2>National Health Care Surveys Release 1.1, DSTU Validation</h2>    

        <h3>Notice</h3>
        
                <p />
The purpose of this site is to facilitate testing of National Health Care Surveys CDA XML documents conformant to HL7 CDA® R2 Implementation Guide: National Health Care Surveys Release 1, DSTU Release 1.1 – US Realm, Draft Standard for Trial Use (DSTU) for certification and other purposes.
<p />
It is important to note, that the National Health Care Surveys (NHCS) strongly prefers developers and implementers to use NHCS IG Release 1.2 to fulfill certification criteria § 170.315(f)(7).  (See <a href="http://www.hl7.org/implement/ standards/product_brief.cfm_product_ id=385">http://www.hl7.org/implement/standards/product_brief.cfm_product_id=385</a> ) 
<p />
For more information on the National Health Care Surveys see: <a href="https://www.cdc.gov/nchs/dhcs/meaningful_use.htm">https://www.cdc.gov/nchs/dhcs/meaningful_use.htm</a>.
        
        <h3>On-line Version</h3>

           <form method="post" enctype="multipart/form-data" action="validationResultXml.jsp" name="myform">
            <table class="standard" >
                <tr bgcolor="#EEEEFF">
                    <td>
                        Step 1.
                    </td>
                    <td>
                        Upload your National Health Care Surveys 1.1 XML document for validation: <input type="file" id="file" name="file" size="50" />
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
