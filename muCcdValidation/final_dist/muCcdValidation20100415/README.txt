README.txt

CCD Validation Tools
http://healthcare.nist.gov/

Release Date: 04/15/2010

This package contains the CCD validation tools used for validating the XML documents against the structure defined by the HL7 Clinical Document Architecture Revision 2 (CDA R2) and HL7 Continuity of Care Document (CCD).


Package Contents:

cdar2/ -- A directory containing the HL7 CDA R2 schema and related files.

schematron/ -- A directory containing the schematron rules and related files for validating HL7 CCD documents.

muCcdValidation.jar -- A bare-bones, simplistic, no frills command-line tool demonstrating how to use the schema and schematron rules.

lib/ -- A directory containing library .jar files needed by muCcdValidation.jar.

src/ -- A directory containing the source code for muCcdValidation.jar


The schematron files are modified versions of the files available for download at the HL7 Wiki: http://wiki.hl7.org/index.php?title=Continuity_of_Care_Document_%28CCD%29
Slight modifications have been made for Meaningful Use testing.  These modifications include the searching for templates/sections required by Meaningful Use (medications section, immunizations section, etc) and the ability to display confirmation of successful tests.



Command-line tool usage:

usage: java -jar CcdValidation.jar
 -input <filename>     input filename
 -output <filename>   output filename (without this option, the default
                    filename is ccdValidationResult[timestamp].xml)


Example: username@localhost:~/ccdfiles> java -jar muCcdValidation.jar -input CCD.xml -output report.xml

Note that the working directory (the directory that the .jar file is launched from) must be the directory containing the muCcdValidation.jar file (and has the lib, cdar2 and schematron directories as one level down from this).

The output of the report is an XML file containing a header information (where the data about the test is displayed, a list of errors/warnings (if any), a list of successes (if any) and the original input.  The XML will be in the following format:

<Report>
   <ReportHeader>
      <ValidationStatus>Complete</ValidationStatus>
      <ServiceName>CCD Validation</ServiceName>
      <ServiceProvider>NIST</ServiceProvider>
      <StandardType>CDA R2</StandardType>
      <StandardVersion>N/A</StandardVersion>
      <DateOfTest>20100331</DateOfTest>
      <TimeOfTest>170300.0548 -0400</TimeOfTest>
      <ReportPositiveIndicator>True</ReportPositiveIndicator>
      <ResultOfTest>Passed</ResultOfTest>
      <ErrorCount>0</ErrorCount>
      <WarningCount>7</WarningCount>
   </ReportHeader>
   <Results severity="errors"/>
   <Results severity="warning">
      <issue>
         <message>CONF-434: The value for "[Act | Observation | Procedure] / code" in a procedure activity SHOULD be selected from LOINC (codeSystem 2.16.840.1.113883.6.1) or SNOMED CT (codeSystem 2.16.840.1.113883.6.96).</message>
         <context>/ClinicalDocument[1]/component[1]/structuredBody[1]/component[5]/section[1]/entry[1]/procedure[1]</context>
         <test>cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96']</test>
      </issue>

...

(The rest of the XML report removed for brevity.)

Whether the overall test was a success or failure will be reported in the header along with other information such as the number of errors, date/time stamp, etc.  Each schematron issue contains a message, the context (an XPath expression showing the location) and the schematron test that was run at that location.

The command-line tool has been tested with Java 1.5.



For more information on this project, please visit http://healthcare.nist.gov/index.html

Any questions/comments/problems should be sent to: andrew.mccaffrey@nist.gov

