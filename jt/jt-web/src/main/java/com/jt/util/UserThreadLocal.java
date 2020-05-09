package com.jt.util;

import com.jt.pojo.User;

public class UserThreadLocal {
	
	//作用:在线程内实现数据共享.  一般在
	private static ThreadLocal<User> thread = new ThreadLocal<>();
	
	//存数据
	public static void set(User user) {
		
		thread.set(user);
	}
	
	//取数据
	public  static User get() {
		
		return thread.get();
	}
	
	//清空数据
	public static void remove() {
		
		thread.remove();
	}
}
