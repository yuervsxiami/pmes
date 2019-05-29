package com.cnuip.pmes2.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于储存一些常用的全局变量
 * @author duwenjuan
 */
public class GlobalVariableUtil {

	private static Map<String,String> variablities;
	
	static {
		variablities = new HashMap<>();
		variablities.put("maxSimilarNumberOfPatents", "7876");
		variablities.put("minSimilarNumberOfPatents", "0");
		variablities.put("maxSiblingPatentNum", "2");
		variablities.put("minSiblingPatentNum", "0");
		variablities.put("sim", "0.5");
	}
	
	public static String get(String key) {
		return variablities.get(key);
	}
	
	
}
