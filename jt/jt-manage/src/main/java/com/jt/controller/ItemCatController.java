package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	
	/**
	 * 业务:根据商品分类ID查询商品分类名称
	 * url: /item/cat/queryItemName
	 * 参数: {itemCatId:val}
	 * 返回值: 商品分类名称 name
	 * 
	 * 知识扩展:
	 * 问题:用户提交的参数是如何给方法的参数赋值?
	 * 铺垫:
	 * 		url:http://localhost:8091/user?id=1&name=tomcat猫
	 * 		1.SpringMVC封装了Servlet request对象 response对象
	 * 		2.servlet中获取用户的参数
	 * 知识讲解:
	 * 		1.利用Serlvet对象的Request对象利用getParameter方法动态获取
	 * 	属性的值.取值的名称必须正确.否则获取的值必然为null
	 * 		String idString = request.getParameter("itemCatId");
			Long idLong = Long.parseLong(idString);
			
			2.SpringMVC将上述的操作进行封装.
			规定!!!!:springMVC中的参数名称,必须与页面中name属性的名称一致
	 * 		<input type="text" name="id" value="100"/>
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		
		return itemCatService.findItemCatNameById(itemCatId);
	}
	
	
	/**
	 * 需求:查询商品分类信息,之后返回List<VO>对象
	 * url: /item/cat/list
	 * 参数: id=438    parentId ?????
	 * 返回值:List<EasyUITree>
	 * 
	 * 问题:
	 * 	1.参数为id但是实际的参数应该parentId  参数名称容易产生歧义
	 * 	2.用户点击目录时才会发起请求,用户在没有点击目录的情况下 应该指定
	 * 	默认值
	 * 解决方案:
	 * 	@RequestParam(value/name/required/defaultValue)
	 * 		作用:动态转化参数名称,并且设定默认值.
	 * 		属性1:value/name 接收url中的参数
	 * 		属性2:required	是否为必填项 默认值true
	 * 		属性3:defaultValue 设置默认值
	 */
	
	//item/cat/list?id=290
	@RequestMapping("/list")
	public List<EasyUITree> findItemCatListByParentId
	(@RequestParam(value="id",defaultValue="0") Long parentId){
		/*Long id = request.getParameter("id");
		
		Long parentId = id==null?"默认值":id;*/
		
		//1.定义parentId  默认条件下查询一级目录信息
		//查询后台的数据库,获取商品分类信息
		return itemCatService.findItemCatListByParentId(parentId);
		//利用下列方法实现数据的缓存查询.
		//return itemCatService.findItemCatCache(parentId);
	}
	
	
	
	
	
	
	
	
	
	
}
