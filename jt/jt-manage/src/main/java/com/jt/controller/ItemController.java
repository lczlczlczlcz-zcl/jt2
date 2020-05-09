package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

//因为ajax异步调用,所以要求返回值都是JSON串
@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * url:http://localhost:8091/item/query?page=1&rows=50
	 * args: 1.page=1 页数   2.rows 行数
	 *   返回值: EasyUITable对象
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows){
		
		return itemService.findItemByPage(page,rows);
	}
	
	
	/**
	 * 业务需求: 实现商品新增
	 * 1.url:/item/save
	 * 2.参数:$("#itemAddForm").serialize() 整个form表单数据
	 * 3.返回值: SysResult对象
	 */
	
	
	/**
	 * 商品关联操作,要求同时入库item/itemDesc
	 * @param item
	 * @return
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 * 实现商品的修改
	 * url:/item/update
	 * 参数: form表单提交
	 * 返回值: SysResult对象
	 *   
	 * 关于SpringMVC接参注意事项
	 * <input type='txt' name="id" value="100"/>
	 * 	item对象(id)/itemDesc(id)一齐接收参数
	 * 如果对象中出现了重名的属性需要额外的注意.
	 * 判断写法是否正确的!!!
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 * url:/item/delete
	 *   参数: ids: 1474392179,1474392178,1474392177,1474392176,1474392175
	 *   返回值:SysResult对象 
	 * 
	 * SpringMVC特性:
	 * 	如果用户传递的参数中间使用","号进行分割.
	 * 	则SpringMVC可以自动的转化为数组类型,按照","号进行拆分.
	 * 	
	 * 	Long... ids 可变参数类型
	 * 	Long[]	ids
	 */
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		int status = 2;	//下架操作
		itemService.updateItemStatus(ids,status);
		return SysResult.success();
	}
	
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		int status = 1;	//上架操作
		itemService.updateItemStatus(ids,status);
		return SysResult.success();
	}
	
	/**
	 * 业务:根据商品id查询商品的详情信息
	 * url: http://localhost:8091/item/query/item/desc/1474392166
	 * 参数:url中传递参数
	 * 返回值:SysResult对象
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}
	
	
	
	
	
}
