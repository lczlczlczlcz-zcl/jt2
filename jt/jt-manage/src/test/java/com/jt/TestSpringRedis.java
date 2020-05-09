package com.jt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.Jedis;

@SpringBootTest	//启动spring容器 并且为测试方法提供支持.
public class TestSpringRedis {
	
	/**
	 * 需求: 需要从spring的容器中动态的获取管理的jedis对象.
	 */
	
	@Autowired
	private Jedis jedis;
	
	@Test
	public void testSet() {
		
		jedis.set("redis", "Spring容器管理redis成功!!!!");
		System.out.println(jedis.get("redis"));
	}
	
	
	
}
