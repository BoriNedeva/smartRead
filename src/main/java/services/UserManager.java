package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import dto.LoginUser;

@Path("user")
public class UserManager {

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(LoginUser user){
		//{ "loginUser" : {"username" : "rest1", "password" : "rest1"}} - sample request
		Gson gson = new Gson();
		System.out.println(user.getPassword());
		System.out.println(user.getUsername());
		return Response.status(Response.Status.OK).entity(gson.toJson(new String("OK!!!"))).build();
	}
}
