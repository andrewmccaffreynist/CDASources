<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://www.ascc.net/xml/schematron">
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <ns prefix="sdtc" uri="urn:hl7-org:sdtc"/>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <phase id="error">
        <active pattern="PATIENT-DEMOGRAPHICS"/>
        <active pattern="CAUSE-OF-DEATH"/>
        <active pattern="DEATH-ADMIN"/>
        <active pattern="DEATH-EVENT"/>
    </phase>
    <pattern id="PATIENT-DEMOGRAPHICS" name="PATIENT-DEMOGRAPHICS">
        <rule context="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole">
            <assert test="cda:patient/cda:administrativeGenderCode[@code = 'M']">Patient Sex Must be
                M</assert>
            <assert test="cda:patient/cda:birthTime[@value = '20000101']">Patient Birth Time Must be
                20000101</assert>
            <assert test="cda:patient/sdtc:deceasedTime[@value = '20170101']">Patient Death Time
                Must be 20170101</assert>
            <assert test="cda:patient/cda:raceCode[@code = '2054-5']">Patient Race Code (1) Must be
                2054-5</assert>
            <assert test="cda:patient/sdtc:raceCode[@code = '2106-3']">Patient Race Code (2) Must be
                2106-3</assert>
            <assert test="cda:patient/cda:ethnicGroupCode[@code = '2180-8']">Patient Ethnic Group
                Code Must be 2180-8</assert>
            <assert
                test="cda:id[@codeSystem = '2.16.840.1.113883.4.1' and @extension = '123-45-6789']"
                >Patient SSN Must be 123-45-6789</assert>
        </rule>
    </pattern>
    <pattern id="CAUSE-OF-DEATH" name="CAUSE-OF-DEATH">
        <rule
            context="//cda:section[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.2.5' and @extension = '2016-12-01']]">
            <assert
                test="cda:entry/cda:organizer[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.9' and @extension = '2016-12-01']]/cda:component/cda:observation[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.3.40' and @extension = '2016-12-01']]/cda:value[@code = 'nope']"
                >Injury involved must be nope</assert>
            <assert
                test="cda:entry/cda:observation[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.14' and @extension = '2016-12-01']]/cda:value[@code = 'nope']"
                >Tobacco Use must be nope</assert>
        </rule>
    </pattern>
    <pattern id="DEATH-ADMIN" name="DEATH-ADMIN">
        <rule
            context="//cda:section[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.2.3' and @extension = '2016-12-01']]">
            <assert
                test="cda:entry/cda:act[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.15' and @extension = '2016-12-01']]/cda:effectiveTime[@value = '20170101']"
                >Time pronounced dead must be 20170101</assert>
            <assert
                test="cda:entry/cda:procedure[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.2' and @extension = '2016-12-01']]/cda:entryRelationship/cda:observation[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.3' and @extension = '2016-12-01']]/cda:value[@value = 'true']"
                >Results available must be true</assert>
            <assert
                test="cda:entry/cda:act[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.4' and @extension = '2016-12-01']]/cda:entryRelationship/cda:observation[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.5']]/cda:value = 'Examined this guy.'"
                >Coroner Referral Note must be Examined this guy.</assert>
        </rule>
    </pattern>
    <pattern id="DEATH-EVENT" name="DEATH-EVENT">
        <rule
            context="//cda:section[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.2.6' and @extension = '2016-12-01']]">
            <assert
                test="cda:entry/cda:observation[cda:templateId[@root = '2.16.840.1.113883.10.20.26.1.11']]/cda:value[@code = '38605008']"
                >Manner of death must be 38605008</assert>
        </rule>
    </pattern>
</schema>
