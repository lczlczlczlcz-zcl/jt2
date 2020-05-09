package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMaper;
	
	/**
	 * 需要根据参数,查询数据库.
	 * type:1 username、2 phone、3 email
	 * 返回值:      true用户已存在，false用户不存在，
	 */
	@Override
	public Boolean checkUser(String param, Integer type) {
		//String column = type==1?"username":(type==2?"phone":"email");
		Map<Integer,String> map = new HashMap<Integer, String>();
		map.put(1, "username");
		map.put(2, "phone");
		map.put(3, "email");
		
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq(map.get(type), param);
		int count = userMaper.selectCount(queryWrapper);
		//返回值:true用户已存在，false用户不存在
		return count==0?false:true;
	}
	
}
