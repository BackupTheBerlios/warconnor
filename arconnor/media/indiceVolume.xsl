<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<Html>
			<head>
				<title>Indice Volume</title>
				<link rel="stylesheet" href="style.css" type="text/css"/>
			</head>
			<body>
				<xsl:apply-templates select="Volume"/>
			</body>
		</Html>
	</xsl:template>
	<xsl:template match="Volume">
		<table class="Border" width="600" align='center'>
			<tr>
				<td class="DetailTitle" colspan="3">
					<xsl:value-of select="@Name"/>
				</td>
			</tr>
			<xsl:apply-templates select="File"/>
			<xsl:apply-templates select="Dir"/>
		</table>
	</xsl:template>
<!-- Template di PRIMO livello --> 	
	<xsl:template match="Volume/Dir">
		<tr>
			<td class="DetailLabel" colspan="3">
				<xsl:value-of select="@Name"/>
			</td>
		</tr>
		<xsl:apply-templates select="File"/>
		<xsl:apply-templates select="Dir"/>
	</xsl:template>
	<xsl:template match="Volume/File">
		<tr>
			<td class="DetailData" colspan="3">
				<xsl:value-of select="@Name"/>
			</td>
		</tr>
	</xsl:template>
<!-- Template di SECONDO livello --> 	
	<xsl:template match="Volume/Dir/Dir">
		<tr>
			<td class="DetailLabel" width="10%"> </td>
			<td class="DetailLabel" colspan="2">
				<xsl:value-of select="@Name"/>
			</td>
		</tr>
		<!-- <xsl:apply-templates select="File"/> -->
		<xsl:apply-templates select="Dir"/>
	</xsl:template>
	<xsl:template match="Volume/Dir/File">
		<tr>
			<td class="DetailLabel" width="10%"> </td>
			<td class="DetailData" colspan="2">
				<xsl:value-of select="@Name"/>
			</td>
		</tr>
	</xsl:template>
<!-- Template di TERZO livello --> 	
	<xsl:template match="Volume/Dir/Dir/Dir">
		<tr>
			<td class="DetailLabel" width="10%"> </td>
			<td class="DetailLabel" width="10%"> </td>
			<td class="DetailLabel" width="80%">
				<xsl:value-of select="@Name"/>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="Volume/Dir/Dir/File">
		<tr>
			<td class="DetailLabel" width="10%"> </td>
			<td class="DetailLabel" width="10%"> </td>
			<td class="DetailData" width="80%">
				<xsl:value-of select="@Name"/>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
