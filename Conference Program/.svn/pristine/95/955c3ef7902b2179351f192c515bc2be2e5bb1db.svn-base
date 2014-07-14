<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>

<xsl:template match="period">
   
	<h1 class="section">
		<xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
		<xsl:value-of select="name"/><xsl:text> </xsl:text>
	</h1>
	<xsl:for-each select="./slot">
        <xsl:variable name="full-session" select="false and boolean(starts-with(name,'Tutorial') or starts-with(name,'Keynote') or starts-with(name,'Panel'))"/>
	    <h2 class="session" onclick="">
            <xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
			<strong>
                <xsl:value-of select="name"/>
                <xsl:if test="$full-session">
                    <xsl:text>: </xsl:text><xsl:value-of select="paper[position()=1]/title"/>
                </xsl:if>
            </strong>
		</h2>
		<h3 class="location">
            <strong>Location: <xsl:value-of select="location/room"/></strong>
        </h3>
        <xsl:if test="chair">
            <h4 class="chair">
                <strong>Chair: <xsl:value-of select="chair"/></strong>
            </h4>
        </xsl:if>
        <xsl:if test="count(./paper) &gt; 0"><br/></xsl:if>
		<xsl:for-each select="./paper">
			<div id="booklet_data" class="booklet_paper">
                <xsl:if test="not($full-session)">
                    <h4 class="paper_title" onclick="">
                        <xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
                        <strong><xsl:value-of select="title"/></strong>
                    </h4>
                </xsl:if>
                <p><xsl:value-of select="authors"/></p>
                <xsl:if test="abstract">
                    <div class="paper_content">
                        <p><strong>Abstract: </strong><xsl:value-of select="abstract"/></p>
                    </div>
     			</xsl:if>
                <xsl:for-each select="./authorInfo">
                    <div class="paper_content">
                        <img class="photo">
                            <xsl:attribute name="src">images/<xsl:value-of select= "photo" /></xsl:attribute>
                        </img>
                    <p><strong>Bio: </strong><xsl:value-of select="bio"/></p>
                    </div>
                </xsl:for-each>
			</div>
		</xsl:for-each>
		<br/>
	</xsl:for-each>
   <br/>
</xsl:template>
</xsl:stylesheet>