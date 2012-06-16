<?xml version="1.0" encoding="UTF-8"?>
<!--
    - RuleEngine XSLT - ResultConverter.xsl
    -
    - Copyright (c) 2010 Thorsten Sprenger - Kenthor GmbH 
-->

<!DOCTYPE stylesheet [
<!ENTITY space "<xsl:text> </xsl:text>">
<!ENTITY cr "<xsl:text>
</xsl:text>">

<!ENTITY URN_GRAPH              "urn:oasis:names:tc:ebxml-regrep:SM:Common:Graph">
<!ENTITY URN_MEMORY             "urn:oasis:names:tc:ebxml-regrep:SM:Common:Memory">
<!ENTITY URN_CONCEPT            "urn:oasis:names:tc:ebxml-regrep:SM:Common:Concept">
<!ENTITY URN_CONNECTOR          "urn:oasis:names:tc:ebxml-regrep:SM:Common:Connector">

<!ENTITY URN_PACKAGE            "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage">
<!ENTITY URN_OBJECT             "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject">
<!ENTITY URN_ASSOCIATION        "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association">
<!ENTITY SM_RULE                "urn:oasis:names:tc:ebxml-regrep:SM:Prime:Rule">
<!ENTITY SM_CHOICE              "urn:oasis:names:tc:ebxml-regrep:SM:Prime:Rule:Choice">
<!ENTITY SM_AND_CONDITION       "urn:oasis:names:tc:ebxml-regrep:SM:Prime:Rule:AND">
<!ENTITY SM_OR_CONDITION        "urn:oasis:names:tc:ebxml-regrep:SM:Prime:Rule:OR">


<!ENTITY REL_CONDITION_PREFIX   "Condition_">

<!ENTITY REL_CONTAINS           "contains">
<!ENTITY REL_SIBLING            "sibling">

<!ENTITY REL_OBJECT             "Object">
<!ENTITY REL_ASSOCIATION        "Association">
<!ENTITY REL_CLASS              "Classification">
<!ENTITY REL_ATTRIBUTE          "Attribute">


<!ENTITY OID_FACT               "PrimaryFact">
<!ENTITY OID_DEDUCED            "PrimaryFact">
<!ENTITY OID_CONDITION          "RuleCondition">

<!ENTITY SLOT_GUID              "GUID">
<!ENTITY SLOT_OWNER             "Owner_GUID">
<!ENTITY SLOT_NAME              "Name">
<!ENTITY SLOT_VALUE             "Value">
<!ENTITY SLOT_SOURCE            "Source_GUID">
<!ENTITY SLOT_TARGET            "Target_GUID">

<!ENTITY VAR_GUID_PREFIX        "Var_GUID_">
<!ENTITY VAR_ATTR_PREFIX        "Var_Attr_">
<!ENTITY VAR_OWNER_GUID         "Var_Owner_GUID">
<!ENTITY VAR_NEW_GUID           "Var_New_GUID">
<!ENTITY VAR_TO_GUID            "Var_Target_GUID">
<!ENTITY VAR_FROM_GUID          "Var_Source_GUID">


<!ENTITY RULES_CLASS_PREFIX     "urn:oasis:names:tc:ebxml-regrep:SM:Prime:Rule">

<!ENTITY COMP_EQUAL             "urn:oasis:names:tc:ebxml-regrep:SM:SlotType:EqualTo">
<!ENTITY COMP_NOTEQUAL          "urn:oasis:names:tc:ebxml-regrep:SM:SlotType:NotEqual">
<!ENTITY COMP_LESS              "urn:oasis:names:tc:ebxml-regrep:SM:SlotType:SmallerThan">
<!ENTITY COMP_LESSEQUAL         "urn:oasis:names:tc:ebxml-regrep:SM:SlotType:SmallerOrEqualThan">
<!ENTITY COMP_GREATER           "urn:oasis:names:tc:ebxml-regrep:SM:SlotType:GreaterThan">
<!ENTITY COMP_GREATEREQUAL      "urn:oasis:names:tc:ebxml-regrep:SM:SlotType:GreaterOrEqualThan">
<!ENTITY COMP_CLOSEDINTERVAL    "urn:oasis:names:tc:ebxml-regrep:SM:SlotType:ClosedInterval">
<!ENTITY COMP_OPENINTERVAL      "urn:oasis:names:tc:ebxml-regrep:SM:SlotType:OpenInterval">

<!ENTITY SLOT_COMP_FROM         "FromAttributeName">
<!ENTITY SLOT_COMP_TO           "ToAttributeName">

]>

<!-- 
    MOD: The entity definitions used for the slot type description must refer to the SlotType taxonomy
    and not to Prime one
