package services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import dao.DatastoreProvider;
import dao.MorphiaProvider;
import model.User;

import com.google.gson.Gson;

@Path("test")
public class TestService {

	@GET
	@Path("test")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response test(){
		User user = new User();
		user.setName("Bori");
		user.setBooks(new ArrayList<String>());
		user.getBooks().add("Harry Potter");
		user.getBooks().add("Harry Potter 2");
		user.getBooks().add("Harry Potter 3");
		
		Morphia morphia = new MorphiaProvider().get();
		DatastoreProvider dsProvider = new DatastoreProvider(morphia);
		Datastore dataStore = dsProvider.getDS();
		try{
			dataStore.save(user);
			Query query = dataStore.createQuery(User.class).filter("name", user.getName());
			User saved = (User) query.get();
			Gson gson = new Gson();
			return Response.status(Response.Status.OK).entity(gson.toJson(saved)).build();
		}catch (Exception e){
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
