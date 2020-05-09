package com.jt.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.annotation.CacheFind;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

@Component	//将对象交给Spring容器管理
@Aspect		//标识切面类
@Slf4j		//引入日志注解
public class RedisAOP {
	
	/**
	 * 案例要求:
	 * 	1.拦截ItemCatServiceImpl的对象
	 */
	//1.配置切入点表达式
	@Pointcut("bean(itemCatServiceImpl)") 
	public void pointCut() {
		
	}
	
	//定义前置通知  反射包的接口对象     
	//joinPoint Spring为方法提供了一个工具API接口(反射原理)
	//ProceedingJoinPoint is only supported for around advice
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		System.out.println("我是前置通知");
		Object target = joinPoint.getTarget();	//获取目标对象
		Object[] objs = joinPoint.getArgs();	//动态获取参数
		String methodName = joinPoint.getSignature().getName();		//获取目标方法的名称
		System.out.println(target.getClass());
		System.out.println(objs);
		System.out.println(methodName);
	}
	
	//service中的全部方法 记录方法的执行时间
	@Around("execution(* com.jt.service..*.*(..))")
	public Object timeAround(ProceedingJoinPoint joinPoint) {
		Long startTime = System.currentTimeMillis();
		try {
			//动态获取的类名
			String className = 
					joinPoint.getSignature().getDeclaringTypeName();
			//获取方法名称
			String methodName = 
					joinPoint.getSignature().getName();
			Object obj = joinPoint.proceed();
			Long endTime = System.currentTimeMillis();
			//System.out.println(className+"."+methodName+"方法的执行时间:"+(endTime-startTime)+"毫秒");
			log.info("{方法"+className+"."+methodName+"的执行时间:"+(endTime-startTime)+"毫秒}");
			return obj;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}	
	}
	
	
	@Autowired(required=false)
	private JedisCluster jedis;	//集群配置
	//private Jedis jedis;			//单台redis节点
	//private ShardedJedis jedis;	//引入Redis分片对象
	//private Jedis jedis;			//从哨兵中动态获取的Jedis对象
	
	
	/**
	 * 业务: 如果方法中添加了@CacheFind注解,则该注解标识的方法,实现缓存的
	 * 业务逻辑.
	 * 
	 * 切入点表达式:拦截注解
	 * 通知选择: 控制目标方法是否执行!!!!   环绕通知!! 
	 * 参数必须添加:ProceedingJoinPoint	  乌龟的屁股=规定
	 */
	//@Around("@annotation(com.jt.annotation.CacheFind)")
	//可以直接将注解添加到参数中.参数只能位于第二位
	@Around("@annotation(cacheFind)")
	public Object cacheAround(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		//1.动态的获取key
		String key = getKey(joinPoint,cacheFind);
		
		//2.引入缓存,实现业务操作
		String value = jedis.get(key);
		//标识返回值对象
		Object obj = null;
		if(StringUtils.isEmpty(value)) {
			//缓存中没有数据,需要查询数据库.
			try {
				obj = joinPoint.proceed(); //执行目标方法
				//将返回值结果 利用redis进行保存
				String json = ObjectMapperUtil.toJSON(obj);
				//根据用户自己设定的超时时间,实现赋值操作
				if(cacheFind.seconds()>0) {
					int seconds = cacheFind.seconds(); //获取用户超时时间
					jedis.setex(key,seconds,json);
				}else {
					jedis.set(key, json);
				}
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
			System.out.println("AOP执行数据库查询!!!!");
		}else {
			//标识缓存中有记录.可以利用缓存直接返回. //利用反射机制动态获取返回值
			Class<?>  targetClass = getReturnType(joinPoint);
			obj = ObjectMapperUtil.toObject(value, targetClass);
			//LinkedHashMap可以自动的转化为对象.只要属性相同即可.
			System.out.println("AOP查询缓存!!!!!!");
		}
		return obj;
	}

	//记住. 动态获取方法的返回值类型
	private Class<?> getReturnType(ProceedingJoinPoint joinPoint) {
		//强制类型转化
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}

	//如果用户自己指定了key
	private String getKey(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
		
		if(!StringUtils.isEmpty(cacheFind.key())) {
			
			return cacheFind.key();
		}
		
		//key应该自动生成
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		//动态获取第一个参数  简化操作
		Object arg0 = joinPoint.getArgs()[0];
		return className+"."+methodName+"::"+arg0;
	}
}
