package services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Book;
import model.BXUser;
import model.Rating;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import util.Constants;
import util.ResponseMessages;
import util.Utils;
import dao.DatastoreProvider;
import dto.AddToListObj;
import dto.RatingObj;

@Path("actions")
public class ActionsService {

//	@POST
//	@Path("addToList")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response addToList(AddToListObj obj) {
//		try {
//			Query<User> query = DatastoreProvider.getDS()
//					.createQuery(User.class).field("username")
//					.equal(obj.getUsername());
//			User usr = (User) query.get();
//			switch (obj.getList()) {
//			case Constants.READ_LIST: {
//				List<Book> read = usr.getRead();
//				if (read == null){
//					read = new ArrayList<Book>();
//					usr.setRead(read);
//				}
//				usr.getRead().add(obj.getBook());
//				UpdateOperations<User> ops = DatastoreProvider.getDS()
//						.createUpdateOperations(User.class)
//						.set("read", usr.getRead());
//				DatastoreProvider.getDS().update(query, ops);
//				break;
//			}
//			case Constants.TO_READ_LIST: {
//				List<Book> toread = usr.getToread();
//				if (toread == null){
//					toread = new ArrayList<Book>();
//					usr.setToread(toread);
//				}
//				usr.getToread().add(obj.getBook());
//				UpdateOperations<User> ops = DatastoreProvider.getDS()
//						.createUpdateOperations(User.class)
//						.set("toread", usr.getToread());
//				DatastoreProvider.getDS().update(query, ops);
//				break;
//			}
//			case Constants.FAVOURITES_LIST: {
//				List<Book> favs = usr.getFavourite();
//				if (favs == null){
//					favs = new ArrayList<Book>();
//					usr.setFavourite(favs);
//				}
//				usr.getFavourite().add(obj.getBook());
//				UpdateOperations<User> ops = DatastoreProvider.getDS()
//						.createUpdateOperations(User.class)
//						.set("favourite", usr.getFavourite());
//				DatastoreProvider.getDS().update(query, ops);
//				break;
//			}
//			default: {
//				return Utils.responseBuilder(Response.Status.BAD_REQUEST,
//						"Error occured!");
//			}
//			}
//			return Utils.responseBuilder(Response.Status.OK, "Saved!");
//		} catch (Exception e) {
//			return Utils.responseBuilder(Response.Status.INTERNAL_SERVER_ERROR,
//					"Error occured!");
//		}
//	}

	@OPTIONS
	@Path("addToList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addToListPre() {
		return Utils.responseBuilder(Response.Status.OK, null);
	}
	
	@OPTIONS
	@Path("rate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ratePre() {
		return Utils.responseBuilder(Response.Status.OK, null);
	}
	
	@POST
	@Path("rate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rate(RatingObj obj) {
		
		try{
			Query<BXUser> query = DatastoreProvider.getDS().find(BXUser.class);
			query.field("username").equal(obj.getUsername());
			int userId = query.project("userId", true).asList().get(0).getUserId();
			
			Query<Rating> queryRating = DatastoreProvider.getDS().find(Rating.class);
			queryRating.field("userId").equal(userId);
			queryRating.field("isbn").equal(obj.getIsbn());
			List<Rating> ratings = queryRating.asList();
			if (ratings.isEmpty()){
				Rating newRating = new Rating(userId, obj.getRating(), obj.getIsbn());
				DatastoreProvider.getDS().save(newRating);
			}else {
				UpdateOperations<Rating> ops = DatastoreProvider.getDS().createUpdateOperations(Rating.class).set("rating", obj.getRating());
				DatastoreProvider.getDS().update(queryRating, ops);
			}
			return Utils.responseBuilder(Response.Status.OK, null);
		}catch (Exception e) {
			return Utils.responseBuilder(Response.Status.INTERNAL_SERVER_ERROR, null);
		}
	}
}
