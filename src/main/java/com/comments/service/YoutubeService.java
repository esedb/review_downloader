package com.comments.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.comments.UtilityService;
import com.comments.beans.CommentBean;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;

public class YoutubeService implements CommentService {
	
	UtilityService utilities = new UtilityService();
	
	String DEVELOPER_KEY = CustomKeyStore.getDeveloperkeys();

    private static final String APPLICATION_NAME = "commentProject";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
       
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }
    
    
    public String getVideoId(String url) {
    	String video_id[] =  url.split("v=");
    	if(video_id.length < 2) {
    		return null;
    		
    	}
    	return video_id[1];
    }
    
    /* It perform the major computing to get youtube comments
     * it uses the youtube library
     * the returned comments size have a maximum size of 500
     */
    public List<CommentBean> process(String url) {
    	   	
    	List<CommentBean> comments = new ArrayList<>();
    	
    	try {
        
	        YouTube youtubeService = getService();
	        YouTube.CommentThreads.List request = youtubeService.commentThreads().list("snippet,replies");
	        String video_id = getVideoId(url);
	        if(video_id == null) {
	        	return null;
	        }
	        CommentThreadListResponse response = request.setKey(DEVELOPER_KEY).setVideoId(video_id).setOrder("time").execute();           
	      
	        String nextpage = response.getNextPageToken();
	        	        	        
	        for(CommentThread t: response.getItems()) {
	        	CommentBean cb = new CommentBean();
	        	String name =  t.getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName();
	        	String comment = utilities.cleanHTML(t.getSnippet().getTopLevelComment().getSnippet().getTextDisplay());  
	        	String date = t.getSnippet().getTopLevelComment().getSnippet().getUpdatedAt().toString();  
	        	Long rating = t.getSnippet().getTopLevelComment().getSnippet().getLikeCount();
	        	cb.setName(name);
	        	cb.setComment(comment);
	        	cb.setDate(date);
	        	cb.setRating(rating.toString());
	        	comments.add(cb);    	
	        	      	
	        	        	
	        }
	        while(nextpage != null) {
	        	request = youtubeService.commentThreads().list("snippet,replies");
	        	response = request.setKey(DEVELOPER_KEY).setVideoId(getVideoId(url)).setPageToken(nextpage).execute();
	        	
	        	nextpage = response.getNextPageToken();
	        	 for(CommentThread t: response.getItems()) {
	        		CommentBean cb = new CommentBean();
	        		String name  = t.getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName();
	             	String comment = utilities.cleanHTML(t.getSnippet().getTopLevelComment().getSnippet().getTextDisplay());  
	             	String date = t.getSnippet().getTopLevelComment().getSnippet().getUpdatedAt().toString();
	             	String rating = String.valueOf(t.getSnippet().getTopLevelComment().getSnippet().getLikeCount()); 
	             	cb.setName(name);
	            	cb.setComment(comment);
	            	cb.setDate(date);
	            	cb.setRating(rating.toString());
	            	System.out.println("comments: " + comment);
	            	comments.add(cb);
	            	
//	            	if(comments.size() == 500) {
//	            		nextpage = null;
//	            	}
	            		             	
	             }
	        	
	        	
	        }
    	}
    	catch(Exception e) {    		
    		e.printStackTrace();
    		return null;
    	}
    	System.out.println("task completed!");
   
    	return comments;
    	
    }


	@Override
	public List<CommentBean> getComments(String url) {		
		return process(url);
	}

}
