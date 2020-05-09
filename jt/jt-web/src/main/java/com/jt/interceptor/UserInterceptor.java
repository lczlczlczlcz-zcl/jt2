package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;
//springMVC对外提供的拦截器的接口
@Component	//将该对象交给Spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	//UserThreadLocal threadLocal = new UserThreadLocal();
	/**
	 * 说明:如果用户没有登陆,应该重定向到用户的登陆页面.
	 * 	   如果用户已经登陆, 则拦截器放行.
	 * 如何确定用户已经登陆?
	 * 	  1.确定用户的cookie中是否有值
	 * 	  2.判断用户的ticket信息,则redis中是否有值.
	 * 
	 * boolean true 拦截器放行
	 * 		   false 拦截器拦截 一般都会配合重定向方式使用.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String ticket = null;
		//1.判断cookie中是否有记录.
		Cookie[] cookies = request.getCookies();
		if(cookies !=null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		
		//2.校验ticket信息是否有效. 查询redis之后进行判断
		if(!StringUtils.isEmpty(ticket)) {
			if(jedisCluster.exists(ticket)) {
				//校验当前ticket信息是否正确. 证明用户已经登陆.
				String userJSON = jedisCluster.get(ticket);
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				//利用Thread实现数据的共享
				//UserThreadLocal threadLocal = new UserThreadLocal();
				//threadLocal.set(user);
				
				//利用request域对象,保存数据.
				//request.getSession().setAttribute("JT_USER", user);
				request.setAttribute("JT_USER", user);
				//应该放行请求.
				return true;
			}
		}
		
		//http://www.jt.com/user/login.html
		response.sendRedirect("/user/login.html");
		return false;	//表示拦截
	}
	
}
