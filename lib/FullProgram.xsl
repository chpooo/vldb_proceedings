<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>

<xsl:template match="period">
    <div class="session_wrapper">
    	<xsl:attribute name="id">sec<xsl:value-of select="id"/></xsl:attribute>
	<h1 class="section">
		<xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
		<xsl:value-of select="name"/><xsl:text> </xsl:text>
	</h1>
	<div class="session_events_wrapper">
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
        <xsl:if test="location/room != ''">
		<h3 class="location">
            		<strong>Location: <xsl:value-of select="location/room"/></strong>
            		<img src="map.png" alt="map" title="Show map" class="paper_controls">
                    		<xsl:attribute name="onclick">TryOpen('../images/<xsl:value-of select="location/map"/>')</xsl:attribute>
            			</img>
        	</h3>
        </xsl:if>
        <xsl:if test="chair">
            <h4 class="chair">
                <strong>Chair: <xsl:value-of select="chair"/></strong>
            </h4>
        </xsl:if>
        <xsl:if test="count(./paper) &gt; 0"><br/></xsl:if>
		<xsl:for-each select="./paper">
			<div id="booklet_data" class="booklet_paper_folded">
		<xsl:if test="link">
                	<img src="pdf.png" alt="pdf" title="Download PDF" class="paper_controls">
                        	<xsl:attribute name="onclick">TryOpen('../<xsl:value-of select="link"/>')</xsl:attribute>
                	</img>
		 </xsl:if>
                <xsl:if test="abstract">
                    <img src="txt.png" alt="txt" title="Show Abstract" class="paper_controls" onclick="foldContent(this)"/>
                </xsl:if>
                <xsl:if test="not($full-session)">
                    <h4 class="paper_title" onclick="">
                        <xsl:attribute name="id">title<xsl:value-of select="id"/></xsl:attribute>
                        <strong><xsl:value-of select="title"/></strong>
                    </h4>
                </xsl:if>
                <xsl:if test="authors != ''"><p><xsl:value-of select="authors"/></p></xsl:if>
                <xsl:if test="abstract">
                    <div class="paper_content">
                        <p><xsl:value-of select="abstract"/></p>
                    </div>
     			</xsl:if>
                <xsl:for-each select="./authorInfo">
                    <div class="paper_content">
                        <img class="photo">
                            <xsl:attribute name="src">../images/<xsl:value-of select= "photo" /></xsl:attribute>
                        </img>
                    <p><strong>Bio: </strong><xsl:value-of select="bio"/></p>
                    </div>
                </xsl:for-each>
			</div>
		</xsl:for-each>
		<br/>
	</xsl:for-each>
	</div>
   </div>
   <br/>
</xsl:template>
</xsl:stylesheet>
