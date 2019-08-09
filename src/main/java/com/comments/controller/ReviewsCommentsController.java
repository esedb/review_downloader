package com.comments.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comments.UtilityService;
import com.comments.Exception.CustomNotFoundException;
import com.comments.Exception.NoResultException;
import com.comments.beans.CommentBean;
import com.comments.beans.ReturnedFileBean;
import com.comments.beans.UserUrlBeans;
import com.comments.service.CommentsImp;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@RestController
@Validated
public class ReviewsCommentsController {
	@Autowired
	TaskExecutor taskexecutor;
	
		
	String file_path = UtilityService.filepath;
	
	
	@GetMapping("/download/{filename}")
	@ResponseBody
	public CompletableFuture<ResponseEntity<Resource>> getFile(@PathVariable("filename") String filename, HttpServletResponse response) {
		
		
		CompletableFuture<ResponseEntity<Resource>> cf =  CompletableFuture.supplyAsync(() -> {				
					      
			      File file = new File(file_path + "/" + filename);		
			      if(!file.exists()) {			    	  
			    	  throw new CustomNotFoundException("The file you requested for does not exist", null);			    	  
			      }
			      HttpHeaders headers = new HttpHeaders();
			      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			      response.setHeader("Content-disposition", "attachment; filename=" + filename);
			      InputStreamResource resource = null;
			      try{
			    	  resource = new InputStreamResource(new FileInputStream(file));
			      }
			      catch(IOException ex) {
			    	  ex.printStackTrace();
			      }
			      System.err.println("system ok");
			      return ResponseEntity.ok()
			              .headers(headers)
			              .contentLength(file.length())
			              .contentType(MediaType.parseMediaType("application/octet-stream"))
			              .body(resource);
			
			
			
		}, taskexecutor);
		
		
		return cf;
		
	 }
	
	@PostMapping("/reviews")
	@ResponseBody
	public CompletableFuture<ResponseEntity<ReturnedFileBean>> sumbitForm(@RequestBody @Valid UserUrlBeans userurlbeans, 
			HttpServletResponse response) {
				
		CompletableFuture<ResponseEntity<ReturnedFileBean>> cf =  CompletableFuture.supplyAsync(() -> {				
						
			      CommentsImp commentimp = new CommentsImp();
			      List<CommentBean> comments = commentimp.getComments(userurlbeans.getUrl());
			      if(comments == null) {
			    	  throw new NoResultException("There is comments or reviews associated with this URL", null);			    	  
			      }			      		      		      
			      	
			      Date date = new Date();
			      long millis = date.getTime();
			      File file = new File(file_path + "/" + "reviews" + millis +  ".csv");
			      
			      System.err.println("filepath: " + file.getAbsolutePath());
			      BufferedWriter writer = null;
			      try {
			    	  if(!file.exists()) {
				    	  file.createNewFile();				    	  
				      }
			    	  writer = new BufferedWriter(new FileWriter(file));
			    	  StatefulBeanToCsv<CommentBean> beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
				      beanToCsv.write(comments);
				      writer.close();
			    	  
			      }
			      catch(IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			    	  e.printStackTrace();			    	  
			      } 			     			      
			      
			      System.err.println("writtern to file");
			      ReturnedFileBean rfbean= new ReturnedFileBean(file.getName()); 
			      
			      return new ResponseEntity<ReturnedFileBean>(rfbean, HttpStatus.OK);			
			
			
		}, taskexecutor);
		
		
		return cf;
		
	 }
	

}
