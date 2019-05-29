package com.cnuip.pmes2.util;

import java.io.IOException;

import com.cnuip.pmes2.controller.api.request.QutationBean;
import com.cnuip.pmes2.controller.api.request.SimilarityBean;
import com.cnuip.pmes2.domain.tkResultBean.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.formula.functions.T;

public class StringToObjectUtil {

	private static ObjectMapper objectMapper;
	
	static {
		objectMapper = new ObjectMapper();
	}
	
	public static QutationBean toQutation(String str) {
		try {
			return objectMapper.readValue(str, QutationBean.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static SimilarityBean toSimlarity(String str) {
		try {
			return objectMapper.readValue(str, SimilarityBean.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKKeyWordResult toTKResult(String str) {
		try {
			return objectMapper.readValue(str, TKKeyWordResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKContentKeyWordResult toTKContentKeyWordResult(String str) {
		try {
			return objectMapper.readValue(str, TKContentKeyWordResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKPatentSimilarityResult toTKPatentSimilarityResult(String str) {
		try {
			return objectMapper.readValue(str, TKPatentSimilarityResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKSimilarityInfoResult toTKSimilarityInfoResult(String str) {
		try {
			return objectMapper.readValue(str, TKSimilarityInfoResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKQuoteNumberResult toTKQuoteNumberResult(String str) {
		try {
			return objectMapper.readValue(str, TKQuoteNumberResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKPatentQuoteListResult toTKPatentQuoteListResult(String str) {
		try {
			return objectMapper.readValue(str, TKPatentQuoteListResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKExpertRecomResult toTKExpertRecomResult(String str) {
		try {
			return objectMapper.readValue(str, TKExpertRecomResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKCompanyRecoResult toTKCompanyRecoResult(String str) {
		try {
			return objectMapper.readValue(str, TKCompanyRecoResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKIPCResult toTKIPCResult(String str) {
		try {
			return objectMapper.readValue(str, TKIPCResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKDetailIPCResult toTKDetailIPCResult(String str) {
		try {
			return objectMapper.readValue(str, TKDetailIPCResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKCompanyOnceNameResult toTKCompanyOnceNameResult(String str) {
		try {
			return objectMapper.readValue(str, TKCompanyOnceNameResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKCompanyPatentResult toTKCompanyPatentResult(String str) {
		try {
			return objectMapper.readValue(str, TKCompanyPatentResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TKPatentListChangeResult toTKPatentListChangeResult(String str) {
		try {
			return objectMapper.readValue(str, TKPatentListChangeResult.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}
