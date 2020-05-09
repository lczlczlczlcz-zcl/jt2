package com.jt.quartz;


import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;


//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{
	
	//修改数据库的超时订单的
	@Autowired
	private OrderMapper orderMapper;
	
	/**
	 * 条件:30分钟超时    1改为6
	 *判断依据:    创建订单的时间    now-created>30分钟
	 *		    created<now-30
	 *sql: update tb_order set status=6,updated=#{date}
	 *		where created <#{timeOut} and status=1;
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//java中专门操作时间的api
		Calendar calendar = Calendar.getInstance(); //获取当前时间
		//field操作的时间属性 分钟 小时  年 月等
		calendar.add(Calendar.MINUTE, -30);
		//获取计算之后的时间
		Date timeOut = calendar.getTime();
		Order order = new Order();
		order.setStatus(6).setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", 1)
					  .lt("created", timeOut);
		orderMapper.update(order, updateWrapper);
		System.out.println("定时任务完成!!!!!");
	}
	
	
	
	
	
	
	
}
