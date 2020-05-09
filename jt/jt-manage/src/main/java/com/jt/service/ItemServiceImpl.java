package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	/**
	 * 实现思路:
	 * 	1.要什么 EasyUITable数据
	 * 	2.维护数据
	
	 rows  分页后的数据查询结果
		 1.查询分页    每页20条记录
		 * 	第一页:
		 	select * from tb_item limit 0,20;   0,19 1页
		 	第二页:
		 	select * from tb_item limit 20,20;	20,39 2页
		 	第三页:
		 	select * from tb_item limit 40,20;	40,59 3页
		 	第N页
		 	select * from tb_item limit (page-1)rows,rows;	
	 */
	/*@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		//total 记录总数
		long total = itemMapper.selectCount(null);
		int start = (page - 1) * rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		return new EasyUITable(total, itemList);
	}*/
	
	
	/**
	 * 参数说明:
	 * 	page	MP封装的一个分页对象信息
	 *  queryWrapper 查询的条件构造器
	 *	new Page<>(查询的页数, 每页条数);	
	 *     
	 *     如果使用MP的方式进行分页查询,则需要配置拦截器.
	 */
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		Page<Item> mpPage = new Page<>(page, rows);
		//按照更新时间降序排列
		QueryWrapper<Item> queryWrapper = new QueryWrapper<Item>();
		queryWrapper.orderByDesc("updated");
		IPage<Item> iPage = itemMapper.selectPage(mpPage, queryWrapper);
		long total = iPage.getTotal();
		List<Item> itemList = iPage.getRecords(); //分页结果
		return new EasyUITable(total, itemList);
	}
	
	//需要添加事物控制
	@Transactional
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		//完成商品入库操作 item主键自增 当数据入库之后,才有会主键
		item.setStatus(1)
			.setCreated(new Date())
			.setUpdated(item.getCreated());//保证时间一致.
		itemMapper.insert(item);
		
		//完成商品详情入库  如何获取商品Id号
		//思路:当item数据入库之后可以实现动态的数据的回显.(MP)
		//则可以获取itemId的值
		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Transactional
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	@Transactional
	public void deleteItems(Long[] ids) {
		//1.利用MP方式实现删除  in (xxx,xxx,xxx)  not in
		/*List<Long> longList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(longList);*/
		
		itemMapper.deleteByIds(ids);	//自己手写的sql
		itemDescMapper.deleteBatchIds(Arrays.asList(ids));//利用MP方式删除
	}

	/**
	 * 修改商品的状态信息 status/updated
	 * 要求:批量修改数据
	 *sql:update tb_item status=#{status},updated=#{date}/now()
	 *	  where id in (101,102,103)
	 */
	@Override
	public void updateItemStatus(Long[] ids, Integer status) {
		/*Long start = System.currentTimeMillis();	//程序开始时间
		//entity需要修改的数据信息
		Item item = new Item();
		item.setStatus(status).setUpdated(new Date());
		//updateWrapper 条件构造器
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		List<Long> longList = Arrays.asList(ids);
		updateWrapper.in("id", longList);
		itemMapper.update(item, updateWrapper);
		Long end = System.currentTimeMillis();	//程序开始时间
		System.out.println("当前方法操作的时间为:"+(end-start)+"毫秒");*/
		//自己独立完成利用Mapper.xml方式实现数据修改.
		
		Long start = System.currentTimeMillis();	//程序开始时间
		
		itemMapper.updateStatus(ids,status,new Date());
		Long end = System.currentTimeMillis();	//程序开始时间
		System.out.println("当前方法操作的时间为:"+(end-start)+"毫秒");
		
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		
		return itemMapper.selectById(itemId);
	}

	
	
	
	
	
	
}
