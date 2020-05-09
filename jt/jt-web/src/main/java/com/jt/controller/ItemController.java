package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

//实现商品的业务逻辑
@Controller			//如果返回时需要跳转页面
@RequestMapping("/items")
//@RestController		//如果返回值为JSON串时
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据商品信息,查询商品详情(item+itemDesc)
	 * url:http://www.jt.com/items/562379.html
	 *   参数:通过url请求地址传递562379 商品id号
	 *   返回值:页面逻辑名称   item.jsp
	 *
	 * 商品详情获取
	 * ${itemDesc.itemDesc }
	 */
	@RequestMapping("/{itemId}")
	public String findItemById(@PathVariable Long itemId,Model model) {
		
		//根据itemId查询后台数据
		Item item = itemService.findItemById(itemId);
		//将数据保存到域中.
		model.addAttribute("item", item);
		//根据商品ID,查询商品详情信息
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
