package com.jt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@RestController
public class FileController {
	
	
	/**
	 * 文件上传入门案例
	 * url:/file   type="post"
	 * 参数:fileImage  文件信息
	 * 返回值: String 提示上传完成.
	 * 
	 * 提示:如果需要使用文件上传,则一般使用InputStream进行参数的接收
	 * 后续:采用输出流实现文件的输出
	 * 
	 * 优化:MultipartFile 封装了输入流/输出流
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * 
	 * 1M的数据
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//1.动态获取文件名称  abc.jpg
		String fileName = fileImage.getOriginalFilename();
		//2.准备文件目录
		String dirPath = "E:/JT_IMAGE/";
		//3.准备文件对象,之后拼接文件的全路径
		File file = new File(dirPath+fileName);
		//4.实现文件上传
		fileImage.transferTo(file);
		return "文件上传成功!!!";
	}
	
	
	@Autowired
	private FileService fileService;
	/**
	 * 需求:完成商品图片上传
	 * url:	http://localhost:8091/pic/upload?dir=image
	 * 参数: uploadFile
	 * 返回值: ImageVO对象
	 */
	@RequestMapping("/pic/upload")
	public ImageVO upload(MultipartFile uploadFile) {
		
		return fileService.upload(uploadFile);
	}
	
	
	
	
	
	
	
}
