package com.jt.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{
	
	//默认分页的排序规则 Id由小到大的方式排列,后台商品维护系统
	//一定不会频繁维护之前旧商品, 应该按照更新时间进行降序排列
	@Select("SELECT * FROM tb_item ORDER BY updated DESC LIMIT #{start},#{rows}")
	List<Item> findItemByPage(@Param("start")Integer start, Integer rows);
	
	//@Delete("能否实现批量删除???  不可以")
	void deleteByIds(Long[] ids);
	
	
	/**
	 * 说明:Mybatis中的参数如果需要进行多值传参时,
	 * 需要将数据封装为map集合  
	 * 高版本可以自动完成
	 * @param ids
	 * @param status
	 * @param updated
	 */
	void updateStatus(@Param("ids")Long[] ids,@Param("status")Integer status,@Param("updated")Date updated);
	
}
