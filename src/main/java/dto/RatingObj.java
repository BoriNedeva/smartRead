package dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RatingObj {
	
	private String username;
	private String isbn;
	private int rating;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}
