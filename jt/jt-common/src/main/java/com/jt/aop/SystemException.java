package com.jt.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import net.sf.jsqlparser.statement.execute.Execute.EXEC_TYPE;

//标识全局异常的处理
@RestControllerAdvice	
//advice表示一个通知   Controller的通知方法.  
//当程序在controller中执行有异常时则会执行通知的具体方法
public class SystemException {
	/**
	 * aop:面向切面编程
	 *  公式:  切面 = 切入点 + 通知方法
	 *  切入点: if判断 当满足切入点时,才会执行通知方法.
	 *  通知方法: 完成的扩展的业务.
	 */
	
	//全局异常处理的写法:当程序出错之后需要返回什么?
	//当出现某种异常时,会执行该通知方法.
	@ExceptionHandler(RuntimeException.class)  //??
	//@ResponseBody	//返回json信息
	public SysResult sysResult(Exception exception) {
		exception.printStackTrace();	//输出异常信息.
		return SysResult.fail();
	}
}
