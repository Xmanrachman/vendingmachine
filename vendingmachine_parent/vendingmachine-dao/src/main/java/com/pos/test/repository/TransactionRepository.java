package com.pos.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pos.test.model.transaction.TransactionProducts;

/**
 * 
 * @author trachman
 * 
 */
@Repository	
public interface TransactionRepository extends JpaRepository<TransactionProducts, Integer>, JpaSpecificationExecutor<TransactionProducts>{

}
