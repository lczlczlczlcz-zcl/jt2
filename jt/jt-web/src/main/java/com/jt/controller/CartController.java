package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

//因为需要页面跳转
@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Reference(check=false)
	private DubboCartService cartService;
	
	/**
	 *   需求:当用户点击购物车按钮时,需要跳转到用户的购物车页面cart.jsp
	 * url:http://www.jt.com/cart/show.html
	 *   参数:暂时不需要
	 *   返回值: 页面逻辑名称   cart
	 *   页面数据显示:  应该根据userId动态的查询购物车记录.之后页面展现.
	 *   页面数据取值: ${cartList} 表示利用el表达式动态获取购物车列表信息
	 */
	@RequestMapping("/show")
	public String show(Model model,HttpServletRequest request) {
		//User user = (User) request.getSession().getAttribute("JT_USER");
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();	
		//利用dubbo实现远程rpc数据查询
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	
	/**
	 * 业务说明:当用户修改商品的数量时,需要发起ajax请求 实现商品数量的修改
	 * url:http://www.jt.com/cart/update/num/562379/15
	 *   参数: 562379商品ID号    15商品的数量信息
	 *   返回值:SysResult对象
	 *   
	 * 难点: 如何确定用户的唯一一条购物信息???? userId/itemId
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();	
		cart.setUserId(userId);
		cartService.updateNum(cart);
		return SysResult.success();
	}
	
	
	/**
	 * 删除购物车操作
	 * url: http://www.jt.com/cart/delete/1474392140.html
	 *   参数: itemId
	 *   返回值: 重定向到购物车列表页面
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();	
		Cart cart = new Cart();
		cart.setUserId(userId).setItemId(itemId);
		cartService.deleteCart(cart);
		//重新请求购物车列表页面
		return "redirect:/cart/show.html";
	}
	
	
	/**
	 * 业务需求:
	 * 	当用户点击加入购物车时,需要将购物车数据入库.
	 *     如果用户第一次新增,则新增数据.
	 * 	如果用户不是第一次新增,则做购物车数量的更新操作.
	 * 
	 * url:  http://www.jt.com/cart/add/562379.html
	 *   参数:   购物车数据
	 *   返回值: 重定向到购物车列表页面
	 *   
	 */
	@RequestMapping("/add/{itemId}")
	public String saveCart(Cart cart,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();	
		cart.setUserId(userId);
		cartService.saveCart(cart);
		return "redirect:/cart/show.html";
	}
}
