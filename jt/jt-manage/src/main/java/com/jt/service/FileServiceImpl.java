package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;
import com.jt.vo.SysResult;

@Service
//指定配置文件进行加载
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	
	@Value("${image.localDir}")
	private String localDir; //= "E:/JT_IMAGE/"; //定义根目录
	@Value("${image.urlPath}")
	private String urlPath;  // = "http://image.jt.com/";
	
	/**
	 * 文件上传的问题:
	 * 	1.校验是否为图片      a.jpg
	 * 	2.防止用户上传恶意程序  木马.exe.jpg
	 * 	3.分目录存储
	 * 	4.防止重名现象	
	 * 
	 * 解决方案:
	 * 	1.利用图片的后缀完成校验	
	 * 	2.利用图片的固有属性 宽度/高度判断是否为图片	
	 * 	3.可以根据时间进行目录的划分
	 * 	4.利用UUID的方式动态生成文件名称.
	 * 
	 * 正则语法:
	 * 	 1.^  $
	 * 	 2. * >=0,  + >=1 ,  ? 0/1
	 * 	 3. {n} 固定次数 ,{n,} 至少为n次,  {1,3} 至少1次,最多3次
	 * 	 4.  .(点)   除了回车换行之外的任意单个字符
	 * 	 5. [a-z]   匹配a-z的任意的单个字母
	 * 	 6. (png|jpg) 至少匹配一个,要么为png或者为jpg
	 */
	@Override
	public ImageVO upload(MultipartFile uploadFile) {
		
		//1.获取图片名称	a.jpg
		String fileName = uploadFile.getOriginalFilename();
		//2.将图片名称都转化为小写字母
		fileName = fileName.toLowerCase();
		//3.如果判断字符串是否满足某种要求,则正则首选.
		if(!fileName.matches("^.+\\.(png|jpg|gif)$")) {
			
			return ImageVO.fail();
		}
		
		//4.判断是否为恶意程序 通过宽度和高度进行判断
		try {
			BufferedImage bufferedImage = 
					ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			
			if(width ==0 || height ==0) {
				//说明 上传的不是图片,为恶意程序.
				return ImageVO.fail();
			}
			
			//5.如果程序执行到这里,则表示用户上传的确实是图片
			//按照时间将目录进行划分 yyyy/MM/dd
			String deteDir = new SimpleDateFormat("yyyy/MM/dd/")
							 .format(new Date());
			
			//6.准备文件目录  E:/JT_IMAGE/yyyy/MM/dd/
			String localFileDir = localDir + deteDir;
			File fileDir = new File(localFileDir);
			if(!fileDir.exists()) {	//如果当前的目录不存在,应该创建目录
				//E:image/a/b/c
				//fileDir.mkdir();	//创建一级目录
				fileDir.mkdirs();	//创建多级目录
			}
			
			//7.动态生成文件名称 uuid.jpg
			String uuid = UUID.randomUUID().toString().replace("-", "");
			//abc.jpg
			int index = fileName.lastIndexOf(".");
			String fileType = fileName.substring(index);
			String realFileName = uuid + fileType;
			
			//8.实现真实的文件上传 磁盘路径+文件名称
			String realFilePath = localFileDir + realFileName;
			File imageFile = new File(realFilePath);
			uploadFile.transferTo(imageFile);
			
			//String url = "https://img14.360buyimg.com/n0/jfs/t1/69718/36/16026/36295/5dd5e14aE4c40b328/943a59f664593e16.jpg";
			//域名+时间+文件名称	
			String url = urlPath+deteDir+realFileName;
			return ImageVO.success(url, width, height);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return ImageVO.fail();	//图片上传失败
		}
	}

}
