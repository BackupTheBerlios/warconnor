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
					<fo:block text-align-last="justify" font-size="16pt" font-weight="bold">
<!--										
border-after-style="solid" border-after-width="1pt" 
										space-before.minimum="1em"
										space-before.optimum="1.5em"
										space-before.maximum="2em"
-->
						<fo:external-graphic src="url('images/00057.jpg')" width="35mm" height="9mm"/>
						<fo:leader leader-pattern="dots"/>
						Note Collaudo Funzioni Prodotti
					</fo:block>
				</fo:static-content>			

				<fo:static-content flow-name="xsl-region-after">
					<fo:block text-align="center" font-size="9pt">
						- <fo:page-number/> / <fo:page-number-citation ref-id="last-page"/> -
					</fo:block>
				</fo:static-content>				
			
        <fo:flow flow-name="xsl-region-body">
			    <xsl:apply-templates select="Blocco[substring(@Id,1,1)!='0']"/>
        </fo:flow>
				
      </fo:page-sequence>
    </fo:root>
	</xsl:template>
	
	<!-- Stile per il titolo dipendente dal livello del blocco -->
	<xsl:attribute-set name="style.titolo">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">
			<xsl:choose>
				<xsl:when test="@Livello=0">14pt</xsl:when>
				<xsl:when test="@Livello=1">12pt</xsl:when>
				<xsl:when test="@Livello=2">10pt</xsl:when>
				<xsl:otherwise>8pt</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<xsl:attribute name="color">
			<xsl:choose>
				<xsl:when test="@Livello=0">red</xsl:when>
				<xsl:when test="@Livello=1">blue</xsl:when>
				<xsl:when test="@Livello=2">black</xsl:when>
				<xsl:otherwise>green</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>		
		<xsl:attribute name="margin-left">
			<xsl:choose>
				<xsl:when test="@Livello=0">0mm</xsl:when>
				<xsl:when test="@Livello=1">5mm</xsl:when>
				<xsl:when test="@Livello=2">10mm</xsl:when>
				<xsl:otherwise>10mm</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<xsl:attribute name="space-after">0.66em</xsl:attribute>
		<xsl:attribute name="break-before">
			<xsl:choose>
				<xsl:when test="@Livello=0">page</xsl:when>
				<xsl:otherwise>auto</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<xsl:attribute name="keep-with-next.within-page">
			<xsl:choose>
				<xsl:when test="@Livello=0">auto</xsl:when>
				<xsl:otherwise>always</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
	</xsl:attribute-set>
	<!-- Stile per tabella  -->
	<xsl:attribute-set name="style.tabella">
		<xsl:attribute name="table-layout">fixed</xsl:attribute>
		<xsl:attribute name="border-collapse">collapse</xsl:attribute>
		<xsl:attribute name="width">100%</xsl:attribute>
	</xsl:attribute-set>
	<!-- Stile per tabella: intestazione primaria  -->
	<xsl:attribute-set name="style.tabella.header">
		<xsl:attribute name="background-color">#DDDDDD</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">thin</xsl:attribute>
		<xsl:attribute name="padding-start">0.3em</xsl:attribute>
		<xsl:attribute name="padding-end">0.2em</xsl:attribute>
		<xsl:attribute name="padding-before">2pt</xsl:attribute>
		<xsl:attribute name="padding-after">2pt</xsl:attribute>
	</xsl:attribute-set>	
	<!-- Stile per tabella: celle primarie  -->
	<xsl:attribute-set name="style.tabella.data">
		<xsl:attribute name="border-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">thin</xsl:attribute>
		<xsl:attribute name="padding-start">0.3em</xsl:attribute>
		<xsl:attribute name="padding-end">0.2em</xsl:attribute>
		<xsl:attribute name="padding-before">2pt</xsl:attribute>
		<xsl:attribute name="padding-after">2pt</xsl:attribute>
	</xsl:attribute-set>	
	<!-- Stile per tabella: celle secondarie  -->
	<xsl:attribute-set name="style.tabella.data2">
		<xsl:attribute name="padding-start">0.3em</xsl:attribute>
		<xsl:attribute name="padding-end">0.2em</xsl:attribute>
		<xsl:attribute name="padding-before">2pt</xsl:attribute>
		<xsl:attribute name="padding-after">2pt</xsl:attribute>
	</xsl:attribute-set>	
	
	<xsl:template match="Blocco">
		<xsl:if test="@Titolo='Funzione'">
			<xsl:if test="count(./Funzione/NoteCollaudo/NotaCollaudo)>0">
				<fo:block xsl:use-attribute-sets="style.titolo">
					<xsl:apply-templates select="Funzione"/>
				</fo:block>
			</xsl:if>
		</xsl:if>
		<xsl:if test="not(@Titolo='Funzione')">
			<fo:block xsl:use-attribute-sets="style.titolo">
				<xsl:value-of select="@Titolo"/>
			</fo:block>
		</xsl:if>
 	</xsl:template>
	
 	<xsl:template match="Funzione">
    <xsl:value-of select="./Descrizione"/>

		<fo:block font-weight="normal"
					margin-left="0mm"
					font-size="8pt" 
					space-after.optimum="5pt">
			<fo:table xsl:use-attribute-sets="style.tabella">
				<fo:table-column column-width="10mm"/>
				<fo:table-column column-width="proportional-column-width(5)"/>
				<fo:table-column column-width="proportional-column-width(5)"/>
				<fo:table-column column-width="proportional-column-width(10)"/>
				<fo:table-column column-width="proportional-column-width(10)"/>
				<fo:table-column column-width="proportional-column-width(20)"/>
				<fo:table-column column-width="proportional-column-width(50)"/>
				<fo:table-body>
					<xsl:apply-templates select="NoteCollaudo"/>
				</fo:table-body>
			</fo:table>
		</fo:block>
