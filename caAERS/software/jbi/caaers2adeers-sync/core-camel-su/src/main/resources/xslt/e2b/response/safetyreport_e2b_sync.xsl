<xsl:stylesheet version="1.0"
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:aer="http://schema.integration.caaers.cabig.nci.nih.gov/aereport"
                xmlns:com="http://schema.integration.caaers.cabig.nci.nih.gov/common">

    <!-- Assuming that the following Parameters are derived runtime from header -->
    <xsl:param name="c2r_msg_id" /> <!-- a UUID -->
    <xsl:param name="c2r_today_204" />
    <xsl:param name="c2r_msg_number" />
    <xsl:param name="c2r_msg_date" />
    <xsl:param name="c2r_msg_sender_id" />
    <xsl:param name="c2r_msg_receiver_id" />

    <xsl:template match="/">
        <ichicsrack lang="en">
            <ichicsrmessageheader>
                <messagetype>ichicsrack</messagetype>
                <!-- always ichicsrack for ach-->
                <messageformatversion>1.1</messageformatversion>
                <!-- ich-icsrack-v1.1.dtd is used for icsr 2.1-->
                <messageformatrelease>1.0</messageformatrelease>
                <!-- release of dtd is 1.0 -->
                <messagenumb><xsl:value-of select="$c2r_msg_id" /></messagenumb>
                <!-- caAERS's unique message number, for this ACK message -->
                <messagesenderidentifier><xsl:value-of select="$c2r_msg_receiver_id" /></messagesenderidentifier>
                <messagereceiveridentifier><xsl:value-of select="$c2r_msg_sender_id" /></messagereceiveridentifier>
                <messagedateformat>204</messagedateformat>
                <messagedate><xsl:value-of select="$c2r_today_204" /></messagedate>
                <!-- Date on which ack got created : in CCYYMMDDHHMMSS format-->
            </ichicsrmessageheader>
            <acknowledgment>
                <messageacknowledgment>
                    <icsrmessagenumb><xsl:value-of select="$c2r_msg_number" /></icsrmessagenumb>
                    <!-- obtained as part of the input safety report message -->
                    <localmessagenumb><xsl:value-of select="//com:ServiceResponse/com:entityProcessingOutcomes/com:entityProcessingOutcome/dataBaseId" /></localmessagenumb>
                    <!-- optional, might not be present if there was processing error. This number was assigned to the input safety Message by caAERS -->
                    <icsrmessagesenderidentifier><xsl:value-of select="$c2r_msg_sender_id" /></icsrmessagesenderidentifier>
                    <icsrmessagereceiveridentifier><xsl:value-of select="$c2r_msg_receiver_id" /></icsrmessagereceiveridentifier>
                    <icsrmessagedateformat>204</icsrmessagedateformat>
                    <icsrmessagedate><xsl:value-of select="$c2r_msg_date" /></icsrmessagedate>
                    <!-- Date in CCYYMMDDHHMMSS -->
                    <!--
                    01= All Reports loaded into database
                    02 = ICSR Error, not all reports loaded into the database, check section B
                    03= SGML parsing error, no data extracted
                    -->
                    <xsl:if test="//soapenv:Fault">
                        <transmissionacknowledgmentcode>02</transmissionacknowledgmentcode>
                        <!--Optional:-->
                        <parsingerrormessage><xsl:value-of select="//soapenv:Fault/faultcode"/> : <xsl:value-of select="//soapenv:Fault/faultstring"/></parsingerrormessage>
                    </xsl:if>
                    <xsl:if test="//com:CaaersServiceResponse/com:ServiceResponse/wsError">
                        <transmissionacknowledgmentcode>02</transmissionacknowledgmentcode>
                        <!--Optional:-->
                        <parsingerrormessage><xsl:value-of select="//com:CaaersServiceResponse/com:ServiceResponse/wsError/errorCode"/> : <xsl:value-of select="//com:CaaersServiceResponse/com:ServiceResponse/wsError/errorDesc"/></parsingerrormessage>
                    </xsl:if>
                    <xsl:if test="//com:ServiceResponse/com:entityProcessingOutcomes/com:entityProcessingOutcome">
                        <transmissionacknowledgmentcode>01</transmissionacknowledgmentcode>
                    </xsl:if>
                </messageacknowledgment>
                <xsl:if test="//com:ServiceResponse/com:entityProcessingOutcomes/com:entityProcessingOutcome">

                    <!--Zero or more repetitions:-->
                    <reportacknowledgment>
                        <safetyreportid><xsl:value-of select="//com:ServiceResponse/com:entityProcessingOutcomes/com:entityProcessingOutcome/businessIdentifier" /></safetyreportid>
                        <!-- obtained from (safetyreport/safetyreportid) of the input safety report message-->
                        <!--Optional:-->
                        <safetyreportversion>2.1</safetyreportversion>
                        <!-- ICSR input message version -->
                        <!--Optional:-->
                        <localreportnumb><xsl:value-of select="//com:ServiceResponse/com:entityProcessingOutcomes/com:entityProcessingOutcome/dataBaseId" /></localreportnumb>
                        <!-- can be Database ID of DC or Report (if multiple reports possible per DC)-->
                        <!-- ****** *******************************************
                        authoritynumb or companynumb     only one of them is allowed in the incoming message and should follow a specific pattern.
                        Note :- AdEERS ticket number do not follow the country-company-message-id pattern.
                        ****** *********************************** -->
                        <!--Optional:-->
                        <authoritynumb><xsl:value-of select="//com:ServiceResponse/com:entityProcessingOutcomes/com:entityProcessingOutcome/dataBaseId" /></authoritynumb>
                        <!-- Identifier assigned by other regulatory authority for this Report-->
                        <!--Optional:-->
                        <!--<companynumb>AdEERS Ticket Number? Case Number ?</companynumb>-->
                        <!--(Refer section B.1.5) Not sure, either this can be Case number or AdEERS ticket number. -->
                        <!--Optional:-->
                        <receiptdateformat>204</receiptdateformat>
                        <!--Optional:-->
                        <receiptdate><xsl:value-of select="$c2r_msg_date" /></receiptdate>
                        <!-- Date on which input safety reprot message was received  : in CCYYMMDD format-->
                        <!--
                        01 = Report Loaded Successfully
                        02 = Report Not Loaded
                        -->
                        <reportacknowledgmentcode>01</reportacknowledgmentcode>
                        <!--Optional:-->
                        <!--<errormessagecomment>No comments or error</errormessagecomment>-->
                    </reportacknowledgment>
                </xsl:if>

            </acknowledgment>
        </ichicsrack>
    </xsl:template>

</xsl:stylesheet>