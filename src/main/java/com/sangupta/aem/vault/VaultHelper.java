package com.sangupta.aem.vault;

import com.sangupta.aem.xml.XmlHelper;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.StringUtils;

public class VaultHelper {
	
	public static String createVaultFilterXml(String[] paths) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(XmlHelper.XML_HEADER);
		builder.append(StringUtils.SYSTEM_NEW_LINE);
		builder.append("<workspaceFilter version=\"1.0\">");
		builder.append(StringUtils.SYSTEM_NEW_LINE);
		
		if(AssertUtils.isNotEmpty(paths)) {
			for(String path : paths) {
				builder.append("    <filter root=\"" + path + "\"/>");
				builder.append(StringUtils.SYSTEM_NEW_LINE);
			}
		}
			
		builder.append("</workspaceFilter>");
		builder.append(StringUtils.SYSTEM_NEW_LINE);
		
		return builder.toString();
	}

}
