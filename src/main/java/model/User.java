package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

@Entity("user")
public class User {

	@Id
	private ObjectId id;
	@Property
	private String username;
	@Property
	private String password;
	@Property
	private String email;
	@Reference
	private List<Book> favourite;
	@Reference
	private List<Book> read;
	@Reference
	private List<Book> toread;

	public User(){};
	
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		favourite = new ArrayList<Book>();
		read = new ArrayList<Book>();
		toread = new ArrayList<Book>();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Book> getFavourite() {
		return favourite;
	}

	public void setFavourite(List<Book> favourite) {
		this.favourite = favourite;
	}

	public List<Book> getRead() {
		return read;
	}

	public void setRead(List<Book> read) {
		this.read = read;
	}

	public List<Book> getToread() {
		return toread;
	}

	public void setToread(List<Book> toread) {
		this.toread = toread;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", email=" + email + ", favourite=" + favourite
				+ ", read=" + read + ", toread=" + toread + "]";
	}
}
