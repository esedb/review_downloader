package com.comments.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.comments.beans.CommentBean;

@Service
public class CommentsImp implements CommentService{	

	public List<CommentBean> getComments(String url) {
		if(url.contains("amazon")) {			
			CommentService as = new AmazonService();
			return as.getComments(url);
		}else if(url.contains("youtube")) {
			CommentService ys = new YoutubeService();
			return ys.getComments(url);
		}		
		return null;
	}

}
