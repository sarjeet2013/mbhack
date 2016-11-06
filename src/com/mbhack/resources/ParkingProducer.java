package com.mbhack.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mbhack.db.DBConnection;
import com.mbhack.payload.ProducerPayload;

@Path("/producer")
public class ParkingProducer {
	
	private static String STATUS_OK = "{ \"Status\" : \"Ok\" }";
	
	  @POST
	  @Path("/add")
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response getInfo(ProducerPayload producerPayload) {
	    try {
           System.out.println("Got Add request. name: " + producerPayload.getName() + ", address: "+ producerPayload.getAddress() +
        		   ", phone: " + producerPayload.getPhone() +", parkingType: " + producerPayload.getParkingType() + ", isCharging: " + producerPayload.getIsCharging()+
        		   ", isAvailable: " + producerPayload.getIsAvailable() + ", isHandicapped: " + producerPayload.getIsHandicapped() +", producerId:" +
        		   producerPayload.getProducerId() +", needRfId: " + producerPayload.getNeedRfid());
           DBConnection conn = DBConnection.getConnection();
           conn.insertProducer(producerPayload);
           
	       return Response.status(Response.Status.OK).entity(STATUS_OK).build();
	    }catch (Exception e) {
	       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
	    } 
	  }
}