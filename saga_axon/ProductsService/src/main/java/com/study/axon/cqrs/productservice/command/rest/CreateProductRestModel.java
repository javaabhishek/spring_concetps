package com.study.axon.cqrs.productservice.command.rest;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateProductRestModel {

	private String title;
	private BigDecimal price;
	private Integer quantity;

}