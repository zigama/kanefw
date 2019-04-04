package com.pivotaccess.kanefw.service.dto;

import com.pivotaccess.kanefw.domain.Customer;

public class CustomerDTO {
	
	private String id;
	private String firstName;
	private String lastName;
	private String accountNumber;
	private String pin;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public CustomerDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public CustomerDTO(Customer customer) {
		// TODO Auto-generated constructor stub
		this.id = customer.getId().toString();
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
		this.accountNumber = customer.getAccountNumber();
		this.pin = customer.getPin();
		
	}

}
