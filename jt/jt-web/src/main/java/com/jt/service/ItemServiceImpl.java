package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private HttpClientService httpClientService;
	
	/**
	 * 问题:在jt-web中如何查询Item的数据????
	 *解决方案: 由jt-web服务器远程请求jt-manage的服务器动态
	 *	获取远程数据.
	 */
	@Override
	public Item findItemById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemById/"+itemId;
		//发起httpClinet请求
		String json = httpClientService.doGet(url);
		return ObjectMapperUtil.toObject(json, Item.class);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
		String itemDescJSON = httpClientService.doGet(url);
		return ObjectMapperUtil.toObject(itemDescJSON, ItemDesc.class);
	}

}
