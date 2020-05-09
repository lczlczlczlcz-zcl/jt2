package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	
	//1.对象转化为JSON
	public static String toJSON(Object obj) {
		
		try {
			//对象转化为JSON调用对象的get方法.
			return MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);	//将检查异常,转化为运行时异常
		}
	}
	
	//2.JSON串转化对象  用户传递什么样的类型,返回什么样的对象.
	//ItemDesc itemDesc = (ItemDesc)ObjectMapperUtil.toObject(json,ItemDesc.class);
	public static <T> T toObject(String json,Class<T> targetClass) {
		
		try {
			//JSON串转化为对象调用对象的Set方法.
			return MAPPER.readValue(json, targetClass);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
