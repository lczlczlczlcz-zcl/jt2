package com.jt.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jt.util.HttpClientService;
@SpringBootTest
public class TestHttpClient {
	
	/**
	 * 1.实例化httpClient对象
	 * 2.确定请求url地址
	 * 3.定义请求方式类型  get post put delete
	 * 4.利用api发起http请求,获取响应结果
	 * 5.判断返回值是否正确   校验状态码.
	 * 6.如果返回值状态码正确的(200),则动态获取返回值信息
	 */
	@Test
	public void test01() {
		//每次使用,直接创建一个新的链接
		HttpClient httpClient = HttpClients.createDefault();
		String url = "https://www.baidu.com";
		HttpGet httpGet = new HttpGet(url);
		try {
			//发起请求,获取响应  200
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int code = httpResponse.getStatusLine().getStatusCode();
			String reason = httpResponse.getStatusLine().getReasonPhrase();
			System.out.println("获取返回值信息:"+code+":"+reason);
			
			if(code==200) {
				//表示用户的请求正确 获取响应的实体
				HttpEntity httpEntity = httpResponse.getEntity();
				//将实体信息.转化为String类型,进行展现,防止乱码 
				String result = EntityUtils.toString(httpEntity,"utf-8");
				System.out.println(result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//利用Spring容器,检查对象是否可用.
	@Autowired
	private HttpClientService httpClientService;
	
	@Test
	public void testGet() {
		String url = "http://www.abc.com";
		Map<String,String> params = new HashMap<String, String>();
		params.put("id","100");
		params.put("name","httpClient测试");
		params.put("sex","男");
		String result = httpClientService.doGet(url, params, null);
	}
}
