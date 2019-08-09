//package com.comments.controller;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.task.AsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.async.DeferredResult;
//
//import com.comments.beans.CommentBean;
//import com.comments.beans.UserUrlBeans;
//import com.comments.service.CommentsImp;
//import com.opencsv.bean.StatefulBeanToCsv;
//import com.opencsv.bean.StatefulBeanToCsvBuilder;
//import java.io.Writer;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@Controller
//public class CommentsController {	
//	
//	TaskExecutor taskexecutor;
//	public CommentsController(AsyncTaskExecutor taskexecutor) {
//		this.taskexecutor = taskexecutor;
//	}
//	
//	@RequestMapping(value = {"/", "/home"}, method = {RequestMethod.GET, RequestMethod.POST}) 
//	public String home(Model model) { 
//	    model.addAttribute("userurlbeans", new UserUrlBeans()); 
//	    return "index"; 
//	}
//		
//	@PostMapping("/comments")
//	public String getComments(@ModelAttribute("userurlbeans") @Valid UserUrlBeans userurlbeans, BindingResult binding, HttpServletResponse response) {		
//		if(binding.hasErrors()) {
//			return "index";
//		}
//		try {
//			  
//		      CommentsImp commentimp = new CommentsImp();
//		      List<CommentBean> comments = commentimp.getComments(userurlbeans.getUrl());
//		      if(comments == null) {
//		    	  FieldError fr = new FieldError("userurlbeans", "url", "There is no customer comments/review associated with this url");
//		    	  binding.addError(fr);
//		    	  return "index";
//		      }
//		      response.setContentType("text/csv");
//		      response.setHeader("Content-disposition", "attachment; filename=reviews.csv");
//		      Writer writer = response.getWriter();
//		      StatefulBeanToCsv<CommentBean> beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
//		      beanToCsv.write(comments);
//		      writer.close();
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//				
//		return "index";
//	}
//	
//
//	
//	
//}
