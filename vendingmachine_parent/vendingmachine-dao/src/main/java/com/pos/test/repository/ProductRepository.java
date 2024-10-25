/**
 * 
 */
package com.pos.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pos.test.model.master.Products;

/**
 *  @author trachman
 *  
 *  
 */
@Repository
public interface ProductRepository extends JpaRepository<Products, Integer>, JpaSpecificationExecutor<Products> {

	Optional<Products> findByIdProduct(Integer idProduct);
	
}
