package com.comments;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class UtilityService {
	
		
	public static String filepath;
	
	public String cleanHTML(String html) {
		return Jsoup.parse(html).text();
	}
	String checkURLType(String url) {
		return null;
	}
	
	
}


