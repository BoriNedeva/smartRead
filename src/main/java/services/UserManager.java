package services;

import java.util.List;

import javax.rmi.CORBA.Util;
import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.soap.AddressingFeature.Responses;

import model.BXUser;

import org.mongodb.morphia.query.Query;

import util.ResponseMessages;
import util.Utils;

import com.google.gson.Gson;

import dao.DatastoreProvider;
import dto.LoginUser;
import dto.RegistrationUser;
import dto.Token;

@Path("user")
public class UserManager {

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginUser user) {

		Query<BXUser> query = DatastoreProvider.getDS().createQuery(BXUser.class)
				.disableValidation().filter("username", user.getUsername())
				.filter("password", user.getPassword());

		List<BXUser> mem = query.asList();

		if (mem.size() == 0) {
			return Utils.responseBuilder(Response.Status.UNAUTHORIZED,
					ResponseMessages.UNAUTHORIZED);
		}

		return Utils.responseBuilder(Response.Status.OK,
				new Token(user.getUsername()));

	}

	@OPTIONS
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginPre() {
		return Utils.responseBuilder(Response.Status.OK, null);
	}

	@POST
	@Path("registration")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registration(RegistrationUser user) {
		// {registrationUser: {username: "sss", password: "ssss", email:
		// "dsfs@kljfdk"}} - sample request
		BXUser member = new BXUser(user.getUsername(), user.getPassword(),
				user.getEmail());
		System.out.println(member.toString());
		try {
			List<BXUser> memberName = DatastoreProvider.getDS()
					.createQuery(BXUser.class)
					.filter("username = ", member.getUsername()).asList();

			List<BXUser> memberEmail = DatastoreProvider.getDS()
					.createQuery(BXUser.class)
					.filter("email = ", member.getEmail()).asList();

			if (memberName.size() != 0) {
				return Utils.responseBuilder(Response.Status.CONFLICT,
						ResponseMessages.ANOTHER_NAME);
			}
			if (memberEmail.size() != 0) {
				return Utils.responseBuilder(Response.Status.CONFLICT,
						ResponseMessages.ANOTHER_EMAIL);
			} else {
				DatastoreProvider.getDS().save(member);
				Query query = DatastoreProvider.getDS().createQuery(BXUser.class)
						.filter("username", member.getUsername());
				BXUser saved = (BXUser) query.get();
				if (saved != null) {
					return Utils.responseBuilder(Response.Status.OK,
							ResponseMessages.SUCCESSFULL_REGISTER);
				} else {
					return Utils.responseBuilder(
							Response.Status.INTERNAL_SERVER_ERROR,
							ResponseMessages.USER_NOT_SAVED);
				}
			}
		} catch (Exception e) {
			return Utils.responseBuilder(Response.Status.INTERNAL_SERVER_ERROR,
					ResponseMessages.USER_NOT_SAVED);
		}
	}

	@OPTIONS
	@Path("registration")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerPre() {
		return Utils.responseBuilder(Response.Status.OK, null);
	}
}
