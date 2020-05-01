<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output encoding="UTF-8" method="html"/>
    <xsl:param name="stylesheet"/>
    <xsl:template match="/">
        <xsl:variable name="light">#EEEEFF</xsl:variable>
        <xsl:variable name="dark">#CCCCDD</xsl:variable>

        <html>
            <head>
                
                <title>Test result -- <xsl:value-of select="Report/ReportHeader/ServiceName"
                /></title>
            </head>

            <body>
                <h2>Validation Results</h2>
                <table>
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$light"/>
                        </xsl:attribute>
                        <td>Validation Status</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/ValidationStatus"/>
                        </td>
                    </tr>
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$dark"/>
                        </xsl:attribute>
                        <td>Service Name</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/ServiceName"/>
                        </td>
                    </tr>
                    <!--
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$light"/>
                        </xsl:attribute>
                        <td>Service Provider</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/ServiceProvider"/>
                        </td>
                    </tr>
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$dark"/>
                        </xsl:attribute>
                        <td>Standard Type</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/StandardType"/>
                        </td>
                    </tr>
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$light"/>
                        </xsl:attribute>
                        <td>Standard Version</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/StandardVersion"/>
                        </td>
                    </tr>
                    -->
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$light"/>
                        </xsl:attribute>
                        <td>Date of Test</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/DateOfTest"/>
                        </td>
                    </tr>
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$dark"/>
                        </xsl:attribute>
                        <td>Time of Test</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/TimeOfTest"/>
                        </td>
                    </tr>
                    <!--
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$dark"/>
                        </xsl:attribute>
                        <td>Report Positive Indicator</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/ReportPositiveIndicator"/>
                        </td>
                    </tr>
                    -->
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$light"/>
                        </xsl:attribute>
                        <td>Result of Test</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/ResultOfTest"/>
                        </td>
                    </tr>
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$dark"/>
                        </xsl:attribute>
                        <td>Error Count</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/ErrorCount"/>
                        </td>
                    </tr>
                    <!--
                    <tr>
                        <xsl:attribute name="bgcolor">
                            <xsl:value-of select="$light"/>
                        </xsl:attribute>

                        <td>Warning Count</td>
                        <td>
                            <xsl:value-of select="Report/ReportHeader/WarningCount"/>
                        </td>
                    </tr>
                    -->

                </table>
<!--
                <h3>Checks:</h3>

                <xsl:for-each select="Report/Results[@severity='checks']/issue">
                    <table cellpadding="3" cellspacing="3">
                        <xsl:attribute name="bgcolor">
                            <xsl:choose>
                                <xsl:when test="(position() mod 2 = 1)">
                                    <xsl:value-of select="$light"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="$dark"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <tr>
                            <td>Message</td>
                            <td>
                                <xsl:value-of select="message"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Context</td>
                            <td>
                                <xsl:value-of select="context"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Test</td>
                            <td>
                                <xsl:value-of select="test"/>
                            </td>
                        </tr>
                    </table>
                </xsl:for-each>
-->

                
                <xsl:if test="count(Report/Results[@severity='schemaViolation']/issue) > 0">
                    <h3>CDA Schema Violations (errors):</h3>
                    <xsl:for-each select="Report/Results[@severity='schemaViolation']/issue">
                        <table cellpadding="3" cellspacing="3">
                            <xsl:attribute name="bgcolor">
                                <xsl:choose>
                                    <xsl:when test="(position() mod 2 = 1)">
                                        <xsl:value-of select="$light"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$dark"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                            <tr>
                                <td>Message</td>
                                <td>
                                    <xsl:value-of select="message"/>
                                </td>
                            </tr>
                        </table>
                    </xsl:for-each>
                </xsl:if>

                <xsl:if test="count(Report/Results[@severity='errors' and @specification='ccd']/issue) > 0">
                    <h3>CCD Errors:</h3>
                    <xsl:for-each select="Report/Results[@severity='errors' and @specification='ccd']/issue">
                        <table cellpadding="3" cellspacing="3">
                            <xsl:attribute name="bgcolor">
                                <xsl:choose>
                                    <xsl:when test="(position() mod 2 = 1)">
                                        <xsl:value-of select="$light"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$dark"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                            <tr>
                                <td>Message</td>
                                <td>
                                    <xsl:value-of select="message"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Context</td>
                                <td>
                                    <xsl:value-of select="context"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Test</td>
                                <td>
                                    <xsl:value-of select="test"/>
                                </td>
                            </tr>
                        </table>
                    </xsl:for-each>
                </xsl:if>

                <xsl:if test="count(Report/Results[@severity='errors' and @specification='cda4cdt']/issue) > 0">
                    <h3>CDA4CDT Errors:</h3>
                    <xsl:for-each select="Report/Results[@severity='errors' and @specification='cda4cdt']/issue">
                        <table cellpadding="3" cellspacing="3">
                            <xsl:attribute name="bgcolor">
                                <xsl:choose>
                                    <xsl:when test="(position() mod 2 = 1)">
                                        <xsl:value-of select="$light"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$dark"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                            <tr>
                                <td>Message</td>
                                <td>
                                    <xsl:value-of select="message"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Context</td>
                                <td>
                                    <xsl:value-of select="context"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Test</td>
                                <td>
                                    <xsl:value-of select="test"/>
                                </td>
                            </tr>
                        </table>
                    </xsl:for-each>
                </xsl:if>

                <xsl:if test="count(Report/Results[@severity='errors' and @specification='hitsp_and_ihe']/issue) > 0">
                    <h3>HITSP and IHE Errors:</h3>
                    <xsl:for-each select="Report/Results[@severity='errors' and @specification='hitsp_and_ihe']/issue">
                        <table cellpadding="3" cellspacing="3">
                            <xsl:attribute name="bgcolor">
                                <xsl:choose>
                                    <xsl:when test="(position() mod 2 = 1)">
                                        <xsl:value-of select="$light"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$dark"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                            <tr>
                                <td>Message</td>
                                <td>
                                    <xsl:value-of select="message"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Context</td>
                                <td>
                                    <xsl:value-of select="context"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Test</td>
                                <td>
                                    <xsl:value-of select="test"/>
                                </td>
                            </tr>
                        </table>
                    </xsl:for-each>
                </xsl:if>

<!--
                <h3>Warnings:</h3>
                <xsl:for-each select="Report/Results[@severity='warning']/issue">
                    <table cellpadding="3" cellspacing="3">
                        <xsl:attribute name="bgcolor">
                            <xsl:choose>
                                <xsl:when test="(position() mod 2 = 1)">
                                    <xsl:value-of select="$light"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="$dark"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <tr>
                            <td>Message</td>
                            <td>
                                <xsl:value-of select="message"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Context</td>
                            <td>
                                <xsl:value-of select="context"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Test</td>
                            <td>
                                <xsl:value-of select="test"/>
                            </td>
                        </tr>
                    </table>
                </xsl:for-each>
-->
            </body>

        </html>
    </xsl:template>

</xsl:stylesheet>
