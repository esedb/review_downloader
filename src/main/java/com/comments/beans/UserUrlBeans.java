package com.comments.beans;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;

public class UserUrlBeans {
	
	@NotBlank(message = "You must provide a valid url")	
	@URL(message = "This is not a valid url")
	@Pattern(regexp = "(http|https)://(www.youtube|www.amazon|youtube|amazon).+", message = "This is not a valid youtube or amazon url")
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
