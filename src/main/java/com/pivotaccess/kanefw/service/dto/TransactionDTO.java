package com.pivotaccess.kanefw.service.dto;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.validation.constraints.Size;

import com.pivotaccess.kanefw.domain.Transaction;



public class TransactionDTO {
	
	private String timeStamp;
	
	@Size(max = 50)
    private String clientFirstName;
	
	@Size(max = 50)
    private String clientLastName;	
	
	@Size(max = 50)
    private String clientAccountNumber;
	
	@Size(max = 50)
    private String clientPinNumber;
	
	private String deviceId;
	
	private String transactionId;
	
	private String transactionAmount;
	
	private String previousBalance;
	
	private String currentBalance;	
	
	

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getPreviousBalance() {
		return previousBalance;
	}

	public void setPreviousBalance(String previousBalance) {
		this.previousBalance = previousBalance;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timestamp) {
		this.timeStamp = timestamp;
	}

	public String getClientFirstName() {
		return clientFirstName;
	}

	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName = clientFirstName;
	}

	public String getClientLastName() {
		return clientLastName;
	}

	public void setClientLastName(String clientLastName) {
		this.clientLastName = clientLastName;
	}

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}

	public String getClientPinNumber() {
		return clientPinNumber;
	}

	public void setClientPinNumber(String clientPinNumber) {
		this.clientPinNumber = clientPinNumber;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}	

	public String getTransactionId() {
		return transactionId;
	}

	public TransactionDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public TransactionDTO(Transaction transaction ) {
		// TODO Auto-generated constructor stub
		
		this.deviceId = String.valueOf(transaction.getDevice().getId());
		this.timeStamp = String.valueOf(transaction.getTimeStamp().getEpochSecond());
		this.clientFirstName = transaction.getCustomer().getFirstName();
		this.clientLastName = transaction.getCustomer().getLastName();
		this.clientAccountNumber = transaction.getCustomer().getAccountNumber();
		this.clientPinNumber = transaction.getCustomer().getPin();
		this.transactionId	= String.valueOf(transaction.getId());
		this.transactionAmount = String.valueOf(transaction.getTransactionAmount());
		this.previousBalance = String.valueOf(transaction.getCustomer().getCurrentBalance());
		this.currentBalance = String.valueOf(transaction.getCustomer().getCurrentBalance() -
								transaction.getTransactionAmount());
	}
	
	
	public static String toJson(TransactionDTO transactionDTO){
		
		Jsonb jsonb = JsonbBuilder.create();
		String result = jsonb.toJson(transactionDTO);		
		return result;
		
	}

}
