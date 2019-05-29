package com.cnuip.pmes2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SystemProperties
 *
 * @author: xiongwei
 * Date: 2017/12/20 下午2:48
 */
@ConfigurationProperties(prefix = "pmes")
@Data
public class SystemProperties {

	private String cniprServer;
	private HighlightProperty highlightProperty;

	@Data
	public static class HighlightProperty {
		private String pre;
		private String post;
	}


}
