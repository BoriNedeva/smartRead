package dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddToListObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private model.Book book;
	private String list;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public model.Book getBook() {
		return book;
	}
	public void setBook(model.Book book) {
		this.book = book;
	}
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
}
