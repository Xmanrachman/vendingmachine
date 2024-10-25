package com.pos.test.service.transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pos.test.model.dto.TransactionDTO;
import com.pos.test.model.master.Products;
import com.pos.test.model.transaction.TransactionProducts;
import com.pos.test.process.ProductsResponse;
import com.pos.test.process.transaction.ProcesTransaction;
import com.pos.test.repository.ProductRepository;
import com.pos.test.repository.TransactionRepository;
import com.pos.test.response.GeneralResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceTransactionProduct {

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private ProductRepository productsRepo;

	@Autowired
	private ProcesTransaction processTransaction;

	public ResponseEntity<Object> transactionProducts(TransactionDTO requestTrx) {

		GeneralResponse<Object> response = new GeneralResponse<Object>();
		TransactionProducts transactionProducts = new TransactionProducts();
		ProductsResponse infoPayment = null;
		if (requestTrx.equals(null) || requestTrx.getIdTranction() != 0) {

			response.setInfo("Data transaction is null ");
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setEntity(transactionProducts);

			return ResponseEntity.status(response.getStatus()).body(response);
		}

		if (requestTrx.getPayment() != 0) {

			infoPayment = processTransaction.processValidationPayment(requestTrx);
			log.info("check data calLembarUang nad CalPayment "+infoPayment.getChange()+ " "+
			infoPayment.getLembarUang()+" "+infoPayment.getCheckingPayment());
			if (infoPayment.getCheckingPayment() == false) {
				response.setInfo("Payment is wrong");
				response.setStatus(HttpStatus.SC_BAD_REQUEST);
				response.setEntity(transactionProducts);

				return ResponseEntity.status(response.getStatus()).body(response);
			}
		}

		if (requestTrx.getDataProducts().getIdProduct() != 0) {
			Products product = null;

			Optional<Products> optProduct = productsRepo.findByIdProduct(requestTrx.getDataProducts().getIdProduct());
			if (optProduct.isPresent()) {
				if (optProduct.get().getQuantityProduct() == 0) {
					response.setInfo("Out Of stock");
					response.setStatus(HttpStatus.SC_BAD_REQUEST);
					response.setEntity(transactionProducts);

					return ResponseEntity.status(response.getStatus()).body(response);
				}
				
				product = processTransaction.processValidationQty(requestTrx.getDataProducts().getQuantityProduct()
						, optProduct.get());
				log.info("checking Qty " + product.getQuantityProduct());
				if (product.getQuantityProduct() < 0 ) {

					response.setInfo("Quantity is not enough");
					response.setStatus(HttpStatus.SC_BAD_REQUEST);
					response.setEntity(transactionProducts);
					
					return ResponseEntity.status(response.getStatus()).body(response);
				} 
			}
			Date dateTrx = convertDate(requestTrx.getDateTransaction());
			
			
			transactionProducts.setCodeTransaction(requestTrx.getCodeTransaction());
			transactionProducts.setDateTransaction(dateTrx);
			transactionProducts.setItemProducts(product);
			transactionProducts.setTotalItems(requestTrx.getTotalItems());
			transactionProducts.setTotalPrice(requestTrx.getTotalPrice());
			transactionProducts.setTotalPayment(requestTrx.getTotalPrice()*Double.parseDouble(String.valueOf(requestTrx.getTotalItems())));
			transactionProducts.setPayment(requestTrx.getPayment());
			transactionProducts.setLembarUang(infoPayment.getLembarUang());
			transactionProducts.setChanges(infoPayment.getChange());
			
			transactionRepo.saveAndFlush(transactionProducts);
			
			if (transactionProducts.getIdTranction() != 0) {
				productsRepo.saveAndFlush(product);

				response.setInfo("Transaction succesfuly");
				response.setStatus(HttpStatus.SC_OK);
				response.setEntity(transactionProducts);
				
			}
			
		}

		return ResponseEntity.status(response.getStatus()).body(response);

	}
	
	private Date convertDate(String convertDateTrx) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date dateTrx = null;
		try {
			
			dateTrx = sdf.parse(convertDateTrx);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dateTrx;
		
	}
}
