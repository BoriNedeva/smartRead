package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

@Entity("bx_books")
public class Book {

	@Id
	private ObjectId id;
	@Property
	private String title;
	@Property
	private String author;
	@Property
	private String isbn;
	@Property
	private String publisher;
	@Property
	private String image_url_m;
	@Property
	private int year;
//	@Property
//	private String genre;
	
	public Book() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getImage_url_m() {
		return image_url_m;
	}

	public void setImage_url_m(String image_url_m) {
		this.image_url_m = image_url_m;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
