package com.jt.config;


import javax.annotation.PreDestroy;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 1.线程知识   创建 启动 线程等待  线程唤醒
 * 2.同步代码块 防止线程并发安全性问题
 * 3.Spring创建对象的执行过程.
 * 4.Spring容器关闭的执行操作.
 * 5.volatile实际应用.
 * @author LYJ
 *
 */
@Component	//交给spring容器管理   start方法
public class HttpClientClose extends Thread{
	@Autowired
	private PoolingHttpClientConnectionManager manage;
	private volatile boolean shutdown;	//开关 volatitle表示多线程可变数据,一个线程修改,其他线程立即修改
	
	public HttpClientClose() {
		//当spring容器启动时,会创建所有管理的对象,如果没有特殊的要求
		//则调用构造方法.
		//线程开启启动
		this.start();
	}
	
	//重写线程的run方法.  关闭超时链接
	@Override
	public void run() {
		try {
			//如果服务没有关闭,执行线程
			while(!shutdown) {
				synchronized (this) {
					wait(5000);			//等待5秒
					//System.out.println("线程开始执行,关闭超时链接");
					//关闭超时的链接
					PoolStats stats = manage.getTotalStats();
					int av = stats.getAvailable();	//获取可用的线程数量
					int pend = stats.getPending();	//获取阻塞的线程数量
					int lea = stats.getLeased();    //获取当前正在使用的链接数量
					int max = stats.getMax();
					//System.out.println("max/"+max+":	av/"+av+":  pend/"+pend+":   lea/"+lea);
					//通过链接池,关闭超时链接.
					manage.closeExpiredConnections();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		super.run();
	}

	// 当spring容器关时,在关闭之前首先执行该操作.
	@PreDestroy
	public void shutdown() {  //关闭当前线程.
		shutdown = true;	  //表示关闭线程
		synchronized (this) {
			//System.out.println("关闭全部链接!!");
			notifyAll(); //全部从等待中唤醒.执行关闭操作;
		}
	}
}
