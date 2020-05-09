package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//编辑商品分类信息
@TableName("tb_item_cat")
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemCat extends BasePojo{
	
	private static final long serialVersionUID = -8545814285854506950L;
	@TableId(type=IdType.AUTO)	//设置主键并且自增
	private Long id;			//商品分类id号
	private Long parentId;		//父级分类Id  
								//如果有父子级关系,可以使用parentId进行关联
	private String name;		//名称
	private Integer status;		//商品分类状态
	private Integer sortOrder;	//排序号
	private Boolean isParent;	//是否为父级 
								//tinyint 0 false /1 true
	  
}