<!--		</fo:block> -->
	</xsl:template>
	
 	<xsl:template match="NoteCollaudo">
		<fo:table-row>
			<fo:table-cell/>
			<fo:table-cell xsl:use-attribute-sets="style.tabella.header">
				<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
				<fo:block>Riferimenti</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="style.tabella.header">
				<fo:block>Data</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="style.tabella.header">
				<fo:block>Utente</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="style.tabella.header">
				<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
				<fo:block>Nota</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<xsl:for-each select="NotaCollaudo">
			<fo:table-row>
				<fo:table-cell/>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data">
					<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
					<fo:block><xsl:value-of select="./@Id"/></fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data">
					<fo:block><xsl:value-of select="./@Data"/></fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data">
					<fo:block><xsl:value-of select="./@User"/></fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data">
					<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
					<fo:block><xsl:apply-templates select="./Nota"/></fo:block>
				</fo:table-cell>
			</fo:table-row>
			<xsl:apply-templates select="./Interventi"/>
		</xsl:for-each>
	</xsl:template>
 	<xsl:template match="Interventi">
		<fo:table-row>
			<fo:table-cell/>
			<fo:table-cell xsl:use-attribute-sets="style.tabella.data2" number-columns-spanned="6">
				<fo:block font-style="italic"><xsl:text>Interventi:</xsl:text></fo:block>
			</fo:table-cell>
		</fo:table-row>
		<xsl:for-each select="Intervento">
			<fo:table-row>
				<fo:table-cell/>
				<fo:table-cell/>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data2">
					<fo:block><xsl:value-of select="./@Id"/></fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data2">
					<fo:block><xsl:value-of select="./@Data"/>
				</fo:block></fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data2">
					<fo:block><xsl:value-of select="./@User"/>
				</fo:block></fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data2">
					<fo:block><xsl:value-of select="./Descrizione"/></fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="style.tabella.data2">
					<fo:block><xsl:apply-templates select="./Nota"/></fo:block>
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
