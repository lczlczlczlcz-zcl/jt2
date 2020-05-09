package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubboCartServiceImpl implements DubboCartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}

	/**
	 * 根据itemId和userId修改购物车记录信息
	 * 修改记录: 商品数量,修改时间
	 */
	@Override
	public void updateNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum())
				.setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("item_id",cart.getItemId())
					 .eq("user_id",cart.getUserId());
		//根据其中不为null的元素当作set条件
		//updateWrapper表示条件构造器.
		cartMapper.update(cartTemp, updateWrapper);
	}

	//cart对象中已经包含了userId和itemId
	@Override
	public void deleteCart(Cart cart) {
		//根据对象中不为null的属性充当where条件.
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);
	}

	/**
	 * 数据库中是否已经存在该数据.   user_id/item_id
	 * 如果不存在,则入库新增.
	 * 如果存在,  则更新购物车数量.
	 */
	@Override
	public void saveCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", cart.getUserId())
					.eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB == null) {
			//说明用户应该做新增操作
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//用户之前新增过记录 数量的修改
			int num = cart.getNum() + cartDB.getNum();
			Cart cartTemp = new Cart();
			cartTemp.setNum(num).setId(cartDB.getId()).setUpdated(new Date());
			//根据对象中不为null的元素更新数据.
			cartMapper.updateById(cartTemp);
			//sql:会将数据库中全部的字段(除了主键)都会进行更新操作.
			//cartMapper.updateById(cartDB);
		}
	}
}
