package com.commonlib.parser;

import org.json.JSONObject;

import com.commonlib.model.BaseModel;


public class ResultJsonParser<T extends BaseModel> extends BaseJsonParser<T> {

	public ResultJsonParser(Class<T> modelClass) {
		super(modelClass);
	}

	/**
	 * 从接口返回的原始字符串解析，如果规则改变，可以改写此方法
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public T parse(String content) throws Exception {
		JSONObject jObj = new JSONObject(content);

		return parse(jObj);

	}
}
