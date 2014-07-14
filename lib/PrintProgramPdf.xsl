<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"
            doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
            doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"/>
    <xsl:template match="/">
        <html xmlns="http://www.w3.org/1999/xhtml">
              <head>
                  <meta name="description" content="VLDB 2013 Program - dbTrento"/>
                  <title>VLDB 2013 Program - dbTrento</title>
                  <style type="text/css">
                      * {
                      	margin: 0;
                      	padding: 0;
                      }

                      body {
                      	background: #FFFFFF;
                      	font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
                      	font-size: 13px;
                      	color: #405060;
                      	min-height: 100%;
                      }

                      h1, h2, h3 {
                      	color: #345BA8;
                      }

                      h1 {
                      	font-size: 125%;
                      }

                      h2 {
                      	font-size: 115%;
                      }

                      h3 {
                      	font-size: 100%;
                      	font-weight: bold;
                      }

                      p, blockquote, ul, ol {
                      	line-height: 150%;
                      }

                      blockquote, ul, ol {
                      	margin-left: 3em;
                      }

                      a {
                      	color: #345BA8;
                      }

                      a:hover {
                      	text-decoration: none;
                      }

                      .content_details{
                      	position:absolute;
                      	top: -500px;
                      	left: 0;
                      	padding: 10px;
                      	visibility: hidden;
                      	border: 5px solid #E9EAEB;
                      	text-align: left;
                      	z-index: 100;
                      	opacity: 0.9;
                      	background-color:#F8F8F8;
                      	width: 300px;
                       }

                      .photo {
                      	float: left;
                      	border: 1px solid #F0F0F0;
                      	margin: 4px 10px 4px 0px;
                      	width: 80px;
                        page-break-inside: never;
                      }

                      .content .person_details {
                      	clear:both;
                      }


                      /* Page */

                      #container {
                      	height: auto;
                      	padding: 10px;
                      	padding-right: 0px;
                      }

                      #page {
                      	width: 830px;
                      	height: 100%;
                      	margin: 0 auto;
                      	border-left: 1px solid #E9EAEB;
                      	border-right: 1px solid #E9EAEB;
                      }

                      #content {
                      	float: none;
                      	width: 100%;
                      	height: auto;
                        margin: 0 auto;
                      }

                    td {
                        text-align: center;
                        border-top: 1px solid #777777;
                        border-left: 1px solid #777777;
                        padding: 2px 2px;
                    }
                    table {
                        min-height: 100%;
                            width: 100%;
                        border-bottom: 1px solid #777777;
                        border-right: 1px solid #777777;
                        page-break-inside: avoid;
                    }
                    .day {
                        background-color: #000000;
                        color: #FFFFFF;
                    }
                    .rooms, .rooms a {
                        background-color: #B3B3B3;
                        color: #000000;
                    }
                    .coffeebreak, .coffeebreak a {
                        background-color: #E6E6E6;
                        color: #000000;
                    }
                    .lunch, .lunch a {
                        background-color: #E6E6E6;
                        color: #000000;
                    }
                    .sessions, .sessions a {
                        background-color: #FFFFFF;
                        color: #000000;
                    }
                    .social, .social a {
                        background-color: #CC0000;
                          color: #FFFFFF;
                    }
                          td.sessions {
                              text-align: center;
                              width: 16%;
                          }
                      .booklet_paper, .booklet_paper_folded {
                      	font-family: Arial;
                      	font-size: 12px;
                      	color: #000000;
                      	margin-bottom: 5px;
                      }
                      .paper_title {
                      	color: #000000;
                      	font-size: 12px;
                      }
                      .paper_content {
                      	background: #F0F0F0;
                      	margin-top: 5px;
                      	margin-bottom: 5px;
                      	overflow: hidden;
                      	border-radius: 5px;
                      	padding-left: 10px;
                      	padding-right: 10px;
                      	padding-top: 5px;
                      	padding-bottom: 5px;
                        text-align: justify;
                      }
                      .location {
                      	color: #2CA9A9;
                      	font-size: 14px;
                      }
                      .chair {
                      	color: #006600;
                      	font-size: 12px;
                      	font-weight: normal;
                      }
                      .session {
                        margin-top: 10px;
                      	color: #CA3200;
                      	font-size: 14px;
                      }
                      .workshop_session, .workshop_session a {
                      	background-color: #FABF8F;
                          color: #000000;
                      }
                      .panel_session, .panel_session a {
                      	background-color: #66CCFF;
                          color: #000000;
                      }
                      .keynote_session, .keynote_session a {
                      	background-color: #CC0000;
                          color: #FFFFFF;
                      }
                      .tutorial_session, .tutorial_session a {
                      	background-color: #00AA00;
                          color: #000000;
                      }
                      .industry_session, .industry_session a {
                      	background-color: #99FE00;
                          color: #000000;
                      }
                      .demo_session, .demo_session a {
                      	background-color: #006666;
                          color: #FFFFFF;
                      }
                      .research_session, .research_session a {
                          background-color: #E9E400;
                          color: #000000;
                      }
                      .booklet_paper_folded .paper_content{
                      	display:none;
                      }
                      .section {
                      	font-weight: normal;
                      	padding: 1px 4px;
                      	color: #FFFFFF;
                      	background: #7C2B21;
                      	margin-top: 10px;
                      	margin-bottom: 10px;
                      	border-radius: 5px;
                        page-break-before: always;
                      }
                      .slot_name {
                          page-break-inside: never;
                      }
                      .paper_controls {
                      	width: 16px;
                      	float: right;
                      }
                  </style>
              </head>
              <body>
                  <div id="content" style="float: none;">
                  <xsl:apply-templates />
                  </div>
              </body>
        </html>
    </xsl:template>

<xsl:template match="period">

	<h1 class="section">
		<xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
		<xsl:value-of select="name"/><xsl:text> </xsl:text>
	</h1>
	<xsl:for-each select="./slot">
        <xsl:variable name="full-session" select="false and boolean(starts-with(name,'Tutorial') or starts-with(name,'Keynote') or starts-with(name,'Panel'))"/>
        <div class="slot_name">
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
        </div>
        <xsl:if test="count(./paper) &gt; 0"><br/></xsl:if>
		<xsl:for-each select="./paper">
			<div id="booklet_data" class="booklet_paper">
                <xsl:if test="not($full-session)">
                    <h4 class="paper_title" onclick="">
                        <xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
                        <strong><xsl:value-of select="title"/></strong>
                    </h4>
                </xsl:if>
                <p style="page-break-inside: never;"><xsl:value-of select="authors"/></p>
                <xsl:if test="abstract">
                    <div class="paper_content">
                        <p><strong>Abstract: </strong><xsl:value-of select="abstract"/></p>
                    </div>
     			</xsl:if>
                <xsl:for-each select="./authorInfo">
                    <div class="paper_content" style="page-break-inside: never;">
                        <img class="photo">
                            <xsl:attribute name="src">images/<xsl:value-of select= "photo" /></xsl:attribute>
                        </img>
                        <p><strong>Bio: </strong><xsl:value-of select="bio"/></p>
                    </div>
                </xsl:for-each>

			</div>
		</xsl:for-each>
        <xsl:if test="count(./paper) &gt; 1"><br/></xsl:if>
	</xsl:for-each>
   <br/>
</xsl:template>
</xsl:stylesheet>