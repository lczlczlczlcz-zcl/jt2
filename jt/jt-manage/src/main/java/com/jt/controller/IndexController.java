package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;

@Controller
public class IndexController {
	
	@RequestMapping("/index")
	public String index() {
		
		//跳转页面路径
		return "index";
	}

	
	/**
	 * RestFul语法1
	 * 		1.参数必须使用{}进行包裹
	 * 		2.参数与参数之间必须使用/分割
	 * 		3.参数的位置必须固定的
	 * 		4.在方法中添加一个同名的参数,并且使用注解@PathVariable动态获取
	 * 
	 * @PathVariable
	 * 		value/name 标识参数名称,解决参数名称不一致的问题
	 * 		required:  是否为必传参数 默认为true
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("/page/{moduleName}")
	public String item_add(@PathVariable String moduleName) {

		return moduleName;	//返回页面逻辑名称
	}

	
	/*//@RequestMapping(value="/user",method=RequestMethod.POST)
	@PostMapping("/user")
	public String saveUser(User user) {
		
		//指定新增业务即可.
	}
	
	//@RequestMapping(value="/user",method=RequestMethod.DELETE)
	@DeleteMapping("/user")
	public String deleteUser(int id) {
		
		//执行删除操作即可
	}
	
	
	@PutMapping("/user")
	public String updateUser(User user) {
		
		//执行修改操作即可
	}
	
	@GetMapping("/user")
	public String updateUser(int id) {
		
		//执行查询操作即可
	}*/

}
