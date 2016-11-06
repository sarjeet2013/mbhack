package com.mbhack.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mbhack.db.DBConnection;
import com.mbhack.payload.ConsumerPayload;
import com.mbhack.payload.PlaceBuilderData;
import com.mbhack.payload.ReturnObject;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.PlaceBuilder;

@Path("/consumer")
public class ParkingConsumer {
	

	
	private static String STATUS_OK = "{ \"Status\" : \"Ok\" }";
	private static String STATUS_ERROR = "{ \"Status\" : \"ERROR\", \"Message\" = \"Unable to get parking spot\" }";

	  @POST
	  @Path("/add")
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response add(ConsumerPayload cp) {
	    try {
	       System.out.println("Got ParkingConsumer.Add request. consumerId: " + cp.getConsumerId() + ", placeId: " +
	         cp.getPlaceId() + ", basePrice: " + cp.getBasePrice());
	  	   DBConnection conn = DBConnection.getConnection();
    	   if (!conn.selectSpotQuery(cp)) {
    		   System.out.println("ParkingConsumer.add - Could not select parking spot.");
    		   return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
    				   new ReturnObject("ERROR", "ParkingConsumer.add - Could not select parking spot.")).build();
    	   }
    	   
    	   //update producer table to false
    	   if (!conn.changeIsAvailable(cp, false)) {
    		   System.out.println("ParkingConsumer.add - Could not make spot unavailable.");
    		   return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
    				   new ReturnObject("ERROR", "ParkingConsumer.add - Could not make spot unavailable.")).build();
    	   }
    	   
    	   //Create entry in the consumer table
    	   conn.insertToConsumerTable(cp);
    	   System.out.println("ParkingConsumer.add - Inserted into consumer table.");
   	   
    	   //Delete from google
    	   deletePlace(cp.getPlaceId());

	       return Response.status(Response.Status.OK).entity(new ReturnObject("OK")).build();
	    }catch (Exception e) {
	       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
	    		   new ReturnObject("ERROR", e.getMessage())).build());
	    } 
	  }
	  
	  @POST
	  @Path("/remove")
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response remove(ConsumerPayload cp) {
		try {
		   System.out.println("Got ParkingConsumer.Add request. consumerId: " + cp.getConsumerId() + ", placeId: " +
		  	   cp.getPlaceId() + ", basePrice: " + cp.getBasePrice());
		  DBConnection conn = DBConnection.getConnection();
		  
		   //Update consumer table
			if (!conn.updateConsumerTable(cp)) {
				System.out.println("ParkingConsumer.remove - Could not update consumer table.");
	    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
	    				new ReturnObject("ERROR", "ParkingConsumer.remove - Could not update consumer table")).build();
			}
			
		   //update producer table
			if (!conn.changeIsAvailable(cp, true)) {
	    		System.out.println("ParkingConsumer.remove - Could not make spot available.");
	    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
	    				new ReturnObject("ERROR", "ParkingConsumer.remove - Could not make spot available.")).build();
	    	}

		   //add place to Google
		   PlaceBuilderData pbd = conn.getPlaceBuilderData(cp.getPlaceId());
		   if (pbd == null) {
	    		System.out.println("ParkingConsumer.remove - Could not add spot back to Google");
	    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
	    				new ReturnObject("ERROR", "ParkingConsumer.remove - Could not add spot back to Google")).build();			   
		   }

	       GooglePlaces client = new GooglePlaces(ParkingService.API_KEY);
           PlaceBuilder builder = new PlaceBuilder(pbd.getName(), Double.parseDouble(pbd.getLat()),
        		   Double.parseDouble(pbd.getLongi()), "parking");
	       Place place = client.addPlace(builder, true);
	       
	       if (!conn.updatePlaceIdInProducerTable(place.getPlaceId(), pbd)) {
				System.out.println("ParkingConsumer.remove - Failed to update place id in Producer table.");
	    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
	    				new ReturnObject("ERROR", "ParkingConsumer.remove - Failed to update place id in Producer table.")).build();
			}
		
	       return Response.status(Response.Status.OK).entity(new ReturnObject("OK")).build();
	    }catch (Exception e) {
	       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
	    		   new ReturnObject("ERROR", e.getMessage())).build());
	    } 
		  
	  }
	  
	  private void deletePlace(String placeId) {
		  try {
		  String payload = "{ \"place_id\" : \"" + placeId + "\"}";

		  URL url = new URL("https://maps.googleapis.com/maps/api/place/delete/json?key=" + ParkingService.API_KEY);
		  URLConnection connection = url.openConnection();
		  connection.setDoInput(true);
		  connection.setDoOutput(true);

		  connection.connect();

		  OutputStream os = connection.getOutputStream();
		  PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
		  pw.write(payload);
		  pw.close();

		  InputStream is = connection.getInputStream();
		  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		  String line = null;
		  StringBuffer sb = new StringBuffer();
		  while ((line = reader.readLine()) != null) {
		      sb.append(line);
		  }
		  is.close();
		  String response = sb.toString();
		  System.out.println(response);
		  } catch (Exception e) {
			  System.out.println("deletePlace - Failed to delete place from Google.");
		  }
	  }
}
