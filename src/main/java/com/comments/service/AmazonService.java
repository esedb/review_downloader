package com.comments.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.comments.beans.CommentBean;

public class AmazonService implements CommentService {	
	
	/* This is the main worker of the service it
	 * the reviews... it use the Jsoup library for 
	 * URL Connecitons.
	 */
	public List<CommentBean> process(String user_url) {
		List<CommentBean> comments = new ArrayList<>();
		
		String[] url_token = user_url.split("/dp/|/ref");
		if(url_token.length < 2) {
			return null;
		}
		String main_url = url_token[0];
		String product_id = url_token[1];
		
		
		int j = 1;
		boolean flag = true;
		do {
			try {
				String m_url =main_url + "/product-reviews/" + product_id + "/ref=cm_cr_dp_d_show_all_btm?ie=UTF8&reviewerType=all_reviews&pageNumber=";
				String url = "";
				if(j > -1) {
					url = m_url + j + "&sortBy=recent";					
				}
				System.out.println("url: " + url);
				
				Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windsows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
						.ignoreContentType(true)
						.timeout(6000)
						.followRedirects(true).get();
												
				Object[] commentlist = doc.getElementsByAttributeValueContaining("data-hook", "review-body").toArray();			
				Object[] review_date = doc.getElementsByAttributeValueContaining("data-hook", "review-date").toArray();
				Object[] review_rating = doc.getElementsByAttributeValueContaining("data-hook", "review-star-rating").toArray();
				Object[] profile_name = doc.getElementsByAttributeValueContaining("class", "a-profile-content").toArray();
				
				int a = 0;
				//Added because the profile name always has 2 repeated profile name
				//for top positive review and top critical review
				if(profile_name.length > commentlist.length) {
					a = profile_name.length - commentlist.length;					
				}
				
				int size = commentlist.length;
				if(size > 0) {
					j++;
				}
				else {
					flag = false;
				}
								
				for(int i = 0; i<size; i++) {
					Element comment = (Element)commentlist[i];
					Element date = (Element) review_date[i];
					Element review = (Element) review_rating[i];
					Element name = (Element) profile_name[i+a];
					
										
					 CommentBean cb = new CommentBean();
					 cb.setName(name.getElementsByTag("span").text());
					 cb.setComment(comment.getElementsByTag("span").first().text());
					 cb.setDate(date.getElementsByTag("span").text());
					 cb.setRating(review.getElementsByTag("span").text());
					 cb.setLink(url);
					 comments.add(cb);
					 
					
				}
//				if(comments.size() == 500) {					
//					flag = false;
//					System.out.println("reviews has reached it maximum of 500!");					
//				}

			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}while(flag);
		
		System.out.println("task completed!");	
	
		return comments;
		
	}

	@Override
	public List<CommentBean> getComments(String url) {		
		return process(url);		
	}

}
