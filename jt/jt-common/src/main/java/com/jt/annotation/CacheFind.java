package com.jt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解的作用目标  方法/类/属性
@Target(ElementType.METHOD)	//元注解  修饰注解的注解
@Retention(RetentionPolicy.RUNTIME)	//运行期有效
public @interface CacheFind {  		//注解中方法即属性
	
	//1.key可以自动生成, 同时也可以由用户指定.
	String key() default "";
	int seconds() default 0;	//0用户不需要设定超时时间
	
}
