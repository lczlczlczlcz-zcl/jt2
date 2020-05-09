package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.annotation.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	//当引入对象的可能将来不会再次使用时添加.保证项目启动不报错.
	@Autowired(required=false)	//从容器中获取对象之后,完成注入
	//required=false 如果先spring中没有实例化的对象暂时先不注入 懒加载的感觉
	private Jedis jedis;

	//获取商品分类信息的名称
	@Override
	public String findItemCatNameById(Long itemCatId) {
		
		return itemCatMapper.selectById(itemCatId).getName();
	}

	
	//查询结果是VO对象 
	//问题:VO中的数据信息从哪里来?
	//数据来源: VO的数据应该从数据库记录中转化而来.
	@Override
	@CacheFind	//该方法适用缓存  k-v 返回值结果
	public List<EasyUITree> findItemCatListByParentId(Long parentId) {
		//1.动态获取数据库记录信息
		List<ItemCat> itemCatList = selectItemCatListByParentId(parentId);
		
		//2.定义空的VOList信息
		List<EasyUITree> treeList = new ArrayList<EasyUITree>(itemCatList.size());
		
		//3.将itemCat对象转化为EasyUITree对象
		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			//如果是父级则closed,否则open
			String state = itemCat.getIsParent()?"closed":"open";
			EasyUITree uiTree = new EasyUITree(id, text, state);
			treeList.add(uiTree);
		}
		return treeList;
	}


	private List<ItemCat> selectItemCatListByParentId(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		//(字段信息,查询的值)
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}

	
	/**
	 * 核心:需要从redis中动态的获取数据.
	 * 步骤:
	 * 	    首先查询缓存,如果缓存中没有数据,则查询数据库.
	 * 	    如果缓存中有数据,则直接返回.
	 * 	  
	 * 	  1.注入redisAPI工具对象
	 * 	  2.准备唯一的key. 包名.类名.方法名::参数ID
	 * 	  3.利用redis查询数据.如果没有查询数据库. 并且将数据库记录保存到缓存中
	 * 		方便下次使用.
	 */
	@SuppressWarnings("unchecked")	//压制警告
	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		Long startTime = System.currentTimeMillis();
		String key = "com.jt.service.ItemCatServiceImpl.findItemCatCache::"+parentId;
		String value = jedis.get(key);
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		if(StringUtils.isEmpty(value)) {
			//value的值为null   查询数据库获取返回的voList集合
			treeList = findItemCatListByParentId(parentId);
			Long endTime = System.currentTimeMillis();
			System.out.println("查询数据库的时间为:"+(endTime-startTime));
			//将数据库记录保存到redis缓存中.
			String json = ObjectMapperUtil.toJSON(treeList);
			jedis.set(key, json);
		}else {
			//表示缓存中有记录,可以直接将缓存记录转化为对象
			treeList = 
					ObjectMapperUtil.toObject(value, treeList.getClass());
			Long endTime = System.currentTimeMillis();
			System.out.println("查询缓存的时间为:"+(endTime-startTime));
		}
		return treeList;
	}
	
}
