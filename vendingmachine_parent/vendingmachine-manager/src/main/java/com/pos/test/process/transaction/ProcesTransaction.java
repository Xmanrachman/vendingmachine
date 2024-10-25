package com.pos.test.process.transaction;

import org.springframework.stereotype.Component;

import com.pos.test.model.dto.TransactionDTO;
import com.pos.test.model.dto.TransactionDTO.PaymentMetode;
import com.pos.test.model.master.Products;
import com.pos.test.process.ProductsResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProcesTransaction {

	public Products processValidationQty(Integer requestProd, Products product) {
		Integer remainsQty = product.getQuantityProduct() - requestProd;
		product.setQuantityProduct(remainsQty);
		return product;
	}

	public ProductsResponse processValidationPayment(TransactionDTO request) {
		boolean checkingPayment = true;
		ProductsResponse responseInquery = new ProductsResponse();

		if (request.getPaymentMetode().size() <= 0) {
			checkingPayment = false;
			responseInquery.setCheckingPayment(checkingPayment);
			return responseInquery;
		}
		int calLembarUang = 0;
		double calPayment = 0;
		double remains = 0;
		for (PaymentMetode loopPaymentMetode : request.getPaymentMetode()) {

			if (loopPaymentMetode.getMoneyPayment() == 2000 || loopPaymentMetode.getMoneyPayment() == 5000
					|| loopPaymentMetode.getMoneyPayment() == 10000 || loopPaymentMetode.getMoneyPayment() == 20000
					|| loopPaymentMetode.getMoneyPayment() == 50000) {

				checkingPayment = true;
				responseInquery.setCheckingPayment(checkingPayment);

			} else {
				checkingPayment = false;
				responseInquery.setCheckingPayment(checkingPayment);
				return responseInquery;
			}
			calLembarUang = loopPaymentMetode.getLembarUang() + calLembarUang;
			calPayment = loopPaymentMetode.getMoneyPayment() + calPayment;
		}

		if (request.getDataProducts().getNameProduct() == "Biskuit") {

			responseInquery = metodePayment(calLembarUang, calPayment, checkingPayment, remains, request);

		} else if (request.getDataProducts().getNameProduct() == "Chips") {

			responseInquery = metodePayment(calLembarUang, calPayment, checkingPayment, remains, request);

		} else if (request.getDataProducts().getNameProduct() == "Oreo") {

			responseInquery = metodePayment(calLembarUang, calPayment, checkingPayment, remains, request);

		} else if (request.getDataProducts().getNameProduct() == "Tango") {

			if (calLembarUang == 2) {
				if (calPayment == 12000) {
					checkingPayment = true;
				} else {
					checkingPayment = false;
					responseInquery.setCheckingPayment(checkingPayment);
					return responseInquery;
				}
			} else {
				responseInquery = metodePayment(calLembarUang, calPayment, checkingPayment, remains, request);
			}
		} else if (request.getDataProducts().getNameProduct() == "Cokelat") {

			if (calLembarUang == 2) {
				if (calPayment == 15000) {
					checkingPayment = true;
					remains = calPayment - request.getDataProducts().getPriceProduct();
					responseInquery.setCheckingPayment(checkingPayment);
					responseInquery.setChange(remains);
					responseInquery.setLembarUang(calLembarUang);

				} else {
					checkingPayment = false;
					responseInquery.setCheckingPayment(checkingPayment);
					return responseInquery;
				}
			} else {
				responseInquery = metodePayment(calLembarUang, calPayment, checkingPayment, remains, request);
			}
		}
		return responseInquery;
	}

	public ProductsResponse metodePayment(int calLembarUang, Double calPayment, boolean checkingPayment, double remains,
			TransactionDTO requestDto) {

		ProductsResponse response = new ProductsResponse();
		if (calLembarUang == 3) {

			if (calPayment == 6000) {
				checkingPayment = true;

			} else {
				checkingPayment = false;

				response.setChange(remains);
				response.setCheckingPayment(checkingPayment);
				return response;
			}
		} else if (calLembarUang == 1) {
			if (calPayment == 10000) {

				remains = calPayment - requestDto.getTotalPayment();
				log.info("Checking remains " + remains);
				checkingPayment = true;

			} else if (calPayment == 20000) {

				remains = calPayment - requestDto.getTotalPayment();
				checkingPayment = true;

			} else if (calPayment == 50000) {

				if (calPayment >= requestDto.getTotalPayment()) {

					remains = calPayment - requestDto.getTotalPayment();
					checkingPayment = true;
				}
			} else {
				checkingPayment = false;
				response.setChange(remains);
				response.setCheckingPayment(checkingPayment);
				return response;
			}
		}

		response.setLembarUang(calLembarUang);
		response.setChange(remains);
		response.setCheckingPayment(checkingPayment);

		return response;
	}

}
