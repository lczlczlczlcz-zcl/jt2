package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName("tb_item_desc")
@Data		//get/set/toString/equals等方法
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true) //JSON转化时忽略未知属性
public class ItemDesc extends BasePojo{
	@TableId	  //没有自增,保证数据的一致性
	private Long itemId;		//主键信息
	private String itemDesc;	//商品详情
	
	public String getVIP() {
		
		return "我是测试get方法是否调用!!!";
	}
	
	
}
