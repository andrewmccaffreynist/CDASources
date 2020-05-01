README.txt

Meaningful Use: National Health Care Surveys Release 1.1, DSTU Validation Tool
http://healthcare.nist.gov/

Release Date: 12/23/2015

This package contains the validation tools used for validating the XML documents against the structure defined by the National Health Care Surveys Release 1, DSTU and all included and referenced standards/specifications/guidelines.


Package Contents:

schema/ -- A directory containing the HL7 CDA R2 schema and related files.  This schema has been modified to include additions to the CDA R2 model.

schematron/ -- A directory containing the schematron rules and related files for validating to National Health Care Surveys Release 1, DSTU.

muValidationNHCS.war -- The .war file that will allow for a local installation of the on-line version National Health Care Surveys Release 1, DSTU Validation Tool.

muNhcsValidator.jar -- A commandline version of the validation tool running the same engine as the on-line version.  See below for usage instructions.

src/ -- A directory containing the source code.

changelog.txt -- List of changes in this and previous releases.

The schematron files are versions of the files available in the National Health Care Surveys Release 1, DSTU Validation Tool download package.



Command-line tool usage:

Command-line tool usage:

usage: java -jar muNhcsValidator.jar
 -input <filename>     input filename
 -output <filename>    output filename (without this option, the default
                                        filename is validationResult[timestamp].xml)


Example: username@localhost:~/cdaFiles> java -jar muNhcsValidator.jar -input CDA.xml -output report.xml

Note that the working directory (the directory that the .jar file is launched from) must be the directory containing the muNhcsValidation.jar file (and has the lib, cdar2 and schematron directories as one level down from this).

The output of the report is an XML file containing header information (where the data about the test is displayed, a list of errors/warnings (if any), a list of successes (if any) and the original input.  The XML will be in the following format:

<Report>
    <ReportHeader>
        <ValidationStatus>Complete</ValidationStatus>
        <ServiceName>National Health Care Surveys Validation Report</ServiceName>
        <DateOfTest>20151119</DateOfTest>
        <TimeOfTest>113106.0166 -0500</TimeOfTest>
        <ResultOfTest>Failed</ResultOfTest>
        <ErrorCount>3</ErrorCount>
    </ReportHeader>
    <Results severity="schemaViolation" specification="cda_r2"/>
    <Results severity="errors" specification="">
        <issue>
            <message>The document must contain at least 1 of the document level templates for this
                schematron to be applicable.</message>
            <context>/ClinicalDocument[1]</context>
            <test>cda:templateId[@root='2.16.840.1.113883.10.20.22.1.1' and @extension =
                '2014-06-09'] or cda:templateId[@root='2.16.840.1.113883.10.20.34.1.2' and
                @extension = '2015-04-01'] or cda:templateId[@root='2.16.840.1.113883.10.20.34.1.3'
                and @extension = '2015-04-01'] or
                cda:templateId[@root='2.16.840.1.113883.10.20.34.1.4' and @extension = '2015-04-01']
                or cda:templateId[@root='2.16.840.1.113883.10.20.34.1.1' and @extension =
                '2015-04-01']</test>
        </issue>

...

(The rest of the XML report removed for brevity.)

Whether the overall test was a success or failure will be reported in the header along with other information such as the number of errors, date/time stamp, etc.  Violations of the CDA R2 schema will display the error message from the XML parser.  Each schematron issue contains a message, the context (an XPath expression showing the location) and the schematron test that was run at that location.

The Report will contain a reference to a stylesheet which will transform the XML report into human-readable HTML.  Opening the XML in most browsers will cause the transformation to automatically occur.

The command-line tool has been tested with Java 1.7.

Depending on the environment, the Java command-line tool may search for voc.xml files in different places.  Please verify that the JVM can find voc.xml file(s) either in the directory of the schematron file which calls them, or in the current working directory of the JVM.



.war file configuration:

Deploy the .war file to your web container (see the documentation of your web container software package for instructions on how this can be accomplished).

Once the .war project has been deployed, you will need to manually configure the application.  Open the webapps/muValidationNHCS/WEB-INF/web.xml file.  You will see several paths that will need to be changed to point to your local installation.

For example, you will see a path such as "/var/lib/tomcat/webapps/muValidationNHCS/schematron/nhcs/CDAR2_IG_NHCS_R1_D2_1_2015SEP.sch".  The "/var/lib/tomcat/" portion of the path will need to be changed to point at the location of your local Tomcat (or other web container) installation.


NOTE: NIST does not provide support for source-based installations of this software.  The source code is provided for demonstration purposes only.

For more information on this project, please visit http://healthcare.nist.gov/index.html

Any questions/comments/problems should be sent to the mailing list at: https://groups.google.com/forum/#!forum/cancer-reg-testing-tool


Disclaimers

Favorable outcome in the use of the test materials on this site does not imply
conformance recognition by NIST or HL7.

Links to non-Federal Government Web sites do not imply NIST endorsement of any
particular product, service, organization, company, information provider, or
content.

This software was developed at the National Institute of Standards and Technology
by employees of the Federal Government in the course of their official duties.
Pursuant to title 17 Section 105 of the United States Code this software is not
subject to copyright protection and is in the public domain.

The CDA Guideline Validator is an experimental system. NIST assumes no responsibility
whatsoever for its use by other parties, and makes no guarantees, expressed or implied,
about its quality, reliability, or any other characteristic. We would appreciate
acknowledgment if the software is used. This software can be redistributed and/or
modified freely provided that any derivative works bear some notice that they are
derived from it, and any modified versions bear some notice that they have been
modified.


