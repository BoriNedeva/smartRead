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

//	@GET
//	@Path("search")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response test(){
//		Book book = new Book();
//		book.setName("Dont come back");
//		book.setAuthor("Lui Chaild");
//		book.setGenre("Criminal");
//		
//		book.setName("61 Hours");
//		book.setAuthor("Lui Chaild");
//		book.setGenre("Criminal");
//		
//		book.setName("Golemanov");
//		book.setAuthor("Stefan Kostov");
//		book.setGenre("Comedy");
//		
//		try{
//			dataStore.save(book);
//			Query query = dataStore.createQuery(Book.class).filter("name", book.getName());
//			Book saved = (Book) query.get();
//			Gson gson = new Gson();
//			return Response.status(Response.Status.OK).entity(gson.toJson(saved)).build();
//		}catch (Exception e){
//			e.printStackTrace();
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}
//	
//	
//	@POST
//	@Path("byname")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response searchByName(SearchBook books) {
//		List<Book> found = dataStore.createQuery(Book.class).filter("name = ", books.getName()).asList();
//
//		Gson gson = new Gson();
//		return Response.status(Response.Status.OK).entity(gson.toJson(found)).build();
//
//	}
//	
//	@POST
//	@Path("byauthor")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response searchByAuthor(SearchBook bok){
//		
//		List<Book> found = dataStore.createQuery(Book.class).filter("author = ", bok.getAuthor()).asList();
//	
//		Gson gson = new Gson();
//		return Response.status(Response.Status.OK).entity(gson.toJson(found)).build();
//
//		
//	}
//	
//	@GET
//	@Path("bygenre")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response searchByGenre(){
//		
//		List<Book> found = dataStore.createQuery(Book.class).filter("genre", "Criminal").asList();
//
//		Gson gson = new Gson();
//		return Response.status(Response.Status.OK).entity(gson.toJson(found)).build();
//	}
	
	@POST
	@Path("searchBooks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(SearchBook searchData){
		
		if (searchData.getName() != null && !searchData.getName().isEmpty() 
				&& searchData.getAuthor() != null && !searchData.getAuthor().isEmpty()
				&& searchData.getGenres() != null && !searchData.getGenres().isEmpty()){
			Book book = new Book();
			book.setName(searchData.getName());
			book.setAuthor(searchData.getAuthor());
			book.setGenre(searchData.getGenres().get(0));
			DatastoreProvider.getDS().save(book);
		}
		
		Query<Book> query = DatastoreProvider.getDS().find(Book.class);
		if (searchData.getName() != null && !searchData.getName().isEmpty()){
			query.field("name").equal(searchData.getName());
		}
		if (searchData.getAuthor() != null && !searchData.getAuthor().isEmpty()){
			query.field("author").equal(searchData.getAuthor());
		}
		if (searchData.getGenres() != null && !searchData.getGenres().isEmpty()){
			query.field("genre").in(searchData.getGenres());
		}
		List<Book> found = query.asList();
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
