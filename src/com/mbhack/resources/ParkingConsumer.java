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

import se.walkercrou.places.GooglePlaces;
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
	       System.out.println("Got Add request. consumerId: " + cp.getConsumerId() + ", placeId: " + cp.getPlaceId());
	  	   DBConnection conn = DBConnection.getConnection();
    	   if (!conn.selectSpotQuery(cp)) {
    		   System.out.println("ParkingConsumer.add - Could not select parking spot.");
    		   return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(STATUS_ERROR).build();
    	   }
    	   
    	   //update producer table to false
    	   if (!conn.makeUnavailable(cp)) {
    		   System.out.println("ParkingConsumer.add - Could not make spot unavailable.");
    		   return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(STATUS_ERROR).build();
    	   }
    	   //Delete from google
    	   //GooglePlaces client = new GooglePlaces(ParkingService.API_KEY);
    	   //Place place = new PlaceBuilder(name, lat, lng, types)
    	   //client.deletePlace(place, extraParams);
    	   deletePlace(cp.getPlaceId());

	       return Response.status(Response.Status.OK).entity(STATUS_OK).build();
	    }catch (Exception e) {
	       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
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
