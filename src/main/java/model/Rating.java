package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

@Entity("bx_ratings")
public class Rating {
	
	@Id
	private ObjectId id;
	@Property
	private int userId;
	@Property
	private int rating;
	
	public Rating(){
		
	}
	
	public ObjectId getId() {
		return id;
	}

	public Rating(int userId, int rating, String isbn) {
		super();
		this.userId = userId;
		this.rating = rating;
		this.isbn = isbn;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	@Property
	private String isbn;
}