-->

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    
    xmlns:sm="urn:de:kp:xsd:sm:1.0"
    xmlns:kt="http://www.kenthor.com/XSLT-Functions"

    exclude-result-prefixes="xs xsi xsl fn kt" version="2.0">
    
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>    
    <xsl:strip-space elements="*"/>
    
    
    <xsl:template match="/">
        <sm:RegistryObjectList>
            <xsl:for-each select="//Atom[Rel='&REL_OBJECT;' and
                fn:not(slot[Ind[1]='&SLOT_GUID;']/Ind[2]=//Atom[Rel='&REL_CONTAINS;']/slot[Ind[1]='&SLOT_TARGET;']/Ind[2]) and
                fn:not(slot[Ind[1]='&SLOT_GUID;']/Ind[2]=//Atom[Rel='&REL_SIBLING;']/slot[Ind[1]='&SLOT_TARGET;']/Ind[2])]">
                <xsl:variable name="childID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                
                <xsl:call-template name="createRegistryElement"/>
                
                <xsl:for-each select="//Atom[Rel='&REL_ASSOCIATION;' and $childID=slot[Ind[1]='&SLOT_SOURCE;']/Ind[2]]">
                    <xsl:call-template name="createAssociation"/>
                </xsl:for-each>
            </xsl:for-each>
        </sm:RegistryObjectList>
    </xsl:template>
    
    
    <xsl:template name="createRegistryElement">
        <xsl:variable name="objID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>

        <xsl:choose>
            <xsl:when test="//Atom[Rel='&REL_CLASS;' and $objID=slot[Ind[1]='&SLOT_OWNER;']/Ind[2] and slot[Ind[1]='&SLOT_VALUE;']/Ind[2]='&URN_GRAPH;']">
                <sm:RegistryPackage>
                    <xsl:attribute name="id" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                    <xsl:attribute name="lid" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                    <xsl:attribute name="objectType" select="'&URN_PACKAGE;'"/>
                    
                    <xsl:call-template name="createSlotList"/>
                    <sm:Name><xsl:value-of select="slot[Ind[1]='&SLOT_NAME;']/Ind[2]"/></sm:Name>
                    <xsl:call-template name="createClassificationList"/>
                    <xsl:call-template name="createChildren"/>
                </sm:RegistryPackage>
            </xsl:when>
            <xsl:when test="//Atom[Rel='&REL_CLASS;' and $objID=slot[Ind[1]='&SLOT_OWNER;']/Ind[2] and slot[Ind[1]='&SLOT_VALUE;']/Ind[2]='&URN_MEMORY;']">
                <sm:RegistryPackage>
                    <xsl:attribute name="id" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                    <xsl:attribute name="lid" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                    <xsl:attribute name="objectType" select="'&URN_PACKAGE;'"/>
                    
                    <xsl:call-template name="createSlotList"/>
                    <sm:Name><xsl:value-of select="slot[Ind[1]='&SLOT_NAME;']/Ind[2]"/></sm:Name>
                    <xsl:call-template name="createClassificationList"/>
                    <xsl:call-template name="createChildren"/>
                </sm:RegistryPackage>
            </xsl:when>
            <xsl:otherwise>
                <sm:ExtrinsicObject>
                    <xsl:attribute name="id" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                    <xsl:attribute name="lid" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                    <xsl:attribute name="objectType" select="'&URN_OBJECT;'"/>
                    
                    <xsl:call-template name="createSlotList"/>
                    <sm:Name><xsl:value-of select="slot[Ind[1]='&SLOT_NAME;']/Ind[2]"/></sm:Name>
                    <xsl:call-template name="createClassificationList"/>
                </sm:ExtrinsicObject>
            </xsl:otherwise>
        </xsl:choose>
        
        <xsl:call-template name="createSiblings"/>
        
    </xsl:template>
    
    
    <xsl:template name="createSiblings">
        <xsl:variable name="siblingID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
        
        <xsl:for-each select="//Atom[Rel='&REL_OBJECT;' and slot[Ind[1]='&SLOT_GUID;']/Ind[2]=//Atom[Rel='&REL_SIBLING;' and $siblingID=slot[Ind[1]='&SLOT_SOURCE;']/Ind[2]]/slot[Ind[1]='&SLOT_TARGET;']/Ind[2]]">
            <xsl:variable name="childID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
            
            <xsl:call-template name="createRegistryElement"/>
            
            <xsl:for-each select="//Atom[Rel='&REL_ASSOCIATION;' and $childID=slot[Ind[1]='&SLOT_SOURCE;']/Ind[2]]">
                <xsl:call-template name="createAssociation"/>
            </xsl:for-each>
        </xsl:for-each>        
    </xsl:template>
    
    
    <xsl:template name="createChildren">
        <xsl:variable name="parentID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
        
        <xsl:if test="fn:count(//Atom[Rel='&REL_OBJECT;' and slot[Ind[1]='&SLOT_GUID;']/Ind[2]=//Atom[Rel='&REL_CONTAINS;' and $parentID=slot[Ind[1]='&SLOT_SOURCE;']/Ind[2]]/slot[Ind[1]='&SLOT_TARGET;']/Ind[2]]) > 0">
            <sm:RegistryObjectList>
                <xsl:for-each select="//Atom[Rel='&REL_OBJECT;' and slot[Ind[1]='&SLOT_GUID;']/Ind[2]=//Atom[Rel='&REL_CONTAINS;' and $parentID=slot[Ind[1]='&SLOT_SOURCE;']/Ind[2]]/slot[Ind[1]='&SLOT_TARGET;']/Ind[2]]">
                    <xsl:variable name="childID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                    
                    <xsl:call-template name="createRegistryElement"/>
                    
                    <xsl:for-each select="//Atom[Rel='&REL_ASSOCIATION;' and $childID=slot[Ind[1]='&SLOT_SOURCE;']/Ind[2]]">
                        <xsl:call-template name="createAssociation"/>
                    </xsl:for-each>
                </xsl:for-each>
            </sm:RegistryObjectList>
        </xsl:if>
    </xsl:template>
      
    
    <xsl:template name="createAssociation">
        <xsl:variable name="objID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>

        <sm:Association>
            <!--xsl:attribute name="home" select="kt:build-str('From: ', //ownedMember[@xmi:id=$src]/@name, ' to: ', //ownedMember[@xmi:id=$dest]/@name)"/-->
            <xsl:choose>
                <xsl:when test="kt:is(slot[Ind[1]='&SLOT_GUID;']/Ind[2])">
                    <xsl:attribute name="id" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                    <xsl:attribute name="lid" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="id" select="fn:generate-id()"/>
                    <xsl:attribute name="lid" select="fn:generate-id()"/>
                </xsl:otherwise>
            </xsl:choose>
            
            <xsl:attribute name="sourceObject" select="slot[Ind[1]='&SLOT_SOURCE;']/Ind[2]"/>
            <xsl:attribute name="targetObject" select="slot[Ind[1]='&SLOT_TARGET;']/Ind[2]"/>
            <xsl:attribute name="objectType" select="'&URN_ASSOCIATION;'"/>
            <xsl:attribute name="associationType" select="slot[Ind[1]='&SLOT_VALUE;']/Ind[2]"/>
            
            <xsl:call-template name="createSlotList"/>
            <sm:Name><xsl:value-of select="slot[Ind[1]='&SLOT_NAME;']/Ind[2]"/></sm:Name>
            <xsl:call-template name="createClassificationList"/>
        </sm:Association>
    </xsl:template>
    
    
    <!-- Build the SlotList of an Object -->

    <xsl:template name="createSlotList">
        <xsl:variable name="objID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>

        <xsl:if test="fn:count(//Atom[Rel='&REL_ATTRIBUTE;' and $objID=slot[Ind[1]='&SLOT_OWNER;']/Ind[2]]) > 0">
            <sm:SlotList>
                <xsl:for-each select="//Atom[Rel='&REL_ATTRIBUTE;' and $objID=slot[Ind[1]='&SLOT_OWNER;']/Ind[2]]">
                    <sm:Slot slotType="XRegistry">
                        <xsl:attribute name="name" select="slot[Ind[1]='&SLOT_NAME;']/Ind[2]"/>
                        <sm:ValueList>
                            <sm:Value>
                                <xsl:value-of select="slot[Ind[1]='&SLOT_VALUE;']/Ind[2]"/>
                            </sm:Value>
                        </sm:ValueList>
                    </sm:Slot>
                </xsl:for-each>
            </sm:SlotList>
        </xsl:if>
    </xsl:template>
    
    
    <!-- Build the ClassificationList of an Object -->
    
    <xsl:template name="createClassificationList">
        <xsl:variable name="objID" select="slot[Ind[1]='&SLOT_GUID;']/Ind[2]"/>
        
        <xsl:if test="fn:count(//Atom[Rel='&REL_CLASS;' and $objID=slot[Ind[1]='&SLOT_OWNER;']/Ind[2]]) > 0">
            <sm:ClassificationList>
                <xsl:for-each select="//Atom[Rel='&REL_CLASS;' and $objID=slot[Ind[1]='&SLOT_OWNER;']/Ind[2]]">
                    <sm:Classification>
                        <xsl:attribute name="classificationNode" select="slot[Ind[1]='&SLOT_VALUE;']/Ind[2]"/>
                    </sm:Classification>
                </xsl:for-each>
            </sm:ClassificationList>
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
