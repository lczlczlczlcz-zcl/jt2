package com.jt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
/**
 * 工具API作用 简化用户远程调用的方式
 * 用户指定访问的url地址,则可以利用工具API动态获取响应数据。
 * @author LYJ
 *
 */
@Service
public class HttpClientService {
	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private RequestConfig requestConfig;

	
	/**
	 * 工具方法编辑思路:
	 * 	参数问题
	 * 		1.url地址
	 * 		2.准备一个map集合  kye=value 都由用户自己输入
	 * 		3.字符编码问题  指定字符集编码
	 *     返回值:向后台服务器发起请求之后,动态获取的结果.
	 * 	
	 * 	请求类型:get请求
	 * 			没有参数: http://manage.jt.com/findItemById
	 * 			有参数:     http://manage.jt.com/findItemById?id=1&name=xx;
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public String doGet(String url,Map<String,String> params,String charset) {
		
		//1.判断用户是否传递字符集编码
		if(StringUtils.isEmpty(charset)) {
			charset = "UTF-8";
		}
		
		//2.判断用户是否传递参数
		if(params !=null) {
			
			url += "?";
			for (Map.Entry<String,String> entry : params.entrySet()) {
				String key = entry.getKey().trim();
				String value = entry.getValue().trim();
				url += key + "=" + value +"&";
			}
			//去除多余的&符.
			url = url.substring(0, url.length()-1);
		}
		
		//3.根据url请求,发起访问.
		HttpGet httpGet = new HttpGet(url);
		//添加请求的超时时间
		httpGet.setConfig(requestConfig);
		
		//4.发起请求
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			//5.校验请求的状态信息是否正确.
			int status = httpResponse.getStatusLine().getStatusCode();
			if(status==200) {
				//表示返回值正确
				HttpEntity httpEntity = httpResponse.getEntity();
				//添加字符集编码
				String result = EntityUtils.toString(httpEntity,charset);
				//思考:是否可能出现请求没有结果的时候,是否允许该现象发生
				//电商项目中可以允许为null,例子,用户没有添加购物车时 查询时为空
				return result;
			}
			String message = "{请求地址:"+url +"状态码信息:"+status+"}";
			throw new RuntimeException(message);
		} catch (IOException e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public String doGet(String url) {
		
		return doGet(url, null, null);
	}
	
	public String doGet(String url,Map<String, String> params) {
		
		return doGet(url, params, null);
	}

	
	//实现httpClient POST提交
	public String doPost(String url,Map<String,String> params,String charset){
		String result = null;

		//1.定义请求类型
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);  	//定义超时时间

		//2.判断字符集是否为null
		if(StringUtils.isEmpty(charset)){

			charset = "UTF-8";
		}

		//3.判断用户是否传递参数
		if(params !=null){
			//3.2准备List集合信息
			List<NameValuePair> parameters = new ArrayList<>();

			//3.3将数据封装到List集合中
			for (Map.Entry<String,String> entry : params.entrySet()) {

				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			//3.1模拟表单提交
			try {
				UrlEncodedFormEntity formEntity = 
						new UrlEncodedFormEntity(parameters,charset); //采用u8编码

				//3.4将实体对象封装到请求对象中
				post.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
		}

		//4.发送请求
		try {
			CloseableHttpResponse response = 
					httpClient.execute(post);

			//4.1判断返回值状态
			if(response.getStatusLine().getStatusCode() == 200) {

				//4.2表示请求成功
				result = EntityUtils.toString(response.getEntity(),charset);
			}else{
				System.out.println("获取状态码信息:"+response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}



	public String doPost(String url){

		return doPost(url, null, null);
	}

	public String doPost(String url,Map<String,String> params){

		return doPost(url, params, null);
	}

	public String doPost(String url,String charset){

		return doPost(url, null, charset);
	}
}
