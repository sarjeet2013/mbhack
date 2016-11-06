package com.mbhack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import com.mbhack.payload.ConsumerPayload;
import com.mbhack.payload.PlaceBuilderData;
import com.mbhack.payload.ProducerPayload;

import se.walkercrou.places.Place;
import se.walkercrou.places.PlaceBuilder;

public class DBConnection {
	// The JDBC Connector Class.
	  private static final String dbClassName = "com.mysql.jdbc.Driver";

	  private static final String connectionString =
	                          "jdbc:mysql://127.0.0.1/testdb";
	  
	  private static DBConnection connection = new DBConnection();
	  
	  private Connection conn = null; 
	  
	  public static DBConnection getConnection() {
		  return connection;
	  }
	  
	  private DBConnection(){
		try {
		  Class.forName(dbClassName);
		  Properties p = new Properties();
		  p.put("user","test");
		  p.put("password","test");

		  // Now try to connect
		  DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection(connectionString,p);
		  System.out.println("Connection established!");
		} catch (Exception e) {
			System.out.println("Failed to establish connection!");
			e.printStackTrace();
		}
	  }
		
		// values("user1", "House No 1, City test, CA", "111-111-1111", "suv", false, true, false, 1, True);
		public void insertProducer(ProducerPayload pp, String placeId) throws Exception {
			try {
			//String query = "insert into test(name, address, phone, parking_type, is_charging, is_available," +  
			//		"is_handicapped, producer_id, need_rfid) values (" + pp.getName() + ", " + pp.getAddress() + ", " + pp.getPhone() +
			//		", " + pp.getParkingType() + ", " + pp.getIsCharging() + ", " + pp.getIsAvailable() + ", " + pp.getIsHandicapped() +
			//		", " + pp.getProducerId() + ", " + pp.getNeedRfid() + ")";
				
			String query = "insert into producer(name, address, phone, parking_type, is_charging, is_available," +  
					"is_handicapped, producer_id, need_rfid, lat, longi, place_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, pp.getName());
			preparedStmt.setString(2, pp.getAddress());
			preparedStmt.setString(3, pp.getPhone());
			preparedStmt.setString(4, pp.getParkingType());
			preparedStmt.setBoolean(5, pp.getIsCharging());
			preparedStmt.setBoolean(6, pp.getIsAvailable());
			preparedStmt.setBoolean(7, pp.getIsHandicapped());
			preparedStmt.setString(8, pp.getProducerId());
			preparedStmt.setBoolean(9, pp.getNeedRfid());
			preparedStmt.setString(10, pp.getLat());
			preparedStmt.setString(11, pp.getLongi());
			preparedStmt.setString(12, placeId);

			preparedStmt.execute();
			} catch (Exception e) {
				System.out.println("insertProducer - QueryFailed!");
				e.printStackTrace();
				throw e;
			}
				
		}
		
		public boolean selectSpotQuery(ConsumerPayload cp) throws Exception {
			
			try {
				String query = "select * from producer where is_available=true and place_id=?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, cp.getPlaceId());
				if (preparedStmt.execute()) {
					ResultSet rs = preparedStmt.getResultSet();
					if (rs.first()) {
						System.out.println("selectSpotQuery - Parking spot found.");
						return true;
					} else {
						System.out.println("selectSpotQuery - Parking spot not found 1.");
						return false;
					}
				} else {
					System.out.println("selectSpotQuery - Parking spot not found 2.");
					return false;
				}
				
			} catch (Exception e) {
				System.out.println("selectSpotQuery - QueryFailed!");
				e.printStackTrace();
				throw e;
			}
		}

