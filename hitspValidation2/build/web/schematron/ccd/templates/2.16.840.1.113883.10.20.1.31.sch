<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!ENTITY ent-2.16.840.1.113883.10.20.1.31 SYSTEM '2.16.840.1.113883.10.20.1.31.ent'>
]>
<schema xmlns="http://www.ascc.net/xml/schematron" xmlns:cda="urn:hl7-org:v3">
<!-- 
To use iso schematron instead of schematron 1.5, 
change the xmlns attribute from
"http://www.ascc.net/xml/schematron" 
to 
"http://purl.oclc.org/dsdl/schematron"
-->

<title>Result observation</title>
<ns prefix="cda" uri="urn:hl7-org:v3"/>

<phase id='errors'>
	<active pattern='p-2.16.840.1.113883.10.20.1.31-errors'/>
</phase>

<phase id='warning'>
	<active pattern='p-2.16.840.1.113883.10.20.1.31-warning'/>
</phase>

<phase id='manual'>
	<active pattern='p-2.16.840.1.113883.10.20.1.31-manual'/>
</phase>




&ent-2.16.840.1.113883.10.20.1.31;
  
</schema>
