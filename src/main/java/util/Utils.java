package util;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;

public class Utils {

	public static Response responseBuilder(Response.Status status, Object toJson){
		if (toJson != null){
			Gson gson = new Gson();
			return Response.status(status).entity(gson.toJson(toJson))
				.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS").header("Access-Control-Allow-Headers", "Content-Type,Accept").build();
		}else{
			return Response.status(status).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS").header("Access-Control-Allow-Headers", "Content-Type,Accept").build();
		}
	}
}
