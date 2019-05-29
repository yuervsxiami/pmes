package com.cnuip.pmes2.resolver;

import org.springframework.util.ResourceUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * xml指定本地dtd
 * @auhor Crixalis
 * @date 2018/10/23 10:24
 */
public class LocalDtdResolver implements EntityResolver {

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		InputStream inputStream = new FileInputStream(ResourceUtils.getFile("classpath:dtd/cn-patent-document-06-10-27.dtd"));
		if(inputStream != null) {
			return new InputSource(inputStream);
		}
		return null;
	}
}