		public boolean changeIsAvailable(ConsumerPayload cp, boolean isAvailable) throws Exception {
			try {

				String query;
				if (isAvailable) {
					query = "update producer SET is_available=true where place_id=?";
				} else {
					query = "update producer SET is_available=false where place_id=?";
				}

				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, cp.getPlaceId());
				if (!preparedStmt.execute()) {
					if (preparedStmt.getUpdateCount() > 0) {
						System.out.println("changeIsAvailable - Parking spot availability set to: " + isAvailable);
						return true;
					}
					else {
						System.out.println("changeIsAvailable - Could not set parking spot availability to 1:" + isAvailable);
						return false;
					}
				} else {
					System.out.println("changeIsAvailable - Could not set parking spot availability to 2:" + isAvailable);
					return false;
				}
				
			} catch (Exception e) {
				System.out.println("changeIsAvailable - QueryFailed!");
				e.printStackTrace();
				throw e;
			}
			
		}
		
		public void insertToConsumerTable(ConsumerPayload cp) throws Exception {
			try {
				String query = "insert into consumer(consumer_Id, place_id, base_price, start_time, end_time) " +
                 "values(?, ?, ?, ?, ?)";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, cp.getConsumerId());
				preparedStmt.setString(2, cp.getPlaceId());
				preparedStmt.setString(3, cp.getBasePrice());
				//preparedStmt.setString(4, "NOW()");
				preparedStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				preparedStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				preparedStmt.execute();
			} catch (Exception e) {
				System.out.println("updateConsumerTable - QueryFailed!");
				e.printStackTrace();
				throw e;
			}
		}

		public boolean updateConsumerTable(ConsumerPayload cp) throws Exception {
			try {

				String query = "update consumer SET end_time=? where consumer_Id=? and place_id=?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				preparedStmt.setString(2, cp.getConsumerId());
				preparedStmt.setString(3, cp.getPlaceId());
				
				if (!preparedStmt.execute()) {
					if (preparedStmt.getUpdateCount() > 0) {
						System.out.println("updateConsumerTable - Update end time for consumer");
						return true;
					}
					else {
						System.out.println("updateConsumerTable - Failed to update end time for consumer 1");
						return false;
					}
				} else {
					System.out.println("Update end time for consumer - Failed to update end time for consumer 2");
					return false;
				}
				
			} catch (Exception e) {
				System.out.println("updateConsumerTable - QueryFailed!");
				e.printStackTrace();
				throw e;
			}
			
		}
		
		public PlaceBuilderData getPlaceBuilderData(String placeId) throws Exception {
			//select name,lat,longi from producer where place_id="qgYvCi0wMDAwMDA1MzgwOTBiNzRkOjgwOGZjOGZmZDkxOjU5YzJjN2E0YzdiZGE1NDc";
			
			try {
				String query = "select name,lat,longi from producer where place_id=?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, placeId);

				if (preparedStmt.execute()) {
					ResultSet rs = preparedStmt.getResultSet();
					if (rs.first()) {
						PlaceBuilderData builderData = new PlaceBuilderData(rs.getString(1), rs.getString(2), rs.getString(3));
						System.out.println("getPlaceBuilderData - Parking spot found.");
						return builderData;
					} else {
						System.out.println("getPlaceBuilderData - Parking spot not found 1.");
						return null;
					}
				} else {
					System.out.println("getPlaceBuilderData - Parking spot not found 2.");
					return null;
				}
				
			} catch (Exception e) {
				System.out.println("getPlaceBuilderData - QueryFailed!");
				e.printStackTrace();
				throw e;
			}
			
		}
		
		//update producer SET place_id=? where lat=? and longi=?
		public boolean updatePlaceIdInProducerTable(String placeId, PlaceBuilderData pbd) throws Exception {
			try {

				String query = "update producer SET place_id=? where lat=? and longi=?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, placeId);
				preparedStmt.setString(2, pbd.getLat());
				preparedStmt.setString(3, pbd.getLongi());
				
				if (!preparedStmt.execute()) {
					if (preparedStmt.getUpdateCount() > 0) {
						System.out.println("updatePlaceIdInProducerTable - Updated place id in producer table");
						return true;
					}
					else {
						System.out.println("updatePlaceIdInProducerTable - Failed to update place id in producer table 1");
						return false;
					}
				} else {
					System.out.println("UpdatePlaceIdInProducerTable - Failed to update place id in producer table 2");
					return false;
				}
				
			} catch (Exception e) {
				System.out.println("updatePlaceIdInProducerTable - QueryFailed!");
				e.printStackTrace();
				throw e;
			}
			
		}
}
