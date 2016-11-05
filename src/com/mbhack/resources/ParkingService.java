package com.mbhack.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class ParkingService {

	 //private Response failureResponse(Exception ex, Response.Status httpStatus, String logMessage){
//       return Response.status(httpStatus).entity(new JsonObject(logMessage +". " + ex.getMessage())).build();
    //}

	public class Location {
		public String lat;
		public String lang;
	}

//curl -X GET --header "Accept: application/json" "http://localhost:8080/mbhack/rest/name"
  @GET
  @Path("/name")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getName() {
   	Location loc = new Location();
   	loc.lat = "10";
   	loc.lang = "10";
   	return Response.status(Response.Status.OK).entity(loc).build();
  }
  //curl -X POST --header "Content-Type: application/json" --header "Accept: application/json" -d "{ \"isChargingNeeded\" : \"false\" }" "http://localhost:8080/mbhack/rest/info/10"
  @POST
  @Path("/info/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getInfo(@PathParam("id") String id, Payload payload) {
    try {
    	Location loc = new Location();
		loc.lat = "10";
    	loc.lang = "10";
    	return Response.status(Response.Status.OK).entity(loc).build();
    }catch (Exception e) {
       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
    } 
  }
}
