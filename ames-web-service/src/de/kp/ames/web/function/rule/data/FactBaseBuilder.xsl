<?xml version="1.0" encoding="UTF-8"?>
<!--
    - RuleEngine XSLT - FactBaseBuilder.xsl
    -
    - Copyright (c) 2010 Thorsten Sprenger - Kenthor GmbH 
-->

<!DOCTYPE stylesheet [
<!ENTITY space "<xsl:text> </xsl:text>">
<!ENTITY cr "<xsl:text>
</xsl:text>">

<!ENTITY REL_CONTAINS           "contains">

<!ENTITY REL_OBJECT             "Object">
<!ENTITY REL_ASSOCIATION        "Association">
<!ENTITY REL_CLASS              "Classification">
<!ENTITY REL_ATTRIBUTE          "Attribute">

<!ENTITY OID_FACT               "PrimaryFact">

<!ENTITY SLOT_GUID              "GUID">
<!ENTITY SLOT_OWNER             "Owner_GUID">
<!ENTITY SLOT_NAME              "Name">
<!ENTITY SLOT_VALUE             "Value">
<!ENTITY SLOT_SOURCE            "Source_GUID">
<!ENTITY SLOT_TARGET            "Target_GUID">

]>

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    
    xmlns:sm="urn:de:kp:xsd:sm:1.0"
    xmlns:kt="http://www.kenthor.com/XSLT-Functions"
    
    exclude-result-prefixes="xs xsi xsl fn sm kt" version="2.0">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>    
    <xsl:strip-space elements="*"/>
    
    <xsl:template match="/">
        <!--
        <Assert>
            <Rulebase mapClosure="universal">
            -->
                &cr;&cr;
                <xsl:apply-templates select="//sm:RegistryPackage"/>
                &cr;&cr;
                <xsl:apply-templates select="//sm:ExtrinsicObject"/>
                &cr;&cr;
                <xsl:apply-templates select="//sm:Association"/>
                &cr;&cr;
                <!--
            </Rulebase>
        </Assert>
        -->
    </xsl:template>
 
    <xsl:template match="sm:Association">
        <Atom>
            <Rel>&REL_ASSOCIATION;</Rel>
            <!--Rel><xsl:value-of select="@associationType"/></Rel-->
            <oid><Ind>&OID_FACT;</Ind></oid>
            <slot><Ind>&SLOT_GUID;</Ind><Ind type="String"><xsl:value-of select="@id"/></Ind></slot>
            <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="sm:Name"/></Ind></slot>
            <slot><Ind>&SLOT_VALUE;</Ind><Ind><xsl:value-of select="@associationType"/></Ind></slot>
            <slot><Ind>&SLOT_SOURCE;</Ind><Ind type="String"><xsl:value-of select="@sourceObject"/></Ind></slot>
            <slot><Ind>&SLOT_TARGET;</Ind><Ind type="String"><xsl:value-of select="@targetObject"/></Ind></slot>
        </Atom>
        <xsl:apply-templates select="sm:SlotList"/>
        <xsl:apply-templates select="sm:ClassificationList"/>
        &cr;&cr;
    </xsl:template>
    
    <xsl:template match="sm:RegistryPackage">
        <Atom>
            <Rel>&REL_OBJECT;</Rel>
            <oid><Ind>&OID_FACT;</Ind></oid>
            <slot><Ind>&SLOT_GUID;</Ind><Ind type="String"><xsl:value-of select="@id"/></Ind></slot>
            <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="sm:Name"/></Ind></slot>
        </Atom>
        <xsl:apply-templates select="sm:SlotList"/>
        <xsl:apply-templates select="sm:ClassificationList"/>        
        &cr;&cr;
        <xsl:for-each select="sm:RegistryObjectList/sm:ExtrinsicObject">
            <Atom>
                <Rel>&REL_CONTAINS;</Rel>
                <oid><Ind>&OID_FACT;</Ind></oid>
                <!--slot><Ind>&SLOT_GUID;</Ind><Ind type="String"><xsl:value-of select="kt:build-str(../../@id, '_', @id)"/></Ind></slot-->
                <slot><Ind>&SLOT_SOURCE;</Ind><Ind type="String"><xsl:value-of select="../../@id"/></Ind></slot>
                <slot><Ind>&SLOT_TARGET;</Ind><Ind type="String"><xsl:value-of select="@id"/></Ind></slot>
            </Atom>
        </xsl:for-each>
        <xsl:for-each select="sm:RegistryObjectList/sm:RegistryPackage">
            <Atom>
                <Rel>&REL_CONTAINS;</Rel>
                <oid><Ind>&OID_FACT;</Ind></oid>
                <!--slot><Ind>&SLOT_GUID;</Ind><Ind type="String"><xsl:value-of select="kt:build-str(../../@id, '_', @id)"/></Ind></slot-->
                <slot><Ind>&SLOT_SOURCE;</Ind><Ind type="String"><xsl:value-of select="../../@id"/></Ind></slot>
                <slot><Ind>&SLOT_TARGET;</Ind><Ind type="String"><xsl:value-of select="@id"/></Ind></slot>
            </Atom>
        </xsl:for-each>
        &cr;&cr;
        <xsl:apply-templates select="sm:RegistryObjectList/*"/>        
        &cr;&cr;
    </xsl:template>
    
    <xsl:template match="sm:ExtrinsicObject">
        <Atom>
            <Rel>&REL_OBJECT;</Rel>
            <oid><Ind>&OID_FACT;</Ind></oid>
            <slot><Ind>&SLOT_GUID;</Ind><Ind type="String"><xsl:value-of select="@id"/></Ind></slot>
            <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="sm:Name"/></Ind></slot>
        </Atom>
        <xsl:apply-templates select="sm:SlotList"/>
        <xsl:apply-templates select="sm:ClassificationList"/>        
        &cr;&cr;
    </xsl:template>
    
    <xsl:template match="sm:ClassificationList">
        <xsl:for-each select="sm:Classification">
            <Atom>
                <Rel>&REL_CLASS;</Rel>
                <oid><Ind>&OID_FACT;</Ind></oid>
                <slot><Ind>&SLOT_OWNER;</Ind><Ind type="String"><xsl:value-of select="../../@id"/></Ind></slot>
                <slot><Ind>&SLOT_VALUE;</Ind><Ind><xsl:value-of select="@classificationNode"/></Ind></slot>
            </Atom>
        </xsl:for-each>
    </xsl:template>

    <!-- Changed to handle attributes of each object/package as individual facts, as partial Plex couldn't be matched within rules -->
    <xsl:template match="sm:SlotList">
        <xsl:if test="kt:is(sm:Slot)">
            <xsl:for-each select="sm:Slot">
                <xsl:if test="kt:is(sm:ValueList)">
                    <Atom>
                        <Rel>&REL_ATTRIBUTE;</Rel>
                        <oid><Ind>&OID_FACT;</Ind></oid>
                        <slot><Ind>&SLOT_OWNER;</Ind><Ind type="String"><xsl:value-of select="../../@id"/></Ind></slot>
                        <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="@name"/></Ind></slot>
                        <slot><Ind>&SLOT_VALUE;</Ind><Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind></slot>
                    </Atom>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    
    
    <!-- ********************************************************************************************************************************* -->
    <!--  -->
    
    <xsl:function name="kt:str" as="xs:string">
        <xsl:param name="obj"/>
        
        <!--xsl:copy-of select="fn:normalize-space(fn:string-join($obj, ' '))"/-->
        <xsl:variable name="tmp"><xsl:value-of select="$obj"/></xsl:variable>
        <xsl:copy-of select="fn:normalize-space($tmp)"/>
    </xsl:function>
    
    <!-- ********************************************************************************************************************************* -->
    <!--  -->
    
    <xsl:function name="kt:space-str" as="xs:string">
        <xsl:param name="obj"/>
        
        <!--xsl:copy-of select="fn:normalize-space(fn:string-join($obj, ' '))"/-->
        <xsl:variable name="tmp"><xsl:value-of select="$obj"/></xsl:variable>
        <xsl:copy-of select="$tmp"/>
    </xsl:function>
    
    <!-- ********************************************************************************************************************************* -->
    <!--  -->
    
    <xsl:function name="kt:is" as="xs:boolean">
        <xsl:param name="obj"/>
        
        <xsl:choose>
            <xsl:when test="kt:str($obj)"><xsl:copy-of select="fn:true()"/></xsl:when>
            <xsl:otherwise><xsl:copy-of select="fn:false()"/></xsl:otherwise>
            <!-- will not work on empty variables or empty value-of commands -->
            <!--xsl:when test="fn:empty($obj)"><xsl:copy-of select="fn:false()"/></xsl:when>
                <xsl:otherwise><xsl:copy-of select="fn:true()"/></xsl:otherwise-->
        </xsl:choose>
    </xsl:function>
    
    <!--  -->
    
    <xsl:function name="kt:is-not" as="xs:boolean">
        <xsl:param name="obj"/>
        
        <xsl:copy-of select="fn:not(kt:is($obj))"/>
    </xsl:function>
    
    <!-- ********************************************************************************************************************************* -->
    <!--  -->
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/>
        <xsl:copy-of select="kt:space-str($a1)"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/><xsl:param name="a5"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4), kt:space-str($a5))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/><xsl:param name="a5"/>
        <xsl:param name="a6"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4), kt:space-str($a5), 
            kt:space-str($a6))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/><xsl:param name="a5"/>
        <xsl:param name="a6"/><xsl:param name="a7"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4), kt:space-str($a5), 
            kt:space-str($a6), kt:space-str($a7))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/><xsl:param name="a5"/>
        <xsl:param name="a6"/><xsl:param name="a7"/><xsl:param name="a8"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4), kt:space-str($a5), 
            kt:space-str($a6), kt:space-str($a7), kt:space-str($a8))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/><xsl:param name="a5"/>
        <xsl:param name="a6"/><xsl:param name="a7"/><xsl:param name="a8"/><xsl:param name="a9"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4), kt:space-str($a5), 
            kt:space-str($a6), kt:space-str($a7), kt:space-str($a8), kt:space-str($a9))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/><xsl:param name="a5"/>
        <xsl:param name="a6"/><xsl:param name="a7"/><xsl:param name="a8"/><xsl:param name="a9"/><xsl:param name="a10"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4), kt:space-str($a5), 
            kt:space-str($a6), kt:space-str($a7), kt:space-str($a8), kt:space-str($a9), kt:space-str($a10))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/><xsl:param name="a5"/>
        <xsl:param name="a6"/><xsl:param name="a7"/><xsl:param name="a8"/><xsl:param name="a9"/><xsl:param name="a10"/>
        <xsl:param name="a11"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4), kt:space-str($a5), 
            kt:space-str($a6), kt:space-str($a7), kt:space-str($a8), kt:space-str($a9), kt:space-str($a10), kt:space-str($a11))"/>
    </xsl:function>
    
    <xsl:function name="kt:build-str" as="xs:string">
        <xsl:param name="a1"/><xsl:param name="a2"/><xsl:param name="a3"/><xsl:param name="a4"/><xsl:param name="a5"/>
        <xsl:param name="a6"/><xsl:param name="a7"/><xsl:param name="a8"/><xsl:param name="a9"/><xsl:param name="a10"/>
        <xsl:param name="a11"/><xsl:param name="a12"/>
        <xsl:copy-of select="fn:concat(kt:space-str($a1), kt:space-str($a2), kt:space-str($a3), kt:space-str($a4), kt:space-str($a5), 
            kt:space-str($a6), kt:space-str($a7), kt:space-str($a8), kt:space-str($a9), kt:space-str($a10), kt:space-str($a11), kt:space-str($a12))"/>
    </xsl:function>
    
    <!-- ********************************************************************************************************************************* -->
    <!--  -->
    
    <xsl:function name="kt:parseDateTime" as="xs:dateTime">
        <xsl:param name="dt"/>
        
        <xsl:choose>
            <xsl:when test="fn:matches(kt:str($dt), '[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}[+-Z]?[0-9:]*')">
                <xsl:analyze-string select="kt:str($dt)" regex=".*([0-9]{{4}}-[0-9]{{2}}-[0-9]{{2}}T[0-9]{{2}}:[0-9]{{2}}:[0-9]{{2}}[+-Z]?[0-9:]*)">
                    <xsl:matching-substring><xsl:copy-of select="xs:dateTime(regex-group(1))"/></xsl:matching-substring>
                </xsl:analyze-string>                
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="xs:dateTime('0001-01-01T00:00:00Z')"/>
                <xsl:message>
                    <xsl:value-of select="kt:build-str('Invalid dateTime format encountered: ', kt:str($dt))"/>
                </xsl:message>
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:function>
    
    <!-- ********************************************************************************************************************************* -->
    <!-- capture all the remaining non-matches and ignore/debug them -->
    
    <xsl:template match="text()">
        &cr;
        <xsl:text>===============GEN================</xsl:text>
        &cr;
        <xsl:text>Parent:</xsl:text><xsl:value-of select="fn:name(..)"/>
        &cr;
        <xsl:text>Node  :</xsl:text><xsl:value-of select="fn:name(.)"/>
        &cr;
        <xsl:text>Copy  :</xsl:text><xsl:copy-of select="."/>
        &cr;
        <xsl:text>Value :</xsl:text><xsl:value-of select="."/>
        &cr;
        <xsl:text>==================================</xsl:text>
        &cr;
    </xsl:template>

</xsl:stylesheet>
