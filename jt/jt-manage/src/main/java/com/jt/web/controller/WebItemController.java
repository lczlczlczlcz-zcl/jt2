package com.jt.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

/**
 * 标识后台为前台提供数据的Controller
 * @author LYJ
 */
@RestController
@RequestMapping("/web/item")
public class WebItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 需求:查询商品信息
	 * url:http://manage.jt.com:80/web/item/findItemById/562379
	 *  参数:restFul风格动态获取参数
	 * 返回值结果: Item对象 ~~~JSON 
	 */
	@RequestMapping("/findItemById/{itemId}")
	public Item findItemById(@PathVariable Long itemId) {
		
		return itemService.findItemById(itemId);
	}
	
	/**
	 * http://manage.jt.com/web/item/findItemDescById/"+itemId
	 *  参数:resultFul
	 * 返回值: itemDesc对象
	 */
	@RequestMapping("/findItemDescById/{itemId}")
	public ItemDesc findItemDescById(@PathVariable Long itemId) {
		
		return itemService.findItemDescById(itemId);
	}
	
	
	
	
	
	
	
}
