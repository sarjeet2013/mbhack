package com.mbhack.payload;

//create table test(name varchar(50), address varchar(50), phone varchar(50), parking_type varchar(50),
//place_id varchar(50), is_charging Boolean, is_handicapped Boolean, producer_id int, need_rfid Boolean);

public class ProducerPayload {
  private String name;
  private String address;
  private String phone;
  private String parkingType;
  //private String placeId;
  private boolean isCharging;
  private boolean isAvailable;
  private boolean isHandicapped;
  private String producerId;
  private boolean needRfid;
  
  public String getName() {
	  return name;
  }
  public void setName(String name) {
	  this.name = name;
  }
  
  public String getAddress() {
	  return address;
  }
  public void setAddress(String address) {
	  this.address = address;
  }
  
  public String getPhone() {
	  return phone;
  }
  public void setPhone(String phone) {
	  this.phone = phone;
  }
  
  public String getParkingType() {
	  return parkingType;
  }
  public void setParkingType(String parkingType) {
	  this.parkingType = parkingType;
  }
  /*
  public String getPlaceId() {
	  return placeId;
  }
  public void setPlaceId(String placeId) {
	  this.placeId = placeId;
  }*/

  public boolean getIsCharging() {
	  return isCharging;
  }
  public void setIsCharging(boolean isCharging) {
	  this.isCharging = isCharging;
  }
  
  public boolean getIsAvailable() {
	  return isAvailable;
  }
  public void setIsAvailable(boolean isAvailable) {
	  this.isAvailable = isAvailable;
  }
  
  public boolean getIsHandicapped() {
	  return isHandicapped;
  }
  public void setIsHandicapped(boolean isHandicapped) {
	  this.isHandicapped = isHandicapped;
  }
  
  public String getProducerId() {
	  return producerId;
  }
  public void setProducerId(String producerId) {
	  this.producerId = producerId;
  }

  public boolean getNeedRfid() {
	  return needRfid;
  }
  public void setNeedRfid(boolean needRfid) {
	  this.needRfid = needRfid;
  }
}
