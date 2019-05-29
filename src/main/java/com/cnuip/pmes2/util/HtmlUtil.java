package com.cnuip.pmes2.util;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.net.URL;

/**
 * HtmlUtil
 *
 * @author: xiongwei
 * Date: 2018/3/16 下午3:03
 */
public class HtmlUtil {

    /**
     * 解析html/xml为普通文本
     * @param xmlContent
     * @return
     */
    public static String parseHtmlToText(String xmlContent) {
        WebClient webClient = new WebClient();
        try {
            URL url = new URL("http://www.example.com");
            HtmlPage page = HTMLParser.parseHtml(new StringWebResponse(xmlContent, url), webClient.getCurrentWindow());
            return page.asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
