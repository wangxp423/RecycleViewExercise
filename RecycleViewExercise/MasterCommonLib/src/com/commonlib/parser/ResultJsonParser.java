package com.commonlib.parser;

import com.commonlib.entity.BaseEntity;

import org.json.JSONObject;


public class ResultJsonParser<T extends BaseEntity> extends BaseJsonParser<T> {

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
