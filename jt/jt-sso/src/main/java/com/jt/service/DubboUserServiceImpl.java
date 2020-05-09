package com.jt.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

//这是dubbo的实现类,使用dubbo的注解
@Service
public class DubboUserServiceImpl implements DubboUserService {
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;	//注入redis集群
	
	/**
	 * 实现用户注册. 用户注册时间/密码加密
	 */
	@Override
	public void saveUser(User user) {
		
		String md5Pass = DigestUtils.md5DigestAsHex
				(user.getPassword().getBytes());
		user.setPassword(md5Pass)
			.setEmail(user.getPhone())
			.setCreated(new Date())
			.setUpdated(user.getCreated());
		userMapper.insert(user);
	}
	
	/**
	 * 核心业务逻辑: 根据用户名和密码查询数据库.
	 * 如果数据中有记录,则保存redis,之后返回uuid
	 * 如果数据库中没有记录,则返回null.
	 */
	@Override
	public String findUserByUP(User user) {
		//1.需要将密码进行加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//2.根据对象中不为null的属性,充当where条件
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		//查询用户的全部记录信息.
		User userDB = userMapper.selectOne(queryWrapper);
		if(userDB ==null) {
			//3.如果userDB对象为null,则表示数据库中没有该记录.用户名密码错误.
			return null;
		}
		//4.说明:用户名和密码正确的
		String key = UUID.randomUUID().toString();
		//5.为了保护用户隐私信息,手机号 email
		userDB.setPassword("你猜猜!!!");  //userId/username
		String userJSON = ObjectMapperUtil.toJSON(userDB);
		jedisCluster.setex(key,7*24*3600, userJSON);
		return key;
	}
}
