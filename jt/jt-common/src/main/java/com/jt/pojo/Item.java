package com.jt.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown=true) //表示JSON转化时忽略未知属性
@TableName("tb_item")
@Data	
@Accessors(chain=true)
//子类继承父类,父类实现了序列号接口,所以子类也实现了序列号接口
public class Item extends BasePojo{
	private static final long serialVersionUID = -113421486821062779L;
	@TableId(type=IdType.AUTO)
	private Long id;				//商品id
	private String title;			//商品标题
	private String sellPoint;		//商品卖点信息
	private Long   price;			//商品价格 Long 速度 > double 精度问题
	private Integer num;			//商品数量
	private String barcode;			//条形码
	private String image;			//商品图片信息   图片地址1.jpg,2.jpg,3.jpg
	private Long   cid;				//表示商品的分类id
	private Integer status;			//1正常，2下架
	
	//为了满足页面调用需求,添加get方法
	public String[] getImages(){
		
		return image.split(",");
	}
}
