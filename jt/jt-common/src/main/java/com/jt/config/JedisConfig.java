package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//标识配置类
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class JedisConfig {
	
	@Value("${redis.cluster}")
	private String nodes; //node,node
	
	@Bean
	public JedisCluster jedisCluster() {
		
		Set<HostAndPort> nodeSet = new HashSet<HostAndPort>();
		String[] nodeArray = nodes.split(",");
		for (String node : nodeArray) {	//node=host:port
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			HostAndPort hostAndPort = new HostAndPort(host, port);
			nodeSet.add(hostAndPort);
		}
		
		return new JedisCluster(nodeSet);
	}
	
}	
	
	
	
	/*@Value("${redis.sentinel}")
	private String sentinel;	//node,node,node
	
	@Bean
	public Jedis jedis() {
		Set<String> sentinels = new HashSet<String>();
		sentinels.add(sentinel);
		JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
		return  pool.getResource();//1主2从 1备份2次
		
	}*/
	
	
	
	/*//引入redis分片  
	@Value("${redis.nodes}")
	private String nodes;	//node,node,node
	
	//将ShardedJedis对象交给Spring容器管理
	@Bean
	public ShardedJedis shardedJedis() {
		List<JedisShardInfo> shards = new ArrayList<>();
		String[] nodeArray = nodes.split(",");
		for (String node : nodeArray) {	//node=HOST:PORT
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			JedisShardInfo info =  new JedisShardInfo(host, port);
			shards.add(info);
		}
		
		return new ShardedJedis(shards);
	}*/
	
	
	
	
	/*@Value("${redis.node}")
	private String node;		//ip:port
	
	//将方法的返回值交给spring容器管理 并且单例对象
	//将redis的数据信息,写入properties文件中,之后动态获取
	@Bean
	public Jedis jedis() {
		String host = node.split(":")[0];
		int port  = Integer.parseInt(node.split(":")[1]);
		return new Jedis(host, port);
	}*/

