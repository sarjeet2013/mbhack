package com.mbhack.payload;

public class ReturnObject {
		private String status = "OK";
		private String message = "";
		
		public ReturnObject(String status) {
			this.status = status;
		}
		
		public ReturnObject(String status, String message) {
			this.status = status;
			this.message = message;
		}
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}

}
