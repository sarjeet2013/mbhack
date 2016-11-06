package com.mbhack.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mbhack.db.DBConnection;
import com.mbhack.payload.ConsumerPayload;

@Path("/consumer")
public class ParkingConsumer {
	
	private static String STATUS_OK = "{ \"Status\" : \"Ok\" }";
	private static String STATUS_ERROR = "{ \"Status\" : \"ERROR\", \"Message\" = \"Unable to get parking spot\" }";

	  @POST
	  @Path("/add")
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response add(ConsumerPayload cp) {
	    try {
	       System.out.println("Got Add request. consumerId: " + cp.getConsumerId() + ", placeId: " + cp.getPlaceId());
	  	   DBConnection conn = DBConnection.getConnection();
    	   if (!conn.selectSpotQuery(cp))
    		   return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(STATUS_ERROR).build();

	       return Response.status(Response.Status.OK).entity(STATUS_OK).build();
	    }catch (Exception e) {
	       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
	    } 
	  }
}
