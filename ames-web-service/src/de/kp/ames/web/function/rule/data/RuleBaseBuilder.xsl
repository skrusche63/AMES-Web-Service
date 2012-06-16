<?xml version="1.0" encoding="UTF-8"?>
<!--
    - RuleEngine XSLT - RuleBaseBuilder.xsl
    -
    - Copyright (c) 2010 Thorsten Sprenger - Kenthor GmbH 
-->

<!DOCTYPE stylesheet [
<!ENTITY space "<xsl:text> </xsl:text>">
<!ENTITY cr "<xsl:text>
</xsl:text>">

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
    exclude-result-prefixes="xs xsi xsl fn sm kt" version="2.0">
    
    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>    
    <xsl:strip-space elements="*"/>

    <!-- ********************************************************************************************************************************* -->
    
    <!-- Search all rule packages -->

    <xsl:template match="/">
        <xsl:apply-templates select="//sm:RegistryPackage[sm:ClassificationList/sm:Classification[@classificationNode='&SM_RULE;']]"/>
    </xsl:template>

    <!-- Single rule package -->

    <xsl:template match="sm:RegistryPackage[sm:ClassificationList/sm:Classification[@classificationNode='&SM_RULE;']]">
        <xsl:apply-templates select="sm:RegistryObjectList/sm:ExtrinsicObject" mode="TREE"/>
    </xsl:template>

    <!-- General template to build to main rule condition body / head  -->

    <xsl:template name="buildRuleConditionBody">
        <Atom>
            <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', ancestor::sm:RegistryPackage[sm:ClassificationList/sm:Classification[@classificationNode='&SM_RULE;']]/sm:Name, '_', ancestor::sm:RegistryPackage[sm:ClassificationList/sm:Classification[@classificationNode='&SM_RULE;']]/@id)"/></Rel>
            <oid><Ind>&OID_CONDITION;</Ind></oid>
            <!--xsl:call-template name="buildVariables"/-->
            <resl><Var/></resl>                        
        </Atom>
    </xsl:template>

    <xsl:template name="buildRuleConditionHead">
        <Atom>
            <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', ancestor::sm:RegistryPackage[sm:ClassificationList/sm:Classification[@classificationNode='&SM_RULE;']]/sm:Name, '_', ancestor::sm:RegistryPackage[sm:ClassificationList/sm:Classification[@classificationNode='&SM_RULE;']]/@id)"/></Rel>
            <oid><Ind>&OID_CONDITION;</Ind></oid>
            <!--xsl:call-template name="buildVariables"/-->
        </Atom>
    </xsl:template>
    
    <!-- ************************ Condition - Tree ************************************* -->
    
    <!-- Create a RuleML rule from a Choice-Rule-Tree -->
    
    <xsl:template match="sm:ExtrinsicObject[sm:ClassificationList/sm:Classification[@classificationNode='&SM_CHOICE;']]" mode="TREE">
        <xsl:variable name="ownID" select="@id"/>
        
        <Implies>
            <And>
                <!-- Go from the Choice to each direct predecessor in the tree and set an ATOM condition for each of them (will be only OR / AND nodes) -->
                <xsl:for-each select="../sm:ExtrinsicObject[@id=../sm:Association[@targetObject=$ownID]/@sourceObject]">
                    <Atom>
                        <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', sm:Name, '_', @id)"/></Rel>
                        <oid><Ind>&OID_CONDITION;</Ind></oid>
                        <!--xsl:call-template name="buildVariables"/-->
                        <resl><Var/></resl>                        
                    </Atom>
                </xsl:for-each>
            </And>
            <xsl:call-template name="buildRuleConditionHead"/>
        </Implies>
        
        <!-- Build the result set -->
        <xsl:for-each select="../*[@id=../sm:Association[@sourceObject=$ownID]/@targetObject]">
            <xsl:variable name="resultID" select="@id"/>
            <xsl:apply-templates select="." mode="RESULT"/>
            <xsl:apply-templates select="../sm:Association[@targetObject=$resultID]" mode="RESULT"/>
        </xsl:for-each>
    </xsl:template>
    
    <!-- Create a RuleML rule from an AND-Tree -->
    
    <xsl:template match="sm:ExtrinsicObject[sm:ClassificationList/sm:Classification[@classificationNode='&SM_AND_CONDITION;']]" mode="TREE">
        <xsl:variable name="ownID" select="@id"/>
        
        <Implies>
            <And>
                <xsl:for-each select="../*[@id=../sm:Association[@targetObject=$ownID]/@sourceObject]">
                    <xsl:choose>
                        <xsl:when test="sm:ClassificationList/sm:Classification[@classificationNode='&SM_AND_CONDITION;']">
                            <Atom>
                                <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', sm:Name, '_', @id)"/></Rel>
                                <oid><Ind>&OID_CONDITION;</Ind></oid>
                                <!--xsl:call-template name="buildVariables"/-->
                                <resl><Var/></resl>                        
                            </Atom>
                        </xsl:when>
                        <xsl:when test="sm:ClassificationList/sm:Classification[@classificationNode='&SM_OR_CONDITION;']">
                            <Atom>
                                <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', sm:Name, '_', @id)"/></Rel>
                                <oid><Ind>&OID_CONDITION;</Ind></oid>
                                <!--xsl:call-template name="buildVariables"/-->
                                <resl><Var/></resl>                        
                            </Atom>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:variable name="condID" select="@id"/>
                            <xsl:apply-templates select="." mode="CONDITION"/>
                            <!-- TODO: VERIFY !!! -->
                            <!-- Also handle all associations which target this condition and originate from another condition of the same AND node -->
                            <xsl:apply-templates select="../sm:Association[@targetObject=$condID and @sourceObject=../*[@id=../sm:Association[@targetObject=$ownID]/@sourceObject]/@id]" mode="CONDITION"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
            </And>
            <Atom>
                <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', sm:Name, '_', @id)"/></Rel>
                <oid><Ind>&OID_CONDITION;</Ind></oid>
                <!--xsl:call-template name="buildVariables"/-->
            </Atom>
        </Implies>
    </xsl:template>
    
    <!-- Create RuleML rules from an OR-Tree -->
    
    <xsl:template match="sm:ExtrinsicObject[sm:ClassificationList/sm:Classification[@classificationNode='&SM_OR_CONDITION;']]" mode="TREE">
        <xsl:variable name="ownID" select="@id"/>
        <xsl:variable name="ownName" select="sm:Name"/>
        
        <!--
        <xsl:variable name="preparedHeadVars">
            <xsl:call-template name="buildVariables"/>
        </xsl:variable>
        -->
        
        <xsl:for-each select="../*[@id=../sm:Association[@targetObject=$ownID]/@sourceObject]">
            <xsl:choose>
                <xsl:when test="sm:ClassificationList/sm:Classification[@classificationNode='&SM_AND_CONDITION;']">
                    <Implies>
                        <And>
                            <Atom>
                                <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', sm:Name, '_', @id)"/></Rel>
                                <oid><Ind>&OID_CONDITION;</Ind></oid>
                                <!--xsl:call-template name="buildVariables"/-->
                                <resl><Var/></resl>                        
                            </Atom>
                        </And>
                        <Atom>
                            <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', $ownName, '_', $ownID)"/></Rel>
                            <oid><Ind>&OID_CONDITION;</Ind></oid>
                            <!-- xsl:call-template name="buildVariables"/ -->
                            <!-- xsl:copy-of select="$preparedHeadVars"/-->
                        </Atom>
                    </Implies>
                </xsl:when>
                <xsl:when test="sm:ClassificationList/sm:Classification[@classificationNode='&SM_OR_CONDITION;']">
                    <Implies>
                        <And>
                            <Atom>
                                <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', sm:Name, '_', @id)"/></Rel>
                                <oid><Ind>&OID_CONDITION;</Ind></oid>
                                <!--xsl:call-template name="buildVariables"/-->
                                <resl><Var/></resl>                        
                            </Atom>
                        </And>
                        <Atom>
                            <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', $ownName, '_', $ownID)"/></Rel>
                            <oid><Ind>&OID_CONDITION;</Ind></oid>
                            <!-- xsl:call-template name="buildVariables"/ -->
                            <!-- xsl:copy-of select="$preparedHeadVars"/-->
                        </Atom>
                    </Implies>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="condID" select="@id"/>
                    <Implies>
                        <And>
                            <xsl:apply-templates select="." mode="CONDITION"></xsl:apply-templates>
                            <!-- An OR node MAY NOT HAVE Extr. Obj.s with associations as conditions -->
                            <!-- In case this is needed, attach another AND node as child for each case -->
                        </And>
                        <Atom>
                            <Rel><xsl:value-of select="kt:build-str('&REL_CONDITION_PREFIX;', $ownName, '_', $ownID)"/></Rel>
                            <oid><Ind>&OID_CONDITION;</Ind></oid>
                            <!-- xsl:call-template name="buildVariables"/ -->
                            <!-- xsl:copy-of select="$preparedHeadVars"/-->
                        </Atom>
                    </Implies>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    
    <!-- Ignore all other matches -->
    
    <xsl:template match="*" mode="TREE">
        <!-- xsl:message select="kt:build-str('Ignore in mode TREE: ', sm:Name, ' Classifications: ', sm:ClassificationList/sm:Classification/@classificationNode)"></xsl:message -->
    </xsl:template>
    
    
    <!-- ************************ Condition - Nodes ************************************* -->
        
    <!-- Create a RuleML atom for each package within the condition graph - mode: CONDITION -->
    <!-- And check for contains associations to any children within this package            -->
    
    <xsl:template match="sm:RegistryPackage" mode="CONDITION">
        <xsl:apply-templates select="sm:ClassificationList/sm:Classification" mode="CONDITION"/>
        <Atom>
            <Rel>&REL_OBJECT;</Rel>
            <oid><Ind>&OID_FACT;</Ind></oid>
            <slot><Ind>&SLOT_GUID;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', sm:Name)"/></Var></slot>
            <resl><Var/></resl>
        </Atom>
        <xsl:apply-templates select="sm:SlotList" mode="CONDITION"/>
        &cr;&cr;
        <xsl:for-each select="sm:RegistryObjectList/sm:ExtrinsicObject">
            <Atom>
                <Rel>&REL_CONTAINS;</Rel>
                <oid><Ind>&OID_FACT;</Ind></oid>
                <slot><Ind>&SLOT_SOURCE;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', ../../sm:Name)"/></Var></slot>
                <slot><Ind>&SLOT_TARGET;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', sm:Name)"/></Var></slot>
                <resl><Var/></resl>
            </Atom>
            &cr;&cr;
        </xsl:for-each>
        <xsl:for-each select="sm:RegistryObjectList/sm:RegistryPackage">
            <Atom>
                <Rel>&REL_CONTAINS;</Rel>
                <oid><Ind>&OID_FACT;</Ind></oid>
                <slot><Ind>&SLOT_SOURCE;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', ../../sm:Name)"/></Var></slot>
                <slot><Ind>&SLOT_TARGET;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', sm:Name)"/></Var></slot>
                <resl><Var/></resl>
            </Atom>
            &cr;&cr;            
        </xsl:for-each>
        <xsl:apply-templates select="sm:RegistryObjectList/*" mode="CONDITION"/>
    </xsl:template>
    
    <!-- Create a RuleML atom for each object within the condition graph - mode: CONDITION -->
    
    <xsl:template match="sm:ExtrinsicObject" mode="CONDITION">
        <xsl:apply-templates select="sm:ClassificationList/sm:Classification" mode="CONDITION"/>
        <Atom>
            <Rel>&REL_OBJECT;</Rel>
            <oid><Ind>&OID_FACT;</Ind></oid>
            <slot><Ind>&SLOT_GUID;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', sm:Name)"/></Var></slot>
            <resl><Var/></resl>
        </Atom>
        <xsl:apply-templates select="sm:SlotList" mode="CONDITION"/>
        &cr;&cr;
    </xsl:template>
    
    <!-- Create the attribute part for the object atom above - mode: CONDITION -->
    
    <xsl:template match="sm:SlotList" mode="CONDITION">
        <xsl:if test="kt:is(sm:Slot)">
            <xsl:for-each select="sm:Slot">
                <xsl:if test="kt:is(sm:ValueList)">
                    <Atom>
                        <Rel>&REL_ATTRIBUTE;</Rel>
                        <oid><Ind>&OID_FACT;</Ind></oid>
                        <slot><Ind>&SLOT_OWNER;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', ../../sm:Name)"/></Var></slot>
                        <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="@name"/></Ind></slot>
                        <slot><Ind>&SLOT_VALUE;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var></slot>
                    </Atom>
                    
                    <xsl:choose>
                        <xsl:when test="@slotType='&COMP_GREATER;'">
                            <Atom>
                                <Rel>greaterThan</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind>
                            </Atom>
                        </xsl:when>
                        <xsl:when test="@slotType='&COMP_GREATEREQUAL;'">
                            <Atom>
                                <Rel>greaterThanOrEqual</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind>
                            </Atom>
                        </xsl:when>
                        <xsl:when test="@slotType='&COMP_LESS;'">
                            <Atom>
                                <Rel>lessThan</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind>
                            </Atom>
                        </xsl:when>
                        <xsl:when test="@slotType='&COMP_LESSEQUAL;'">
                            <Atom>
                                <Rel>lessThanOrEqual</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind>
                            </Atom>
                        </xsl:when>
                        <xsl:when test="@slotType='&COMP_CLOSEDINTERVAL;'">
                            <Atom>
                                <Rel>greaterThanOrEqual</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind>
                            </Atom>
                            <Atom>
                                <Rel>lessThanOrEqual</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[2]"/></Ind>
                            </Atom>
                        </xsl:when>
                        <xsl:when test="@slotType='&COMP_OPENINTERVAL;'">
                            <Atom>
                                <Rel>greaterThan</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind>
                            </Atom>
                            <Atom>
                                <Rel>lessThan</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[2]"/></Ind>
                            </Atom>
                        </xsl:when>
                        <xsl:when test="@slotType='&COMP_NOTEQUAL;'">
                            <Atom>
                                <Rel>notEqual</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind>
                            </Atom>
                        </xsl:when>
                        <xsl:otherwise>
                            <Atom>
                                <Rel>equal</Rel>
                                <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', ../../sm:Name, '_', @name)"/></Var>
                                <Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind>
                            </Atom>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    
    <!-- Create a RuleML atom for each classification of each object within the condition graph - mode: CONDITION -->
    
    <xsl:template match="sm:Classification" mode="CONDITION">
        <xsl:if test="fn:not(fn:contains(@classificationNode, '&RULES_CLASS_PREFIX;'))">
            <Atom>
                <Rel>&REL_CLASS;</Rel>
                <!--Rel><xsl:value-of select="kt:build-str('&REL_CLASS_PREFIX;', @classificationNode)"/></Rel-->
                <oid><Ind>&OID_FACT;</Ind></oid>
                <slot><Ind>&SLOT_OWNER;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', ../../sm:Name)"/></Var></slot>
                <slot><Ind>&SLOT_VALUE;</Ind><Ind><xsl:value-of select="@classificationNode"/></Ind></slot>
                <resl><Var/></resl>
            </Atom>
        </xsl:if>
    </xsl:template>
    
    <!-- Create a RuleML atom for each association within the condition graph - mode: CONDITION -->
    
    <xsl:template match="sm:Association" mode="CONDITION">
        <xsl:choose>
            <xsl:when test="fn:not(fn:contains(@associationType, '&RULES_CLASS_PREFIX;'))">
                <xsl:apply-templates select="sm:ClassificationList/sm:Classification" mode="CONDITION"/>
                <xsl:variable name="FromGUID" select="@sourceObject"/>
                <xsl:variable name="ToGUID" select="@targetObject"/>
                <Atom>
                    <Rel>&REL_ASSOCIATION;</Rel>
                    <oid><Ind>&OID_FACT;</Ind></oid>
                    <slot><Ind>&SLOT_VALUE;</Ind><Ind><xsl:value-of select="@associationType"/></Ind></slot>
                    <slot><Ind>&SLOT_SOURCE;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', ../*[@id=$FromGUID]/sm:Name)"/></Var></slot>
                    <slot><Ind>&SLOT_TARGET;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', ../*[@id=$ToGUID]/sm:Name)"/></Var></slot>
                    <resl><Var/></resl>
                </Atom>
                &cr;&cr;
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="buildInterObjectComparision"/>
            </xsl:otherwise>
        </xsl:choose>        
    </xsl:template>
    
    <!-- Ignore all other matches -->
    
    <xsl:template match="*" mode="CONDITION">
        <xsl:message select="kt:build-str('Ignore in mode CONDITION: ', sm:Name, ' Classifications: ', sm:ClassificationList/sm:Classification/@classificationNode)"></xsl:message>
    </xsl:template>
    
    <!-- Build an inter object/package comparision -->
    
    <xsl:template name="buildInterObjectComparision">
        <xsl:variable name="FromGUID" select="@sourceObject"/>
        <xsl:variable name="ToGUID" select="@targetObject"/>
        
        <!--
        <xsl:variable name="FromName" select="ancestor::sm:RegistryPackage[sm:ClassificationList/sm:Classification[@classificationNode='&CONDITION;']]//*[@id=$FromGUID]/sm:Name"/>
        <xsl:variable name="ToName" select="ancestor::sm:RegistryPackage[sm:ClassificationList/sm:Classification[@classificationNode='&CONDITION;']]//*[@id=$ToGUID]/sm:Name"/>
        -->
        <xsl:variable name="FromName" select="../*[@id=$FromGUID]/sm:Name"/>
        <xsl:variable name="ToName" select="../*[@id=$ToGUID]/sm:Name"/>
        
        <xsl:variable name="FromAttributeName" select="sm:SlotList/sm:Slot[@name='&SLOT_COMP_FROM;']/sm:ValueList/sm:Value[1]"/>
        <xsl:variable name="ToAttributeName" select="sm:SlotList/sm:Slot[@name='&SLOT_COMP_TO;']/sm:ValueList/sm:Value[1]"/>
        
        <xsl:if test="kt:is($FromAttributeName) and kt:is($ToAttributeName)">
            <Atom>
                <Rel>&REL_ATTRIBUTE;</Rel>
                <oid><Ind>&OID_FACT;</Ind></oid>
                <slot><Ind>&SLOT_OWNER;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', $FromName)"/></Var></slot>
                <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="$FromAttributeName"/></Ind></slot>
                <slot><Ind>&SLOT_VALUE;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $FromName, '_', $FromAttributeName)"/></Var></slot>
            </Atom>
            <Atom>
                <Rel>&REL_ATTRIBUTE;</Rel>
                <oid><Ind>&OID_FACT;</Ind></oid>
                <slot><Ind>&SLOT_OWNER;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_GUID_PREFIX;', $ToName)"/></Var></slot>
                <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="$ToAttributeName"/></Ind></slot>
                <slot><Ind>&SLOT_VALUE;</Ind><Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $ToName, '_', $ToAttributeName)"/></Var></slot>
            </Atom>
            <xsl:choose>
                <xsl:when test="@associationType='&COMP_GREATER;'">
                    <Atom>
                        <Rel>greaterThan</Rel>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $FromName, '_', $FromAttributeName)"/></Var>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $ToName, '_', $ToAttributeName)"/></Var>
                    </Atom>
                </xsl:when>
                <xsl:when test="@associationType='&COMP_GREATEREQUAL;'">
                    <Atom>
                        <Rel>greaterThanOrEqual</Rel>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $FromName, '_', $FromAttributeName)"/></Var>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $ToName, '_', $ToAttributeName)"/></Var>
                    </Atom>
                </xsl:when>
                <xsl:when test="@associationType='&COMP_LESS;'">
                    <Atom>
                        <Rel>lessThan</Rel>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $FromName, '_', $FromAttributeName)"/></Var>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $ToName, '_', $ToAttributeName)"/></Var>
                    </Atom>
                </xsl:when>
                <xsl:when test="@associationType='&COMP_LESSEQUAL;'">
                    <Atom>
                        <Rel>lessThanOrEqual</Rel>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $FromName, '_', $FromAttributeName)"/></Var>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $ToName, '_', $ToAttributeName)"/></Var>
                    </Atom>
                </xsl:when>
                <!--                                        -->
                <!-- Cannot be handled - default to 'equal' -->
                <!--
                    <xsl:when test="@associationType='&COMP_CLOSEDINTERVAL;'">
                    </xsl:when>
                    <xsl:when test="@associationType='&COMP_OPENINTERVAL;'">
                    </xsl:when>
                -->
                <xsl:when test="@associationType='&COMP_NOTEQUAL;'">
                    <Atom>
                        <Rel>notEqual</Rel>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $FromName, '_', $FromAttributeName)"/></Var>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $ToName, '_', $ToAttributeName)"/></Var>
                    </Atom>
                </xsl:when>
                <xsl:otherwise>
                    <Atom>
                        <Rel>equal</Rel>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $FromName, '_', $FromAttributeName)"/></Var>
                        <Var><xsl:value-of select="kt:build-str('&VAR_ATTR_PREFIX;', $ToName, '_', $ToAttributeName)"/></Var>
                    </Atom>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <!-- ************************ Result ************************************* -->
        
    <!-- Create a RuleML rule for each new association within the result graph - mode: RESULT -->
    
    <xsl:template match="sm:Association" mode="RESULT">
        <xsl:variable name="FromGUID" select="@sourceObject"/>
        <xsl:variable name="ToGUID" select="@targetObject"/>
        <Implies>
            <And>
                <!-- Final rule condition, created from CHOICE object -->
                <xsl:call-template name="buildRuleConditionBody"/>
            </And>
            <Atom>
                <Rel>&REL_ASSOCIATION;</Rel>
                <oid><Ind>&OID_DEDUCED;</Ind></oid>
                <!--slot><Ind>&SLOT_GUID;</Ind><Var>&VAR_NEW_GUID;</Var></slot-->
                <slot><Ind>&SLOT_GUID;</Ind><Ind><xsl:value-of select="@id"/></Ind></slot>
                <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="sm:Name"/></Ind></slot>
                <slot><Ind>&SLOT_VALUE;</Ind><Ind><xsl:value-of select="@associationType"/></Ind></slot>
                <slot><Ind>&SLOT_SOURCE;</Ind><Ind type="String"><xsl:value-of select="@sourceObject"/></Ind></slot>
                <slot><Ind>&SLOT_TARGET;</Ind><Ind type="String"><xsl:value-of select="@targetObject"/></Ind></slot>
            </Atom>
        </Implies>
        <xsl:apply-templates select="sm:ClassificationList/sm:Classification" mode="RESULT"/>
        <xsl:apply-templates select="sm:SlotList" mode="RESULT"/>
    </xsl:template>
    
    <!-- Create a RuleML rule for each new package within the result graph - mode: RESULT -->
    
    <xsl:template match="sm:RegistryPackage" mode="RESULT">
        <Implies>
            <And>
                <!-- Final rule condition, created from CHOICE object -->
                <xsl:call-template name="buildRuleConditionBody"/>
            </And>
            <Atom>
                <Rel>&REL_OBJECT;</Rel>
                <oid><Ind>&OID_DEDUCED;</Ind></oid>
                <!-- slot><Ind>&SLOT_GUID;</Ind><Var>&VAR_NEW_GUID;</Var></slot -->
                <slot><Ind>&SLOT_GUID;</Ind><Ind type="String"><xsl:value-of select="@id"/></Ind></slot>
                <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="sm:Name"/></Ind></slot>
            </Atom>
        </Implies>
        <xsl:apply-templates select="sm:ClassificationList/sm:Classification" mode="RESULT"/>
        <xsl:apply-templates select="sm:SlotList" mode="RESULT"/>

        <xsl:if test="fn:not(../../sm:ClassificationList/sm:Classification[@classificationNode = '&SM_RULE;'])">
            &cr;<xsl:comment select="kt:build-str(' CONTAINS: parent: ', ../../sm:Name, ' child: ', sm:Name)"/>&cr;
            <xsl:call-template name="buildContainsAssociation">
                <xsl:with-param name="parent" select="../.."/>
                <xsl:with-param name="child" select="."/>
            </xsl:call-template>
        </xsl:if>
        &cr;&cr;
    </xsl:template>
    
    <!-- Create a RuleML rule for each new object within the result graph - mode: RESULT -->
    
    <xsl:template match="sm:ExtrinsicObject" mode="RESULT">
        <Implies>
            <And>
                <!-- Final rule condition, created from CHOICE object -->
                <xsl:call-template name="buildRuleConditionBody"/>
            </And>
            <Atom>
                <Rel>&REL_OBJECT;</Rel>
                <oid><Ind>&OID_DEDUCED;</Ind></oid>
                <!-- slot><Ind>&SLOT_GUID;</Ind><Var>&VAR_NEW_GUID;</Var></slot -->
                <slot><Ind>&SLOT_GUID;</Ind><Ind type="String"><xsl:value-of select="@id"/></Ind></slot>
                <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="sm:Name"/></Ind></slot>
            </Atom>
        </Implies>
        <xsl:apply-templates select="sm:ClassificationList/sm:Classification" mode="RESULT"/>
        <xsl:apply-templates select="sm:SlotList" mode="RESULT"/>

        <xsl:if test="fn:not(../../sm:ClassificationList/sm:Classification[@classificationNode = '&SM_RULE;'])">
            &cr;<xsl:comment select="kt:build-str(' CONTAINS: parent: ', ../../sm:Name, ' child: ', sm:Name)"/>&cr;
            <xsl:call-template name="buildContainsAssociation">
                <xsl:with-param name="parent" select="../.."/>
                <xsl:with-param name="child" select="."/>
            </xsl:call-template>
        </xsl:if>
        &cr;&cr;
    </xsl:template>

    <!-- Create the attribute rules for the new object rule above -->

    <xsl:template match="sm:SlotList" mode="RESULT">
        <xsl:if test="kt:is(sm:Slot)">
            <xsl:for-each select="sm:Slot">
                <xsl:if test="kt:is(sm:ValueList)">
                    <Implies>
                        <And>
                            <!-- Final rule condition, created from CHOICE object -->
                            <xsl:call-template name="buildRuleConditionBody"/>
                        </And>
                        <Atom>
                            <Rel>&REL_ATTRIBUTE;</Rel>
                            <oid><Ind>&OID_DEDUCED;</Ind></oid>
                            <!-- slot><Ind>&SLOT_OWNER;</Ind><Var>&VAR_NEW_GUID;</Var></slot -->
                            <slot><Ind>&SLOT_OWNER;</Ind><Ind type="String"><xsl:value-of select="../../@id"/></Ind></slot>
                            <slot><Ind>&SLOT_NAME;</Ind><Ind><xsl:value-of select="@name"/></Ind></slot>
                            <slot><Ind>&SLOT_VALUE;</Ind><Ind><xsl:value-of select="sm:ValueList/sm:Value[1]"/></Ind></slot>
                        </Atom>
                    </Implies>                  
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    
    <!-- Create the attribute rules for the new object rule above -->
    
    <xsl:template match="sm:Classification" mode="RESULT">
        <xsl:if test="fn:not(fn:contains(@classificationNode,'&RULES_CLASS_PREFIX;'))">
            <Implies>
                <And>
                    <!-- Final rule condition, created from CHOICE object -->
                    <xsl:call-template name="buildRuleConditionBody"/>
                </And>
                <Atom>
                    <Rel>&REL_CLASS;</Rel>
                    <oid><Ind>&OID_DEDUCED;</Ind></oid>
                    <!-- slot><Ind>&SLOT_OWNER;</Ind><Var>&VAR_NEW_GUID;</Var></slot -->
                    <slot><Ind>&SLOT_OWNER;</Ind><Ind type="String"><xsl:value-of select="../../@id"/></Ind></slot>
                    <slot><Ind>&SLOT_VALUE;</Ind><Ind><xsl:value-of select="@classificationNode"/></Ind></slot>
                </Atom>
            </Implies>     
        </xsl:if>
    </xsl:template>

    <!-- Function to build 'contains' association -->
    
    <xsl:template name="buildContainsAssociation">
        <xsl:param name="parent"/>
        <xsl:param name="child"/>
        
        <Implies>
            <And>
                <!-- Final rule condition, created from CHOICE object -->
                <xsl:call-template name="buildRuleConditionBody"/>
            </And>
            <Atom>
                <Rel>&REL_CONTAINS;</Rel>
                <oid><Ind>&OID_DEDUCED;</Ind></oid>
                <slot><Ind>&SLOT_SOURCE;</Ind><Ind type="String"><xsl:value-of select="$parent/@id"/></Ind></slot>
                <slot><Ind>&SLOT_TARGET;</Ind><Ind type="String"><xsl:value-of select="$child/@id"/></Ind></slot>
            </Atom>
        </Implies>     
    </xsl:template>
        
    <!-- Ignore all other matches -->
    
    <xsl:template match="*" mode="RESULT">
        <!-- xsl:message select="kt:build-str('Ignore in mode RESULT: ', sm:Name, ' Classifications: ', sm:ClassificationList/sm:Classification/@classificationNode)"></xsl:message -->
    </xsl:template>
     
     
     
     
     
     
     
    <!-- ********************************************************************************************************************************* -->
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
    
    <xsl:template match="text()" mode="CONDITION">
        &cr;
        <xsl:text>===============CON================</xsl:text>
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
    
    <xsl:template match="text()" mode="TREE">
        &cr;
        <xsl:text>===============TREE===============</xsl:text>
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
    
    <xsl:template match="text()" mode="RESULT">
        &cr;
        <xsl:text>===============RES================</xsl:text>
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
