package com.jt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;
//@SpringBootTest	//启动spring容器
public class TestRedis {
	
	
	/**
	 * redis测试案例
	 * 1.关闭Linux的防火墙
	 * 2.检查redis的配置文件  IP绑定注释,保护模式关闭
	 * 3.重启redis redis-server redis.conf
	 */
	@Test
	public void test01() {
		String host = "192.168.126.166";	//redis的主机地址
		int port = 6379;
		Jedis jedis = new Jedis(host, port);
		jedis.set("redis", "您好Redis");
		System.out.println(jedis.get("redis"));
		
	}
	
	
	private Jedis jedis;
	
	//初始化Jedis对象
	@BeforeEach	//当@Test注解方法执行前,执行该方法.
	public void init() {
		String host = "192.168.126.166";	//redis的主机地址
		int port = 6379;
		jedis = new Jedis(host, port);
	}
	
	/**
	 * 需求: 如果redis中已经存在key则不允许做修改操作
	 */
	@Test
	public void testExists() {
		String key = "game";
		if(!jedis.exists(key)) {	//key不存在时赋值
			
			jedis.set(key,"英雄联盟");
		}
		System.out.println(jedis.get(key));
	}
	
	/**
	 * setnx 只有当key不存在时才能赋值成功.
	 */
	@Test
	public void testNX() {
		jedis.flushAll();	//清空redis数据.
		jedis.setnx("game", "王者农药");
		jedis.setnx("game", "人体描边大师");
		System.out.println(jedis.get("game"));
	}
	
	/**
	 * 需求:redis添加记录,同时添加超时时间,并且展现剩余存活时间
	 * @throws InterruptedException 
	 * 问题说明: 不能保证操作的原子性,将多条命令放到一起执行.
	 */
	@Test
	public void testExpire() throws InterruptedException {
		
		jedis.set("a", "123456");
		//机房断电 运行时异常 等
		int a = 1/0;
		jedis.expire("a", 60);
		Thread.sleep(3000);
		Long seconds = jedis.ttl("a"); //>0 表示有超时时间  -1 表示永不超时  -2 没有该数据.
		System.out.println("当前剩余存活时间:"+seconds);
	}
	
	//保证入库操作和添加超时时间的原子性
	@Test
	public void testSetEx() throws InterruptedException {
		jedis.setex("b", 10, "测试超时问题");//原子性操作
		Thread.sleep(3000);
		System.out.println("剩余存活时间:"+jedis.ttl("b"));
	}
	
	/**
	 * 需求: 只有key不存在时才能赋值,同时要求添加超时时间,保证原子性
	 * 参数:   NX:只有当key不存在时,才能赋值
	 * 		  XX:只有key存在时,才能赋值
	 * 		  EX:秒
	 * 		  PX:毫秒
	 */
	@Test
	public void testSetNXEX() {
		SetParams setParams = new SetParams();
		setParams.xx().ex(10);
		String result = jedis.set("c", "测试方法!!!", setParams);
		System.out.println("结果:"+result);
	}
	
	//item商品信息.保存到redis中
	@Test
	public void testHash() {
		jedis.hset("user", "id", "101");
		jedis.hset("user", "name", "名称");
		jedis.hset("user", "age", "18");
		
		Map<String,String> map = jedis.hgetAll("user");
		System.out.println(map);
	}
	
		//item商品信息.保存到redis中
		@Test
		public void testList() {
			
			jedis.lpush("list", "1","2","3","4","5");
			System.out.println(jedis.rpop("list"));
			//1  12345
		}
		
		//redis控制事物
		@Test
		public void testMulti() {
			Transaction transaction = jedis.multi();	//开始事物
			try {
				transaction.set("ww", "www");
				transaction.set("ww", "www");
				transaction.set("ww", "www");
				transaction.exec();			//事物提交
			} catch (Exception e) {
				e.printStackTrace();
				transaction.discard();		//事物回滚
			}
			
		}
	
		
		@Test
		public void testShards() {
			List<JedisShardInfo> shards = new ArrayList<>();
			shards.add(new JedisShardInfo("192.168.126.166", 6379));
			shards.add(new JedisShardInfo("192.168.126.166", 6380));
			shards.add(new JedisShardInfo("192.168.126.166", 6381));
			ShardedJedis jedis = new ShardedJedis(shards);
			jedis.set("shards", "测试redis分片机制!!!!");
			System.out.println(jedis.get("shards"));
		}
		
		
		/**
		 * 测试哨兵机制
		 * masterName:当前主机的变量名称
		 */
		@Test
		public void testSentinel() {
			Set<String> sentinels = new HashSet<String>();
			sentinels.add("192.168.126.166:26379");
			JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
			Jedis jedis = pool.getResource();//1主2从 1备份2次
			jedis.set("sentinel", "AAAAA");
			System.out.println(jedis.get("sentinel"));
		}
		
	
		@Test
		public void testCluster() {
			Set<HostAndPort> nodes = new HashSet<HostAndPort>();
			nodes.add(new HostAndPort("192.168.126.166",7000));
			nodes.add(new HostAndPort("192.168.126.166",7001));
			nodes.add(new HostAndPort("192.168.126.166",7002));
			nodes.add(new HostAndPort("192.168.126.166",7003));
			nodes.add(new HostAndPort("192.168.126.166",7004));
			nodes.add(new HostAndPort("192.168.126.166",7005));
			JedisCluster jedisCluster = new JedisCluster(nodes);
			jedisCluster.set("redisCluster","集群测试");
			System.out.println(jedisCluster.get("redisCluster"));
		}
		
		
		
		
	
}	
