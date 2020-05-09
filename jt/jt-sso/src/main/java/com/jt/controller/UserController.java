package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

//要求返回的是JSON数据
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/getMsg")
	public String getMsg() {
		
		return "单点登陆系统配置成功!!!!";
	}
	
	/**
	 * 根据web中传递的参数,实现数据校验
	 * url:http://sso.jt.com/user/check/{param}/{type}
	 *   参数:param/type
	 *   返回值: SysResult对象
	 * JSONP跨域访问 需要数据的封装
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
								 @PathVariable Integer type,
								 String callback) {
		
		//查询后台数据库,检查数据是否存在.
		Boolean flag = userService.checkUser(param,type);
		//封装返回值结果
		SysResult sysResult = SysResult.success(flag);
		return new JSONPObject(callback, sysResult);
	}
	
	
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 业务需求:根据ticket信息查询用户信息.
	 * url:http://sso.jt.com/user/query/58f8000b-bdec-40e6-910e-ac4f0cfb6cd8?callback=jsonp1585296544219&_=1585296544497
	 * 参数:ticket信息
	 * 返回值: SysResult对象
	 * 
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		String userJSON = jedisCluster.get(ticket);
		//判断查询是否正确
		if(StringUtils.isEmpty(userJSON)) {
			//如果为null,则表示数据,没有查询到.
			SysResult sysResult = SysResult.fail();
			return new JSONPObject(callback, sysResult);
		}
		//说明:用户信息没有错.   返回的数据是一个字符串
		//返回user对象
		//User user = ObjectMapperUtil.toObject(userJSON, User.class);
		SysResult sysResult = SysResult.success(userJSON);
		return new JSONPObject(callback, sysResult);
	}
	
	
	
	
	
	
}
