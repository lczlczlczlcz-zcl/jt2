package com.jt.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITree implements Serializable{

	private static final long serialVersionUID = -3374824151482249728L;
	private Long id;		//节点id信息
	private String text;	//节点名称
	private String state;	//节点状态  open/closed
}	
