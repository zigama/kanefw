package com.pivotaccess.kanefw.repository;

import com.pivotaccess.kanefw.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data  repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("SELECT customer FROM Customer customer WHERE customer.accountNumber =:accountNumber AND customer.pin=:pin")
	Customer findByAccountNumberAndPin(	@Param("accountNumber") String clientAccountNumber,
										@Param("pin")String clientPinNumber);

}
