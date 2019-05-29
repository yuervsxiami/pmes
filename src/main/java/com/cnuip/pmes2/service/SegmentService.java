package com.cnuip.pmes2.service;

/**
 * @auhor Crixalis
 * @date 2018/10/18 10:26
 */
public interface SegmentService {

	/**
	 * 根据专利尾号生成语料库
	 * @param endNum
	 */
	void generateTxt(int endNum);

}
