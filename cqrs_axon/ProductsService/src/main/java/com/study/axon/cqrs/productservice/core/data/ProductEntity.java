package com.study.axon.cqrs.productservice.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name="products")
@Data
public class ProductEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -227264951080660124L;
	
	@Id
	@Column(unique=true)
	private String productId;
	
	@Column(unique=true)
	private String title;
	private BigDecimal price;
	private Integer quantity;

}