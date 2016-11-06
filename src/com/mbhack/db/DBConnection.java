package com.mbhack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import com.mbhack.payload.ProducerPayload;

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
		
	  public void executeQuery(String query) {
			
    	}
		
		// values("user1", "House No 1, City test, CA", "111-111-1111", "suv", false, true, false, 1, True);
		public void insertProducer(ProducerPayload pp){
			try {
			//String query = "insert into test(name, address, phone, parking_type, is_charging, is_available," +  
			//		"is_handicapped, producer_id, need_rfid) values (" + pp.getName() + ", " + pp.getAddress() + ", " + pp.getPhone() +
			//		", " + pp.getParkingType() + ", " + pp.getIsCharging() + ", " + pp.getIsAvailable() + ", " + pp.getIsHandicapped() +
			//		", " + pp.getProducerId() + ", " + pp.getNeedRfid() + ")";
				
			String query = "insert into test(name, address, phone, parking_type, is_charging, is_available," +  
					"is_handicapped, producer_id, need_rfid, lat, longi) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
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

			preparedStmt.execute();
			} catch (Exception e) {
				System.out.println("QueryFailed!");
				e.printStackTrace();
				
			}
				
		}


}
