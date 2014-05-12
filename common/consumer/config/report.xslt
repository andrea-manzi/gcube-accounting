<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="html" version="1.0" encoding="UTF-8"
		indent="yes" />
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Cache-Control" content="No-cache" />
				<meta http-equiv="Expires" content="0" />
				<meta http-equiv="Pragma" content="No-cache" />
				<meta http-equiv="Cache-Control" content="no-store" />
				<meta http-equiv="Cache-Control" content="max-age=0" />
				<link href="../styles.css" rel="stylesheet" type="text/css" />

			</head>
			<body>

				<xsl:for-each select="ROOT/TestSubtype">
					<p>
						<font size="5" color="red" face="Verdana">
							<xsl:value-of select="@Name" />
						</font>
					</p>
					<table border="1">
						<tbody>
							<tr>
								<th bgcolor="#9acd32" class="header">GHN</th>
								<th bgcolor="#9acd32" class="header">Type</th>
								<th bgcolor="#9acd32" class="header">ServiceName</th>
								<th bgcolor="#9acd32" class="header">ServiceClass</th>
								<th bgcolor="#9acd32" class="header">Message</th>
								<th bgcolor="#9acd32" class="header">Time</th>

							</tr>
							<xsl:for-each select="Notification">
								<tr>
									<td>
										<xsl:value-of select="GHN" />
									</td>
									<td>
										<xsl:value-of select="TestType" />
									</td>
									<td>
										<xsl:value-of select="ServiceName" />
									</td>
									<td>
										<xsl:value-of select="ServiceClass" />
									</td>
									<td>
										<xsl:value-of select="Message" />
									</td>
									<td>
										<xsl:value-of select="Time" />
									</td>
								</tr>
							</xsl:for-each>
						</tbody>
					</table>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
