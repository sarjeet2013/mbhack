package com.mbhack.payload;

public class ConsumerPayload {
   private String consumerId;
   private String placeId;
   private String basePrice = "0";
   
   
   public String getConsumerId() {
	   return consumerId;
   }
   public void setConsumerId(String consumerId) {
	   this.consumerId = consumerId;
   }
   
   public String getPlaceId() {
	   return placeId;
   }
   public void setPlaceId(String placeId) {
	   this.placeId = placeId;
   }

   public String getBasePrice() {
	   return basePrice;
   }
   public void setBasePrice(String basePrice) {
	   this.basePrice = basePrice;
   }
}
