<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
	<xsl:template match="FunzioniProdotti">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="1cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block font-size="16pt" font-weight="bold" space-after="5mm">Funzioni di Prodotti</fo:block>
			    <xsl:apply-templates select="Blocco"/>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
	</xsl:template>
	<xsl:template match="Blocco">
	  <fo:block font-size="10pt" font-weight="bold" space-after.optimum="10pt">
			<xsl:value-of select="@Titolo"/>
		</fo:block>
		<xsl:apply-templates select="TestoBlocco"/>
		<xsl:apply-templates select="Immagine"/>
		<xsl:apply-templates select="Funzione"/>
 	</xsl:template>
	<xsl:template match="TestoBlocco">
		<fo:block font-size="10pt" text-align="start" space-after.optimum="15pt">
	    <xsl:apply-templates />
		</fo:block>
	</xsl:template>
	<xsl:template match="Immagine">
		<fo:block font-size="10pt" text-align="start" space-after.optimum="15pt">
	    <xsl:apply-templates />
		</fo:block>
	</xsl:template>
 	<xsl:template match="Funzione">
		<fo:block font-size="10pt" text-align="start" space-after.optimum="15pt">
	    <xsl:apply-templates />
		</fo:block>
	</xsl:template>

	
	<xsl:template match="br"><fo:block/></xsl:template>
	
	<xsl:template match="b">
		<fo:inline font-weight="bold">
			<xsl:apply-templates />
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="i">
		<fo:inline font-style="italic">
			<xsl:apply-templates />
		</fo:inline>
	</xsl:template>

	<xsl:template match="ul">
		<fo:list-block provisional-distance-between-starts="2em"
									provisional-label-separation="0.5em" >
			<xsl:apply-templates/>
		</fo:list-block>
	</xsl:template>
	<xsl:template match="ul/li">
		<fo:list-item>
			<fo:list-item-label end-indent="label-end()">
				<fo:block><fo:inline font-family="Symbol">&#x2022;</fo:inline></fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block>
					<xsl:apply-templates/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	
	
</xsl:stylesheet>
