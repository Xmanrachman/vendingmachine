package com.pos.test.process;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsResponse {
	
	private Boolean checkingPayment;
	private Double change;
	private Integer lembarUang;
	
	
}

