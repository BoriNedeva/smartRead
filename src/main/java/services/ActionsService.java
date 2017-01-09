package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import util.Utils;
import dto.AddToListObj;

@Path("actions")
public class ActionsService {
	
	@POST
	@Path("addToList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addToList(AddToListObj obj){
		return null;
	}
	
	@OPTIONS
	@Path("addToList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addToListPre(){
		return Utils.responseBuilder(Response.Status.OK, null);
	}
}
