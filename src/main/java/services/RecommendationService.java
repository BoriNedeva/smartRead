package services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.BXUser;
import model.Book;

import org.mongodb.morphia.query.Query;

import recommendation.KNNRecommendation;
import dao.DatastoreProvider;
import dto.Token;
import util.Utils;

@Path("recommendations")
public class RecommendationService {
	
	@OPTIONS
	@Path("knn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response knnPre() {
		return Utils.responseBuilder(Response.Status.OK, null);
	}

	@POST
	@Path("knn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response knn(Token token) {
		try {
			Query<BXUser> query = DatastoreProvider.getDS().find(BXUser.class);
			query.field("username").equal(token.getToken());
			int userId = query.project("userId", true).asList().get(0).getUserId();
			KNNRecommendation recommendation = new KNNRecommendation(userId);
			List<Book> recommendations = recommendation.getRecommendations();
			
			return Utils.responseBuilder(Response.Status.OK, recommendations);
		}catch(Exception e){
			return Utils.responseBuilder(Response.Status.INTERNAL_SERVER_ERROR, null);
		}
		
	}
}
