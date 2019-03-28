package com.pivotaccess.kanefw.service.dto;

import javax.validation.constraints.Size;

import com.pivotaccess.kanefw.domain.Transaction;

public class TransactionDTO {
	
	private String timestamp;
	
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
	
	

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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
		this.timestamp = String.valueOf(transaction.getTimeStamp().getEpochSecond());
		this.clientFirstName = transaction.getCustomer().getFirstName();
		this.clientLastName = transaction.getCustomer().getLastName();
		this.clientAccountNumber = transaction.getCustomer().getAccountNumber();
		this.clientPinNumber = transaction.getCustomer().getPin();
		this.transactionId	= String.valueOf(transaction.getId());
		
	}

}
