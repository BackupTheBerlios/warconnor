<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
	<xsl:template match="Volume">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="1cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body column-count="2" column-gap="0.25cm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block span="all" font-size="11pt" font-weight="bold" space-after="3mm">Indice Volume: <xsl:value-of select="@Name"/></fo:block>
          <xsl:apply-templates select="Dir"/>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
	</xsl:template>
	<xsl:template match="Dir">
	  <fo:block span="all" font-size="9pt" font-weight="bold" color="white" background-color="#31659C"><xsl:value-of select="@Name"/></fo:block>
		<xsl:apply-templates select="File"/>
	</xsl:template>
	<xsl:template match="File">
		<fo:block font-size="7pt" text-align="start">
			<xsl:value-of select="@Name"/>
		</fo:block>
		<xsl:if test="position()=round(last() div 2)">
			<fo:block break-after="column"/>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
