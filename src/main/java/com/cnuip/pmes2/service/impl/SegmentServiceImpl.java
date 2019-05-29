package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.inventory.PatentSub3Info;
import com.cnuip.pmes2.repository.core.PatentMapper;
import com.cnuip.pmes2.service.SegmentService;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.PerceptronPOSTagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/10/18 10:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SegmentServiceImpl implements SegmentService{

	@Autowired
	private PatentMapper patentMapper;

	private Logger logger = LoggerFactory.getLogger(getClass());
	private PerceptronPOSTagger tagger;

	@Override
	public void generateTxt(int endNum) {
		String lastSYSID = null;
		logger.info(String.format("*********快速生成尾号%d的语料库文件*********", endNum));
		String fileName = String.format("cnuip%d.txt", endNum);
		File file = null;
		try {
			file = ResourceUtils.getFile("classpath:data/model/cnuip/" + fileName);
//			file = ResourceUtils.getFile("classpath:data/model/cnuip/" + fileName);
			if(!file.exists()) {
				file.createNewFile();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = null;
		try {
			pw = new PrintWriter((new OutputStreamWriter(new FileOutputStream(file, true), "utf-8")));
			while (true) {
				List<PatentSub3Info> patentSub3InfoList = patentMapper.searchKeywordsByEndNum(endNum, lastSYSID);
				lastSYSID = printInText(pw, patentSub3InfoList);
				logger.info(String.format("尾号为%d的文件完成%d条专利", endNum, patentSub3InfoList.size()));
				if(patentSub3InfoList.size() < 1000) {
					logger.info(String.format("尾号为%d的文件生成完毕", endNum));
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(pw != null) {
				pw.close();
			}
		}
	}

	private String printInText(PrintWriter printWriter, List<PatentSub3Info> patentSub3InfoList) throws IOException {
		if(tagger == null) {
			tagger = new PerceptronPOSTagger(HanLP.Config.CRFPOSModelPath);
		}
		if(patentSub3InfoList == null || patentSub3InfoList.size() == 0) {
			return null;
		}
		int count = 0;
		for(PatentSub3Info patentSub3Info: patentSub3InfoList) {
			String[] keywords = patentSub3Info.getTrskeyword().split(";");
			String[] tags = tagger.tag(keywords);
			for(int i=0; i< keywords.length; i++) {
				printWriter.print(String.format("%s/%s  ", keywords[i], tags[i]));
				count++;
				if(count%1000 == 0) {
					printWriter.println();
				}
			}
			printWriter.flush();
		}
		return patentSub3InfoList.get(patentSub3InfoList.size()-1).getSysId();
	}


}
