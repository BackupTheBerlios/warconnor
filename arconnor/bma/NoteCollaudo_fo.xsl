<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
	<xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
	<xsl:template match="FunzioniProdotti">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="1cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body margin="20mm 00mm 20mm 00mm"/>
					<fo:region-before border-after-style="solid" border-width="1pt" extent="10mm" display-align="after"/>					
					<fo:region-after border-before-style="solid" border-width="1pt" extent="10mm" display-align="before"/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
			
				<fo:static-content flow-name="xsl-region-before">
<!--				
					<fo:external-graphic src="c:/Progetti/warconnor_cvs/arconnor/bma/images/00057.jpg"/>
-->
					<fo:block text-align="right" font-size="16pt" font-weight="bold">
						<xsl:text>Note Collaudo Funzioni Prodotti</xsl:text>
					</fo:block>
				</fo:static-content>			

				<fo:static-content flow-name="xsl-region-after">
					<fo:block text-align="center" font-size="9pt">
						<fo:page-number/>
<!--					
						- <fo:page-number/> / <fo:page-number-citation ref-id="last-paragraph"/> -
-->
					</fo:block>
				</fo:static-content>				
			
        <fo:flow flow-name="xsl-region-body">
			    <xsl:apply-templates select="Blocco[substring(@Id,1,1)!='0']"/>
        </fo:flow>
				
      </fo:page-sequence>
    </fo:root>
	</xsl:template>
	
	<xsl:attribute-set name="blocco.data">
		<!-- Indent 
		<xsl:attribute name="margin-left">
			<xsl:value-of select="@Livello"/>
			<xsl:text>em</xsl:text>
		</xsl:attribute>
		-->
		<!-- Space Before -->
		<xsl:attribute name="space-before">
			<xsl:choose>
				<xsl:when test="@Livello=0">3em</xsl:when>
				<xsl:when test="@Livello=1">2em</xsl:when>
				<xsl:when test="@Livello=2">1em</xsl:when>
				<xsl:otherwise>1em</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">
			<xsl:choose>
				<xsl:when test="@Livello=0">16pt</xsl:when>
				<xsl:when test="@Livello=1">14pt</xsl:when>
				<xsl:when test="@Livello=2">12pt</xsl:when>
				<xsl:otherwise>10pt</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:template match="Blocco">
		<xsl:if test="@Titolo='Funzione'">
			<xsl:if test="count(./Funzione/NoteCollaudo/NotaCollaudo)>0">
				<fo:block xsl:use-attribute-sets="blocco.data">
					<xsl:apply-templates select="Funzione"/>
				</fo:block>
			</xsl:if>
		</xsl:if>
		<xsl:if test="not(@Titolo='Funzione')">
			<fo:block xsl:use-attribute-sets="blocco.data">
				<xsl:value-of select="@Titolo"/>
			</fo:block>
		</xsl:if>
 	</xsl:template>
	
 	<xsl:template match="Funzione">
    <xsl:value-of select="./Descrizione"/>
		<fo:block font-weight="normal" font-size="10pt" space-before.optimum="3pt" space-after.optimum="15pt">
			<fo:table table-layout="fixed">
				<fo:table-column column-width="1cm"/>
				<fo:table-column column-width="1cm"/>
				<fo:table-column column-width="2cm"/>
				<fo:table-column column-width="2cm"/>
				<fo:table-column column-width="5cm"/>
				<fo:table-column column-width="5cm"/>
				<fo:table-body>
					<xsl:apply-templates select="NoteCollaudo"/>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
	
 	<xsl:template match="NoteCollaudo">
		<xsl:for-each select="NotaCollaudo">
			<fo:table-row>
				<fo:table-cell number-columns-spanned="2" border-style="solid" border-width="0.5pt">
					<fo:block>
						<xsl:value-of select="./@Id"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block>
						<xsl:value-of select="./@Data"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block>
						<xsl:value-of select="./@User"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell number-columns-spanned="2">
					<fo:block font-size="8pt">
						<xsl:apply-templates select="./Nota"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:apply-templates select="./Interventi"/>
		</xsl:for-each>
	</xsl:template>
 	<xsl:template match="Interventi">
		<fo:table-row>
			<fo:table-cell number-columns-spanned="6">
				<fo:block font-size="8pt" font-style="italic"><xsl:text>Interventi:</xsl:text></fo:block>
			</fo:table-cell>
		</fo:table-row>
		<xsl:for-each select="Intervento">
			<fo:table-row>
				<fo:table-cell/>
				<fo:table-cell>
					<fo:block font-size="8pt">
						<xsl:value-of select="./@Id"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="8pt">
						<xsl:value-of select="./@Data"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="8pt">
						<xsl:value-of select="./@User"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="8pt">
						<xsl:value-of select="./Descrizione"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block font-size="8pt">
						<xsl:apply-templates select="./Nota"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="Nota">
		<xsl:apply-templates/>
	</xsl:template>
	
	
	<xsl:template match="br|BR"><fo:block/></xsl:template>
	<xsl:template match="b|B">
		<fo:inline font-weight="bold">
			<xsl:apply-templates />
		</fo:inline>
	</xsl:template>
	<xsl:template match="i|I">
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
