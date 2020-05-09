package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

	EasyUITable findItemByPage(Integer page, Integer rows);

	void updateItem(Item item, ItemDesc itemDesc);

	void deleteItems(Long[] ids);

	void updateItemStatus(Long[] ids, Integer status);

	void saveItem(Item item, ItemDesc itemDesc);

	ItemDesc findItemDescById(Long itemId);

	Item findItemById(Long itemId);
	
}
