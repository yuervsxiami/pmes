package com.cnuip.pmes2.domain.core;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于解析patentXml
 * @auhor Crixalis
 * @date 2018/10/22 9:45
 */
@Getter
@Setter
public class PatentXml {

	// 发明名称
	private String name;
	// 摘要
	private String ab;
	// 类型
	private String type;
	// 权利要求书
	private String claims;
	// 说明书
	private String description;

	@Override
	public String toString() {
		return "PatentXml{" +
				"name='" + name + '\'' +
				", type='" + type + '\'' +
				", claims='" + claims + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	public String toKeywordContent() {
		return name + ab + claims + description;
	}

}
