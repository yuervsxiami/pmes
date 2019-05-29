package com.cnuip.pmes2.util;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

/**
 * xml工具类
 * @auhor Crixalis
 * @date 2018/10/22 14:39
 */
public class XmlUtil {

	public static String selectNodeText(Document doc, String node) {
		List<Element> claims = selectElements(doc, node);
		StringBuilder sb = new StringBuilder("");
		for(Element claim: claims) {
			sb.append(claim.getText());
		}
		return sb.toString();
	};

	public static Element selectElement(Document doc, String node) {
		Element element =  (Element) doc.selectSingleNode(node);
		return element;
	}

	public static List<Element> selectElements(Document doc, String node) {
		List<Element> elements = doc.selectNodes(node);
		return elements;
	}
}
