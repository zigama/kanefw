package com.pivotaccess.kanefw.web.rest;
import com.pivotaccess.kanefw.domain.Customer;
import com.pivotaccess.kanefw.domain.Device;
import com.pivotaccess.kanefw.domain.Transaction;
import com.pivotaccess.kanefw.repository.CustomerRepository;
import com.pivotaccess.kanefw.repository.DeviceRepository;
import com.pivotaccess.kanefw.repository.TransactionRepository;
import com.pivotaccess.kanefw.repository.search.TransactionSearchRepository;
import com.pivotaccess.kanefw.service.dto.TransactionDTO;
import com.pivotaccess.kanefw.web.rest.errors.BadRequestAlertException;
import com.pivotaccess.kanefw.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Transaction.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "transaction";

    private final TransactionRepository transactionRepository;

    private final TransactionSearchRepository transactionSearchRepository;
    
    private final CustomerRepository customerRepository;
    
    private final DeviceRepository deviceRepository;

    public TransactionResource(TransactionRepository transactionRepository, 
    		TransactionSearchRepository transactionSearchRepository, 
    		CustomerRepository customerRepository,
    		DeviceRepository deviceRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionSearchRepository = transactionSearchRepository;
        this.customerRepository = customerRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * POST  /transactions : Create a new transaction.
     *
     * @param transaction the transaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transaction, or with status 400 (Bad Request) if the transaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transaction);
        if (transaction.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transaction result = transactionRepository.save(transaction);
        transactionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    
    @PostMapping("/testStartTransaction")
    public ResponseEntity<String> createDeviceTransaction(
    		@Valid @RequestBody Map<String, Object> transactionData
    		//@RequestParam("deviceId") String deviceId,
    		//@RequestParam("timeStamp") String timeStamp,
    		//@RequestParam("clientFirstName") String clientFirstName,
    		//@RequestParam("clientLastName") String clientLastName,
    		//@RequestParam("accountNumber") String clientAccountNumber,
    		//@RequestParam("clientPinNumber") String clientPinNumber,
    		//@RequestParam("transactionAmount") String transactionAmount
    		) throws URISyntaxException {
        
    	//log.debug("TRANSACTION DATA: {}", TransactionDTO.toJson(transactionData) );
    	Transaction transaction = new Transaction();
    	Optional<Device> device = deviceRepository.findById(Long.parseLong(
    												String.valueOf(transactionData.get("deviceId"))));
    	
    	Customer customer = customerRepository.findByAccountNumberAndPin(
    											String.valueOf(transactionData.get("accountNumber")),
    											String.valueOf(transactionData.get("clientPinNumber")) );
    	
        if (customer == null || customer.getId() == null) {
            throw new BadRequestAlertException("Invalid account number or pin", "Customer", "notfound");
        }
        
        if (device.get() == null || device.get().getId() == null) {
            throw new BadRequestAlertException("Invalid device ID", "Device", "notfound");
        }
        
        try {
        	transaction.setCustomer(customer);
        	transaction.setDevice(device.get());
            transaction.setTimeStamp(Instant.ofEpochSecond(
            							Long.parseLong(String.valueOf(transactionData.get("timeStamp")))));
            transaction.setTransactionAmount(Double.parseDouble(
            									String.valueOf(transactionData.get("transactionAmount"))));
            
            log.debug("REST request to save Transaction : {}", transaction);
            
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        
        Transaction result = transactionRepository.save(transaction);
        
        transactionSearchRepository.save(result);
        
        customer.setCurrentBalance(customer.getCurrentBalance() - transaction.getTransactionAmount());
        
        customerRepository.save(customer);
        
        TransactionDTO transactionDTO = new TransactionDTO(result);
        
        return ResponseEntity.created(new URI("/api/testStartTransaction/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(TransactionDTO.getTestTransactionResponse(transactionDTO));
        
    }

    /**
     * PUT  /transactions : Updates an existing transaction.
     *
     * @param transaction the transaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transaction,
     * or with status 400 (Bad Request) if the transaction is not valid,
     * or with status 500 (Internal Server Error) if the transaction couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transactions")
    public ResponseEntity<Transaction> updateTransaction(@Valid @RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}", transaction);
        if (transaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Transaction result = transactionRepository.save(transaction);
        transactionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transactions : get all the transactions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transactions in body
     */
    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        log.debug("REST request to get all Transactions");
        return transactionRepository.findAll();
    }

    /**
     * GET  /transactions/:id : get the "id" transaction.
     *
     * @param id the id of the transaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transaction, or with status 404 (Not Found)
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(transaction);
    }

    /**
     * DELETE  /transactions/:id : delete the "id" transaction.
     *
     * @param id the id of the transaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
        transactionSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transactions?query=:query : search for the transaction corresponding
     * to the query.
     *
     * @param query the query of the transaction search
     * @return the result of the search
     */
    @GetMapping("/_search/transactions")
    public List<Transaction> searchTransactions(@RequestParam String query) {
        log.debug("REST request to search Transactions for query {}", query);
        return StreamSupport
            .stream(transactionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
