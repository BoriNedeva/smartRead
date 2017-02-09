package services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import util.Utils;
import dao.DatastoreProvider;
import model.Book;
import dto.SearchBook;

import com.google.gson.Gson;

@Path("search")
public class SearchService {

	@POST
	@Path("searchBooks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(SearchBook searchData){
		
//		if (searchData.getName() != null && !searchData.getName().isEmpty() 
//				&& searchData.getAuthor() != null && !searchData.getAuthor().isEmpty()
//				&& searchData.getGenres() != null && !searchData.getGenres().isEmpty()){
//			Book book = new Book();
//			book.setTitle(searchData.getName());
//			book.setAuthor(searchData.getAuthor());
//			book.setGenre(searchData.getGenres().get(0));
//			DatastoreProvider.getDS().save(book);
//		}
		
		Query<Book> query = DatastoreProvider.getDS().find(Book.class);
		if (searchData.getName() != null && !searchData.getName().isEmpty()){
			query.field("title").equal(searchData.getName());
		}
		if (searchData.getAuthor() != null && !searchData.getAuthor().isEmpty()){
			query.field("author").equal(searchData.getAuthor());
		}
//		if (searchData.getGenres() != null && !searchData.getGenres().isEmpty()){
//			query.field("genre").in(searchData.getGenres());
//		}
		List<Book> found;
		if (searchData.getName() != null && !searchData.getName().isEmpty() 
				|| searchData.getAuthor() != null && !searchData.getAuthor().isEmpty()){
			found = query.asList();
		}else{
			found = DatastoreProvider.getDS().find(Book.class).limit(100).asList();
		}
		
		return Utils.responseBuilder(Response.Status.OK, found);
	}
	
	@OPTIONS
	@Path("searchBooks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchPre() {
		return Utils.responseBuilder(Response.Status.OK, null);
	}
}
