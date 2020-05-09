package com.jt.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@RestController
public class JSONPController {
	
	/**
	 *   跨域访问,获取商品详情信息
	 * url:http://manage.jt.com/web/testJSONP?callback=jQuery11110973588632504319_1585021758055&_=1585021758056
	 *  参数:?callback=jQuery1111   传递了回调函数名称
	 *  返回值:  必须特殊的格式封装  callback(JSON数据)
	 */
	//@RequestMapping("/web/testJSONP")
	public String testJSONP(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1000L).setItemDesc("JSONP跨域测试");
		//将对象转化为JSON
		String json = ObjectMapperUtil.toJSON(itemDesc);
		//将数据封装
		return callback +"("+json+")";
	}
	
	
	/**
	 * JSONP工具API
	 * function:回调函数方法
	 * value:  后台服务器返回的数据
	 */
	@RequestMapping("/web/testJSONP")
	public JSONPObject testJSONP2(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(2000L).setItemDesc("工具API测试!!!");
		return new JSONPObject(callback, itemDesc);
	}
	
	
	
	
	
}
