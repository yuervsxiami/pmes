package com.cnuip.pmes2.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* 辅助计算专利的公式工具类
* Create By Crixalis:
* 2018年1月22日 下午1:59:05
*/
public class CalculateUtil {

	/**
	 * @param pr 优先权信息,以'yyyy.mm.dd 国别号 编号'形式存储,如有多条使用;切割
	 * @return 将时间与之后的信息分割,如果有多条仍然以;切割
	 * list第一条为时间,第二条为国别号,编号
	 */
	public static List<String> splitPr(String prStr) {
		String[] prs = prStr.split(";");
		StringBuilder times = new StringBuilder();
		StringBuilder codes = new StringBuilder();
		for (String pr : prs) {
			//字符串无法容下一个时间则跳过
			if(pr.length()<8) {
				continue;
			}
			int secondPointLocation = pr.indexOf(".",pr.indexOf(".")+1);
			if(times.length()!=0) {
				times.append(";");
			}
			times.append(pr.substring(0, secondPointLocation+3).trim());
			if(codes.length()!=0) {
				codes.append(";");
			}
			codes.append(pr.substring(secondPointLocation+3).trim());
		}
		return Arrays.asList(times.toString(),codes.toString());
	}
	
	/**
	 * 截取专利申请号前的国别号
	 * @param an 申请号
	 * @return 国别号
	 */
	public static String getCountry(String an) {
		Pattern p = Pattern.compile("([0-9])");
		Matcher m = p.matcher(an);
		m.find();
		return an.substring(0, an.indexOf(m.group(0)));
	}
	/**
	 * 截取专利申请号国别号后的编号
	 * @param an 申请号
	 * @return 专利编号
	 */
	public static String getCode(String an) {
		Pattern p = Pattern.compile("([0-9])");
		Matcher m = p.matcher(an);
		m.find();
		return an.substring(an.indexOf(m.group(0)));
	}
	
}
