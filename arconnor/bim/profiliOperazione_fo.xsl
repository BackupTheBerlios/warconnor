<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
	<xsl:template match="root">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="21cm" page-width="29.7cm" 
									margin-top="15mm" margin-bottom="15mm" 
									margin-left="15mm" margin-right="15mm">
          <fo:region-body	margin-top="10mm" margin-bottom="15mm"/> 
					<fo:region-before extent="10mm" display-align="after"/>
					<fo:region-after extent="10mm" display-align="before"/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">			
				<fo:static-content flow-name="xsl-region-before">
					<fo:block text-align="end" font-size="8pt">
						Profilo Operazioni Contabili
					</fo:block>
				</fo:static-content> 
				<fo:static-content flow-name="xsl-region-after">
					<fo:block text-align="end" font-size="8pt">
							pag. <fo:page-number/>
					</fo:block>
				</fo:static-content> 
        <fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates select="operazione"/>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
	</xsl:template>
	<xsl:attribute-set name="set-titolo-1">
		<xsl:attribute name="font-size">11pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="set-titolo-2">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="set-titolo-3">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="set-testo">
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="space-before">0em</xsl:attribute>
		<xsl:attribute name="space-after">0em</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="set-tabella">
		<xsl:attribute name="width">100%</xsl:attribute>
		<xsl:attribute name="table-layout">fixed</xsl:attribute>
		<xsl:attribute name="border-color">black</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-collapse">collapse</xsl:attribute>
		<xsl:attribute name="break-after">page</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="set-riga">
		<xsl:attribute name="keep-together">always</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="set-colonna">
		<xsl:attribute name="padding-start">0.2em</xsl:attribute>
		<xsl:attribute name="padding-end">0.2em</xsl:attribute>
		<xsl:attribute name="padding-before">2pt</xsl:attribute>
		<xsl:attribute name="padding-after">1pt</xsl:attribute>		
		<xsl:attribute name="display-align">before</xsl:attribute>		
		<xsl:attribute name="border-top-color">black</xsl:attribute>
		<xsl:attribute name="border-top-style">solid</xsl:attribute>
		<xsl:attribute name="border-top-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-bottom-color">black</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">0.5pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:template match="operazione">
		<fo:table xsl:use-attribute-sets="set-tabella">
			<fo:table-column column-width="proportional-column-width(12.5%)"/>
			<fo:table-column column-width="proportional-column-width(12.5%)"/>
			<fo:table-column column-width="proportional-column-width(12.5%)"/>
			<fo:table-column column-width="proportional-column-width(12.5%)"/>
			<fo:table-column column-width="proportional-column-width(12.5%)"/>
			<fo:table-column column-width="proportional-column-width(12.5%)"/>
			<fo:table-column column-width="proportional-column-width(12.5%)"/>
			<fo:table-column column-width="proportional-column-width(12.5%)"/>
			<fo:table-body>
				<fo:table-row xsl:use-attribute-sets="set-riga" background-color="palegoldenrod">
					<fo:table-cell xsl:use-attribute-sets="set-colonna" number-columns-spanned="8">
						<fo:block xsl:use-attribute-sets="set-titolo-1">
							<xsl:value-of select="@Codice"/> - <xsl:value-of select="@Descrizione"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row xsl:use-attribute-sets="set-riga">
					<fo:table-cell xsl:use-attribute-sets="set-colonna" number-columns-spanned="2">
						<fo:block xsl:use-attribute-sets="set-titolo-3">Classe:</fo:block>
						<fo:block xsl:use-attribute-sets="set-testo"><xsl:value-of select="@Classe"/></fo:block>
					</fo:table-cell>
					<xsl:for-each select="tipo|rif_documento|dat_documento|soggetto|divisa|ritenuta">
						<xsl:call-template name="regolaValore"/>
					</xsl:for-each>
				</fo:table-row>
				<xsl:apply-templates select="iva|rata|causale"/>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<xsl:template name="regolaValore">
		<fo:table-cell xsl:use-attribute-sets="set-colonna">
			<fo:block xsl:use-attribute-sets="set-titolo-3"><xsl:value-of select="name(.)"/></fo:block>
			<xsl:if test="@Regola"><fo:block xsl:use-attribute-sets="set-testo">R=<xsl:value-of select="@Regola"/></fo:block></xsl:if>
			<xsl:if test="@Valore"><fo:block xsl:use-attribute-sets="set-testo">V=<xsl:value-of select="@Valore"/></fo:block></xsl:if>
			<xsl:if test="./text"><fo:block xsl:use-attribute-sets="set-testo">T=<xsl:value-of select="./text"/></fo:block></xsl:if>
			<xsl:if test="./input"><fo:block xsl:use-attribute-sets="set-testo">I=<xsl:value-of select="./input"/></fo:block></xsl:if>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="iva">
		<fo:table-row xsl:use-attribute-sets="set-riga">
			<fo:table-cell xsl:use-attribute-sets="set-colonna">
				<fo:block xsl:use-attribute-sets="set-titolo-3">Iva</fo:block>
			</fo:table-cell>
			<xsl:for-each select="codice|imponibile|imp_iva">
				<xsl:call-template name="regolaValore"/>
			</xsl:for-each>
		</fo:table-row>
	</xsl:template>
	
	<xsl:template match="rata">
		<fo:table-row xsl:use-attribute-sets="set-riga">
			<fo:table-cell xsl:use-attribute-sets="set-colonna">
				<fo:block xsl:use-attribute-sets="set-titolo-3">Rata</fo:block>
			</fo:table-cell>
			<xsl:for-each select="scadenza|importo|modo_pagamento">
				<xsl:call-template name="regolaValore"/>
			</xsl:for-each>
		</fo:table-row>
	</xsl:template>
	
	<xsl:template match="causale">
		<fo:table-row xsl:use-attribute-sets="set-riga">
			<fo:table-cell xsl:use-attribute-sets="set-colonna">
				<fo:block xsl:use-attribute-sets="set-titolo-3">Causale</fo:block>
				<fo:block xsl:use-attribute-sets="set-testo">
					<xsl:value-of select="@Codice"/>(<xsl:value-of select="@Segno"/>)
				</fo:block>
			</fo:table-cell>
			<xsl:for-each select="tipo|importo|conto|ente|prodotto|valuta|descrizione">
				<xsl:call-template name="regolaValore"/>
			</xsl:for-each>
		</fo:table-row>
		<fo:table-row>
			<fo:table-cell/>
			<xsl:for-each select="competenza|protocollo|partita">
				<xsl:call-template name="regolaValore"/>
			</xsl:for-each>
		</fo:table-row>
	</xsl:template>
	
</xsl:stylesheet>
