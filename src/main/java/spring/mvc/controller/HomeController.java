package spring.mvc.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/download1",method = RequestMethod.GET)
	public void download1(HttpServletResponse response) {
		try {
			//File file=(ResourceUtils.getFile("classpath:file/demo.txt"));
			File file=new ClassPathResource("file/demo.txt").getFile();
			byte[] data=FileUtils.readFileToByteArray(file);
			
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename="+file.getName());
			response.setContentLength(data.length);
			
			InputStream inputStream=new BufferedInputStream(new ByteArrayInputStream(data));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/download2",method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> download2(){
		HttpHeaders headers=new HttpHeaders();
		
		try {
			File file=resourceLoader.getResource("classpath:file/demo.txt").getFile();
			byte[] data=FileUtils.readFileToByteArray(file);
			
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.set("content-disposition", "attachment; filename="+file.getName());
			headers.setContentLength(data.length);
			InputStream inputStream=new BufferedInputStream(new ByteArrayInputStream(data));
			InputStreamResource inputStreamResource=new InputStreamResource(inputStream);
			
			return new ResponseEntity<InputStreamResource>(inputStreamResource,headers,HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<InputStreamResource>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
