package com.comments.beans;

import com.opencsv.bean.CsvBindByName;

public class CommentBean {
	
	
	@CsvBindByName(column = "Names")
	String name;
	
	
	@CsvBindByName(column = "Dates")
	String date;
	
	
	@CsvBindByName(column = "Ratings")
	String rating;
	
	
	@CsvBindByName(column = "Comments")
	String comment;
	
	
	@CsvBindByName(column = "Links")
	String link;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}


}
