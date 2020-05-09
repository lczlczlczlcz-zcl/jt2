package com.jt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public class TestObjectMapper {
	
	//设定常量对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * 对象与JSON串的互相转化.
	 * @throws JsonProcessingException 
	 */
	@Test
	public void testJSONObject() throws JsonProcessingException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(101L)
				.setItemDesc("属性测试")
				.setCreated(new Date())
				.setUpdated(itemDesc.getCreated());
		//将对象转化为JSON
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//JSON转化为对象
		ItemDesc itemDesc2 = MAPPER.readValue(json,ItemDesc.class);
		System.out.println(itemDesc.getItemId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testListJSON() throws JsonProcessingException {
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(101L)
				.setItemDesc("属性测试")
				.setCreated(new Date())
				.setUpdated(itemDesc.getCreated());
		
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(101L)
				.setItemDesc("属性测试")
				.setCreated(new Date())
				.setUpdated(itemDesc.getCreated());
		list.add(itemDesc);
		list.add(itemDesc2);
				
		String json = MAPPER.writeValueAsString(list);
		System.out.println(json);
		
		//将JSON串转化为对象
		List<ItemDesc> list2 = MAPPER.readValue(json,list.getClass());
		System.out.println(list2);
	}
	
	/**
	 * 面试题:
	 * 		简述JSON转化的底层实现原理? 
	 * 		MAPPER.writeValueAsString(itemDesc);
	 * 答:
	 * 		
	 * 		对象转化为JSON,实际调用的对象的get方法,获取属性和属性的值.
	 * 		获取对象的所有getItemDesc()~~~~~去掉get前缀,
	 * 		之后讲首字母小写获取属性的key~~~itemDesc
	 * 		之后利用get方法的调用获取属性的值value
	 * 		之后开始拼接JSON  {"itemDesc":"value"}
	 * 		
	 * 		JSON转化为对象 ,调用对象的setXXX()方法,为属性赋值.
	 * 		如果发现set/get方法不对应,则可以通过
	 * 		@JsonIgnoreProperties(ignoreUnknown=true) //JSON转化时忽略未知属性
	 * 		实现JSON串转化.
	 * 	
	 */
	
	
	
	
	
	
	
	
}	
