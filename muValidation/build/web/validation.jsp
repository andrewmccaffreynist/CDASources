<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css" media="screen"/>
        <title>Meaningful Use HITSP/C32 v2.5 Validation</title>
    </head>
    
    <body>

        <h2>Meaningful Use HITSP/C32 v2.5 Validation</h2>

        <h3>Download</h3>

        <ul>
            <li><a href="http://xreg2.nist.gov/cda-validation/downloads/muValidation20101001.tar.gz">muValidation20101001.tar.gz</a> -- This release (dated October 1st, 2010) contains the schema and schematron rules for HITSP/C32 v2.5 HITSP Summary Documents Using HL7 Continuity of Care Document (CCD) Component (and all associated and referenced specifications) as referenced in Meaningful Use.  Please read the included README.txt file for more information.  See the included changelog.txt for a list of changes.  <strong>This is the latest release.</strong></li>
            <li><a href="http://xreg2.nist.gov/cda-validation/downloads/muValidation20100923.tar.gz">muValidation20100923.tar.gz</a> -- This release (dated September 23rd, 2010) contains the schema and schematron rules for HITSP/C32 v2.5 HITSP Summary Documents Using HL7 Continuity of Care Document (CCD) Component (and all associated and referenced specifications) as referenced in Meaningful Use.  Please read the included README.txt file for more information.  This is a legacy link.</li>
            <li><a href="http://xreg2.nist.gov/cda-validation/downloads/muValidation20100813.tar.gz">muValidation20100813.tar.gz</a> -- This release (dated August 13th, 2010) contains the schema and schematron rules for HITSP/C32 v2.5 HITSP Summary Documents Using HL7 Continuity of Care Document (CCD) Component (and all associated and referenced specifications) as referenced in Meaningful Use.  Please read the included README.txt file for more information.  This is a legacy link.</li>
            <li><a href="http://xreg2.nist.gov/cda-validation/downloads/muCcdValidation20100415.tar.gz">muCcdValidation20100415.tar.gz</a> -- This release (dated April 15th, 2010) contains the schema and schematron rules for HL7 Continuity of Care Document as referenced in Meaningful Use.  Please read the included README.txt file for more information. This is a legacy link.</li>
            <li><a href="http://xreg2.nist.gov/cda-validation/downloads/muCcdValidation20100407.tar.gz">muCcdValidation20100407.tar.gz</a> -- This release (dated April 7th, 2010) contains the schema and schematron rules for HL7 Continuity of Care Document as referenced in Meaningful Use.  Please read the included README.txt file for more information. This is a legacy link.</li>
        </ul>

        <h3>On-line Version</h3>


           <form method="post" enctype="multipart/form-data" action="validationResultXml.jsp" name="myform">
            <table class="standard" >
                <tr bgcolor="#EEEEFF">
                    <td>
                        Step 1.
                    </td>
                    <td>
         Upload your HITSP/C32 v2.5 document for validation: <input type="file" id="file" name="file" size="50" />
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
