package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

@Entity("bx_users")
public class BXUser {

	@Id
	private ObjectId id;
	@Property
	private String username;
	@Property
	private String password;
	@Property
	private String email;
	@Property
	private String location;
	@Property
	private int age;
	@Property
	private int userId;
//	@Reference
//	private List<Book> favourite;
//	@Reference
//	private List<Book> read;
//	@Reference
//	private List<Book> toread;
	boolean isKey;
	boolean isClassed;

	public boolean isKey() {
		return isKey;
	}
	
	public void setKey(boolean isKey) {
		this.isKey = isKey;
		this.isClassed = true;
	}

	public boolean isClassed() {
		return isClassed;
	}

	public void setClassed(boolean isClassed) {
		this.isClassed = isClassed;
	}

	public BXUser(){};
	
	public BXUser(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
//		favourite = new ArrayList<Book>();
//		read = new ArrayList<Book>();
//		toread = new ArrayList<Book>();
	}
	
	public BXUser(int age, int userId) {
		this.age = age;
		this.userId = userId;
	}
	
	public BXUser(String str) {
		String[] p = str.split(",");
		this.age = Integer.parseInt(p[0]);
		this.userId = Integer.parseInt(p[1]);
		//this.y = Integer.parseInt(p[1]);
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", email=" + email + ", location=" + location
				+ ", age=" + age + "]";
	}
}
