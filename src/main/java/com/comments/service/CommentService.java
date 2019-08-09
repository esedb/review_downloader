package com.comments.service;
import java.util.List;

import com.comments.beans.CommentBean;

public interface CommentService {
	List<CommentBean> getComments(String url);
}
