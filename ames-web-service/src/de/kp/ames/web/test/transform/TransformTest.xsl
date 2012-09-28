<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0"
    >
    <xsl:strip-space elements="*"/>
    
    <xsl:template match="/">
        <html>
            <body>
                <h2>List of namespaces</h2>  
                <xsl:apply-templates/>  
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="rim:RegistryObject/rim:Name">
            <xsl:for-each select="rim:LocalizedString">
                <p>
                <xsl:value-of select="@value"/>
                </p>
            </xsl:for-each>
    </xsl:template>
    
</xsl:stylesheet> 